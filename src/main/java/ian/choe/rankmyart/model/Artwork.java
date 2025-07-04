package ian.choe.rankmyart.model;

import lombok.Data;

@Data
public class Artwork {
    private Integer id;
    private String title;
    private String category;
    private String description;
    private String imageUrl;
    private Integer upvotes;
    private Integer views;
}