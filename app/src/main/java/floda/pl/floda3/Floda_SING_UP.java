package floda.pl.floda3;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class Floda_SING_UP extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floda__sing__up);
        Button req =  findViewById(R.id.sing_up_butt);

        final EditText name =  findViewById(R.id.sing_name);
        final EditText surname =  findViewById(R.id.sing_surname);
        final EditText nick =  findViewById(R.id.sing_nick);
        final EditText mail =  findViewById(R.id.sing_mail);
        final EditText pass = findViewById(R.id.sing_pass);
        final EditText repass =  findViewById(R.id.sing_repass);
        final CheckBox agree =  findViewById(R.id.sing_agree);

        req.setOnClickListener(v -> {
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
            StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://www.serwer1727017.home.pl/2ti/floda/app/singup.php", response -> {
                AlertDialog.Builder b = new AlertDialog.Builder(Floda_SING_UP.this);
                b.setMessage(response).setTitle(R.string.success).setPositiveButton((R.string.superr), (dialog, which) -> onBackPressed()).setOnKeyListener((dialog, keyCode, event) -> {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        onBackPressed();
                    }
                    return true;
                }).create();
                b.show();
            }, error -> Log.e("singup", "błąd")) {
                @Override
                protected Map<String, String> getParams()  {
                    Map<String, String> d = new HashMap<>();
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
                    data[i].setError(getString(R.string.empty_field));
                    correct = false;
                }
                if(i==3&&isValidEmailId(data[i].getText().toString())){
                    data[i].setError(getString(R.string.incorrect_mail));
                    correct = false;
                }
                if (i == 5 && (!data[i].getText().toString().equals(data[i - 1].getText().toString()))) {
                    data[i].setError(getString(R.string.haslo_takie_same));
                    correct = false;
                } else {
                    if (i == 5)
                        data[i].setError(null);
                }

            }
            if (!agree.isChecked()) {
                correct = false;
                agree.setError(getString(R.string.polityka_pryw));
            } else {
                agree.setError(null);
            }
            if (correct) {

                RequestQueue q = new RequestQueue(new DiskBasedCache(getCacheDir(), 1024 * 1024), new BasicNetwork(new HurlStack()));
                q.start();
                q.add(stringRequest);
            }
        });


    }
    private boolean isValidEmailId(String email){
        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9]))|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
