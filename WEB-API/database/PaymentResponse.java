package com.takeawayfood.database;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PaymentResponse extends DbConnection{
    public void updatePaymentStatus(String status,int orderId){
        System.out.println("update");
        try {
            initializeDbConnection();
            if(status.equals("success"))
                smt.executeUpdate("UPDATE OrderTable SET PaymentStatus=1 WHERE OrderId="+orderId);
            else
                smt.executeUpdate("UPDATE OrderTable SET PaymentStatus=2 WHERE OrderId="+orderId);
        } catch (SQLException ex) {
            Logger.getLogger(PaymentResponse.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            closeDbConnection(connection);
        }
        
    }
}
