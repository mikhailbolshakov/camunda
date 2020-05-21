package org.camunda.bootstrapper;

import org.camunda.bpm.engine.ProcessEngineConfiguration;
import org.camunda.bpm.engine.spring.ProcessEngineFactoryBean;
import org.camunda.bpm.engine.spring.SpringProcessEngineConfiguration;
import org.camunda.bpm.engine.spring.SpringProcessEngineServicesConfiguration;
import org.camunda.spin.plugin.impl.SpinProcessEnginePlugin;
import org.camunda.wf.bpmn.plugin.ProcessEngineDeploymentPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Arrays;

@Configuration
@Import(SpringProcessEngineServicesConfiguration.class)
public class CamundaConfiguration {

    private final static Logger logger = LoggerFactory.getLogger(CamundaConfiguration.class.getName());

    @Value("${org.camunda.autodeployment-path:classpath:/processes/*.bpmn}")
    private String autoDeploymentPath;
    @Value("${org.camunda.autodeployment:false}")
    private Boolean autoDeployment;

    private final DataSource dataSource;
    private final ResourcePatternResolver resourceLoader;
    private final ProcessEngineDeploymentPlugin deploymentPlugin;

    @Autowired
    public CamundaConfiguration(DataSource dataSource,
                                ResourcePatternResolver resourceLoader,
                                ProcessEngineDeploymentPlugin deploymentPlugin) {
        this.dataSource = dataSource;
        this.resourceLoader = resourceLoader;
        this.deploymentPlugin = deploymentPlugin;
    }

    @Bean
    public SpringProcessEngineConfiguration processEngineConfiguration() throws IOException {

        logger.debug("[CamundaConfiguration] Process engine configuration");

        SpringProcessEngineConfiguration config = new SpringProcessEngineConfiguration();

        config.setDataSource(dataSource);
        config.setDatabaseSchemaUpdate("true");

        config.setTransactionManager(transactionManager());

        config.setHistory(ProcessEngineConfiguration.HISTORY_FULL);

        config.setJobExecutorActivate(true);
        config.setMetricsEnabled(false);

        config.setProcessEnginePlugins(Arrays.asList(new SpinProcessEnginePlugin(),
                                                     deploymentPlugin));

        if (autoDeployment)
            autoDeployment(config);

        return config;
    }

    private void autoDeployment(SpringProcessEngineConfiguration config) throws IOException {

        Resource[] resources = resourceLoader.getResources(autoDeploymentPath);

        logger.debug(String.format("[CamundaConfiguration] Autodeployment from path: %s \n", autoDeploymentPath));

        config.setDeploymentResources(resources);
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public ProcessEngineFactoryBean processEngine() throws IOException {
        ProcessEngineFactoryBean factoryBean = new ProcessEngineFactoryBean();
        factoryBean.setProcessEngineConfiguration(processEngineConfiguration());
        return factoryBean;
    }

}
