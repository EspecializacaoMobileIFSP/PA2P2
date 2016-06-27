package br.edu.ifspsaocarlos.sdm2.asynctaskws;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

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

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btAcessarWs = (Button) findViewById(R.id.bt_acessar_ws);
        btAcessarWs.setOnClickListener(this);
        mProgress = (ProgressBar) findViewById(R.id.pb_carregando);
    }

    public void onClick(View v) {
        if (v == btAcessarWs) {
            buscarTexto("http://www.nobile.pro.br/sdm/texto.php");
            buscarData("http://www.nobile.pro.br/sdm/data.php");
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
                    }
                } catch (IOException ioe) {
                    Log.e("SDM", "Erro na recuperação de texto");
                }
                return sb.toString();
            }

            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                TextView tvTexto = ((TextView) findViewById(R.id.tv_texto));
                tvTexto.setText(s);
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
                    }
                    jsonObject = new JSONObject(sb.toString());
                } catch (IOException ioe) {
                    Log.e("SDM", "Erro na recuperação de objeto");
                } catch (JSONException jsone) {
                    Log.e("SDM", "Erro no processamento do objeto JSON");
                }
                return jsonObject;
            }

            protected void onPostExecute(JSONObject s) {
                String data = null, hora = null, ds = null;
                super.onPostExecute(s);
                try {
                    data = s.getInt("mday") + "/" + s.getInt("mon") + "/" + s.getInt("year");
                    hora = s.getInt("hours") + ":" + s.getInt("minutes") + ":" + s.getInt
                            ("seconds");
                    ds
                            = s.getString("weekday");
                } catch (JSONException jsone) {
                    Log.e("SDM", "Erro no processamento do objeto JSON");
                }
                ((TextView) findViewById(R.id.tv_data)).setText(data + "\n" + hora + "\n" + ds);
                mProgress.setVisibility(View.GONE);
            }
        };
        tarefa.execute(url);
    }
}