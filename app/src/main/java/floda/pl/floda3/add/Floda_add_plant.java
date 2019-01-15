package floda.pl.floda3.add;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

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
import java.util.Map;

import floda.pl.floda3.R;

public class Floda_add_plant extends AppCompatActivity {
    Button test,list;
    TextInputEditText log, has;
    StringRequest stringRequest;
    int id_genre=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floda_add_plant);
        test = findViewById(R.id.test_add);
        String sql = "http://serwer1727017.home.pl/2ti/floda/add/test.php";
        log = findViewById(R.id.nrsondy);
        has = findViewById(R.id.haslosondy);
        list = findViewById(R.id.list_of_genre);
        test.setOnClickListener(v -> {
            Log.e("d", log.getText().toString() + " " + has.getText().toString());
            if (has.getText().toString() != "") {
                if (log.getText().toString() != "") {
                    stringRequest = new StringRequest(Request.Method.POST, sql, response -> {
                        Log.e("d", response);
                        if (response.contains("1")) {
                            test.setBackgroundColor(getResources().getColor(R.color.center));
                            test.setText("Ok!");
                        } else {
                            test.setBackgroundColor(getResources().getColor(R.color.red));
                            test.setText("Złe dane");
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> parms = new HashMap<>();
                            parms.put("log", log.getText().toString());
                            parms.put("has", has.getText().toString());
                            return parms;
                        }
                    };
                } else {
                    log.setError("Puste pole");
                }
            } else {
                has.setError("Puste pole");
            }
            RequestQueue q = new RequestQueue(new DiskBasedCache(getCacheDir(), 1024 * 1024), new BasicNetwork(new HurlStack()));
            q.add(stringRequest);
            q.start();
        });
        list.setOnClickListener(v -> {
            Intent i = new Intent(this, Floda_list_genre.class);
            startActivityForResult(i, 1);

        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                 id_genre = data.getIntExtra("id",0);
            }
        }
    }
}

