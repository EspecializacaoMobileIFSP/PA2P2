package br.edu.ifspsaocarlos.sdm2.asynctaskws;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

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
        AsyncTask<String, Void, String> tarefa = new AsyncTask<String, Void, String>() {

            protected String doInBackground(String... params) {
                StringBuilder sb = new StringBuilder();
                try {
                    HttpURLConnection conexao =
                            (HttpURLConnection) (new URL(params[0])).openConnection();
                    if (conexao.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        InputStream is = conexao.getInputStream();
                        BufferedReader br = new BufferedReader(new InputStreamReader(is));
                        String temp;
                        while ((temp = br.readLine()) != null) {
                            sb.append(temp);
                        }
                    } else {
                        showError();
                    }
                } catch (IOException ioe) {
                    Log.e("SDM", "Erro na recuperação de texto");
                    showError();
                }
                return sb.toString();
            }

            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                mTextoTV.setText(s);
            }
        };
        tarefa.execute(url);
    }

    private void buscarData(String url) {
        AsyncTask<String, Void, JSONObject> tarefa = new AsyncTask<String, Void, JSONObject>() {
            protected void onPreExecute() {
                super.onPreExecute();
                mProgress.setVisibility(View.VISIBLE);
            }

            protected JSONObject doInBackground(String... params) {
                JSONObject jsonObject = null;
                StringBuilder sb = new StringBuilder();
                try {
                    HttpURLConnection conexao =
                            (HttpURLConnection) (new URL(params[0])).openConnection();
                    if (conexao.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        InputStream is = conexao.getInputStream();
                        BufferedReader br = new BufferedReader(new InputStreamReader(is));
                        String temp;
                        while ((temp = br.readLine()) != null) {
                            sb.append(temp);
                        }
                    } else {
                        showError();
                    }
                    jsonObject = new JSONObject(sb.toString());
                } catch (IOException ioe) {
                    Log.e("SDM", "Erro na recuperação de objeto");
                    showError();
                } catch (JSONException jsone) {
                    Log.e("SDM", "Erro no processamento do objeto JSON");
                    showError();
                }
                return jsonObject;
            }

            protected void onPostExecute(JSONObject s) {
                String data = null, hora = null, ds = null;
                super.onPostExecute(s);
                try {
                    if (s != null) {
                        data = s.getInt("mday") + "/" + s.getInt("mon") + "/" + s.getInt("year");
                        hora = s.getInt("hours") + ":" + s.getInt("minutes") + ":" + s.getInt
                                ("seconds");
                        ds = s.getString("weekday");
                        mDataTV.setText(data + "\n" + hora + "\n" + ds);
                    } else {
                        Log.e("SDM", "Erro no processamento do objeto JSON");
                        showError();
                    }
                } catch (JSONException jsone) {
                    Log.e("SDM", "Erro no processamento do objeto JSON");
                    showError();
                }
                mProgress.setVisibility(View.GONE);
            }
        };
        tarefa.execute(url);
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