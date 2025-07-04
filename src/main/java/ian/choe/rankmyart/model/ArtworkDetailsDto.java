package ian.choe.rankmyart.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

// Data Transfer Object (DTO).
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArtworkDetailsDto {
    private Artwork artwork;
    private List<Comment> comments;
}