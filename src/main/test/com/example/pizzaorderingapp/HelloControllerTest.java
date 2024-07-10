package com.example.pizzaorderingapp;

import com.example.pizzaorderingapp.HelloController;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class HelloControllerTest {

    @Test
    public void testCalculateTotalBillXL() {
        HelloController controller = new HelloController();


        double totalBill = controller.calculateTotalBill("XL", 3);
        assertEquals(15.00 + 3 * 1.50 + (15.00 + 3 * 1.50) * 0.15, totalBill, 0.01);
    }

    @Test
    public void testCalculateTotalBillL() {
        HelloController controller = new HelloController();


        double totalBill = controller.calculateTotalBill("L", 2);
        assertEquals(12.00 + 2 * 1.50 + (12.00 + 2 * 1.50) * 0.15, totalBill, 0.01);
    }

    @Test
    public void testCalculateTotalBillM() {
        HelloController controller = new HelloController();


        double totalBill = controller.calculateTotalBill("M", 1);
        assertEquals(10.00 + 1 * 1.50 + (10.00 + 1 * 1.50) * 0.15, totalBill, 0.01);
    }

    @Test
    public void testCalculateTotalBillS() {
        HelloController controller = new HelloController();


        double totalBill = controller.calculateTotalBill("S", 0);
        assertEquals(8.00 + (8.00) * 0.15, totalBill, 0.01);
    }

    @Test
    public void testCalculateTotalBillInvalidSize() {
        HelloController controller = new HelloController();


        double totalBill = controller.calculateTotalBill("XL", 3);
        assertEquals(0.0, totalBill, 0.01);
    }
}
