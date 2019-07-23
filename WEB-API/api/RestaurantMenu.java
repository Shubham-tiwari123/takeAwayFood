package com.takeawayfood.api;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.takeawayfood.database.RestaurantMenuDb;
import java.io.BufferedReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class RestaurantMenu extends HttpServlet {

    RestaurantMenuDb menuDb = new RestaurantMenuDb();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        StringBuffer buffer = new StringBuffer();
        BufferedReader reader = request.getReader();
        String line;
        while((line = reader.readLine())!= null)
            buffer.append(line);
        String data = buffer.toString();
        JSONParser parser = new JSONParser();
        try {
            JSONObject jSONObject = (JSONObject) parser.parse(data);
            String restaurantId= jSONObject.get("restaurantId").toString();
            String itemNo= jSONObject.get("itemNo").toString();
            String itemName= jSONObject.get("itemName").toString();
            String itemPrice= jSONObject.get("itemPrice").toString();
            String ingrediants= jSONObject.get("ingrediants").toString();
            
            int restaurantUniqueNo = Integer.parseInt(restaurantId);
            int itemId = Integer.parseInt(itemNo);
            int itemValue = Integer.parseInt(itemPrice);
            
            menuDb.insertData(restaurantUniqueNo, itemId, itemName, itemValue, ingrediants);
            
        } catch (ParseException ex) {
            Logger.getLogger(GenerateOtp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
