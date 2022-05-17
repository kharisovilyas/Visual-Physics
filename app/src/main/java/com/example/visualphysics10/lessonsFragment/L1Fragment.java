package com.example.visualphysics10.lessonsFragment;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.example.visualphysics10.databinding.L1FragmentBinding;
import com.example.visualphysics10.inform.input.FullScreenDialog;
import com.example.visualphysics10.inform.output.FullScreenInfo;
import com.example.visualphysics10.inform.test.FragmentTest;
import com.example.visualphysics10.lessonInformFragment.L1FragInform;
import com.example.visualphysics10.objects.PhysicsModel;
import com.example.visualphysics10.physics.PhysicView;
import com.example.visualphysics10.ui.MainFlag;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textview.MaterialTextView;

import java.text.DecimalFormat;
import java.util.Objects;

public class L1Fragment extends Fragment {
    private L1FragmentBinding binding;
    private PhysicView gameView;
    private boolean flagInput = false;
    private boolean startToast = true;
    private boolean startVisual = true;
    public static boolean isMoving = false;
    private EditText input_speed;
    private EditText input_acc;
    private FloatingActionButton info;
    private FloatingActionButton play;
    private DrawerLayout drawerLayout;
    private NavigationView navigation;
    AppDataBase db = App.getInstance().getDatabase();
    LessonData lessonData = FullScreenDialog.getInstance();
    private int count = 0;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = L1FragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addToolbar();
        gameView = binding.physicsView;
        MainActivity.isFragment = true;
        PhysicsModel.L1 = true;
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
            //toggleBottomSheetInput();
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

    @SuppressLint("SetTextI18n")
    private void outputData() {
        drawerLayout = binding.drawerLayout;
        navigation = binding.navigationView;
        addToolbarNav();
        MaterialTextView outputSpeed = binding.outputSpeed;
        MaterialTextView outputAcc = binding.outputAcc;
        MaterialTextView outputSpeedEnd = binding.outputSpeedEnd;
        db.dataDao().getAllLiveData();
        String string = "Вы ввели значение скорости тела - " + lessonData.speed + "[м/с]";
        String string2 = "Вы ввели значение ускорения тела - " + lessonData.acc + "[м/с^2]";
        String string3 = "Значение скорости перед остановкой - " + PhysicsData.getSpeedEnd() + "[м/с]";
        outputSpeed.setText(string);
        outputAcc.setText(string2);
        outputSpeedEnd.setText(string3);
        //work
    }

    private void addToolbarNav() {
        Toolbar toolbar = binding.toolbarNavView;
        ((MainActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(toolbar);
        toolbar.setTitle("Введенные данные");
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
        PhysicsData.setAcc(lessonData.acc);
        PhysicsData.setSpeed(lessonData.speed);
        gameView.updateMoving(lessonData.speed, 0, 0);
        outputData();
    }


    private void startTesting() {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.nav_default_enter_anim, R.anim.nav_default_exit_anim)
                .replace(R.id.container, new FragmentTest())
                .addToBackStack(null)
                .commit();
    }

    private void createdFullScreenInfo() {
        DialogFragment dialogInfoFragment = FullScreenInfo.newInstance();
        dialogInfoFragment.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), "info");
    }

    private void createdFullScreenDialog() {
        DialogFragment dialogFragment = FullScreenDialog.newInstance();
        dialogFragment.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), "input");
    }

    @SuppressLint("ResourceType")
    private void createDialog() {
        play.setImageResource(R.drawable.play_arrow);
        count += count % 2;
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

    private void restartAll() {
        gameView.restartClick(0);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.icon_toolbar, menu);
    }

    @SuppressLint("RestrictedApi")
    private void addToolbar() {
        Toolbar toolbar = binding.toolbar;
        ((MainActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.arrow_back);
        toolbar.setTitle(R.string.titleL1);
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void toggleBottomSheetInput() {
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
                info.setVisibility(View.VISIBLE);
            } catch (Exception e) {
                e.printStackTrace();
            }
            dialog.dismiss();
        });
    }


    private void saveData() {
        lessonData.speed = Double.parseDouble(input_speed.getText().toString());
        lessonData.acc = Double.parseDouble(input_acc.getText().toString());
        rightInput(lessonData.speed, lessonData.acc);
        db.dataDao().insert(lessonData);
    }

    @SuppressLint("SetTextI18n")
    private void rightInput(double speed, double acc) {
        //TODO: заменить на проверку пустой строки в LoginFragment
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    private void toggleBottomSheetOutput() {
        View view = getLayoutInflater().inflate(R.layout.l1_bottom_sheet_output, null);
        BottomSheetDialog dialog = new BottomSheetDialog(
                Objects.requireNonNull(getContext()), R.style.BottomSheetDialogTheme
        );
        TextView output_speed = view.findViewById(R.id.output_speed);
        output_speed.setText((int) lessonData.speed + " [м/с] - начальная скорость");
        TextView output_speedEnd = view.findViewById(R.id.output_speedEnd);
        output_speedEnd.setText(new DecimalFormat("#0.00").format(PhysicsData.getSpeedEnd()) + " [м/с] - конечная скорость");
        TextView output_distance = view.findViewById(R.id.output_distance);
        output_distance.setText("1000 [м] - расстояние");
        TextView output_acc = view.findViewById(R.id.output_acc);
        output_acc.setText((int) lessonData.acc + " [м/с^2] - ускорение");
        FloatingActionButton restartInput = view.findViewById(R.id.restart);
        restartInput.setOnClickListener(v -> {
            PhysicsModel.onRestartClick = true;
            startVisual = true;
            flagInput = true;
            restartAll();
            dialog.dismiss();
        });
        Button toNextFrag = view.findViewById(R.id.toNextFrag);
        toNextFrag.setOnClickListener(v -> {
            gameView.stopThread();
            MainFlag.setThreadStop(true);
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
