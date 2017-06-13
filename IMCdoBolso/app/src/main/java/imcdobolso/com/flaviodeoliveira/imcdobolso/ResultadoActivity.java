package imcdobolso.com.flaviodeoliveira.imcdobolso;

import android.annotation.TargetApi;
import android.icu.math.BigDecimal;
import android.icu.text.DecimalFormat;
import android.icu.text.NumberFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.w3c.dom.Text;

public class ResultadoActivity extends AppCompatActivity {

    private TextView resultado;


    @TargetApi(Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultado);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Resultado IMC");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //ads
        AdView adView = (AdView) findViewById(R.id.adViewResultado);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        //ads banner topo
        AdView adViewTopo = (AdView) findViewById(R.id.adViewResultadoTopo);
        AdRequest adRequestTopo = new AdRequest.Builder().build();
        adViewTopo.loadAd(adRequestTopo);


        resultado = (TextView) findViewById(R.id.textView_Resultado);

        //aplicando máscara no campo resultado
        SimpleMaskFormatter sfm = new SimpleMaskFormatter("R$ NNN,NN");
        MaskTextWatcher mtw = new MaskTextWatcher(resultado, sfm);
        resultado.addTextChangedListener(mtw);

        //recuperando os dados da MainActivity
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            String resultadoIMC = bundle.getString("resultado");
            resultado.setText(resultadoIMC);

        }

    }

}
