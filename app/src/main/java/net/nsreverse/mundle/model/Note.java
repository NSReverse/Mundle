package net.nsreverse.mundle.model;

/**
 * Created by Robert on 7/21/2017.
 */

public class Note {
    private String authorId;
    private String author;
    private String content;
    private String title;

    public Note() {
        authorId = "";
        author = "";
        content = "";
        title = "";
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthorId() {
        return authorId;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public String getTitle() {
        return title;
    }
}
