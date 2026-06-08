package BackEnd;

public class BorrowRecord {
    int recordId;
    int bookNumber;
    String borrowerName;
    String borrowDate;
    String expectedReturnDate;
    boolean returned;
    BorrowRecord next;

    public BorrowRecord(int recordId, int bookNumber, String borrowerName, String borrowDate, String expectedReturnDate) {
        this.recordId = recordId;
        this.bookNumber = bookNumber;
        this.borrowerName = borrowerName;
        this.borrowDate = borrowDate;
        this.expectedReturnDate = expectedReturnDate;
        this.returned = false;
        this.next = null;
    }

    @Override
    public String toString() {
        return "Record ID: " + recordId +
                ", Book Number: " + bookNumber +
                ", Borrower Name: " + borrowerName +
                ", Borrow Date: " + borrowDate +
                ", Expected Return Date: " + expectedReturnDate +
                ", Returned: " + returned;
    }
}
