package com.takeawayfood.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import com.takeawayfood.database.PaymentResponse;

public class PayUResponse extends HttpServlet {
    PaymentResponse paymentResponse = new PaymentResponse();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("hiii post");
        StringBuffer buffer = new StringBuffer();
        BufferedReader reader = request.getReader();
        String line;
        while((line = reader.readLine())!= null)
            buffer.append(line);
        String data = buffer.toString();
        JSONParser parser = new JSONParser();
        System.out.println("responseData:-"+data);
        
        try {
            JSONObject jSONObject = (JSONObject) parser.parse(data);
            String responseMsg = jSONObject.get("payuResponse").toString();
            
            JSONObject jSONObject2 = (JSONObject)parser.parse(responseMsg);
            String result = jSONObject2.get("result").toString();
            
            JSONObject jSONObject1 = (JSONObject)parser.parse(result);
            String status = jSONObject1.get("status").toString();
            String hash = jSONObject1.get("hash").toString();
            String udf1 = jSONObject1.get("udf1").toString();
            String udf2 = jSONObject1.get("udf2").toString();  
            String email = jSONObject1.get("email").toString();
            String firstname = jSONObject1.get("firstname").toString();
            String productinfo = jSONObject1.get("productinfo").toString();
            String amount = jSONObject1.get("amount").toString();
            String txnid = jSONObject1.get("txnid").toString();
            String key = jSONObject1.get("key").toString();
            String salt = "X3PUtWzgW3";
            
            System.out.println("payuResponse:-"+responseMsg);
            //System.out.println("result:-"+result);
            System.out.println("status:-"+status+""
                    + "\nhash:-"+hash+"\nudf1:-"+udf1+"\nudf2:-"+udf2+"\nemail:-"+email+
                    "\nfirstname:-"+firstname+"\nproductinfo:-"+productinfo+"\namount:-"+amount+
                    "\ntxnid:-"+txnid+"\nkey:-"+key+"\nsalt:-"+salt);
            
            String hashSequence = salt+"|"+status+"|udf1|udf2|||||"+email+"|"
                +firstname+"|"+productinfo+"|"+amount+"|"+txnid+"|"+key;
            
            String hash2 = generateHash("SHA-512", hashSequence);
            System.out.println("\nHash2:-"+hash2);
            
            JSONObject responseObj = new JSONObject();
            if(status.equals("success"))
                responseObj.put("respo", "success");
            else
                responseObj.put("respo", "failed");
            int orderID = Integer.parseInt(txnid);
            paymentResponse.updatePaymentStatus(status,orderID);
            
            JSONObject jSONObject3 = new JSONObject();
            jSONObject3.put("respoObj", responseObj);
            PrintWriter printWriter = response.getWriter();
            printWriter.print(jSONObject3);
            
        } catch (ParseException ex) {
            Logger.getLogger(PayUResponse.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public String generateHash(String type, String hashString){
        StringBuilder hash = new StringBuilder();
        MessageDigest messageDigest = null;
	try{
            messageDigest = MessageDigest.getInstance(type);
            messageDigest.update(hashString.getBytes());
            byte[] mdbytes = messageDigest.digest();
            for(byte hashByte : mdbytes) {
                     hash.append(Integer.toString((hashByte & 0xff) + 0x100, 16).substring(1));
            }
	}catch (NoSuchAlgorithmException e) {
            System.out.println("exception:-"+e);
	}
        System.out.println("hash:-"+hash.toString());
        return hash.toString();
    }
}
