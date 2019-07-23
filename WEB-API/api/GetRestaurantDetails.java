package com.takeawayfood.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;
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

public class GetRestaurantDetails extends HttpServlet {
    RestaurantConnectedDb connectedDb = new RestaurantConnectedDb();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String id = null;
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
            id = jSONObject.get("id").toString();
        } catch (ParseException ex) {
            Logger.getLogger(DisplayStudentTransaction.class.getName()).log(Level.SEVERE, null, ex);
        }
        long restaurantID = Long.parseLong(id);
        List<String> details = connectedDb.getDetails(restaurantID);
        String name = details.get(0);
        String email = details.get(1);
        String openTime = details.get(2);
        String closeTime = details.get(3);
        JSONObject detailObj = new JSONObject();
        
        List<Integer> transactionDetails = connectedDb.getTransactionDetails(restaurantID);
        
        
        detailObj.put("userName", name);
        detailObj.put("userEmail", email);
        detailObj.put("openTime", openTime);
        detailObj.put("closeTime", closeTime);
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("result", detailObj);
        System.out.println(detailObj);
        PrintWriter printWriter = response.getWriter();
        
        printWriter.print(jSONObject);
    }
}
