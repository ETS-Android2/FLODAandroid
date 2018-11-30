package floda.pl.floda3;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Floda_LOGIN extends AppCompatActivity {
    String id = "0";
    AlertDialog alert;

    @Override
    public void onBackPressed() {
        Log.e("cofanie nie mozliwe","1");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floda__login);
        Button singiup = (Button) findViewById(R.id.sing_up_login);
        Button login = (Button) findViewById(R.id.login_in_login);
        singiup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Floda_SING_UP.class);
                startActivity(i);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder g = new AlertDialog.Builder(Floda_LOGIN.this);
                LayoutInflater background = (LayoutInflater) getApplicationContext().getSystemService(getBaseContext().LAYOUT_INFLATER_SERVICE);
                final View dialogView = background.inflate(R.layout.dialoglogin, null);
                final EditText login = (EditText) dialogView.findViewById(R.id.login_edit);
                final EditText password = (EditText) dialogView.findViewById(R.id.login_edit2);
                final Button log = (Button) dialogView.findViewById(R.id.button);
                final Button ca = (Button) dialogView.findViewById(R.id.button2);

                g.setView(dialogView);

                alert = g.create();
                ca.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alert.dismiss();
                    }
                });
                log.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String r;
                        //Database_connection database_connection = new Database_connection(new RequestQueue(new DiskBasedCache(getCacheDir(), 1024 * 1024), new BasicNetwork(new HurlStack())));
                        getLoginAbout(login.getText().toString(), password.getText().toString(), new RequestQueue(new DiskBasedCache(getCacheDir(), 1024 * 1024), new BasicNetwork(new HurlStack())));
                    }
                });
                alert.show();
            }
        });
    }

    public void getLoginAbout(String login, String password, RequestQueue q) {
        String sql = "http://www.serwer1727017.home.pl/2ti/floda/floda_login.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, sql, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray j = new JSONArray(response);
                    JSONObject o = j.getJSONObject(0);
                    id = o.getString("id");
                    if (id.equals("0")) {
                        AlertDialog.Builder f = new AlertDialog.Builder(Floda_LOGIN.this);
                        f.setTitle("Failed to login");
                        f.setNeutralButton("Try again", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                alert.show();
                            }
                        });
                        f.create().show();

                    } else {
                        Intent i = new Intent(getBaseContext(), Floda_main.class);
                        i.putExtra("ID", id);
                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("ID",id);
                        editor.commit();
                        editor.apply();
                        startActivity(i);
                    }
                    Log.e("r", id);

                } catch (JSONException e) {
                    Log.e("json", e.toString());
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("blad", error.toString());
                //foo[0] =null;
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> h = new HashMap<>();
                h.put("login", login);
                h.put("password", password);
                return h;
            }
        };
        q.add(stringRequest);

        q.start();

        Log.e("r", id);

    }
}
