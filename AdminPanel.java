import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.util.LinkedList;

class Book {
    String title;
    String author;
    String synopsis;
    int numOfPages;
    String genre;
    ImageIcon coverImage;

    Book(String title, String author, String synopsis, int numOfPages, String genre, ImageIcon coverImage) {
        this.title = title;
        this.author = author;
        this.synopsis = synopsis;
        this.numOfPages = numOfPages;
        this.genre = genre;
        this.coverImage = coverImage;
    }
}

public class AdminPanel extends JFrame {
    private LinkedList<Book> bookList = new LinkedList<>();
    private JTextField titleField, authorField, numOfPagesField;
    private JTextArea synopsisField;
    private JButton addImageButton, submitButton, editButton, deleteButton, clearButton, backButton;
    private JTextArea bookDisplay;
    private JPanel bookDetailsPanel;
    private JComboBox<String> genreComboBox;

    public AdminPanel() {
        setTitle("AdminPanel");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(8, 2));
        inputPanel.add(new JLabel("Title: "));
        titleField = new JTextField(20);
        inputPanel.add(titleField);
        inputPanel.add(new JLabel("Author: "));
        authorField = new JTextField(20);
        inputPanel.add(authorField);

        numOfPagesField = new JTextField(6); // Maximum of 6 characters
        inputPanel.add(new JLabel("Number of Pages: "));
        inputPanel.add(numOfPagesField);

        inputPanel.add(new JLabel("Genre: "));
        String[] genres = {"Mystery", "Science Fiction", "Fantasy", "Historical Fiction", "Romance", "Non-Fiction"};
        genreComboBox = new JComboBox<>(genres);
        inputPanel.add(genreComboBox);
        inputPanel.add(new JLabel("Synopsis: "));
        synopsisField = new JTextArea(4, 20);
        inputPanel.add(new JScrollPane(synopsisField));

        addImageButton = new JButton("Add Image");
        inputPanel.add(addImageButton);

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Image files", "jpg", "jpeg", "png", "gif"));

        addImageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    ImageIcon image = new ImageIcon(selectedFile.getAbsolutePath());
                    displayBookDetails(titleField.getText(), authorField.getText(), synopsisField.getText(), numOfPagesField.getText(), (String) genreComboBox.getSelectedItem(), image);
                    submitButton.setEnabled(true);
                }
            }
        });

        submitButton = new JButton("Submit");
        editButton = new JButton("Edit");
        deleteButton = new JButton("Delete");
        clearButton = new JButton("Clear");
        backButton = new JButton("Exit");

        submitButton.setEnabled(false);
        editButton.setEnabled(false);
        deleteButton.setEnabled(false);
        clearButton.setEnabled(true);

        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String title = titleField.getText();
                String author = authorField.getText();
                String synopsis = synopsisField.getText();
                String pagesText = numOfPagesField.getText().trim();
                int numOfPages = pagesText.isEmpty() ? 0 : Integer.parseInt(pagesText);
                String genre = (String) genreComboBox.getSelectedItem();
                ImageIcon coverImage = getCoverImage();
                bookList.add(new Book(title, author, synopsis, numOfPages, genre, coverImage));
                JOptionPane.showMessageDialog(null, "Book submitted!");
                clearFields();
            }
        });

        editButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Implement the edit functionality here
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Implement the delete functionality here
            }
        });

        clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                titleField.setText("");
                authorField.setText("");
                numOfPagesField.setText("");
                genreComboBox.setSelectedIndex(0);
                synopsisField.setText("");
                submitButton.setEnabled(false);
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

        add(inputPanel, BorderLayout.NORTH);

        bookDisplay = new JTextArea();
        bookDisplay.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(bookDisplay);
        add(scrollPane, BorderLayout.CENTER);

        bookDetailsPanel = new JPanel();
        bookDetailsPanel.setLayout(new BorderLayout());
        add(bookDetailsPanel, BorderLayout.SOUTH);

        setSize(400, 400);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void displayBookDetails(String title, String author, String synopsis, String numOfPages, String genre, ImageIcon coverImage) {
        bookDetailsPanel.removeAll();

        JTextArea bookDetailsTextArea = new JTextArea();
        bookDetailsTextArea.setWrapStyleWord(true);
        bookDetailsTextArea.setLineWrap(true);
        bookDetailsTextArea.setEditable(false);
        bookDetailsTextArea.append("Title: " + title + "\nAuthor: " + author + "\nNum of Pages: " + numOfPages + "\nGenre: " + genre + "\nSynopsis:\n" + synopsis);

        JScrollPane scrollPane = new JScrollPane(bookDetailsTextArea);
        bookDetailsPanel.add(scrollPane, BorderLayout.CENTER);

        if (coverImage != null) {
            int maxWidth = 200;
            int maxHeight = 300;
            int imgWidth = coverImage.getIconWidth();
            int imgHeight = coverImage.getIconHeight();
            if (imgWidth > maxWidth || imgHeight > maxHeight) {
                if (imgWidth * maxHeight > maxWidth * imgHeight) {
                    imgWidth = maxWidth;
                    imgHeight = maxHeight * imgHeight / imgWidth;
                } else {
                    imgHeight = maxHeight;
                    imgWidth = maxWidth * imgWidth / imgHeight;
                }
            }
            ImageIcon scaledImage = new ImageIcon(coverImage.getImage().getScaledInstance(imgWidth, imgHeight, Image.SCALE_SMOOTH));
            JLabel coverImageLabel = new JLabel(scaledImage);
            bookDetailsPanel.add(coverImageLabel, BorderLayout.NORTH);
        }

        bookDetailsPanel.revalidate();
    }

    private ImageIcon getCoverImage() {
        // Implement this if you want to get the cover image from a different source
        // You can return the currently selected cover image from your code
        return null;
    }

    private void clearFields() {
        titleField.setText("");
        authorField.setText("");
        numOfPagesField.setText("");
        genreComboBox.setSelectedIndex(0);
        synopsisField.setText("");
        submitButton.setEnabled(false);
        editButton.setEnabled(false);
        deleteButton.setEnabled(false);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new AdminPanel();
        });
    }
}
