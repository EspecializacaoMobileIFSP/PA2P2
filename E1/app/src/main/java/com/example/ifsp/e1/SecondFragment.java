package com.example.ifsp.e1;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SecondFragment extends Fragment {

    private Integer id;
    private TextView informationText;

    public SecondFragment() {
    }

    public SecondFragment(Integer id) {
        this.id = id;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_second, container, false);
        informationText = (TextView) view.findViewById(R.id.information_text);
        informationText.setText("ID: 51");

        return view;
    }
}
