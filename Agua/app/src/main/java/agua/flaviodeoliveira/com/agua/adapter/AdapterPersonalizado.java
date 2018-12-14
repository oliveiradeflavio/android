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

public class AdapterPersonalizado extends BaseAdapter {

    private final List<Pedido> pedidos;
    private final Activity act;

    public AdapterPersonalizado(List<Pedido> pedidos, Activity act){

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

        View view = act.getLayoutInflater().inflate(R.layout.listapersonalizada, parent, false);

        Pedido pd = pedidos.get(position);

        TextView garrafapct = view.findViewById(R.id.txt_garrafapct);
        TextView garrafao10L = view.findViewById(R.id.txt_Garrafao10L);
        TextView garrafao20L = view.findViewById(R.id.txt_Garrafao20L);
        TextView dataehorario = view.findViewById(R.id.txt_DataeHorario);
        TextView valorTotal = view.findViewById(R.id.txt_ValorTotal);
        TextView statusPedido = view.findViewById(R.id.txt_StatusPedido);

        garrafapct.setText("Qnt. Garrafa pct: " + pd.getGarrafapct());
        garrafao10L.setText("Qnt. Garrafão 10L: " + pd.getGarrafao10());
        garrafao20L.setText("Qnt. Garrafão 20L: " + pd.getGarrafao20());
        dataehorario.setText("Data/Hora: " + pd.getDataehorario());
        valorTotal.setText("Total R$: " + pd.getTotalgeral());
        statusPedido.setText("Status: " + pd.getStatus());

        return view;
    }
}
