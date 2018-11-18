package floda.pl.floda3;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class Starting extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String id = preferences.getString("ID","0");
        Log.e("Id",id);
        Thread welcomeThread = new Thread() {

            @Override
            public void run() {
                try {
                    super.run();
                    sleep(2000);  //Delay of 2 seconds
                } catch (Exception e) {

                } finally {

                    if(!id.equals("0")){
                        Intent i = new Intent(getBaseContext(),Floda_main.class);
                        i.putExtra("ID",id);

                        startActivity(i);
                    }else{
                        Intent i = new Intent(getBaseContext(),Floda_LOGIN.class);
                        //i.putExtra("ID",id);

                        startActivity(i);
                    }
                    finish();
                }
            }
        };
        welcomeThread.start();
    }
}
