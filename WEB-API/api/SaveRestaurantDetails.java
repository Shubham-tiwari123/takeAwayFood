package com.takeawayfood.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Time;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import com.takeawayfood.database.RestaurantConnectedDb;
public class SaveRestaurantDetails extends HttpServlet {
    RestaurantConnectedDb connectedDb = new RestaurantConnectedDb();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String phoneNo = null;
        String userEmail = null;
        String openTime = null;
        String closeTime = null;
        
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
            phoneNo = jSONObject.get("userPhoneNo").toString();
            userEmail = jSONObject.get("userEmail").toString();
            openTime = jSONObject.get("openTime").toString();
            closeTime = jSONObject.get("closeTime").toString();
            
            Time openTimes = java.sql.Time.valueOf(openTime+":00");
            Time closeTimes = java.sql.Time.valueOf(closeTime+":00");
            long restaurantId = Long.parseLong(phoneNo);
            connectedDb.saveRestaurantDetails(restaurantId,userEmail, openTimes, closeTimes);
            
        } catch (ParseException ex) {
            Logger.getLogger(DisplayStudentTransaction.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println(phoneNo+" "+userEmail+" "+ openTime+" "+closeTime);
    }
}
