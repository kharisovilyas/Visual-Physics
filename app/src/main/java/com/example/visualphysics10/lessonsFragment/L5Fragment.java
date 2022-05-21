package com.example.visualphysics10.lessonsFragment;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
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
import com.example.visualphysics10.databinding.L5FragmentBinding;
import com.example.visualphysics10.inform.input.FullScreenDialog5;
import com.example.visualphysics10.inform.output.FragmentInfo;
import com.example.visualphysics10.inform.test.FragmentTest5;
import com.example.visualphysics10.objects.PhysicsModel;
import com.example.visualphysics10.physics.PhysicView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textview.MaterialTextView;

import java.util.List;
import java.util.Objects;
//TODO: Look in L1Fragment if logic this fragment unclear
// because the identical fragment
public class L5Fragment extends Fragment {
    private PhysicView gameView;
    public static boolean isMoving = false;
    private FloatingActionButton info;
    private FloatingActionButton play;
    private int count = 0;
    private L5FragmentBinding binding;
    private LessonViewModel viewModel;
    private DrawerLayout drawerLayout;
    private NavigationView navigation;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = L5FragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        PhysicsModel.L5 = true;
        gameView = binding.physicsView;
        waitingForSV();
        addToolbar();
        count = 0;
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
    }

    private void waitingForSV() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //call the engine constructor for first fragment to Velocity
                gameView.addModelGV5(0);
                gameView.addModelGV5(1);
            }
            //minimal latency for users
        }, 100);
    }

    public void outputData() {
        drawerLayout = binding.drawerLayout;
        navigation = binding.navigationView;
        addToolbarNav();
        MaterialTextView outputSpeed = binding.outputSpeed;
        MaterialTextView outputMass1 = binding.outputMass1;
        MaterialTextView outputSpeed2 = binding.outputSpeed2;
        MaterialTextView outputMass2 = binding.outputMass2;
        MaterialTextView outputImpulse1 = binding.outputImpulse1;
        MaterialTextView outputImpulse2 = binding.outputImpulse2;
        String string = getString(R.string.outputSpeed) + PhysicsData.getSpeed() + "[м/с]";
        String string2 = getString(R.string.outputAcc) + PhysicsData.getAcc() + "[м/с^2]";
        String string3 = getString(R.string.outputAngle) + PhysicsData.getAngle() + "[°]";
        String string4 = getString(R.string.outputSpeed);
        outputSpeed.setText(string);
        outputMass1.setText(string2);
        outputSpeed2.setText(string3);
        outputMass2.setText(string4);
    }

    private void addToolbarNav() {
        Toolbar toolbar = binding.toolbarNavView;
        ((MainActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(toolbar);
        toolbar.setTitle("Введенные данные");
    }


    private void pauseClick() {
        play.setImageResource(R.drawable.play_arrow);
        gameView.stopDraw(0);
        gameView.stopDraw(1);

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
                PhysicsData.setMass1(lessonData.get(0).mass1);
                PhysicsData.setSpeed2(lessonData.get(0).speed2);
                PhysicsData.setMass2(lessonData.get(0).mass2);
                PhysicsData.setElasticImpulse(lessonData.get(0).elasticImpulse);
            }
        });
        gameView.updateMoving(PhysicsData.getSpeed(), 0, 0);
        gameView.updateMoving(-PhysicsData.getSpeed2(), 0, 1);
    }


    private void startTesting() {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.nav_default_enter_anim, R.anim.nav_default_exit_anim)
                .replace(R.id.container, new FragmentTest5())
                .addToBackStack(null)
                .commit();
    }

    private void createdFullScreenInfo() {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.nav_default_enter_anim, R.anim.nav_default_exit_anim)
                .replace(R.id.container, new FragmentInfo())
                .addToBackStack(null)
                .commit();
    }

    private void createdFullScreenDialog() {
        DialogFragment dialogFragment = FullScreenDialog5.newInstance();
        dialogFragment.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), "input");
    }

    @SuppressLint("ResourceType")
    private void createDialog() {
        play.setImageResource(R.drawable.play_arrow);
        count += count % 2;
        gameView.restartClick(0);
        gameView.restartClick(1);
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
        toolbar.setTitle(R.string.titleL5);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        PhysicsModel.L5 = false;
        binding = null;
    }
}
