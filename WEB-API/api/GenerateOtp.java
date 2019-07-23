package com.takeawayfood.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class GenerateOtp extends HttpServlet {
    
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
            String phoneNumber= jSONObject.get("phoneNumber").toString();
            System.out.println("phoneNumber:-"+phoneNumber);
            
            //save phone number to database
            
            //generating otp for user
            
            String numbers = "0123456789";
            int otpLength= 5;
            Character[] otp = new Character[otpLength];
            Random randomOtp = new Random();
            StringBuilder builder = new StringBuilder();
            for(int i=0;i<otpLength;i++){
                builder.append(randomOtp.nextInt(numbers.length()));
            }
            System.out.println("OTP:-"+builder);
            
            //save otp,date and time in db 
            
            //pass the otp to the firebase for msg;
            
            
        } catch (ParseException ex) {
            Logger.getLogger(GenerateOtp.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
