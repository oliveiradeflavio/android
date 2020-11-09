package com.br.flavio.expressocafe.util;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;
/*
Flávio Oliveira
https://github.com/oliveiradeflavio
 */
public class FirebasePersistence extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        /*Quando o usuário estiver offline o aplicativo
         * irá usar os dados da última vez que ele estava online para ser exibido*/
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}