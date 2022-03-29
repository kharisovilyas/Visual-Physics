package com.example.visualphysics10.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.visualphysics10.MainActivity;
import com.example.visualphysics10.R;
import com.example.visualphysics10.lessonsFragment.L1Fragment;
import com.example.visualphysics10.lessonsFragment.L2Fragment;
import com.example.visualphysics10.lessonsFragment.L3Fragment;
import com.example.visualphysics10.lessonsFragment.L4Fragment;
import com.example.visualphysics10.lessonsFragment.L5Fragment;
import com.example.visualphysics10.placeholder.PlaceholderContent;

import java.util.ArrayList;
import java.util.Objects;

public class ItemFragment extends Fragment implements RecyclerViewAdapter.OnLessonListener {
    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;

    PlaceholderContent mLesson;
    ArrayList<PlaceholderContent> mValues = new ArrayList<>();

    public static ItemFragment newInstance(int columnCount) {
        ItemFragment fragment = new ItemFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @SuppressLint("ResourceType")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_item_list, container, false);
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new RecyclerViewAdapter(PlaceholderContent.ITEMS, this));
        }
        MainActivity.isFragment = false;


        return view;
    }

    @Override
    public void onLessonClick(int position) {

        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.nav_default_enter_anim, R.anim.nav_default_exit_anim)
                .replace(R.id.container, Objects.requireNonNull(switchFragment(position)))
                .addToBackStack(null)
                .commit();
    }

    private Fragment switchFragment(int position) {
        switch (position){
            case 0: return new L1Fragment();
            case 1: return new L2Fragment();
            case 2: return new L3Fragment();
            case 3: return new L4Fragment();
            case 4: return new L5Fragment();
            default:
                return new Fragment();
        }
    }

}