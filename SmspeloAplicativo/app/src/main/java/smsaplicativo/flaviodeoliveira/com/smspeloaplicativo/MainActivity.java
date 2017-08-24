package smsaplicativo.flaviodeoliveira.com.smspeloaplicativo;

import android.Manifest;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;

public class MainActivity extends AppCompatActivity {

    //criando os EditText e o Button
    private EditText edt_Numero;
    private EditText edt_Mensagem;
    private Button btn_EnviarSMS;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
        Assim que é iniciado o Aplicativo, um dialog perguntará ao usuário, se ele aceita
        que o aplicativo mande SMS. Caso ele tocar em negar (deny), o mesmo aplicativo
        não irá funcionar (não foi trato por enquanto, como mostrado no vídeo).

        Lembrando que no arquivo de Manifest, é preciso colocar a permissão de envio de sms
         */
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.SEND_SMS
        }, 1);


        //recuperando as ids do XML de cada EditText e Button
        edt_Numero = (EditText) findViewById(R.id.edt_Numero);
        edt_Mensagem = (EditText) findViewById(R.id.edt_Mensagem);
        btn_EnviarSMS = (Button) findViewById(R.id.btn_Enviar);

        //Criando a máscara para o campo de celular
        SimpleMaskFormatter smf = new SimpleMaskFormatter("(NN)NNNNN-NNNN");
        MaskTextWatcher mtw = new MaskTextWatcher(edt_Numero, smf);
        edt_Numero.addTextChangedListener(mtw);
        

        btn_EnviarSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String numeroCelular = edt_Numero.getText().toString();
                String mensagem = edt_Mensagem.getText().toString();

                //criando uma variavel do tipo SMSManager
                //Usando como padrão o getDefault() para o envio de SMS
                SmsManager smsManager = SmsManager.getDefault();

                /*
                Setando os valores, o primeiro com o número do celular do destinatário
                e o terceiro será com a mensagem digitada pelos usuários. Os dois
                últimos valores são PendingIntent, que nesse caso não estamos
                utilizando o retorno da mensagem (resposta do destinatário para o
                remetente).
                 */
                smsManager.sendTextMessage(numeroCelular, null, mensagem, null, null);

                //Exibindo uma mensagem informativa de sucesso ao enviar.
                Toast.makeText(MainActivity.this, "Mensagem Enviada", Toast.LENGTH_SHORT).show();

            }
        });
    }
}
