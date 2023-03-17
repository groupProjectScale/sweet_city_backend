package com.example.services;

import com.example.configurations.S3Configuration;
import com.example.dto.ImageDto;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

@Service
public class S3Service {
    private final String BUCKET;
    private S3Client s3Client;
    private final String endpointUrl;

    public S3Service(S3Client s3Client, S3Configuration s3Configuration) {
        this.s3Client = s3Client;
        this.BUCKET = s3Configuration.Bucket();
        this.endpointUrl = s3Configuration.getEndpointUrl();
    }

    public ImageDto uploadFile(String activityId, String path, String fileName) {
        try {
            String key = "/" + activityId + "/" + fileName;
            PutObjectRequest request = PutObjectRequest.builder().bucket(BUCKET).key(key).build();

            s3Client.putObject(request, RequestBody.fromBytes(getObjectFile(path)));
            return new ImageDto(fileName, endpointUrl + "/" + BUCKET + key, activityId + fileName);
        } catch (S3Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        return null;
    }

    private static byte[] getObjectFile(String filePath) {

        FileInputStream fileInputStream = null;
        byte[] bytesArray = null;

        try {
            File file = new File(filePath);
            bytesArray = new byte[(int) file.length()];
            fileInputStream = new FileInputStream(file);
            fileInputStream.read(bytesArray);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return bytesArray;
    }

    public void downloadFile(String activityId, String fileName) {
        try {
            String path = "/" + activityId + "/" + fileName;
            GetObjectRequest req = GetObjectRequest.builder().bucket(BUCKET).key(path).build();
            ResponseInputStream<GetObjectResponse> response = s3Client.getObject(req);
            BufferedOutputStream outputStream =
                    new BufferedOutputStream(new FileOutputStream(fileName));

            byte[] buffer = new byte[4096];
            int bytesRead = -1;

            while ((bytesRead = response.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            response.close();
            outputStream.close();
        } catch (AwsServiceException e) {
            e.printStackTrace();
        } catch (SdkClientException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
