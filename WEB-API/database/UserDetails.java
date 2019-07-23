package com.takeawayfood.database;

import com.mysql.jdbc.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class UserDetails extends DbConnection{
    List<String> details = new LinkedList<>();
    String userName;
    String userEmail;
    
    public List<String> getDetails(long phoneNo){
        try{
            details.clear();
            initializeDbConnection();
            resultSet = (ResultSet)smt.executeQuery("SELECT * FROM UserDetails WHERE  PhoneNo="+phoneNo);
            while(resultSet.next()){
                userName = resultSet.getString("Name");
                userEmail = resultSet.getString("EmailID");
            }
            if(userName!=null && userEmail!=null){
                details.add(userName);
                details.add(userEmail);
            }
            else if(userName==null && userEmail!=null){
                details.add("not");
                details.add(userEmail);
            }
            else if(userName!=null && userEmail==null){
                details.add(userName);
                details.add("not");
            }
            else{
                details.add("not");
                details.add("not");
            }
               
        } catch (SQLException ex) {
            Logger.getLogger(UserDetails.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally{
            closeDbConnection(connection);
        }
        return details;
    }
    
    public void saveDetails(long phoneNo,String userName,String userEmail){
        try {
            initializeDbConnection();
            smt.executeUpdate("UPDATE UserDetails SET Name='"+userName+"',EmailID='"+userEmail+"'"
                    + " WHERE PhoneNo="+phoneNo);
        } catch (SQLException ex) {
            Logger.getLogger(UserDetails.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally{
            closeDbConnection(connection);
        }
    }
}
