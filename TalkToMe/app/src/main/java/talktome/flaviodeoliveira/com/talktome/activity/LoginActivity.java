package talktome.flaviodeoliveira.com.talktome.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import talktome.flaviodeoliveira.com.talktome.R;
import talktome.flaviodeoliveira.com.talktome.helper.Base64Custom;
import talktome.flaviodeoliveira.com.talktome.helper.ConfiguracaoFirebase;
import talktome.flaviodeoliveira.com.talktome.helper.Preferencias;
import talktome.flaviodeoliveira.com.talktome.model.Usuario;

public class LoginActivity extends AppCompatActivity {

    private EditText email;
    private EditText senha;
    private Button botaoLogar;
    private Usuario usuario;
    private FirebaseAuth autenticacao;
    private DatabaseReference firebase;
    private String identificadorUsuarioLogado;

    private ValueEventListener valueEventListenerUsuario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(talktome.flaviodeoliveira.com.talktome.R.layout.activity_login);

        verificarUsuarioLogado();

        email = (EditText) findViewById(R.id.edit_login_email);
        senha = (EditText) findViewById(R.id.edit_login_senha);
        botaoLogar = (Button) findViewById(R.id.bt_logar);

        botaoLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    usuario = new Usuario();
                    usuario.setEmail( email.getText().toString() );
                    usuario.setSenha( senha.getText().toString() );
                    validarLogin();

                }catch (Exception e){
                    Toast.makeText(LoginActivity.this, "Verifique os campos e tente novamente", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }


            }
        });

    }

    private void verificarUsuarioLogado() {

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        if (autenticacao.getCurrentUser() != null) {

            abrirTelaPrincipal();

        }
    }

    private void validarLogin(){
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.signInWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha()

        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if( task.isSuccessful() ){

                    identificadorUsuarioLogado = Base64Custom.codificarBase64(usuario.getEmail());

                    firebase = ConfiguracaoFirebase.getFirebase()
                            .child("usuarios")
                            .child(identificadorUsuarioLogado);

                    valueEventListenerUsuario = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            Usuario usuarioRecuperado = dataSnapshot.getValue( Usuario.class);

                            Preferencias preferencias = new Preferencias(LoginActivity.this);
                            preferencias.salvarDados( identificadorUsuarioLogado, usuarioRecuperado.getNome() );

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    };

                    firebase.addListenerForSingleValueEvent( valueEventListenerUsuario );



                    abrirTelaPrincipal();

                    //Toast.makeText(LoginActivity.this,"Sucesso ao fazer o login", Toast.LENGTH_LONG).show();

                }else {

                    String erroExcecao = "";
                    try{

                        throw task.getException();


                    }catch(FirebaseAuthInvalidCredentialsException e){

                        erroExcecao = "A senha dessa conta está incorreta.";

                    } catch (FirebaseAuthInvalidUserException e) {

                        erroExcecao = "A conta para esse e-mail não existe ou foi desabilitada.";

                    }catch(Exception e ){
                        erroExcecao = "Ao efetuar login. Confira seus dados e tente novamente. ";
                        e.printStackTrace();
                    }

                    Toast.makeText(LoginActivity.this, "Erro: " + erroExcecao, Toast.LENGTH_LONG).show();
                }

            }
        });


    }

    private void abrirTelaPrincipal(){
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void abrirCadastroUsuario(View view){

        Intent intent  = new Intent(LoginActivity.this, CadastroUsuarioActivity.class);
        startActivity(intent);

    }


}
