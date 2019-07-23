package com.takeawayfood.database;

import com.mysql.jdbc.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CookingStatus extends DbConnection{
    
    public int cookingStatus(int orderId){
        int cookingStatus = 0,parcelStatus = 0;
        int deliveredStatus =0;
        Time orderTime = null; 
        try {
            initializeDbConnection();
            resultSet = (ResultSet) smt.executeQuery("SELECT * FROM OrderTable WHERE OrderId="+orderId);
            while(resultSet.next()){
                cookingStatus = resultSet.getInt("CookingStatus");
                deliveredStatus = resultSet.getInt("DeliveryStatus");
                orderTime = resultSet.getTime("OrderTime");
            }
            DateFormat dateFormat = new SimpleDateFormat("HH:mm");
            String systemCurrentTime = dateFormat.format(Calendar.getInstance().getTime());
            String time1 = orderTime.toString().substring(3, 5);
            String time2 = systemCurrentTime.substring(3, 5);
            
            int num1 = Integer.parseInt(time1);
            int num2 = Integer.parseInt(time2);
            
            if(deliveredStatus==1){
                return 3;
            }
            else{
                if(cookingStatus==1 && Math.abs(num1-num2)<=20){
                    return 1;
                } 
                if(cookingStatus==1 && Math.abs(num1-num2)>=20 && deliveredStatus ==0)
                    return 2;
                else if(cookingStatus == 0)
                    return 4;
            }
        } catch (SQLException ex) {
            Logger.getLogger(CookingStatus.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            closeDbConnection(connection);
        }
        return 0;
    }
}
