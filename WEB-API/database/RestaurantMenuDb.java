package com.takeawayfood.database;

import java.sql.SQLException;
import java.sql.Time;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.takeawayfood.extraclasses.RestaurantMenuDetails;
import com.mysql.jdbc.ResultSet;
import java.util.LinkedList;
import java.util.List;

public class RestaurantMenuDb extends DbConnection{
    
    public void insertData(int restaurantId,int itemId,String itemName, int itemPrice, 
            String ingrediants){
        
        try {
            initializeDbConnection();
            smt.executeUpdate("INSERT INTO RestaurantMenu(RestaurantId,"
                    + "Itemid,ItemName,ItemPrice,Ingrediant) VALUES "
                    + "("+restaurantId+","+itemId+",'"+itemName+"',"+itemPrice+","
                            + "'"+ingrediants+"')");
        } catch (SQLException ex) {
            Logger.getLogger(RestaurantConnectedDb.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally{
            closeDbConnection(connection);
        }
    }
    
    public List<RestaurantMenuDetails> displayRestaurantMenu(int restaurantId){
        List<RestaurantMenuDetails> menu = new LinkedList<>();
        try {
            
            initializeDbConnection();
            resultSet = (ResultSet) smt.executeQuery("SELECT * FROM RestaurantMenu WHERE "
                    + "RestaurantId="+restaurantId);
            
            while(resultSet.next()){
                RestaurantMenuDetails restaurantMenu = new RestaurantMenuDetails();
                restaurantMenu.itemName = resultSet.getString("ItemName");
                restaurantMenu.itemPrice = resultSet.getInt("ItemPrice");
                restaurantMenu.ingrediants = resultSet.getString("Ingrediant");
                
                menu.add(restaurantMenu);
            }
        } catch (SQLException ex) {
            Logger.getLogger(RestaurantMenuDb.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally{
            closeDbConnection(connection);
        }
        return menu;
    }
    
}
