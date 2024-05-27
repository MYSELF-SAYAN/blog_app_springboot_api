package blog.app.model;

import java.util.List;

public class PostCreationRequest {
    private String title;
    private String content;
    private String imageUrl;
    private String authorId;
    private List<String> tags;

    public PostCreationRequest(String title, String content, List<String> tags, String imageUrl, String authorId) {
        this.title = title;
        this.content = content;
        this.tags = tags;
        this.imageUrl = imageUrl;
        this.authorId = authorId;
    }

    public PostCreationRequest() {
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
