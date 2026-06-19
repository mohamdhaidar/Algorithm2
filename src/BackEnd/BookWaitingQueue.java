package BackEnd;

import java.util.ArrayList;

public class BookWaitingQueue {
    private static WaitingRequest front = null;

    public static String addRequest(int requestId, int bookNumber, String studentName, boolean graduatingStudent, String requestDate) {
        if (searchByRequestId(requestId) != null) {
            System.out.println("Waiting request already exists.");
            return "Waiting request already exists.";
        }

        WaitingRequest newRequest = new WaitingRequest(requestId, bookNumber, studentName, graduatingStudent, requestDate);

        if (front == null) {
            front = newRequest;
            return "Done.";
        }

        if (newRequest.graduatingStudent && !front.graduatingStudent) {
            newRequest.next = front;
            front = newRequest;
            return "Done.";
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
        return "Done.";
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

    public static WaitingRequest serveNextRequest(int bookNumber) {
        if (front == null) {
            return null;
        }

        if (front.bookNumber == bookNumber) {
            WaitingRequest temp = front;
            front = front.next;
            temp.next = null;
            return temp;
        }

        WaitingRequest cur = front;

        while (cur.next != null) {
            if (cur.next.bookNumber == bookNumber) {
                WaitingRequest temp = cur.next;
                cur.next = cur.next.next;
                temp.next = null;
                return temp;
            }

            cur = cur.next;
        }

        return null;
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