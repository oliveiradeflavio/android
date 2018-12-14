package agua.flaviodeoliveira.com.agua.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import agua.flaviodeoliveira.com.agua.R;
import agua.flaviodeoliveira.com.agua.dao.Pedido;

/**
 * Created by flaviooliveira on 06/08/17.
 */

public class AdapterPersonalizadoAdm extends BaseAdapter {

    private final List<Pedido> pedidos;
    private final Activity act;

    public AdapterPersonalizadoAdm(List<Pedido> pedidos, Activity act){

        this.pedidos = pedidos;
        this.act = act;
    }

    @Override
    public int getCount() {
        return pedidos.size();
    }

    @Override
    public Object getItem(int position) {
        return pedidos.get(position);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = act.getLayoutInflater().inflate(R.layout.listapersonalizada_adm, parent, false);

        Pedido pd = pedidos.get( position );

        TextView nome = view.findViewById(R.id.txt_AdminNome);
        TextView endereco = view.findViewById(R.id.txt_AdmEndereco);
        TextView celular = view.findViewById(R.id.txt_AdmCelular);
        TextView garrafa = view.findViewById(R.id.txt_AdminGarrafa);
        TextView garrafao10l = view.findViewById(R.id.txt_AdmGarrafao10L);
        TextView garrafao20l = view.findViewById(R.id.txt_AdmGarrafao20l);
        TextView status = view.findViewById(R.id.txt_AdmStatus);
        TextView valor = view.findViewById(R.id.txt_AdmValor);
        TextView troco = view.findViewById(R.id.txt_AdmTroco);
        TextView dataehorario = view.findViewById(R.id.txt_AdmDataHora);

        nome.setText(pd.getNome());
        endereco.setText(pd.getEndereco());
        celular.setText(pd.getCelular());
        garrafa.setText("Qnt. Garrafa PCT: " + pd.getGarrafapct());
        garrafao10l.setText("Qnt. Garrafão 10L: " + pd.getGarrafao10());
        garrafao20l.setText("Qnt. Garrafão 20L: " + pd.getGarrafao20());
        status.setText("Status: " + pd.getStatus());
        valor.setText("Total: " + pd.getTotalgeral());
        troco.setText("Troco para: " + pd.getTroco());
        dataehorario.setText("Data/Hora: " + pd.getDataehorario());

        return view;
    }
}
