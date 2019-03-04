package floda.pl.floda3.settings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;

import java.util.Locale;

import floda.pl.floda3.Floda_LOGIN;
import floda.pl.floda3.R;

public class User_settings extends AppCompatActivity {
    Spinner language;
    int vicon[];
    String[] vcode;
    int option_lang = 0;
    TextView ust_password, ust_repassword;
    ImageButton backbutton, acceptbutton;
    boolean refresh=false;
    boolean back_to_login=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);
        language = findViewById(R.id.language_spinner);
        ust_password = findViewById(R.id.ust_password);
        ust_repassword = findViewById(R.id.ust_repassword);
        acceptbutton = findViewById(R.id.acceptbutton);
        language.setAdapter(new LanguageAdapter());
        vicon = getResources().getIntArray(R.array.flag);
        vcode = new String[]{"0", "1", "2"};
        //Toast.makeText(getApplicationContext(), getResources().getConfiguration().locale.getDisplayLanguage(), Toast.LENGTH_LONG).show();

        acceptbutton.setOnClickListener(v -> {
            Toast.makeText(getBaseContext(), "Saving in progress", Toast.LENGTH_SHORT).show();
            Resources res = getBaseContext().getResources();
// Change locale settings in the app.
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            String url = "http://www.serwer1727017.home.pl/2ti/floda/usr_settings.php?";

// Use conf.locale = new Locale(...) if targeting lower versions
            SharedPreferences s = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
            SharedPreferences.Editor e = s.edit();
            url += "ID=" + s.getString("ID", "") + "&";
            switch (option_lang) {
                case 1:
                    conf.locale = new Locale("pl"); // API 17+ only.
                    res.updateConfiguration(conf, dm);
                    e.putString("language", "pl");
                    url += "lang=pl&";
                    refresh=true;
                    break;
                case 2:
                    conf.locale = new Locale("en"); // API 17+ only.
                    res.updateConfiguration(conf, dm);
                    e.putString("language", "en");
                    url += "lang=en&";
                    refresh=true;
                    break;
                case 3:
                    conf.setLocale(new Locale("de"));
                    res.updateConfiguration(conf, dm);
                    e.putString("language", "de");
                    url += "lang=de&";
                    refresh=true;
                    break;

            }
            e.apply();
            if (ust_password.length() > 0) {
                if (ust_password.getText().toString().equals(ust_repassword.getText().toString())) {
                    url += "password=" + ust_repassword.toString() + "&";
                    back_to_login=true;
                } else {
                    Toast.makeText(this, getString(R.string.haslo_takie_same), Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response -> {
                if(back_to_login){
                    Intent i = new Intent(this,Floda_LOGIN.class);
                    startActivity(i);
                }else if(refresh){
                    Intent intent = new Intent();
                    intent.putExtra("re", true);
                    setResult(RESULT_OK, intent);
                    finish();
                }else{
                    finish();
                }
            }, error -> {
            });
            RequestQueue q = new RequestQueue(new DiskBasedCache(getCacheDir(), 1024 * 1024), new BasicNetwork(new HurlStack()));
            q.add(stringRequest);
            q.start();


        });
        language.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                option_lang = position;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        /*switch(getResources().getConfiguration().locale.getDisplayLanguage()){
            case "pl":
                language.setSelection(0);
                break;
            case "en":
                language.setSelection(1);
                break;
            case "de":
                language.setSelection(2);
                break;
        }*/
    }


    private class LanguageAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //View view = convertView;
            convertView = getLayoutInflater().inflate(R.layout.row_zmiana, parent, false);
            ImageView icon = convertView.findViewById(R.id.flag_zmiana);
            TextView names = convertView.findViewById(R.id.text_zmiana);
            switch (position) {
                case 0:
                    //icon.setImageDrawable(getResources().getDrawable(R.drawable.poland));
                    names.setText(getString(R.string.chose_lang));
                    break;
                case 1:
                    icon.setImageDrawable(getResources().getDrawable(R.drawable.poland));
                    names.setText(getString(R.string.polski));
                    break;
                case 2:
                    icon.setImageDrawable(getResources().getDrawable(R.drawable.uk));
                    names.setText(getString(R.string.angielski));
                    break;
                case 3:
                    icon.setImageDrawable(getResources().getDrawable(R.drawable.germany));
                    names.setText(getString(R.string.niemiecki));
                    break;

            }

            return convertView;
        }
    }
}
