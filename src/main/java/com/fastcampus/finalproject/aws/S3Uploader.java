package com.fastcampus.finalproject.aws;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.fastcampus.finalproject.config.YmlFlaskConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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

    // 멀티 파트 -> File 변환해서 업로드
//    public String uploadMultipartFile(MultipartFile multipartFile) {
//
//    }

    public String uploadFile(File uploadFile, String userPath, String extension) {
        String bucketPath = userPath + "/" + uploadFile.getName();
        String uploadUrl = putS3(uploadFile, bucketPath);

        if(extension.equals(flaskConfig.getVideoExtension())) {
            //removeFile(new File(flaskConfig.getFilePath()));
            removeLocalFile(uploadFile, "영상");
        }
        return uploadUrl;
    }

    public void removeFile() {

    }

    private String putS3(File uploadFile, String bucketPath) {
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, bucketPath, uploadFile).withCannedAcl(CannedAccessControlList.PublicRead);
        amazonS3Client.putObject(putObjectRequest);

        return amazonS3Client.getUrl(bucket, bucketPath).toString();
    }

    private void removeLocalFile(File targetFile, String fileType) {
        if(targetFile.delete()) {
            log.info(fileType + " 파일이 삭제됨");
        } else {
            log.info(fileType + " 파일이 삭제되지 않음");
        }
    }
}