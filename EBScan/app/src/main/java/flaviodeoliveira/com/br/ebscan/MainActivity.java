package flaviodeoliveira.com.br.ebscan;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity {

    private Button btn_Escaner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().hide();


        btn_Escaner = (Button) findViewById(R.id.btn_Escanear);
        final Activity activity = this;

        btn_Escaner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                IntentIntegrator integrator = new IntentIntegrator(activity);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES); //tipo de código para escanear
                integrator.setPrompt("Verificando QRCode");
                integrator.setCameraId(0); //codigo 0 para usar a câmera traseira do smartphone
                integrator.initiateScan(); //iniciando o scan

            }
        });


       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        try {

            if (result != null){

                String resultadoQrcode;

                /*
                Como o app foi utilizado como um complemento
                para a apresentação do nosso trabalho de Pós-Graduação,
                utilizei dois mapas simbolizando a mercadoria com o rastreio
                correto e um incorreto.

                No caso, tinhamos duas etiquetas impressas com QRCode simbolizando
                o produto original e o pirata.
                */


                if (result.getContents().equals("produto original")){

                    Intent resultado = new Intent(MainActivity.this, Resultado.class);
                    resultado.putExtra("resultadoQrcode", "sim");
                    startActivity(resultado);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);


                  //  Toast.makeText(this, "QRCODE LIDO COM SUCESSO", Toast.LENGTH_LONG).show();

                }else {

                    Intent resultado = new Intent(MainActivity.this, Resultado.class);
                    resultado.putExtra("resultadoQrcode", "não");
                    startActivity(resultado);
                    Log.d("resultado nao", "nao");
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

                   // Toast.makeText(this, "Mercadoria Piratão", Toast.LENGTH_LONG).show();
                }

            }else {

                super.onActivityResult(requestCode, resultCode, data);
            }

        }catch (Exception e){
            e.printStackTrace();
        }

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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
