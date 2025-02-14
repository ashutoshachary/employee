//package com.ashutosh.employeetesting.service;
//
//import com.amazonaws.services.s3.AmazonS3;
//import com.amazonaws.services.s3.model.PutObjectRequest;
//import com.amazonaws.services.s3.model.DeleteObjectRequest;
//import com.amazonaws.services.s3.model.ListObjectsV2Request;
//import com.amazonaws.services.s3.model.ListObjectsV2Result;
//import com.amazonaws.services.s3.model.S3ObjectSummary;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.data.mongodb.core.MongoTemplate;
//import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Service;
//
//import java.io.File;
//import java.io.FileWriter;
//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
//import java.util.List;
//
//@Service
//@EnableScheduling
//public class BackupService {
//
//    @Autowired
//    private MongoTemplate mongoTemplate;
//
//    @Autowired
//    private AmazonS3 amazonS3Client;
//
//    @Value("${aws.s3.bucket}")
//    private String bucketName;
//
//    private static final String[] COLLECTIONS = {"employees", "todos"};
//    
//    @Scheduled(cron = "0 0 0 * * *")
//    public void performDailyBackup() {
//        String currentDate = LocalDate.now().format(DateTimeFormatter.ISO_DATE);
//        String directoryPath = "backups/" + currentDate + "/";
//        
//        // Create temporary files with backup data
//        try {
//            for (String collection : COLLECTIONS) {
//                // Get all documents from collection
//                List<?> documents = mongoTemplate.findAll(List.class, collection);
//                
//                // Create temporary file
//                File tempFile = File.createTempFile(collection + "-backup", ".json");
//                try (FileWriter writer = new FileWriter(tempFile)) {
//                    // Write documents to file (you might want to use a proper JSON writer here)
//                    writer.write(documents.toString());
//                }
//                
//                // Upload to S3
//                String s3Key = directoryPath + collection + ".json";
//                amazonS3Client.putObject(new PutObjectRequest(bucketName, s3Key, tempFile));
//                
//                // Clean up temp file
//                tempFile.delete();
//            }
//            
//            // Delete previous day's backup
//            deletePreviousBackup(currentDate);
//            
//        } catch (Exception e) {
//            // Add proper error handling and logging here
//            throw new RuntimeException("Failed to perform backup", e);
//        }
//    }
//    
//    private void deletePreviousBackup(String currentDate) {
//        ListObjectsV2Request listRequest = new ListObjectsV2Request()
//                .withBucketName(bucketName)
//                .withPrefix("backups/");
//                
//        ListObjectsV2Result listResult = amazonS3Client.listObjectsV2(listRequest);
//        
//        for (S3ObjectSummary objectSummary : listResult.getObjectSummaries()) {
//            String key = objectSummary.getKey();
//            if (!key.startsWith("backups/" + currentDate)) {
//                amazonS3Client.deleteObject(new DeleteObjectRequest(bucketName, key));
//            }
//        }
//    }
//}

//package com.ashutosh.employeetesting.service;
//
//import com.amazonaws.services.s3.AmazonS3;
//import com.amazonaws.services.s3.model.PutObjectRequest;
//import com.amazonaws.services.s3.model.DeleteObjectRequest;
//import com.amazonaws.services.s3.model.ListObjectsV2Request;
//import com.amazonaws.services.s3.model.ListObjectsV2Result;
//import com.amazonaws.services.s3.model.S3ObjectSummary;
//
//import jakarta.annotation.PostConstruct;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.data.mongodb.core.MongoTemplate;
//import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Service;
//import java.io.File;
//import java.io.FileWriter;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.List;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//@Service
//@EnableScheduling
//public class BackupService {
//
//    private static final Logger logger = LoggerFactory.getLogger(BackupService.class);
//
//    @Autowired
//    private MongoTemplate mongoTemplate;
//
//    @Autowired
//    private AmazonS3 amazonS3Client;
//
//    @Value("${aws.s3.bucket}")
//    private String bucketName;
//
//    private static final String[] COLLECTIONS = {"employees", "todos"};
//    
//    // This will run immediately when the service starts
//    @PostConstruct
//    public void init() {
//        logger.info("Initiating immediate backup...");
//        performBackup();
//    }
//    
//    // This will run every 24 hours after the initial backup
//    @Scheduled(fixedDelay = 24 * 60 * 60 * 1000) // 24 hours in milliseconds
//    public void scheduledBackup() {
//        performBackup();
//    }
//    
//    private void performBackup() {
//        String currentTimestamp = LocalDateTime.now().format(
//            DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss")
//        );
//        String directoryPath = "backups/" + currentTimestamp + "/";
//        
//        logger.info("Starting backup for timestamp: {}", currentTimestamp);
//        
//        try {
//            for (String collection : COLLECTIONS) {
//                // Get all documents from collection
//                List<?> documents = mongoTemplate.findAll(List.class, collection);
//                
//                // Create temporary file
//                File tempFile = File.createTempFile(collection + "-backup", ".json");
//                try (FileWriter writer = new FileWriter(tempFile)) {
//                    // Write documents to file
//                    writer.write(documents.toString());
//                }
//                
//                // Upload to S3
//                String s3Key = directoryPath + collection + ".json";
//                amazonS3Client.putObject(new PutObjectRequest(bucketName, s3Key, tempFile));
//                
//                logger.info("Backed up collection: {} to S3 path: {}", collection, s3Key);
//                
//                // Clean up temp file
//                tempFile.delete();
//            }
//            
//            // Delete previous backups
//            deletePreviousBackups(currentTimestamp);
//            
//            logger.info("Backup completed successfully for timestamp: {}", currentTimestamp);
//            
//        } catch (Exception e) {
//            logger.error("Failed to perform backup", e);
//            throw new RuntimeException("Failed to perform backup", e);
//        }
//    }
//    
//    private void deletePreviousBackups(String currentTimestamp) {
//        ListObjectsV2Request listRequest = new ListObjectsV2Request()
//                .withBucketName(bucketName)
//                .withPrefix("backups/");
//                
//        ListObjectsV2Result listResult = amazonS3Client.listObjectsV2(listRequest);
//        
//        for (S3ObjectSummary objectSummary : listResult.getObjectSummaries()) {
//            String key = objectSummary.getKey();
//            if (!key.contains(currentTimestamp)) {
//                amazonS3Client.deleteObject(new DeleteObjectRequest(bucketName, key));
//                logger.info("Deleted previous backup: {}", key);
//            }
//        }
//    }
//}


//package com.ashutosh.employeetesting.service;
//
//import com.amazonaws.services.s3.AmazonS3;
//import com.amazonaws.services.s3.model.PutObjectRequest;
//import com.amazonaws.services.s3.model.DeleteObjectRequest;
//import com.amazonaws.services.s3.model.ListObjectsV2Request;
//import com.amazonaws.services.s3.model.ListObjectsV2Result;
//import com.amazonaws.services.s3.model.S3ObjectSummary;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Service;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.data.mongodb.core.MongoTemplate;
//import org.springframework.scheduling.annotation.EnableScheduling;
//
//import jakarta.annotation.PostConstruct;
//import java.io.File;
//import java.io.FileWriter;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.List;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//@Service
//@EnableScheduling
//public class BackupService {
//
//    private static final Logger logger = LoggerFactory.getLogger(BackupService.class);
//
//    @Autowired
//    private MongoTemplate mongoTemplate;
//
//    @Autowired
//    private AmazonS3 amazonS3Client;
//
//    @Value("${aws.s3.bucket}")
//    private String bucketName;
//
//    private static final String[] COLLECTIONS = {"employees", "todos"}; // Add your MongoDB collections
//
//    // Runs immediately when the service starts
//    @PostConstruct
//    public void init() {
//        logger.info("Initiating immediate backup...");
//        performBackup();
//    }
//
//    // Runs every 24 hours
//    @Scheduled(fixedDelay = 24 * 60 * 60 * 1000) // 24 hours in milliseconds
//    public void scheduledBackup() {
//        performBackup();
//    }
//
//    private void performBackup() {
//        String currentTimestamp = LocalDateTime.now().format(
//            DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss")
//        );
//        String directoryPath = "backups/" + currentTimestamp + "/";
//
//        logger.info("Starting backup for timestamp: {}", currentTimestamp);
//
//        try {
//            for (String collection : COLLECTIONS) {
//                List<?> documents = mongoTemplate.findAll(List.class, collection);
//
//                // Create a temporary file
//                File tempFile = File.createTempFile(collection + "-backup", ".json");
//                try (FileWriter writer = new FileWriter(tempFile)) {
//                    writer.write(documents.toString());
//                }
//
//                // Upload to S3
//                String s3Key = directoryPath + collection + ".json";
//                amazonS3Client.putObject(new PutObjectRequest(bucketName, s3Key, tempFile));
//
//                logger.info("Backed up collection: {} to S3 path: {}", collection, s3Key);
//
//                // Delete temporary file
//                tempFile.delete();
//            }
//
//            // Delete old backups
//            deletePreviousBackups(currentTimestamp);
//
//            logger.info("Backup completed successfully for timestamp: {}", currentTimestamp);
//
//        } catch (Exception e) {
//            logger.error("Failed to perform backup", e);
//        }
//    }
//
//    private void deletePreviousBackups(String currentTimestamp) {
//        ListObjectsV2Request listRequest = new ListObjectsV2Request()
//                .withBucketName(bucketName)
//                .withPrefix("backups/");
//
//        ListObjectsV2Result listResult = amazonS3Client.listObjectsV2(listRequest);
//
//        for (S3ObjectSummary objectSummary : listResult.getObjectSummaries()) {
//            String key = objectSummary.getKey();
//            if (!key.contains(currentTimestamp)) {
//                amazonS3Client.deleteObject(new DeleteObjectRequest(bucketName, key));
//                logger.info("Deleted old backup: {}", key);
//            }
//        }
//    }
//}
package com.ashutosh.employeetesting.service;

import com.ashutosh.employeetesting.model.Employee;
import com.ashutosh.employeetesting.repository.EmployeeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class BackupService {
    private static final Logger logger = LoggerFactory.getLogger(BackupService.class);
    private final EmployeeRepository employeeRepository;
    private final ObjectMapper objectMapper;
    private final S3Client s3Client;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    public BackupService(EmployeeRepository employeeRepository, S3Client s3Client) {
        this.employeeRepository = employeeRepository;
        this.s3Client = s3Client;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @Scheduled(cron = "0 0 0 * * *") // Runs every day 
    public void performBackup() {
        try {
            String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
            List<Employee> employees = employeeRepository.findAll();

            if (employees == null || employees.isEmpty()) {
                logger.warn("No data to back up.");
                return;
            }

            // Convert employees to JSON
            byte[] backupData = objectMapper.writeValueAsBytes(employees);
            String key = "backups/employee_backup_" + timestamp + ".json";

            // Upload to S3
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(backupData));
            logger.info("Backup successful: s3://{}/{}", bucketName, key);

            // Cleanup old backups
            cleanupOldBackups();

        } catch (Exception e) {
            logger.error("Failed to perform backup: {}", e.getMessage(), e);
        }
    }

    private void cleanupOldBackups() {
        try {
            // List all backups
            ListObjectsV2Request listRequest = ListObjectsV2Request.builder()
                    .bucket(bucketName)
                    .prefix("backups/employee_backup_")
                    .build();

            ListObjectsV2Response listResponse = s3Client.listObjectsV2(listRequest);
            List<S3Object> backupFiles = listResponse.contents();

            if (backupFiles.size() <= 10) {
                return;
            }

            // Sort by last modified time (newest first)
            backupFiles.sort((o1, o2) -> o2.lastModified().compareTo(o1.lastModified()));

            // Delete older backups (keep only the latest 10)
            for (int i = 10; i < backupFiles.size(); i++) {
                DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                        .bucket(bucketName)
                        .key(backupFiles.get(i).key())
                        .build();

                s3Client.deleteObject(deleteRequest);
                logger.info("Deleted old backup: {}", backupFiles.get(i).key());
            }
        } catch (Exception e) {
            logger.error("Failed to cleanup old backups: {}", e.getMessage(), e);
        }
    }
}