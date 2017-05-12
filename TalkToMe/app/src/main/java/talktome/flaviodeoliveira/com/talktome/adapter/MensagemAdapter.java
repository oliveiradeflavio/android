package talktome.flaviodeoliveira.com.talktome.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import talktome.flaviodeoliveira.com.talktome.R;
import talktome.flaviodeoliveira.com.talktome.helper.Preferencias;
import talktome.flaviodeoliveira.com.talktome.model.Mensagem;

/**
 * Created by flaviooliveira on 10/05/17.
 */

public class MensagemAdapter extends ArrayAdapter<Mensagem>{

    private Context context;
    private ArrayList<Mensagem> mensagens;


    public MensagemAdapter(Context c, ArrayList<Mensagem> objects) {
        super(c, 0, objects);
        this.context = c;
        this.mensagens = objects;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = null;

        //verifica se a lista está preenchida
        if( mensagens != null  ){

            //recupera dados do usuario remetente
            Preferencias preferencias = new Preferencias(context);
            String idUsuarioRemetente = preferencias.getIdentificador();

            //inicia o objeto para a montagem do layout
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            //recupera mensagem
            Mensagem mensagem = mensagens.get(position);

            //monta a view a partir do xml
            if( idUsuarioRemetente.equals( mensagem.getIdUsuario()) ){
                view  = inflater.inflate(R.layout.item_mensagem_direita, parent, false);

            }else{
                view  = inflater.inflate(R.layout.item_mensagem_esquerda, parent, false);
            }


            //recupera o elemento para exibição
            TextView textoMensagem = (TextView) view.findViewById(R.id.tv_mensagem);
            textoMensagem.setText( mensagem.getMensagem()  );
        }else{

        }


        return view;

    }
}
