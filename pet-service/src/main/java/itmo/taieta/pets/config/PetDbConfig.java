package itmo.taieta.pets.config;

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
        basePackages = "itmo.taieta.pets.repositories",
        entityManagerFactoryRef = "petEntityManagerFactory",
        transactionManagerRef = "petTransactionManager"
)
public class PetDbConfig {

    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource.pets")
    public DataSource petDataSource() {
        return DataSourceBuilder.create()
                .driverClassName("org.postgresql.Driver")
                .build();
    }

    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean petEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("petDataSource") DataSource dataSource) {
        return builder
                .dataSource(dataSource)
                .packages("itmo.taieta.pets.models")
                .persistenceUnit("pets")
                .build();
    }

    @Bean
    @Primary
    public PlatformTransactionManager petTransactionManager(
            @Qualifier("petEntityManagerFactory") EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }
}
