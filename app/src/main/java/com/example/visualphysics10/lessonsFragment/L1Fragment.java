package com.example.visualphysics10.lessonsFragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.visualphysics10.InterimFragment;
import com.example.visualphysics10.MainActivity;
import com.example.visualphysics10.R;
import com.example.visualphysics10.database.App;
import com.example.visualphysics10.database.AppDataBase;
import com.example.visualphysics10.database.LessonData;
import com.example.visualphysics10.database.PhysicsData;
import com.example.visualphysics10.lessonInformFragment.L1FragInform;
import com.example.visualphysics10.objects.PhysicsModel;
import com.example.visualphysics10.physics.PhysicView;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DecimalFormat;
import java.util.Objects;

public class L1Fragment extends Fragment {

    private PhysicView gameView;
    private boolean flagInput = false;
    private boolean startToast = true;
    public static boolean isMoving = false;
    private boolean startVisual = true;
    public int switchFab = 0;
    public EditText input_speed;
    public EditText input_acc;
    public TextView output_speed;
    public TextView output_speedEnd;
    public TextView output_distance;
    public TextView output_acc;
    public TextView output_scale;
    AppDataBase db = App.getInstance().getDatabase();
    LessonData lessonData = new LessonData();
    private boolean endInput = true;
    private int countListener = 0;

    @SuppressLint("ResourceType")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.l1_fragment, container, false);

        gameView = view.findViewById(R.id.physics_view);
        PhysicsModel.L1 = true;
        PhysicsData.setThreadStop(false);
        gameView.addModelGV(0);
        initializationButton(view, switchFab);
        view.findViewById(R.id.bottom_sheet_event).setOnClickListener(v -> {
            switchBottomSheetFragment(startVisual, view);
        });

        output_scale = view.findViewById(R.id.scale);

        Objects.requireNonNull(initializationButton(view, 1)).setOnClickListener(v -> {
            countListener++;
            if (flagInput && countListener % 2 != 0) {
                Objects.requireNonNull(initializationButton(view, 1)).setImageResource(R.drawable.ic_baseline_pause_circle_outline_24);
                flagInput = false;
                isMoving = true;
                db.dataDao().getAllLiveData();
                PhysicsData.setAcc(lessonData.acc);
                PhysicsData.setDistance(lessonData.distance);
                gameView.updateMoving(lessonData.speed, 0, 0);
                db.dataDao().delete(lessonData);
            } else if (countListener % 2 == 0) {
                PhysicsModel.onStopClick = true;
                Objects.requireNonNull(initializationButton(view, 1)).setImageResource(R.drawable.ic_baseline_play_arrow_24);
            } else {
                PhysicsModel.onStopClick = false;
                Objects.requireNonNull(initializationButton(view, 1)).setImageResource(R.drawable.ic_baseline_pause_circle_outline_24);
            }
        });
        Objects.requireNonNull(initializationButton(view, 2)).setOnClickListener(v -> {
            PhysicsModel.onRestartClick = true;
            startVisual = true;
            createDialogAndRestart();
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
        } else {
            toggleBottomSheetOutput();
        }
    }


    private FloatingActionButton initializationButton(View view, int indexOfFab) {
        FloatingActionsMenu floatingActionsMenu = view.findViewById(R.id.action_menu_l1);
        FloatingActionButton fab1 = view.findViewById(R.id.fab1_l1);
        FloatingActionButton fab2 = view.findViewById(R.id.fab2_l1);
        floatingActionsMenu.setOnFloatingActionsMenuUpdateListener(new FloatingActionsMenu.OnFloatingActionsMenuUpdateListener() {
            @Override
            public void onMenuExpanded() {
                if (endInput) {
                    fab1.setVisibility(View.VISIBLE);
                    fab2.setVisibility(View.VISIBLE);
                } else {
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
        View view = getLayoutInflater().inflate(R.layout.l1_bottom_sheet, null);
        BottomSheetDialog dialog = new BottomSheetDialog(
                Objects.requireNonNull(getContext()), R.style.BottomSheetDialogTheme
        );
        dialog.setContentView(view);
        dialog.show();
        input_speed = view.findViewById(R.id.input_speed);
        input_acc = view.findViewById(R.id.input_acc);
        FloatingActionButton saveInput = view.findViewById(R.id.save);
        saveInput.setOnClickListener(v -> {
            try {
                saveData();
                startToast = false;
                startVisual = false;
                flagInput = true;
                setTextForBS(view1);
                setVisibilityFab(view1);
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("если ошибка", "проверь трюкач");
            }
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
        lessonData.acc = Double.parseDouble(input_acc.getText().toString());
        rightInput(lessonData.speed, lessonData.acc);
        db.dataDao().insert(lessonData);
    }

    @SuppressLint("SetTextI18n")
    private void rightInput(double speed, double acc) {
        if (speed > 50 && acc > 50 && speed < 100 && acc < 100) {
            lessonData.speed = speed / 5;
            lessonData.acc = acc / 5;
            if (output_scale != null) {
                output_scale.setText("Маштаб времени составляет 1 к 5");
            }
        }
        if (speed > 100 && acc > 100) {
            lessonData.speed = speed / 10;
            lessonData.acc = acc / 10;
            if (output_scale != null) {
                output_scale.setText("Маштаб времени составляет 1 к 10");
            }
        }
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    private void toggleBottomSheetOutput() {
        View view = getLayoutInflater().inflate(R.layout.l1_bottom_sheet_output, null);
        BottomSheetDialog dialog = new BottomSheetDialog(
                Objects.requireNonNull(getContext()), R.style.BottomSheetDialogTheme
        );
        output_speed = view.findViewById(R.id.output_speed);
        output_speed.setText((int) lessonData.speed + " [м/с] - начальная скорость");
        output_speedEnd = view.findViewById(R.id.output_speedEnd);
        output_speedEnd.setText(new DecimalFormat("#0.00").format(PhysicsData.getSpeedEnd()) + " [м/с] - конечная скорость");
        output_distance = view.findViewById(R.id.output_distance);
        output_distance.setText("1000 [м] - расстояние");
        output_acc = view.findViewById(R.id.output_acc);
        output_acc.setText((int) lessonData.acc + " [м/с^2] - ускорение");
        FloatingActionButton restartInput = view.findViewById(R.id.restart);
        restartInput.setOnClickListener(v -> {
            PhysicsModel.onRestartClick = true;
            startVisual = true;
            flagInput = true;
            createDialogAndRestart();
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
                    .replace(R.id.container, new L1FragInform())
                    .addToBackStack(null)
                    .commit();
        });
        dialog.setContentView(view);
        dialog.show();
    }

}
