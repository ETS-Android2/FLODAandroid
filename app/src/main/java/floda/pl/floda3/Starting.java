package floda.pl.floda3;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Starting extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting);

        ConstraintLayout constraintLayout = findViewById(R.id.startingbck);
        AnimationDrawable drawable = (AnimationDrawable) constraintLayout.getBackground();

        runOnUiThread(() -> {
            AnimationDrawable anim;
            anim = (AnimationDrawable) constraintLayout.getBackground();
            anim.setEnterFadeDuration(4000);
            anim.setExitFadeDuration(1000);
            anim.start();
        });
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String id = preferences.getString("ID", "0");
        Log.e("Id", id);
        Resources res = getBaseContext().getResources();
// Change locale settings in the app.
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        String lang = preferences.getString("language", "");
        if (lang.length() != 0) {
            conf.locale = new Locale(lang); // API 17+ only.
            res.updateConfiguration(conf, dm);
        }
        String url="http://serwer1727017.home.pl/2ti/floda/checkinglogin.php";
        StringRequest s = new StringRequest(Request.Method.POST, url, (Response.Listener<String>) response -> {
            assert id != null;
            if (!id.equals("0") && !response.contains("0")) {
                Intent i = new Intent(getBaseContext(), Floda_main.class);
                i.putExtra("ID", id);
                startActivity(i);
            } else {
                Intent i = new Intent(getBaseContext(), Floda_LOGIN.class);
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

                    sleep(2000);  //Delay of 2 seconds
                } catch (Exception e) {
                    Log.e("e", e.toString());
                }finally {
                    RequestQueue q = new RequestQueue(new DiskBasedCache(getCacheDir(), 1024 * 1024), new BasicNetwork(new HurlStack()));
                    q.add(s);

                    q.start();
                }
            }
        };

        welcomeThread.start();

    }
}
