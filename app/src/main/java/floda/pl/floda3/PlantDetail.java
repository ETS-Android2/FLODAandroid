package floda.pl.floda3;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.ArrayMap;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import org.json.JSONArray;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PlantDetail extends AppCompatActivity {
    String id;
    CombinedChart nawodnienie;
    LineData line;
    ArrayList<Integer> datanawodnienie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_detail);
        Intent i = getIntent();
        id = i.getStringExtra("ID");
        Toolbar t = findViewById(R.id.plantdettool);
        setSupportActionBar(t);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.detailbar);
        TextView z = (TextView) t.findViewById(R.id.detailtitle);
        z.setText("cos");
        Log.e("c", Calendar.getInstance().getTime().toString());
        nawodnienie = findViewById(R.id.nawodnienie);

        nawodnienie.setBackgroundColor(Color.TRANSPARENT);

        nawodnienie.setDrawOrder(new CombinedChart.DrawOrder[]{
                CombinedChart.DrawOrder.BAR, CombinedChart.DrawOrder.LINE
        });
        nawodnienie();

    }

    void databaseGet(String id) {
        datanawodnienie = new ArrayList<>();
        String sql = "";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, sql, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONArray podst = new JSONArray(response);
                    //TODO: Nalezy tutaj dodac funkcje zczytujaca na poczatek podstawowe dane a nastepnie poszczegolne na godzine
                }catch (Exception e){
                    Log.e("e", String.valueOf(e));
                }
            }}, new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parms = new HashMap<>();
                parms.put("ID",id);
                return parms;
            }
        };
    }

    BarData setNawodnienie() {
        line = new LineData();
        ArrayList<BarEntry> info = new ArrayList<>();
        ArrayList<Entry> max = new ArrayList<>();
        for (int index = 0; index < 20; index++) {
            info.add(new BarEntry(index, index + 5));

        }
        max.add(new Entry(0, 20));
        max.add(new Entry(19, 20));
        LineDataSet set = new LineDataSet(max, "Brak wody");
        set.setCircleRadius(.1f);
        set.setLineWidth(2.5f);
        set.setColor(Color.RED);
        set.setFillColor(Color.WHITE);
        set.setDrawValues(false);
        line.addDataSet(set);

        BarDataSet d = new BarDataSet(info, "pkt");
        d.setColor(Color.BLUE);
        d.setBarBorderWidth(1f);
        d.setBarBorderColor(Color.WHITE);
        d.setAxisDependency(YAxis.AxisDependency.LEFT);
        d.setValueTextColor(Color.WHITE);
        float barSpace = 0.02f; // x2 dataset
        float barWidth = 0.45f; // x2 dataset
        BarData barData = new BarData(d);
        // barData.setBarWidth(barWidth);
        return barData;

    }

    void nawodnienie() {
        Legend l = nawodnienie.getLegend();
        l.setWordWrapEnabled(true);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setTextColor(Color.WHITE);
        l.setDrawInside(false);
        YAxis rightAxis = nawodnienie.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setTextColor(Color.WHITE);
        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        YAxis leftAxis = nawodnienie.getAxisLeft();
        leftAxis.setTextColor(Color.WHITE);
        leftAxis.setDrawGridLines(false);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        XAxis xAxis = nawodnienie.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
        xAxis.setAxisMinimum(0f);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setGranularity(0.5f);
        xAxis.setLabelRotationAngle(45);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return "yep";
            }
        });
        CombinedData data = new CombinedData();
        data.setData(setNawodnienie());
        data.setData(line);
        //data.setData();
        xAxis.setAxisMaximum(data.getXMax() + 0.25f);
        nawodnienie.setData(data);
        nawodnienie.invalidate();
    }
}
