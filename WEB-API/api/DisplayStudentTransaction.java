package com.takeawayfood.api;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.takeawayfood.database.StudentTransactionDb;
import java.util.List;
import com.takeawayfood.extraclasses.StudentOrderTransaction;
import java.io.BufferedReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
public class DisplayStudentTransaction extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String studentPhone = null;
        StudentTransactionDb std = new StudentTransactionDb();
        StudentOrderTransaction orderTransaction;
        List<StudentOrderTransaction> transactions;
        
        StringBuffer buffer = new StringBuffer();
        BufferedReader reader = request.getReader();
        String line;
        while((line = reader.readLine())!= null)
            buffer.append(line);
        String data = buffer.toString();
        JSONParser parser = new JSONParser();
        System.out.println("dataOrder:-"+data);
        
        
        try {
            JSONObject jSONObject = (JSONObject) parser.parse(data);
            studentPhone = jSONObject.get("studentPhone").toString();
        } catch (ParseException ex) {
            Logger.getLogger(DisplayStudentTransaction.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        transactions = std.displayHistory(studentPhone);
        JSONObject nameObj = new JSONObject();
        JSONObject priceObj = new JSONObject();
        JSONObject qntObj = new JSONObject();
        JSONObject orderIDObj = new JSONObject();
        JSONObject orderTotalAmountObj = new JSONObject();
        JSONObject orderDateObj = new JSONObject();
        JSONObject orderStatusObj = new JSONObject();
        
        for(int i=0;i<transactions.size();i++){
            orderTransaction = transactions.get(i);
            if(nameObj.isEmpty()){
                nameObj.put(orderTransaction.orderId, orderTransaction.itemName);
                String itemValue = String.valueOf(orderTransaction.itemPrice);
                
                priceObj.put(orderTransaction.orderId,itemValue);
                
                String itemQntString = String.valueOf(orderTransaction.itemQnt);
                qntObj.put(orderTransaction.orderId, itemQntString);
                
                orderStatusObj.put(orderTransaction.orderId, orderTransaction.orderStatus);
            }
            else{
                if(nameObj.containsKey(orderTransaction.orderId)){
                    String itemNames = nameObj.get(orderTransaction.orderId).toString();
                    String itemPrices = priceObj.get(orderTransaction.orderId).toString();
                    String itemQnts = qntObj.get(orderTransaction.orderId).toString();
                    
                    String newItemName = itemNames+","+orderTransaction.itemName;
                    String itemValue = String.valueOf(orderTransaction.itemPrice);
                    String newItemPrice = itemPrices+","+itemValue;
                    
                    String itemQntString = String.valueOf(orderTransaction.itemQnt);
                    String newItemQnt = itemQnts+","+itemQntString;
                    
                    nameObj.put(orderTransaction.orderId, newItemName);
                    priceObj.put(orderTransaction.orderId, newItemPrice);
                    qntObj.put(orderTransaction.orderId, newItemQnt);
                    
                    orderStatusObj.put(orderTransaction.orderId, orderTransaction.orderStatus);
                }
                else{
                    nameObj.put(orderTransaction.orderId, orderTransaction.itemName);
                    String itemValue = String.valueOf(orderTransaction.itemPrice);
                    priceObj.put(orderTransaction.orderId,itemValue);
                    String itemQntString = String.valueOf(orderTransaction.itemQnt);
                    qntObj.put(orderTransaction.orderId, itemQntString);
                    orderStatusObj.put(orderTransaction.orderId, orderTransaction.orderStatus);
                }
                
            }
        }
        StudentOrderTransaction orderTransaction1;
        List<StudentOrderTransaction> orderIDList = std.getOrderIDList(studentPhone);
        
        for(int i=0;i<orderIDList.size();i++){
            orderTransaction1 = orderIDList.get(i);
            orderIDObj.put(i, orderTransaction1.orderIDs);
            orderTotalAmountObj.put(orderTransaction1.orderIDs,orderTransaction1.orderAmount);
            String date = orderTransaction1.orderDate.toString().substring(6, 10);
            String dates[] = date.split("-");
            
            String month = dates[0];
            String day = dates[1];
            String newDate;
            switch(month){
                case "1": 
                    newDate = day+" Jan";
                    orderDateObj.put(orderTransaction1.orderIDs, newDate);
                    break;
                case "2":
                    newDate = day+" Feb";
                    orderDateObj.put(orderTransaction1.orderIDs, newDate);
                    break;
                case "3":
                    newDate = day+" Mar";
                    orderDateObj.put(orderTransaction1.orderIDs, newDate);
                    break;
                case "4":
                    newDate = day+" Apr";
                    orderDateObj.put(orderTransaction1.orderIDs, newDate);
                    break;
                case "5":
                    newDate = day+" May";
                    orderDateObj.put(orderTransaction1.orderIDs, newDate);
                    break;
                case "6":
                    newDate = day+" Jun";
                    orderDateObj.put(orderTransaction1.orderIDs, newDate);
                    break;
                case "7":
                    newDate = day+" Jul";
                    orderDateObj.put(orderTransaction1.orderIDs, newDate);
                    break;
                case "8":
                    newDate = day+" Aug";
                    orderDateObj.put(orderTransaction1.orderIDs, newDate);
                    break;
                case "9":
                    newDate = day+" Sep";
                    orderDateObj.put(orderTransaction1.orderIDs, newDate);
                    break;
                case "10":
                    newDate = day+" Oct";
                    orderDateObj.put(orderTransaction1.orderIDs, newDate);
                    break;
                case "11":
                    newDate = day+" Nov";
                    orderDateObj.put(orderTransaction1.orderIDs, newDate);
                    break;
                case "12":
                    newDate = day+" Dec";
                    orderDateObj.put(orderTransaction1.orderIDs, newDate);
                    break;
            }
        }
        
        JSONObject jSONObject3 = new JSONObject();
        jSONObject3.put("itemName", nameObj);
        jSONObject3.put("itemPrice", priceObj);
        jSONObject3.put("itemQnt", qntObj);
        jSONObject3.put("orderId", orderIDObj);
        jSONObject3.put("totalBill", orderTotalAmountObj);
        jSONObject3.put("orderDate", orderDateObj);
        jSONObject3.put("statusCode", orderStatusObj);
        PrintWriter out = response.getWriter();
        out.println(jSONObject3);
        
        System.out.println("\nname:-"+nameObj+"\nPrice:-"+priceObj+"\nQnt:-"+qntObj);
        
        System.out.println("\nid:-"+orderIDObj+"\nAmount:-"+orderTotalAmountObj+"\nDate:-"+orderDateObj);
    }
}
