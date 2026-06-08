package BackEnd;

public class BookWaitingQueue {
    private static WaitingRequest front = null;

    public static void addRequest(int requestId, int bookNumber, String studentName, boolean graduatingStudent, String requestDate) {
        if (searchByRequestId(requestId) != null) {
            System.out.println("Waiting request already exists.");
            return;
        }

        WaitingRequest newRequest = new WaitingRequest(requestId, bookNumber, studentName, graduatingStudent, requestDate);

        if (front == null) {
            front = newRequest;
            return;
        }

        if (newRequest.graduatingStudent && !front.graduatingStudent) {
            newRequest.next = front;
            front = newRequest;
            return;
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

    public static void printAllRequests() {
        if (front == null) {
            System.out.println("There are no waiting requests.");
            return;
        }

        WaitingRequest cur = front;

        while (cur != null) {
            System.out.println(cur);
            cur = cur.next;
        }
    }

    public static void printRequestsByBookNumber(int bookNumber) {
        WaitingRequest cur = front;
        boolean found = false;

        while (cur != null) {
            if (cur.bookNumber == bookNumber) {
                System.out.println(cur);
                found = true;
            }

            cur = cur.next;
        }

        if (!found) {
            System.out.println("There are no waiting requests for this book.");
        }
    }
}