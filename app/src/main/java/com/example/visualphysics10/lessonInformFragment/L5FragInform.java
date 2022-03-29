package com.example.visualphysics10.lessonInformFragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.visualphysics10.R;
import com.example.visualphysics10.adapter.ItemFragment;
import com.example.visualphysics10.database.PhysicsData;
import com.example.visualphysics10.lessonsFragment.L1Fragment;
import com.example.visualphysics10.lessonsFragment.L2Fragment;
import com.example.visualphysics10.lessonsFragment.L3Fragment;
import com.example.visualphysics10.lessonsFragment.L4Fragment;
import com.example.visualphysics10.lessonsFragment.L5Fragment;

public class L5FragInform extends Fragment {
    TextView inform;
    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.l5_frag_inform, container, false);
        inform = view.findViewById(R.id.textView_5);
        setText(inform);
        Button closeInform = view.findViewById(R.id.closeInform);
        Button closeLesson = view.findViewById(R.id.closeLesson);
        closeInform.setOnClickListener(v->{
            closeInform();
        });
        closeLesson.setOnClickListener(v->{
            closeLesson();
        });
        return view;
    }
    private void setText(TextView text) {
        Runnable runnable = new Runnable() {
            public void run() {
                text.setText(R.string.lesson5_inform);
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }
    private void closeLesson() {
        requireActivity()
                .getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new ItemFragment())
                .setCustomAnimations(R.anim.nav_default_pop_exit_anim, R.anim.nav_default_pop_enter_anim)
                .commit();
    }

    private void closeInform() {
        requireActivity()
                .getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, switchPosition(PhysicsData.getPosition()))
                .setCustomAnimations(R.anim.nav_default_pop_exit_anim, R.anim.nav_default_pop_enter_anim)
                .commit();
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