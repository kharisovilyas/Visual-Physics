package com.example.visualphysics10.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.visualphysics10.MainActivity;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        thread.start();
        super.onCreate(savedInstanceState);
        try {
            thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    private static final Thread thread = new Thread();
    @Override
    protected void onDestroy() {
        super.onDestroy();
        thread.interrupt();
    }
}