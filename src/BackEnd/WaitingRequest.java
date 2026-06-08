package BackEnd;

public class WaitingRequest {
    int requestId;
    int bookNumber;
    String studentName;
    boolean graduatingStudent;
    String requestDate;

    WaitingRequest next;

    public WaitingRequest(int requestId, int bookNumber, String studentName, boolean graduatingStudent, String requestDate) {
        this.requestId = requestId;
        this.bookNumber = bookNumber;
        this.studentName = studentName;
        this.graduatingStudent = graduatingStudent;
        this.requestDate = requestDate;
        this.next = null;
    }

    @Override
    public String toString() {
        return "Request ID: " + requestId +
                ", Book Number: " + bookNumber +
                ", Student Name: " + studentName +
                ", Graduating Student: " + graduatingStudent +
                ", Request Date: " + requestDate;
    }
}
