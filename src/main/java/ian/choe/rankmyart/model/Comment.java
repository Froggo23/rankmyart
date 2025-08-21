package ian.choe.rankmyart.model;

import lombok.Data;

import java.util.Date;

@Data
public class Comment {
    int id;
    int artworkId;
    String content;
    String author;
    Date createdAt;
    boolean isEdited;
    private String authorProfileImg;
}
