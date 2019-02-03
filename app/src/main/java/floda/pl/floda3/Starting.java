package floda.pl.floda3;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;

import java.util.Locale;

public class Starting extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting);

        ConstraintLayout constraintLayout = findViewById(R.id.startingbck);
        AnimationDrawable drawable = (AnimationDrawable) constraintLayout.getBackground();
        drawable.setEnterFadeDuration(0);
        drawable.setExitFadeDuration(4500);
        drawable.start();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String id = preferences.getString("ID", "0");
        Log.e("Id", id);
        Resources res = getBaseContext().getResources();
// Change locale settings in the app.
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        String lang = preferences.getString("language", "");
        conf.locale= new Locale(lang); // API 17+ only.
        res.updateConfiguration(conf, dm);
        Thread welcomeThread = new Thread() {

            @Override
            public void run() {
                try {
                    super.run();
                    sleep(2000);  //Delay of 2 seconds
                } catch (Exception e) {
                    Log.e("e", e.toString());
                } finally {

                    assert id != null;
                    if (!id.equals("0")) {
                        Intent i = new Intent(getBaseContext(), Floda_main.class);
                        i.putExtra("ID", id);

                        startActivity(i);
                    } else {
                        Intent i = new Intent(getBaseContext(), Floda_LOGIN.class);
                        //i.putExtra("ID",id);

                        startActivity(i);
                    }
                    finish();
                }
            }
        };
        welcomeThread.start();
    }
}
