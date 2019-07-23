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

public class DisplayRestaurantAcceptedOrder extends HttpServlet {

   
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
        
        ordersList = displayOrderDb.displayAcceptedOrders(restaurantID);
        
        JSONObject nameObj = new JSONObject();
        JSONObject priceObj = new JSONObject();
        JSONObject qntObj = new JSONObject();
        JSONObject orderIDObj = new JSONObject();
        JSONObject orderTotalAmountObj = new JSONObject();
        JSONObject orderTimeObj = new JSONObject();
        JSONObject cookingStatus = new JSONObject();
        for(int i=0;i<ordersList.size();i++){
            displayOrders = ordersList.get(i);
            if(nameObj.isEmpty()){
                nameObj.put(displayOrders.orderId, displayOrders.itemName);
                priceObj.put(displayOrders.orderId, displayOrders.itemPrice);
                String itemQntString = String.valueOf(displayOrders.itemQnt);
                qntObj.put(displayOrders.orderId, itemQntString);
            }
            else{
                if(nameObj.containsKey(displayOrders.orderId)){
                    String itemNames = nameObj.get(displayOrders.orderId).toString();
                    String itemPrices = priceObj.get(displayOrders.orderId).toString();
                    String itemQnts = qntObj.get(displayOrders.orderId).toString();
                    
                    String newItemName = itemNames+","+displayOrders.itemName;
                    String newItemPrice = itemPrices+","+displayOrders.itemPrice;
                    String itemQntString = String.valueOf(displayOrders.itemQnt);
                    String newItemQnt = itemQnts+","+itemQntString;
                    
                    nameObj.put(displayOrders.orderId, newItemName);
                    priceObj.put(displayOrders.orderId, newItemPrice);
                    qntObj.put(displayOrders.orderId, newItemQnt);
                }
                else{
                    nameObj.put(displayOrders.orderId, displayOrders.itemName);
                    priceObj.put(displayOrders.orderId, displayOrders.itemPrice);
                    String itemQntString = String.valueOf(displayOrders.itemQnt);
                    qntObj.put(displayOrders.orderId, itemQntString);
                }
                
            }
        }
        DisplayOrders displayOrders1;
        List<DisplayOrders> ordersList2 = displayOrderDb.getAcceptedOrderDetailsWithTime(restaurantID);
        for(int i=0;i<ordersList2.size();i++){
            displayOrders1 = ordersList2.get(i);
            orderIDObj.put(i, displayOrders1.orderIDs);
            orderTotalAmountObj.put(displayOrders1.orderIDs,displayOrders1.orderAmount);
            cookingStatus.put(displayOrders1.orderIDs, displayOrders1.cookingStatus);
            orderTimeObj.put(displayOrders1.orderIDs, (displayOrders1.orderTime).toString()
                    .substring(0, 5));
        }
        
        JSONObject jSONObject3 = new JSONObject();
        jSONObject3.put("itemName", nameObj);
        jSONObject3.put("itemQnt", qntObj);
        jSONObject3.put("orderId", orderIDObj);
        jSONObject3.put("totalBill", orderTotalAmountObj);
        jSONObject3.put("orderTime", orderTimeObj);
        jSONObject3.put("cookingStatus", cookingStatus);
        
        System.out.println("\nname:-"+nameObj+"\nPrice:-"+priceObj+"\nQnt:-"+qntObj);
        
        System.out.println("\nid:-"+orderIDObj+"\nAmount:-"+orderTotalAmountObj+
                "\nDate:-"+orderTimeObj);
        
        PrintWriter out = response.getWriter();
        out.println(jSONObject3);
    }
}
