package org.camunda.bootstrapper;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import org.camunda.bpm.engine.ProcessEngineConfiguration;
import org.camunda.bpm.engine.spring.ProcessEngineFactoryBean;
import org.camunda.bpm.engine.spring.SpringProcessEngineConfiguration;
import org.camunda.bpm.engine.spring.SpringProcessEngineServicesConfiguration;
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

@Configuration
@Import(SpringProcessEngineServicesConfiguration.class)
public class CamundaConfiguration {

    private final static Logger logger = LoggerFactory.getLogger(CamundaConfiguration.class.getName());

    @Autowired
    private DataSource dataSource;

    @Value("${org.camunda.autodeployment-path}")
    private String autoDeploymentPath;

    @Autowired
    private ResourcePatternResolver resourceLoader;

    @Bean
    public SpringProcessEngineConfiguration processEngineConfiguration() throws IOException {
        SpringProcessEngineConfiguration config = new SpringProcessEngineConfiguration();

        config.setDataSource(dataSource);
        config.setDatabaseSchemaUpdate("true");

        config.setTransactionManager(transactionManager());

        config.setHistory(ProcessEngineConfiguration.HISTORY_FULL);

        config.setJobExecutorActivate(true);
        config.setMetricsEnabled(false);

        logger.debug(String.format("[CamundaConfiguration] Process engine configuration"));

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
