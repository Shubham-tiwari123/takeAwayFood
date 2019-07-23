package com.takeawayfood.api;

import com.takeawayfood.extraclasses.FinalBillDetails;
import com.takeawayfood.database.DisplayFinalBill;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
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

public class GetOrderStatus extends HttpServlet {
    DisplayFinalBill bill = new DisplayFinalBill();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String ordersId = null;
        System.out.println("servers");
        List<FinalBillDetails> billDetailsList = null, extraDetails;
        DisplayFinalBill bill = new DisplayFinalBill();
        FinalBillDetails billDetail = null;
        
        
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
            ordersId = jSONObject.get("orderId").toString();
        } catch (ParseException ex) {
            Logger.getLogger(DisplayStudentTransaction.class.getName()).log(Level.SEVERE, null, ex);
        }
        int orderID = Integer.parseInt(ordersId);
        JSONObject msgObj = new JSONObject();
        if (bill.orderStatus(orderID) == 1) {
            msgObj.put("msgs", "accepted");
        } else if (bill.orderStatus(orderID) == 2) {
            msgObj.put("msgs", "rejected");
        } else if (bill.orderStatus(orderID) == 0) {
            msgObj.put("msgs", "waiting");
        }
        JSONObject jSONObject3 = new JSONObject();
        jSONObject3.put("msgObj", msgObj);
        PrintWriter out = response.getWriter();
        out.println(jSONObject3);
    }
}
