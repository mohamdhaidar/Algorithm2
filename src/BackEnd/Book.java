package BackEnd;

public class Book {
    int bookNumber;
    int copiesNumber;
    int height;
    Book LChild;
    Book RChild;
    String author;

    public Book(int bookNumber, int copiesNumber, int height, String author) {
        this.bookNumber = bookNumber;
        this.copiesNumber = copiesNumber;
        this.height = height;
        author = author;
    }
}
