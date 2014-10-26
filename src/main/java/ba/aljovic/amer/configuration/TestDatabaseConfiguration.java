package ba.aljovic.amer.configuration;

import org.flywaydb.core.Flyway;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;

@EnableBatchProcessing
@Configuration
@Profile("test")
public class TestDatabaseConfiguration
{
    @Bean
    public DataSource dataSource()
    {
        EmbeddedDatabase db = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .setScriptEncoding("UTF-8")
                .build();
        return db;
    }

    @Bean(initMethod = "migrate")
    public Flyway flyway()
    {
        Flyway flyway = new Flyway();
        flyway.setDataSource(dataSource());
        flyway.setLocations("classpath:db.migration.test");
        return flyway;
    }
}
