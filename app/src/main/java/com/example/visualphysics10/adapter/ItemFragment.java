package com.example.visualphysics10.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.visualphysics10.MainActivity;
import com.example.visualphysics10.R;
import com.example.visualphysics10.databinding.FragmentItemListBinding;
import com.example.visualphysics10.itemUi.FragmentLecture;
import com.example.visualphysics10.itemUi.SettingsFragment;
import com.example.visualphysics10.itemUi.TaskListFragment;
import com.example.visualphysics10.lessonsFragment.L1Fragment;
import com.example.visualphysics10.lessonsFragment.L2Fragment;
import com.example.visualphysics10.lessonsFragment.L3Fragment;
import com.example.visualphysics10.lessonsFragment.L4Fragment;
import com.example.visualphysics10.lessonsFragment.L5Fragment;
import com.example.visualphysics10.placeholder.PlaceholderContent;
import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

public class ItemFragment extends Fragment implements RecyclerViewAdapter.OnLessonListener {
    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private FragmentItemListBinding binding;
    private DrawerLayout drawerLayout;
    private NavigationView navigation;

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
        binding = FragmentItemListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MainActivity.isFragment = false;
        Context context = view.getContext();
        RecyclerView recyclerView = binding.list;
        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }
        recyclerView.setAdapter(new RecyclerViewAdapter(PlaceholderContent.ITEMS, this));
        binding.forOurTest.setOnClickListener(v->{
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.nav_default_enter_anim, R.anim.nav_default_exit_anim)
                    .replace(R.id.container, new TaskListFragment())
                    .addToBackStack(null)
                    .commit();
        });
        addToolbar();
        listenerNav();
    }

    private void listenerNav() {
        drawerLayout = binding.drawerLayout;
        navigation = binding.navigationView;
        navigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menu) {
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.enter_left_to_right, R.anim.exit_left_to_right)
                        .replace(R.id.container, selectDrawerItem(menu))
                        .addToBackStack(null)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                        .commit();
                return true;
            }
        });
    }

    @SuppressLint("NonConstantResourceId")
    private Fragment selectDrawerItem(MenuItem menu) {
        Fragment fragment;
        switch(menu.getItemId()) {
            case R.id.nav_first_fragment:
                fragment = new SettingsFragment();
                break;
            case R.id.nav_second_fragment:
                fragment = new TaskListFragment();
                break;
            case R.id.nav_third_fragment:
                fragment = new FragmentLecture();
                break;
            default:
                fragment = null;
        }
        menu.setChecked(true);
        drawerLayout.closeDrawers();
        return fragment;
    }

    private void addToolbar() {
        Toolbar toolbar = binding.toolbar;
        ((MainActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.menu);
        toolbar.setTitle(R.string.app_name);
        toolbar.setNavigationOnClickListener(v -> {
            createDrawer();
        });
        NavigationView navigationView = binding.navigationView;
        navigationView.setItemIconTintList(null);
    }

    private void createDrawer() {
        DrawerLayout drawerLayout = binding.drawerLayout;
        drawerLayout.openDrawer(GravityCompat.START);
    }

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