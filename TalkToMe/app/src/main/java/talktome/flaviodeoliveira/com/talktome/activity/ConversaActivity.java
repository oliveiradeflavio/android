package talktome.flaviodeoliveira.com.talktome.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import talktome.flaviodeoliveira.com.talktome.R;
import talktome.flaviodeoliveira.com.talktome.adapter.MensagemAdapter;
import talktome.flaviodeoliveira.com.talktome.helper.Base64Custom;
import talktome.flaviodeoliveira.com.talktome.helper.ConfiguracaoFirebase;
import talktome.flaviodeoliveira.com.talktome.helper.Preferencias;
import talktome.flaviodeoliveira.com.talktome.model.Conversa;
import talktome.flaviodeoliveira.com.talktome.model.Mensagem;

public class ConversaActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText editMensagem;
    private ImageButton btMensagem;
    private DatabaseReference firebase;
    private ListView listView;
    private ArrayList<Mensagem> mensagens;
    private ArrayAdapter<Mensagem> adapter;
    private ValueEventListener valueEventListenerMensagem;

    //dados do destinatário
    private String nomeUsuarioDestinatario;
    private String idUsuarioDestinatario;

    //dados do remetente
    private String idUsuarioRemetente;
    private String nomeUsuarioRemetente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversa);

        toolbar = (Toolbar) findViewById(R.id.tb_conversa);
        editMensagem = (EditText) findViewById(R.id.edit_mensagem);
        btMensagem = (ImageButton) findViewById(R.id.bt_enviar);
        listView = (ListView) findViewById(R.id.lv_conversas);

        //dados do usuário logado
        Preferencias preferencias = new Preferencias(ConversaActivity.this);
        idUsuarioRemetente = preferencias.getIdentificador();
        nomeUsuarioRemetente  = preferencias.getNome();

        Bundle extra = getIntent().getExtras();

        if (extra != null) {

            nomeUsuarioDestinatario = extra.getString("nome");
            String emailDestinatario = extra.getString("email");
            idUsuarioDestinatario = Base64Custom.codificarBase64(emailDestinatario);
        }

        //configurar o título
        toolbar.setTitle(nomeUsuarioDestinatario);
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left);
        setSupportActionBar(toolbar);

        //montar a listview e adapter
        mensagens = new ArrayList<>();
        adapter = new MensagemAdapter(ConversaActivity.this, mensagens);
        listView.setAdapter(adapter);

        //recuperar as mensagens que estão no Firebase
        firebase = ConfiguracaoFirebase.getFirebase()
                .child("mensagens")
                .child(idUsuarioRemetente)
                .child(idUsuarioDestinatario);

        //criar  listener para mensagens
        valueEventListenerMensagem = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mensagens.clear();

                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    Mensagem mensagem = dados.getValue(Mensagem.class);
                    mensagens.add(mensagem);
                }

                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        firebase.addValueEventListener(valueEventListenerMensagem);

        //Enviar mensagem
        btMensagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textoMesangem = editMensagem.getText().toString();

                if (textoMesangem.isEmpty()) {
                    Toast.makeText(ConversaActivity.this, "Digite uma mensagem para enviar", Toast.LENGTH_SHORT).show();

                } else {

                    Mensagem mensagem = new Mensagem();
                    mensagem.setIdUsuario(idUsuarioRemetente);
                    mensagem.setMensagem(textoMesangem);

                    //salvando mensagem para o remetente
                    Boolean retornoMensagemRemetente = salvarMensagem(idUsuarioRemetente, idUsuarioDestinatario, mensagem);
                    if (!retornoMensagemRemetente){

                        Toast.makeText(ConversaActivity.this,"Problema ao salvar mensagem, tente novamente!"
                        , Toast.LENGTH_LONG).show();

                    }else {

                        //salvando mensagem para o destinatário
                        Boolean retornoMensagemDestinatario = salvarMensagem(idUsuarioDestinatario, idUsuarioRemetente, mensagem);

                        if (!retornoMensagemDestinatario){

                            Toast.makeText(ConversaActivity.this,"Problema ao salvar mensagem, tente novamente!"
                                    , Toast.LENGTH_LONG).show();

                        }
                    }

                    //salvar conversa para o remetente
                    Conversa conversa = new Conversa();
                    conversa.setIdUsuario( idUsuarioDestinatario );
                    conversa.setNome(nomeUsuarioDestinatario);
                    conversa.setMensagem(textoMesangem);
                    salvarConversa(idUsuarioRemetente, idUsuarioDestinatario, conversa );
                    Boolean retornoConversaRemetente = salvarConversa(idUsuarioRemetente, idUsuarioDestinatario, conversa);
                    if (!retornoConversaRemetente){

                        Toast.makeText(ConversaActivity.this,"Problema ao salvar mensagem, tente novamente!"
                                , Toast.LENGTH_LONG).show();

                    }else{

                        //salvar conversa para o destinatario
                        conversa.setIdUsuario( idUsuarioRemetente);
                        conversa.setNome( nomeUsuarioRemetente );
                        conversa.setMensagem(textoMesangem);

                        Boolean retornoConversaDestinatario = salvarConversa(idUsuarioDestinatario, idUsuarioRemetente, conversa);
                        if (!retornoConversaDestinatario){

                            Toast.makeText(ConversaActivity.this,"Problema ao salvar mensagem, tente novamente!"
                                    , Toast.LENGTH_LONG).show();
                        }
                    }

                    editMensagem.setText("");


                }
            }
        });


    }

    private boolean salvarMensagem(String idRemetente, String idDestinatario, Mensagem mensagem) {
        try {

            firebase = ConfiguracaoFirebase.getFirebase().child("mensagens");

            firebase.child(idRemetente)
                    .child(idDestinatario)
                    .push()
                    .setValue(mensagem);
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean salvarConversa(String idRemetente, String idDestinatario, Conversa conversa){

        try {

            firebase = ConfiguracaoFirebase.getFirebase().child("conversas");
            firebase.child(idRemetente)
                    .child(idDestinatario)
                    .setValue( conversa );
            return true;

        }catch (Exception e){
                e.printStackTrace();
                return  false;


        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebase.removeEventListener( valueEventListenerMensagem );
    }
}
