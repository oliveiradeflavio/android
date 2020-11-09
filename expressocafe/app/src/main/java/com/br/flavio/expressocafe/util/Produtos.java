package com.br.flavio.expressocafe.util;
/*
Flávio Oliveira
https://github.com/oliveiradeflavio
 */
public class Produtos {

    String uuid;
    String nomeProduto;

    public Produtos(){

    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    @Override
    public String toString() {
        return nomeProduto;
    }
}
