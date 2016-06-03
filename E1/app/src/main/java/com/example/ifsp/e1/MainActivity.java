package com.example.ifsp.e1;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Random;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment, new MainActivityFragment());
        ft.addToBackStack(null);
        ft.commit();
    }
}
