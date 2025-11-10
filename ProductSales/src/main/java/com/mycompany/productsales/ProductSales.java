

package com.mycompany.productsales;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;
public class ProductSales {
 private JFrame frame;
    private JTextField year1TextField, year2TextField, year3TextField;
    private JTextArea resultArea;
    private JButton loadButton, saveButton, calculateButton;
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
        // Create the main frame of the application
        frame = new JFrame("Product Sales Main Page");
        frame.setBounds(100, 100, 600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout(0, 0));

        // Panel for input fields and labels
        JPanel inputPanel = new JPanel();
        frame.getContentPane().add(inputPanel, BorderLayout.NORTH);
        inputPanel.setLayout(new GridLayout(4, 2, 10, 10));

        // Labels and Text Fields for Yearly Sales Data Input
        inputPanel.add(new JLabel("Year 1 Sales (comma-separated): "));
        year1TextField = new JTextField();
        inputPanel.add(year1TextField);

        inputPanel.add(new JLabel("Year 2 Sales (comma-separated): "));
        year2TextField = new JTextField();
        inputPanel.add(year2TextField);

        inputPanel.add(new JLabel("Year 3 Sales (comma-separated): "));
        year3TextField = new JTextField();
        inputPanel.add(year3TextField);

        // Panel for buttons (Load, Save, Calculate)
        JPanel buttonPanel = new JPanel();
        frame.getContentPane().add(buttonPanel, BorderLayout.CENTER);
        buttonPanel.setLayout(new GridLayout(1, 3, 10, 10));

        // Load Button to load sales data from a file
        loadButton = new JButton("Load");
        loadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadSalesData();
            }
        });
        buttonPanel.add(loadButton);

        // Save Button to save entered sales data to a file
        saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveSalesData();
            }
        });
        buttonPanel.add(saveButton);

        // Calculate Button to calculate total, average, max, and min sales
        calculateButton = new JButton("Calculate");
        calculateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                calculateSalesData();
            }
        });
        buttonPanel.add(calculateButton);

        // Text area to display results (total, average, etc.)
        resultArea = new JTextArea();
        resultArea.setEditable(false);
        frame.getContentPane().add(new JScrollPane(resultArea), BorderLayout.SOUTH);
    }

    // Method to load sales data from a file
    private void loadSalesData() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(frame);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try (BufferedReader br = new BufferedReader(new FileReader(selectedFile))) {
                String line;
                List<int[]> loadedData = new ArrayList<>();
                while ((line = br.readLine()) != null) {
                    String[] values = line.split(",");
                    int[] sales = new int[values.length];
                    for (int i = 0; i < values.length; i++) {
                        sales[i] = Integer.parseInt(values[i].trim());
                    }
                    loadedData.add(sales);
                }
                // Convert List<int[]> to 2D array
                salesData = loadedData.toArray(new int[0][]);
                // Update text fields with loaded data (assuming 3 years)
                if (salesData.length >= 1) year1TextField.setText(arrayToString(salesData[0]));
                if (salesData.length >= 2) year2TextField.setText(arrayToString(salesData[1]));
                if (salesData.length >= 3) year3TextField.setText(arrayToString(salesData[2]));
                resultArea.setText("Sales Data Loaded Successfully.");
            } catch (IOException | NumberFormatException e) {
                e.printStackTrace();
                resultArea.setText("Error loading sales data.");
            }
        }
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

    // Helper method to convert int[] to comma-separated string
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

        salesData = new int[3][];
        salesData[0] = new int[salesYear1.length];
        salesData[1] = new int[salesYear2.length];
        salesData[2] = new int[salesYear3.length];

        for (int i = 0; i < salesYear1.length; i++) {
            salesData[0][i] = Integer.parseInt(salesYear1[i].trim());
        }

        for (int i = 0; i < salesYear2.length; i++) {
            salesData[1][i] = Integer.parseInt(salesYear2[i].trim());
        }

        for (int i = 0; i < salesYear3.length; i++) {
            salesData[2][i] = Integer.parseInt(salesYear3[i].trim());
        }
    }
}
        
    

