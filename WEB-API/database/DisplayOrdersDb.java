package com.takeawayfood.database;

import com.takeawayfood.extraclasses.DisplayOrders;
import com.mysql.jdbc.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DisplayOrdersDb extends DbConnection{
    //put date contraint in search querry
    public List<DisplayOrders> displayNewOrders(String restaurantId){
        ResultSet resultSet2;
        List<DisplayOrders> orderList = new LinkedList<>();
        List<Long>list = new LinkedList<>();
        
        long idNo=Long.parseLong(restaurantId);
        try {
            initializeDbConnection();
            resultSet = (ResultSet) smt.executeQuery("SELECT * FROM OrderTable WHERE "
                    + "RestaurantId="+idNo+" AND OrderStatus=0 AND PaymentStatus=1");
            while(resultSet.next()){
                long number = resultSet.getLong("OrderId");
                list.add(number);
            }
            while(!list.isEmpty()){
                
                resultSet2 = (ResultSet) smt.executeQuery("SELECT * FROM OrderItemTable WHERE "
                        + "OrderId="+list.get(0));
                while(resultSet2.next()){
                    DisplayOrders displayOrders = new DisplayOrders();
                    String itemName = resultSet2.getString("itemName");
                    int itemQnt = resultSet2.getInt("itemQnt");
                    int itemPrice = resultSet2.getInt("itemPrice");
                    int id=resultSet2.getInt("OrderId");
                    
                    displayOrders.itemName=itemName;
                    displayOrders.itemPrice = itemPrice;
                    displayOrders.itemQnt=itemQnt;
                    displayOrders.orderId = id;
                    orderList.add(displayOrders);
                }
                list.remove(0);
            }
        } catch (SQLException ex) {
            Logger.getLogger(StudentTransactionDb.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally{
            closeDbConnection(connection);
        }
        return orderList;
    }
    
    public List<DisplayOrders> displayAcceptedOrders(String restaurantId){
        ResultSet resultSet2;
        List<DisplayOrders> orderList = new LinkedList<>();
        List<Long>list = new LinkedList<>();
        
        
        long idNo=Long.parseLong(restaurantId);
        try {
            initializeDbConnection();
            resultSet = (ResultSet) smt.executeQuery("SELECT * FROM OrderTable WHERE "
                    + "RestaurantId="+idNo+" AND OrderStatus=1");
            while(resultSet.next()){
                long number = resultSet.getLong("OrderId");
                list.add(number);
            }
            while(!list.isEmpty()){
                
                resultSet2 = (ResultSet) smt.executeQuery("SELECT * FROM OrderItemTable WHERE "
                        + "OrderId="+list.get(0));
                while(resultSet2.next()){
                    DisplayOrders displayOrders = new DisplayOrders();
                    String itemName = resultSet2.getString("itemName");
                    int itemQnt = resultSet2.getInt("itemQnt");
                    int itemPrice = resultSet2.getInt("itemPrice");
                    int id=resultSet2.getInt("OrderId");
                    
                    displayOrders.itemName=itemName;
                    displayOrders.itemPrice = itemPrice;
                    displayOrders.itemQnt=itemQnt;
                    displayOrders.orderId = id;
                    orderList.add(displayOrders);
                }
                list.remove(0);
            }
        } catch (SQLException ex) {
            Logger.getLogger(StudentTransactionDb.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally{
            closeDbConnection(connection);
        }
        return orderList;
    }
    
    public List<DisplayOrders> getAcceptedOrderDetailsWithTime(String restaurantID){
        long restaurantId = Long.parseLong(restaurantID);
        List<DisplayOrders> orderIdList = new LinkedList<>();
        try {
            initializeDbConnection();
            resultSet = (ResultSet) smt.executeQuery("SELECT * FROM OrderTable WHERE "
                    + "RestaurantId="+restaurantId+" AND OrderStatus=1");
            while (resultSet.next()) {
                DisplayOrders displayOrders = new DisplayOrders();
                displayOrders.orderAmount = resultSet.getInt("TotalAmount");
                displayOrders.orderIDs = resultSet.getInt("OrderId");
                displayOrders.orderTime = resultSet.getTime("OrderTime");
                int cookingStatus = resultSet.getInt("CookingStatus");
                //int parcelStatus = resultSet.getInt("ParcelStatus");
                if(cookingStatus==1)
                    displayOrders.cookingStatus = 1;
                else
                    displayOrders.cookingStatus = 0;
                orderIdList.add(displayOrders);
            }
        } catch (SQLException ex) {
            Logger.getLogger(StudentTransactionDb.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeDbConnection(connection);
        }
        return orderIdList;
    }
    
    public List<DisplayOrders> getNewOrderDetailsWithTime(String restaurantID){
        long restaurantId = Long.parseLong(restaurantID);
        List<DisplayOrders> orderIdList = new LinkedList<>();
        try {
            initializeDbConnection();
            resultSet = (ResultSet) smt.executeQuery("SELECT * FROM OrderTable WHERE "
                    + "RestaurantId="+restaurantId+" AND OrderStatus=0 AND PaymentStatus=1");
            while (resultSet.next()) {
                DisplayOrders displayOrders = new DisplayOrders();
                displayOrders.orderAmount = resultSet.getInt("TotalAmount");
                displayOrders.orderIDs = resultSet.getInt("OrderId");
                displayOrders.orderTime = resultSet.getTime("OrderTime");
                orderIdList.add(displayOrders);
            }
        } catch (SQLException ex) {
            Logger.getLogger(StudentTransactionDb.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeDbConnection(connection);
        }
        return orderIdList;
    }
    
    public List<DisplayOrders> displayOrderHistory(String restaurantID){
        ResultSet resultSet2;
        List<DisplayOrders> orderList = new LinkedList<>();
        List<Long>list = new LinkedList<>();
        List<Integer>statusList = new LinkedList<>();
        
        long idNo=Long.parseLong(restaurantID);
        try {
            initializeDbConnection();
            resultSet = (ResultSet) smt.executeQuery("SELECT * FROM OrderTable WHERE "
                    + "RestaurantId="+idNo+" AND PaymentStatus=1 AND NOT OrderStatus=0");
            while(resultSet.next()){
                long number = resultSet.getLong("OrderId");
                int ordersStatus = resultSet.getInt("OrderStatus");
                list.add(number);
                statusList.add(ordersStatus);
            }
            while(!list.isEmpty()){
                
                resultSet2 = (ResultSet) smt.executeQuery("SELECT * FROM OrderItemTable WHERE "
                        + "OrderId="+list.get(0));
                while(resultSet2.next()){
                    DisplayOrders displayOrders = new DisplayOrders();
                    String itemName = resultSet2.getString("itemName");
                    int itemQnt = resultSet2.getInt("itemQnt");
                    int itemPrice = resultSet2.getInt("itemPrice");
                    int id=resultSet2.getInt("OrderId");
                    
                    
                    displayOrders.itemName=itemName;
                    displayOrders.itemPrice = itemPrice;
                    displayOrders.itemQnt=itemQnt;
                    displayOrders.orderId = id;
                    displayOrders.orderStatus = statusList.get(0);
                    
                    orderList.add(displayOrders);
                }
                list.remove(0);
                statusList.remove(0);
            }
        } catch (SQLException ex) {
            Logger.getLogger(StudentTransactionDb.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally{
            closeDbConnection(connection);
        }
        return orderList;
    }
    
    public List<DisplayOrders> getOrderHistoryWithDate(String restaurantID){
        long restaurantId = Long.parseLong(restaurantID);
        List<DisplayOrders> orderIdList = new LinkedList<>();
        try {
            initializeDbConnection();
            resultSet = (ResultSet) smt.executeQuery("SELECT * FROM OrderTable WHERE "
                    + "RestaurantId=" + restaurantId+" AND PaymentStatus=1 AND NOT OrderStatus=0");
            while (resultSet.next()) {
                DisplayOrders orderTransaction = new DisplayOrders();
                orderTransaction.orderAmount = resultSet.getInt("TotalAmount");
                orderTransaction.orderIDs = resultSet.getInt("OrderId");
                orderTransaction.orderDate = resultSet.getDate("OrderDate");
                orderIdList.add(orderTransaction);
            }
        } catch (SQLException ex) {
            Logger.getLogger(StudentTransactionDb.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeDbConnection(connection);
        }
        return orderIdList;
    }
}
