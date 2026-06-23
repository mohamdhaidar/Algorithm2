package BackEnd;

import java.util.ArrayList;

public class BookWaitingQueue {
    private static WaitingRequest front = null;

    public static String addRequest(
            int requestId,
            int bookNumber,
            String studentName,
            boolean graduatingStudent,
            String requestDate
    ) {
        if (requestId <= 0) {
            return "Request ID must be greater than 0.";
        }

        if (bookNumber <= 0) {
            return "Book Number must be greater than 0.";
        }

        if (studentName == null || studentName.trim().isEmpty()) {
            return "Student name is required.";
        }

        if (requestDate == null || requestDate.trim().isEmpty()) {
            return "Request date is required.";
        }

        if (searchByRequestId(requestId) != null) {
            return "Waiting request already exists.";
        }

        Book book = BookTree.search(bookNumber);

        if (book == null) {
            return "The book doesn't exist .";
        }

        if (book.getAvailableCopies() > 0) {
            return "This book is currently available, so a waiting request is not needed.";
        }

        WaitingRequest newRequest = new WaitingRequest(
                requestId,
                bookNumber,
                studentName.trim(),
                graduatingStudent,
                requestDate.trim()
        );

        if (front == null) {
            front = newRequest;
            return "Done .";
        }

        if (newRequest.graduatingStudent && !front.graduatingStudent) {
            newRequest.next = front;
            front = newRequest;
            return "Done .";
        }

        WaitingRequest cur = front;

        while (cur.next != null) {
            if (newRequest.graduatingStudent && !cur.next.graduatingStudent) {
                break;
            }

            cur = cur.next;
        }

        newRequest.next = cur.next;
        cur.next = newRequest;

        return "Done .";
    }
    public static WaitingRequest peekNextRequest(int bookNumber) {
        WaitingRequest cur = front;

        while (cur != null) {
            if (cur.bookNumber == bookNumber) {
                return cur;
            }

            cur = cur.next;
        }

        return null;
    }
    public static String validateServeNextRequest(int bookNumber) {
        if (bookNumber <= 0) {
            return "Book Number must be greater than 0.";
        }

        Book book = BookTree.search(bookNumber);

        if (book == null) {
            return "The book doesn't exist .";
        }

        if (peekNextRequest(bookNumber) == null) {
            return "There is no waiting request for this book.";
        }

        if (book.getAvailableCopies() <= 0) {
            return "There is no available copy for this book yet.";
        }

        return "Done .";
    }


    public static String serveNextRequest(
            int bookNumber,
            int recordId,
            String borrowDate,
            String expectedReturnDate
    ) {
        String validationMessage = validateServeNextRequest(bookNumber);

        if (!"Done .".equals(validationMessage)) {
            return validationMessage;
        }

        if (recordId <= 0) {
            return "Record ID must be greater than 0.";
        }

        if (borrowDate == null || borrowDate.trim().isEmpty()) {
            return "Borrow date is required.";
        }

        if (expectedReturnDate == null || expectedReturnDate.trim().isEmpty()) {
            return "Expected return date is required.";
        }

        WaitingRequest nextRequest = peekNextRequest(bookNumber);

        String borrowResult = BorrowRecordList.borrowBookWithRecord(
                recordId,
                bookNumber,
                nextRequest.getStudentName(),
                borrowDate.trim(),
                expectedReturnDate.trim()
        );

        if (!"Done .".equals(borrowResult)) {
            return borrowResult;
        }

        removeNextRequest(bookNumber);

        return "Done .";
    }


    private static void removeNextRequest(int bookNumber) {
        if (front == null) {
            return;
        }

        if (front.bookNumber == bookNumber) {
            WaitingRequest removedRequest = front;
            front = front.next;
            removedRequest.next = null;
            return;
        }

        WaitingRequest cur = front;

        while (cur.next != null) {
            if (cur.next.bookNumber == bookNumber) {
                WaitingRequest removedRequest = cur.next;
                cur.next = cur.next.next;
                removedRequest.next = null;
                return;
            }

            cur = cur.next;
        }
    }

    public static WaitingRequest searchByRequestId(int requestId) {
        WaitingRequest cur = front;

        while (cur != null) {
            if (cur.requestId == requestId) {
                return cur;
            }

            cur = cur.next;
        }

        return null;
    }

    public static ArrayList<WaitingRequest> getAllRequests() {
        ArrayList<WaitingRequest> requests = new ArrayList<>();
        WaitingRequest cur = front;

        while (cur != null) {
            requests.add(cur);
            cur = cur.next;
        }

        return requests;
    }

    public static ArrayList<WaitingRequest> getRequestsByBookNumber(int bookNumber) {
        ArrayList<WaitingRequest> requests = new ArrayList<>();
        WaitingRequest cur = front;

        while (cur != null) {
            if (cur.bookNumber == bookNumber) {
                requests.add(cur);
            }

            cur = cur.next;
        }

        return requests;
    }

}