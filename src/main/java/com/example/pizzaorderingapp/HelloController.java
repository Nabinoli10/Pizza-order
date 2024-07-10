package com.example.pizzaorderingapp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.*;

public class HelloController {

    @FXML
    private TextField nameInput;
    @FXML
    private TextField mobileInput;
    @FXML
    private CheckBox xlSize;
    @FXML
    private CheckBox lSize;
    @FXML
    private CheckBox mSize;
    @FXML
    private CheckBox sSize;
    @FXML
    private TextField toppingsInput;
    @FXML
    private TableView<PizzaOrder> table;
    @FXML
    private TableColumn<PizzaOrder, String> nameColumn;
    @FXML
    private TableColumn<PizzaOrder, String> mobileColumn;
    @FXML
    private TableColumn<PizzaOrder, String> sizeColumn;
    @FXML
    private TableColumn<PizzaOrder, Integer> toppingsColumn;
    @FXML
    private TableColumn<PizzaOrder, Double> totalBillColumn;

    private final ObservableList<PizzaOrder> orders;

    public HelloController() {
        orders = FXCollections.observableArrayList();
    }

    @FXML
    public void initialize() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        mobileColumn.setCellValueFactory(new PropertyValueFactory<>("mobileNumber"));
        sizeColumn.setCellValueFactory(new PropertyValueFactory<>("size"));
        toppingsColumn.setCellValueFactory(new PropertyValueFactory<>("toppings"));
        totalBillColumn.setCellValueFactory(new PropertyValueFactory<>("totalBill"));

        fetchOrdersFromDatabase();
        table.setItems(orders);
    }

    @FXML
    public void handleAddButtonAction() {
        String size = getSelectedSize();
        if (size == null) {
            showAlert("Size selection is required.");
            return;
        }

        int toppings = parseToppingsInput();
        double totalBill = calculateTotalBill(size, toppings);

        PizzaOrder order = new PizzaOrder(nameInput.getText(), mobileInput.getText(), size, toppings, totalBill);
        insertOrderIntoDatabase(order);
        orders.add(order);
        clearInputs();
    }

    @FXML
    public void handleUpdateButtonAction() {
        PizzaOrder selectedOrder = table.getSelectionModel().getSelectedItem();
        if (selectedOrder != null) {
            String size = getSelectedSize();
            if (size == null) {
                showAlert("Size selection is required.");
                return;
            }

            int toppings = parseToppingsInput();
            double totalBill = calculateTotalBill(size, toppings);

            selectedOrder.setCustomerName(nameInput.getText());
            selectedOrder.setMobileNumber(mobileInput.getText());
            selectedOrder.setSize(size);
            selectedOrder.setToppings(toppings);
            selectedOrder.setTotalBill(totalBill);

            updateOrderInDatabase(selectedOrder);
            table.refresh();
            clearInputs();
        } else {
            showAlert("Select an order to update.");
        }
    }

    @FXML
    public void handleDeleteButtonAction() {
        PizzaOrder selectedOrder = table.getSelectionModel().getSelectedItem();
        if (selectedOrder != null) {
            deleteOrderFromDatabase(selectedOrder);
            orders.remove(selectedOrder);
        } else {
            showAlert("Select an order to delete.");
        }
    }

    @FXML
    public void handleClearButtonAction() {
        clearInputs();
    }

    @FXML
    public void handleListButtonAction() {
        fetchOrdersFromDatabase();
        table.setItems(orders);
    }

    private void fetchOrdersFromDatabase() {
        orders.clear();

        String jdbcUrl = "jdbc:mysql://localhost:3306/pizzaorderingapp";
        String dbUser = "root";
        String dbPassword = "";

        try (Connection connection = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword)) {
            String query = "SELECT * FROM pizzaorders";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                String customerName = resultSet.getString("customerName");
                String mobileNumber = resultSet.getString("mobileNumber");
                String size = resultSet.getString("size");
                int toppings = resultSet.getInt("toppings");
                double totalBill = resultSet.getDouble("totalBill");

                PizzaOrder order = new PizzaOrder(customerName, mobileNumber, size, toppings, totalBill);
                orders.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void insertOrderIntoDatabase(PizzaOrder order) {
        String jdbcUrl = "jdbc:mysql://localhost:3306/pizzaorderingapp";
        String dbUser = "root";
        String dbPassword = "";

        try (Connection connection = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword)) {
            String query = "INSERT INTO pizzaorders (customerName, mobileNumber, size, toppings, totalBill) " +
                    "VALUES (?, ?, ?, ?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, order.getCustomerName());
            preparedStatement.setString(2, order.getMobileNumber());
            preparedStatement.setString(3, order.getSize());
            preparedStatement.setInt(4, order.getToppings());
            preparedStatement.setDouble(5, order.getTotalBill());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();

        }
    }

    private void updateOrderInDatabase(PizzaOrder order) {
        String jdbcUrl = "jdbc:mysql://localhost:3306/pizzaorderingapp";
        String dbUser = "root";
        String dbPassword = "";

        try (Connection connection = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword)) {
            String query = "UPDATE pizzaorders SET customerName=?, mobileNumber=?, size=?, toppings=?, totalBill=? " +
                    "WHERE customer_name=? AND mobile_number=? AND pizza_size=?"; // Assuming a unique key combination

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, order.getCustomerName());
            preparedStatement.setString(2, order.getMobileNumber());
            preparedStatement.setString(3, order.getSize());
            preparedStatement.setInt(4, order.getToppings());
            preparedStatement.setDouble(5, order.getTotalBill());
            preparedStatement.setString(6, order.getCustomerName());
            preparedStatement.setString(7, order.getMobileNumber());
            preparedStatement.setString(8, order.getSize());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error updating order in database.");
        }
    }

    private void deleteOrderFromDatabase(PizzaOrder order) {
        String jdbcUrl = "jdbc:mysql://localhost:3306/pizzaorderingapp";
        String dbUser = "root";
        String dbPassword = "";

        try (Connection connection = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword)) {
            String query = "DELETE FROM pizzaorders WHERE customerName=? AND mobileNumber=? AND size=?";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, order.getCustomerName());
            preparedStatement.setString(2, order.getMobileNumber());
            preparedStatement.setString(3, order.getSize());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();

        }
    }

    private String getSelectedSize() {
        if (xlSize.isSelected()) return "XL";
        if (lSize.isSelected()) return "L";
        if (mSize.isSelected()) return "M";
        if (sSize.isSelected()) return "S";
        return null;
    }

    private int parseToppingsInput() {
        try {
            return Integer.parseInt(toppingsInput.getText());
        } catch (NumberFormatException e) {
            showAlert("Invalid input for toppings.");
            return 0;
        }
    }

    public double calculateTotalBill(String size, int toppings) {
        double basePrice = switch (size) {
            case "XL" -> 15.00;
            case "L" -> 12.00;
            case "M" -> 10.00;
            case "S" -> 8.00;
            default -> 0;
        };

        double toppingsCost = toppings * 1.50;
        double subtotal = basePrice + toppingsCost;
        double hst = subtotal * 0.15;
        return subtotal + hst;
    }

    private void clearInputs() {
        nameInput.clear();
        mobileInput.clear();
        xlSize.setSelected(false);
        lSize.setSelected(false);
        mSize.setSelected(false);
        sSize.setSelected(false);
        toppingsInput.clear();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
