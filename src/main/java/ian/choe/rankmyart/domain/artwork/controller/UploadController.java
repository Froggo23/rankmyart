package ian.choe.rankmyart.domain.artwork.controller;

import ian.choe.rankmyart.domain.artwork.service.ArtworkService;
import ian.choe.rankmyart.domain.artwork.service.S3FileService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.WebUtils;

import java.io.IOException;

@Controller
public class UploadController {

    private final S3FileService s3FileService;
    private final ArtworkService artworkService;

    @Autowired
    public UploadController(S3FileService s3FileService, ArtworkService artworkService) {
        this.s3FileService = s3FileService;
        this.artworkService = artworkService;
    }

    @GetMapping("/upload")
    public String showUploadPage(HttpServletRequest request) {
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
            @RequestParam("tags") String tags,
            HttpServletRequest request) { // Add HttpServletRequest here

        Cookie loginCookie = WebUtils.getCookie(request, "username");
        if (loginCookie == null) { 
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You must be logged in to upload.");
        }
        String username = loginCookie.getValue();

        if (imageFile.isEmpty()) {
            return ResponseEntity.badRequest().body("Select image to upload");
        }

        try {
            String imageUrl = s3FileService.uploadFile(imageFile);
            // Pass the username to the service method
            artworkService.saveArtwork(title, tags, description, imageUrl, username);

            System.out.println("Uploaded Image URL: " + imageUrl);
            System.out.println("Title: " + title);
            System.out.println("Description: " + description);
            System.out.println("Tags: " + tags);

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