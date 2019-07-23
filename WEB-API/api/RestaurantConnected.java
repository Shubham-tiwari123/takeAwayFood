package com.takeawayfood.api;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.takeawayfood.database.RestaurantConnectedDb;
import java.sql.Time;

public class RestaurantConnected extends HttpServlet {
    RestaurantConnectedDb connectedDb = new RestaurantConnectedDb();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String restaurantId = request.getParameter("restaurantId");
        String restaurantName = request.getParameter("restaurantName");
        String addharNumber = request.getParameter("addharNumber");
        String address = request.getParameter("address");
        String openTime = request.getParameter("openTime");
        String closeTime = request.getParameter("closeTime");
        
        Time openTimes = java.sql.Time.valueOf(openTime+":00");
        Time closeTimes = java.sql.Time.valueOf(closeTime+":00");
        long addharNo = Long.parseLong(addharNumber);
        int restaurantID = Integer.parseInt(restaurantId);
        
        connectedDb.insertData(restaurantID, restaurantName, addharNo, address, openTimes, closeTimes);
        System.out.println(addharNo+" "+openTimes+" "+closeTimes+" "+restaurantName);
    }
}
