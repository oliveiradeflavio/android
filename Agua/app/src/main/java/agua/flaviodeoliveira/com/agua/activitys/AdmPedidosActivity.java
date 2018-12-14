package agua.flaviodeoliveira.com.agua.activitys;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import agua.flaviodeoliveira.com.agua.R;
import agua.flaviodeoliveira.com.agua.adapter.AdapterPersonalizadoAdm;
import agua.flaviodeoliveira.com.agua.dao.Pedido;

public class AdmPedidosActivity extends AppCompatActivity {

    private AutoCompleteTextView edt_AdmStatus;
    private ListView listview_AdmPedidos;
    private ProgressBar progressBar;
    private List<Pedido> listPedidosAdm = new ArrayList<>();

    //firebase
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    Pedido pedidoAdmin;
    String nomeChild;

    String nome, endereco, celular, garrafapct, garrafao10, garrafao20, troco, totalgeral,
            dataehorario;

    private static final String[] dicasPalavras = new String[]{
            "Saiu para entrega", "Aguardando motoboy retornar"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adm_pedidos);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Adm Pedidos");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        edt_AdmStatus = (AutoCompleteTextView) findViewById(R.id.edt_AdminStatusPedido);
        listview_AdmPedidos = (ListView) findViewById(R.id.lisview_AdmPedidosClientes);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, dicasPalavras);
        edt_AdmStatus.setAdapter(adapter);

        iniciaFirebase();

        mostrarPedidos();

        listview_AdmPedidos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                pedidoAdmin = (Pedido) adapterView.getItemAtPosition(i);
                nomeChild = pedidoAdmin.getCelular();
                Log.i("nomechild", nomeChild);

                nome = pedidoAdmin.getNome();
                endereco = pedidoAdmin.getEndereco();
                celular = pedidoAdmin.getCelular();
                garrafapct = pedidoAdmin.getGarrafapct();
                garrafao10 = pedidoAdmin.getGarrafao10();
                garrafao20 = pedidoAdmin.getGarrafao20();
                troco = pedidoAdmin.getTroco();
                totalgeral = pedidoAdmin.getTotalgeral();
                dataehorario = pedidoAdmin.getDataehorario();
                edt_AdmStatus.setText(pedidoAdmin.getStatus());

                return false;
            }
        });
    }

    private void iniciaFirebase() {

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_admin_pedido, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.salvarAdm) {
            salvar();
            return true;

        } else if (id == R.id.excluirAdm) {
            excluir();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void excluir() {

        Pedido pd = new Pedido();

        if (pedidoAdmin != null){

            try {

                pd.setUuid(pedidoAdmin.getUuid());

                databaseReference.child("Pedidos").child(celular).child(pd.getUuid()).removeValue();
                databaseReference.child("AdmPedido").child("Pedido").child(pd.getUuid()).removeValue();

                Toast.makeText(AdmPedidosActivity.this, "Pedido foi removido (cancelado).",
                        Toast.LENGTH_SHORT).show();

            }catch (Exception e){
                Toast.makeText(AdmPedidosActivity.this, "Erro: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

        }

    }

    private void salvar() {

        try {

            Pedido pd = new Pedido();

            if (pedidoAdmin != null){

                pd.setUuid(pedidoAdmin.getUuid());
                pd.setNome(nome);
                pd.setEndereco(endereco);
                pd.setCelular(celular);
                pd.setGarrafapct(garrafapct);
                pd.setGarrafao10(garrafao10);
                pd.setGarrafao20(garrafao20);
                pd.setTroco(troco);
                pd.setTotalgeral(totalgeral);
                pd.setDataehorario(dataehorario);
                pd.setStatus(edt_AdmStatus.getText().toString());

                databaseReference.child("Pedidos").child(celular).child(pd.getUuid()).setValue(pd);
                databaseReference.child("AdmPedido").child("Pedido").child(pd.getUuid()).setValue(pd);
                Toast.makeText(AdmPedidosActivity.this, "Salvo com sucesso", Toast.LENGTH_SHORT).show();
                pedidoAdmin = null;
                edt_AdmStatus.setText("");

            }else{
                Toast.makeText(AdmPedidosActivity.this, "Nenhum pedido selecionado. Toque no pedido " +
                        "e segure por alguns segundos", Toast.LENGTH_LONG).show();
            }
        }catch (Exception e){
            Toast.makeText(AdmPedidosActivity.this, "Erro: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }


    }

    private void mostrarPedidos() {

        progressBar.setVisibility(View.VISIBLE);
        databaseReference.child("AdmPedido").child("Pedido").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Pedido pd = new Pedido();

                listPedidosAdm.clear();

                for (DataSnapshot objSnapshot : dataSnapshot.getChildren()) {

                    pd = objSnapshot.getValue(Pedido.class);
                    listPedidosAdm.add(pd);
                }

                if (listPedidosAdm.size() <= 0) {
                    Toast.makeText(AdmPedidosActivity.this, "Não há nenhum pedido",
                            Toast.LENGTH_SHORT).show();

                } else {

                    AdapterPersonalizadoAdm adapter = new AdapterPersonalizadoAdm(listPedidosAdm,
                            AdmPedidosActivity.this);

                    listview_AdmPedidos.setAdapter(adapter);
                    progressBar.setVisibility(View.INVISIBLE);

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}
