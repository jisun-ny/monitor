package com.delivery.monitor.tables;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface TableMapper {

    @Update("DELETE FROM OrderDetails;")
    void deleteOrderDetails();

    @Update("ALTER TABLE OrderDetails AUTO_INCREMENT = 1;")
    void resetOrderDetailsAutoIncrement();

    @Update("DELETE FROM Baskets;")
    void deleteBaskets();

    @Update("ALTER TABLE Baskets AUTO_INCREMENT = 1;")
    void resetBasketsAutoIncrement();

    @Update("DELETE FROM Recalls;")
    void deleteRecalls();

    @Update("ALTER TABLE Recalls AUTO_INCREMENT = 1;")
    void resetRecallsAutoIncrement();

    @Update("DELETE FROM Deliveries;")
    void deleteDeliveries();

    @Update("ALTER TABLE Deliveries AUTO_INCREMENT = 1;")
    void resetDeliveriesAutoIncrement();

    @Update("DELETE FROM Orders;")
    void deleteOrders();

    @Update("ALTER TABLE Orders AUTO_INCREMENT = 1;")
    void resetOrdersAutoIncrement();

    @Update("DELETE FROM Products;")
    void deleteProducts();

    @Update("ALTER TABLE Products AUTO_INCREMENT = 1;")
    void resetProductsAutoIncrement();

    @Update("DELETE FROM Admins;")
    void deleteAdmins();

    @Update("ALTER TABLE Admins AUTO_INCREMENT = 1;")
    void resetAdminsAutoIncrement();
}
