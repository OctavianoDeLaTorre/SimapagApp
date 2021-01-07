package com.esteban.simapag.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.esteban.simapag.MainActivity;
import com.esteban.simapag.R;
import com.esteban.simapag.analizador.AnalizadorJSON;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class LoginActivity extends AppCompatActivity {

    EditText cajaUser;
    EditText cajaPassword;
    Button btnLogin;

    String usuario = "";
    String contraseña = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        cajaUser = findViewById(R.id.cajaUser);
        cajaPassword = findViewById(R.id.cajaPassword);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ProcesarLogin().execute(cajaUser.getText().toString(), cajaPassword.getText().toString());
            }
        });
    }


    class ProcesarLogin extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... datos) {
            AnalizadorJSON analizadorJSON = new AnalizadorJSON();
            String script = "android_procesar_login.php";
            String metodoEnvio = "POST";
            String cadenaJSON = "";

            try {
                cadenaJSON = "{ \"user\" : \"" + URLEncoder.encode(datos[0], "UTF-8") + "\", "
                        + "\"password\" : \"" + URLEncoder.encode(datos[1], "UTF-8") + "\" }";
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            JSONObject objetoJSON = analizadorJSON.peticionHTTP(script, metodoEnvio, cadenaJSON);
            try {
                JSONArray jsonArray = objetoJSON.getJSONArray("usuario");
                usuario = jsonArray.getJSONObject(0).getString("user");
                contraseña = jsonArray.getJSONObject(0).getString("password");
                boolean exito = (Boolean) objetoJSON.get("exito");

                if (exito){
                    publishProgress(usuario, contraseña, jsonArray.optJSONObject(0).getString("tipo_usuario"),String.valueOf(exito));
                } else {
                    publishProgress(usuario, contraseña, "null",String.valueOf(exito));
                }



            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);

            if (values[3].toUpperCase().equals("TRUE")){
                if (values[0].equals(cajaUser.getText().toString()) && values[1].equals(cajaPassword.getText().toString())) {
                    Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                    i.putExtra("id", cajaUser.getText().toString());
                    i.putExtra("tipo_usuario", values[2]);
                    Toast.makeText(getApplicationContext(), "Login exitoso", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this,MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "El usuario o contraseña son incorrectos", Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(getApplicationContext(), "El usuario o contraseña son incorrectos", Toast.LENGTH_SHORT).show();
            }

        }
    }
}