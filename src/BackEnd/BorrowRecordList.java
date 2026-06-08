package BackEnd;

public class BorrowRecordList {
    private static BorrowRecord head = null;

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
}