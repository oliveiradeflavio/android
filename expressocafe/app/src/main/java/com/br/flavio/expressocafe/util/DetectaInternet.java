package com.br.flavio.expressocafe.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
/*
Flávio Oliveira
https://github.com/oliveiradeflavio
 */
public class DetectaInternet {

    private Context context;

    public DetectaInternet (Context context){
        this.context = context;
    }

    public boolean existeConexao(){

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(
                Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()){
            return true;

        }else{

            return false;
        }
    }
}
