package com.ashutosh.employeetesting.service;

import com.mongodb.client.MongoClient;

import jakarta.annotation.PreDestroy;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class MongoDBConnectionService {

    @Autowired
    private MongoClient mongoClient;

    @PreDestroy
    public void closeMongoClient() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }
}
