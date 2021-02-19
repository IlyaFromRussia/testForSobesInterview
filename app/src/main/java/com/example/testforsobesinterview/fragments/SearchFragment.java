package com.example.testforsobesinterview.fragments;
/*
 * author Lobov-IR
 */

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.testforsobesinterview.MainActivity;
import com.example.testforsobesinterview.R;
import com.example.testforsobesinterview.Town;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {
    public SearchFragment(){ }

    private View rootView;
    private ListView list;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable  ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_search,container,false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        list = rootView.findViewById(R.id.list);
        List<Town> towns = ((MainActivity) getActivity()).getTowns();
        ArrayAdapter<Town> adapter = new ArrayAdapter<>(getActivity(),R.layout.list_item, towns);
        list.setAdapter(adapter);

        super.onViewCreated(view, savedInstanceState);
    }
}