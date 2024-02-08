 package expensetrackergui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class ExpenseTrackerGUI extends JFrame {
    private ArrayList<Expense> expenses;
    private DefaultTableModel expenseTableModel;
    private JTextField descriptionField;
    private JTextField amountField;
    private JTextField categoryField;
    private JTable expenseTable;
    private JLabel summaryLabel;
    private Map<String, Double> categoryTotals;

    public ExpenseTrackerGUI() {
        expenses = new ArrayList<>();
        categoryTotals = new HashMap<>();

        setTitle("Expense Tracker");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(3, 2));
        inputPanel.add(new JLabel("Description:"));
        descriptionField = new JTextField();
        inputPanel.add(descriptionField);
        inputPanel.add(new JLabel("Amount:"));
        amountField = new JTextField();
        inputPanel.add(amountField);
        inputPanel.add(new JLabel("Category:"));
        categoryField = new JTextField();
        inputPanel.add(categoryField);

        JButton addButton = new JButton("Add Expense");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addExpense();
            }
        });

        JButton viewExpensesButton = new JButton("View Expenses");
        viewExpensesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewExpenses();
            }
        });

        JButton calculateCategoryTotalButton = new JButton("Calculate Category Total");
        calculateCategoryTotalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculateCategoryTotal();
            }
        });

        summaryLabel = new JLabel("Expense Summary: ");
        JPanel summaryPanel = new JPanel();
        summaryPanel.add(summaryLabel);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(viewExpensesButton);
        buttonPanel.add(calculateCategoryTotalButton);

        expenseTableModel = new DefaultTableModel();
        expenseTableModel.addColumn("Description");
        expenseTableModel.addColumn("Amount");
        expenseTableModel.addColumn("Category");
        expenseTable = new JTable(expenseTableModel);

        JScrollPane tableScrollPane = new JScrollPane(expenseTable);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(inputPanel, BorderLayout.NORTH);
        getContentPane().add(buttonPanel, BorderLayout.CENTER);
        getContentPane().add(summaryPanel, BorderLayout.SOUTH);
        getContentPane().add(tableScrollPane, BorderLayout.EAST);
    }

    private void addExpense() {
        String description = descriptionField.getText();
        String amountText = amountField.getText();
        String category = categoryField.getText();

        try {
            double amount = Double.parseDouble(amountText);
            Expense expense = new Expense(description, amount, category);
            expenses.add(expense);
            Object[] rowData = { description, "Rs. " + amount, category };
            expenseTableModel.addRow(rowData);
            updateSummary(category, amount);
            clearFields();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid amount.", "Invalid Input",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void viewExpenses() {
        JFrame viewExpensesFrame = new JFrame("View Expenses");
        viewExpensesFrame.setSize(600, 300);
        viewExpensesFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        DefaultTableModel viewExpensesTableModel = new DefaultTableModel();
        viewExpensesTableModel.addColumn("Description");
        viewExpensesTableModel.addColumn("Amount");
        viewExpensesTableModel.addColumn("Category");
        JTable viewExpensesTable = new JTable(viewExpensesTableModel);

        for (Expense expense : expenses) {
            Object[] rowData = { expense.getDescription(), "Rs. " + expense.getAmount(), expense.getCategory() };
            viewExpensesTableModel.addRow(rowData);
        }

        JScrollPane viewExpensesScrollPane = new JScrollPane(viewExpensesTable);

        viewExpensesFrame.add(viewExpensesScrollPane);
        viewExpensesFrame.setVisible(true);
    }

    private void calculateCategoryTotal() {
        String targetCategory = JOptionPane.showInputDialog(this,
                "Enter the category to calculate total expenses for:");
        if (targetCategory == null || targetCategory.trim().isEmpty()) {
            return;
        }

        double totalForCategory = 0.0;
        for (Expense expense : expenses) {
            if (expense.getCategory().equalsIgnoreCase(targetCategory)) {
                totalForCategory += expense.getAmount();
            }
        }

        JOptionPane.showMessageDialog(this,
                "Total expenses for category '" + targetCategory + "': Rs. " + totalForCategory);
    }

    private void updateSummary(String category, double amount) {
        categoryTotals.put(category, categoryTotals.getOrDefault(category, 0.0) + amount);
        StringBuilder summaryText = new StringBuilder("Expense Summary:\n");
        for (Map.Entry<String, Double> entry : categoryTotals.entrySet()) {
            summaryText.append(entry.getKey()).append(": Rs. ").append(entry.getValue()).append("\n");
        }
        summaryLabel.setText(summaryText.toString());
    }

    private void clearFields() {
        descriptionField.setText("");
        amountField.setText("");
        categoryField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ExpenseTrackerGUI gui = new ExpenseTrackerGUI();
                gui.setVisible(true);
            }
        });
    }
}

class Expense {
    private String description;
    private double amount;
    private String category;

    public Expense(String description, double amount, String category) {
        this.description = description;
        this.amount = amount;
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public double getAmount() {
        return amount;
    }

    public String getCategory() {
        return category;
    }
}

    
