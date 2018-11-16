package floda.pl.floda3;

import android.content.DialogInterface;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.textclassifier.TextLinks;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Floda_SING_UP extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floda__sing__up);
        Button req = (Button) findViewById(R.id.sing_up_butt);

        final EditText name = (EditText) findViewById(R.id.sing_name);
        final EditText surname = (EditText) findViewById(R.id.sing_surname);
        final EditText nick = (EditText) findViewById(R.id.sing_nick);
        final EditText mail = (EditText) findViewById(R.id.sing_mail);
        final EditText pass = (EditText) findViewById(R.id.sing_pass);
        final EditText repass = (EditText) findViewById(R.id.sing_repass);
        final CheckBox agree = (CheckBox) findViewById(R.id.sing_agree);

        req.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText[] data = new EditText[6];
                data[0] = name;
                data[1] = surname;
                data[2] = nick;
                data[3] = mail;
                data[4] = pass;
                data[5] = repass;
                //data[6]=agree.isChecked()==true? "1":"0";
                boolean correct = true;
                Log.d("c", String.valueOf(name.getText()));
                StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://www.serwer1727017.home.pl/2ti/floda/app/singup.php", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        AlertDialog.Builder b = new AlertDialog.Builder(Floda_SING_UP.this);
                        b.setMessage(response).setTitle("Sukces!").setPositiveButton("Super!", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                onBackPressed();
                            }
                        }).setOnKeyListener(new DialogInterface.OnKeyListener() {
                            @Override
                            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                                if (keyCode == KeyEvent.KEYCODE_BACK) {
                                    onBackPressed();
                                }
                                return true;
                            }
                        }).create();
                        b.show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("singup", "błąd");
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> d = new HashMap<String, String>();
                        d.put("name", data[0].getText().toString());
                        d.put("surname", data[1].getText().toString());
                        d.put("nick", data[2].getText().toString());
                        d.put("mail", data[3].getText().toString());
                        d.put("password", data[4].getText().toString());
                        return d;

                    }
                };

                for (int i = 0; i < data.length; i++) {
                    if (data[i].getText().toString().isEmpty()) {
                        data[i].setError("Empty field");
                        correct = false;
                    }
                    if(i==3&&!data[i].getText().toString().contains("@")){
                        data[i].setError("This is not an email!");
                        correct = false;
                    }
                    if (i == 5 && (!data[i].getText().toString().equals(data[i - 1].getText().toString()))) {
                        data[i].setError("Password must be equal to the first");
                        correct = false;
                    } else {
                        if (i == 5)
                            data[i].setError(null);
                    }

                }
                if (!agree.isChecked()) {
                    correct = false;
                    agree.setError("Accept the policy");
                } else {
                    agree.setError(null);
                }
                if (correct) {

                    RequestQueue q = new RequestQueue(new DiskBasedCache(getCacheDir(), 1024 * 1024), new BasicNetwork(new HurlStack()));
                    q.start();
                    q.add(stringRequest);
                }
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
