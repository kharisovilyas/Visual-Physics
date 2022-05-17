package com.example.visualphysics10.lessonsFragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.visualphysics10.MainActivity;
import com.example.visualphysics10.R;
import com.example.visualphysics10.database.App;
import com.example.visualphysics10.database.AppDataBase;
import com.example.visualphysics10.database.LessonData;
import com.example.visualphysics10.database.PhysicsData;
import com.example.visualphysics10.databinding.L2FragmentBinding;
import com.example.visualphysics10.inform.input.FullScreenDialog;
import com.example.visualphysics10.inform.output.FullScreenInfo;
import com.example.visualphysics10.inform.test.FragmentTest;
import com.example.visualphysics10.objects.PhysicsModel;
import com.example.visualphysics10.physics.PhysicView;
import com.example.visualphysics10.ui.MainFlag;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

public class L2Fragment extends Fragment {
    private PhysicView gameView;
    private L2FragmentBinding binding;
    public static boolean flagInput = false;
    public static boolean isMoving = false;
    private boolean startVisual = true;
    private boolean startToast = true;
    public int switchFab = 0;
    public EditText input_speed;
    public EditText input_radius;
    public TextView output_speed;
    private FloatingActionButton info;
    private FloatingActionButton play;
    AppDataBase db = App.getInstance().getDatabase();
    LessonData lessonData = FullScreenDialog.getInstance();
    private boolean endInput = true;
    private TextView output_scale;
    private int count = 0;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = L2FragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        gameView = binding.physicsView;
        addToolbar();
        PhysicsModel.L2 = true;
        PhysicsModel.L2start = true;
        PhysicsModel.firstDraw = true;
        MainFlag.setThreadStop(false);
        gameView.addModelGV();
        play = binding.play;
        FloatingActionButton restart = binding.restart;
        FloatingActionButton startInput = binding.startInput;
        FloatingActionButton startTest = binding.startTest;
        info = binding.info;
        play.setOnClickListener(v -> {
            if (count % 2 == 0) playClick();
            else pauseClick();
            count++;
        });
        restart.setOnClickListener(v -> {
            startVisual = true;
            createDialog();
        });
        startInput.setOnClickListener(v -> {
            createdFullScreenDialog();
        });

        startTest.setOnClickListener(v -> {
            startTesting();
        });

        info.setOnClickListener(v -> {
            gameView.stopThread();
            createdFullScreenInfo();
        });
        outputData();
    }

    private void outputData() {

    }

    private void addToolbar() {
        Toolbar toolbar = binding.toolbar;
        ((MainActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.arrow_back);
        toolbar.setTitle(R.string.titleL2);
        toolbar.setNavigationOnClickListener(v -> {
            getActivity().onBackPressed();
        });
        toolbar.inflateMenu(R.menu.icon_toolbar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                createDrawer();
                return true;
            }
        });
    }

    private void createDrawer() {
        DrawerLayout drawerLayout = binding.drawerLayout;
        drawerLayout.openDrawer(GravityCompat.END);
    }

    private void pauseClick() {
        play.setImageResource(R.drawable.play_arrow);
        gameView.stopDraw(0);

    }

    private void playClick() {
        play.setImageResource(R.drawable.pause_circle);
        flagInput = false;
        isMoving = true;
        db.dataDao().getAllLiveData();
        info.setVisibility(View.VISIBLE);
        gameView.updateMoving(lessonData.speed, 0, 0);
        PhysicsData.setSpeed(lessonData.speed);
        PhysicsData.setRadius(lessonData.radius);
        db.dataDao().delete(lessonData);
    }

    private void createdFullScreenInfo() {
        DialogFragment dialogInfoFragment = FullScreenInfo.newInstance();
        dialogInfoFragment.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), "info");
    }

    private void createdFullScreenDialog() {
        DialogFragment dialogFragment = FullScreenDialog.newInstance();
        dialogFragment.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), "input");
    }


    private void startTesting() {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.nav_default_enter_anim, R.anim.nav_default_exit_anim)
                .replace(R.id.container, new FragmentTest())
                .addToBackStack(null)
                .commit();
    }

    private void createDialog() {
        play.setImageResource(R.drawable.play_arrow);
        count += count%2;
        new MaterialAlertDialogBuilder(Objects.requireNonNull(getContext()))
                .setTitle("Перезапуск")
                .setMessage("Сохранить введенные данные?")
                .setCancelable(false)
                .setPositiveButton("Сохранить", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        gameView.restartClick(0);
                    }
                })
                .setNegativeButton("Ввести новые", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        gameView.restartClick(0);
                        lessonData = new LessonData();
                    }
                })
                .show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }


    private void saveData() {
        lessonData.speed = Double.parseDouble(input_speed.getText().toString());
        lessonData.radius = Double.parseDouble(input_radius.getText().toString());
        db.dataDao().insert(lessonData);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


}