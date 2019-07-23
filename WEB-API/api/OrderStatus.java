package com.takeawayfood.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import com.takeawayfood.database.ChangeOrderStatus;

public class OrderStatus extends HttpServlet {
    ChangeOrderStatus changeStatus = new ChangeOrderStatus();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String restaurantID;
            String orderId;
            String flag;
            StringBuffer buffer = new StringBuffer();
            BufferedReader reader = request.getReader();
            String line;
            while((line = reader.readLine())!= null)
                buffer.append(line);
            String data = buffer.toString();
            JSONParser jSONParser = new JSONParser();
            JSONObject jSONObject = (JSONObject) jSONParser.parse(data);
            flag = jSONObject.get("flagValue").toString();
            restaurantID = jSONObject.get("restaurantID").toString();
            orderId = jSONObject.get("orderId").toString();
            System.out.println("flag:-"+flag+" res:-"+restaurantID+" order:-"+orderId);
            int flagValue = Integer.parseInt(flag);
            int restaurantIdValue = Integer.parseInt(restaurantID);
            int orderIdValue = Integer.parseInt(orderId);
            switch (flagValue) {
                case 1:changeStatus.acceptOrder(orderIdValue, restaurantIdValue);
                    break;
                case 2:changeStatus.rejectOrder(orderIdValue, restaurantIdValue);
                    break;
                case 3:changeStatus.cookingStatus(orderIdValue, restaurantIdValue);
                    break;
                case 4:changeStatus.deliveryStatus(orderIdValue, restaurantIdValue);
                    break;
                case 5:changeStatus.parcelStatus(orderIdValue, restaurantIdValue);
                    break;
            }
        } catch (ParseException ex) {
            Logger.getLogger(OrderStatus.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
