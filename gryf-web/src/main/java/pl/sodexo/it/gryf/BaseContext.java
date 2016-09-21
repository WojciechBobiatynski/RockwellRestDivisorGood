package pl.sodexo.it.gryf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableLoadTimeWeaving;
import org.springframework.instrument.classloading.LoadTimeWeaver;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.logging.Level;
import java.util.logging.Logger;


@Configuration
@ComponentScan
@EnableLoadTimeWeaving
@EnableTransactionManagement
@EnableScheduling
public class BaseContext {
    
    @PostConstruct
    public void init() {
        try {
            // brudny hak
            Class.forName("oracle.jdbc.OracleDriver");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(BaseContext.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Autowired
    LoadTimeWeaver loadTimeWeaver;
    
    @Bean
    public DataSource dataSource() {
      JndiDataSourceLookup dataSourceLookup = new JndiDataSourceLookup();
      DataSource dataSource = dataSourceLookup.getDataSource("jdbc/srvee");
      return dataSource;
    }    
    
    @Bean
    LocalValidatorFactoryBean lvfb() {
        LocalValidatorFactoryBean lvfb = new LocalValidatorFactoryBean();
        return lvfb;
    }    
    
    @Bean
    public JpaTransactionManager jtm() {
        JpaTransactionManager jtm = new JpaTransactionManager();
        jtm.setEntityManagerFactory(lcemfb().getNativeEntityManagerFactory());
        return jtm;
    }
    
    @Bean
    public LocalContainerEntityManagerFactoryBean lcemfb() {
        LocalContainerEntityManagerFactoryBean lcemfb = new LocalContainerEntityManagerFactoryBean();
        //lcemfb.setDataSource(dataSource);
        lcemfb.setLoadTimeWeaver(loadTimeWeaver);
        lcemfb.setPackagesToScan("pl.sodexo.it.gryf.model");
        lcemfb.setPersistenceUnitName("gryf_PU");
        lcemfb.afterPropertiesSet();
        return lcemfb;
    }
    
}
