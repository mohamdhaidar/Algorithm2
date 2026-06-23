package BackEnd;

import java.util.ArrayList;

public class BorrowRecordList {
    private static BorrowRecord head = null;
    private static final int MAX_BORROW_LIMIT = 3;

    public static String addBorrowRecord(int recordId, int bookNumber, String borrowerName, String borrowDate, String expectedReturnDate) {
        if (recordId <= 0) {
            return "Record ID must be greater than 0.";
        }

        if (bookNumber <= 0) {
            return "Book number must be greater than 0.";
        }

        if (borrowerName == null || borrowerName.trim().isEmpty()) {
            return "Borrower name is required.";
        }

        if (borrowDate == null || borrowDate.trim().isEmpty()) {
            return "Borrow date is required.";
        }

        if (expectedReturnDate == null || expectedReturnDate.trim().isEmpty()) {
            return "Expected return date is required.";
        }

        if (searchByRecordId(recordId) != null) {
            return "Borrow record already exists.";
        }

        BorrowRecord newRecord = new BorrowRecord(recordId, bookNumber, borrowerName, borrowDate, expectedReturnDate);

        if (head == null) {
            head = newRecord;
            return "Done .";
        }

        BorrowRecord cur = head;

        while (cur.next != null) {
            cur = cur.next;
        }

        cur.next = newRecord;
        return "Done .";
    }

    public static BorrowRecord searchByRecordId(int recordId) {
        BorrowRecord cur = head;

        while (cur != null) {
            if (cur.recordId == recordId) {
                return cur;
            }

            cur = cur.next;
        }

        return null;
    }

    public static ArrayList<BorrowRecord> searchByBorrowerName(String borrowerName) {
        ArrayList<BorrowRecord> result = new ArrayList<>();
        BorrowRecord cur = head;

        while (cur != null) {
            if (cur.borrowerName.equalsIgnoreCase(borrowerName)) {
                result.add(cur);
            }

            cur = cur.next;
        }

        return result;
    }

    public static boolean updateExpectedReturnDate(int recordId, String newExpectedReturnDate) {
        BorrowRecord record = searchByRecordId(recordId);

        if (record == null) {
            return false;
        }

        record.expectedReturnDate = newExpectedReturnDate;
        return true;
    }


    public static String returnBorrowedBook(int recordId) {
        BorrowRecord record = searchByRecordId(recordId);

        if (recordId <= 0) {
            return "Record ID must be greater than 0.";
        }

        if (record == null) {
            return "Borrow record not found.";
        }

        if (record.returned) {
            return "This borrow record is already returned.";
        }

        String result = BookTree.returnBook(record.bookNumber);

        if (!"Done .".equals(result)) {
            return result;
        }

        record.returned = true;
        return "Done .";
    }

    public static int countActiveBorrowRecords(String borrowerName) {
        BorrowRecord cur = head;
        int count = 0;

        while (cur != null) {
            if (cur.borrowerName.equalsIgnoreCase(borrowerName) && !cur.returned ) {
                count++;
            }

            cur = cur.next;
        }

        return count;
    }

    public static boolean canBorrowMore(String borrowerName) {
        return countActiveBorrowRecords(borrowerName) < MAX_BORROW_LIMIT;
    }

    public static String addBorrowRecordWithLimitCheck(int recordId, int bookNumber, String borrowerName, String borrowDate, String expectedReturnDate) {
        if (!canBorrowMore(borrowerName)) {
            return "Borrowing failed. The borrower has reached the maximum borrow limit.";
        }

        String result = addBorrowRecord(recordId, bookNumber, borrowerName, borrowDate, expectedReturnDate);

        if (!"Done .".equals(result)) {
            return result;
        }

        return "Borrow record added successfully.";
    }

    public static void printAllRecords() {
        if (head == null) {
            System.out.println("There are no borrow records.");
            return;
        }

        BorrowRecord cur = head;

        while (cur != null) {
            System.out.println(cur);
            cur = cur.next;
        }
    }

    private static int countBorrowRecords() {
        BorrowRecord cur = head;
        int count = 0;

        while (cur != null) {
            count++;
            cur = cur.next;
        }

        return count;
    }

    // the most read books

    private static int findBookIndex(int[] bookNumbers, int size, int bookNumber) {
        for (int i = 0; i < size; i++) {
            if (bookNumbers[i] == bookNumber) {
                return i;
            }
        }

        return -1;
    }

    public static String getMostBorrowedBooksReport() {
        int recordsCount = countBorrowRecords();

        if (recordsCount == 0) {
            return "There are no borrow records.";
        }

        int[] bookNumbers = new int[recordsCount];
        int[] borrowCounts = new int[recordsCount];
        int uniqueBooks = 0;

        BorrowRecord cur = head;

        while (cur != null) {
            int index = findBookIndex(bookNumbers, uniqueBooks, cur.bookNumber);

            if (index == -1) {
                bookNumbers[uniqueBooks] = cur.bookNumber;
                borrowCounts[uniqueBooks] = 1;
                uniqueBooks++;
            } else {
                borrowCounts[index]++;
            }

            cur = cur.next;
        }

        int max = borrowCounts[0];

        for (int i = 1; i < uniqueBooks; i++) {
            if (borrowCounts[i] > max) {
                max = borrowCounts[i];
            }
        }

        StringBuilder report = new StringBuilder("Most borrowed book(s):\n");

        for (int i = 0; i < uniqueBooks; i++) {
            if (borrowCounts[i] == max) {
                report.append("Book Number: ")
                        .append(bookNumbers[i])
                        .append(", Borrow Count: ")
                        .append(borrowCounts[i])
                        .append("\n");
            }
        }

        return report.toString();
    }

    // the most read authors

    private static int findAuthorIndex(String[] authors, int size, String author) {
        for (int i = 0; i < size; i++) {
            if (authors[i].equalsIgnoreCase(author)) {
                return i;
            }
        }

        return -1;
    }

    public static String getMostReadAuthorsReport() {
        int recordsCount = countBorrowRecords();

        if (recordsCount == 0) {
            return "There are no borrow records.";
        }

        String[] authors = new String[recordsCount];
        int[] authorCounts = new int[recordsCount];
        int uniqueAuthors = 0;

        BorrowRecord cur = head;

        while (cur != null) {
            Book book = BookTree.search(cur.bookNumber);

            if (book != null) {
                String author = book.getAuthor();

                int index = findAuthorIndex(authors, uniqueAuthors, author);

                if (index == -1) {
                    authors[uniqueAuthors] = author;
                    authorCounts[uniqueAuthors] = 1;
                    uniqueAuthors++;
                } else {
                    authorCounts[index]++;
                }
            }

            cur = cur.next;
        }

        if (uniqueAuthors == 0) {
            return "No authors found for the borrowed books.";
        }

        int max = authorCounts[0];

        for (int i = 1; i < uniqueAuthors; i++) {
            if (authorCounts[i] > max) {
                max = authorCounts[i];
            }
        }

        StringBuilder report = new StringBuilder("Most read author(s):\n");

        for (int i = 0; i < uniqueAuthors; i++) {
            if (authorCounts[i] == max) {
                report.append("Author: ")
                        .append(authors[i])
                        .append(", Borrow Count: ")
                        .append(authorCounts[i])
                        .append("\n");
            }
        }

        return report.toString();
    }

    public static ArrayList<BorrowRecord> getAllRecords() {
        ArrayList<BorrowRecord> records = new ArrayList<>();
        BorrowRecord cur = head;

        while (cur != null) {
            records.add(cur);
            cur = cur.next;
        }

        return records;
    }

    public static String borrowBookWithRecord(int recordId, int bookNumber, String borrowerName, String borrowDate, String expectedReturnDate) {
        if (searchByRecordId(recordId) != null) {
            return "Borrow record already exists.";
        }

        if (!canBorrowMore(borrowerName)) {
            return "Borrowing failed. The borrower has reached the maximum borrow limit.";
        }

        String borrowResult = BookTree.BorrowBook(bookNumber);

        if (!"Done .".equals(borrowResult)) {
            return borrowResult;
        }

        String addRecordResult = addBorrowRecord(recordId, bookNumber, borrowerName, borrowDate, expectedReturnDate);

        if (!"Done .".equals(addRecordResult)) {
            BookTree.returnBook(bookNumber);
            return addRecordResult;
        }

        return "Done .";

    }

}