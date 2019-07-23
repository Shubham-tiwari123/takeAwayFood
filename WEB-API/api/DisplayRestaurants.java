package com.takeawayfood.api;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.takeawayfood.extraclasses.RestaurantsDetails;
import com.takeawayfood.database.RestaurantConnectedDb;
import java.util.List;
import org.json.simple.JSONObject;
public class DisplayRestaurants extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RestaurantConnectedDb connected = new RestaurantConnectedDb();
        RestaurantsDetails restaurantDetails;
        System.out.println("hiii");
        JSONObject nameObj = new JSONObject();
        JSONObject openTimeObj = new JSONObject();
        JSONObject closeTimeObj = new JSONObject();
        JSONObject restaurantIDsObj = new JSONObject();
        
        List<RestaurantsDetails> details;
        details = connected.displayRestaurantConn();
        
        for(int i=0;i<details.size();i++){
            restaurantDetails = details.get(i);
            nameObj.put(i, restaurantDetails.name);
            openTimeObj.put(i, restaurantDetails.openTime.toString().substring(0, 5));
            closeTimeObj.put(i, restaurantDetails.closeTime.toString().substring(0, 5));
            restaurantIDsObj.put(i, restaurantDetails.restaurantId);    
        }
        
        JSONObject jSONObject3 = new JSONObject();
        jSONObject3.put("name", nameObj);
        jSONObject3.put("openTime", openTimeObj);
        jSONObject3.put("closeTime", closeTimeObj);
        jSONObject3.put("restaurantId", restaurantIDsObj);
        PrintWriter out = response.getWriter();
        out.println(jSONObject3);
    }
}
