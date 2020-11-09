package com.br.flavio.expressocafe.util;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

/*
Flávio Oliveira
https://github.com/oliveiradeflavio
 */
public class Permissao {

    public static boolean validaPermissao(int requestCode, Activity activity, String[] permissoes){

        if(Build.VERSION.SDK_INT >= 23){

            List<String> listaPermissoes = new ArrayList<String>();

            for(String permissao : permissoes){

                Boolean validaPermissao = ContextCompat.checkSelfPermission(activity, permissao) ==
                        PackageManager.PERMISSION_GRANTED;

                if (!validaPermissao) listaPermissoes.add(permissao);

            }
            //se a lista estiver vazia (sem permissao)
            if (listaPermissoes.isEmpty()){
                return  true;
            }

            //converter a lista em array
            String[] novasPermissoes = new String[listaPermissoes.size()];
            listaPermissoes.toArray(novasPermissoes);

            //solicitando as permissoes
            ActivityCompat.requestPermissions(activity, novasPermissoes, requestCode);
        }
        return true;

    }

}
