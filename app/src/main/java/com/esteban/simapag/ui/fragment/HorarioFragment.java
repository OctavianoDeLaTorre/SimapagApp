package com.esteban.simapag.ui.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.esteban.simapag.MyApp;
import com.esteban.simapag.R;
import com.esteban.simapag.analizador.AnalizadorJSON;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

public class HorarioFragment extends Fragment {
    private String id;
    private String mParam2;

    ListView listViewHorario;
    ArrayList<String> listaDatos = new ArrayList<>();
    ArrayAdapter<String> adaptador;

    TextView txtIdTrabajador;
    TextView txtNombre;

    public HorarioFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        id = MyApp.getInstnce().getIdTrabajador();
        new ConsultarHorario().execute(id);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_horario, container, false);
        listViewHorario = view.findViewById(R.id.listaHorario);
        txtIdTrabajador = view.findViewById(R.id.txtIdTrabajador);
        txtNombre = view.findViewById(R.id.txtNombre);
        return view;
    }

    class ConsultarHorario extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... datos) {
            AnalizadorJSON analizadorJSON = new AnalizadorJSON();

            String script = "android_consultar_horario.php";
            String metodoEnvio = "POST";

            String cadenaJSON = "";
            try {
                cadenaJSON = "{ \"id_trabajador\" : \"" + URLEncoder.encode(datos[0], "UTF-8") + "\" }";
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            Log.i("cadenaHorario", cadenaJSON);

            JSONObject objetoJSON = analizadorJSON.peticionHTTP(script, metodoEnvio, cadenaJSON);
            try {
                JSONArray jsonArrayEntrada = objetoJSON.getJSONArray("horarioEntrada");
                JSONArray jsonArraySalida = objetoJSON.getJSONArray("horarioSalida");

                String lunes = "Lunes: \n" + jsonArrayEntrada.getJSONObject(0).getString("lunes") +
                        " - " + jsonArraySalida.getJSONObject(0).getString("lunes");
                listaDatos.add(lunes);
                String martes = "Martes: \n" + jsonArrayEntrada.getJSONObject(0).getString("martes") +
                        " - " + jsonArraySalida.getJSONObject(0).getString("martes");
                listaDatos.add(martes);
                String miercoles = "Miercoles: \n" + jsonArrayEntrada.getJSONObject(0).getString("miercoles") +
                        " - " + jsonArraySalida.getJSONObject(0).getString("miercoles");
                listaDatos.add(miercoles);
                String jueves = "Jueves: \n" + jsonArrayEntrada.getJSONObject(0).getString("jueves") +
                        " - " + jsonArraySalida.getJSONObject(0).getString("jueves");
                listaDatos.add(jueves);
                String viernes = "Viernes: \n" + jsonArrayEntrada.getJSONObject(0).getString("viernes") +
                        " - " + jsonArraySalida.getJSONObject(0).getString("viernes");
                listaDatos.add(viernes);


                adaptador = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, listaDatos);

                publishProgress(jsonArrayEntrada.getJSONObject(0).getString("id"),
                        jsonArrayEntrada.getJSONObject(0).getString("nombre") + " " +
                                jsonArrayEntrada.getJSONObject(0).getString("primer_ap") + " " +
                                jsonArrayEntrada.getJSONObject(0).getString("segundo_ap"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            txtIdTrabajador.setText(values[0]);
            txtNombre.setText(values[1]);
            listViewHorario.setAdapter(adaptador);
        }
    }
}