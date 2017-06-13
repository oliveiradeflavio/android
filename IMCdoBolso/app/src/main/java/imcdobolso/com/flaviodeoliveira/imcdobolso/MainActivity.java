package imcdobolso.com.flaviodeoliveira.imcdobolso;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private Spinner spinnerIdade;
    private EditText idade;
    private EditText salario;
    private Button calcular;
    private int selecao;

    //ads intersticial
    private InterstitialAd meuAdsIntersticial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //ads intersticial
        MobileAds.initialize(this, "ca-app-pub-5013914840993100~2824516275");
        meuAdsIntersticial = new InterstitialAd(this);
        meuAdsIntersticial.setAdUnitId("ca-app-pub-5013914840993100/4301249473");

        meuAdsIntersticial.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewIntersticial();
                verificaCampos();
            }
        });
        requestNewIntersticial();

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        spinnerIdade = (Spinner) findViewById(R.id.spinner_Idade);
        idade = (EditText) findViewById(R.id.editText_idade);
        salario = (EditText) findViewById(R.id.editText_salario);
        calcular = (Button) findViewById(R.id.btn_calcular);

        //Mascara para o campo salário
        SimpleMaskFormatter smf = new SimpleMaskFormatter("N.NNN,NN");
        MaskTextWatcher mtw = new MaskTextWatcher(salario, smf);
        salario.addTextChangedListener(mtw);

        //Montando o spinner
        ArrayAdapter adapter = ArrayAdapter.createFromResource(MainActivity.this,
                R.array.spinner_idade, android.R.layout.simple_list_item_1);

        adapter.setDropDownViewResource(android.R.layout.simple_selectable_list_item);
        spinnerIdade.setAdapter(adapter);
        spinnerIdade.setOnItemSelectedListener(this);

        calcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                verificaCampos();

            }
        });

        //ads banner
        AdView adView = (AdView) findViewById(R.id.adViewMain);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

    }

    private void requestNewIntersticial() {
        AdRequest adRequest = new AdRequest.Builder().build();

        meuAdsIntersticial.loadAd(adRequest);

    }

    private void calcularIMC() {
        int idade_inteiro = Integer.parseInt(idade.getText().toString());

        //removendo a máscara aplicada no campo salario
        String salarioformatado = salario.getText().toString().replace(".", "");
        String salarioformatado2 = salarioformatado.replace(",","");

        int salario_inteiro = Integer.parseInt(salarioformatado2);

        if (selecao == 0) {

            int porcento = idade_inteiro - 15;
            int porcentoSalario = salario_inteiro * porcento / 100;
            if(meuAdsIntersticial.isLoaded()){
                meuAdsIntersticial.show();
            }else{

                Intent intent = new Intent(MainActivity.this, ResultadoActivity.class);
                intent.putExtra("resultado", Integer.toString(porcentoSalario));
                startActivity(intent);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                // resultado.setText(Integer.toString(porcentoSalario));
            }


        } else if (selecao == 1) {

            int porcento = idade_inteiro - 10;
            int porcentoSalario = (salario_inteiro * porcento) / 100;

            if(meuAdsIntersticial.isLoaded()){
                meuAdsIntersticial.show();
            }else{

                Intent intent = new Intent(MainActivity.this, ResultadoActivity.class);
                intent.putExtra("resultado", Integer.toString(porcentoSalario));
                startActivity(intent);
                // resultado.setText(Integer.toString(porcentoSalario));
            }

        } else if (selecao == 2) {
            //int porcento = Integer.parseInt(idade.getText().toString()) - 15;
            int porcentoSalario = (salario_inteiro * 50) / 100;

            if(meuAdsIntersticial.isLoaded()){
                meuAdsIntersticial.show();
            }else{

                Intent intent = new Intent(MainActivity.this, ResultadoActivity.class);
                intent.putExtra("resultado", Integer.toString(porcentoSalario));
                startActivity(intent);
                // resultado.setText(Integer.toString(porcentoSalario));
            }

        } else {
            Toast.makeText(MainActivity.this, "Verifique as opções e tente novamente",
                    Toast.LENGTH_SHORT).show();
        }
    }
    private void verificaCampos(){
        try {
            if(selecao == 0) {
                // Toast.makeText(MainActivity.this,"Estou na posicao : " + selecao, Toast.LENGTH_LONG ).show();
                if (Integer.parseInt(idade.getText().toString()) < 25 ||
                        Integer.parseInt(idade.getText().toString()) > 40) {

                    idade.setError("Selecione outra data para poupar");

                }else if(salario.getText().toString().isEmpty()) {

                    salario.setError("Preencha o campo");

                }else{
                    calcularIMC();
                }
            }else if(selecao == 1) {
                //  Toast.makeText(MainActivity.this,"Estou na posicao : " + selecao, Toast.LENGTH_LONG ).show();
                if (Integer.parseInt(idade.getText().toString()) < 41 ||
                        Integer.parseInt(idade.getText().toString()) > 49) {

                    idade.setError("Selecione outra data para poupar");

                }else if(salario.getText().toString().isEmpty()) {

                    salario.setError("Preencha o campo");
                }else{
                    calcularIMC();

                }
            }else if(selecao == 2){
                if (Integer.parseInt(idade.getText().toString()) < 50 ) {

                    idade.setError("Selecione outra data para poupar");

                }else if(salario.getText().toString().isEmpty()) {

                    salario.setError("Preencha o campo");

                }else if(Integer.parseInt(idade.getText().toString()) > 100) {
                    idade.setError("Você está vivinho da silva heim =D");

                }else{
                    calcularIMC();
                }
            }else {
                Toast.makeText(MainActivity.this,"Verifique os campos e tente novamente",
                        Toast.LENGTH_SHORT).show();
            }


        }catch (Exception e){
            Toast.makeText(MainActivity.this,"Verifique os campos e tente novamente",
                    Toast.LENGTH_SHORT).show();
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
        if (id == R.id.ajuda) {
            Intent ajuda = new Intent(MainActivity.this, AjudaActivity.class);
            startActivity(ajuda);
            return true;

        }else if(id == R.id.sobre){
            Intent sobre = new Intent(MainActivity.this, SobreActivity.class);
            startActivity(sobre);
            return true;

        }else if(id == R.id.sair){
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    //spinner metodos
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        selecao = (int) id;

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
