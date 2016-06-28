package br.edu.ifspsaocarlos.sdm.asynctaskwsimagem;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends Activity implements View.OnClickListener {
    private Button btAcessarWs;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btAcessarWs = (Button) findViewById(R.id.bt_acessar_ws);
        btAcessarWs.setOnClickListener(this);
    }

    public void onClick(View v) {
        if (v == btAcessarWs) {
            //http://www.nobile.pro.br/sdm/logo_ifsp.png
            buscarImagem("https://wallpaperscraft" +
                    ".com/image/queen_group_bohemian_rhapsody_celebrities_98883_3840x2160.jpg");
        }
    }

    private void buscarImagem(String url) {

        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        AsyncTask<String, Void, Bitmap> tarefa = new AsyncTask<String, Void, Bitmap>() {
            protected void onPreExecute() {
                progressBar.setVisibility(View.VISIBLE);
                super.onPreExecute();
            }

            protected Bitmap doInBackground(String... params) {
                Bitmap image = null;
                try {
                    URL url = new URL(params[0]);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    InputStream stream = connection.getInputStream();
                    image = BitmapFactory.decodeStream(stream, null, null);
                } catch (IOException e) {
                    final String errorMsg = getString(R.string.error_msg);
                    Toast.makeText(MainActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                    Log.e((String) getText(R.string.app_name), errorMsg);
                }
                return image;
            }

            protected void onPostExecute(Bitmap bitmap) {
                super.onPostExecute(bitmap);
                ImageView ivImagem = (ImageView) findViewById(R.id.iv_imagem);
                ivImagem.setImageBitmap(bitmap);
                progressBar.setVisibility(View.GONE);
            }
        };
        tarefa.execute(url);
    }
}