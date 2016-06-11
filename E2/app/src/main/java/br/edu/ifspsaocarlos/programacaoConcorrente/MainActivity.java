package br.edu.ifspsaocarlos.programacaoConcorrente;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import br.edu.ifspsaocarlos.programacaoConcorrente.view.MainFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showMainFragment();
    }

    private void showMainFragment() {
        changeFragment(MainFragment.newInstance());
    }

    public void changeFragment(final Fragment newFragment) {
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.container_layout, newFragment)
                .addToBackStack(null)
                .commit();
    }
}
