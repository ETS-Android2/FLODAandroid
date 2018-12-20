package floda.pl.floda3;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.ArrayMap;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
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
import org.json.JSONObject;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PlantDetail extends AppCompatActivity {
    String id;
    CombinedChart nawodnienie;
    LineData nawo;
    ArrayList<BarEntry> datanawodnienie;
    TextView z;
    ArrayList<Entry> nmax;
    ArrayList<String> values;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_detail);
        Intent i = getIntent();
         values = new ArrayList<>();
        id = i.getStringExtra("ID");
        Toolbar t = findViewById(R.id.plantdettool);
        setSupportActionBar(t);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.detailbar);
        z = (TextView) t.findViewById(R.id.detailtitle);

        databaseGet(id);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.floda_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_ssettings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    void databaseGet(String id) {
        datanawodnienie = new ArrayList<>();
        String sql = "http://serwer1727017.home.pl/2ti/floda/detail/data.php?id="+id;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, sql, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    nmax = new ArrayList<>();
                    JSONArray podst = new JSONArray(response);
                    JSONObject det = podst.getJSONObject(0);
                    JSONObject foo;
                    z.setText(det.getString("name")+" ("+det.getString("Nazwa")+")");
                    for (int index = 1; index < podst.length(); index++) {
                        foo = podst.getJSONObject(index);
                        datanawodnienie.add(new BarEntry(index, Integer.valueOf(foo.getString("soil"))));
                        values.add(index-1,foo.getString("time"));
                    }
                    values.add(det.length(),"out");
                    nmax.add(new Entry(0,300));
                    nmax.add(new Entry(podst.length()-1,300));
                    nawodnienie();
                    //TODO: Nalezy tutaj dodac funkcje zczytujaca na poczatek podstawowe dane a nastepnie poszczegolne na godzine

                } catch (Exception e) {
                    Log.e("e", String.valueOf(e));
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parms = new HashMap<>();
                parms.put("ID", id);
                return parms;
            }
        };
        RequestQueue q = new RequestQueue(new DiskBasedCache(getCacheDir(), 1024 * 1024), new BasicNetwork(new HurlStack()));
        q.add(stringRequest);
        q.start();
    }


    BarData setNawodnienie() {
        nawo = new LineData();
        LineDataSet set = new LineDataSet(nmax, "Brak wody");
        set.setCircleRadius(.1f);
        set.setLineWidth(2.5f);
        set.setColor(Color.RED);
        set.setFillColor(Color.WHITE);
        set.setDrawValues(false);
        nawo.addDataSet(set);
        BarDataSet d = new BarDataSet(datanawodnienie, "pkt");
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

        nawodnienie = findViewById(R.id.nawodnienie);

        nawodnienie.setBackgroundColor(Color.TRANSPARENT);

        nawodnienie.setDrawOrder(new CombinedChart.DrawOrder[]{
                CombinedChart.DrawOrder.BAR, CombinedChart.DrawOrder.LINE
        });
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
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMinimum(0f);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setGranularity(0.5f);
        xAxis.setLabelRotationAngle(45);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return values.get((int)value);
            }
        });
        CombinedData data = new CombinedData();
        data.setData(setNawodnienie());
        data.setData(nawo);
        //data.setData();
        xAxis.setAxisMaximum(data.getXMax() + 0.25f);
        nawodnienie.setData(data);
        Description foo =new Description();
        foo.setText("");
        nawodnienie.setDescription(foo);
        nawodnienie.invalidate();
    }
}
