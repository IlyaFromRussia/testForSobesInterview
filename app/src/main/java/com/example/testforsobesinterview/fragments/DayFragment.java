package com.example.testforsobesinterview.fragments;
/*
 * author Lobov-IR
 */

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.example.testforsobesinterview.R;


public class DayFragment  extends Fragment {
    public DayFragment(){ }

    private ImageButton search;
    private ImageButton navigate;
    private View rootView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        rootView = inflater.inflate(R.layout.fragment_day, container, false);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        search = rootView.findViewById(R.id.search);
        search.setOnClickListener((View v) ->{
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fragment_main_view, SearchFragment.class, null).commit();
        });

        navigate = rootView.findViewById(R.id.navigate);
        navigate.setOnClickListener((View v) ->{
            // select closer town
        });

        super.onViewCreated(view, savedInstanceState);
    }
}