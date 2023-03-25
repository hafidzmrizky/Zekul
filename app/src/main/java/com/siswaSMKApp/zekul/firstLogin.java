package com.siswaSMKApp.zekul;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RadioButton;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class firstLogin extends AppCompatActivity {
    SharedPreferences sharedPref;
    private RadioButton radio_english;
    private Button languageBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_login);
        sharedPref = getSharedPreferences("notuserdata", MODE_PRIVATE);
        addListenerButton();
    }

    private void addListenerButton() {
        radio_english = (RadioButton) findViewById(R.id.radio_english);
        languageBtn = (Button) findViewById(R.id.languageBtn);
        Button loginBtn  = (Button) findViewById(R.id.loginBtn);
        Button registerBtn  = (Button) findViewById(R.id.registerBtn);

        loginBtn.setOnClickListener(v -> {
            Intent i = new Intent(this, login.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        });

        registerBtn.setOnClickListener(v -> {
            Intent i = new Intent(this, register.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        });




        languageBtn.setOnClickListener(v -> {
            if (radio_english.isChecked()) {
                // English
                sharedPref.edit().putString("language", "en").apply();
                Animation slideDown = AnimationUtils.loadAnimation(this, R.anim.slidedown);
                View selectLanguage = findViewById(R.id.selectLanguage);
                selectLanguage.setAnimation(slideDown);
                selectLanguage.setVisibility(View.GONE);
                View behind = findViewById(R.id.behind);
                behind.setAlpha(1);

            } else {
                // Indonesia
            }
        });
    }
}