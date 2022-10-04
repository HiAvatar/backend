package com.fastcampus.finalproject.aws;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.fastcampus.finalproject.config.YmlFlaskConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.File;

@Slf4j
@RequiredArgsConstructor
@Component
public class S3Uploader {

    private final AmazonS3Client amazonS3Client;
    private final YmlFlaskConfig flaskConfig;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String uploadFile(File uploadFile, String userPath) {
        String bucketPath = userPath + "/" + uploadFile.getName();
        return putS3(uploadFile, bucketPath);
    }

    public void removeFile(String projectPath, String fileName) {
        amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, projectPath + "/" + fileName));
        removeLocalFile(new File(flaskConfig.getFilePath() + "/" + fileName));
    }

    private String putS3(File uploadFile, String bucketPath) {
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, bucketPath, uploadFile).withCannedAcl(CannedAccessControlList.PublicRead);
        amazonS3Client.putObject(putObjectRequest);

        return amazonS3Client.getUrl(bucket, bucketPath).toString();
    }

    public void removeLocalFile(File targetFile) {
        if(targetFile.delete()) {
            log.info(targetFile.getName() + " 파일이 삭제됨");
        } else {
            log.info(targetFile.getName() + " 파일이 삭제되지 않음");
        }
    }
}