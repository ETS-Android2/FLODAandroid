package floda.pl.floda.activities;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Locale;

import floda.pl.floda.R;
import floda.pl.floda.dao.GetBasicIdUserDataByCredentialsDAO;

public class LoginActivity extends AppCompatActivity {
    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floda__login);
        final FloatingActionButton buttonSign = findViewById(R.id.registerButtonLoginActivity);
        final Button buttonLogin = findViewById(R.id.loginButtonLoginActivity);
        final EditText editTextLoginLoginActivity = findViewById(R.id.editTextLoginLoginActivity);
        final EditText editTextPasswordLoginActivity = findViewById(R.id.editTextPasswordLoginActivity);
        final TextView textViewLoginFailedLoginActivity = findViewById(R.id.textViewLoginFailedLoginActivity);
        textViewLoginFailedLoginActivity.setVisibility(View.INVISIBLE);
        buttonLogin.setOnClickListener(v -> {
            GetBasicIdUserDataByCredentialsDAO getBasicIdUserDataByCredentialsDAO = new GetBasicIdUserDataByCredentialsDAO();
            String[] data = getBasicIdUserDataByCredentialsDAO.getBasicIdUserDataByCredentialsDAO(editTextLoginLoginActivity.getText().toString(), editTextPasswordLoginActivity.getText().toString(), new RequestQueue(new DiskBasedCache(getCacheDir(), 1024 * 1024), new BasicNetwork(new HurlStack())));
            if (data[0].equals("0")) {

                textViewLoginFailedLoginActivity.setVisibility(View.VISIBLE);
                editTextLoginLoginActivity.setError("Dane logowania niepoprawne");
                editTextPasswordLoginActivity.setError("Dane logowania niepoprawne");

            } else {
                Intent i = new Intent(getBaseContext(), MenuActivity.class);
                i.putExtra("ID", data[0]);
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("ID", data[0]);
                editor.putString("language", data[1]);
                editor.apply();
                Resources res = getBaseContext().getResources();
                DisplayMetrics dm = res.getDisplayMetrics();
                Configuration conf = res.getConfiguration();
                conf.locale = new Locale(data[1]);
                res.updateConfiguration(conf, dm);
                startActivity(i);
            }
        });

        buttonSign.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), RegistrationActivity.class);
            startActivity(i);
        });


    }
}
