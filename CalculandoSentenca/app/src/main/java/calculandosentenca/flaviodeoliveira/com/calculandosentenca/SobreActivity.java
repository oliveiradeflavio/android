package calculandosentenca.flaviodeoliveira.com.calculandosentenca;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class SobreActivity extends AppCompatActivity {

    private TextView nomeApp;
    private ImageView linkedin;
    PackageInfo pinfo;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sobre);

        nomeApp = (TextView) findViewById(R.id.txt_NomeVersaoApp);
        linkedin = (ImageView) findViewById(R.id.imageView_Linkedin);


        //ads
        MobileAds.initialize(this, "ca-app-pub-5013914840993100~6392248272");
        AdView adView = (AdView) findViewById(R.id.adViewSobre);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Sobre");
        toolbar.setNavigationIcon(R.drawable.ic_keyboard_arrow_left);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);


        try{

            pinfo = getPackageManager().getPackageInfo(getPackageName(),0);

        }catch (Exception e ){ e.printStackTrace();}

        nomeApp.setText(pinfo.applicationInfo.loadLabel(getPackageManager()).toString() + " Versão " +
         pinfo.versionName);

        linkedin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentLinkedin = new Intent();
                intentLinkedin.setAction(Intent.ACTION_VIEW);
                intentLinkedin.addCategory(Intent.CATEGORY_BROWSABLE);
                intentLinkedin.setData(Uri.parse("https://www.linkedin.com/in/talitavnobrega/"));
                startActivity(intentLinkedin);
            }
        });
    }
}
