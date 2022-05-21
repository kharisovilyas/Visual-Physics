package com.example.visualphysics10.inform.test;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.visualphysics10.MainActivity;
import com.example.visualphysics10.R;
import com.example.visualphysics10.databinding.FragmentTestBinding;
import com.example.visualphysics10.net.AppForNet;
import com.example.visualphysics10.net.InternetConnection;
import com.example.visualphysics10.net.TestingList;
import com.example.visualphysics10.net.Testings;
import com.example.visualphysics10.ui.EndEducationDialog;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentTest extends Fragment {
    public static FragmentTest newInstance(String param1, String param2) {
        return new FragmentTest();
    }

    //TODO: tasks for all lessons
    private FragmentTestBinding binding;
    private ArrayList<Testings> taskList;
    private MaterialTextView taskTextView;
    private boolean right;
    private boolean right2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTestBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addToolbar();
        getDataFromNetwork(0);
        binding.saveAnswer.setOnClickListener(v -> {
            binding.answer.setEnabled(false);
            setAnswer();
        });
        binding.toNext.setOnClickListener(v -> {
            if (right)
            {
                binding.answer.setEnabled(false);
                taskTextView.setText("");
                binding.progressBar.setVisibility(View.VISIBLE);
                getDataFromNetwork(1);
                binding.answer.setEnabled(true);
            }else if(right2){
                createdEnd();
            }
            else{
                createdNegative();
            }
            right = false;
        });
    }

    private void createdNullAnswer() {
        Snackbar snackbar = Snackbar
                .make(binding.containerAnswer, "Вы не дали ответа!", Snackbar.LENGTH_LONG)
                .setAction("Повторить", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
        snackbar.setActionTextColor(Color.RED);
        snackbar.show();
    }

    private void createdPositive() {
        Snackbar snackbar = Snackbar
                .make(binding.containerAnswer, "Поздравляем !", Snackbar.LENGTH_LONG)
                .setAction("Ответ верный", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(right2) createdEnd();
                    }
                });
        snackbar.setActionTextColor(Color.GREEN);
        snackbar.show();
    }

    private void createdEnd() {
        DialogFragment dialogFragment = EndEducationDialog.newInstance();
        dialogFragment.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), "test is ok");
    }

    private void createdNegative() {
        Snackbar snackbar = Snackbar
                .make(binding.containerAnswer, "Ответ неверный !", Snackbar.LENGTH_LONG)
                .setAction("Поробовать еще раз", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        binding.answer.setEnabled(true);
                    }
                });
        snackbar.setActionTextColor(Color.RED);
        snackbar.show();
    }


    //the method checks the entered answer and outputs the score to the user
    private void setAnswer() {
        TextInputEditText answer = binding.answer;
        try {
            right = RightAnswer.task1FromL1(Double.parseDouble(Objects.requireNonNull(answer.getText()).toString()));
            right2 = RightAnswer.task2FromL1(Double.parseDouble(Objects.requireNonNull(answer.getText()).toString()));
            outputMark();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("SetTextI18n")
    private void outputMark() {
        if (right) {
            createdPositive();
            binding.mark1.setVisibility(View.VISIBLE);
        }else if (!binding.toNext.callOnClick()){
            createdNegative();
        }
        if (right2) {
            createdPositive();
            binding.mark2.setVisibility(View.VISIBLE);
        }
    }


    //parsing
    private void getDataFromNetwork(int index) {
        if (InternetConnection.checkConnection(Objects.requireNonNull(getContext()))) {
            Call<TestingList> task = AppForNet.api.getTask();
            taskList = new ArrayList<>();
            taskTextView = binding.materialTextView;

            //parsing task text from the site, all entities in the folder "net"
            task.enqueue(new Callback<TestingList>() {
                @Override
                public void onResponse(@NonNull Call<TestingList> call, @NonNull Response<TestingList> response) {
                    if (response.isSuccessful()) {
                        assert response.body() != null;
                        taskList = (ArrayList<Testings>) response.body().getTask();
                        taskTextView.setText((CharSequence) taskList);
                    }
                }

                //in case of failure, parsing from R.string.task
                @Override
                public void onFailure(Call<TestingList> call, Throwable t) {
                    binding.progressBar.setVisibility(View.GONE);
                    if (index == 0) {
                        taskTextView.setText(R.string.l1task1);
                    } else {
                        taskTextView.setText(R.string.l1task2);
                    }
                }
            });
        }
    }

    private void addToolbar() {
        MaterialToolbar toolbar = binding.toolbar;
        ((MainActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.close);
        toolbar.setTitle(R.string.test);
        toolbar.setNavigationOnClickListener(v -> {
            requireActivity().onBackPressed();
        });
    }
}