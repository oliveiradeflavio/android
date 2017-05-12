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
import talktome.flaviodeoliveira.com.talktome.model.Contato;

/**
 * Created by flaviooliveira on 09/05/17.
 */
public class ContatoAdapter extends ArrayAdapter<Contato> {

    private ArrayList<Contato> contatos;
    private Context context;


    public ContatoAdapter(Context c, ArrayList<Contato> objects) {
        super(c, 0, objects);

        this.contatos = objects;
        this.context = c;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = null;

        //Verifica se a lista está vazia
        if( contatos != null ){
            //inicializar objeto para montagem da view
            LayoutInflater inflater = (LayoutInflater) context.getSystemService( context.LAYOUT_INFLATER_SERVICE);

            //montar a view a partir do xml
            view = inflater.inflate(R.layout.lista_contato, parent, false );

            //recupera o elemento para exibição
            TextView nomeContato = (TextView) view.findViewById(R.id.tv_titulo);
            TextView emailContato = (TextView) view.findViewById(R.id.tv_subtitulo);

            Contato contato = contatos.get(position);
            nomeContato.setText(contato.getNome());
            emailContato.setText(contato.getEmail());
        }

        return  view;
    }
}
