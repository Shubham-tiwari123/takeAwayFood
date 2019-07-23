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
import com.takeawayfood.extraclasses.FinalBillDetails;
import com.takeawayfood.database.DisplayFinalBill;
import java.util.LinkedList;
import java.util.List;

public class FinalBillSummery extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String ordersId = null;
        List<FinalBillDetails> billDetailsList = null, extraDetails;
        DisplayFinalBill bill = new DisplayFinalBill();
        FinalBillDetails billDetail = null;

        StringBuffer buffer = new StringBuffer();
        BufferedReader reader = request.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
        }
        String data = buffer.toString();
        JSONParser parser = new JSONParser();
        System.out.println("dataOrder:-" + data);

        try {
            JSONObject jSONObject = (JSONObject) parser.parse(data);
            ordersId = jSONObject.get("orderId").toString();
        } catch (ParseException ex) {
            Logger.getLogger(DisplayStudentTransaction.class.getName()).log(Level.SEVERE, null, ex);
        }
        int orderID = Integer.parseInt(ordersId);

        billDetailsList = bill.displayFinallBill(orderID);
        JSONObject nameObj = new JSONObject();
        JSONObject priceObj = new JSONObject();
        JSONObject qntObj = new JSONObject();
        JSONObject orderTime = new JSONObject();
        JSONObject totalAmount = new JSONObject();
        

        for (int i = 0; i < billDetailsList.size(); i++) {
            billDetail = billDetailsList.get(i);
            nameObj.put(i, billDetail.itemName);
            String itemValue = String.valueOf(billDetail.itemPrice);
            priceObj.put(i, itemValue);
            String itemQnt = String.valueOf(billDetail.itemQnt);
            qntObj.put(i, itemQnt);
        }

        extraDetails = bill.extraBillDetails(orderID);
        billDetail = extraDetails.get(0);
        orderTime.put("orderTime", (billDetail.orderTime).toString().substring(0, 5));
        int billValue = billDetail.totalAmount;
        String billAmount = String.valueOf(billValue);
        totalAmount.put("orderAmount", billAmount);

        JSONObject jSONObject3 = new JSONObject();
        jSONObject3.put("itemName", nameObj);
        jSONObject3.put("itemPrice", priceObj);
        jSONObject3.put("itemQnt", qntObj);
        jSONObject3.put("orderTime", orderTime);
        jSONObject3.put("totalAmount", totalAmount);
        PrintWriter out = response.getWriter();
        out.println(jSONObject3);
    }
}
