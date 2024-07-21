package platform.domain;

public class Review {
    private String uid;
    private int rating;
    private String comment;
    private int author_id;
    private int receiver_id;

    public Review() {}

    public Review(String uid, int rating, String comment, int author_id, int receiver_id) {
        this.uid = uid;
        this.rating = rating;
        this.comment = comment;
        this.author_id = author_id;
        this.receiver_id = receiver_id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(int author_id) {
        this.author_id = author_id;
    }

    public int getReceiver_id() {
        return receiver_id;
    }

    public void setReceiver_id(int receiver_id) {
        this.receiver_id = receiver_id;
    }
}
