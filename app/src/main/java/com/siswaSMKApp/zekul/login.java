package com.siswaSMKApp.zekul;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;


public class login extends AppCompatActivity {
    SharedPreferences sharedPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        addListenerButton();
    }

    private void addListenerButton() {
        Button loginBtn = (Button) findViewById(R.id.loginButton);
        ImageView back = (ImageView) findViewById(R.id.back);
        EditText email = (EditText) findViewById(R.id.emailLogin);
        EditText password = (EditText) findViewById(R.id.passwordLogin);
        back.setOnClickListener(v -> {
            finish();
        });

        loginBtn.setOnClickListener(v -> {
            String emailLogin = email.getText().toString();
            String passwordLogin = password.getText().toString();

            new Thread(() -> {
                try {
                    URL url = new URL("http://api.gud.my.id:2095/login");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setDoOutput(true);
                    connection.setRequestProperty("Content-Type", "application/json");

                    JSONObject requestBody = new JSONObject();
                    requestBody.put("email", emailLogin);
                    requestBody.put("password", passwordLogin);

                    OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
                    writer.write(requestBody.toString());
                    writer.flush();
                    writer.close();

                    if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        InputStream responseStream = connection.getInputStream();
                        String response = readStream(responseStream);
                        responseStream.close();
                        String apiKey = new JSONObject(response).getString("apiKey");
                        String status = new JSONObject(response).getString("status");

                        if (apiKey.length() > 0) {
                            SharedPreferences sharedPreferences = PreferenceManager
                                    .getDefaultSharedPreferences(this);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("apiKey", apiKey);
                            editor.apply();
                            Intent i = new Intent(this, MainActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);

                        } else {

                        }


                    } else {
                        // handle the error response
                    }

                    connection.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();


        });
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