package flaviodeoliveira.com.br.ebscan;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class Resultado extends AppCompatActivity {

    private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultado);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().hide();

        img = (ImageView) findViewById(R.id.imageView_Resultado);


        Bundle bundle = getIntent().getExtras();
        if (bundle != null){

            String recebendoResultado;
            recebendoResultado = bundle.getString("resultadoQrcode");

            if (recebendoResultado.equals("sim")){


               // ImageView img = new ImageView(this);
                img.setBackgroundResource(R.drawable.produtooriginal);



            }else{

               // ImageView img = new ImageView(this);
                img.setBackgroundResource(R.drawable.produtofalsificado);

            }
        }




    }

}
