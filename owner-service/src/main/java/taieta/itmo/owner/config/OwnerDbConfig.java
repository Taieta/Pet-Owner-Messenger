package taieta.itmo.owner.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories(
        basePackages = "taieta.itmo.owner.repositories",
        entityManagerFactoryRef = "ownerEntityManagerFactory",
        transactionManagerRef = "ownerTransactionManager"
)
public class OwnerDbConfig {

    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource.owners")
    public DataSource ownerDataSource() {
        return DataSourceBuilder.create()
                .driverClassName("org.postgresql.Driver")
                .build();
    }

    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean ownerEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("ownerDataSource") DataSource dataSource) {
        return builder
                .dataSource(dataSource)
                .packages("taieta.itmo.owner.models")
                .persistenceUnit("owners")
                .build();
    }

    @Bean
    @Primary
    public PlatformTransactionManager ownerTransactionManager(
            @Qualifier("ownerEntityManagerFactory") EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }
}
