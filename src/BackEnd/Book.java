package BackEnd;

public class Book {
    int bookNumber;
    int copiesNumber;
    Book LChild;
    Book RChild;

    public Book(int bookNumber, int copiesNumber) {
        this.bookNumber = bookNumber;
        this.copiesNumber = copiesNumber;
    }
}
