package FrontEnd;

import BackEnd.Book;
import BackEnd.BookTree;

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

public class BooksPanel extends JPanel {

    private JTable booksTable;
    private DefaultTableModel tableModel;

    private JTextField bookNumberField;
    private JTextField authorField;
    private JTextField copiesField;

    public BooksPanel() {
        initComponents();
        refreshBooksTable();
    }

    private void initComponents() {
        setLayout(new BorderLayout(20, 20));
        setBackground(UIHelper.BACKGROUND_COLOR);
        setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        add(createHeaderPanel(), BorderLayout.NORTH);

        tableModel = new DefaultTableModel(
                new Object[]{
                        "Book Number",
                        "Author",
                        "Total Copies",
                        "Borrowed Copies",
                        "Available Copies"
                },
                0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        booksTable = new JTable(tableModel);
        UIHelper.styleTable(booksTable);
        booksTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        booksTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                fillFieldsFromSelectedRow();
            }
        });

        JScrollPane scrollPane = new JScrollPane(booksTable);

        JPanel rightPanel = new JPanel(new BorderLayout(10, 15));
        rightPanel.setOpaque(false);
        rightPanel.setPreferredSize(new Dimension(340, 0));

        rightPanel.add(createFormPanel(), BorderLayout.NORTH);
        rightPanel.add(createButtonsPanel(), BorderLayout.CENTER);

        add(scrollPane, BorderLayout.CENTER);
        add(rightPanel, BorderLayout.EAST);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("Books Management");
        titleLabel.setFont(UIHelper.PAGE_TITLE_FONT);
        titleLabel.setForeground(UIHelper.TEXT_COLOR);

        JLabel subtitleLabel = new JLabel("Add, search, update, and delete books from the AVL tree.");
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

        bookNumberField = UIHelper.createTextField();
        authorField = UIHelper.createTextField();
        copiesField = UIHelper.createTextField();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 0, 8, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        JLabel sectionTitle = new JLabel("Book Details");
        sectionTitle.setFont(UIHelper.SECTION_TITLE_FONT);
        sectionTitle.setForeground(UIHelper.TEXT_COLOR);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        formPanel.add(sectionTitle, gbc);

        addFormRow(formPanel, gbc, 1, "Book Number", bookNumberField);
        addFormRow(formPanel, gbc, 2, "Author", authorField);
        addFormRow(formPanel, gbc, 3, "Copies", copiesField);

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
        JPanel buttonsPanel = new JPanel(new GridLayout(7, 1, 0, 10));
        buttonsPanel.setOpaque(false);

        JButton addButton = UIHelper.createPrimaryButton("Add Book");
        JButton searchButton = UIHelper.createPrimaryButton("Search Book");
        JButton addCopiesButton = UIHelper.createPrimaryButton("Add Copies");
        JButton deleteCopiesButton = UIHelper.createPrimaryButton("Delete Copies");
        JButton deleteBookButton = UIHelper.createDangerButton("Delete Book");
        JButton refreshButton = UIHelper.createPrimaryButton("Refresh Table");
        JButton clearButton = UIHelper.createPrimaryButton("Clear Fields");

        addButton.addActionListener(e -> addBook());
        searchButton.addActionListener(e -> searchBook());
        addCopiesButton.addActionListener(e -> addCopies());
        deleteCopiesButton.addActionListener(e -> deleteCopies());
        deleteBookButton.addActionListener(e -> deleteBook());
        refreshButton.addActionListener(e -> refreshBooksTable());
        clearButton.addActionListener(e -> clearFields());

        buttonsPanel.add(addButton);
        buttonsPanel.add(searchButton);
        buttonsPanel.add(addCopiesButton);
        buttonsPanel.add(deleteCopiesButton);
        buttonsPanel.add(deleteBookButton);
        buttonsPanel.add(refreshButton);
        buttonsPanel.add(clearButton);

        return buttonsPanel;
    }

    private void addBook() {
        try {
            int bookNumber = readIntegerField(bookNumberField, "Book Number");
            int copies = readIntegerField(copiesField, "Copies");
            String author = authorField.getText().trim();

            String message = BookTree.insert(bookNumber, copies, author);

            if (isSuccessMessage(message)) {
                UIHelper.showSuccessMessage(this, message);
                refreshBooksTable();
                clearFields();
            } else {
                UIHelper.showErrorMessage(this, message);
            }

        } catch (IllegalArgumentException ex) {
            UIHelper.showErrorMessage(this, ex.getMessage());
        }
    }

    private void searchBook() {
        try {
            int bookNumber = readIntegerField(bookNumberField, "Book Number");

            Book book = BookTree.search(bookNumber);

            if (book == null) {
                UIHelper.showErrorMessage(this, "Book not found.");
                return;
            }

            tableModel.setRowCount(0);
            addBookToTable(book);

            authorField.setText(book.getAuthor());
            copiesField.setText(String.valueOf(book.getCopiesNumber()));

            UIHelper.showSuccessMessage(this, "Book found.");

        } catch (IllegalArgumentException ex) {
            UIHelper.showErrorMessage(this, ex.getMessage());
        }
    }

    private void addCopies() {
        try {
            int bookNumber = readIntegerField(bookNumberField, "Book Number");
            int copies = readIntegerField(copiesField, "Copies");

            String message = BookTree.addCopies(bookNumber, copies);

            if (isSuccessMessage(message)) {
                UIHelper.showSuccessMessage(this, message);
                refreshBooksTable();
                clearFields();
            } else {
                UIHelper.showErrorMessage(this, message);
            }

        } catch (IllegalArgumentException ex) {
            UIHelper.showErrorMessage(this, ex.getMessage());
        }
    }

    private void deleteCopies() {
        try {
            int bookNumber = readIntegerField(bookNumberField, "Book Number");
            int copies = readIntegerField(copiesField, "Copies");

            String message = BookTree.deleteCopies(bookNumber, copies);

            if (isSuccessMessage(message)) {
                UIHelper.showSuccessMessage(this, message);
                refreshBooksTable();
                clearFields();
            } else {
                UIHelper.showErrorMessage(this, message);
            }

        } catch (IllegalArgumentException ex) {
            UIHelper.showErrorMessage(this, ex.getMessage());
        }
    }

    private void deleteBook() {
        try {
            int bookNumber = readIntegerField(bookNumberField, "Book Number");

            int choice = UIHelper.showConfirmMessage(
                    this,
                    "Are you sure you want to delete this book?"
            );

            if (choice != UIHelper.YES_OPTION) {
                return;
            }

            String message = BookTree.delete(bookNumber);

            if (isSuccessMessage(message)) {
                UIHelper.showSuccessMessage(this, message);
                refreshBooksTable();
                clearFields();
            } else {
                UIHelper.showErrorMessage(this, message);
            }

        } catch (IllegalArgumentException ex) {
            UIHelper.showErrorMessage(this, ex.getMessage());
        }
    }

    public void refreshBooksTable() {
        tableModel.setRowCount(0);

        ArrayList<Book> books = BookTree.getBooks();

        for (Book book : books) {
            addBookToTable(book);
        }
    }

    private void addBookToTable(Book book) {
        tableModel.addRow(new Object[]{
                book.getBookNumber(),
                book.getAuthor(),
                book.getCopiesNumber(),
                book.getBorrowedCopies(),
                book.getCopiesNumber() - book.getBorrowedCopies()
        });
    }

    private void fillFieldsFromSelectedRow() {
        int selectedRow = booksTable.getSelectedRow();

        if (selectedRow == -1) {
            return;
        }

        bookNumberField.setText(String.valueOf(tableModel.getValueAt(selectedRow, 0)));
        authorField.setText(String.valueOf(tableModel.getValueAt(selectedRow, 1)));
        copiesField.setText(String.valueOf(tableModel.getValueAt(selectedRow, 2)));
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

    private boolean isSuccessMessage(String message) {
        if (message == null) {
            return false;
        }

        return message.toLowerCase().contains("done");
    }

    private void clearFields() {
        bookNumberField.setText("");
        authorField.setText("");
        copiesField.setText("");
        booksTable.clearSelection();
    }
}