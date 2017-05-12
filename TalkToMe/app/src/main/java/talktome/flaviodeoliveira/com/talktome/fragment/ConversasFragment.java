package talktome.flaviodeoliveira.com.talktome.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import talktome.flaviodeoliveira.com.talktome.R;
import talktome.flaviodeoliveira.com.talktome.activity.ConversaActivity;
import talktome.flaviodeoliveira.com.talktome.adapter.ConversaAdapter;
import talktome.flaviodeoliveira.com.talktome.adapter.MensagemAdapter;
import talktome.flaviodeoliveira.com.talktome.helper.Base64Custom;
import talktome.flaviodeoliveira.com.talktome.helper.ConfiguracaoFirebase;
import talktome.flaviodeoliveira.com.talktome.helper.Preferencias;

import talktome.flaviodeoliveira.com.talktome.model.Conversa;


/**
 * A simple {@link Fragment} subclass.
 */
public class ConversasFragment extends Fragment {

    private ListView listView;
    private ArrayList<Conversa> conversas;
    private ArrayAdapter<Conversa> adapter;
    private DatabaseReference firebase;
    private ValueEventListener valueEventListenerConversas;


    public ConversasFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_conversas, container, false);

        //monta a listview e adapter
        conversas = new ArrayList<>();
        listView = (ListView) view.findViewById(R.id.lv_conversas);
        adapter = new ConversaAdapter(getActivity(), conversas);
        listView.setAdapter( adapter );

        //recuperar dados do Usuário
        Preferencias preferencias = new Preferencias(getActivity());
        String idUsuarioLogado = preferencias.getIdentificador();


        //recuperar conversas do Firebase
        firebase = ConfiguracaoFirebase.getFirebase()
                .child("conversas")
                       .child( idUsuarioLogado );

        valueEventListenerConversas = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //limpar a lista (array de conversas)
                conversas.clear();

                //listar as conversas
                for (DataSnapshot dados: dataSnapshot.getChildren() ){

                    Conversa conversa = dados.getValue( Conversa.class );
                    conversas.add( conversa );

                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        //adicionar evento de clique na lista de conversas
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getActivity(), ConversaActivity.class);

                //recuperando dados a serem passados
                Conversa conversa = conversas.get( position );

                intent.putExtra("nome", conversa.getNome() );
                String email = Base64Custom.decofificarBase64( conversa.getIdUsuario() );
                intent.putExtra("email", email);

                startActivity( intent );
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        firebase.addValueEventListener(valueEventListenerConversas);
    }

    @Override
    public void onStop() {
        super.onStop();
        firebase.removeEventListener(valueEventListenerConversas);
    }
}
