package com.takeawayfood.database;

import java.sql.SQLException;
import java.sql.Time;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.takeawayfood.extraclasses.RestaurantsDetails;
import com.mysql.jdbc.ResultSet;
import java.util.LinkedList;
import java.util.List;

public class RestaurantConnectedDb extends DbConnection{
    //RestaurantsDetails details = new RestaurantsDetails();
    public void insertData(int restaurantId, String name, long adhaarNumber,
            String addess, Time openTime, Time closeTime){
        
        try {
            initializeDbConnection();
            smt.executeUpdate("INSERT INTO RestaurantConn(RestaurantId,"
                    + "Name,AdhaarNumber,Address,OpenTime,CloseTime) VALUES "
                    + "("+restaurantId+",'"+name+"',"+adhaarNumber+","
                            + "'"+addess+"','"+openTime+"','"+closeTime+"')");
        } catch (SQLException ex) {
            Logger.getLogger(RestaurantConnectedDb.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally{
            closeDbConnection(connection);
        }
    }
    
    public List<RestaurantsDetails> displayRestaurantConn(){
        
        List<RestaurantsDetails> details = new LinkedList<>();
        try {
            initializeDbConnection();
            resultSet = (ResultSet) smt.executeQuery("SELECT * FROM RestaurantConn");
            while(resultSet.next()){
                RestaurantsDetails rd = new RestaurantsDetails();
                rd.restaurantId = resultSet.getInt("RestaurantId");
                rd.name = resultSet.getString("Name");
                rd.address = resultSet.getString("Address");
                rd.openTime = resultSet.getTime("OpenTime");
                rd.closeTime = resultSet.getTime("CloseTime");
                details.add(rd);
                System.out.println("details:-"+details);
            }
        } catch (SQLException ex) {
            Logger.getLogger(RestaurantConnectedDb.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally{
            closeDbConnection(connection);
        }
        return details;
    } 
    
    public void saveRestaurantDetails(long restaurantPhone,String email,
            Time openTime,Time closeTime){
        try {
            initializeDbConnection();
            smt.executeUpdate("UPDATE RestaurantConn SET OpenTime='"+openTime+"',"
                    + "CloseTime='"+closeTime+"',Email='"+email+"' WHERE RestaurantId="+restaurantPhone);
        } catch (SQLException ex) {
            Logger.getLogger(RestaurantConnectedDb.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally{
            closeDbConnection(connection);
        }
    }
    
    public List<String> getDetails(long id){
        List<String> details = new LinkedList<>();
        String name = null,email = null;
        Time openTime = null,closeTime = null;
        try {
            initializeDbConnection();
            resultSet = (ResultSet) smt.executeQuery("SELECT * FROM RestaurantConn WHERE "
                    + "RestaurantId="+id);
            while (resultSet.next()) {                
                name = resultSet.getString("Name");
                email = resultSet.getString("Email");
                openTime = resultSet.getTime("OpenTime");
                closeTime= resultSet.getTime("CloseTime");
            }
        } catch (SQLException ex) {
            Logger.getLogger(RestaurantConnectedDb.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            closeDbConnection(connection);
        }
        String openTiming = openTime.toString().substring(0, 5);
        String closeTiming = closeTime.toString().substring(0,5);
        details.add(name);
        details.add(email);
        details.add(openTiming);
        details.add(closeTiming);
        return details;
    }
    
    public List<Integer> getTransactionDetails(long id){
        List<Integer> details = new LinkedList<>();
        
        return details;
    }
        
}
