package com.br.flavio.expressocafe.activitys;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.br.flavio.expressocafe.R;
import com.br.flavio.expressocafe.util.DetectaInternet;
import com.br.flavio.expressocafe.util.Produtos;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.UUID;

/*
Flávio Oliveira
https://github.com/oliveiradeflavio
 */

public class AdminProdutosActivity extends AppCompatActivity {

    private AlertDialog.Builder dialog;
    private AlertDialog alertaDialog;
    private EditText edt_CadNovoProduto;
    private ListView listaProdutos;
    private ArrayList<Produtos> arrayList = new ArrayList<Produtos>();
    private ArrayAdapter<Produtos> adapter;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    DetectaInternet detectaInternet = new DetectaInternet(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_produtos);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Cadastro de Produtos");
        setSupportActionBar(toolbar);

        iniciaFirebase();

        dialog = new AlertDialog.Builder(this);
        listaProdutos = (ListView) findViewById(R.id.listviewProdutos);


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                edt_CadNovoProduto = new EditText(AdminProdutosActivity.this);
                edt_CadNovoProduto.setHint("digite aqui");
                edt_CadNovoProduto.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
                edt_CadNovoProduto.setGravity(Gravity.CENTER);


                dialog.setTitle("Cadastro de Produtos");
                dialog.setIcon(R.drawable.logoicone);
                dialog.setCancelable(false);
                dialog.setView(edt_CadNovoProduto);
                dialog.setMessage("Digite o nome do novo produto a ser cadastrado.");
                dialog.setPositiveButton("Cadastrar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String novoProduto = edt_CadNovoProduto.getText().toString();

                        if (novoProduto.isEmpty()) {
                            edt_CadNovoProduto.setError("Campo não pode ser vazio");

                        } else {

                            Produtos produtos = new Produtos();

                            produtos.setUuid(UUID.randomUUID().toString());
                            produtos.setNomeProduto(edt_CadNovoProduto.getText().toString().trim());

                            databaseReference.child("Produtos").child(produtos.getUuid()).setValue(produtos);
                            Toast.makeText(AdminProdutosActivity.this, "Salvo com sucesso", Toast.LENGTH_SHORT).show();

                        }

                    }
                });


               /* Snackbar.make(view, "Produto cadastrado com sucesso.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/

                dialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                    }
                });
                dialog.create();
                dialog.show();


            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (verificaInternet()) {
            adapter = new ArrayAdapter<Produtos>(AdminProdutosActivity.this, android.R.layout.simple_list_item_1, arrayList);
            listaProdutos.setAdapter(adapter);

            databaseReference.child("Produtos").addValueEventListener(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            arrayList.clear();
                            for (DataSnapshot obj : dataSnapshot.getChildren()) {
                                Produtos produtos = obj.getValue(Produtos.class);
                                arrayList.add(produtos);

                            }
                            Collections.sort(arrayList, new Comparator<Produtos>() {
                                @Override
                                public int compare(Produtos o1, Produtos o2) {
                                    return o1.getNomeProduto().compareTo(o2.getNomeProduto());
                                }
                            });
                           /* arrayList.sort(new Comparator<Produtos>() {
                                @Override
                                public int compare(Produtos o1, Produtos o2) {
                                    return o1.getNomeProduto().compareTo(o2.getNomeProduto());
                                }
                            });*/
                            adapter.notifyDataSetChanged();


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    }

            );


            listaProdutos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                    Produtos valorSelecionado = arrayList.get(position);
                    final Produtos p = new Produtos();
                    p.setUuid(valorSelecionado.getUuid());

                    AlertDialog.Builder builder = new AlertDialog.Builder(AdminProdutosActivity.this);
                    builder.setCancelable(false);
                    builder.setTitle("Atenção");
                    builder.setMessage("Deseja realmente excluir o produto " + arrayList.get(position) + "" +
                            " do banco de dados?");

                    builder.setIcon(R.drawable.logoicone);
                    builder.setPositiveButton("Excluir", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            databaseReference.child("Produtos").child(p.getUuid()).removeValue();
                            adapter.notifyDataSetChanged();
                            Toast.makeText(AdminProdutosActivity.this, "Removido com sucesso.", Toast.LENGTH_SHORT).show();


                        }
                    });
                    builder.setNegativeButton(android.R.string.cancel, null);
                    alertaDialog = builder.create();
                    alertaDialog.show();
                    return false;
                }
            });


        }

    }

    private void iniciaFirebase() {

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    private boolean verificaInternet() {

        if (detectaInternet.existeConexao()) {
            return true;
        } else {

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

