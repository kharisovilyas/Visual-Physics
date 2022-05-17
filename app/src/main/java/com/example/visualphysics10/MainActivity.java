package com.example.visualphysics10;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.visualphysics10.adapter.ItemFragment;
import com.example.visualphysics10.databinding.ActivityMainBinding;
import com.example.visualphysics10.physics.PhysicView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class MainActivity extends AppCompatActivity{
    FragmentManager fragmentManager = getSupportFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    ItemFragment itemFragment = new ItemFragment();
    public static boolean isFragment;
    private PhysicView gameView;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        if (itemFragment != null) {
            fragmentTransaction.add(R.id.container, itemFragment).commit();
        }
        createdSplash();
    }

    private void createdSplash() {
        Thread thread = new Thread();
        thread.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        thread.interrupt();
    }


    @Override
    public void onBackPressed() {

        int count = fragmentManager.getBackStackEntryCount();

        if (count == 0) {
            exitApp();
        } else {
            exitFragment();
            getFragmentManager().popBackStack();
        }

    }

    private void exitFragment() {
        new MaterialAlertDialogBuilder(this)
                .setTitle("Выход")
                .setMessage("Вы уверены что хотите завершить урок ?")
                .setCancelable(false)
                .setPositiveButton("Завершить", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        fragmentManager.popBackStack();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Отмена", null)
                .show();
    }

    private void exitApp() {
        new MaterialAlertDialogBuilder(this)
                .setTitle("Выход")
                .setMessage("Вы уверены что хотите выйти из приложения ?")
                .setCancelable(false)
                .setPositiveButton("Выйти", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        MainActivity.super.onBackPressed();
                    }
                })
                .setNegativeButton("Назад", null)
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}