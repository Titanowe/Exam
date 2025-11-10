import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class ProductSalesMainPage {
    private JFrame frame;
    private JTextField year1TextField, year2TextField, year3TextField;
    private JTextArea resultArea;
    private int[][] salesData;
    private final int SUCCESS_THRESHOLD = 500; // Success threshold for a product

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                ProductSalesMainPage window = new ProductSalesMainPage();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public ProductSalesMainPage() {
        initialize();
    }

    private void initialize() {
        frame = createFrame();
        frame.setContentPane(createMainPanel());
        frame.pack();
    }

    private JFrame createFrame() {
        JFrame frame = new JFrame("Product Sales Main Page");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(10, 10));
        return frame;
    }

    private JPanel createMainPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Input panel
        JPanel inputPanel = createInputPanel();
        panel.add(inputPanel, BorderLayout.NORTH);

        // Buttons panel
        JPanel buttonPanel = createButtonPanel();
        panel.add(buttonPanel, BorderLayout.CENTER);

        // Results area
        resultArea = new JTextArea(5, 30);
        resultArea.setEditable(false);
        panel.add(new JScrollPane(resultArea), BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createInputPanel() {
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(4, 2, 10, 10));

        inputPanel.add(new JLabel("Year 1 Sales:"));
        year1TextField = new JTextField();
        inputPanel.add(year1TextField);

        inputPanel.add(new JLabel("Year 2 Sales:"));
        year2TextField = new JTextField();
        inputPanel.add(year2TextField);

        inputPanel.add(new JLabel("Year 3 Sales:"));
        year3TextField = new JTextField();
        inputPanel.add(year3TextField);

        return inputPanel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 10, 10));

        // Load Button
        JButton loadButton = createButton("Load", e -> loadSalesData());
        buttonPanel.add(loadButton);

        // Save Button
        JButton saveButton = createButton("Save", e -> saveSalesData());
        buttonPanel.add(saveButton);

        // Calculate Button
        JButton calculateButton = createButton("Calculate", e -> calculateSalesData());
        buttonPanel.add(calculateButton);

        return buttonPanel;
    }

    // Helper to create a button with action listener and mouse hover effect
    private JButton createButton(String label, ActionListener listener) {
        JButton button = new JButton(label);
        button.addActionListener(listener);

        // Mouse Hover Effect: Change button color on hover
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(Color.CYAN); // On hover, change background
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(UIManager.getColor("Button.background")); // Reset background color
            }
        });
        return button;
    }

    // Method to load sales data from a file
    private void loadSalesData() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(frame);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try (BufferedReader br = new BufferedReader(new FileReader(selectedFile))) {
                List<int[]> loadedData = new ArrayList<>();
                String line;
                while ((line = br.readLine()) != null) {
                    String[] values = line.split(",");
                    int[] sales = new int[values.length];
                    for (int i = 0; i < values.length; i++) {
                        sales[i] = Integer.parseInt(values[i].trim());
                    }
                    loadedData.add(sales);
                }
                salesData = loadedData.toArray(new int[0][]);
                fillTextFields();
                resultArea.setText("Sales Data Loaded Successfully.");
            } catch (IOException | NumberFormatException e) {
                e.printStackTrace();
                resultArea.setText("Error loading sales data.");
            }
        }
    }

    // Method to fill text fields with loaded data
    private void fillTextFields() {
        if (salesData.length >= 1) year1TextField.setText(arrayToString(salesData[0]));
        if (salesData.length >= 2) year2TextField.setText(arrayToString(salesData[1]));
        if (salesData.length >= 3) year3TextField.setText(arrayToString(salesData[2]));
    }

    // Method to save sales data to a file
    private void saveSalesData() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showSaveDialog(frame);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(selectedFile))) {
                for (int[] year : salesData) {
                    String line = arrayToString(year);
                    bw.write(line);
                    bw.newLine();
                }
                resultArea.setText("Sales Data Saved Successfully.");
            } catch (IOException e) {
                e.printStackTrace();
                resultArea.setText("Error saving sales data.");
            }
        }
    }

    // Helper to convert int[] to comma-separated string
    private String arrayToString(int[] array) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            sb.append(array[i]);
            if (i < array.length - 1) sb.append(", ");
        }
        return sb.toString();
    }

    // Method to calculate and display the average sales, sales over and under the threshold
    private void calculateSalesData() {
        parseSalesData();
        int totalSales = 0, count = 0, salesOverLimit = 0, salesUnderLimit = 0;
        double totalAverage = 0;

        for (int[] year : salesData) {
            int yearTotal = 0;
            for (int sale : year) {
                totalSales += sale;
                yearTotal += sale;
                if (sale >= SUCCESS_THRESHOLD) salesOverLimit++;
                else salesUnderLimit++;
            }
            double yearAverage = (double) yearTotal / year.length;
            totalAverage += yearAverage;
            count++;
        }

        totalAverage /= count;

        resultArea.setText(String.format(
            "Total Sales: %d\n" +
            "Average Sales (across all years): %.2f\n" +
            "Sales Over %d: %d\n" +
            "Sales Under %d: %d",
            totalSales, totalAverage, SUCCESS_THRESHOLD, salesOverLimit, SUCCESS_THRESHOLD, salesUnderLimit
        ));
    }

    // Method to parse sales data from text fields and convert it into a 2D array
    private void parseSalesData() {
        String inputYear1 = year1TextField.getText().trim();
        String inputYear2 = year2TextField.getText().trim();
        String inputYear3 = year3TextField.getText().trim();

        String[] salesYear1 = inputYear1.split(",");
        String[] salesYear2 = inputYear2.split(",");
        String[] salesYear3 = inputYear3.split(",");

        salesData =
