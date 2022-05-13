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
import com.example.visualphysics10.ui.MainFlag;
import com.example.visualphysics10.ui.MainHelper;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class MainActivity extends AppCompatActivity implements MainHelper {
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
    }

    @Override
    public void onBackPressed() {
        gameView = findViewById(R.id.physicsView);
        if (!MainFlag.getThreadStop()) {
            gameView.stopThread();
            MainFlag.setThreadStop(true);
        }

        if(MainActivity.isFragment){
            onBackPressedFragment();

        }else{
            new MaterialAlertDialogBuilder(this)
                    .setTitle("Выход")
                    .setMessage("Вы уверены что хотите выйти из приложения ?")
                    .setCancelable(false)
                    .setPositiveButton("Выйти", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            MainActivity.this.finish();
                        }
                    })
                    .setNegativeButton("Назад", null)
                    .show();
        }
    }
    @Override
    public void onBackPressedFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        if (!MainFlag.getThreadStop()) {
            gameView.stopThread();
            MainFlag.setThreadStop(true);
        }
        new MaterialAlertDialogBuilder(this)
                .setTitle("Выход")
                .setMessage("Вы уверены что хотите завершить урок ?")
                .setCancelable(false)
                .setPositiveButton("Завершить", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ft.replace(R.id.container, new ItemFragment())
                                .setCustomAnimations(R.anim.nav_default_pop_enter_anim, R.anim.nav_default_pop_exit_anim)
                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                                .commit();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Отмена", null)
                .show();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}