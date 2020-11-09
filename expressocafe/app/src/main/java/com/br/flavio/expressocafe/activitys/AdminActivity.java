package com.br.flavio.expressocafe.activitys;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.UUID;

import com.br.flavio.expressocafe.R;
import com.br.flavio.expressocafe.util.DetectaInternet;
import com.br.flavio.expressocafe.util.Fornecedores;

/*
Flávio Oliveira
https://github.com/oliveiradeflavio
 */

public class AdminActivity extends AppCompatActivity {

    private EditText Adm_edtFornecedor;
    private Button Adm_btnSalvar;
    private ListView listaFornecedores;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    private ArrayList<Fornecedores> arrayList = new ArrayList<Fornecedores>();
    private ArrayAdapter<Fornecedores> adapter;

    private AlertDialog alertaDialog;
    AlertDialog.Builder dialog;

    DetectaInternet detectaInternet = new DetectaInternet(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Administração de Fornecedor");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        iniciaFirebase();
        dialog = new AlertDialog.Builder(this);


        Adm_edtFornecedor = (EditText) findViewById(R.id.admin_edtFornecedor);
        Adm_btnSalvar = (Button) findViewById(R.id.btnSalvar);
        listaFornecedores = (ListView) findViewById(R.id.listviewFornecedor);

        if (verificaInternet()){
            adapter = new ArrayAdapter<Fornecedores>(AdminActivity.this, android.R.layout.simple_list_item_1, arrayList);
            listaFornecedores.setAdapter(adapter);

            databaseReference.child("Fornecedores").addValueEventListener(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            arrayList.clear();
                            for (DataSnapshot obj: dataSnapshot.getChildren()){
                                Fornecedores fornecedores = obj.getValue(Fornecedores.class);
                                arrayList.add(fornecedores);

                            }
                            arrayList.sort(new Comparator<Fornecedores>() {
                                @Override
                                public int compare(Fornecedores o1, Fornecedores o2) {
                                    return o1.getNomeFornecedor().compareTo(o2.getNomeFornecedor());
                                }
                            });
                            adapter.notifyDataSetChanged();



                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    }

            );


            listaFornecedores.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                    Fornecedores valorSelecionado = arrayList.get(position);
                    final Fornecedores f = new Fornecedores();
                    f.setUuid(valorSelecionado.getUuid());

                    AlertDialog.Builder builder = new AlertDialog.Builder(AdminActivity.this);
                    builder.setCancelable(false);
                    builder.setTitle("Atenção");
                    builder.setMessage("Deseja realmente excluir o fornecedor " + arrayList.get(position) + "" +
                            " do banco de dados?");

                    builder.setIcon(R.drawable.logoicone);
                    builder.setPositiveButton("Excluir", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            databaseReference.child("Fornecedores").child(f.getUuid()).removeValue();
                            adapter.notifyDataSetChanged();
                            Toast.makeText(AdminActivity.this, "Removido com sucesso.", Toast.LENGTH_SHORT).show();


                        }
                    });
                    builder.setNegativeButton(android.R.string.cancel, null);
                    alertaDialog = builder.create();
                    alertaDialog.show();
                    return false;
                }
            });


            Adm_btnSalvar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (Adm_edtFornecedor.getText().toString().isEmpty()) {
                        Adm_edtFornecedor.setError("Campo não pode ser vazio");

                    }else {
                        Fornecedores fornecedores = new Fornecedores();

                        fornecedores.setUuid(UUID.randomUUID().toString());
                        fornecedores.setNomeFornecedor(Adm_edtFornecedor.getText().toString().trim());

                        databaseReference.child("Fornecedores").child(fornecedores.getUuid()).setValue(fornecedores);
                        Toast.makeText(AdminActivity.this, "Salvo com sucesso", Toast.LENGTH_SHORT).show();
                        Adm_edtFornecedor.setText(" ");
                    }


                }
            });

        }


    }

    private void iniciaFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    private boolean verificaInternet(){

        if(detectaInternet.existeConexao()){
            return true;
        }else {

            dialog.setTitle("Atenção");
            dialog.setMessage("Para adicionar novos forncedores ou remover, seu aparelho precisa estar conectado a internet. " +
                    "Verificamos que está sem acesso a internet ou com conexão limitada.");

            dialog.setCancelable(false);
            dialog.setPositiveButton("Tentar novamente", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    verificaInternet();

                }
            });
            dialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            dialog.show();
            dialog.create();

            return false;
        }

    }


}
