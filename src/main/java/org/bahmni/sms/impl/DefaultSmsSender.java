package org.bahmni.sms.impl;

import org.bahmni.sms.SMSSender;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

public class DefaultSmsSender implements SMSSender {

    public static final String ACCOUNT_SID = "AC176c1306e708bb48516e5a71f8a4a3eb";
    public static final String AUTH_TOKEN = "d295f2333b8dfae22ea94e90012c8488";

    @Override
    public String send(String senderInfo, String phoneNumber, String messageText) {
        try {

            // Construct data
            String message = "Body=" + messageText;
            String sender = "&From=" + senderInfo;
            String numbers = "&To=" + phoneNumber;

            // Send data
            HttpURLConnection conn = (HttpURLConnection) new URL("https://api.twilio.com/2010-04-01/Accounts/"+ACCOUNT_SID+"/Messages.json").openConnection();
            String data = message + sender +numbers;
            String encoding = Base64.getEncoder().encodeToString((ACCOUNT_SID + ":" + AUTH_TOKEN).getBytes());
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Basic " + encoding);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length", Integer.toString(data.length()));
            conn.getOutputStream().write(data.getBytes("UTF-8"));

            final BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            final StringBuffer stringBuffer = new StringBuffer();
            String line;
            while ((line = rd.readLine()) != null) {
                stringBuffer.append(line);
            }

            rd.close();

            return stringBuffer.toString();
        } catch (Exception e) {
            System.out.println("Error SMS "+e);
            return "Error "+e;
        }
    }
}
