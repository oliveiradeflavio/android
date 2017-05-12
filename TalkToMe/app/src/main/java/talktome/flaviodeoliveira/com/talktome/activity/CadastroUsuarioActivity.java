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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import talktome.flaviodeoliveira.com.talktome.R;
import talktome.flaviodeoliveira.com.talktome.helper.Base64Custom;
import talktome.flaviodeoliveira.com.talktome.helper.ConfiguracaoFirebase;
import talktome.flaviodeoliveira.com.talktome.helper.Preferencias;
import talktome.flaviodeoliveira.com.talktome.model.Usuario;

public class CadastroUsuarioActivity extends AppCompatActivity {

    private EditText nome;
    private EditText email;
    private EditText senha;
    private Button botaoCadastrar;
    private Usuario usuario;

    private FirebaseAuth autenticacao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadasto_usuario);

        nome = (EditText) findViewById(R.id.edit_cadastro_nome);
        email = (EditText) findViewById(R.id.edit_cadastro_email);
        senha = (EditText) findViewById(R.id.edit_cadastro_senha);
        botaoCadastrar = (Button) findViewById(R.id.bt_cadastrar);

        botaoCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    usuario = new Usuario();
                    usuario.setNome(nome.getText().toString());
                    usuario.setEmail(email.getText().toString());
                    usuario.setSenha(senha.getText().toString());
                    cadastrarUsuario();

                }catch (Exception e){
                    Toast.makeText(CadastroUsuarioActivity.this, "Verifique os campos e tente novamente", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();

                }



            }
        });

    }
    private void cadastrarUsuario(){

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.createUserWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha()
        ).addOnCompleteListener(CadastroUsuarioActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){

                    Toast.makeText(CadastroUsuarioActivity.this, "Usuário cadastrado com sucesso.", Toast.LENGTH_LONG).show();

                    String identificadorUsuario = Base64Custom.codificarBase64( usuario.getEmail() );
                    usuario.setId(identificadorUsuario );
                    usuario.salvar();

                    Preferencias preferencias = new Preferencias(CadastroUsuarioActivity.this);
                    preferencias.salvarDados( identificadorUsuario, usuario.getNome() );

                   // autenticacao.signOut();
                  //  finish();
                    abrirLoginUsuario();

                }else{
                    String erroExcecao = "";


                    try {

                        throw task.getException();


                    }catch(FirebaseAuthInvalidCredentialsException e){

                        erroExcecao = "E-mail incorreto. Digite um e-mail válido.";

                    }catch(FirebaseAuthUserCollisionException e){

                        erroExcecao = "Esse e-mail já está em uso no App.";

                    }catch(Exception e){

                        if (senha.length() < 8) {
                            senha.setText("");//Limpa o campo de texto para o usuário digitar uma nova senha mais forte
                            senha.requestFocus();//Seta o focus no campo de texto
                            Toast.makeText(CadastroUsuarioActivity.this, "Por favor! Para sua segurança, crie uma senha " +
                                    "com no minimo 8 caracteres misturados com letras e números!", Toast.LENGTH_LONG).show();
                        }

                            erroExcecao = "Ao efetuar o cadastro.";
                            e.printStackTrace();

                      }

                    Toast.makeText(CadastroUsuarioActivity.this, "Erro: " + erroExcecao, Toast.LENGTH_LONG).show();

                }
            }
        });


    }
    public void abrirLoginUsuario(){
        Intent intent = new Intent(CadastroUsuarioActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
