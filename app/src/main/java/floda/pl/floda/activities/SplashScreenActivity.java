package floda.pl.floda.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import floda.pl.floda.R;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String id = preferences.getString("ID", "0");
        Resources res = getBaseContext().getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        String lang = preferences.getString("language", "");
        if (lang.length() != 0) {
            conf.locale = new Locale(lang);
            res.updateConfiguration(conf, dm);
        }
        String url="http://serwer1727017.home.pl/2ti/floda/checkinglogin.php";
        StringRequest s = new StringRequest(Request.Method.POST, url, response -> {
            assert id != null;
            if (!id.equals("0") && !response.contains("0")) {
                Intent i = new Intent(getBaseContext(), MenuActivity.class);
                i.putExtra("ID", id);
                startActivity(i);
            } else {
                Intent i = new Intent(getBaseContext(), LoginActivity.class);
                //i.putExtra("ID",id);
                startActivity(i);
            }
            finish();
        }, error -> { }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> h = new HashMap<>();
                h.put("login", id);
                return h;
            }
        };
        Thread welcomeThread = new Thread() {

            @Override
            public void run() {
                try {
                    super.run();
                    RequestQueue q = new RequestQueue(new DiskBasedCache(getCacheDir(), 1024 * 1024), new BasicNetwork(new HurlStack()));
                    q.add(s);

                    q.start();
                } catch (Exception e) {
                    Log.e("e", e.toString());
                }
            }
        };

        welcomeThread.start();

    }
}
