package ian.choe.rankmyart.model;

import lombok.Data;

import java.util.Date;

@Data
public class Comment {
    private int id;
    private int artworkId;
    private String content;
    private String author;
    private Date createdAt;
    private boolean isEdited;
}
