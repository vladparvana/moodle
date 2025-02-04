package pos.proiect.AcademiaMongoAPI.config;

import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import java.util.Collections;

@Configuration
public class MongoDBConfig {

    @Value("${spring.data.mongodb.username}")
    private String username;

    @Value("${spring.data.mongodb.password}")
    private String password;

    @Value("${spring.data.mongodb.host}")
    private String host;

    @Value("${spring.data.mongodb.port}")
    private int port;
    @Value("${spring.data.mongodb.database}")
    private String database;

    @Bean
    public MongoClient mongoClient() {
        MongoCredential credential = MongoCredential.createCredential(username, "admin", password.toCharArray());
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyToClusterSettings(builder -> builder.hosts(Collections.singletonList(new ServerAddress(host, port))))
                .credential(credential)
                .build();
        return MongoClients.create(settings);
    }


    @Bean
    public MongoTemplate mongoTemplate() {
        MongoMappingContext mappingContext = new MongoMappingContext();
        mappingContext.setAutoIndexCreation(true);
        MappingMongoConverter converter = new MappingMongoConverter(
                org.springframework.data.mongodb.core.convert.NoOpDbRefResolver.INSTANCE,
                mappingContext);
        converter.setTypeMapper(new DefaultMongoTypeMapper(null));
        SimpleMongoClientDatabaseFactory mongoDbFactory = new SimpleMongoClientDatabaseFactory(mongoClient(), database);
        return new MongoTemplate(mongoDbFactory, converter);
    }
}