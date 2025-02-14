package com.ashutosh.employeetesting.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;

import jakarta.annotation.PostConstruct;

@Configuration
public class MongoDBConfig {

    private final MongoTemplate mongoTemplate;

    public MongoDBConfig(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @PostConstruct
    public void initIndexes() {
        mongoTemplate.indexOps("employees")
                .ensureIndex(new Index().on("email", org.springframework.data.domain.Sort.Direction.ASC).unique());
    }
}
