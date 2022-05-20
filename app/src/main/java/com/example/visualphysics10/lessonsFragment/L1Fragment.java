package com.example.visualphysics10.lessonsFragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.visualphysics10.MainActivity;
import com.example.visualphysics10.R;
import com.example.visualphysics10.database.LessonData;
import com.example.visualphysics10.database.LessonViewModel;
import com.example.visualphysics10.database.PhysicsData;
import com.example.visualphysics10.databinding.L1FragmentBinding;
import com.example.visualphysics10.inform.input.FullScreenDialog;
import com.example.visualphysics10.inform.output.FullScreenInfo;
import com.example.visualphysics10.inform.test.FragmentTest;
import com.example.visualphysics10.objects.PhysicsModel;
import com.example.visualphysics10.physics.PhysicView;
import com.example.visualphysics10.ui.EndEducationDialog;
import com.example.visualphysics10.ui.MainFlag;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textview.MaterialTextView;

import java.util.List;
import java.util.Objects;

public class L1Fragment extends Fragment {
    private L1FragmentBinding binding;
    private PhysicView gameView;
    public static boolean isMoving = false;
    private FloatingActionButton info;
    private FloatingActionButton play;
    private DrawerLayout drawerLayout;
    private NavigationView navigation;
    private LessonViewModel viewModel;
    private boolean step1 = true;
    private boolean step2 = false;
    private boolean step3 = false;
    private boolean step4 = FullScreenDialog.getStep();
    private int count = 0;
    private LessonData lessonDataList;
    SharedPreferences education;
    private String EDUCATION_PREFERENCES = "educationEnd";
    private boolean educationEnd;
    private int targetCount = 0;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = L1FragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addToolbar();
        count = 0;
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
        education = getContext().getSharedPreferences(EDUCATION_PREFERENCES, Context.MODE_PRIVATE);
        if (education.contains(EDUCATION_PREFERENCES)) {
            educationEnd = education.getBoolean(EDUCATION_PREFERENCES, false);
        }
        if (!educationEnd) {
            startEducation();
        }
    }

    private void startEducation() {
        new TapTargetSequence((Activity) getContext()).targets(
                TapTarget.forView(binding.startInput,
                        "Введите исходные данные здесь", "Не забудьте сохранить данные и закройте окно")
                        .outerCircleColor(R.color.primary)
                        .outerCircleAlpha(0.96f)
                        .targetCircleColor(R.color.white)
                        .titleTextSize(24)
                        .descriptionTextSize(18)
                        .titleTextColor(R.color.white)
                        .descriptionTextColor(R.color.black)
                        .textTypeface(Typeface.SANS_SERIF)
                        .dimColor(R.color.black)
                        .drawShadow(true)
                        .cancelable(false)
                        .tintTarget(true)
                        .transparentTarget(true)
                        .targetRadius(100),
                TapTarget.forView(binding.play,
                        "Нажмите старт", "Чтобы начать визуализацию")
                        .outerCircleColor(R.color.primary)
                        .outerCircleAlpha(0.96f)
                        .targetCircleColor(R.color.white)
                        .titleTextSize(24)
                        .descriptionTextSize(18)
                        .titleTextColor(R.color.white)
                        .descriptionTextColor(R.color.black)
                        .textTypeface(Typeface.SANS_SERIF)
                        .dimColor(R.color.black)
                        .drawShadow(true)
                        .cancelable(false)
                        .tintTarget(true)
                        .transparentTarget(true)
                        .targetRadius(100),
                TapTarget.forView(binding.info,
                        "Нажмите инфо", "Чтобы получить больше информации, прослушать лекцию")
                        .outerCircleColor(R.color.primary)
                        .outerCircleAlpha(0.96f)
                        .targetCircleColor(R.color.white)
                        .titleTextSize(24)
                        .descriptionTextSize(18)
                        .titleTextColor(R.color.white)
                        .descriptionTextColor(R.color.black)
                        .textTypeface(Typeface.SANS_SERIF)
                        .dimColor(R.color.black)
                        .drawShadow(true)
                        .cancelable(false)
                        .tintTarget(true)
                        .transparentTarget(true)
                        .targetRadius(10),
                TapTarget.forView(binding.toolbar,
                        "Нажмите или свайпните", "Чтобы посмотреть введеные и найденные данные")
                        .outerCircleColor(R.color.primary)
                        .outerCircleAlpha(0.96f)
                        .targetCircleColor(R.color.white)
                        .titleTextSize(24)
                        .descriptionTextSize(18)
                        .titleTextColor(R.color.white)
                        .descriptionTextColor(R.color.black)
                        .textTypeface(Typeface.SANS_SERIF)
                        .dimColor(R.color.black)
                        .drawShadow(true)
                        .cancelable(false)
                        .tintTarget(true)
                        .transparentTarget(true)
                        .targetRadius(100),
                TapTarget.forView(binding.startTest,
                        "Нажмите и пройдите тест", "Чтобы закрепить усвоенный материал")
                        .outerCircleColor(R.color.primary)
                        .outerCircleAlpha(0.96f)
                        .targetCircleColor(R.color.white)
                        .titleTextSize(24)
                        .descriptionTextSize(18)
                        .titleTextColor(R.color.white)
                        .descriptionTextColor(R.color.black)
                        .textTypeface(Typeface.SANS_SERIF)
                        .dimColor(R.color.black)
                        .drawShadow(true)
                        .cancelable(false)
                        .tintTarget(true)
                        .transparentTarget(true)
                        .targetRadius(100)).listener(new TapTargetSequence.Listener() {
            @Override
            public void onSequenceFinish() {

            }

            @Override
            public void onSequenceStep(TapTarget lastTarget, boolean targetClicked) {
                targetCount++;
                if (targetCount == 5) {
                    createEndEducationDialog();
                    educationEnd();
                }
            }

            @Override
            public void onSequenceCanceled(TapTarget lastTarget) {

            }
        }).start();
    }

    private void createEndEducationDialog() {
        DialogFragment dialogFragment = EndEducationDialog.newInstance();
        dialogFragment.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), "congratulations!");
    }

    @SuppressLint("CommitPrefEdits")
    private void educationEnd() {
        SharedPreferences.Editor editor = education.edit();
        editor.putBoolean(EDUCATION_PREFERENCES, true);
        editor.apply();
    }


    @SuppressLint("SetTextI18n")
    public void outputData() {
        drawerLayout = binding.drawerLayout;
        navigation = binding.navigationView;
        addToolbarNav();
        MaterialTextView outputSpeed = binding.outputSpeed;
        MaterialTextView outputAcc = binding.outputAcc;
        String string = "Вы ввели значение скорости тела - " + PhysicsData.getSpeed() + "[м/с]";
        String string2 = "Вы ввели значение ускорения тела - " + PhysicsData.getAcc() + "[м/с^2]";
        outputSpeed.setText(string);
        outputAcc.setText(string2);
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
        isMoving = true;
        info.setVisibility(View.VISIBLE);
        viewModel = ViewModelProviders.of(requireActivity()).get(LessonViewModel.class);
        viewModel.getLessonLiveData().observe(this, new Observer<List<LessonData>>() {
            @Override
            public void onChanged(List<LessonData> lessonData) {
                PhysicsData.setSpeed(lessonData.get(0).speed);
                PhysicsData.setAcc(lessonData.get(0).acc);
                Log.d(" ", " " + lessonData.get(lessonData.size() - 1).sound);
            }
        });
        gameView.updateMoving(PhysicsData.getSpeed(), 0, 0);
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
        DialogFragment dialogFragment = FullScreenInfo.newInstance();
        dialogFragment.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), "start video");
    }

    private void createdFullScreenDialog() {
        DialogFragment dialogFragment = FullScreenDialog.newInstance();
        dialogFragment.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), "input");
    }

    @SuppressLint("ResourceType")
    private void createDialog() {
        play.setImageResource(R.drawable.play_arrow);
        count += count % 2;
        gameView.restartClick(0);
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.icon_toolbar, menu);
        setHasOptionsMenu(true);
        super.onCreateOptionsMenu(menu, inflater);
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
                return false;
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        PhysicsModel.L1 = false;
        binding = null;
    }

    private void createDrawer() {
        DrawerLayout drawerLayout = binding.drawerLayout;
        drawerLayout.openDrawer(GravityCompat.END);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

}
