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

import com.esteban.simapag.MyApp;
import com.esteban.simapag.R;
import com.esteban.simapag.analizador.AnalizadorJSON;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

public class PerfilFragment extends Fragment {

    private static final String ARG_PARAM1 = "id";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String id;
    private String mParam2;

    ListView listViewPerfil;
    ArrayList<String> listaDatos = new ArrayList<String>();
    ArrayAdapter<String> adaptador;

    public PerfilFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        id = MyApp.getInstnce().getIdTrabajador();
        new ConsultarPerfil().execute(id);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);
        listViewPerfil = view.findViewById(R.id.listaPerfil);
        return view;
    }

    class ConsultarPerfil extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... datos) {
            AnalizadorJSON analizadorJSON = new AnalizadorJSON();

            String script = "android_consultar_perfil.php";

            String metodoEnvio = "POST";

            String cadenaJSON = "";
            try {
                cadenaJSON = "{ \"id_trabajador\" : \"" + URLEncoder.encode(datos[0], "UTF-8") + "\" }";
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            Log.i("cadenaPerfil", cadenaJSON);

            JSONObject objetoJSON = analizadorJSON.peticionHTTP(script, metodoEnvio, cadenaJSON);
            try {
                JSONArray jsonArray = objetoJSON.getJSONArray("perfil");

                listaDatos.add("====INFORMACION BASICA====");
                listaDatos.add("ID: "+jsonArray.getJSONObject(0).getString("id"));
                listaDatos.add("NOMBRE: "+jsonArray.getJSONObject(0).getString("nombre"));
                listaDatos.add("PRIMER APELLIDO: "+jsonArray.getJSONObject(0).getString("primer_ap"));
                listaDatos.add("SEGUNDO APELLIDO: "+jsonArray.getJSONObject(0).getString("segundo_ap"));
                listaDatos.add("SEXO: "+jsonArray.getJSONObject(0).getString("sexo"));
                listaDatos.add("FECHA NACIMIENTO: "+jsonArray.getJSONObject(0).getString("fecha_nac"));
                listaDatos.add("====DIRECCION====");
                listaDatos.add("CALLE: "+jsonArray.getJSONObject(0).getString("calle"));
                listaDatos.add("NUMERO: "+jsonArray.getJSONObject(0).getString("numero"));
                listaDatos.add("COLONIA: "+jsonArray.getJSONObject(0).getString("colonia"));
                listaDatos.add("POSTAL: "+jsonArray.getJSONObject(0).getString("codigo_postal"));
                listaDatos.add("CIUDAD: "+jsonArray.getJSONObject(0).getString("ciudad"));
                listaDatos.add("====INFORMACION TRABAJO====");
                listaDatos.add("PUESTO: "+jsonArray.getJSONObject(0).getString("puesto"));
                listaDatos.add("AREA: "+jsonArray.getJSONObject(0).getString("area"));
                listaDatos.add("SUBAREA: "+jsonArray.getJSONObject(0).getString("subarea"));

                adaptador = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, listaDatos);

                publishProgress();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            listViewPerfil.setAdapter(adaptador);
        }
    }

}