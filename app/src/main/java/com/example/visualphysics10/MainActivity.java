package com.example.visualphysics10;

import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.visualphysics10.adapter.ItemFragment;
import com.example.visualphysics10.databinding.ActivityMainBinding;
import com.example.visualphysics10.objects.PhysicsModel;
import com.example.visualphysics10.ui.MainFlag;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class MainActivity extends AppCompatActivity{
    FragmentManager fragmentManager = getSupportFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    ItemFragment itemFragment = new ItemFragment();
    public static boolean isFragment;
    private ActivityMainBinding binding;
    private int count;
    private MediaPlayer collision;
    private MediaPlayer end;
    private MediaPlayer landing;
    private MediaPlayer rotation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        if (itemFragment != null) {
            fragmentTransaction.add(R.id.container, itemFragment).commit();
        }
        addMediaPlayer();
    }

    private void addMediaPlayer() {
        end = MediaPlayer.create(this, R.raw.end);
        rotation = MediaPlayer.create(this, R.raw.rotation);
        landing = MediaPlayer.create(this, R.raw.end);
        collision = MediaPlayer.create(this, R.raw.end);
        PhysicsModel.addSound(end, rotation, landing, collision);
    }

    @Override
    public void onBackPressed() {

        count = fragmentManager.getBackStackEntryCount();

        if (count == 0) {
            exitApp();
        } else {
            exitFragment();
            MainFlag.setNotLesson(false);
        }

    }

    private void exitFragment() {
        if (count > 1 || MainFlag.isNotLesson()) fragmentManager.popBackStack();
        else new MaterialAlertDialogBuilder(this)
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