package com.br.flavio.expressocafe.activitys;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.text.Html;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.br.flavio.expressocafe.util.BloqueioApp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import org.w3c.dom.DOMConfiguration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.br.flavio.expressocafe.R;
import com.br.flavio.expressocafe.adapterModificados.adapterProdutoModificado;
import com.br.flavio.expressocafe.util.DetectaInternet;
import com.br.flavio.expressocafe.util.Fornecedores;
import com.br.flavio.expressocafe.util.GeradorNumeracaoRelatorio;
import com.br.flavio.expressocafe.util.NumeroOrdemPedido;
import com.br.flavio.expressocafe.util.Permissao;
import com.br.flavio.expressocafe.util.Produtos;
import com.br.flavio.expressocafe.util.QuantidadeProdutos;
import com.br.flavio.expressocafe.util.SendMail;
/*
Flávio Oliveira
https://github.com/oliveiradeflavio
 */
public class MainActivity extends AppCompatActivity {
    private Spinner  spFornecedor;
    private ListView listaProdutos;
    private ListView listaSelecionados;
    private TextView txtData;
    private TextView txtOP;
    private EditText edtQntProduto;
    private String fornecedorSelecionado;

    AlertDialog.Builder dialog;
    private File pdfFile;
    private String nomeFornecedor, dataPedido;
    public Integer numeroRelatorio;

    DetectaInternet detectaInternet = new DetectaInternet(this);

    private String[] permissoesNecessarias = new String[]{

            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.INTERNET,
            android.Manifest.permission.ACCESS_NETWORK_STATE
    };

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;


    private ArrayList<Fornecedores> arrayList = new ArrayList<Fornecedores>();
    private ArrayList<Produtos> arrayListProdutos = new ArrayList<Produtos>();
    private ArrayAdapter<Produtos> adapter;

    private List<QuantidadeProdutos> arrayListQnt = new ArrayList<QuantidadeProdutos>();
    private ArrayAdapter<QuantidadeProdutos> adapterQuantidade;


    private AlertDialog alertaDialog;
    private String valorNumeroOrdemPedidoBd;


    QuantidadeProdutos quantidadeProdutos = new QuantidadeProdutos();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Ordem de Pedido");
        setSupportActionBar(toolbar);

        Permissao.validaPermissao(1, this, permissoesNecessarias);
        dialog = new AlertDialog.Builder(this);

        iniciaFirebase();

        bloqueioApp();

        if (verificaInternet()) {

            numeroRelatorio = GeradorNumeracaoRelatorio.getProximoRelatorio();

            spFornecedor = (Spinner) findViewById(R.id.spFornecedor);
            listaProdutos = (ListView) findViewById(R.id.listviewProdutosMain);
            listaSelecionados = (ListView) findViewById(R.id.listviewProdutosSelecionados);
            txtData = (TextView) findViewById(R.id.txtData);
            txtOP = (TextView) findViewById(R.id.txtOP);

            //configura automaticamente a data do dia
            dataAtual();


            databaseReference.child("Fornecedores").addValueEventListener(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            final List<Fornecedores> fornecedoresSpinner = new ArrayList<>();

                            arrayList.clear();
                            for (DataSnapshot obj : dataSnapshot.getChildren()) {
                                Fornecedores fornecedores = obj.getValue(Fornecedores.class);
                                fornecedoresSpinner.add(fornecedores);

                            }

                            Collections.sort(fornecedoresSpinner, new Comparator<Fornecedores>() {
                                @Override
                                public int compare(Fornecedores o1, Fornecedores o2) {
                                    return o1.getNomeFornecedor().compareTo(o2.getNomeFornecedor());
                                }
                            });

                         /*   fornecedoresSpinner.sort(new Comparator<Fornecedores>() {
                                @Override
                                public int compare(Fornecedores o1, Fornecedores o2) {
                                    return o1.getNomeFornecedor().compareTo(o2.getNomeFornecedor());
                                }
                            });*/

                            ArrayAdapter<Fornecedores> fornAdapter = new ArrayAdapter<>(MainActivity.this, R.layout.spinner_text, fornecedoresSpinner);
                            fornAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);
                            spFornecedor.setAdapter(fornAdapter);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    }

            );

            spFornecedor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                    fornecedorSelecionado = String.valueOf(parent.getItemAtPosition(position));
                    //Toast.makeText(MainActivity.this, "Item selecionado " + itemSelecionado, Toast.LENGTH_SHORT).show();


                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            adapter = new ArrayAdapter<Produtos>(MainActivity.this, android.R.layout.simple_list_item_1, arrayListProdutos);
            listaProdutos.setAdapter(adapter);

            databaseReference.child("Produtos").addValueEventListener(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            arrayListProdutos.clear();
                            for (DataSnapshot obj : dataSnapshot.getChildren()) {
                                Produtos produtos = obj.getValue(Produtos.class);
                                arrayListProdutos.add(produtos);

                            }
                            Collections.sort(arrayListProdutos, new Comparator<Produtos>() {
                                @Override
                                public int compare(Produtos o1, Produtos o2) {
                                    return o1.getNomeProduto().compareTo(o2.getNomeProduto());
                                }
                            });
                            adapter.notifyDataSetChanged();


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    }

            );

            databaseReference.child("Numero do Pedido").child("Numero do Pedido").child("numeroOrdemPedido")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            NumeroOrdemPedido numeroOrdemPedido = new NumeroOrdemPedido();

                            Object numeroRecebibo = dataSnapshot.getValue();
                            txtOP.setText("Número do Pedido: " + numeroRecebibo);
                            valorNumeroOrdemPedidoBd = numeroRecebibo.toString();


                            //Mostra os pedidos selecionados referente a ordem do pedido
                            adapterQuantidade = new adapterProdutoModificado(MainActivity.this, (ArrayList<QuantidadeProdutos>) arrayListQnt);
                            databaseReference.child("Ordem Pedido").child(valorNumeroOrdemPedidoBd).addValueEventListener(
                                    new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                            arrayListQnt.clear();
                                            for (DataSnapshot obj : dataSnapshot.getChildren()) {
                                                QuantidadeProdutos quantidadeProdutos = obj.getValue(QuantidadeProdutos.class);
                                                arrayListQnt.add(quantidadeProdutos);

                                            }
                                            Collections.sort(arrayListQnt, new Comparator<QuantidadeProdutos>() {
                                                @Override
                                                public int compare(QuantidadeProdutos o1, QuantidadeProdutos o2) {
                                                    return o1.getNomeProduto().compareTo(o2.getNomeProduto());
                                                }
                                            });

                                            adapterQuantidade.notifyDataSetChanged();
                                            listaSelecionados.setAdapter(adapterQuantidade);

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    }

                            );


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        }

        //habilita o clique no listView de produtos selecionados
        listaProdutos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                Produtos valorSelecionado = arrayListProdutos.get(position);
                final Produtos p = new Produtos();
                p.setUuid(valorSelecionado.getUuid());

                edtQntProduto = new EditText(MainActivity.this);
                edtQntProduto.setHint("digite aqui");
                edtQntProduto.setInputType(InputType.TYPE_CLASS_NUMBER);
                edtQntProduto.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
                edtQntProduto.setGravity(Gravity.CENTER);

                dialog.setTitle("Quantidade");
                dialog.setIcon(R.drawable.logoicone);
                dialog.setCancelable(false);
                dialog.setView(edtQntProduto);
                dialog.setMessage("Digite a quantidade do produto " + arrayListProdutos.get(position));
                dialog.setPositiveButton("Cadastrar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String qntProduto = edtQntProduto.getText().toString().trim();

                        if (qntProduto.isEmpty()) {
                            edtQntProduto.setError("Campo não pode ser vazio");

                        } else {

                            quantidadeProdutos.setUuid(UUID.randomUUID().toString());
                            quantidadeProdutos.setNomeProduto(String.valueOf(arrayListProdutos.get(position)));
                            quantidadeProdutos.setQuantidadeProduto(qntProduto);
                            quantidadeProdutos.setNomeFornecedor(fornecedorSelecionado);

                            final Integer nOrodemPedido = Integer.valueOf(valorNumeroOrdemPedidoBd );
                            databaseReference.child("Ordem Pedido").child(String.valueOf(nOrodemPedido))
                                    .child(quantidadeProdutos.getUuid()).setValue(quantidadeProdutos);
                        }

                    }
                });
                dialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                    }
                });
                dialog.create();
                dialog.show();
                return false;


            }
        });


        //habilita o clique na listview de produtos
        listaSelecionados.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                QuantidadeProdutos quantidadeProdutos = arrayListQnt.get(position);
                final QuantidadeProdutos q = new QuantidadeProdutos();
                q.setUuid(quantidadeProdutos.getUuid());

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setCancelable(false);
                builder.setTitle("Atenção");
                builder.setMessage("Deseja excluir o produto " + arrayListProdutos.get(position)
                        + " com quantidade " + arrayListQnt.get(position) + "" +
                        " da ordem de pedidos?");

                builder.setIcon(R.drawable.logoicone);
                builder.setPositiveButton("Excluir", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        databaseReference.child("Ordem Pedido").child(valorNumeroOrdemPedidoBd)
                                .child(q.getUuid()).removeValue();
                        adapter.notifyDataSetChanged();
                        Toast.makeText(MainActivity.this, "Removido com sucesso.", Toast.LENGTH_SHORT).show();

                    }
                });
                builder.setNegativeButton(android.R.string.cancel, null);
                alertaDialog = builder.create();
                alertaDialog.show();
                return false;
            }
        });

    }


     private void bloqueioApp() {

        databaseReference.child("BloqueioApp").child("Bloqueio").addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                        BloqueioApp bloqueioApp = new BloqueioApp();
                        bloqueioApp = dataSnapshot.getValue(BloqueioApp.class);

                        String bloquearApp;
                        bloquearApp =  bloqueioApp.getBloqueio();

                        if (bloquearApp.equals("sim")){


                            builder.setTitle("ATENÇÃO");
                            builder.setMessage("Este aplicativo está com restrição de uso. Entre " +
                                    "em contato com o desenvolvedor. " +
                                    "Para mais informações, envie um e-mail para flaviooliveira.developer@gmail.com");

                            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            });
                            builder.setIcon(R.drawable.ic_block);
                            builder.setCancelable(false);
                            alertaDialog = builder.create();
                            alertaDialog.show();

                            ((TextView)alertaDialog.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                }
        );
    }

    private void iniciaFirebase() {

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.enviarpedido) {

            if (arrayListQnt.size() > 0){
                enviaPedido();
                return true;
            }else{
                Toast.makeText(MainActivity.this, "Você não tem nenhum produto escolhido.", Toast.LENGTH_SHORT).show();
            }



        }else if (id == R.id.administracao){
            Intent iAdmin = new Intent(MainActivity.this, AdminActivity.class);
            startActivity(iAdmin);

        }else if (id == R.id.adminProdutos){
            Intent iAdminProdutos = new Intent(MainActivity.this, AdminProdutosActivity.class);
            startActivity(iAdminProdutos);
        }

        return super.onOptionsItemSelected(item);
    }

    private void numeroOP(){

        final Integer nOrodemPedido = Integer.valueOf(valorNumeroOrdemPedidoBd ) + 1;

        databaseReference.child("Numero do Pedido").child("Numero do Pedido").child("numeroOrdemPedido")
                .setValue(nOrodemPedido);

    }

    private boolean verificaInternet(){

        if(detectaInternet.existeConexao()){
            return true;
        }else {

            dialog.setTitle("Atenção");
            dialog.setMessage("Para enviar os pedidos, seu aparelho precisa estar conectado a internet. " +
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


    private void dataAtual(){

        long date;
        date = System.currentTimeMillis();
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
        String dateString = sdf.format(date);
        txtData.setText("Data: " + dateString);
        dataPedido = dateString;

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for (int resultado : grantResults) {
            if (resultado == PackageManager.PERMISSION_DENIED) {
                alertaValidaPermissao();
            }
        }
    }

    private void alertaValidaPermissao() {

        dialog.setTitle("Permissão de Uso");
        dialog.setCancelable(false);
        dialog.setMessage("O aplicativo precisa ter acesso a escrita na memória interna do smartphone. Para utilizar" +
                " o aplicativo você precisa aceitar esta permissão.");
        dialog.setPositiveButton("Aceitar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Permissao.validaPermissao(1, MainActivity.this, permissoesNecessarias);
            }
        });
        dialog.show();
        dialog.create();
    }

    private void enviaPedido(){


        final Integer nOrodemPedido = Integer.valueOf(valorNumeroOrdemPedidoBd );
        databaseReference.child("Ordem Pedido").child(String.valueOf(nOrodemPedido))
                .child(quantidadeProdutos.getUuid()).setValue(quantidadeProdutos);

        try {
            createPdf();


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }

    }



    private void createPdf() throws FileNotFoundException, DocumentException {

        final File docsFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/EXPRESSOCAFE");
        if (!docsFolder.exists()) {
            docsFolder.mkdir();


        }else if (verificaInternet()){

            //Mostra os pedidos selecionados referente a ordem do pedido
            adapterQuantidade = new adapterProdutoModificado(MainActivity.this, (ArrayList<QuantidadeProdutos>) arrayListQnt);
            databaseReference.child("Ordem Pedido").child(valorNumeroOrdemPedidoBd).addValueEventListener(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            arrayListQnt.clear();
                            for (DataSnapshot obj : dataSnapshot.getChildren()) {
                                QuantidadeProdutos quantidadeProdutos = obj.getValue(QuantidadeProdutos.class);
                                arrayListQnt.add(quantidadeProdutos);

                            }
                            arrayListQnt.sort(new Comparator<QuantidadeProdutos>() {
                                @Override
                                public int compare(QuantidadeProdutos o1, QuantidadeProdutos o2) {
                                    return o1.getNomeProduto().compareTo(o2.getNomeProduto());
                                }
                            });

                            adapterQuantidade.notifyDataSetChanged();
                            listaSelecionados.setAdapter(adapterQuantidade);


                            Document document = new Document(PageSize.A4, 50, 50, 50, 50);


                            pdfFile = new File(docsFolder.getAbsolutePath(), valorNumeroOrdemPedidoBd + "_expressocafe_pedido.pdf");

                            try {
                                OutputStream output = new FileOutputStream(pdfFile);

                                PdfWriter.getInstance(document, output);

                                document.open();
                                document.addTitle("EXPRESSO CAFÉ - PEDIDOS FEITOS PELO APLICATIVO");


                                document.add(new Paragraph(" "));
                                document.add(new Paragraph("NÚMERO DO PEDIDO: " + valorNumeroOrdemPedidoBd));
                                document.add(new Paragraph("FORNECEDOR: " + fornecedorSelecionado));
                                document.add(new Paragraph("DATA: " + dataPedido));
                                document.add(new Paragraph(" "));
                                document.add(new Paragraph(" "));

                                PdfPTable table = new PdfPTable(2);

                                PdfPCell cel1 = new PdfPCell(new Paragraph("PRODUTO"));
                                PdfPCell cel2 = new PdfPCell(new Paragraph("QUANTIDADE"));

                                table.addCell(cel1);
                                table.addCell(cel2);



                                int i;

                                for (i = 0; i < arrayListQnt.size(); i++) {

                                    cel1 = new PdfPCell(new Paragraph(arrayListQnt.get(i).getNomeProduto()));
                                    cel2 = new PdfPCell(new Paragraph( arrayListQnt.get(i).getQuantidadeProduto()));

                                    table.addCell(cel1);
                                    table.addCell(cel2);

                                }

                                document.add(table);
                                document.close();
                                //Toast.makeText(MainActivity.this, "PDF Salvo com sucesso", Toast.LENGTH_SHORT).show();


                                // Log.i("pedido", String.valueOf(pdfFile));
                                if (verificaInternet()){
                                    String caminhoPedido = String.valueOf(pdfFile);
                                    SendMail sm = new SendMail(MainActivity.this, nomeFornecedor, dataPedido, "Expresso Café - segue em anexo pedido realizado", caminhoPedido);
                                    sm.execute();
                                    numeroOP();
                                }


                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (DocumentException e) {
                                e.printStackTrace();
                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


        }


    }

}