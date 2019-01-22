package floda.pl.floda3.add;

import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;

import floda.pl.floda3.R;

public class FLODA_add_new_genre extends AppCompatActivity {
    CheckBox bool_naslonecznienie, bool_temperatura, bool_podlewanie, bool_wilgotnosc, bool_poradnik;
    LinearLayout naslonecznienie, podlewanie, temperatura, wilgotnosc, poradnik;
    RadioButton pod_dni,pod_warn,sun_man,sun_sett;
    EditText pod_il_dni;
    Spinner pod_warnspin,naslonecz_spinner;
    ConstraintLayout con_naslonecznienie;
    Button send_button;
    int activated = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floda_add_new_genre);

        con_naslonecznienie = findViewById(R.id.con_naslonecznienie);
        bool_naslonecznienie = findViewById(R.id.bool_naslonecznienie);
        bool_temperatura = findViewById(R.id.bool_temperatura);
        bool_podlewanie = findViewById(R.id.bool_podlewanie);
        bool_wilgotnosc = findViewById(R.id.bool_wilgotnosc);
        bool_poradnik = findViewById(R.id.bool_poradnik);
        naslonecznienie = findViewById(R.id.naslonecznienie);
        podlewanie = findViewById(R.id.podlewanie);
        temperatura = findViewById(R.id.temperatura);
        naslonecz_spinner = findViewById(R.id.naslonecz_spinner);
        wilgotnosc = findViewById(R.id.wilgotnosc);
        poradnik = findViewById(R.id.poradnik);
        pod_dni = findViewById(R.id.pod_dni);
        send_button=findViewById(R.id.send_button);
        pod_warn = findViewById(R.id.pod_warn);
        sun_man = findViewById(R.id.sun_man);
        sun_sett = findViewById(R.id.sun_sett);
        pod_il_dni = findViewById(R.id.pod_il_dni);
        pod_warnspin = findViewById(R.id.pod_warnspin);
        pod_dni.setOnClickListener(v->{
            pod_il_dni.setVisibility(View.VISIBLE);
            pod_warnspin.setVisibility(View.INVISIBLE);
        });
        pod_warn.setOnClickListener(v->{
            pod_il_dni.setVisibility(View.INVISIBLE);
            pod_warnspin.setVisibility(View.VISIBLE);
        });
        sun_sett.setOnClickListener(v->{
            con_naslonecznienie.setVisibility(View.INVISIBLE);
            naslonecz_spinner.setVisibility(View.VISIBLE);
        });
        sun_man.setOnClickListener(v->{
            con_naslonecznienie.setVisibility(View.VISIBLE);
            naslonecz_spinner.setVisibility(View.INVISIBLE);
        });
        bool_naslonecznienie.setOnClickListener(v -> {
            if (bool_naslonecznienie.isChecked()) {
                naslonecznienie.setBackground(getDrawable(R.drawable.formbackgroundstrong));
                activated++;
            } else {
                naslonecznienie.setBackground(getDrawable(R.drawable.formbackgroundstrongdeactivated));
                activated--;

            }
            check();

        });
        bool_temperatura.setOnClickListener(v -> {
            if (bool_temperatura.isChecked()) {
                temperatura.setBackground(getDrawable(R.drawable.formbackgroundstrong));
                activated++;

            } else {
                temperatura.setBackground(getDrawable(R.drawable.formbackgroundstrongdeactivated));
                activated--;

            }
            check();

        });
        bool_podlewanie.setOnClickListener(v -> {
            if (bool_podlewanie.isChecked()) {
                podlewanie.setBackground(getDrawable(R.drawable.formbackgroundstrong));
                activated++;

            } else {
                podlewanie.setBackground(getDrawable(R.drawable.formbackgroundstrongdeactivated));
                activated--;

            }
            check();

        });
        bool_wilgotnosc.setOnClickListener(v -> {
            if (bool_wilgotnosc.isChecked()) {
                wilgotnosc.setBackground(getDrawable(R.drawable.formbackgroundstrong));
                activated++;

            } else {
                wilgotnosc.setBackground(getDrawable(R.drawable.formbackgroundstrongdeactivated));
                activated--;

            }
            check();
        });
        bool_poradnik.setOnClickListener(v -> {
            if (bool_poradnik.isChecked()) {
                poradnik.setBackground(getDrawable(R.drawable.formbackgroundstrong));
                activated++;

            } else {
                poradnik.setBackground(getDrawable(R.drawable.formbackgroundstrongdeactivated));
                activated--;

            }
            check();
        });

    }
    void check(){
        if(activated>0){
            send_button.setEnabled(true);
            send_button.setBackground(getDrawable(R.drawable.formbackgroundstrong));
        }else{
            send_button.setEnabled(false);
            send_button.setBackground(getDrawable(R.drawable.formbackgroundstrongdeactivated));

        }
    }
}
