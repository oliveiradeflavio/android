package com.br.flavio.expressocafe.activitys;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.br.flavio.expressocafe.R;
/*
Flávio Oliveira
https://github.com/oliveiradeflavio
 */
public class SplashScreen extends AppCompatActivity {


    private boolean ativado;
    private static final int TIMER = 1000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        Thread timerThread = new Thread(){
            @Override
            public void run() {
                ativado = true;
                try{
                    int espera = 0;
                    while (ativado && (espera < TIMER)){
                        sleep(100);
                        if (ativado){
                            espera += 100;
                        }
                    }
                }catch (InterruptedException e){

                }finally {
                    iniciaAPP();
                }
            }
        };
        timerThread.start();


    }

    private void iniciaAPP(){
        Intent iniciaApp = new Intent(this, MainActivity.class);
        startActivity(iniciaApp);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }



}
