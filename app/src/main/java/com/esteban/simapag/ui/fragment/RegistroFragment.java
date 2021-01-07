package com.esteban.simapag.ui.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.esteban.simapag.MyApp;
import com.esteban.simapag.R;
import com.esteban.simapag.analizador.AnalizadorJSON;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

public class RegistroFragment extends Fragment {

    private static final String ARG_PARAM1 = "id";
    private static final String ARG_PARAM2 = "param2";

    private String id;
    private String mParam2;


    ListView listViewRegistros;
    ArrayList<String> listaDatos = new ArrayList<String>();
    ArrayAdapter<String> adaptador;
    TextView txtIdTrabajador;
    TextView txtNombre;
    Button btnRegistrarEntradaSalida;

    public RegistroFragment() {

    }


    public static RegistroFragment newInstance(String param1, String param2) {
        RegistroFragment fragment = new RegistroFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        id = MyApp.getInstance().getIdTrabajador();
        mParam2 = MyApp.getInstance().getTipoUsuario();


        new ConsultarRegistros().execute(id);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        btnRegistrarEntradaSalida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AgregarRegistros().execute(id);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_registro, container, false);

        listViewRegistros = view.findViewById(R.id.listaRegistros);
        txtIdTrabajador = view.findViewById(R.id.txtIdTrabajador);
        txtNombre = view.findViewById(R.id.txtNombre);
        btnRegistrarEntradaSalida = view.findViewById(R.id.btnRegistrarEntradaSalida);

        return view;
    }

    class ConsultarRegistros extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... datos) {
            AnalizadorJSON analizadorJSON = new AnalizadorJSON();

            String script = "android_consultar_registros.php";
            String metodoEnvio = "POST";

            String cadenaJSON = "";
            try {
                cadenaJSON = "{ \"id_trabajador\" : \"" + URLEncoder.encode(datos[0], "UTF-8") + "\" }";
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            JSONObject objetoJSON = analizadorJSON.peticionHTTP(script, metodoEnvio, cadenaJSON);

            try {
                JSONArray jsonArrayEntrada = objetoJSON.getJSONArray("registrosEntrada");
                JSONArray jsonArraySalida = objetoJSON.getJSONArray("registrosSalida");

                String registro;

                for (int i = 0; i < jsonArraySalida.length(); i++) {
                    registro = jsonArrayEntrada.getJSONObject(i).getString("fecha") +
                            "\n" + jsonArrayEntrada.getJSONObject(i).getString("hora") +
                            " - " + jsonArraySalida.getJSONObject(i).getString("hora");
                    listaDatos.add(registro);
                }

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

            listViewRegistros.setAdapter(adaptador);
        }
    }

    class AgregarRegistros extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... datos) {
            AnalizadorJSON analizadorJSON = new AnalizadorJSON();

            String script = "android_alta_registros.php";
            String metodoEnvio = "POST";

            String cadenaJSON = "";
            try {
                cadenaJSON = "{ \"id_trabajador\" : \"" + URLEncoder.encode(datos[0], "UTF-8") + "\" }";
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            JSONObject objetoJSON = analizadorJSON.peticionHTTP(script, metodoEnvio, cadenaJSON);

            try {
                JSONArray jsonArrayRegistrar = objetoJSON.getJSONArray("registrar");

                String registro = "";
                if (jsonArrayRegistrar.getJSONObject(0).getBoolean("entrada")==true &&
                        jsonArrayRegistrar.getJSONObject(0).getBoolean("salida")==false) {
                    registro = "Registro de entrada";
                } else if (jsonArrayRegistrar.getJSONObject(0).getBoolean("entrada")==false &&
                        jsonArrayRegistrar.getJSONObject(0).getBoolean("salida")==true){
                    registro = "Registro de salida";
                }

                publishProgress(registro);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            Toast.makeText(getContext(), values[0], Toast.LENGTH_SHORT).show();
        }
    }

}