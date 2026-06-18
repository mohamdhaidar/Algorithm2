package BackEnd;

public class BorrowRecordList {
    private static BorrowRecord head = null;
    private static final int MAX_BORROW_LIMIT = 3;

    public static void addBorrowRecord(int recordId, int bookNumber, String borrowerName, String borrowDate, String expectedReturnDate) {
        if (searchByRecordId(recordId) != null) {
            System.out.println("Borrow record already exists.");
            return;
        }

        BorrowRecord newRecord = new BorrowRecord(recordId, bookNumber, borrowerName, borrowDate, expectedReturnDate);

        if (head == null) {
            head = newRecord;
            return;
        }

        BorrowRecord cur = head;

        while (cur.next != null) {
            cur = cur.next;
        }

        cur.next = newRecord;
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

    public static void searchByBorrowerName(String borrowerName) {
        BorrowRecord cur = head;
        boolean found = false;

        while (cur != null) {
            if (cur.borrowerName.equalsIgnoreCase(borrowerName)) {
                System.out.println(cur);
                found = true;
            }

            cur = cur.next;
        }

        if (!found) {
            System.out.println("No borrow records found for this borrower.");
        }
    }

    public static boolean updateExpectedReturnDate(int recordId, String newExpectedReturnDate) {
        BorrowRecord record = searchByRecordId(recordId);

        if (record == null) {
            return false;
        }

        record.expectedReturnDate = newExpectedReturnDate;
        return true;
    }

    public static boolean markAsReturned(int recordId) {
        BorrowRecord record = searchByRecordId(recordId);

        if (record == null) {
            return false;
        }

        record.returned = true;
        return true;
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

        addBorrowRecord(recordId, bookNumber, borrowerName, borrowDate, expectedReturnDate);
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

    public static void printMostBorrowedBooks() {
        int recordsCount = countBorrowRecords();

        if (recordsCount == 0) {
            System.out.println("There are no borrow records.");
            return;
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

        System.out.println("Most borrowed book(s):");

        for (int i = 0; i < uniqueBooks; i++) {
            if (borrowCounts[i] == max) {
                System.out.println("Book Number: " + bookNumbers[i] + ", Borrow Count: " + borrowCounts[i]);
            }
        }

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

    public static void printMostReadAuthors() {
        int recordsCount = countBorrowRecords();

        if (recordsCount == 0) {
            System.out.println("There are no borrow records.");
            return;
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
            System.out.println("No authors found for the borrowed books.");
            return;
        }

        int max = authorCounts[0];

        for (int i = 1; i < uniqueAuthors; i++) {
            if (authorCounts[i] > max) {
                max = authorCounts[i];
            }
        }

        System.out.println("Most read author(s):");

        for (int i = 0; i < uniqueAuthors; i++) {
            if (authorCounts[i] == max) {
                System.out.println("Author: " + authors[i] + ", Borrow Count: " + authorCounts[i]);
            }
        }

    }


}