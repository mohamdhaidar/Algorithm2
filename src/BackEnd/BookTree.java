package BackEnd;

import static java.lang.Math.max;

public class BookTree {
    static private Book Root = null;

    public static Book search(int number) {
        return search(Root, number);
    }

    private static Book search(Book cur, int number) {
        if (cur == null) return null;

        if (cur.bookNumber == number) return cur;

        else if (cur.bookNumber < number)
            return search(cur.RChild, number);
        else
            return search(cur.LChild, number);
    }

    private static int getHeight(Book b) {
        if (b == null) return 0;
        return b.height;
    }

    private static int getBalance(Book b) {
        if (b == null)
            return 0;
        return getHeight(b.LChild) - getHeight(b.RChild);
    }

    // LL Rotate
    private static Book rightRotate(Book b) {
        Book b2 = b.LChild;
        Book cur = b2.RChild;

        b2.RChild = b;
        b.LChild = cur;

        b.height = max(getHeight(b.RChild), getHeight(b.LChild)) + 1;
        b2.height = max(getHeight(b2.RChild), getHeight(b2.LChild)) + 1;

        return b2;
    }

    // RR Rotate
    private static Book leftRotate(Book b) {
        Book b2 = b.RChild;
        Book cur = b2.LChild;

        b2.LChild = b;
        b.RChild = cur;

        b.height = max(getHeight(b.RChild), getHeight(b.LChild)) + 1;
        b2.height = max(getHeight(b2.RChild), getHeight(b2.LChild)) + 1;

        return b2;
    }

    public static void insert(int number, int copies, String auth) {
        Root = insert(Root, number, copies, auth);
    }

    private static Book insert(Book cur, int number, int copies, String auth) {
        if (cur == null) {
            cur = new Book(number, copies, 1, auth);
            return cur;
        }

        if (number > cur.bookNumber)
            cur.RChild = insert(cur.RChild, number, copies, auth);
        else if (number < cur.bookNumber)
            cur.LChild = insert(cur.LChild, number, copies, auth);
        else
            return cur;

        cur.height = max(getHeight(cur.LChild), getHeight(cur.RChild)) + 1;

        int balance = getBalance(cur);

        // LL case
        if (balance > 1 && number < cur.LChild.bookNumber)
            return rightRotate(cur);
            // RR case
        else if (balance < -1 && number > cur.RChild.bookNumber)
            return leftRotate(cur);
            // LR
        else if (balance > 1 && number > cur.LChild.bookNumber) {
            cur.LChild = leftRotate(cur.LChild);
            return rightRotate(cur);
        }
        // RL
        else if (balance < -1 && number < cur.RChild.bookNumber) {
            cur.RChild = rightRotate(cur.RChild);
            return leftRotate(cur);
        }

        return cur;
    }

    private static Book minValue(Book b) {
        Book cur = b;
        while (cur.LChild != null)
            cur = cur.LChild;
        return cur;
    }

    public static void delete(int number) {
        Root = delete(Root, number);
    }

    private static Book delete(Book cur, int number) {
        if (cur == null) return cur;

        if (cur.bookNumber < number)
            cur.RChild = delete(cur.RChild, number);
        else if (cur.bookNumber > number)
            cur.LChild = delete(cur.LChild, number);
        else {
            // case 1 0 child
            if (cur.LChild == null || cur.RChild == null) {
                Book temp;
                if (cur.RChild != null)
                    temp = cur.RChild;
                else
                    temp = cur.LChild;

                cur = temp;
            }
            // case 2 children
            else {
                Book temp = minValue(cur.RChild);

                cur.bookNumber = temp.bookNumber;
                cur.copiesNumber = temp.copiesNumber;
                cur.author = temp.author;

                cur.RChild = delete(cur.RChild, temp.bookNumber);
            }
        }
        if (cur == null)
            return cur;

        cur.height = max(getHeight(cur.RChild), getHeight(cur.LChild)) + 1;
        int bal = getBalance(cur);

        // LL case
        if (bal > 1 && getBalance(cur.LChild) >= 0)
            return rightRotate(cur);
        // RR case
        if (bal < -1 && getBalance(cur.RChild) <= 0)
            return leftRotate(cur);
        // LR case
        if (bal > 1 && getBalance(cur.LChild) < 0) {
            cur.LChild = leftRotate(cur.LChild);
            return rightRotate(cur);
        }
        // RL case
        if (bal < -1 && getBalance(cur.RChild) > 0) {
            cur.RChild = rightRotate(cur.RChild);
            return leftRotate(cur);
        }

        return cur;
    }
}