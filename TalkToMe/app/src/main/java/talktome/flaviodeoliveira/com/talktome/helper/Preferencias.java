package talktome.flaviodeoliveira.com.talktome.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.renderscript.Sampler;

import java.util.HashMap;

/**
 * Created by flaviooliveira on 09/04/17.
 */
public class Preferencias {
    private Context contexto;
    private SharedPreferences preferences;
    private final String NOME_ARQUIVO = "talktomePreferencias";
    private final int MODE = 0;
    private SharedPreferences.Editor editor;

    private final String CHAVE_IDENTIFICADOR = "identificadorUsuarioLogado";
    private final String CHAVE_NOME = "nomeUsuarioLogado";

    public Preferencias(Context contextoParametro ){

        contexto = contextoParametro;
        preferences = contexto.getSharedPreferences(NOME_ARQUIVO, MODE);
        editor = preferences.edit();

    }

    public void salvarDados(String identificadoUsuarioLogado, String nomeUsuario){

        editor.putString(CHAVE_IDENTIFICADOR, identificadoUsuarioLogado);
        editor.putString(CHAVE_NOME, nomeUsuario);

        editor.commit();

    }

    public String getIdentificador(){

        return preferences.getString(CHAVE_IDENTIFICADOR, null);
    }

    public String getNome(){

        return preferences.getString(CHAVE_NOME, null);
    }




}
