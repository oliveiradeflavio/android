package agua.flaviodeoliveira.com.agua.activitys;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.InputType;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

import agua.flaviodeoliveira.com.agua.R;
import agua.flaviodeoliveira.com.agua.dao.Pedido;

public class MainActivity extends AppCompatActivity {

    private EditText edt_Nome;
    private EditText edt_Endereco;
    private EditText edt_Celular;
    private NumberPicker np_Garrafa;
    private NumberPicker np_Garrafao10;
    private NumberPicker np_Garrafao20;
    private TextView txt_totalGarrafa;
    private TextView txt_totalGarrafao10;
    private TextView txt_totalGarrafao20;
    private EditText edt_TotalGeral;
   // private RadioGroup radioG_Principal;
   // private RadioButton radioB_Escolhido;
   // private RadioButton radioButtonNao;
    private EditText edt_troco;


    private int qntGarrafa;
    private int qntGarrafao10;
    private int qntGarrafao20;
    private int total_Garrafa;
    private int total_Garrafao10;
    private int total_Garrafao20;
    private int totalGeral = 0;
  //  private String troco;

    AlertDialog alerta;
    AlertDialog.Builder dialog;


    //firebase
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Faça seu pedido");
        setSupportActionBar(toolbar);

        edt_Nome = (EditText) findViewById(R.id.edt_Nome);
        edt_Endereco = (EditText) findViewById(R.id.edt_Endereco);
        edt_Celular = (EditText) findViewById(R.id.edt_Celular);
        np_Garrafa = (NumberPicker) findViewById(R.id.np_Garrafa);
        np_Garrafao10 = (NumberPicker) findViewById(R.id.np_garrafao10l);
        np_Garrafao20 = (NumberPicker) findViewById(R.id.np_garrafao20l);
        txt_totalGarrafa = (TextView) findViewById(R.id.txt_TotalGarrafa);
        txt_totalGarrafao10 = (TextView) findViewById(R.id.txt_Total10L);
        txt_totalGarrafao20 = (TextView) findViewById(R.id.txt_Total20L);
       // edt_TotalGeral = (EditText) findViewById(R.id.edt_Total);
       // radioG_Principal = (RadioGroup) findViewById(R.id.radioGroup_Principal);
       // radioButtonNao = (RadioButton) findViewById(R.id.rb_Nao);

        //recuperando foco no primeiro edittext
        edt_Nome.requestFocus();

        //criando exibição de dialog
        dialog  = new AlertDialog.Builder(MainActivity.this);

        //criando mascaras para o campo telefone e celular
        SimpleMaskFormatter smf2 = new SimpleMaskFormatter("(NN)NNNNN-NNNN");
        MaskTextWatcher mtw2 = new MaskTextWatcher(edt_Celular, smf2);
        edt_Celular.addTextChangedListener(mtw2);

        //configurando os numberpickers
        np_Garrafa.setMaxValue(5);
        np_Garrafao10.setMaxValue(5);
        np_Garrafao20.setMaxValue(5);

        np_Garrafa.setMinValue(0);
        np_Garrafao10.setMinValue(0);
        np_Garrafao20.setMinValue(0);

        np_Garrafa.setWrapSelectorWheel(true);
        np_Garrafao10.setWrapSelectorWheel(true);
        np_Garrafao20.setWrapSelectorWheel(true);

        escolheQntGalao();
        //FIM DA CONFIGURACAO NUMBERPICKERS

       // radioButtonEscolhido();

        //inicia firebase;
        iniciaFirebase();

    }

    private void iniciaFirebase() {

       firebaseDatabase = FirebaseDatabase.getInstance();
       databaseReference = firebaseDatabase.getReference();

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
        if (id == R.id.enviarPedido) {
            enviarPedido();
            return true;

        }else if (id == R.id.sair){
            System.exit(0);
            return true;

        }else if (id == R.id.consultar_pedido){
            consultarPedidos();
            return true;

        }else if (id == R.id.administra_pedido){
            administrarPedidos();
            return true;

        }else if (id == R.id.sobre){
            sobre();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void sobre() {
       /* Intent iSobre = new Intent(this, SobreActivity.class);
        startActivity(iSobre);*/

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);


        //definir o titulo
        builder.setTitle("Saiba mais");
        //definir mensagem
        builder.setMessage(Html.fromHtml("<a href=\"https://www.flaviodeoliveira.com.br\">Clique aqui para " +
                "conferir meus outros jobs</a>"));
       // builder.setIcon(R.drawable.flaviologo);
        builder.setPositiveButton(android.R.string.ok, null);
        alerta = builder.create();
        alerta.show();
        ((TextView)alerta.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());


    }

    private void administrarPedidos() {
        Intent iAdmPedidos = new Intent(MainActivity.this, AdmPedidosActivity.class);
        startActivity(iAdmPedidos);
    }

    private void consultarPedidos() {
        Intent iConsultaPedido = new Intent(this, ConsultaPedidoActivity.class);
        startActivity(iConsultaPedido);

    }

    private void enviarPedido() {

        try {
            somaPedido();
            final Pedido pd = new Pedido();

            if (edt_Nome.getText().toString().length() < 10){
                edt_Nome.setError("Digite seu nome completo");

            }else if (edt_Nome.getText().toString().isEmpty()) {
                edt_Nome.setError("Campo Obrigatório");

            }else if (edt_Endereco.getText().toString().length() < 10){
                edt_Endereco.setError("Digite o endereço completo");

            }else if (edt_Endereco.getText().toString().isEmpty()){
                edt_Endereco.setError("Campo Obrigatório");

            }else if (edt_Celular.getText().toString().isEmpty()) {
                edt_Celular.setError("Campo Obrigatório");

            }else if (total_Garrafa <= 0 && total_Garrafao10 <= 0 && total_Garrafao20 <= 0){
                Toast.makeText(MainActivity.this, "Você não escolheu a quantidade de galões de água",
                        Toast.LENGTH_SHORT).show();

            }else {

                final String nomeNo = edt_Celular.getText().toString();
                edt_troco = new EditText(MainActivity.this);
                edt_troco.setHint("Troco para quanto?");
                edt_troco.setInputType(InputType.TYPE_CLASS_NUMBER);
                edt_troco.setGravity(Gravity.CENTER);

                pd.setUuid(UUID.randomUUID().toString());
                pd.setNome(edt_Nome.getText().toString());
                pd.setEndereco(edt_Endereco.getText().toString());
                pd.setCelular(edt_Celular.getText().toString());
                pd.setGarrafapct(String.valueOf(qntGarrafa));
                pd.setGarrafao10(String.valueOf(qntGarrafao10));
                pd.setGarrafao20(String.valueOf(qntGarrafao20));

                dialog.setTitle("Informações do Pedido");
                dialog.setIcon(R.drawable.ic_pedido);
                dialog.setCancelable(false);
                dialog.setView(edt_troco);

                dialog.setMessage(edt_Nome.getText().toString() + ", o total do seu pedido " +
                        "foi R$ " + totalGeral + ". Se você precisa de troco, digite no campo " +
                        "abaixo, caso contrário deixei em branco." +
                        "" +
                        "" );

                dialog.setPositiveButton("Confirmar Pedido", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        String troco = edt_troco.getText().toString();
                        String status = "Pedido Enviado";
                        long date;
                        date = System.currentTimeMillis();
                        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyy HH:mm");
                        String dateString = sdf.format(date);

                        pd.setTroco(troco);
                        pd.setTotalgeral(String.valueOf(totalGeral));
                        pd.setDataehorario(dateString);
                        pd.setStatus(status);

                        databaseReference.child("Pedidos").child(nomeNo).child(pd.getUuid()).setValue(pd);
                        databaseReference.child("AdmPedido").child("Pedido").child(pd.getUuid()).setValue(pd);

                        Toast.makeText(MainActivity.this, "Pedido Enviado com sucesso", Toast.LENGTH_SHORT).show();
                        limpaCampos();


                    }
                });
                dialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        totalGeral = 0;
                    }
                });

                dialog.create();
                dialog.show();
            }

        }catch (Exception e){
            Toast.makeText(MainActivity.this, "Erro: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }


    }

    private void escolheQntGalao(){

        np_Garrafa.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int numEscolhido) {

                qntGarrafa = numEscolhido;
                total_Garrafa = qntGarrafa * 10;
                txt_totalGarrafa.setText("R$ " + total_Garrafa);
                Log.i("garrafa", String.valueOf(qntGarrafa));

            }
        });

        np_Garrafao10.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int numEscolhido) {

                qntGarrafao10 = numEscolhido;
                total_Garrafao10 = qntGarrafao10 * 5;
                txt_totalGarrafao10.setText("R$ " + total_Garrafao10);
                Log.i("garrafa10", String.valueOf(qntGarrafao10));

            }
        });

        np_Garrafao20.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int numEscolhido) {

                qntGarrafao20 = numEscolhido;
                total_Garrafao20 = qntGarrafao20 * 10;
                txt_totalGarrafao20.setText("R$ " + total_Garrafao20);
                Log.i("garrafa20", String.valueOf(qntGarrafao20));

                 }

            });
    }

   /* private void radioButtonEscolhido() {

        radioButtonNao.setChecked(true);
        edt_troco.setEnabled(false);

        radioG_Principal.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {

                int idRadioButtonEscolhido = radioG_Principal.getCheckedRadioButtonId();
                radioB_Escolhido = (RadioButton) findViewById(idRadioButtonEscolhido);


                if (idRadioButtonEscolhido < 0) {
                    troco = "nada";
                    Log.i("radiobutton", "nada");

                } else if (idRadioButtonEscolhido > 0) {

                    if (radioB_Escolhido.getText().equals("Sim")) {
                        //Toast.makeText(MainActivity.this, "Sim", Toast.LENGTH_SHORT).show();
                        troco = "Sim";
                        Log.i("radiobutton", "sim");
                        edt_troco.setEnabled(true);
                        edt_troco.setHint("Ex: 2,50");

                    } else {
                        troco = "Nao";
                        // Toast.makeText(MainActivity.this, "Nao", Toast.LENGTH_SHORT).show();
                        Log.i("radiobutton", "nao");
                        edt_troco.setEnabled(false);
                        edt_troco.setHint("");

                    }
                }
            }
        });
    }*/

    private void somaPedido(){
        totalGeral = total_Garrafa + total_Garrafao10 + total_Garrafao20;
    }

    private void limpaCampos(){

        edt_Nome.setText("");
        edt_Endereco.setText("");
        edt_Celular.setText("");
        edt_troco.setText("");
        np_Garrafa.setValue(0);
        np_Garrafao10.setValue(0);
        np_Garrafao20.setValue(0);
        total_Garrafa = 0;
        total_Garrafao10 = 0;
        total_Garrafao20 = 0;
        txt_totalGarrafa.setText("R$ ");
        txt_totalGarrafao10.setText("R$ ");
        txt_totalGarrafao20.setText("R$ ");
        edt_Nome.requestFocus();

    }
}
