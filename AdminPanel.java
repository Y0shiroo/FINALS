import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;

class Book {
    String title;
    String author;
    String synopsis;
    int numOfPages;
    String genre;

    Book(String title, String author, String synopsis, int numOfPages, String genre) {
        this.title = title;
        this.author = author;
        this.synopsis = synopsis;
        this.numOfPages = numOfPages;
        this.genre = genre;
    }
}

public class AdminPanel extends JFrame {
    private LinkedList<Book> books = new LinkedList<>();
    private JTextField titleField, authorField, pagesField;
    private JTextArea synopsisField;
    private JButton submitButton, editButton, deleteButton, clearButton, backButton;
    private JTextArea bookDisplay;
    private JComboBox<String> genreComboBox;

    private JTable bookTable;
    private DefaultTableModel tableModel;

    private int selectedRowIndex = -1; // Keep track of the selected row index for editing

    public AdminPanel() {
        setTitle("AdminPanel");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create a JSplitPane to divide the frame into two sections
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(400); // Set the initial divider location

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(0, 2));

        addFormField(inputPanel, "Title:", titleField = new JTextField(20));
        addFormField(inputPanel, "Author:", authorField = new JTextField(20));

        // Number of Pages field with character limit
        pagesField = new JTextField(6);
        pagesField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) || pagesField.getText().length() >= 6) {
                    e.consume();
                }
            }
        });
        addFormField(inputPanel, "Number of Pages:", pagesField);

        addFormField(inputPanel, "Genre:", genreComboBox = new JComboBox<>(new String[]{"Mystery", "Science Fiction", "Fantasy", "Historical Fiction", "Romance", "Non-Fiction"}));
        addFormField(inputPanel, "Synopsis:", new JScrollPane(synopsisField = new JTextArea(4, 20)));

        splitPane.setLeftComponent(inputPanel);

        // Initialize the book table
        initBookTable();
        splitPane.setRightComponent(new JScrollPane(bookTable));

        add(splitPane, BorderLayout.CENTER);

        submitButton = new JButton("Submit");
        editButton = new JButton("Edit");
        deleteButton = new JButton("Delete");
        clearButton = new JButton("Clear");
        backButton = new JButton("Exit");

        submitButton.setEnabled(true);
        editButton.setEnabled(false);
        deleteButton.setEnabled(false);
        clearButton.setEnabled(true);

        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String title = titleField.getText();
                String author = authorField.getText();
                String synopsis = synopsisField.getText();
                String pagesText = pagesField.getText().trim();
                int numOfPages = pagesText.isEmpty() ? 0 : Integer.parseInt(pagesText);
                String genre = (String) genreComboBox.getSelectedItem();
                books.add(new Book(title, author, synopsis, numOfPages, genre));
                tableModel.addRow(new Object[]{title, author, genre, pagesText});
                JOptionPane.showMessageDialog(null, "Book submitted!");
                clearFields();
            }
        });

        editButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (selectedRowIndex != -1) {
                    // Get the edited data from the text fields
                    String title = titleField.getText();
                    String author = authorField.getText();
                    String synopsis = synopsisField.getText();
                    String pagesText = pagesField.getText().trim();
                    int numOfPages = pagesText.isEmpty() ? 0 : Integer.parseInt(pagesText);
                    String genre = (String) genreComboBox.getSelectedItem();

                    // Update the book in the list and the table
                    Book book = books.get(selectedRowIndex);
                    book.title = title;
                    book.author = author;
                    book.synopsis = synopsis;
                    book.numOfPages = numOfPages;
                    book.genre = genre;
                    tableModel.setValueAt(title, selectedRowIndex, 0);
                    tableModel.setValueAt(author, selectedRowIndex, 1);
                    tableModel.setValueAt(genre, selectedRowIndex, 2);
                    tableModel.setValueAt(pagesText, selectedRowIndex, 3);

                    JOptionPane.showMessageDialog(null, "Book successfully edited!");
                    clearFields();
                    selectedRowIndex = -1;
                    editButton.setEnabled(false);

                    // Save data to a file here
                    // saveDataToFile();
                }
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (selectedRowIndex != -1) {
                    books.remove(selectedRowIndex);
                    tableModel.removeRow(selectedRowIndex);
                    JOptionPane.showMessageDialog(null, "Book successfully deleted!");
                    clearFields();
                    selectedRowIndex = -1;
                    editButton.setEnabled(false);
                    deleteButton.setEnabled(false);
                }
            }
        });

        clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clearFields();
                selectedRowIndex = -1;
                editButton.setEnabled(false);
                deleteButton.setEnabled(false);
            }
        });

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Implement navigation back to a previous window or screen
            }
        });

        inputPanel.add(submitButton);
        inputPanel.add(editButton);
        inputPanel.add(deleteButton);
        inputPanel.add(clearButton);
        inputPanel.add(backButton);

        bookDisplay = new JTextArea();
        bookDisplay.setEditable(false);
        add(bookDisplay, BorderLayout.SOUTH);

        // Add a ListSelectionListener to the book table to track selected rows
        bookTable.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = bookTable.getSelectedRow();
            if (selectedRow != -1) {
                // Populate the text fields with the selected row's data
                titleField.setText(tableModel.getValueAt(selectedRow, 0).toString());
                authorField.setText(tableModel.getValueAt(selectedRow, 1).toString());
                genreComboBox.setSelectedItem(tableModel.getValueAt(selectedRow, 2).toString());
                pagesField.setText(tableModel.getValueAt(selectedRow, 3).toString());
                synopsisField.setText(books.get(selectedRow).synopsis);

                // Enable the edit and delete buttons
                selectedRowIndex = selectedRow;
                editButton.setEnabled(true);
                deleteButton.setEnabled(true);
            }
        });

        setSize(800, 400);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initBookTable() {
        String[] columnNames = {"Title", "Author", "Genre", "Number of Pages"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make the table uneditable
            }
        };
        bookTable = new JTable(tableModel);
        bookTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        bookTable.setAutoCreateRowSorter(true);
    }

    private void addFormField(JPanel panel, String label, Component field) {
        panel.add(new JLabel(label));
        panel.add(field);
    }

    private void clearFields() {
        titleField.setText("");
        authorField.setText("");
        pagesField.setText("");
        genreComboBox.setSelectedIndex(0);
        synopsisField.setText("");
        submitButton.setEnabled(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new AdminPanel();
        });
    }
}
