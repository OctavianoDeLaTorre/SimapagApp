package com.esteban.simapag;

import android.app.Application;

public class MyApp extends Application {

    private static MyApp instnce;
    private String idTrabajador;
    private String tipoUsuario;

    public static MyApp getInstnce() {
        return instnce;
    }

    public static void setInstnce(MyApp instnce) {
        MyApp.instnce = instnce;
    }

    public String getIdTrabajador() {
        return idTrabajador;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public void setIdTrabajador(String idTrabajador) {
        this.idTrabajador = idTrabajador;
    }

    public static MyApp getInstance(){
        return instnce;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instnce=this;
    }
}
