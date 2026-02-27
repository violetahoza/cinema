package models;

public class Review {
    private int reviewId;
    private int movieId;
    private int userId;
    private int rating;
    private String reviewText;

    public Review(int reviewId, int movieId, int userId, int rating, String reviewText) {
        this.reviewId = reviewId;
        this.movieId = movieId;
        this.userId = userId;
        this.rating = rating;
        this.reviewText = reviewText;
    }
    public int getRating() {
        return rating;
    }

    public String getReviewText() {
        return reviewText;
    }
}
