package com.example.visualphysics10.itemUi;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.visualphysics10.MainActivity;
import com.example.visualphysics10.R;
import com.example.visualphysics10.adapter.RecyclerViewAdapter;
import com.example.visualphysics10.databinding.FragmentTaskListBinding;
import com.example.visualphysics10.inform.test.FragmentTest;
import com.example.visualphysics10.inform.test.FragmentTest2;
import com.example.visualphysics10.inform.test.FragmentTest3;
import com.example.visualphysics10.inform.test.FragmentTest4;
import com.example.visualphysics10.inform.test.FragmentTest5;
import com.example.visualphysics10.placeholder2.PlaceHolderContent2;

import java.util.Objects;

public class TaskListFragment extends Fragment implements RecyclerViewAdapter.OnLessonListener {
    private FragmentTaskListBinding binding;
    private final int mColumnCount = 1;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentTaskListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addRecycler(view);
        addToolbar();
    }

    @SuppressLint("ResourceAsColor")
    private void addToolbar() {
        Toolbar toolbar = binding.toolbar;
        ((MainActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.arrow_back);
        toolbar.setTitle(R.string.test);
        toolbar.setNavigationOnClickListener(v -> {
            getActivity().onBackPressed();
        });
    }

    private void addRecycler(View view) {
        Context context = view.getContext();
        RecyclerView recyclerView = binding.listTask;
        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }
        recyclerView.setAdapter(new RecyclerViewAdapter(PlaceHolderContent2.ITEMS, this, "FOR TASK FRAGMENT"));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    //perform a fragment transaction on a specific-Lesson click
    @Override
    public void onLessonClick(int position) {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.enter_left_to_right, R.anim.exit_left_to_right)
                .replace(R.id.container, Objects.requireNonNull(selectFragment(position)))
                .addToBackStack(null)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
    }


    private Fragment selectFragment(int position) {
        switch (position) {
            case 0:
                return new FragmentTest();
            case 1:
                return new FragmentTest2();
            case 2:
                return new FragmentTest3();
            case 3:
                return new FragmentTest4();
            case 4:
                return new FragmentTest5();
            default:
                return new Fragment();
        }
    }

}
