package ian.choe.rankmyart.domain.google.model;

import lombok.Data;

@Data
public class GoogleUser {
    private String sub;
    private String name;
    private String given_name;
    private String family_name;
    private String picture;
    private String email;
    private boolean email_verified;
    private String locale;
}
