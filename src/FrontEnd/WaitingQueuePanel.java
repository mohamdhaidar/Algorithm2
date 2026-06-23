package FrontEnd;

import BackEnd.BookWaitingQueue;
import BackEnd.WaitingRequest;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
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
import javax.swing.JOptionPane;

public class WaitingQueuePanel extends JPanel {

    private JTable requestsTable;
    private DefaultTableModel tableModel;

    private JTextField requestIdField;
    private JTextField bookNumberField;
    private JTextField studentNameField;
    private JTextField requestDateField;
    private JCheckBox graduatingStudentCheckBox;

    public WaitingQueuePanel() {
        initComponents();
        refreshRequestsTable();
    }

    private void initComponents() {
        setLayout(new BorderLayout(20, 20));
        setBackground(UIHelper.BACKGROUND_COLOR);
        setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        add(createHeaderPanel(), BorderLayout.NORTH);

        tableModel = new DefaultTableModel(
                new Object[]{
                        "Request ID",
                        "Book Number",
                        "Student Name",
                        "Graduating Student",
                        "Request Date"
                },
                0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        requestsTable = new JTable(tableModel);
        UIHelper.styleTable(requestsTable);
        requestsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        requestsTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                fillFieldsFromSelectedRow();
            }
        });

        JScrollPane scrollPane = new JScrollPane(requestsTable);

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

        JLabel titleLabel = new JLabel("Waiting Queue");
        titleLabel.setFont(UIHelper.PAGE_TITLE_FONT);
        titleLabel.setForeground(UIHelper.TEXT_COLOR);

        JLabel subtitleLabel = new JLabel("Manage priority waiting requests for unavailable books.");
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

        requestIdField = UIHelper.createTextField();
        bookNumberField = UIHelper.createTextField();
        studentNameField = UIHelper.createTextField();
        requestDateField = UIHelper.createTextField();

        graduatingStudentCheckBox = new JCheckBox("Graduating Student");
        graduatingStudentCheckBox.setFont(UIHelper.NORMAL_FONT);
        graduatingStudentCheckBox.setForeground(UIHelper.TEXT_COLOR);
        graduatingStudentCheckBox.setBackground(UIHelper.WHITE_COLOR);
        graduatingStudentCheckBox.setFocusPainted(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 0, 8, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        JLabel sectionTitle = new JLabel("Waiting Request Details");
        sectionTitle.setFont(UIHelper.SECTION_TITLE_FONT);
        sectionTitle.setForeground(UIHelper.TEXT_COLOR);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        formPanel.add(sectionTitle, gbc);

        addFormRow(formPanel, gbc, 1, "Request ID", requestIdField);
        addFormRow(formPanel, gbc, 2, "Book Number", bookNumberField);
        addFormRow(formPanel, gbc, 3, "Student Name", studentNameField);
        addFormRow(formPanel, gbc, 4, "Request Date", requestDateField);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        formPanel.add(graduatingStudentCheckBox, gbc);

        return formPanel;
    }

    private void addFormRow(JPanel panel, GridBagConstraints gbc, int row, String labelText, JTextField field) {
        JLabel label = new JLabel(labelText);
        label.setFont(UIHelper.NORMAL_FONT);
        label.setForeground(UIHelper.TEXT_COLOR);

        gbc.gridwidth = 1;

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        panel.add(label, gbc);

        gbc.gridx = 1;
        gbc.gridy = row;
        gbc.weightx = 1;
        panel.add(field, gbc);
    }

    private JPanel createButtonsPanel() {
        JPanel buttonsPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        buttonsPanel.setOpaque(false);

        JButton addRequestButton = UIHelper.createPrimaryButton("Add Request");
        JButton showByBookButton = UIHelper.createPrimaryButton("Show Requests By Book");
        JButton peekNextButton = UIHelper.createPrimaryButton("Peek Next Request");
        JButton serveNextButton = UIHelper.createPrimaryButton("Serve Next Request");
        JButton refreshButton = UIHelper.createPrimaryButton("Refresh Table");
        JButton clearButton = UIHelper.createPrimaryButton("Clear Fields");

        addRequestButton.addActionListener(e -> addRequest());
        showByBookButton.addActionListener(e -> showRequestsByBook());
        peekNextButton.addActionListener(e -> peekNextRequest());
        serveNextButton.addActionListener(e -> serveNextRequest());
        refreshButton.addActionListener(e -> refreshRequestsTable());
        clearButton.addActionListener(e -> clearFields());

        buttonsPanel.add(addRequestButton);
        buttonsPanel.add(showByBookButton);
        buttonsPanel.add(peekNextButton);
        buttonsPanel.add(serveNextButton);
        buttonsPanel.add(refreshButton);
        buttonsPanel.add(clearButton);

        return buttonsPanel;
    }

    private void addRequest() {
        try {
            int requestId = readIntegerField(requestIdField, "Request ID");
            int bookNumber = readIntegerField(bookNumberField, "Book Number");
            String studentName = readTextField(studentNameField, "Student Name");
            String requestDate = readTextField(requestDateField, "Request Date");
            boolean graduatingStudent = graduatingStudentCheckBox.isSelected();

            String message = BookWaitingQueue.addRequest(
                    requestId,
                    bookNumber,
                    studentName,
                    graduatingStudent,
                    requestDate
            );

            if ("Done .".equals(message)) {
                UIHelper.showSuccessMessage(
                        this,
                        "Waiting request added successfully."
                );

                refreshRequestsTable();
                clearFields();
            } else {
                UIHelper.showErrorMessage(this, message);
            }

        } catch (IllegalArgumentException ex) {
            UIHelper.showErrorMessage(this, ex.getMessage());
        }
    }
    private void showRequestsByBook() {
        try {
            int bookNumber = readIntegerField(bookNumberField, "Book Number");

            ArrayList<WaitingRequest> requests = BookWaitingQueue.getRequestsByBookNumber(bookNumber);

            tableModel.setRowCount(0);

            if (requests.isEmpty()) {
                UIHelper.showErrorMessage(this, "There are no waiting requests for this book.");
                return;
            }

            for (WaitingRequest request : requests) {
                addRequestToTable(request);
            }

            UIHelper.showSuccessMessage(this, "Requests found.");

        } catch (IllegalArgumentException ex) {
            UIHelper.showErrorMessage(this, ex.getMessage());
        }
    }

    private void peekNextRequest() {
        try {
            int bookNumber = readIntegerField(bookNumberField, "Book Number");

            WaitingRequest request = BookWaitingQueue.peekNextRequest(bookNumber);

            if (request == null) {
                UIHelper.showErrorMessage(this, "There is no waiting request for this book.");
                return;
            }

            UIHelper.showSuccessMessage(this, buildRequestInfo(request));

        } catch (IllegalArgumentException ex) {
            UIHelper.showErrorMessage(this, ex.getMessage());
        }
    }

    private void serveNextRequest() {
        try {
            int bookNumber = readIntegerField(bookNumberField, "Book Number");

            String validationMessage =
                    BookWaitingQueue.validateServeNextRequest(bookNumber);

            if (!"Done .".equals(validationMessage)) {
                UIHelper.showErrorMessage(this, validationMessage);
                return;
            }

            WaitingRequest nextRequest =
                    BookWaitingQueue.peekNextRequest(bookNumber);

            int choice = UIHelper.showConfirmMessage(
                    this,
                    "The next priority request is:\n\n"
                            + buildRequestInfo(nextRequest)
                            + "\n\nDo you want to create a borrow record for this student?"
            );

            if (choice != UIHelper.YES_OPTION) {
                return;
            }

            String recordIdText = readRequiredDialogText(
                    "Enter a new Borrow Record ID:",
                    "Borrow Record ID"
            );

            if (recordIdText == null) {
                return;
            }

            int recordId = readPositiveIntegerText(
                    recordIdText,
                    "Borrow Record ID"
            );

            String borrowDate = readRequiredDialogText(
                    "Enter Borrow Date (example: 2026-06-21):",
                    "Borrow Date"
            );

            if (borrowDate == null) {
                return;
            }

            String expectedReturnDate = readRequiredDialogText(
                    "Enter Expected Return Date (example: 2026-07-05):",
                    "Expected Return Date"
            );

            if (expectedReturnDate == null) {
                return;
            }

            String message = BookWaitingQueue.serveNextRequest(
                    bookNumber,
                    recordId,
                    borrowDate,
                    expectedReturnDate
            );

            if ("Done .".equals(message)) {
                UIHelper.showSuccessMessage(
                        this,
                        "Request served successfully.\n\n"
                                + "A borrow record was created for:\n"
                                + nextRequest.getStudentName()
                );

                refreshRequestsTable();
                clearFields();
            } else {
                UIHelper.showErrorMessage(this, message);
            }

        } catch (IllegalArgumentException ex) {
            UIHelper.showErrorMessage(this, ex.getMessage());
        }
    }

    public void refreshRequestsTable() {
        tableModel.setRowCount(0);

        ArrayList<WaitingRequest> requests = BookWaitingQueue.getAllRequests();

        for (WaitingRequest request : requests) {
            addRequestToTable(request);
        }
    }

    private void addRequestToTable(WaitingRequest request) {
        tableModel.addRow(new Object[]{
                request.getRequestId(),
                request.getBookNumber(),
                request.getStudentName(),
                request.isGraduatingStudent() ? "Yes" : "No",
                request.getRequestDate()
        });
    }

    private void fillFieldsFromSelectedRow() {
        int selectedRow = requestsTable.getSelectedRow();

        if (selectedRow == -1) {
            return;
        }

        requestIdField.setText(String.valueOf(tableModel.getValueAt(selectedRow, 0)));
        bookNumberField.setText(String.valueOf(tableModel.getValueAt(selectedRow, 1)));
        studentNameField.setText(String.valueOf(tableModel.getValueAt(selectedRow, 2)));
        requestDateField.setText(String.valueOf(tableModel.getValueAt(selectedRow, 4)));

        String graduatingValue = String.valueOf(tableModel.getValueAt(selectedRow, 3));
        graduatingStudentCheckBox.setSelected(graduatingValue.equalsIgnoreCase("Yes"));
    }

    private String buildRequestInfo(WaitingRequest request) {
        return "Request ID: " + request.getRequestId()
                + "\nBook Number: " + request.getBookNumber()
                + "\nStudent Name: " + request.getStudentName()
                + "\nGraduating Student: " + (request.isGraduatingStudent() ? "Yes" : "No")
                + "\nRequest Date: " + request.getRequestDate();
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

    private String readRequiredDialogText(String message, String fieldName) {
        String text = JOptionPane.showInputDialog(this, message);

        if (text == null) {
            return null;
        }

        text = text.trim();

        if (text.isEmpty()) {
            throw new IllegalArgumentException(fieldName + " is required.");
        }

        return text;
    }


    private int readPositiveIntegerText(String text, String fieldName) {
        try {
            int value = Integer.parseInt(text.trim());

            if (value <= 0) {
                throw new IllegalArgumentException(
                        fieldName + " must be greater than 0."
                );
            }

            return value;

        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(
                    fieldName + " must be a valid number."
            );
        }
    }

    private void clearFields() {
        requestIdField.setText("");
        bookNumberField.setText("");
        studentNameField.setText("");
        requestDateField.setText("");
        graduatingStudentCheckBox.setSelected(false);
        requestsTable.clearSelection();
    }
}