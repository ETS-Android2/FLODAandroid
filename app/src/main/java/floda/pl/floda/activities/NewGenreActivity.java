package floda.pl.floda.activities;

import android.content.Intent;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;

import java.util.Objects;

import floda.pl.floda.R;

public class NewGenreActivity extends AppCompatActivity {
    CheckBox bool_naslonecznienie, bool_temperatura, bool_podlewanie, bool_wilgotnosc, bool_poradnik;
    LinearLayout naslonecznienie, podlewanie, temperatura, wilgotnosc, poradnik;
    RadioButton pod_dni, pod_warn, sun_man, sun_sett;
    EditText pod_il_dni, sun_min, sun_max, min_temp, max_temp, min_wilg, max_wilg, link_poradnik, genre_name;
    Spinner pod_warnspin, naslonecz_spinner;
    ConstraintLayout con_naslonecznienie;
    Button send_button;
    ImageButton back_arrow_add;
    int activated = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floda_add_new_genre);
        sun_min = findViewById(R.id.sun_min);
        sun_max = findViewById(R.id.sun_max);
        min_temp = findViewById(R.id.min_temp);
        max_temp = findViewById(R.id.max_temp);
        min_wilg = findViewById(R.id.min_wilg);
        max_wilg = findViewById(R.id.max_wilg);

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
        genre_name = findViewById(R.id.genre_name);
        wilgotnosc = findViewById(R.id.wilgotnosc);
        poradnik = findViewById(R.id.poradnik);
        pod_dni = findViewById(R.id.pod_dni);
        send_button = findViewById(R.id.send_button);
        pod_warn = findViewById(R.id.pod_warn);
        sun_man = findViewById(R.id.sun_man);
        sun_sett = findViewById(R.id.sun_sett);
        pod_il_dni = findViewById(R.id.pod_il_dni);
        pod_warnspin = findViewById(R.id.pod_warnspin);
        link_poradnik = findViewById(R.id.link_poradnik);
        back_arrow_add = findViewById(R.id.back_arrow_add);
        back_arrow_add.setOnClickListener(v -> {
            super.onBackPressed();
        });
        pod_dni.setOnClickListener(v -> {
            pod_il_dni.setVisibility(View.VISIBLE);
            pod_warnspin.setVisibility(View.INVISIBLE);
        });
        pod_warn.setOnClickListener(v -> {
            pod_il_dni.setVisibility(View.INVISIBLE);
            pod_warnspin.setVisibility(View.VISIBLE);
        });
        sun_sett.setOnClickListener(v -> {
            con_naslonecznienie.setVisibility(View.INVISIBLE);
            naslonecz_spinner.setVisibility(View.VISIBLE);
        });
        sun_man.setOnClickListener(v -> {
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
        send_button.setOnClickListener(v -> {
            String sql = "http://serwer1727017.home.pl/2ti/floda/add/add_genre.php?";
            if (bool_podlewanie.isChecked()) {
                if (pod_dni.isChecked()) {
                    if (pod_il_dni.getText().toString().length() > 0) {
                        sql += "podlewanie=" + pod_il_dni.getText().toString() + "&";
                    } else {
                        Toast.makeText(this, getString(R.string.add_new_gen_day_empty), Toast.LENGTH_LONG).show();
                        return;
                    }
                } else {
                    if (pod_warn.isChecked()) {
                        if (pod_warnspin.getSelectedItemPosition() > 2) {
                            sql += "podlewanieZ=w" + (pod_warnspin.getSelectedItemPosition() - 1) + "&";
                        } else {
                            Toast.makeText(this, getString(R.string.add_new_gen_list_water_error), Toast.LENGTH_LONG).show();
                            return;
                        }
                    } else {
                        Toast.makeText(this, getString(R.string.add_new_gen_water_error), Toast.LENGTH_LONG).show();
                        return;
                    }
                }
            }
            if (bool_naslonecznienie.isChecked()) {
                if (sun_man.isChecked()) {
                    if (Integer.valueOf(sun_min.getText().toString()) < Integer.valueOf(sun_max.getText().toString()) && !Objects.equals(sun_min.getText().toString(), "") && !Objects.equals(sun_max.getText().toString(), "")) {
                        sql += "naslonecznieniemin=" + sun_min.getText().toString() + "&naslonecznieniemax=" + sun_max.getText().toString() + "&";
                    } else {
                        Toast.makeText(this, getString(R.string.add_new_gen_sun_error), Toast.LENGTH_LONG).show();
                        return;
                    }
                } else {
                    if (sun_sett.isChecked()) {
                        if (naslonecz_spinner.getSelectedItemPosition() > 0) {
                            sql += "naslonecznienieZ=n" + (naslonecz_spinner.getSelectedItemPosition()) + "&";
                        } else {
                            Toast.makeText(this, getString(R.string.add_new_gen_list_error), Toast.LENGTH_LONG).show();
                            return;
                        }
                    } else {
                        Toast.makeText(this, getString(R.string.add_new_gen_rules_err), Toast.LENGTH_LONG).show();
                        return;
                    }
                }
            }
            if (bool_temperatura.isChecked()) {
                if (Integer.valueOf(min_temp.getText().toString()) < Integer.valueOf(max_temp.getText().toString()) && !Objects.equals(min_temp.getText().toString(), "") && !Objects.equals(max_temp.getText().toString(), "")) {
                    sql += "tempmin=" + min_temp.getText().toString() + "&tempmax=" + max_temp.getText().toString() + "&";
                } else {
                    Toast.makeText(this, getString(R.string.add_new_gen_temp_error), Toast.LENGTH_LONG).show();
                    return;
                }
            }
            if (bool_wilgotnosc.isChecked()) {

                if (Integer.valueOf(min_wilg.getText().toString()) < Integer.valueOf(max_wilg.getText().toString()) && !Objects.equals(min_wilg.getText().toString(), "") && !Objects.equals(max_wilg.getText().toString(), "")) {
                    sql += "wilgmin=" + min_wilg.getText().toString() + "&wilgmax=" + max_wilg.getText().toString() + "&";
                } else {
                    Toast.makeText(this, getString(R.string.add_new_gen_wilg_err), Toast.LENGTH_LONG).show();
                    return;
                }
            }
            if (bool_poradnik.isChecked()) {
                if (link_poradnik.getText().toString().contains("http://") && link_poradnik.getText().toString().length() > 7) {
                    sql += "www=" + link_poradnik.getText().toString() + "&";
                } else {
                    Toast.makeText(this, getString(R.string.add_new_gen_del_problem), Toast.LENGTH_LONG).show();
                    return;
                }
            }
            if (genre_name.getText().length() > 0) {
                genre_name.setText(genre_name.getText().toString().replaceAll("[0-9]", ""));
                Intent i = getIntent();
                sql += "name=" + genre_name.getText().toString() + "&autor=" + i.getStringExtra("ID") ;

            } else {

                Toast.makeText(this, getString(R.string.add_new_gen_pusta), Toast.LENGTH_LONG).show();
                return;
            }
            StringRequest stringRequest = new StringRequest(Request.Method.GET, sql, response -> {
                if (response.contains("z")) {
                    Intent i = getIntent();
                    response = response.replace("z", "");
                    i.putExtra("ID", response);
                    setResult(RESULT_OK, i);
                    Toast.makeText(this, getString(R.string.add_new_gen_ok), Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(this, response, Toast.LENGTH_LONG).show();
                }
            }, error -> {
                Log.e("error", error.toString());
            });
            RequestQueue q = new RequestQueue(new DiskBasedCache(getCacheDir(), 1024 * 1024), new BasicNetwork(new HurlStack()));
            q.add(stringRequest);
            q.start();

        });
    }

    void check() {
        if (activated > 0) {
            send_button.setEnabled(true);
            send_button.setBackground(getDrawable(R.drawable.formbackgroundstrong));
        } else {
            send_button.setEnabled(false);
            send_button.setBackground(getDrawable(R.drawable.formbackgroundstrongdeactivated));

        }
    }

}
