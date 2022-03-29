package com.example.visualphysics10.lessonsFragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.visualphysics10.InterimFragment;
import com.example.visualphysics10.MainActivity;
import com.example.visualphysics10.R;
import com.example.visualphysics10.database.App;
import com.example.visualphysics10.database.AppDataBase;
import com.example.visualphysics10.database.LessonData;
import com.example.visualphysics10.database.PhysicsData;
import com.example.visualphysics10.lessonInformFragment.L2FragInform;
import com.example.visualphysics10.objects.PhysicsModel;
import com.example.visualphysics10.physics.PhysicView;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

public class L2Fragment extends Fragment {
    private PhysicView gameView;
    public static boolean flagInput = false;
    public static boolean isMoving = false;
    private boolean startVisual = true;
    private boolean startToast = true;
    public int switchFab = 0;
    public EditText input_speed;
    public EditText input_radius;
    public TextView output_speed;
    public TextView output_radius;
    public TextView output_frequency;
    private FloatingActionButton saveInput;
    private FloatingActionButton restartInput;
    AppDataBase db = App.getInstance().getDatabase();
    LessonData lessonData = new LessonData();
    private boolean endInput = true;
    private TextView output_scale;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.l2_fragment, container, false);
        gameView = view.findViewById(R.id.physics_view);
        PhysicsModel.L2 = true;
        PhysicsModel.firstDraw = true;
        PhysicsData.setThreadStop(false);
        gameView.addModelGV(0, 0, 0, 0);
        initializationButton(view, switchFab);
        view.findViewById(R.id.bottom_sheet_event).setOnClickListener(v -> {
            switchBottomSheetFragment(startVisual, view);
        });

        output_scale = view.findViewById(R.id.scale);

        Objects.requireNonNull(initializationButton(view, 1)).setOnClickListener(v -> {
            if (flagInput) {
                Objects.requireNonNull(initializationButton(view, 1)).setImageResource(R.drawable.ic_baseline_pause_circle_outline_24);
                flagInput = false;
                isMoving = true;
                db.dataDao().getAllLiveData();
                gameView.updateMoving(lessonData.speed, 0, 0);
                PhysicsData.setSpeed(lessonData.speed);
                PhysicsData.setRadius(lessonData.radius);
                db.dataDao().delete(lessonData);
            } else if (startToast) {
                Toast.makeText(getContext(), "Для начала введите исходные данные", Toast.LENGTH_SHORT).show();
            } else {
                Objects.requireNonNull(initializationButton(view, 1)).setImageResource(R.drawable.ic_baseline_play_arrow_24);
                PhysicsModel.onStopClick = true;
            }

        });
        Objects.requireNonNull(initializationButton(view, 2)).setOnClickListener(v -> {
            if (PhysicsModel.onStopClick) {
                PhysicsModel.onRestartClick = true;
                startVisual = true;
                createDialogAndRestart();
            } else {
                Toast.makeText(getContext(), "Дождитесь завершения или нажмите паузу", Toast.LENGTH_SHORT).show();
            }
        });
        MainActivity.isFragment = true;
        return view;

    }
    private void createDialogAndRestart() {
        if(!PhysicsData.getThreadStop()){
            gameView.stopThread();
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, new InterimFragment())
                    .setCustomAnimations(R.anim.nav_default_enter_anim, R.anim.nav_default_exit_anim)
                    .addToBackStack(null)
                    .commit();
            PhysicsData.setThreadStop(true);
        }
    }

    private FloatingActionButton initializationButton(View view, int indexOfFab) {
        FloatingActionsMenu floatingActionsMenu = view.findViewById(R.id.action_menu);
        FloatingActionButton fab1 = view.findViewById(R.id.fab1_l2);
        FloatingActionButton fab2 = view.findViewById(R.id.fab2_l2);
        floatingActionsMenu.setOnFloatingActionsMenuUpdateListener(new FloatingActionsMenu.OnFloatingActionsMenuUpdateListener() {
            @Override
            public void onMenuExpanded() {
                if (endInput) {
                    fab1.setVisibility(View.VISIBLE);
                    fab2.setVisibility(View.VISIBLE);
                }else{
                    fab1.setVisibility(View.GONE);
                    fab2.setVisibility(View.GONE);
                }
            }

            @Override
            public void onMenuCollapsed() {
                fab1.setVisibility(View.GONE);
                fab2.setVisibility(View.GONE);
                endInput = true;
            }
        });

        switch (indexOfFab) {
            case 1:
                return fab1;
            case 2:
                return fab2;
            default:
                return null;
        }
    }

    private void switchBottomSheetFragment(boolean startVisual, View view) {
        if (startVisual) {
            toggleBottomSheetInput(view);
        } else {
            toggleBottomSheetOutput();
        }
    }

    public void toggleBottomSheetInput(View view1) {
        @SuppressLint("InflateParams")
        View view = getLayoutInflater().inflate(R.layout.l2_bottom_sheet, null);
        BottomSheetDialog dialog = new BottomSheetDialog(
                Objects.requireNonNull(getContext()), R.style.BottomSheetDialogTheme
        );
        dialog.setContentView(view);
        dialog.show();
        input_speed = view.findViewById(R.id.input_speed);
        input_radius = view.findViewById(R.id.input_radius);
        saveInput = view.findViewById(R.id.save);
        saveInput.setOnClickListener(v -> {
            try {
                saveData();
                startToast = false;
                startVisual = false;
                flagInput = true;
                setVisibilityFab(view1);
            } catch (Exception e) {
                e.printStackTrace();
            }
            dialog.dismiss();
        });

    }

    private void setVisibilityFab(View view) {
        endInput = false;
        Objects.requireNonNull(initializationButton(view, 1)).setVisibility(View.VISIBLE);
        Objects.requireNonNull(initializationButton(view, 2)).setVisibility(View.VISIBLE);
    }

    private void saveData() {
        lessonData.speed = Double.parseDouble(input_speed.getText().toString());
        lessonData.radius = Double.parseDouble(input_radius.getText().toString());
        rightInput(lessonData.speed, lessonData.radius);
        db.dataDao().insert(lessonData);
    }
    @SuppressLint("SetTextI18n")
    private void rightInput(double speed, double radius) {
        if(speed > 100){
            lessonData.speed = speed / 10;
            if(output_scale!=null){
                output_scale.setText("Маштаб плоскости составляет 1 к 6");
            }
        }
        if (radius > 600){
            lessonData.radius = radius / 6;
            if(output_scale!=null){
                output_scale.setText("Маштаб плоскости составляет 1 к 10");
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private void toggleBottomSheetOutput() {
        View view = getLayoutInflater().inflate(R.layout.l2_bottom_sheet_output, null);
        BottomSheetDialog dialog = new BottomSheetDialog(
                Objects.requireNonNull(getContext()), R.style.BottomSheetDialogTheme
        );
        restartInput = view.findViewById(R.id.restart);
        output_speed = view.findViewById(R.id.output_speed);
        output_speed.setText((int) lessonData.speed + " [м/с] - скорость");
        output_radius = view.findViewById(R.id.output_radius);
        output_radius.setText((int) lessonData.radius + " [м] - радиус");
        restartInput.setOnClickListener(v -> {
            startVisual = true;
            createDialogAndRestart();
            db.dataDao().delete(lessonData);
            dialog.dismiss();
        });
        Button toNextFrag = view.findViewById(R.id.toNextFrag);
        toNextFrag.setOnClickListener(v -> {
            dialog.dismiss();
            gameView.stopThread();
            PhysicsData.setThreadStop(true);
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.nav_default_enter_anim, R.anim.nav_default_exit_anim)
                    .replace(R.id.container, new L2FragInform())
                    .addToBackStack(null)
                    .commit();
        });
        dialog.setContentView(view);
        dialog.show();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


}