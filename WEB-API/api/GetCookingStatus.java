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
import com.takeawayfood.database.CookingStatus;

public class GetCookingStatus extends HttpServlet {
    CookingStatus cookingStatus = new CookingStatus();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String orderID = null;
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
            orderID = jSONObject.get("orderId").toString();
        } catch (ParseException ex) {
            Logger.getLogger(DisplayStudentTransaction.class.getName()).log(Level.SEVERE, null, ex);
        }
        int orderId=-1;
        if(!orderID.equals(""))
            orderId= Integer.parseInt(orderID);
        int status = cookingStatus.cookingStatus(orderId);
        JSONObject statusObj = new JSONObject();
        switch (status) {
            case 1:
                statusObj.put("orderStatus", "cooking");
                break;
            case 2:
                statusObj.put("orderStatus", "ready");
                break;
            case 3:
                statusObj.put("orderStatus", "delivered");
                break;
            case 4:
                statusObj.put("orderStatus", "not started");
                break;
            default:
                break;
        }
        JSONObject jSONObject3 = new JSONObject();
        jSONObject3.put("status", statusObj);
        
        PrintWriter printWriter = response.getWriter();
        printWriter.print(jSONObject3);
        
    }
}
