package com.br.flavio.expressocafe.util;

/*
Flávio Oliveira
https://github.com/oliveiradeflavio
 */
public class QuantidadeProdutos {

    String uuid;
    String quantidadeProduto;
    String nomeProduto;
    String nomeFornecedor;

    public QuantidadeProdutos(){

    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getQuantidadeProduto() {
        return quantidadeProduto;
    }

    public void setQuantidadeProduto(String quantidadeProduto) {
        this.quantidadeProduto = quantidadeProduto;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public String getNomeFornecedor() {
        return nomeFornecedor;
    }

    public void setNomeFornecedor(String nomeFornecedor) {
        this.nomeFornecedor = nomeFornecedor;
    }

    @Override
    public String toString() {
        return quantidadeProduto;
    }
}
