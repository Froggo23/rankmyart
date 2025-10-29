package ian.choe.rankmyart.domain.google.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import ian.choe.rankmyart.domain.user.repository.UserRepository;
import ian.choe.rankmyart.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.UUID;

@Service
public class GoogleService {

    private final GoogleIdTokenVerifier verifier;
    private final UserRepository userRepository;

    public GoogleService(@Value("${google.client.id}") String clientId, UserRepository userRepository) throws GeneralSecurityException, IOException {
        this.userRepository = userRepository;
        this.verifier = new GoogleIdTokenVerifier.Builder(
                GoogleNetHttpTransport.newTrustedTransport(), GsonFactory.getDefaultInstance())
                .setAudience(Collections.singletonList(clientId))
                .build();
    }

    public User loginOrRegisterWithGoogle(String credential) throws GeneralSecurityException, IOException {
        GoogleIdToken idToken = verifier.verify(credential);
        if (idToken != null) {
            GoogleIdToken.Payload payload = idToken.getPayload();
            String email = payload.getEmail();

            User user = userRepository.findByEmail(email);
            if (user == null) {
                // 신규 가입
                String randomName = "User_" + UUID.randomUUID().toString().replace("-", "").substring(0, 8);
                String pictureUrl = (String) payload.get("picture");

                user = new User();
                user.setUsername(randomName);
                user.setEmail(email);
                user.setProfileImg(pictureUrl);
                userRepository.save(user);
            }

            // 로그인 처리(가입 또는 기존 유저)
            return user;
        }
        return null;
    }
}

