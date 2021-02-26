package floda.pl.floda.activities;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.widget.Toast;

import floda.pl.floda.R;
import floda.pl.floda.fragments.PlantDBFragment;

public class MenuActivity extends AppCompatActivity {


    FloatingActionButton fab;
    private boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        fab = findViewById(R.id.button_favourite);


        fab.setOnClickListener(v -> {

          }
        );


        //getDetail(idd, new RequestQueue(new DiskBasedCache(getCacheDir(), 1024 * 1024), new BasicNetwork(new HurlStack())));
        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.menu_content_frame, new PlantDBFragment());
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
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }


}
