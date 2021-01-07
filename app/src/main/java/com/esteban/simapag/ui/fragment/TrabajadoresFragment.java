package com.esteban.simapag.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.esteban.simapag.R;
import com.esteban.simapag.analizador.AnalizadorJSON;
import com.esteban.simapag.ui.activities.ManejarTrabajador;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TrabajadoresFragment extends Fragment {


    ListView listViewTrabajadores;
    ArrayList<String> listaDatos = new ArrayList<String>();
    ArrayList<ArrayList> listaDatos2 = new ArrayList<ArrayList>();
    ArrayList<ArrayList> listaTrabajador = new ArrayList<ArrayList>();

    String datos;
    int textlength = 0;

    ArrayAdapter<String> adaptador;
    EditText cajaBusqueda;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new ConsultarTrabajadores().execute();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        cajaBusqueda.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textlength = cajaBusqueda.getText().length();
                listaDatos.clear();

                for (int i = 0; i < listaDatos2.size(); i++) {
                    if (textlength <= listaDatos2.get(i).get(2).toString().length()) {
                        if (cajaBusqueda.getText().toString().equalsIgnoreCase(
                                listaDatos2.get(i).get(2).toString().subSequence(0, textlength).toString() )) {
                            datos =  listaDatos2.get(i).get(0).toString() + "\n" +
                                    listaDatos2.get(i).get(1).toString() + " " +
                                    listaDatos2.get(i).get(2).toString() + " " +
                                    listaDatos2.get(i).get(3).toString();
                            listaDatos.add(datos);
                        }
                    }
                }
                adaptador = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, listaDatos);
                listViewTrabajadores.setAdapter(adaptador);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        listViewTrabajadores.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String item = (String) parent.getItemAtPosition(position);

                Intent i = new Intent(getContext(), ManejarTrabajador.class);
                i.putExtra("trabajador", listaTrabajador.get(position));
                startActivityForResult(i, 1);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_trabajadores, container, false);
        listViewTrabajadores = view.findViewById(R.id.listaTrabajadores);
        cajaBusqueda = view.findViewById(R.id.cajaBusqueda);
        return view;
    }

    class ConsultarTrabajadores extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {
            AnalizadorJSON analizadorJSON = new AnalizadorJSON();

            String script = "android_consultar_trabajadores.php";
            String metodoEnvio = "POST";

            JSONObject objetoJSON = analizadorJSON.peticionHTTP(script);

            try {
                JSONArray jsonArrayTrabajadores = objetoJSON.getJSONArray("trabajadores");

                for (int i=0; i<jsonArrayTrabajadores.length(); i++) {
                    ArrayList<String> listaDatosTrabajador = new ArrayList<>();

                    listaDatosTrabajador.add(jsonArrayTrabajadores.getJSONObject(i).getString("id"));
                    listaDatosTrabajador.add(jsonArrayTrabajadores.getJSONObject(i).getString("nombre"));
                    listaDatosTrabajador.add(jsonArrayTrabajadores.getJSONObject(i).getString("primer_ap"));
                    listaDatosTrabajador.add(jsonArrayTrabajadores.getJSONObject(i).getString("segundo_ap"));

                    listaDatos2.add(listaDatosTrabajador);

                    datos = jsonArrayTrabajadores.getJSONObject(i).getString("id") + "\n" +
                            jsonArrayTrabajadores.getJSONObject(i).getString("nombre") + " " +
                            jsonArrayTrabajadores.getJSONObject(i).getString("primer_ap") + " " +
                            jsonArrayTrabajadores.getJSONObject(i).getString("segundo_ap");

                    listaDatos.add(datos);

                    ArrayList<String> trabajador = new ArrayList<String>();

                    trabajador.add(jsonArrayTrabajadores.getJSONObject(i).getString("id"));
                    trabajador.add(jsonArrayTrabajadores.getJSONObject(i).getString("nombre"));
                    trabajador.add(jsonArrayTrabajadores.getJSONObject(i).getString("primer_ap"));
                    trabajador.add(jsonArrayTrabajadores.getJSONObject(i).getString("segundo_ap"));
                    trabajador.add(jsonArrayTrabajadores.getJSONObject(i).getString("sexo"));
                    trabajador.add(jsonArrayTrabajadores.getJSONObject(i).getString("fecha_nac"));
                    trabajador.add(jsonArrayTrabajadores.getJSONObject(i).getString("calle"));
                    trabajador.add(jsonArrayTrabajadores.getJSONObject(i).getString("numero"));
                    trabajador.add(jsonArrayTrabajadores.getJSONObject(i).getString("colonia"));
                    trabajador.add(jsonArrayTrabajadores.getJSONObject(i).getString("codigo_postal"));
                    trabajador.add(jsonArrayTrabajadores.getJSONObject(i).getString("ciudad"));
                    trabajador.add(jsonArrayTrabajadores.getJSONObject(i).getString("puesto"));
                    trabajador.add(jsonArrayTrabajadores.getJSONObject(i).getString("area"));
                    trabajador.add(jsonArrayTrabajadores.getJSONObject(i).getString("subarea"));

                    listaTrabajador.add(trabajador);

                }

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
            listViewTrabajadores.setAdapter(adaptador);
        }
    }

}