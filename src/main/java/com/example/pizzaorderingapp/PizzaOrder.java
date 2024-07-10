package com.example.pizzaorderingapp;

public class PizzaOrder {
    private String customerName;
    private String mobileNumber;
    private String size;
    private int toppings;
    private double totalBill;

    public PizzaOrder(String customerName, String mobileNumber, String size, int toppings, double totalBill) {
        this.customerName = customerName;
        this.mobileNumber = mobileNumber;
        this.size = size;
        this.toppings = toppings;
        this.totalBill = totalBill;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public int getToppings() {
        return toppings;
    }

    public void setToppings(int toppings) {
        this.toppings = toppings;
    }

    public double getTotalBill() {
        return totalBill;
    }

    public void setTotalBill(double totalBill) {
        this.totalBill = totalBill;
    }

    public double calculateTotalBill() {
        double basePrice;

        switch (size) {
            case "XL":
                basePrice = 15.00;
                break;
            case "L":
                basePrice = 12.00;
                break;
            case "M":
                basePrice = 10.00;
                break;
            case "S":
                basePrice = 8.00;
                break;
            default:
                basePrice = 0;
                break;
        }

        double toppingsCost = toppings * 1.50;
        double subtotal = basePrice + toppingsCost;
        double hst = subtotal * 0.15;
        return subtotal + hst;
    }
}
