package ba.aljovic.amer.configuration.test;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

@Configuration
@Profile("test")
public class TestDatabaseConfiguration
{
   @Autowired
   private DataSource dataSource;

    @Bean(initMethod = "migrate")
    public Flyway flyway()
    {
        Flyway flyway = new Flyway();
        flyway.setDataSource(dataSource);
        flyway.setLocations("classpath:db.migration.test");
        return flyway;
    }
}
