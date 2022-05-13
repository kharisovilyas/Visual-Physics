package com.example.visualphysics10.inform;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.visualphysics10.R;
import com.example.visualphysics10.lessonsFragment.L1Fragment;
import com.example.visualphysics10.lessonsFragment.L2Fragment;
import com.example.visualphysics10.lessonsFragment.L3Fragment;
import com.example.visualphysics10.lessonsFragment.L4Fragment;
import com.example.visualphysics10.lessonsFragment.L5Fragment;
import com.example.visualphysics10.ui.MainFlag;

public class InterimFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_interim, container, false);
        requireActivity().getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.nav_default_enter_anim, R.anim.nav_default_exit_anim)
                .replace(R.id.container, switchPosition(MainFlag.getPosition()))
                .addToBackStack(null)
                .commit();
        return view;
    }
    private Fragment switchPosition(int position) {
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