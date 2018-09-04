package silva.davidson.com.br.culinary;

import android.os.Bundle;
import android.os.Handler;

import silva.davidson.com.br.culinary.views.BaseActivity;

public class SplashScreenActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Handler handle = new Handler();
        handle.postDelayed(new Runnable() {
            @Override
            public void run() {
                showMainActivity();
            }
        }, 2000);

    }

    private void showMainActivity() {
        MainActivity.startActivity(this);
        finish();
    }
}
