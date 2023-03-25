package com.siswaSMKApp.zekul;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class home extends AppCompatActivity {
    SharedPreferences sharedPref;
    public void setText(String usernames) {
        TextView text = findViewById(R.id.username);
        text.setText(usernames);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        String apiKey = sharedPreferences.getString("apiKey", "none");

        new Thread(() -> {
            try {
                URL url = new URL("http://api.gud.my.id:2095/getUsername");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "application/json");

                JSONObject requestBody = new JSONObject();
                requestBody.put("apiKey", apiKey);

                OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
                writer.write(requestBody.toString());
                writer.flush();
                writer.close();

                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStream responseStream = connection.getInputStream();
                    String response = readStream(responseStream);
                    responseStream.close();
                    String username = new JSONObject(response).getString("username");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            TextView textView = findViewById(R.id.username);
                            textView.setText(username);
                        }
                    });

                } else {
                    // handle the error response
                }

                connection.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void setText(final TextView text,final String username) {
        runOnUiThread(() -> text.setText(username));
    }

    private String readStream(InputStream stream) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append('\n');
        }
        return sb.toString();
    }
}