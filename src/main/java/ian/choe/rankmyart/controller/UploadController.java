package ian.choe.rankmyart.controller;

import ian.choe.rankmyart.service.S3FileService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.WebUtils;

import java.io.IOException;

@Controller
public class UploadController {

    @Autowired
    private S3FileService s3FileService;

    @Autowired // Autowire JdbcTemplate
    private JdbcTemplate jdbcTemplate;


    @GetMapping("/upload")
    public String showUploadPage(HttpServletRequest request) {
        // cookies
         Cookie loginCookie = WebUtils.getCookie(request, "username");
         if (loginCookie == null) {
             return "redirect:/signup";
         }
        

        return "upload";
    }


    @PostMapping("/upload")
    public ResponseEntity<String> handleFileUpload(
            @RequestParam("imageFile") MultipartFile imageFile,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("tags") String tags) {

        if (imageFile.isEmpty()) {
            return ResponseEntity.badRequest().body("Select image to upload");
        }

        try {
            // 1. S3 file upload
            String imageUrl = s3FileService.uploadFile(imageFile);

            String sql = "INSERT INTO artworks (title, category, description, image_url) VALUES (?, ?, ?, ?)";
            jdbcTemplate.update(sql, title, tags, description, imageUrl);


            System.out.println("Uploaded Image URL: " + imageUrl);
            System.out.println("Title: " + title);
            System.out.println("Description: " + description);
            System.out.println("Tags: " + tags);

            // alert
            return ResponseEntity.status(HttpStatus.FOUND).header("Location", "/gallery").build();

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred during file upload" + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred during processing:" + e.getMessage());
        }
    }
}