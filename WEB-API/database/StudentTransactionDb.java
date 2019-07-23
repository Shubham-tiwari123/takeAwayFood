package com.takeawayfood.database;

import com.mysql.jdbc.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.takeawayfood.extraclasses.StudentOrderTransaction;

public class StudentTransactionDb extends DbConnection{
    
    public List<StudentOrderTransaction> displayHistory(String phoneNumber){
        ResultSet resultSet2;
        List<StudentOrderTransaction> orderList = new LinkedList<>();
        List<Long>list = new LinkedList<>();
        List<Integer>statusList = new LinkedList<>();
        long phoneNo=Long.parseLong(phoneNumber);
        try {
            initializeDbConnection();
            resultSet = (ResultSet) smt.executeQuery("SELECT * FROM OrderTable WHERE "
                    + "StudentPhone="+phoneNo+" AND PaymentStatus=1");
            while(resultSet.next()){
                long number = resultSet.getLong("OrderId");
                int ordersStatus = resultSet.getInt("OrderStatus");
                if(ordersStatus==4||ordersStatus==2){
                    list.add(number);
                    statusList.add(ordersStatus);
                }
            }
            System.out.println("statuslist:-"+statusList);
            System.out.println("orderId:-"+list);
            while(!list.isEmpty()){
                
                resultSet2 = (ResultSet) smt.executeQuery("SELECT * FROM OrderItemTable WHERE "
                        + "OrderId="+list.get(0));
                while(resultSet2.next()){
                    StudentOrderTransaction orderTransaction = new StudentOrderTransaction();
                    String itemName = resultSet2.getString("itemName");
                    int itemQnt = resultSet2.getInt("itemQnt");
                    int itemPrice = resultSet2.getInt("itemPrice");
                    int id=resultSet2.getInt("OrderId");
                    
                    orderTransaction.itemName=itemName;
                    orderTransaction.itemPrice = itemPrice;
                    orderTransaction.itemQnt=itemQnt;
                    orderTransaction.orderId = id;
                    orderTransaction.orderStatus = statusList.get(0);
                    
                    orderList.add(orderTransaction);
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
    
    public List<StudentOrderTransaction> getOrderIDList(String phoneNumber){
        long phoneNo = Long.parseLong(phoneNumber);
        List<StudentOrderTransaction> orderIdList = new LinkedList<>();
        try {
            initializeDbConnection();
            resultSet = (ResultSet) smt.executeQuery("SELECT * FROM OrderTable WHERE "
                    + "StudentPhone="+phoneNo+" AND PaymentStatus=1 AND (OrderStatus=4 OR OrderStatus=2)");
            while (resultSet.next()) {
                StudentOrderTransaction orderTransaction = new StudentOrderTransaction();
                orderTransaction.orderAmount = resultSet.getInt("TotalAmount");
                orderTransaction.orderIDs = resultSet.getInt("OrderId");
                orderTransaction.orderDate = resultSet.getDate("OrderDate");
                System.out.println("orderDate:-"+orderTransaction.orderDate.toString().substring(6, 10));
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
