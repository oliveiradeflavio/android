package br.com.flaviodeoliveira.sendfirstmessage;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

   // private Spinner spinnerDDI;
    private EditText edtCelular;
    private Button btnEnviarMsg;
    private AdView adView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Envie sua primeira mensagem");
        setSupportActionBar(toolbar);

        //ADS do tipo banner
        MobileAds.initialize(this, "ca-app-pub-5013914840993100~6428678725");
        adView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        //adView.loadAd(adRequest);



        //spinnerDDI = (Spinner) findViewById(R.id.spinnerDDI);
        edtCelular = (EditText) findViewById(R.id.edtNumeroCelular);
        btnEnviarMsg = (Button) findViewById(R.id.btnEnviar);

        SimpleMaskFormatter smf = new SimpleMaskFormatter("(NN) NNNNN - NNNN");
        MaskTextWatcher mtw =  new MaskTextWatcher(edtCelular, smf);
        edtCelular.addTextChangedListener(mtw);

        edtCelular.setFocusable(true);

        /*ArrayAdapter arrayAdapter = ArrayAdapter.createFromResource(MainActivity.this,
                R.array.country, android.R.layout.simple_dropdown_item_1line);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinnerDDI.setAdapter(arrayAdapter);
*/



/*
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/



        btnEnviarMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String celular = "55";
                String numeroDireto, numeroTratado1, numeroTratado2, numeroTratado3, numeroTratato4;


                numeroDireto = edtCelular.getText().toString();
                numeroTratado1 = numeroDireto.replace("(", "");
                numeroTratado2 = numeroTratado1.replace(")", "");
                numeroTratado3 = numeroTratado2.replace("-", "");
                numeroTratato4 = numeroTratado3.replace(" ", "");
                celular += numeroTratato4;


                if (edtCelular.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Número inválido ou vazio", Toast.LENGTH_SHORT).show();


                }else if (celular.length() < 11) {
                    Toast.makeText(MainActivity.this, "Número de celular deve conter 11 dígitos", Toast.LENGTH_SHORT).show();

                }else {

                   //Toast.makeText(MainActivity.this, celular, Toast.LENGTH_LONG).show();
                   String url = "https://api.whatsapp.com/send?phone="+celular;
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                }

            }
        });

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
        if (id == R.id.creditos) {

            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            AlertDialog alerta;

            builder.setTitle("Créditos");
            builder.setMessage(Html.fromHtml("<a href=\"https://www.flaviodeoliveira.com.br\">Clique aqui para " +
                    "conferir meus jobs</a>"));
            builder.setPositiveButton(android.R.string.ok, null);
            alerta = builder.create();
            alerta.show();
            ((TextView)alerta.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
