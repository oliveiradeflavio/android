package talktome.flaviodeoliveira.com.talktome.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.support.v4.app.Fragment;
import android.util.Log;
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
import talktome.flaviodeoliveira.com.talktome.activity.MainActivity;
import talktome.flaviodeoliveira.com.talktome.adapter.ContatoAdapter;
import talktome.flaviodeoliveira.com.talktome.helper.ConfiguracaoFirebase;
import talktome.flaviodeoliveira.com.talktome.helper.Preferencias;
import talktome.flaviodeoliveira.com.talktome.model.Contato;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContatosFragment extends Fragment {

    private ListView listView;
    private ArrayAdapter adapter;
    private ArrayList<Contato> contatos;
    private DatabaseReference firebase;
    private ValueEventListener valueEventListenerContatos;


    public ContatosFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        firebase.addValueEventListener( valueEventListenerContatos );
        Log.i("ValueEventListener", "onStart");
    }

    @Override
    public void onStop() {
        super.onStop();
        firebase.removeEventListener( valueEventListenerContatos );
        Log.i("ValueEventListener", "onStop");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //instânciar o arraylist
        contatos = new ArrayList<>();


        // Inflate the layout for this fragment
        View view  = inflater.inflate(R.layout.fragment_contatos, container, false);

        listView = (ListView) view.findViewById(R.id.lv_contatos);

        adapter = new ContatoAdapter(getActivity(), contatos);
        listView.setAdapter( adapter );

        //recuperar contatos que estao no firebase
        Preferencias preferencias = new Preferencias(getActivity());
        String identificadorUsuarioLogado = preferencias.getIdentificador();
        firebase = ConfiguracaoFirebase.getFirebase()
                    .child("contatos")
                    .child( identificadorUsuarioLogado  );

        //listener para recuperar contatos
        valueEventListenerContatos = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //limpar a lista (array de contatos)
                contatos.clear();

                //listar os contatos
                for (DataSnapshot dados: dataSnapshot.getChildren() ){

                    Contato contato = dados.getValue( Contato.class );
                    contatos.add( contato );

                }
                adapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {



            }
        };

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ConversaActivity.class);

                //recupera dados a serem passados
                Contato contato = contatos.get(position);

                //enviando dados para conversa Activity
                intent.putExtra("nome", contato.getNome() );
                intent.putExtra("email", contato.getEmail() );



                startActivity(intent);
            }
        });

        return view;
    }

}
