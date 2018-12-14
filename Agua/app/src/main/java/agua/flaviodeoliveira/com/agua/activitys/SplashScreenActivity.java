package agua.flaviodeoliveira.com.agua.activitys;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import agua.flaviodeoliveira.com.agua.R;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent iSS = new Intent(SplashScreenActivity.this, MainActivity.class);
                startActivity(iSS);
                finish();
            }
        },1000);
    }
}
