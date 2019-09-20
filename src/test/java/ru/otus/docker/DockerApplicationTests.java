package ru.otus.docker;

import com.mongodb.MongoClient;
import org.bson.Document;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.GenericContainer;

import java.util.HashMap;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DockerApplicationTests {
    private MongoTemplate mongoTemplate;
    @Rule
    public GenericContainer mongo = new GenericContainer("mongo").withExposedPorts(27017);
    
    @Before()
    public void before() throws Exception {
        mongoTemplate = new MongoTemplate(new MongoClient(mongo.getContainerIpAddress(), mongo.getFirstMappedPort()), "test");
    }
    
    @Test
    public void contextLoads() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", "Steve");
        mongoTemplate.save(map, "someCollection");
        final List<Document> result = mongoTemplate.findAll(Document.class, "someCollection");
        Assert.assertEquals("Result size is 1", 1, result.size());
        Assert.assertEquals("Name is the same", result.get(0).get("name"), "Steve");
    }
}
