package imcdobolso.com.flaviodeoliveira.imcdobolso;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class AjudaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajuda);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Resumo - Ajuda");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //ads
        AdView adView = (AdView) findViewById(R.id.adViewAjuda);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        //ads banner topo
        AdView adViewTopo = (AdView) findViewById(R.id.adViewAjudaTopo);
        AdRequest adRequestTopo = new AdRequest.Builder().build();
        adViewTopo.loadAd(adRequestTopo);


    }

}
