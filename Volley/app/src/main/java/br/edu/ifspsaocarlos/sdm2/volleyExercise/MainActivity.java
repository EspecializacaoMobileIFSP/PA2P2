package br.edu.ifspsaocarlos.sdm2.volleyExercise;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends Activity implements View.OnClickListener {
    private Button btAcessarWs;
    private ProgressBar mProgress;
    private EditText mTextoEdt, mDataEdt;
    private TextView mTextoTV, mDataTV;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btAcessarWs = (Button) findViewById(R.id.bt_acessar_ws);
        btAcessarWs.setOnClickListener(this);
        mProgress = (ProgressBar) findViewById(R.id.pb_carregando);

        mTextoEdt = (EditText) findViewById(R.id.urltexto);
        mDataEdt = (EditText) findViewById(R.id.urldata);
        mTextoTV = ((TextView) findViewById(R.id.tv_texto));
        mDataTV = (TextView) findViewById(R.id.tv_data);
    }

    public void onClick(View v) {
        if (v == btAcessarWs) {
            final String urlTexto = mTextoEdt.getText().toString();
            final String urlDate = mDataEdt.getText().toString();

            if (!TextUtils.isEmpty(urlTexto) && !TextUtils.isEmpty(urlDate)) {
                buscarTexto(urlTexto);
                buscarData(urlDate);
            } else {
                mTextoTV.setText("");
                mDataTV.setText("");
                showError();
            }
        }
    }

    private void buscarTexto(String url) {
        mProgress.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mProgress.setVisibility(View.GONE);
                mTextoTV.setText(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("MainActivity", error.toString());
                mProgress.setVisibility(View.GONE);
                showError();
            }
        });

        AppController.getInstance().addToRequestQueue(stringRequest, "cancelTag");
    }

    private void buscarData(String url) {
        mProgress.setVisibility(View.VISIBLE);

        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        String data = null, hora = null, ds = null;
                        try {
                            if (response != null) {
                                data = response.getInt("mday") + "/" + response.getInt("mon") +
                                        "/" + response.getInt("year");
                                hora = response.getInt("hours") + ":" + response.getInt("minutes")
                                        + ":" + response.getInt("seconds");
                                ds = response.getString("weekday");
                                mDataTV.setText(data + "\n" + hora + "\n" + ds);
                            } else {
                                Log.e("SDM", "Erro no processamento do objeto JSON");
                                showError();
                            }
                        } catch (JSONException jsone) {
                            Log.e("SDM", "Erro no processamento do objeto JSON");
                            showError();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        showError();
                    }
                });

        mProgress.setVisibility(View.GONE);
        AppController.getInstance().addToRequestQueue(jsonRequest, "cancelTagJson");

    }

    private void showError() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, R.string.error_msg, Toast.LENGTH_SHORT).show();
            }
        });
    }
}