package ian.choe.rankmyart.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class S3FileService {

    @Autowired
    private AmazonS3 s3Client;

    // S3 bucket name from application.properties or application.yml
    @Value("${aws.s3.bucketName}")
    private String bucketName;

    public String uploadFile(MultipartFile file) throws IOException {
        // Set unique file name
        String originalFileName = file.getOriginalFilename();
        String fileExtension = "";
        if (originalFileName != null && originalFileName.contains(".")) {
            fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        }
        String fileName = UUID.randomUUID().toString() + fileExtension;

        // Set file metadata
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        // Upload file to s3
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fileName, file.getInputStream(), metadata);

        s3Client.putObject(putObjectRequest);

        // Return the S3 URL of the uploaded file
        return s3Client.getUrl(bucketName, fileName).toString();
    }
    // 아직안씀, 곧 사진 내리는것도 해야됨
    public void deleteFile(String fileUrl) {
        if (fileUrl == null || fileUrl.isEmpty()) {
            return;
        }
        try {
            // Extract file key (name) from the URL
            String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
            s3Client.deleteObject(new DeleteObjectRequest(bucketName, fileName));
        } catch (Exception e) {
            System.err.println("Error deleting image: " + e.getMessage());
        }
    }
}