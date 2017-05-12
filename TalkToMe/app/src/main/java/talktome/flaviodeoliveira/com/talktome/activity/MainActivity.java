package talktome.flaviodeoliveira.com.talktome.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.PersistableBundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.prefs.PreferenceChangeEvent;

import talktome.flaviodeoliveira.com.talktome.R;
import talktome.flaviodeoliveira.com.talktome.adapter.TabAdapter;
import talktome.flaviodeoliveira.com.talktome.helper.Base64Custom;
import talktome.flaviodeoliveira.com.talktome.helper.ConfiguracaoFirebase;
import talktome.flaviodeoliveira.com.talktome.helper.Preferencias;
import talktome.flaviodeoliveira.com.talktome.helper.SlidingTabLayout;
import talktome.flaviodeoliveira.com.talktome.model.Contato;
import talktome.flaviodeoliveira.com.talktome.model.Usuario;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private FirebaseAuth usuarioAutenticacao;

    private SlidingTabLayout slidingTabLayout;
    private ViewPager viewPager;
    private String identifcadorContato;
    private DatabaseReference firebase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usuarioAutenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("TalkToMe");
        setSupportActionBar( toolbar  );

        slidingTabLayout = (SlidingTabLayout) findViewById(R.id.stl_tabs);
        viewPager = (ViewPager) findViewById(R.id.vp_pagina);

        //configurar abas
        slidingTabLayout.setDistributeEvenly(true);
        slidingTabLayout.setSelectedIndicatorColors(ContextCompat.getColor(this, R.color.colorAccent));

        //configuração do adapter
        TabAdapter tabAdapter = new TabAdapter( getSupportFragmentManager() );
        viewPager.setAdapter( tabAdapter );

        slidingTabLayout.setViewPager( viewPager );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate( R.menu.menu_main, menu );

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch( item.getItemId()  ){
            case R.id.item_sair:
                deslogarUsuario();
                return true;

            case R.id.item_adicionar:
                abrirCadastroContato();
                return true;

            case R.id.item_pesquisa:
                return true;

            case R.id.item_sobre:
                abrirSobre();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void abrirCadastroContato(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this, android.R.style.Theme_Material_Light_Dialog_NoActionBar);
        //configurações do Dialog
        alertDialog.setTitle("Novo contato");
        alertDialog.setMessage("E-mail do usuário");
        alertDialog.setCancelable(false);

        //criando caixa de texto dentro do dialog
        final EditText editText = new EditText(MainActivity.this);
        editText.setTextColor(Color.BLACK);
        alertDialog.setView(editText);

        //configurações dos botões
        alertDialog.setPositiveButton("Cadastrar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String emailContato = editText.getText().toString();
                //validando o email digitado
                if(emailContato.isEmpty()){
                    Toast.makeText(MainActivity.this, "Preencha o campo com o e-mail", Toast.LENGTH_SHORT).show();

                }else{
                    //verificar se o usuário já está cadastrado no App
                    identifcadorContato = Base64Custom.codificarBase64(emailContato);

                    //recuperar instância do Firebase
                    firebase = ConfiguracaoFirebase.getFirebase();
                    firebase = firebase.child("usuarios").child( identifcadorContato );

                    firebase.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            if(dataSnapshot.getValue() != null){

                                //recuperar dados do contato a ser adicionado
                                Usuario usuarioContato = dataSnapshot.getValue( Usuario.class  );


                                //recuperar identificador usuario logado (base64)
                                Preferencias preferencias = new Preferencias(MainActivity.this);
                                String identificadorUsuarioLogado = preferencias.getIdentificador();


                                firebase = ConfiguracaoFirebase.getFirebase();
                                firebase = firebase.child("contatos")
                                                   .child(identificadorUsuarioLogado)
                                                   .child(identifcadorContato);

                                Contato contato = new Contato();
                                contato.setIdentificadorUsuario( identifcadorContato );
                                contato.setEmail( usuarioContato.getEmail()  );
                                contato.setNome( usuarioContato.getNome()  );

                                firebase.setValue( contato );
                                Toast.makeText(MainActivity.this,"Usuário adicionado", Toast.LENGTH_SHORT).show();

                            }else {
                                Toast.makeText(MainActivity.this,"Usuário não está cadastrado no aplicativo", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }



            }
        });
        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


            }
        });

        alertDialog.create();
        alertDialog.show();
    }

    private void deslogarUsuario(){

        usuarioAutenticacao.signOut();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();

    }
    private void abrirSobre(){

        Intent intent = new Intent(MainActivity.this, SobreActivity.class);
        startActivity(intent);

    }
}
