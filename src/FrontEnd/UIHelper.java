package FrontEnd;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.JTableHeader;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;

public class UIHelper {

    public static final Color SIDEBAR_COLOR = new Color(35, 47, 62);
    public static final Color SIDEBAR_ACTIVE_COLOR = new Color(52, 73, 94);
    public static final Color BACKGROUND_COLOR = new Color(245, 247, 250);
    public static final Color WHITE_COLOR = Color.WHITE;
    public static final Color TEXT_COLOR = new Color(33, 37, 41);
    public static final Color SECONDARY_TEXT_COLOR = new Color(100, 110, 120);
    public static final Color BUTTON_COLOR = new Color(41, 128, 185);
    public static final Color BUTTON_HOVER_COLOR = new Color(52, 152, 219);
    public static final Color DANGER_COLOR = new Color(192, 57, 43);

    public static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 24);
    public static final Font PAGE_TITLE_FONT = new Font("Arial", Font.BOLD, 28);
    public static final Font SECTION_TITLE_FONT = new Font("Arial", Font.BOLD, 18);
    public static final Font NORMAL_FONT = new Font("Arial", Font.PLAIN, 15);
    public static final Font BUTTON_FONT = new Font("Arial", Font.BOLD, 14);

    public static final int YES_OPTION = JOptionPane.YES_OPTION;

    private UIHelper() {
        // Utility class - no objects needed
    }

    public static JButton createSidebarButton(String text) {
        JButton button = new JButton(text);

        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(BUTTON_FONT);
        button.setForeground(WHITE_COLOR);
        button.setBackground(SIDEBAR_COLOR);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return button;
    }

    public static void setSidebarButtonActive(JButton button) {
        button.setBackground(SIDEBAR_ACTIVE_COLOR);
    }

    public static void setSidebarButtonInactive(JButton button) {
        button.setBackground(SIDEBAR_COLOR);
    }

    public static JButton createPrimaryButton(String text) {
        JButton button = new JButton(text);

        button.setFocusPainted(false);
        button.setFont(BUTTON_FONT);
        button.setForeground(WHITE_COLOR);
        button.setBackground(BUTTON_COLOR);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));

        return button;
    }

    public static JButton createDangerButton(String text) {
        JButton button = new JButton(text);

        button.setFocusPainted(false);
        button.setFont(BUTTON_FONT);
        button.setForeground(WHITE_COLOR);
        button.setBackground(DANGER_COLOR);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));

        return button;
    }

    public static JTextField createTextField() {
        JTextField textField = new JTextField();

        textField.setFont(NORMAL_FONT);
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 205, 210)),
                BorderFactory.createEmptyBorder(6, 8, 6, 8)
        ));

        return textField;
    }

    public static void styleTable(JTable table) {
        table.setFont(NORMAL_FONT);
        table.setRowHeight(28);
        table.setGridColor(new Color(220, 225, 230));
        table.setSelectionBackground(new Color(214, 234, 248));
        table.setSelectionForeground(TEXT_COLOR);

        JTableHeader header = table.getTableHeader();
        header.setFont(BUTTON_FONT);
        header.setBackground(SIDEBAR_COLOR);
        header.setForeground(WHITE_COLOR);
    }

    public static void addPadding(JComponent component, int top, int left, int bottom, int right) {
        component.setBorder(BorderFactory.createEmptyBorder(top, left, bottom, right));
    }

    public static void showSuccessMessage(Component parent, String message) {
        JOptionPane.showMessageDialog(
                parent,
                message,
                "Success",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    public static void showErrorMessage(Component parent, String message) {
        JOptionPane.showMessageDialog(
                parent,
                message,
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }

    public static int showConfirmMessage(Component parent, String message) {
        return JOptionPane.showConfirmDialog(
                parent,
                message,
                "Confirm",
                JOptionPane.YES_NO_OPTION
        );
    }
}