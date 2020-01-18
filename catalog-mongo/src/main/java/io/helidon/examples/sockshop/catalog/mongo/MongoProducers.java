package io.helidon.examples.sockshop.catalog.mongo;

import java.util.Collections;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import com.mongodb.MongoClientSettings;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.Conventions;
import org.bson.codecs.pojo.PojoCodecProvider;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

/**
 */
@ApplicationScoped
public class MongoProducers {
    @Produces
    @ApplicationScoped
    public static MongoClient client() {
        CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
                                                         fromProviders(PojoCodecProvider.builder()
                                                                               .automatic(true)
                                                                               .conventions(Conventions.DEFAULT_CONVENTIONS)
                                                                               .build()));
        MongoClientSettings settings = MongoClientSettings.builder()
                .applicationName("catalog")
                .applyToClusterSettings(
                        builder -> builder.hosts(Collections.singletonList(new ServerAddress("catalog-db", 27017))))
                .codecRegistry(pojoCodecRegistry)
                .build();
        return MongoClients.create(settings);
    }
    
    @Produces
    @ApplicationScoped
    public static MongoDatabase db(MongoClient client) {
        return client.getDatabase("catalog");
    }

    @Produces
    @ApplicationScoped
    public static MongoCollection<MongoSock> socks(MongoDatabase db) {
        return db.getCollection("socks", MongoSock.class);
    }
}
