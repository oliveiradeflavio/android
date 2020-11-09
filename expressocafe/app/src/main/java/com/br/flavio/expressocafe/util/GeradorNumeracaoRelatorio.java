package com.br.flavio.expressocafe.util;

/*
Flávio Oliveira
https://github.com/oliveiradeflavio
 */
public class GeradorNumeracaoRelatorio {

    private static int ID = 000;

    public static int getProximoRelatorio(){
        return ID++;
    }


}
