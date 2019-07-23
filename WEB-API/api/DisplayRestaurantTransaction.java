package com.takeawayfood.api;

import com.takeawayfood.extraclasses.DisplayOrders;
import com.takeawayfood.database.DisplayOrdersDb;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class DisplayRestaurantTransaction extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        DisplayOrdersDb displayOrderDb = new DisplayOrdersDb();
        DisplayOrders displayOrders;
        List<DisplayOrders> ordersList;
        
        String restaurantID = null;
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
            restaurantID = jSONObject.get("restaurantID").toString();
        } catch (ParseException ex) {
            
        }
        
        ordersList = displayOrderDb.displayOrderHistory(restaurantID);
        
        JSONObject nameObj = new JSONObject();
        JSONObject priceObj = new JSONObject();
        JSONObject qntObj = new JSONObject();
        JSONObject orderIDObj = new JSONObject();
        JSONObject orderTotalAmountObj = new JSONObject();
        JSONObject orderDateObj = new JSONObject();
        JSONObject orderStatusObj = new JSONObject();
        
        for(int i=0;i<ordersList.size();i++){
            displayOrders = ordersList.get(i);
            if(nameObj.isEmpty()){
                nameObj.put(displayOrders.orderId, displayOrders.itemName);
                String itemValue = String.valueOf(displayOrders.itemPrice);
                
                priceObj.put(displayOrders.orderId,itemValue);
                String itemQntString = String.valueOf(displayOrders.itemQnt);
                qntObj.put(displayOrders.orderId, itemQntString);
                
                orderStatusObj.put(displayOrders.orderId, displayOrders.orderStatus);
            }
            else{
                if(nameObj.containsKey(displayOrders.orderId)){
                    String itemNames = nameObj.get(displayOrders.orderId).toString();
                    String itemPrices = priceObj.get(displayOrders.orderId).toString();
                    String itemQnts = qntObj.get(displayOrders.orderId).toString();
                    
                    String newItemName = itemNames+","+displayOrders.itemName;
                    String itemValue = String.valueOf(displayOrders.itemPrice);
                    String newItemPrice = itemPrices+","+itemValue;
                    
                    String itemQntString = String.valueOf(displayOrders.itemQnt);
                    String newItemQnt = itemQnts+","+itemQntString;
                    
                    nameObj.put(displayOrders.orderId, newItemName);
                    priceObj.put(displayOrders.orderId, newItemPrice);
                    qntObj.put(displayOrders.orderId, newItemQnt);
                    orderStatusObj.put(displayOrders.orderId, displayOrders.orderStatus);
                }
                else{
                    nameObj.put(displayOrders.orderId, displayOrders.itemName);
                    String itemValue = String.valueOf(displayOrders.itemPrice);
                    priceObj.put(displayOrders.orderId,itemValue);
                    String itemQntString = String.valueOf(displayOrders.itemQnt);
                    qntObj.put(displayOrders.orderId, itemQntString);
                    orderStatusObj.put(displayOrders.orderId, displayOrders.orderStatus);
                }
                
            }
        }
        DisplayOrders displayOrders1;
        
        List<DisplayOrders> ordersList2 = displayOrderDb.getOrderHistoryWithDate(restaurantID);
        for(int i=0;i<ordersList2.size();i++){
            displayOrders1 = ordersList2.get(i);
            orderIDObj.put(i, displayOrders1.orderIDs);
            orderTotalAmountObj.put(displayOrders1.orderIDs,displayOrders1.orderAmount);
            
            String date = displayOrders1.orderDate.toString().substring(6, 10);
            String dates[] = date.split("-");
            String month = dates[0];
            String day = dates[1];
            String newDate;
            switch(month){
                case "1": 
                    newDate = day+" Jan";
                    orderDateObj.put(displayOrders1.orderIDs, newDate);
                    break;
                case "2":
                    newDate = day+" Feb";
                    orderDateObj.put(displayOrders1.orderIDs, newDate);
                    break;
                case "3":
                    newDate = day+" Mar";
                    orderDateObj.put(displayOrders1.orderIDs, newDate);
                    break;
                case "4":
                    newDate = day+" Apr";
                    orderDateObj.put(displayOrders1.orderIDs, newDate);
                    break;
                case "5":
                    newDate = day+" May";
                    orderDateObj.put(displayOrders1.orderIDs, newDate);
                    break;
                case "6":
                    newDate = day+" Jun";
                    orderDateObj.put(displayOrders1.orderIDs, newDate);
                    break;
                case "7":
                    newDate = day+" Jul";
                    orderDateObj.put(displayOrders1.orderIDs, newDate);
                    break;
                case "8":
                    newDate = day+" Aug";
                    orderDateObj.put(displayOrders1.orderIDs, newDate);
                    break;
                case "9":
                    newDate = day+" Sep";
                    orderDateObj.put(displayOrders1.orderIDs, newDate);
                    break;
                case "10":
                    newDate = day+" Oct";
                    orderDateObj.put(displayOrders1.orderIDs, newDate);
                    break;
                case "11":
                    newDate = day+" Nov";
                    orderDateObj.put(displayOrders1.orderIDs, newDate);
                    break;
                case "12":
                    newDate = day+" Dec";
                    orderDateObj.put(displayOrders1.orderIDs, newDate);
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
        
        System.out.println("\nname:-"+nameObj+"\nPrice:-"+priceObj+"\nQnt:-"+qntObj);
        
        System.out.println("\nid:-"+orderIDObj+"\nAmount:-"+orderTotalAmountObj+"\nDate:-"+orderDateObj);
        
        PrintWriter out = response.getWriter();
        out.println(jSONObject3);
    }
}
