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
import com.example.visualphysics10.lessonInformFragment.L3FragInform;
import com.example.visualphysics10.objects.PhysicsModel;
import com.example.visualphysics10.physics.PhysicView;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

public class L3Fragment extends Fragment {
    private PhysicView gameView;
    public static boolean flagInput = false;
    public static boolean isMoving = false;
    private boolean startVisual = true;
    private boolean startToast = true;
    public int switchFab = 0;
    public EditText input_speed;
    public EditText input_force;
    public EditText input_acc;
    public TextView output_speed;
    public TextView output_force;
    public TextView output_distance;
    public TextView output_acc;
    public TextView output_time;
    private TextView output_scale;

    public static double l3acc = 0;
    private FloatingActionButton saveInput;
    private FloatingActionButton restartInput;
    AppDataBase db = App.getInstance().getDatabase();
    LessonData lessonData = new LessonData();
    private boolean endInput = true;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.l3_fragment, container, false);
        PhysicsModel.L3 = true;
        PhysicsData.setThreadStop(false);
        gameView = view.findViewById(R.id.physics_view);
        gameView.addModelGV();
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
                PhysicsData.setAcc(lessonData.acc);
                PhysicsData.setDistance(lessonData.distance);
                PhysicsData.setSpeed(lessonData.speed);
                PhysicsData.setForce(lessonData.strength);
                gameView.updateMoving(lessonData.speed, 0, 0);
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
        FloatingActionButton fab1 = view.findViewById(R.id.fab1_l3);
        FloatingActionButton fab2 = view.findViewById(R.id.fab2_l3);
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
            Log.d("input", "true");
        } else {
            toggleBottomSheetOutput();
            Log.d("output", "true");
        }
    }

    public void toggleBottomSheetInput(View view1) {
        @SuppressLint("InflateParams")
        View view = getLayoutInflater().inflate(R.layout.l3_bottom_sheet, null);
        BottomSheetDialog dialog = new BottomSheetDialog(
                Objects.requireNonNull(getContext()), R.style.BottomSheetDialogTheme
        );
        dialog.setContentView(view);
        dialog.show();
        input_speed = view.findViewById(R.id.input_speed);
        input_acc = view.findViewById(R.id.input_acc);
        input_force = view.findViewById(R.id.input_force);
        saveInput = view.findViewById(R.id.save);
        saveInput.setOnClickListener(v -> {
            try {
                saveData();
                startToast = false;
                startVisual = false;
                setTextForBS(view1);
                setVisibilityFab(view1);
            } catch (Exception e) {
                e.printStackTrace();
            }
            flagInput = true;
            dialog.dismiss();
        });
    }

    private void setTextForBS(View view) {
        TextView textForBS = view.findViewById(R.id.text_for_bs);
        textForBS.setText("Сохраненные данные и ...");
    }

    private void setVisibilityFab(View view) {
        endInput = false;
        Objects.requireNonNull(initializationButton(view, 1)).setVisibility(View.VISIBLE);
        Objects.requireNonNull(initializationButton(view, 2)).setVisibility(View.VISIBLE);
    }

    private void saveData() {
        lessonData.speed = Double.parseDouble(input_speed.getText().toString());
        lessonData.strength = Double.parseDouble(input_force.getText().toString());
        lessonData.acc = Double.parseDouble(input_acc.getText().toString());
        rightInput(lessonData.speed, lessonData.acc);
        db.dataDao().insert(lessonData);
    }
    private void rightInput(double speed, double acc) {
        if (speed > 50 && acc > 50 && speed < 100 && acc < 100){
            lessonData.speed = speed / 5;
            lessonData.acc = acc / 5;
            if(output_scale!=null){
                output_scale.setText("Маштаб времени составляет 1 к 5");
            }
        }
        if (speed > 100 && acc > 100){
            lessonData.speed = speed / 10;
            lessonData.acc = acc / 10;
            if(output_scale!=null){
                output_scale.setText("Маштаб времени составляет 1 к 10");
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private void toggleBottomSheetOutput() {
        View view = getLayoutInflater().inflate(R.layout.l3_bottom_sheet_output, null);
        BottomSheetDialog dialog = new BottomSheetDialog(
                Objects.requireNonNull(getContext()), R.style.BottomSheetDialogTheme
        );
        restartInput = view.findViewById(R.id.restart);
        output_speed = view.findViewById(R.id.output_speed);
        output_speed.setText((int) lessonData.speed + " [м/с] - скорость");
        output_distance = view.findViewById(R.id.output_distance);
        output_distance.setText("1000 [м] - расстояние");
        output_force = view.findViewById(R.id.output_force);
        output_force.setText((int) lessonData.strength + " [Н] - сила");
        output_acc = view.findViewById(R.id.output_acc);
        output_acc.setText((int) lessonData.acc + " [м/с^2] - ускорение");
        restartInput.setOnClickListener(v -> {
            startVisual = true;
            PhysicsData.setThreadStop(false);
            db.dataDao().delete(lessonData);
            dialog.dismiss();
        });
        Button toNextFrag = view.findViewById(R.id.toNextFrag);
        toNextFrag.setOnClickListener(v -> {
            gameView.stopThread();
            PhysicsData.setThreadStop(true);
            dialog.dismiss();
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.nav_default_enter_anim, R.anim.nav_default_exit_anim)
                    .replace(R.id.container, new L3FragInform())
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