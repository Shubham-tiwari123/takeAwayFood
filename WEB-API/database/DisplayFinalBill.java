package com.takeawayfood.database;

import com.mysql.jdbc.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.takeawayfood.extraclasses.FinalBillDetails;
import java.sql.Time;
import java.util.LinkedList;
import java.util.List;

public class DisplayFinalBill extends DbConnection{
    
    public int orderStatus(int orderId){
        int ordersStatus = 0 ;
        try {
            initializeDbConnection();
            resultSet = (ResultSet) smt.executeQuery("SELECT * FROM OrderTable WHERE OrderId = "+orderId);
            while (resultSet.next()) {
                ordersStatus = resultSet.getInt("OrderStatus");
            }
            if(ordersStatus==1)
                return 1;
            else if(ordersStatus==2)
                return 2;
            else if(ordersStatus==0)
                return 0;
        } catch (SQLException ex) {
            Logger.getLogger(DisplayFinalBill.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            closeDbConnection(connection);
        }
        return 2;
    }
    
    public List<FinalBillDetails>  displayFinallBill(int orderId){
        List<FinalBillDetails> billDetails = new LinkedList<>();
        
        try {
            initializeDbConnection();
            resultSet = (ResultSet) smt.executeQuery("SELECT * FROM OrderItemTable WHERE OrderId = "+orderId);
            while(resultSet.next()){
                String itemName = resultSet.getString("itemName");
                int itemQnt = resultSet.getInt("itemQnt");
                int itemPrice = resultSet.getInt("itemPrice");
                FinalBillDetails details = new FinalBillDetails();
                details.itemName = itemName;
                details.itemPrice = itemPrice;
                details.itemQnt = itemQnt;
                billDetails.add(details);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DisplayFinalBill.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            closeDbConnection(connection);
        }
        return billDetails;
    }
    
     public List<FinalBillDetails>  extraBillDetails(int orderId){
        List<FinalBillDetails> billDetails = new LinkedList<>();
        
        try {
            initializeDbConnection();
            resultSet = (ResultSet) smt.executeQuery("SELECT * FROM OrderTable WHERE OrderId="+orderId);
            while(resultSet.next()){
                int totalAmount = resultSet.getInt("TotalAmount");
                Time orderTime = resultSet.getTime("OrderTime");
                FinalBillDetails details = new FinalBillDetails();
                details.totalAmount = totalAmount;
                details.orderTime = orderTime;
                billDetails.add(details);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DisplayFinalBill.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            closeDbConnection(connection);
        }
        return billDetails;
    }
}
