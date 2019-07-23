package com.takeawayfood.api;

import com.takeawayfood.database.UserDetails;
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

public class SaveUserDetails extends HttpServlet {

    UserDetails userAllDetails = new UserDetails();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String phoneNo = null;
        String userEmail = null;
        String userName = null;
        
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
            userName = jSONObject.get("userName").toString();
            userEmail = jSONObject.get("userEmail").toString();
        } catch (ParseException ex) {
            Logger.getLogger(DisplayStudentTransaction.class.getName()).log(Level.SEVERE, null, ex);
        }
        long phoneNumber = Long.parseLong(phoneNo);
        userAllDetails.saveDetails(phoneNumber, userName, userEmail);
    }
}
