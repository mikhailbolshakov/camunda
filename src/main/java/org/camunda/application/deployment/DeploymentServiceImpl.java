package org.camunda.application.deployment;

import com.google.gson.Gson;
import org.camunda.api.deployment.dto.DeploymentItemRq;
import org.camunda.api.deployment.dto.DeploymentItemRs;
import org.camunda.api.deployment.dto.DeploymentRq;
import org.camunda.api.deployment.dto.DeploymentRs;
import org.camunda.api.deployment.service.DeploymentService;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.repository.DeploymentBuilder;
import org.camunda.bpm.engine.repository.DeploymentWithDefinitions;
import org.camunda.bpm.engine.spring.ProcessEngineFactoryBean;
import org.camunda.common.application.base.ApplicationServiceBaseImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.zip.ZipInputStream;

@Component
public class DeploymentServiceImpl extends ApplicationServiceBaseImpl implements DeploymentService {

    private final ProcessEngine processEngine;

    //@Value("${org.camunda.deployment.source-path:#{null}}")
    private String deploymentFolder;

    @Autowired
    public DeploymentServiceImpl(ProcessEngine processEngine) {
        this.processEngine = processEngine;
    }

    private void validate(DeploymentRq request) {

        try  {

            if(request == null || request.items == null || request.items.size() == 0)
                throw new IllegalArgumentException("Invalid request");

            File folderFile = new File(request.folder);

            if(!folderFile.exists())
                throw new IllegalArgumentException("Folder doesn't exist");

            if (!folderFile.isDirectory())
                throw new IllegalArgumentException("Folder is not a directory");
        }
        catch(Exception e){
            E("Deployment validation error: %s \n Request: %s", e.getMessage(), new Gson().toJson(request) );
            throw e;
        }
    }

    private void validateItem(DeploymentRq request, DeploymentItemRq itemRequest) {

        if(StringUtils.isEmpty(itemRequest.deploymentName))
            throw new IllegalArgumentException("Deployment name cannot be empty");

        if(StringUtils.isEmpty(itemRequest.fileName))
            throw new IllegalArgumentException("Deployment file name cannot be empty");

        String folder = request.folder;

        if(!folder.endsWith("/"))
            folder += "/";

        String fileName = folder + itemRequest.fileName;

        if(!(new File(fileName)).exists())
            throw new IllegalArgumentException(String.format("File not found: %s", fileName));

    }

    @Override
    public DeploymentRs deploy(DeploymentRq request) throws FileNotFoundException {

        request.folder = StringUtils.isEmpty(request.folder)
                ? deploymentFolder
                : request.folder;

        D("Deployment from folder %s", request.folder);

        validate(request);

        String folder = request.folder;

        if(!folder.endsWith("/"))
            folder += "/";

        DeploymentRs response = new DeploymentRs();

        for (DeploymentItemRq deployItemRq : request.items) {

            D("Deploying: %s\n Request: %s", deployItemRq.deploymentName, new Gson().toJson(deployItemRq));

            DeploymentItemRs deployItemRs = new DeploymentItemRs();
            response.items.add(deployItemRs);

            deployItemRs.deploymentName = deployItemRq.deploymentName;

            try {

                validateItem(request, deployItemRq);

                InputStream stream = new FileInputStream(new File(folder + deployItemRq.fileName));

                DeploymentBuilder deploymentBuilder = processEngine
                        .getRepositoryService()
                        .createDeployment()
                        .name(deployItemRq.deploymentName);

                if (!deployItemRq.fileName.endsWith(".bar") && !deployItemRq.fileName.endsWith(".zip") && !deployItemRq.fileName.endsWith(".jar")) {
                    deploymentBuilder.addInputStream(deployItemRq.fileName, stream);
                } else {
                    deploymentBuilder.addZipInputStream(new ZipInputStream(stream));
                }

                DeploymentWithDefinitions result = deploymentBuilder.deployWithResult();

                deployItemRs.deploymentId = result.getId();
                deployItemRs.deploymentTime = result.getDeploymentTime();

                D("Deployed: %s. Id: %s", deployItemRq.deploymentName, deployItemRs.deploymentId);

            }
            catch (Exception e) {
                deployItemRs.error = e.getMessage();
                E(e, "Deployment item error.\nItem: %s\nError: %s", new Gson().toJson(deployItemRq), e.getMessage());
            }

        }

        return response;
    }
}
