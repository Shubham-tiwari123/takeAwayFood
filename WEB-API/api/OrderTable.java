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
import com.takeawayfood.database.OrderTableDb;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import org.json.simple.JSONArray;

public class OrderTable extends HttpServlet {
    
    OrderTableDb  orderTableDb = new OrderTableDb();
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
        System.out.println("dataOrder:-"+data);
        try {
            JSONObject jSONObject = (JSONObject) parser.parse(data);
            String studentPhone = jSONObject.get("studentPhone").toString();
            String restaurantId = jSONObject.get("restaurantId").toString();
            String totalAmount = jSONObject.get("totalAmount").toString();
            String itemname = jSONObject.get("itemName").toString();
            String itemqnt = jSONObject.get("itemQnt").toString();
            String itemprice = jSONObject.get("itemPrice").toString();
            
            int restaurantUniqueNo = Integer.parseInt(restaurantId);
            long studentNumber = Long.parseLong(studentPhone);
            int billAmount = Integer.parseInt(totalAmount);
            
            itemname = itemname.substring(1, itemname.length()-1);
            itemqnt = itemqnt.substring(1,itemqnt.length()-1);
            itemprice = itemprice.substring(1,itemprice.length()-1);
            
            String items[] = itemname.split(",");
            String qnts[] = itemqnt.split(", ");
            String price[] = itemprice.split(", ");
            
            ArrayList<String> itemName = new ArrayList<>();
            ArrayList<Integer> itemQnt = new ArrayList<>();
            ArrayList<Integer> itemPrice = new ArrayList<>();
            
            itemName.addAll(Arrays.asList(items));
            System.out.println("1");
            for(int i=0;i<qnts.length;i++){
                System.out.println("2");
                itemQnt.add(Integer.parseInt(qnts[i]));
                itemPrice.add(Integer.parseInt(price[i]));
            }
            
            System.out.println("name:-"+itemName+"\n price:-"+itemPrice+"\n qnt:-"+itemQnt);
            
            int orderId = orderTableDb.getOrderId();
           
            boolean status = orderTableDb.insertOrderData(orderId+1,studentNumber, restaurantUniqueNo, billAmount,
                     itemName,itemQnt,itemPrice);
            JSONObject jSONObject3 = new JSONObject();
            if(status){
                int newOrderId=orderId+1;
                String orderID = String.valueOf(newOrderId);
                jSONObject3.put("msg", "True");
                jSONObject3.put("itemName", orderID);
                PrintWriter out = response.getWriter();
                out.println(jSONObject3);
            }else{
                jSONObject3.put("msg", "False");
                PrintWriter out = response.getWriter();
                out.print(jSONObject3);
            }
            
        } catch (ParseException ex) {
            Logger.getLogger(GenerateOtp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
