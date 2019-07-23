package com.takeawayfood.database;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChangeOrderStatus extends DbConnection{
     /*
        * 0 - for no status
        * 1 - for order accepted
        * 2 - for order rejected
        * 3 - for order cooking
        * 4 - for order delivered
        * */
    public void acceptOrder(int orderId,int restaurantId){
        try {
            initializeDbConnection();
            smt.executeUpdate("UPDATE OrderTable SET OrderStatus = 1 WHERE OrderId="+orderId+
                    " AND RestaurantId="+restaurantId);
            System.out.println("done");
        } catch (SQLException ex) {
            Logger.getLogger(ChangeOrderStatus.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            closeDbConnection(connection);
        }
    } 
    
    public void rejectOrder(int orderId,int restaurantId){
        try {
            initializeDbConnection();
            smt.executeUpdate("UPDATE OrderTable SET OrderStatus = 2 WHERE OrderId="+orderId+
                    " AND RestaurantId="+restaurantId);
            System.out.println("done");
        } catch (SQLException ex) {
            Logger.getLogger(ChangeOrderStatus.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            closeDbConnection(connection);
        }
    } 
    
    public void cookingStatus(int orderId,int restaurantId){
        try {
            initializeDbConnection();
            smt.executeUpdate("UPDATE OrderTable SET CookingStatus = 1 WHERE OrderId="+orderId+
                    " AND RestaurantId="+restaurantId);
            System.out.println("done");
        } catch (SQLException ex) {
            Logger.getLogger(ChangeOrderStatus.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            closeDbConnection(connection);
        }
    } 
    
    public void parcelStatus(int orderId,int restaurantId){
        try {
            initializeDbConnection();
            smt.executeUpdate("UPDATE OrderTable SET ParcelStatus = 1 WHERE OrderId="+orderId+
                    " AND RestaurantId="+restaurantId);
            System.out.println("done");
        } catch (SQLException ex) {
            Logger.getLogger(ChangeOrderStatus.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            closeDbConnection(connection);
        }
    } 
    
    public void deliveryStatus(int orderId,int restaurantId){
        try {
            initializeDbConnection();
            connection.setAutoCommit(false);
            smt.executeUpdate("UPDATE OrderTable SET DeliveryStatus = 1 WHERE OrderId="+orderId+
                    " AND RestaurantId="+restaurantId);
            System.out.println("done");
            smt.executeUpdate("UPDATE OrderTable SET OrderStatus = 4 WHERE OrderId="+orderId+
                    " AND RestaurantId="+restaurantId);
            connection.commit();
        } catch (SQLException ex) {
            Logger.getLogger(ChangeOrderStatus.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            closeDbConnection(connection);
        }
    } 
}
