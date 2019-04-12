package IntegrationTest;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.test.util.EnvironmentTestUtils;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.localstack.LocalStackContainer;


@Getter
@Setter
public class EnvInit implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    public static final String BUCKET_NAME = "integration-test-bucket";

    public static final MySQLContainer mySQLContainer = new MySQLContainer()
            .withDatabaseName("pii")
            .withUsername("test")
            .withPassword("test");

    public LocalStackContainer localstack = new LocalStackContainer()
            .withServices(LocalStackContainer.Service.S3)
            .withNetworkAliases("s3")
            .withEnv("DEFAULT_REGION", "us-east-1")
            .withEnv("USE_SSL", "false")
            .withEnv("HOSTNAME_EXTERNAL", "s3");

    static MySQLContainer database() {
        return mySQLContainer;
    }

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        EnvironmentTestUtils.addEnvironment(
                "test-containers",
                applicationContext.getEnvironment(),
                "spring.datasource.url=" + mySQLContainer.getJdbcUrl(),
                "spring.datasource.username=" + mySQLContainer.getUsername(),
                "spring.datasource.password=" + mySQLContainer.getPassword(),
                "spring.datasource.driver-class-name=" + mySQLContainer.getDriverClassName(),
                "spring.jpa.hibernate.ddl-auto = update",
                "spring.jpa.show-sql = true",
                "spring.jpa.properties.hibernate.dialect = org.hibernate.dialect.MySQL5InnoDBDialect",
                "spring.jpa.properties.hibernate.enable_lazy_load_no_trans=true"
        );
    }
}
