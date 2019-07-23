
package com.takeawayfood.api;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.takeawayfood.database.RestaurantMenuDb;
import com.takeawayfood.extraclasses.RestaurantMenuDetails;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class DisplayRestaurantMenu extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        System.out.println("hiiii");
        RestaurantMenuDb menuDb = new RestaurantMenuDb();
        RestaurantMenuDetails details;
        
        StringBuilder buffer = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
        }
        String data = buffer.toString();
        System.out.println(data+"-data");
        
        JSONParser parser = new JSONParser();
        JSONObject json;
        String restaurantId = null;
        try {
            json = (JSONObject) parser.parse(data);
            restaurantId=""+json.get("restaurantId");
        } catch (ParseException ex) {
            Logger.getLogger(DisplayRestaurantMenu.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        List<RestaurantMenuDetails> menuDetails;
        int id = Integer.parseInt(restaurantId);
        menuDetails = menuDb.displayRestaurantMenu(id);
        
        JSONObject nameObj = new JSONObject();
        JSONObject priceObj = new JSONObject();
        JSONObject ingrediantObj = new JSONObject();
        
        for(int i=0;i<menuDetails.size();i++){
            details = menuDetails.get(i);
            nameObj.put(i, details.itemName);
            priceObj.put(i, details.itemPrice);
            ingrediantObj.put(i, details.ingrediants);
        }
        JSONObject jSONObject3 = new JSONObject();
        jSONObject3.put("itemName", nameObj);
        jSONObject3.put("itemPrice", priceObj);
        jSONObject3.put("itemIngrediants", ingrediantObj);
        PrintWriter out = response.getWriter();
        out.println(jSONObject3);
        System.out.println(" "+nameObj+" "+priceObj+" "+ingrediantObj);
    }
}
