package org.camunda.bootstrapper;

import org.camunda.bpm.engine.ProcessEngineConfiguration;
import org.camunda.bpm.engine.spring.ProcessEngineFactoryBean;
import org.camunda.bpm.engine.spring.SpringProcessEngineConfiguration;
import org.camunda.bpm.engine.spring.SpringProcessEngineServicesConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
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

    // configure data source via application.properties
    @Autowired
    private DataSource dataSource;

    @Autowired
    private ResourcePatternResolver resourceLoader;

    @Bean
    public SpringProcessEngineConfiguration processEngineConfiguration() throws IOException {
        SpringProcessEngineConfiguration config = new SpringProcessEngineConfiguration();

        config.setDataSource(dataSource);
        config.setDatabaseSchemaUpdate("true");

        //config.setAuthorizationEnabled(false);

        config.setTransactionManager(transactionManager());

        config.setHistory(ProcessEngineConfiguration.HISTORY_FULL);

        config.setJobExecutorActivate(true);
        config.setMetricsEnabled(false);

        // deploy all processes from folder 'processes'
        Resource[] resources = resourceLoader.getResources("classpath:/processes/*.bpmn");
        config.setDeploymentResources(resources);

        return config;
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
