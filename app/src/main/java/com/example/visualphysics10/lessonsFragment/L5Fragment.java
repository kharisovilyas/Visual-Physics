package com.example.visualphysics10.lessonsFragment;


import android.annotation.SuppressLint;
import android.media.MediaPlayer;
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
import com.example.visualphysics10.inform.youtube.FragmentInfo;
import com.example.visualphysics10.inform.test.FragmentTest5;
import com.example.visualphysics10.objects.PhysicsModel;
import com.example.visualphysics10.physics.MathPart;
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
    private MediaPlayer collision;


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
        addMediaPlayer();
        addToolbar();
        count = 0;
        play = binding.play;
        FloatingActionButton restart = binding.restart;
        FloatingActionButton startInput = binding.startInput;
        FloatingActionButton startTest = binding.startTest;
        info = binding.info;
        getMessage();
        play.setOnClickListener(v -> {
            if (count % 2 == 0) {
                playClick();
                outputData();
            }
            else pauseClick();
            count++;
        });
        restart.setOnClickListener(v -> {
            addMediaPlayer();
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

    private void addMediaPlayer() {
        //подумать ...
        collision = MediaPlayer.create(getContext(), R.raw.collision);
        PhysicsModel.addSound5(collision);
    }

    private void getMessage() {
        addToolbarNav();
        MaterialTextView outputMes = binding.outputSpeed;
        MaterialTextView outputNull = binding.outputMass1;
        MaterialTextView outputNull2 = binding.outputSpeed2;
        MaterialTextView outputNull3 = binding.outputMass2;
        MaterialTextView outputNull4 = binding.outputImpulse1;
        MaterialTextView outputNull5 = binding.outputImpulse2;
        outputMes.setText(R.string.outputMes);
        outputNull.setText("");
        outputNull2.setText("");
        outputNull3.setText("");
        outputNull4.setText("");
        outputNull5.setText("");
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
        String string = getString(R.string.outputSpeed1) + "\n" + PhysicsData.getSpeed() + " [м/с]";
        String s1 = getString(R.string.outputSpeed2) + "\n" + PhysicsData.getSpeed2() + " [м/с]";
        String s2 = getString(R.string.outputMass1) + "\n" + PhysicsData.getMass1() + " [кг]";
        String s3 = getString(R.string.outputMass2) + "\n" + PhysicsData.getMass2() + " [кг]";
        String s4 = getString(R.string.outputImp1) + "\n" + MathPart.getImp1(PhysicsData.getSpeed(), PhysicsData.getMass1()) + " [кг * м/с]";
        String s5 = getString(R.string.outputImp2) + "\n" + MathPart.getImp2(PhysicsData.getSpeed2(), PhysicsData.getMass2()) + " [кг * м/с]";
        outputSpeed.setText(string);
        outputSpeed2.setText(s1);
        outputMass1.setText(s2);
        outputMass2.setText(s3);
        outputImpulse1.setText(s4);
        outputImpulse2.setText(s5);
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
        getMessage();
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
