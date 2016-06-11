package br.edu.ifspsaocarlos.programacaoConcorrente.view;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

import br.edu.ifspsaocarlos.programacaoConcorrente.R;

public class MainFragment extends Fragment {

    private View mRootView;

    public static MainFragment newInstance() {
        MainFragment myFragment = new MainFragment();
        return myFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_main, container, false);

        configureElements();
        return mRootView;
    }

    private void configureElements() {
        final Button loginButton = (Button) mRootView.findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new Thread() {
                    public void run() {
                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                loginButton.setEnabled(false);
                                TextView idTextView = (TextView)
                                        mRootView.findViewById(R.id.id_text_view);
                                int id = new Random().nextInt(99999 - 1000 + 1) + 1000;
                                idTextView.setText(String.valueOf(id));
                                idTextView.setVisibility(View.VISIBLE);
                            }
                        });
                    }
                }.start();
            }
        });
    }
}
