package com.example.testforsobesinterview.fragments;
/*
 * author Lobov-IR
 */

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.testforsobesinterview.MainActivity;
import com.example.testforsobesinterview.R;
import com.example.testforsobesinterview.Town;
import com.example.testforsobesinterview.database.TownBaseHelper;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {
    public SearchFragment(){ }

    private View rootView;
    private RecyclerView list;
    private List<Town> townList;
    private EditText editText;
    private TownAdapter adapter;
    private ImageButton newTown;
    private TownBaseHelper townBaseHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable  ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_search,container,false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        editText = rootView.findViewById(R.id.enteredText);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String currentName = editText.getText().toString();
                List<Town> newList = new ArrayList<>();
                for (Town town : townList){
                    if (town.getName().contains(currentName))
                        newList.add(town);
                }
                adapter = new TownAdapter(newList);
                list.setAdapter(adapter);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        list = rootView.findViewById(R.id.list);
        list.setLayoutManager(new LinearLayoutManager(getActivity()));

        newTown = rootView.findViewById(R.id.newTown);
        newTown.setOnClickListener((v) ->{
            // insert новый город
            // (количество встроенных городов не ограничено)

            // для тестирования
            getActivity().getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_main_view,NightFragment.class, null).commit();
        });

        updateUI();
        return rootView;
    }

    private void updateUI(){
        townBaseHelper = new TownBaseHelper(getContext());
        townList = townBaseHelper.getAllTowns();
        adapter = new TownAdapter(townList);
        list.setAdapter(adapter);
    }

    /*
                        ADAPTER
    */
    private class TownAdapter extends RecyclerView.Adapter<TownHolder> {
        private List<Town> townList;

        public TownAdapter(List<Town> townList) {
            this.townList = townList;
        }

        @NonNull
        @Override
        public TownHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            return new TownHolder(inflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull TownHolder holder, int position) {
            Town town = townList.get(position);
            holder.bind(town);
        }

        @Override
        public int getItemCount() {
            return townList.size();
        }
    }

    /*
                        HOLDER
    */
    private class TownHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private CheckedTextView firstLine;
        private TextView secondLine;
        private Town town;

        public TownHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.list_item,parent,false));
            itemView.setOnClickListener(this::onClick);
            firstLine = itemView.findViewById(R.id.firstLine);
            secondLine = itemView.findViewById(R.id.secondLine);
        }

        public void bind(Town town){
            this.town = town;
            firstLine.setText(town.getName());
            secondLine.setText(town.getLatitude() + "  " + town.getLongitude());
            if (town.isLast() == 1)
                firstLine.setChecked(true);
        }

        @Override
        public void onClick(View v) {
            firstLine.setChecked(true);
            ((InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(editText.getWindowToken(),0);   // иначе баг с открытой клавиатурой при возврате в прошлый фрагмент
            town.setLast(1);
            townBaseHelper.reloadMark(town);

            FragmentActivity activity = getActivity();
            ((MainActivity) activity).resolveFragment(activity.getSupportFragmentManager());
        }
    }
}