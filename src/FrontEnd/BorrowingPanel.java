package FrontEnd;

import BackEnd.BorrowRecord;
import BackEnd.BorrowRecordList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.ArrayList;

public class BorrowingPanel extends JPanel {

    private JTable recordsTable;
    private DefaultTableModel tableModel;

    private JTextField recordIdField;
    private JTextField bookNumberField;
    private JTextField borrowerNameField;
    private JTextField borrowDateField;
    private JTextField expectedReturnDateField;

    public BorrowingPanel() {
        initComponents();
        refreshRecordsTable();
    }

    private void initComponents() {
        setLayout(new BorderLayout(20, 20));
        setBackground(UIHelper.BACKGROUND_COLOR);
        setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        add(createHeaderPanel(), BorderLayout.NORTH);

        tableModel = new DefaultTableModel(
                new Object[]{
                        "Record ID",
                        "Book Number",
                        "Borrower Name",
                        "Borrow Date",
                        "Expected Return Date",
                        "Returned"
                },
                0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        recordsTable = new JTable(tableModel);
        UIHelper.styleTable(recordsTable);
        recordsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        recordsTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                fillFieldsFromSelectedRow();
            }
        });

        JScrollPane scrollPane = new JScrollPane(recordsTable);

        JPanel rightPanel = new JPanel(new BorderLayout(10, 15));
        rightPanel.setOpaque(false);
        rightPanel.setPreferredSize(new Dimension(360, 0));

        rightPanel.add(createFormPanel(), BorderLayout.NORTH);
        rightPanel.add(createButtonsPanel(), BorderLayout.CENTER);

        add(scrollPane, BorderLayout.CENTER);
        add(rightPanel, BorderLayout.EAST);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("Borrowing Management");
        titleLabel.setFont(UIHelper.PAGE_TITLE_FONT);
        titleLabel.setForeground(UIHelper.TEXT_COLOR);

        JLabel subtitleLabel = new JLabel("Borrow books, return books, and manage borrow records.");
        subtitleLabel.setFont(UIHelper.NORMAL_FONT);
        subtitleLabel.setForeground(UIHelper.SECONDARY_TEXT_COLOR);
        subtitleLabel.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));

        JPanel textPanel = new JPanel(new BorderLayout());
        textPanel.setOpaque(false);
        textPanel.add(titleLabel, BorderLayout.NORTH);
        textPanel.add(subtitleLabel, BorderLayout.CENTER);

        headerPanel.add(textPanel, BorderLayout.WEST);

        return headerPanel;
    }

    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(UIHelper.WHITE_COLOR);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new java.awt.Color(220, 225, 230)),
                BorderFactory.createEmptyBorder(18, 18, 18, 18)
        ));

        recordIdField = UIHelper.createTextField();
        bookNumberField = UIHelper.createTextField();
        borrowerNameField = UIHelper.createTextField();
        borrowDateField = UIHelper.createTextField();
        expectedReturnDateField = UIHelper.createTextField();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 0, 8, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        JLabel sectionTitle = new JLabel("Borrow Record Details");
        sectionTitle.setFont(UIHelper.SECTION_TITLE_FONT);
        sectionTitle.setForeground(UIHelper.TEXT_COLOR);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        formPanel.add(sectionTitle, gbc);

        addFormRow(formPanel, gbc, 1, "Record ID", recordIdField);
        addFormRow(formPanel, gbc, 2, "Book Number", bookNumberField);
        addFormRow(formPanel, gbc, 3, "Borrower Name", borrowerNameField);
        addFormRow(formPanel, gbc, 4, "Borrow Date", borrowDateField);
        addFormRow(formPanel, gbc, 5, "Expected Return Date", expectedReturnDateField);

        return formPanel;
    }

    private void addFormRow(JPanel panel, GridBagConstraints gbc, int row, String labelText, JTextField field) {
        JLabel label = new JLabel(labelText);
        label.setFont(UIHelper.NORMAL_FONT);
        label.setForeground(UIHelper.TEXT_COLOR);

        gbc.gridwidth = 1;

        gbc.gridx = 0;
        gbc.gridy = row * 2;
        gbc.weightx = 1;
        panel.add(label, gbc);

        gbc.gridx = 0;
        gbc.gridy = row * 2 + 1;
        gbc.weightx = 1;
        panel.add(field, gbc);
    }

    private JPanel createButtonsPanel() {
        JPanel buttonsPanel = new JPanel(new GridLayout(6, 1, 0, 10));
        buttonsPanel.setOpaque(false);

        JButton borrowButton = UIHelper.createPrimaryButton("Borrow Book");
        JButton returnButton = UIHelper.createPrimaryButton("Return Book");
        JButton updateReturnDateButton = UIHelper.createPrimaryButton("Update Return Date");
        JButton searchByBorrowerButton = UIHelper.createPrimaryButton("Search By Borrower");
        JButton refreshButton = UIHelper.createPrimaryButton("Refresh Table");
        JButton clearButton = UIHelper.createPrimaryButton("Clear Fields");

        borrowButton.addActionListener(e -> borrowBook());
        returnButton.addActionListener(e -> returnBook());
        updateReturnDateButton.addActionListener(e -> updateReturnDate());
        searchByBorrowerButton.addActionListener(e -> searchByBorrower());
        refreshButton.addActionListener(e -> refreshRecordsTable());
        clearButton.addActionListener(e -> clearFields());

        buttonsPanel.add(borrowButton);
        buttonsPanel.add(returnButton);
        buttonsPanel.add(updateReturnDateButton);
        buttonsPanel.add(searchByBorrowerButton);
        buttonsPanel.add(refreshButton);
        buttonsPanel.add(clearButton);

        return buttonsPanel;
    }

    private void borrowBook() {
        try {
            int recordId = readIntegerField(recordIdField, "Record ID");
            int bookNumber = readIntegerField(bookNumberField, "Book Number");
            String borrowerName = readTextField(borrowerNameField, "Borrower Name");
            String borrowDate = readTextField(borrowDateField, "Borrow Date");
            String expectedReturnDate = readTextField(expectedReturnDateField, "Expected Return Date");

            String message = BorrowRecordList.borrowBookWithRecord(
                    recordId,
                    bookNumber,
                    borrowerName,
                    borrowDate,
                    expectedReturnDate
            );

            if (isSuccessMessage(message)) {
                UIHelper.showSuccessMessage(this, message);
                refreshRecordsTable();
                clearFields();
            } else {
                UIHelper.showErrorMessage(this, message);
            }

        } catch (IllegalArgumentException ex) {
            UIHelper.showErrorMessage(this, ex.getMessage());
        }
    }

    private void returnBook() {
        try {
            int recordId = readIntegerField(recordIdField, "Record ID");

            int choice = UIHelper.showConfirmMessage(
                    this,
                    "Are you sure you want to return this book?"
            );

            if (choice != UIHelper.YES_OPTION) {
                return;
            }

            String message = BorrowRecordList.returnBorrowedBook(recordId);

            if (isSuccessMessage(message)) {
                UIHelper.showSuccessMessage(this, message);
                refreshRecordsTable();
                clearFields();
            } else {
                UIHelper.showErrorMessage(this, message);
            }

        } catch (IllegalArgumentException ex) {
            UIHelper.showErrorMessage(this, ex.getMessage());
        }
    }

    private void updateReturnDate() {
        try {
            int recordId = readIntegerField(recordIdField, "Record ID");
            String newExpectedReturnDate = readTextField(expectedReturnDateField, "Expected Return Date");

            boolean updated = BorrowRecordList.updateExpectedReturnDate(
                    recordId,
                    newExpectedReturnDate
            );

            if (updated) {
                UIHelper.showSuccessMessage(this, "Expected return date updated successfully.");
                refreshRecordsTable();
                clearFields();
            } else {
                UIHelper.showErrorMessage(this, "Borrow record not found.");
            }

        } catch (IllegalArgumentException ex) {
            UIHelper.showErrorMessage(this, ex.getMessage());
        }
    }

    private void searchByBorrower() {
        try {
            String borrowerName = readTextField(borrowerNameField, "Borrower Name");

            ArrayList<BorrowRecord> records = BorrowRecordList.searchByBorrowerName(borrowerName);

            tableModel.setRowCount(0);

            if (records.isEmpty()) {
                UIHelper.showErrorMessage(this, "No records found for this borrower.");
                return;
            }

            for (BorrowRecord record : records) {
                addRecordToTable(record);
            }

            UIHelper.showSuccessMessage(this, "Records found.");

        } catch (IllegalArgumentException ex) {
            UIHelper.showErrorMessage(this, ex.getMessage());
        }
    }

    public void refreshRecordsTable() {
        tableModel.setRowCount(0);

        ArrayList<BorrowRecord> records = BorrowRecordList.getAllRecords();

        for (BorrowRecord record : records) {
            addRecordToTable(record);
        }
    }

    private void addRecordToTable(BorrowRecord record) {
        tableModel.addRow(new Object[]{
                record.getRecordId(),
                record.getBookNumber(),
                record.getBorrowerName(),
                record.getBorrowDate(),
                record.getExpectedReturnDate(),
                record.isReturned() ? "Yes" : "No"
        });
    }

    private void fillFieldsFromSelectedRow() {
        int selectedRow = recordsTable.getSelectedRow();

        if (selectedRow == -1) {
            return;
        }

        recordIdField.setText(String.valueOf(tableModel.getValueAt(selectedRow, 0)));
        bookNumberField.setText(String.valueOf(tableModel.getValueAt(selectedRow, 1)));
        borrowerNameField.setText(String.valueOf(tableModel.getValueAt(selectedRow, 2)));
        borrowDateField.setText(String.valueOf(tableModel.getValueAt(selectedRow, 3)));
        expectedReturnDateField.setText(String.valueOf(tableModel.getValueAt(selectedRow, 4)));
    }

    private int readIntegerField(JTextField field, String fieldName) {
        String text = field.getText().trim();

        if (text.isEmpty()) {
            throw new IllegalArgumentException(fieldName + " is required.");
        }

        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(fieldName + " must be a valid number.");
        }
    }

    private String readTextField(JTextField field, String fieldName) {
        String text = field.getText().trim();

        if (text.isEmpty()) {
            throw new IllegalArgumentException(fieldName + " is required.");
        }

        return text;
    }

    private boolean isSuccessMessage(String message) {
        if (message == null) {
            return false;
        }

        String lowerMessage = message.toLowerCase();

        return lowerMessage.contains("done")
                || lowerMessage.contains("success")
                || lowerMessage.contains("updated")
                || lowerMessage.contains("returned");
    }

    private void clearFields() {
        recordIdField.setText("");
        bookNumberField.setText("");
        borrowerNameField.setText("");
        borrowDateField.setText("");
        expectedReturnDateField.setText("");
        recordsTable.clearSelection();
    }
}