package floda.pl.floda3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import com.github.mikephil.charting.charts.LineChart;

public class PlantDetail extends AppCompatActivity {
    String id;
    LineChart nawodnienie;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_detail);
        Intent i = getIntent();
        id= i.getStringExtra("ID");
        Toolbar t = findViewById(R.id.plantdettool);
        setSupportActionBar(t);

        nawodnienie= (LineChart) findViewById(R.id.nawodnienie);
    }
}
