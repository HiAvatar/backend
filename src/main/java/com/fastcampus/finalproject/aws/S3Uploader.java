package com.fastcampus.finalproject.aws;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.fastcampus.finalproject.config.YmlFlaskConfig;
import com.fastcampus.finalproject.enums.FileType;
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

    public String uploadFile(File uploadFile, FileType fileType, String parentPath) {
        String bucketPath = parentPath + "/" + fileType.getValue() + "/" + uploadFile.getName();
        return putS3(uploadFile, bucketPath);
    }

    public void removeFile(String parentPath, FileType fileType, String fileName, String fileExtension) {
        amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, parentPath + "/" + fileType.getValue() + "/" + fileName + fileExtension));

        File file = new File(flaskConfig.getFilePath() + "/" + fileName);
        if(file.exists()) {
            removeLocalFile(file);
        }
    }

    /*
     * 회원 탈퇴 기능이 추가되면 영상 디렉토리 삭제하는 데 사용
     */
    public void removeDirectory(String parentPath) {
        DeleteObjectsResult deleteObjectsResult = amazonS3Client.deleteObjects(new DeleteObjectsRequest(bucket)
                .withKeys(parentPath));
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