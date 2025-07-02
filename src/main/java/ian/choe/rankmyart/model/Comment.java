package ian.choe.rankmyart.model;

import lombok.Data;

import java.util.Date;

@Data
public class Comment {
    int id;
    int postId;

    String content;
    String author;
    Date commentDate;

    boolean isEdited;
}
