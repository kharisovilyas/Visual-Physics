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
import com.example.visualphysics10.databinding.L2FragmentBinding;
import com.example.visualphysics10.inform.input.FullScreenDialog;
import com.example.visualphysics10.inform.output.FragmentInfo;
import com.example.visualphysics10.inform.test.FragmentTest2;
import com.example.visualphysics10.objects.PhysicsModel;
import com.example.visualphysics10.physics.MathPart;
import com.example.visualphysics10.physics.PhysicView;
import com.example.visualphysics10.ui.MainFlag;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textview.MaterialTextView;

import java.util.List;
import java.util.Objects;

//TODO: Look in L1Fragment if logic this fragment unclear
// because the identical fragment
public class L2Fragment extends Fragment {
    private PhysicView gameView;
    private L2FragmentBinding binding;
    public static boolean isMoving = false;
    private FloatingActionButton info;
    private FloatingActionButton play;
    LessonData lessonData = FullScreenDialog.getInstance();
    private int count = 0;
    private LessonViewModel viewModel;
    private DrawerLayout drawerLayout;
    private NavigationView navigation;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = L2FragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        gameView = binding.physicsView;
        count = 0;
        addToolbar();
        PhysicsModel.L2 = true;
        PhysicsModel.L2start = true;
        PhysicsModel.firstDraw = true;
        MainFlag.setThreadStop(false);
        waitingForSV();
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

    private void getMessage() {
        addToolbarNav();
        MaterialTextView outputMes = binding.outputSpeed;
        MaterialTextView outputNull = binding.outputRad;
        MaterialTextView outputNull2 = binding.outputFrequency;
        outputMes.setText(R.string.outputMes);
        outputNull.setText("");
        outputNull2.setText("");
    }

    private void waitingForSV() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //call the engine constructor for first fragment to Velocity
                gameView.addModelGV();
            }
            //minimal latency for users
        }, 100);
    }

    public void outputData() {
        drawerLayout = binding.drawerLayout;
        navigation = binding.navigationView;
        MathPart.setFrequency((2 * Math.PI * PhysicsData.getRadius()) / PhysicsData.getSpeed());
        addToolbarNav();
        MaterialTextView outputSpeed = binding.outputSpeed;
        MaterialTextView outputAcc = binding.outputRad;
        MaterialTextView outputSpeedEnd = binding.outputFrequency;
        String string = getString(R.string.outputSpeed) + "\n" + PhysicsData.getSpeed() + " [м/с]";
        String string2 = getString(R.string.outputAcc) + "\n" + PhysicsData.getRadius() + " [м/с^2]";
        String string3 = getString(R.string.outputFrequency) + "\n" + MathPart.getFrequency() + " [c^-1]";
        outputSpeed.setText(string);
        outputAcc.setText(string2);
        outputSpeedEnd.setText(string3);
    }

    private void addToolbarNav() {
        Toolbar toolbar = binding.toolbarNavView;
        ((MainActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(toolbar);
        toolbar.setTitle("Введенные данные");
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
        toolbar.setTitle(R.string.titleL2);
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

    private void createDrawer() {
        DrawerLayout drawerLayout = binding.drawerLayout;
        drawerLayout.openDrawer(GravityCompat.END);
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
                PhysicsData.setRadius(lessonData.get(0).radius);
            }
        });
        gameView.updateMoving(lessonData.speed, 0, 0);
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
        DialogFragment dialogFragment = FullScreenDialog.newInstance();
        dialogFragment.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), "input");
    }


    private void startTesting() {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.nav_default_enter_anim, R.anim.nav_default_exit_anim)
                .replace(R.id.container, new FragmentTest2())
                .addToBackStack(null)
                .commit();
    }

    private void createDialog() {
        play.setImageResource(R.drawable.play_arrow);
        count += count % 2;
        gameView.restartClick(0);
        getMessage();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        PhysicsModel.L2 = false;
        binding = null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


}