package pos.proiect.AcademiaMongoAPI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.mongodb.MongoException;

@Component
public class DatabaseConnectionVerifier implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseConnectionVerifier.class);

    private final MongoTemplate mongoTemplate;

    @Autowired
    public DatabaseConnectionVerifier(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void run(String... args) {
        try {
            mongoTemplate.getDb().listCollectionNames();
            logger.info("Successfully connected to MongoDB.");


        } catch (MongoException e) {
            logger.error("Failed to connect to MongoDB. Reason: " + e.getMessage());

        }
    }
}