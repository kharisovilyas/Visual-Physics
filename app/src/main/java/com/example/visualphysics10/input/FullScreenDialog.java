package com.example.visualphysics10.input;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

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
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

public class FullScreenDialog extends DialogFragment {
    private FullscreenDialogBinding binding;
    private EditText input_speed;
    private EditText input_acc;
    AppDataBase db = App.getInstance().getDatabase();
    public static LessonData lessonData = new LessonData();
    public static SharedPreferences sp;

    public static LessonData getLessonData() {
        return lessonData;
    }

    public static SharedPreferences getSp() {
        return sp;
    }

    public static FullScreenDialog newInstance() {
        return new FullScreenDialog();
    }



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FullscreenDialogBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogTheme);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addToolbar();
        input();
    }

    private void input() {
        input_speed = binding.inputSpeed;
        input_acc = binding.inputAcc;
        FloatingActionButton saveInput = binding.save;
        saveInput.setOnClickListener(v -> {
            try {
                saveData();
                addSpParams();
                Log.d("tag2022", "" + lessonData.speed);
            } catch (Exception e) {
                e.printStackTrace();
            }
            dismiss();
        });
    }

    private void addSpParams() {
        PhysicsData.setElasticImpulse(true);

    }

    private void saveData() {
        lessonData.speed = Double.parseDouble(input_speed.getText().toString());
        lessonData.acc = Double.parseDouble(input_acc.getText().toString());
        db.dataDao().insert(lessonData);
        Log.d("tag2022", "" + lessonData.speed);
    }

    private void addToolbar() {
        MaterialToolbar toolbar = binding.toolbar;
        ((MainActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.close);
        toolbar.setTitle(R.string.title_sheet);
        toolbar.setNavigationOnClickListener(v -> {
            dismiss();
        });
    }
}
