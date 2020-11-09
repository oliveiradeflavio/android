package com.br.flavio.expressocafe.util;

import androidx.annotation.NonNull;

/*
Flávio Oliveira
https://github.com/oliveiradeflavio
 */
public class Fornecedores {

    private String uuid;
    private String nomeFornecedor;



    public Fornecedores(){

    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getNomeFornecedor() {
        return nomeFornecedor;
    }

    public void setNomeFornecedor(String nomeFornecedor) {
        this.nomeFornecedor = nomeFornecedor;
    }

    @NonNull
    @Override
    public String toString() {
        return nomeFornecedor;
    }
}
