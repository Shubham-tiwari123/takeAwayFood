package com.takeawayfood.database;

import com.mysql.jdbc.ResultSet;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OrderTableDb extends DbConnection{
    
    public boolean insertOrderData(int orderId,long studentNumber, int restaurantId,
            int billAmount,ArrayList<String>itemName,ArrayList<Integer> itemQnt,
            ArrayList<Integer>itemPrice){
        
        String orderTable= "OrderTable";
        int count = orderId-1;
        long millis=System.currentTimeMillis();  
        java.sql.Date orderDate=new java.sql.Date(millis);  
        
        Time orderTime = java.sql.Time.valueOf(LocalTime.now());
        
        try {
            initializeDbConnection(); 
            connection.setAutoCommit(false);
                       
            smt.executeUpdate("INSERT INTO OrderTable(OrderId,StudentPhone,RestaurantId,"
                    + "OrderDate,OrderTime,TotalAmount) VALUES ("+orderId+","+studentNumber+","+restaurantId+","
                            + "'"+orderDate+"','"+orderTime+"',"+billAmount+")");
            for(int i=0;i<itemPrice.size();i++){
                String itemNames = itemName.get(i);
                int itemQnts = itemQnt.get(i);
                int itemPrices = itemPrice.get(i);
                
                smt.executeUpdate("INSERT INTO OrderItemTable (OrderId,itemName,itemQnt,itemPrice) "
                        + "VALUES("+orderId+",'"+itemNames+"',"+itemQnts+","+itemPrices+")");
            }
            
            smt.executeUpdate("UPDATE CounterTable SET count="+orderId+" WHERE TableName='"+orderTable+"' AND count="+count+"");
            connection.commit();
            return true;
            
        } catch (SQLException ex) {
            Logger.getLogger(RestaurantConnectedDb.class.getName()).log(Level.SEVERE, null, ex);
            try {
                connection.rollback();
                return false;
            } catch (SQLException ex1) {
                Logger.getLogger(OrderTableDb.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        finally{
            closeDbConnection(connection);
        }
        return false;
    }
    
    public int getOrderId(){
        int orderId = 0;
        String orderTable= "OrderTable";
        initializeDbConnection();
        try {
            resultSet = (ResultSet) smt.executeQuery("SELECT * FROM CounterTable WHERE TableName = '"+orderTable+"'");
            while (resultSet.next()) {                
                orderId = resultSet.getInt("count");
            }
        } catch (SQLException ex) {
            Logger.getLogger(OrderTableDb.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally{
            closeDbConnection(connection);
        }
        return orderId;
    }
}
