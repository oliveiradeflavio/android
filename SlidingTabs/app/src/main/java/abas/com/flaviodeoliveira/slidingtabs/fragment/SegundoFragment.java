package abas.com.flaviodeoliveira.slidingtabs.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import abas.com.flaviodeoliveira.slidingtabs.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class SegundoFragment extends Fragment {

    private Switch ligadoDesligado;

    public SegundoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_segundo, container, false);

        ligadoDesligado = (Switch) view.findViewById(R.id.switch_LD);

        ligadoDesligado.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    Toast.makeText(getContext(), "LIGADO: Estou no segundo Fragment", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getContext(),"DESLIGADO: Estou no segundo Fragment", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

}
