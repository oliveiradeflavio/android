package agua.flaviodeoliveira.com.agua.activitys;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import agua.flaviodeoliveira.com.agua.R;
import agua.flaviodeoliveira.com.agua.adapter.AdapterPersonalizado;
import agua.flaviodeoliveira.com.agua.dao.Pedido;

public class ConsultaPedidoActivity extends AppCompatActivity {

    private EditText edt_ConsultaNumeroPedido;
    private ListView listView_ConsultaPedido;
    private Button botaoConsultaPedido;
    private ProgressBar progressBarButton;
    private List<Pedido> listPedidos = new ArrayList<>();
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    Pedido pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta_pedido);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Consulta Status do Pedido");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        edt_ConsultaNumeroPedido = (EditText) findViewById(R.id.edt_CelularConsultaPedido);
        listView_ConsultaPedido = (ListView) findViewById(R.id.listview_ConsultaPedido);
        botaoConsultaPedido = (Button) findViewById(R.id.btn_ConsultaPedido);
        progressBarButton = (ProgressBar) findViewById(R.id.progress_bar);

        //mascara
        SimpleMaskFormatter smf = new SimpleMaskFormatter("(NN)NNNNN-NNNN");
        MaskTextWatcher mtw = new MaskTextWatcher(edt_ConsultaNumeroPedido, smf);
        edt_ConsultaNumeroPedido.addTextChangedListener(mtw);

        iniciaFirebase();

        botaoConsultaPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mostrarPedido();
            }
        });
    }

    private void iniciaFirebase() {

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

    }

    private void mostrarPedido(){

        progressBarButton.setVisibility(View.VISIBLE);
        if (edt_ConsultaNumeroPedido.getText().toString().isEmpty()){
            Toast.makeText(ConsultaPedidoActivity.this, "Campo Celular vazio",
                    Toast.LENGTH_SHORT).show();

        }else {

            String nomeNo = edt_ConsultaNumeroPedido.getText().toString();
            Log.i("nomeNo", nomeNo);

            databaseReference.child("Pedidos").child(nomeNo)
                    .addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    listPedidos.clear();
                    for (DataSnapshot objSnapshot : dataSnapshot.getChildren()){

                        pd = objSnapshot.getValue(Pedido.class);
                        listPedidos.add(pd);
                    }

                    if (listPedidos.size() <= 0 ){
                        Toast.makeText(ConsultaPedidoActivity.this, "Não há nenhum pedido",
                                Toast.LENGTH_SHORT).show();

                    }else{

                        AdapterPersonalizado adapter = new AdapterPersonalizado(listPedidos,
                                ConsultaPedidoActivity.this);

                        listView_ConsultaPedido.setAdapter(adapter);
                        progressBarButton.setVisibility(View.GONE);
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

    }

}
