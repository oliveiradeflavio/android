package com.br.flavio.expressocafe.util;

/*
Flávio Oliveira
https://github.com/oliveiradeflavio
 */
public class NumeroOrdemPedido {

    String uuid;
    String numeroOrdemPedido;
    String quantidadeProduto;
    String nomeProduto;


    public NumeroOrdemPedido(){

    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getNumeroOrdemPedido() {
        return numeroOrdemPedido;
    }

    public void setNumeroOrdemPedido(String numeroOrdemPedido) {
        this.numeroOrdemPedido = numeroOrdemPedido;
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

    @Override
    public String toString() {
        return numeroOrdemPedido;
    }
}
