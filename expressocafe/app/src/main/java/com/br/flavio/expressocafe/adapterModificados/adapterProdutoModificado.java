package com.br.flavio.expressocafe.adapterModificados;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import com.br.flavio.expressocafe.R;
import com.br.flavio.expressocafe.util.QuantidadeProdutos;
/*
Flávio Oliveira
https://github.com/oliveiradeflavio
 */
public class adapterProdutoModificado  extends ArrayAdapter<QuantidadeProdutos> {

    private final Context context;
    private final ArrayList<QuantidadeProdutos> elementos;


    public adapterProdutoModificado(@NonNull Context context, @NonNull ArrayList<QuantidadeProdutos> elementos) {
        super(context, R.layout.lista_personalizada_produtos, elementos);

        this.context = context;
        this.elementos = elementos;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.lista_personalizada_produtos, parent, false);

        TextView nomeProduto = (TextView) rowView.findViewById(R.id.txtNomeProduto);
        TextView qntProduto = (TextView) rowView.findViewById(R.id.txtQntProduto);

        nomeProduto.setText(elementos.get(position).getNomeProduto());
        qntProduto.setText(elementos.get(position).getQuantidadeProduto());

        return rowView;

    }
}
