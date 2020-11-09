package com.br.flavio.expressocafe.util;
/*
Flávio Oliveira
https://github.com/oliveiradeflavio
 */
public class BloqueioApp {

    String uuid;
    String bloqueio;

    public BloqueioApp(){

    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getBloqueio() {
        return bloqueio;
    }

    public void setBloqueio(String bloqueio) {
        this.bloqueio = bloqueio;
    }

    @Override
    public String toString() {
        return bloqueio;
    }
}
