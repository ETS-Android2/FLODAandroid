package floda.pl.floda.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;

import java.util.Objects;

import floda.pl.floda.R;
import floda.pl.floda.dto.UserDto;

public class MenuActivity extends AppCompatActivity{
    UserDto usr;
    String idd;
    FloatingActionButton fab;
    private boolean doubleBackToExitPressedOnce = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.appbar);
        fab = findViewById(R.id.fab);
        RequestQueue re = new RequestQueue(new DiskBasedCache(getCacheDir(), 1024 * 1024), new BasicNetwork(new HurlStack()));
        String urlr = "http://serwer1727017.home.pl/2ti/floda/floda_list.php?ID=" + idd;



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


        //getDetail(idd, new RequestQueue(new DiskBasedCache(getCacheDir(), 1024 * 1024), new BasicNetwork(new HurlStack())));
        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.content_fram, new PlantDBActivity());
        t.commit();


    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.finishAffinity();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }



    /*@Override
    protected void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
        Toast.makeText(this,"Reloading...",Toast.LENGTH_SHORT).show();
    }*/
}
