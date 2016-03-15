package com.diamondoperators.android.magister;

import net.ilexiconn.magister.container.User;
import net.ilexiconn.magister.util.HttpUtil;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

/**
 * Test API stuff
 */
public class Aapje {
    public static final String USERNAME = "611155";
    public static final String PASSWORD = "¯\\_(ツ)_/¯";

    public static void main(String[] args) throws IOException {
        String version = httpGet("https://pantarijn.magister.net/api/versie");
        System.out.println(version);
        String logout = httpDelete("https://pantarijn.magister.net/api/sessies/huidige");
        System.out.println(logout);
        String data = "Gebruikersnaam=" + USERNAME + "&Wachtwoord=" + PASSWORD + "&IngelogdBlijven=true";
        System.out.println(data);
        Map<String, String> map = new HashMap<>();
        map.put("Gebruikersnaam", USERNAME);
        map.put("Wachtwoord", PASSWORD);
        map.put("IngelogdBlijven", "true");
        User user = new User(USERNAME, PASSWORD, true);

        InputStreamReader isr = HttpUtil.httpPost("https://pantarijn.magister.net/api/sessies", map);
        String session = readInput(isr);
        isr.close();
        //String session = httpPost("https://pantarijn.magister.net/api/sessies", data);
        System.out.println(session);

        /*try {
            InputStreamReader login = httpPost("https://pantarijn.magister.net/api/sessies/",
                    );
            System.out.println(readInput(login));
            login.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("IOException");
        }*/
    }

    private static String httpGet(String url) throws IOException {
        HttpsURLConnection connection = (HttpsURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("GET");
        connection.connect();
        InputStreamReader isr = new InputStreamReader(connection.getInputStream());
        String result = readInput(isr);
        isr.close();
        return result;
    }

    public static String httpDelete(String url) throws IOException {
        HttpsURLConnection connection = (HttpsURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("DELETE");
        connection.connect();
        InputStreamReader isr = new InputStreamReader(connection.getInputStream());
        String result = readInput(isr);
        isr.close();
        return result;
    }

    private static String httpPost(String url, String data) throws IOException {
        HttpsURLConnection connection = (HttpsURLConnection) new URL(url).openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        byte[] data_url = data.getBytes("UTF-8");
        DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
        outputStream.write(data_url);
        outputStream.flush();
        outputStream.close();
        InputStreamReader isr = new InputStreamReader(connection.getInputStream());
        String result = readInput(isr);
        isr.close();
        return result;
    }

    private static String readInput(InputStreamReader reader) throws IOException {
        BufferedReader buff = new BufferedReader(reader);
        StringBuilder result = new StringBuilder();
        String line;
        while ((line = buff.readLine()) != null) {
            result.append(line).append("\n");
        }
        return result.toString();
    }
}
