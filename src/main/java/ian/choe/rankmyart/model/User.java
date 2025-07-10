package ian.choe.rankmyart.model;

public class User {
    private int id;
    private String username;
    private String email;
    private String password; // Stores the plain text password
    private String profileImg; // New field for profile image URL
    private String bio;        // New field for user bio

    // Constructors
    public User() {}

    public User(int id, String username, String email, String password, String profileImg, String bio) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.profileImg = profileImg; // Initialize new field
        this.bio = bio;              // Initialize new field
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // New getters and setters for profileImg and bio
    public String getProfileImg() {
        return profileImg;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
}
