package abas.com.flaviodeoliveira.slidingtabs.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import abas.com.flaviodeoliveira.slidingtabs.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class PrimeiroFragment extends Fragment {

    private Button botaoFragmento;

    public PrimeiroFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_primeiro, container, false);

        //recuperando o elemento botão
        botaoFragmento = (Button) view.findViewById(R.id.btn_CliqueAqui);

        botaoFragmento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Você clicou em mim. Primeiro Fragmento", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

}
