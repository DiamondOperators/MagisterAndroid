package com.diamondoperators.android.magister;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Test API stuff
 */
public class Aapje {
    private static final String USERNAME = "611155";
    private static final String PASSWORD = "¯\\_(ツ)_/¯";

    public static void main(String[] args) {
        try {
            InputStreamReader login = httpPost("https://pantarijn.magister.net/api/sessies/",
                    "Gebruikersnaam=" + USERNAME + "&Wachtwoord=" + PASSWORD + "&IngelogdBlijven=true");
            System.out.println(readInput(login));
            login.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("IOException");
        }
    }

    private static InputStreamReader httpPost(String url, String data) throws IOException {
        HttpsURLConnection connection = (HttpsURLConnection) new URL(url).openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        // connection.setRequestProperty("Cookie", getCurrentCookies());
        // connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        byte[] data_url = data.getBytes("UTF-8");
        DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
        outputStream.write(data_url);
        outputStream.flush();
        outputStream.close();
        //storeCookies(connection);
        return new InputStreamReader(connection.getInputStream());
    }

    private static String readInput(InputStreamReader reader) throws IOException {
        BufferedReader buff = new BufferedReader(reader);
        StringBuilder result = new StringBuilder();
        String line;
        while ((line = buff.readLine()) != null) {
            result.append(line).append("\n");
        }
        reader.close();
        return result.toString();
    }
}
