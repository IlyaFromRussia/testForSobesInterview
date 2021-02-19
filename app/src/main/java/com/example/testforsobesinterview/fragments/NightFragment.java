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

public class NightFragment  extends Fragment {
    public NightFragment(){
        super(R.layout.fragment_night);
    }

    private ImageButton search;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        search = getActivity().findViewById(R.id.search);
        search.setOnClickListener((View v) ->{
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.fragment_main_view, SearchFragment.class, null).commit();
        });

        super.onViewCreated(view, savedInstanceState);
    }
}
