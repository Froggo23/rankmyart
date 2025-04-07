package ian.choe.rankmyart;

public class Artwork {
    private Integer id;
    private String title;
    private String category;
    private String description;
    private String imageUrl;
    private Integer upvotes;
    private Integer views;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public Integer getUpvotes() { return upvotes; }
    public void setUpvotes(Integer upvotes) { this.upvotes = upvotes; }
    public Integer getViews() { return views; }
    public void setViews(Integer views) { this.views = views; }
}
