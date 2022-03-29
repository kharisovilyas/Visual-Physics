package com.example.visualphysics10.lessonsFragment;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
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
import com.example.visualphysics10.lessonInformFragment.L5FragInform;
import com.example.visualphysics10.objects.PhysicsModel;
import com.example.visualphysics10.physics.PhysicView;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

public class L5Fragment extends Fragment {
    private PhysicView gameView;
    public static boolean isMoving = false;
    public static boolean flagInput = false;
    private boolean startToast = true;
    private boolean startVisual = true;
    private boolean endInput = true;
    public int switchFab = 0;
    private EditText input_speed;
    private EditText input_speed2;
    private EditText input_mass1;
    private EditText input_mass2;
    private TextView output_speed;
    private TextView output_speed2;
    private TextView output_mass1;
    private TextView output_mass2;
    private TextView elastic;
    private TextView output_scale;
    private FloatingActionButton saveInput;
    private FloatingActionButton restartInput;
    private boolean elasticImpulse;
    AppDataBase db = App.getInstance().getDatabase();
    LessonData lessonData = new LessonData();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.l5_fragment, container, false);
        PhysicsModel.L5 = true;
        PhysicsData.setThreadStop(false);
        gameView = view.findViewById(R.id.physics_view);
        gameView.addModelGVI(30, 720, 0, 0, 0);
        gameView.addModelGVI(800, 720, 0, 0, 1);
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
                PhysicsData.setElasticImpulse(lessonData.elasticImpulse);
                PhysicsData.setSpeed(lessonData.speed);
                PhysicsData.setSpeed2(lessonData.speed2);
                gameView.updateMoving(lessonData.speed, 0, 0);
                gameView.updateMoving(-lessonData.speed2, 0, 1);
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
        if (!PhysicsData.getThreadStop()) {
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

    private void switchBottomSheetFragment(boolean startVisual, View view) {
        if (startVisual) {
            toggleBottomSheetInput(view);
            Log.d("input", "true");
        } else {
            toggleBottomSheetOutput();
            Log.d("output", "true");
        }
    }


    private FloatingActionButton initializationButton(View view, int indexOfFab) {
        FloatingActionsMenu floatingActionsMenu = view.findViewById(R.id.action_menu);
        FloatingActionButton fab1 = view.findViewById(R.id.fab1_l5);
        FloatingActionButton fab2 = view.findViewById(R.id.fab2_l5);
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


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void toggleBottomSheetInput(View view1) {
        @SuppressLint("InflateParams")
        View view = getLayoutInflater().inflate(R.layout.l5_bottom_sheet, null);
        BottomSheetDialog dialog = new BottomSheetDialog(
                requireContext(), R.style.BottomSheetDialogTheme
        );
        dialog.setContentView(view);
        dialog.show();
        input_speed = view.findViewById(R.id.input_speed1);
        input_speed2 = view.findViewById(R.id.input_speed2);
        input_mass1 = view.findViewById(R.id.input_mass1);
        input_mass2 = view.findViewById(R.id.input_mass2);
        saveInput = view.findViewById(R.id.save);
        RadioButton elastic = view.findViewById(R.id.elastic);
        RadioButton noElastic = view.findViewById(R.id.noElastic);
        elastic.setOnClickListener(v -> {
            elasticImpulse = true;
        });
        noElastic.setOnClickListener(v -> {
            elasticImpulse = false;
        });

        saveInput.setOnClickListener(v -> {
            try {
                saveData();
                startToast = false;
                startVisual = false;
                setVisibilityFab(view1);
            } catch (Exception e) {
                e.printStackTrace();
            }
            flagInput = true;
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
        lessonData.speed2 = Double.parseDouble(input_speed2.getText().toString());
        lessonData.mass1 = Double.parseDouble(input_mass1.getText().toString());
        lessonData.mass2 = Double.parseDouble(input_mass2.getText().toString());
        lessonData.elasticImpulse = elasticImpulse;
        rightInput(lessonData.speed, lessonData.speed2, lessonData.mass1, lessonData.mass2);
        db.dataDao().insert(lessonData);
    }

    private void rightInput(double speed, double speed2, double mass1, double mass2) {
        if (speed > 100 && speed2 > 100){
            lessonData.speed = speed / 10;
            lessonData.speed2 = speed2 / 10;
            if(output_scale!=null){
                output_scale.setText("Маштаб времени составляет 1 к 10");
            }
        }
        if (mass1 > 100 && mass2 > 100){
            lessonData.mass1 = mass1 / 10;
            lessonData.mass2 = mass2 / 10;
            if(output_scale!=null){
                output_scale.setText("Маштаб времени составляет 1 к 10");
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private void toggleBottomSheetOutput() {
        View view = getLayoutInflater().inflate(R.layout.l5_bottom_sheet_output, null);
        BottomSheetDialog dialog = new BottomSheetDialog(
                requireContext(), R.style.BottomSheetDialogTheme
        );
        restartInput = view.findViewById(R.id.restart);

        output_speed = view.findViewById(R.id.output_speed);
        output_speed.setText((int) lessonData.speed + " [м/с] - скорость");

        output_mass1 = view.findViewById(R.id.output_mass1);
        output_mass1.setText((int) lessonData.mass1 + " [кг] - масса");

        output_speed2 = view.findViewById(R.id.output_speed2);
        output_speed2.setText((int) lessonData.speed2 + " [м/с] - скорость");

        output_mass2 = view.findViewById(R.id.output_mass2);
        output_mass2.setText((int) lessonData.mass2 + " [кг] - масса");

        elastic = view.findViewById(R.id.output_elastic);

        if (PhysicsData.getElasticImpulse()) {
            elastic.setText("Упругое");
        } else {
            elastic.setText("Неупругое");
        }

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
                    .replace(R.id.container, new L5FragInform())
                    .addToBackStack(null)
                    .commit();
        });
        dialog.setContentView(view);
        dialog.show();
    }
}
