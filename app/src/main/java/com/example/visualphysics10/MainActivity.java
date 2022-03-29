package com.example.visualphysics10;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.visualphysics10.adapter.ItemFragment;
import com.example.visualphysics10.database.PhysicsData;
import com.example.visualphysics10.lessonsFragment.L1Fragment;
import com.example.visualphysics10.physics.PhysicView;

public class MainActivity extends AppCompatActivity {
    FragmentManager fragmentManager = getSupportFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    ItemFragment itemFragment = new ItemFragment();
    public static boolean isFragment;
    private PhysicView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (itemFragment != null) {
            fragmentTransaction.add(R.id.container, itemFragment).commit();
        }
        addActionBar(false);
    }


    public void addActionBar(boolean isFragment) {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(v -> {
            onBackPressed();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        gameView = findViewById(R.id.physics_view);
        if (!PhysicsData.getThreadStop()){
            gameView.stopThread();
            PhysicsData.setThreadStop(true);
        }
        if (isFragment) {
            new AlertDialog.Builder(this)
                    .setTitle("Выход")
                    .setMessage("Вы уверены что хотите завершить урок ?")
                    .setCancelable(false)
                    .setPositiveButton("Завершить", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            if (itemFragment != null) {
                                ft.replace(R.id.container, itemFragment)
                                        .setCustomAnimations(R.anim.nav_default_pop_exit_anim, R.anim.nav_default_pop_enter_anim)
                                        .commit();
                            }
                        }
                    })
                    .setNegativeButton("Отмена", null)
                    .show();
        }else {
            MainActivity.this.finish();
        }
    }
}