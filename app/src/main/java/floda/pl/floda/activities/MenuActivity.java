package floda.pl.floda.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

import floda.pl.floda.fragments.ForumFragment;
import floda.pl.floda.fragments.MemberListFragment;
import floda.pl.floda.fragments.FLODASetUpFragment;
import floda.pl.floda.R;
import floda.pl.floda.dto.UserDto;
import floda.pl.floda.fragments.FAQFragment;

public class MenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    UserDto usr;
    String idd;
    FloatingActionButton fab;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floda_main);
        Intent intent = getIntent();
        idd = intent.getStringExtra("ID");
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.appbar);
        fab = findViewById(R.id.fab);
        RequestQueue re = new RequestQueue(new DiskBasedCache(getCacheDir(), 1024 * 1024), new BasicNetwork(new HurlStack()));
        String urlr = "http://serwer1727017.home.pl/2ti/floda/floda_list.php?ID=" + idd;
        AtomicBoolean raised_not = new AtomicBoolean(false);
        StringRequest notifi = new StringRequest(Request.Method.GET, urlr, response -> {
            if (!Objects.equals(response, "0")&&!raised_not.get()) {
                raised_not.set(true);
            } else {
                raised_not.set(false);
            }
        }, error -> {
        });
        Thread notif = new Thread() {

            @Override
            public void run() {
                try {
                    while (true) {
                        super.run();
                        re.stop();
                        re.add(notifi);
                        re.start();
                        sleep(10000000);
                    }
                } catch (Exception e) {
                    Log.e("e", e.toString());
                }
            }
        };
        notif.start();
        fab.setOnClickListener(
                v -> {
                    AlertDialog alertDialog;
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    LayoutInflater l = (LayoutInflater) getApplicationContext().getSystemService(getBaseContext().LAYOUT_INFLATER_SERVICE);
                    View dialog = l.inflate(R.layout.addchose, null);
                    Button sonda = dialog.findViewById(R.id.sonda);
                    Button gatunek = dialog.findViewById(R.id.gatunek);
                    builder.setView(dialog);
                    alertDialog = builder.create();
                    alertDialog.show();
                    sonda.setOnClickListener(v1 -> {
                        Intent i = new Intent(this.getBaseContext(), NewPlantActivity.class);
                        i.putExtra("ID", idd);
                        alertDialog.hide();
                        startActivity(i);
                    });
                    gatunek.setOnClickListener(v1 -> {
                        Intent i = new Intent(this.getBaseContext(), NewGenreActivity.class);
                        i.putExtra("ID", idd);
                        alertDialog.hide();
                        startActivity(i);
                    });

                }
        );

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        getDetail(idd, new RequestQueue(new DiskBasedCache(getCacheDir(), 1024 * 1024), new BasicNetwork(new HurlStack())));
        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.content_fram, new PlantDBActivity());
        t.commit();


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            drawer.openDrawer(GravityCompat.START);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.floda_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent n = new Intent(this, UserSettingsActivity.class);
            startActivityForResult(n, 2);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                if (data.getBooleanExtra("re", true)) {
                    finish();
                    startActivity(getIntent());
                    Toast.makeText(this, "Reloading...", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        switch (id) {
            case R.id.p_list:
                t.replace(R.id.content_fram, new PlantDBActivity());
                fab.show();
                break;
            case R.id.log_out:
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor e = preferences.edit();
                e.remove("ID");
                e.apply();
                Intent i = new Intent(this, LoginActivity.class);
                startActivity(i);
                break;
            case R.id.faq:
                t.replace(R.id.content_fram, new FAQFragment());
                fab.hide();
                break;
            case R.id.pedia:
                Intent j = new Intent(this, ListGenreActivity.class);
                j.putExtra("NR", "0");
                startActivity(j);
                break;
            case R.id.forum:
                t.replace(R.id.content_fram, new ForumFragment());
                fab.hide();
                break;
            case R.id.users:
                t.replace(R.id.content_fram, new MemberListFragment());
                fab.hide();
                break;
            case R.id.esp_add:
                t.replace(R.id.content_fram, new FLODASetUpFragment());
                fab.hide();
                break;

        }
        t.commit();
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void getDetail(String id, RequestQueue q) {
        String sql = "http://www.serwer1727017.home.pl/2ti/floda/floda_ifo.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, sql, response -> {
            try {
                JSONArray j = new JSONArray(response);
                JSONObject o = j.getJSONObject(0);
                NavigationView navigationView = findViewById(R.id.nav_view);
                View header = navigationView.getHeaderView(0);
                usr = new UserDto(o.getString("Name"), o.getString("email"), o.getString("ID"), o.getString("nick"), o.getString("Surname"), Boolean.getBoolean(o.getString("su") == "0" ? "false" : "true"));
                Log.e("cos", usr.getMail());
                TextView t = (TextView) header.findViewById(R.id.nav_name);
                TextView e = (TextView) header.findViewById(R.id.nav_email);
                t.setText(o.getString("Name") + " " + o.getString("Surname") + " (" + o.getString("nick") + ")");
                e.setText(o.getString("email"));
                Log.e("id", o.getString("email"));
                if (o.getString("su").contentEquals("1")) {
                    t.setTextColor(getResources().getColor(R.color.red));
                }
            } catch (JSONException e) {
                Log.e("json", e.toString());
                e.printStackTrace();
            }


        }, error -> {
            Log.e("blad", error.toString());
            //foo[0] =null;
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> h = new HashMap<>();
                h.put("ID", idd);

                return h;
            }
        };
        q.add(stringRequest);
        q.start();
        Log.e("r", id);

    }

    /*@Override
    protected void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
        Toast.makeText(this,"Reloading...",Toast.LENGTH_SHORT).show();
    }*/
}
