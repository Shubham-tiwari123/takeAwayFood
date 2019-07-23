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
import com.takeawayfood.database.UserDetails;
import java.util.List;

public class GetUserDetails extends HttpServlet {
    UserDetails userAllDetails = new UserDetails();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String phoneNo = null;
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
        } catch (ParseException ex) {
            Logger.getLogger(DisplayStudentTransaction.class.getName()).log(Level.SEVERE, null, ex);
        }
        long phoneNumber = Long.parseLong(phoneNo);
        List<String> details = userAllDetails.getDetails(phoneNumber);
        String name = details.get(0);
        String emailId = details.get(1);
        JSONObject detailObj = new JSONObject();
        
        detailObj.put("userName", name);
        detailObj.put("userEmail", emailId);
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("result", detailObj);
        System.out.println(detailObj);
        PrintWriter printWriter = response.getWriter();
        
        printWriter.print(jSONObject);
    }
}
