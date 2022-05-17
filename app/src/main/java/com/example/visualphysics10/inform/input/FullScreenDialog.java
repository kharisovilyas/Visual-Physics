package com.example.visualphysics10.inform.input;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.visualphysics10.MainActivity;
import com.example.visualphysics10.R;
import com.example.visualphysics10.database.App;
import com.example.visualphysics10.database.AppDataBase;
import com.example.visualphysics10.database.LessonData;
import com.example.visualphysics10.database.PhysicsData;
import com.example.visualphysics10.databinding.FullscreenDialogBinding;
import com.example.visualphysics10.ui.MainFlag;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class FullScreenDialog extends DialogFragment {
    private FullscreenDialogBinding binding;
    private TextInputEditText input_speed;
    private TextInputEditText input_acc;
    private TextInputEditText input_radius;
    private TextInputEditText input_force;
    private TextInputEditText input_mass;
    private TextInputEditText input_angle;
    AppDataBase db = App.getInstance().getDatabase();
    public static LessonData lessonData = new LessonData();
    public static SharedPreferences sp;

    public static LessonData getInstance() {
        return lessonData;
    }

    public static SharedPreferences getSp() {
        return sp;
    }

    public static FullScreenDialog newInstance() {
        return new FullScreenDialog();
    }


    //dialogFragment customization
    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        assert dialog != null;
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = ViewGroup.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setLayout(width, height);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogTheme);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FullscreenDialogBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addToolbar();
        editHint();
        input();
    }


    //adaptability setting of EditText
    private void editHint() {
        TextInputLayout inputAccLay = binding.inputAccLayout;
        TextInputLayout inputRadLay = binding.inputRadLay;
        TextInputLayout inputForceLay = binding.inputForceLay;
        TextInputLayout inputMassLay = binding.inputMassLay;
        TextInputLayout inputAngleLay = binding.inputAngleLay;
        switch (MainFlag.getPosition()) {
            case 0:
                break;
            case 1:
                inputAccLay.setVisibility(View.GONE);
                inputRadLay.setVisibility(View.VISIBLE);
                break;
            case 2:
                inputAccLay.setVisibility(View.GONE);
                inputForceLay.setVisibility(View.VISIBLE);
                inputMassLay.setVisibility(View.VISIBLE);
                break;
            case 3:
                inputAngleLay.setVisibility(View.VISIBLE);

        }
    }

    //initialization EditText and try/catch
    private void input() {
        input_speed = binding.inputSpeed;
        input_acc = binding.inputAcc;
        input_radius = binding.inputRad;
        input_force = binding.inputForce;
        input_mass = binding.inputMass;
        input_angle = binding.inputAngle;
        FloatingActionButton saveInput = binding.save;
        saveInput.setOnClickListener(v -> {
            try {
                saveData();
                addSpParams();
            } catch (Exception e) {
                e.printStackTrace();
            }
            dismiss();
        });
    }

    private void addSpParams() {
        PhysicsData.setElasticImpulse(true);
    }


    //save to db data from EditText
    private void saveData() {
        Log.d("", "" + MainFlag.getPosition());
        lessonData.speed = toDouble(input_speed);
        switch (MainFlag.getPosition()) {
            case 0:
                lessonData.acc = toDouble(input_acc);
                break;
            case 1:
                lessonData.radius = toDouble(input_radius);
            case 2:
                lessonData.strength = toDouble(input_force);
                lessonData.mass1 = toDouble(input_mass);
            case 3:
                lessonData.angle = toDouble(input_angle);
        }
        db.dataDao().insert(lessonData);
    }

    private double toDouble(TextInputEditText input) {
        return Double.parseDouble(String.valueOf(input.getText()));
    }

    private void addToolbar() {
        MaterialToolbar toolbar = binding.toolbar;
        ((MainActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.close);
        toolbar.setTitle(R.string.title_input);
        toolbar.setNavigationOnClickListener(v -> {
            dismiss();
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}