package com.takeawayfood.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class GenerateHash extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String amount = null;
        String key = null;
        String txnid = null;
        String productinfo=null;
        String firstname=null;
        String email=null;
        String udf1="",udf2="",udf3="",udf4="",udf5="";
        String salt=null;
        
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
            amount = jSONObject.get("amount").toString();
            txnid = jSONObject.get("txnid").toString();
            productinfo = jSONObject.get("productinfo").toString();
            firstname = jSONObject.get("firstName").toString();
            email = jSONObject.get("email").toString();
            key = "t5BFXZtl";
            salt = "X3PUtWzgW3";
            
        } catch (ParseException ex) {
            System.out.println("Exception:-"+ex);
        }
        
        String hashSequence = key+"|"+txnid+"|"+amount+"|"+productinfo+"|"+firstname+"|"
                +email+"|"+udf1+"|"+udf2+"|"+udf3+"|"+udf4+"|"+udf5+"||||||"+salt;
        
        String hash = generateHash("SHA-512", hashSequence);
        
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("hashValue", hash);
        
        JSONObject jSONObject1= new JSONObject();
        jSONObject1.put("hashObj", jSONObject);
        
        PrintWriter printWriter = response.getWriter();
        printWriter.print(jSONObject1);
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
