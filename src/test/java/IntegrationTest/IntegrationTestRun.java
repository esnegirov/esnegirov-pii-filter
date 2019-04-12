package IntegrationTest;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.slice.Application;
import com.slice.dao.RegexpTableRepository;
import com.slice.models.Item;
import com.slice.models.RegexpTable;
import com.slice.service.ItemMapper;
import com.slice.service.ReplaceJob;
import com.slice.service.S3Utils;
import org.junit.*;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;


@RunWith(SpringRunner.class)
@ContextConfiguration(classes = Application.class, initializers = { EnvInit.class })
@ActiveProfiles({"unit-test"})
public class IntegrationTestRun {

    private final static Logger LOGGER = LoggerFactory.getLogger(IntegrationTestRun.class);

    @Autowired
    ReplaceJob replaceJob;

    @Autowired
    @Qualifier("regexpRepository")
    private RegexpTableRepository regexpTableRepository;

    @Autowired
    private S3Utils s3Utils;

    @Autowired
    ItemMapper itemMapper;

    private EnvInit envInit = new EnvInit();

    @ClassRule
    public static MySQLContainer MY_SQL_CONTAINER = EnvInit.database();


    private ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void startDb() throws SQLException {
        MY_SQL_CONTAINER.start();
        envInit.getLocalstack().start();
        fillRegExpDB();
        createS3Bucket();
        fillS3Bucket();

    }

    @Test
    public void startStopTest(){
        replaceJob.startJob(envInit.BUCKET_NAME, s3Client());
    }

    @After
    public void stopDb(){
        MY_SQL_CONTAINER.stop();
        envInit.getLocalstack().stop();

    }

    private void fillRegExpDB() throws SQLException {
        LOGGER.info("fillRegExpDB");

        regexpTableRepository.save(new RegexpTable(1, "\\d+", "*number*"));
        regexpTableRepository.save(new RegexpTable(2, "[A-Z]", "*Capital letter*"));
    }

    private AmazonS3 s3Client() {
        AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(envInit.getLocalstack().getEndpointConfiguration(LocalStackContainer.Service.S3))
                .withCredentials(envInit.getLocalstack().getDefaultCredentialsProvider()).build();
        return s3Client;
    }

    private void fillS3Bucket(){
        LOGGER.info("fillS3Bucket");
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("static/data.json").getFile());
        try {
            Item[] item = objectMapper.readValue(file, Item[].class);
            Arrays.asList(item).forEach(obj ->
                    {
                        try {
                            s3Utils.writeToS3(envInit.BUCKET_NAME,obj.getId()+".json", itemMapper.itemToJson(obj) , s3Client());
                        } catch (JsonProcessingException e) {
                            e.printStackTrace();
                        }
                    }
            );
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void createS3Bucket(){
        s3Client().createBucket(envInit.BUCKET_NAME);

    }
}
