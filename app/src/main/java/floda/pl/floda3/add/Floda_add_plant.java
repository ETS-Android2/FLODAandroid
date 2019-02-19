package floda.pl.floda3.add;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Objects;

import floda.pl.floda3.Floda_LOGIN;
import floda.pl.floda3.Floda_main;
import floda.pl.floda3.R;

public class Floda_add_plant extends AppCompatActivity {
    Button test, list, new_genre_dod_rosl, add_plant_butt;
    TextInputEditText log, has;
    StringRequest stringRequest;
    TextInputEditText add_plant_title;
    String id_genre = "";
    boolean spoko = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floda_add_plant);
        test = findViewById(R.id.test_add);
        String sql = "http://serwer1727017.home.pl/2ti/floda/add/test.php";
        add_plant_butt = findViewById(R.id.add_plant_butt);
        log = findViewById(R.id.nrsondy);
        has = findViewById(R.id.haslosondy);
        list = findViewById(R.id.list_of_genre);
        add_plant_title = findViewById(R.id.add_plant_title);
        new_genre_dod_rosl = findViewById(R.id.new_genre_dod_rosl);
        stringRequest = new StringRequest(Request.Method.POST, sql, response -> {
            Log.e("d", response);
            if (response.contains("1")) {
                test.setBackgroundColor(getResources().getColor(R.color.center));
                test.setText(getString(R.string.Ok));
                spoko = true;
            } else {
                test.setBackgroundColor(getResources().getColor(R.color.red));
                test.setText(getString(R.string.bad_data));
                spoko = false;
            }
        }, error -> {
            Log.e("Blad", error.toString());
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parms = new HashMap<>();
                parms.put("log", log.getText().toString());
                parms.put("has", has.getText().toString());
                return parms;
            }
        };
        test.setOnClickListener(v -> {
            Log.e("d", log.getText().toString() + " " + has.getText().toString());

            if (log.getText().length() != 0) {
                if (has.getText().length() != 0) {

                    RequestQueue q = new RequestQueue(new DiskBasedCache(getCacheDir(), 1024 * 1024), new BasicNetwork(new HurlStack()));
                    q.add(stringRequest);
                    q.start();
                } else {
                    has.setError(getString(R.string.empty_field));
                }
            } else {
                log.setError(getString(R.string.empty_field));
            }

        });
        list.setOnClickListener(v -> {
            Intent i = new Intent(this, Floda_list_genre.class);
            i.putExtra("NR", "1");
            startActivityForResult(i, 1);

        });
        new_genre_dod_rosl.setOnClickListener(v -> {
            Intent i = new Intent(this, FLODA_add_new_genre.class);
            Intent g = getIntent();
            i.putExtra("ID", g.getStringExtra("ID"));
            startActivityForResult(i, 2);
        });
        add_plant_butt.setOnClickListener(v -> {
            add_plant_title.setText(add_plant_title.getText().toString().replaceAll("[0-9]", ""));
            AlertDialog alertDialog5;
            AlertDialog.Builder builder5 = new AlertDialog.Builder(this);
            LayoutInflater l5 = (LayoutInflater) getApplicationContext().getSystemService(getBaseContext().LAYOUT_INFLATER_SERVICE);
            View v5 = l5.inflate(R.layout.loading, null);
            builder5.setView(v5);
            alertDialog5 = builder5.create();
            alertDialog5.show();
            if (!Objects.equals(add_plant_title.getText().toString(), "")) {

                Log.e("f", "" + spoko);
                if (id_genre != "0" && id_genre != "") {
                    String sql2 = "http://serwer1727017.home.pl/2ti/floda/add/add_plant.php";
                    StringRequest s = new StringRequest(Request.Method.POST, sql2, response -> {
                        if (response.contains("1")) {
                            Toast.makeText(this, getString(R.string.add_plant_set_1) + add_plant_title.getText().toString(), Toast.LENGTH_LONG).show(); //todo: w tych przypadkach zrobic box
                            alertDialog5.hide();
                            finish();
                        } else {
                            Toast.makeText(this, getString(R.string.add_plant_set_0), Toast.LENGTH_LONG).show();
                            alertDialog5.hide();
                        }
                    }, error -> {
                        Log.e("Blad wysylania", error.toString());
                        alertDialog5.hide();
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> parms = new HashMap<>();
                            Intent l = getIntent();
                            parms.put("has", has.getText().toString());
                            parms.put("log", log.getText().toString());
                            parms.put("name", add_plant_title.getText().toString());
                            parms.put("id_gatunku", id_genre);
                            parms.put("id_autora", l.getStringExtra("ID"));
                            return parms;
                        }
                    };
                    RequestQueue z = new RequestQueue(new DiskBasedCache(getCacheDir(), 1024 * 1024), new BasicNetwork(new HurlStack()));
                    z.add(s);
                    z.start();
                } else {
                    Toast.makeText(this, getString(R.string.out_off_data), Toast.LENGTH_LONG).show();
                    alertDialog5.hide();
                }
            } else {
                add_plant_title.setError(getString(R.string.epmty_field));
                alertDialog5.hide();
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                id_genre = data.getStringExtra("ID");
                /*Toast d = Toast.makeText(this,id_genre,Toast.LENGTH_LONG);
                 d.show();*/
                list.setText(getString(R.string.choosen_genre) + id_genre);
                list.setBackgroundColor(getColor(R.color.center));
            }
        }
        if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                id_genre = data.getStringExtra("ID");
                /*Toast d = Toast.makeText(this,id_genre,Toast.LENGTH_LONG);
                 d.show();*/
                new_genre_dod_rosl.setText(getString(R.string.choosen_genre) + id_genre);
                new_genre_dod_rosl.setBackgroundColor(getColor(R.color.center));
                list.setEnabled(false);
                new_genre_dod_rosl.setEnabled(false);
            }
        }
    }
}

