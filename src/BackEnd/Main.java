package BackEnd;

public class Main {
    public static void main(String[] args) {
        BorrowRecordList.addBorrowRecord(1, 1001, "Ahmad", "2026-06-07", "2026-06-14");
        BorrowRecordList.addBorrowRecord(2, 1002, "Sara", "2026-06-07", "2026-06-15");
        BorrowRecordList.addBorrowRecord(3, 1003, "Ahmad", "2026-06-08", "2026-06-16");

        System.out.println("All records:");
        BorrowRecordList.printAllRecords();

        System.out.println("\nSearch by record ID:");
        BorrowRecord record = BorrowRecordList.searchByRecordId(2);

        if (record != null) {
            System.out.println(record);
        } else {
            System.out.println("Record not found.");
        }

        System.out.println("\nSearch by borrower name:");
        BorrowRecordList.searchByBorrowerName("Ahmad");

        System.out.println("\nUpdate expected return date:");
        boolean updated = BorrowRecordList.updateExpectedReturnDate(1, "2026-06-20");

        if (updated) {
            System.out.println("Record updated successfully.");
        } else {
            System.out.println("Record not found.");
        }

        System.out.println("\nMark as returned:");
        boolean returned = BorrowRecordList.markAsReturned(1);

        if (returned) {
            System.out.println("Book marked as returned.");
        } else {
            System.out.println("Record not found.");
        }

        System.out.println("\nAll records after update:");
        BorrowRecordList.printAllRecords();


        // This test is for the BookWaitingQueue class.
        System.out.println("\nTesting BookWaitingQueue:\n\n");
        BookWaitingQueue.addRequest(1, 1001, "Ahmad", false, "2026-06-07");
        BookWaitingQueue.addRequest(2, 2002, "Sara", true, "2026-06-07");
        BookWaitingQueue.addRequest(3, 1001, "Omar", false, "2026-06-08");
        BookWaitingQueue.addRequest(4, 1001, "Lana", true, "2026-06-08");

        System.out.println("All waiting requests:");
        BookWaitingQueue.printAllRequests();

        System.out.println("\nWaiting requests for book 1001:");
        BookWaitingQueue.printRequestsByBookNumber(1001);

        System.out.println("\nNext student for book 1001:");
        System.out.println(BookWaitingQueue.peekNextRequest(1001));

        System.out.println("\nServing next request for book 1001:");
        WaitingRequest served = BookWaitingQueue.serveNextRequest(1001);

        if (served != null) {
            System.out.println("Served: " + served);
        } else {
            System.out.println("No waiting request found for this book.");
        }

        System.out.println("\nAll waiting requests after serving:");
        BookWaitingQueue.printAllRequests();
    }
}