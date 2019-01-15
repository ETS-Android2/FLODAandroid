package floda.pl.floda3;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.github.mikephil.charting.charts.CombinedChart;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import floda.pl.floda3.settings.ESPsettings;

public class PlantDetail extends AppCompatActivity {
    String id;
    String ID;
    CombinedChart nawodnienie, naslonecznienie, wilgotnosc, temperatura;
    LineData nawo,temp,wilg,slo;
    ArrayList<BarEntry> datanawodnienie;
    ArrayList<BarEntry> datanaslonecznienie;
    ArrayList<BarEntry> datawilgotnosc;
    ArrayList<BarEntry> datatemperatura;
    TextView title,subtitle;
    TextView ostatnie;
    int tryb;
    ArrayList<Entry> nmax, smax, smin, wmax, wmin, tmax, tmin; //nawodnienie min i max slonce max i min wilgotnosc min i max temperatura max i min
    ArrayList<String> timeof;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_detail);
        Intent i = getIntent();
        ostatnie = findViewById(R.id.nw2);
        tryb=0;
        timeof = new ArrayList<>();
        id = i.getStringExtra("ID");
        Log.e("id", id);
        ID=id;
        Toolbar t = findViewById(R.id.plantdettool);
        setSupportActionBar(t);
         title = findViewById(R.id.detail_title);
         subtitle = findViewById(R.id.detail_subtitle);
        nmax = new ArrayList<>();
        smax = new ArrayList<>();
        smin = new ArrayList<>();
        tmax = new ArrayList<>();
        tmin = new ArrayList<>();
        wmax = new ArrayList<>();
        wmin = new ArrayList<>();
        databaseGet(id, tryb);
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
        switch(id) {
            case R.id.action_ssettings:
                if (tryb == 0) {
                    tryb = 1;
                } else {
                    tryb = 0;
                }
                datanaslonecznienie.clear();
                datanawodnienie.clear();
                datatemperatura.clear();
                datawilgotnosc.clear();
                naslonecznienie.clear();
                wilgotnosc.clear();
                temperatura.clear();
                nawodnienie.clear();
                nmax.clear();
                smax.clear();
                smin.clear();
                wmax.clear();
                wmin.clear();
                tmax.clear();
                tmin.clear();
                databaseGet(ID, tryb);
                break;
            case R.id.espsettings:
                Intent a = new Intent(this, ESPsettings.class);
                a.putExtra("ID",ID);
                startActivity(a);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    void databaseGet(String id, int nr) {
        StringRequest stringRequest;
        datanawodnienie = new ArrayList<>();
        datanaslonecznienie = new ArrayList<>();
        datatemperatura = new ArrayList<>();
        datawilgotnosc = new ArrayList<>();
        String sql = "http://serwer1727017.home.pl/2ti/floda/detail/data.php";
        if (nr == 0) {
            stringRequest = new StringRequest(Request.Method.POST, sql, response -> {
                try {

                    JSONArray podst = new JSONArray(response);
                    JSONObject det = podst.getJSONObject(0);

                    JSONObject foo;
                    title.setText(det.getString("name"));
                    subtitle.setText(" (" + det.getString("Nazwa") + ")");

                    for (int index = 1; index < podst.length(); index++) {

                        foo = podst.getJSONObject(index);
                        Log.e("Data", id + " " + foo.getString("sun") + " " + foo.getString("temperature") + " " + foo.getString("humidity"));
                        datanawodnienie.add(new BarEntry(index - 1, Integer.valueOf(foo.getString("soil"))));
                        datanaslonecznienie.add(new BarEntry(index - 1, Integer.valueOf(foo.getString("sun"))));
                        datatemperatura.add(new BarEntry(index - 1, Integer.valueOf(foo.getString("temperature"))));
                        datawilgotnosc.add(new BarEntry(index - 1, Integer.valueOf(foo.getString("humidity"))));
                        timeof.add(index - 1, foo.getString("time"));

                    }
                    //todo filtr czasu i dodatkowe info
                    //timeof.add(podst.length(), podst.getJSONObject(podst.length()).getString("time"));

                    Log.e("ble", String.valueOf(response));
                    if(det.getInt("a_w_g")>0) {
                        nmax.add(new Entry(0, det.getInt("a_w_g")));
                        nmax.add(new Entry(podst.length() - 2, det.getInt("a_w_g")));
                        ostatnie.setText("");
                    }else{
                        ostatnie.setText("PODLEWANIE");
                    }
                    smax.add(new Entry(0, det.getInt("s_d_s")+40));
                    smax.add(new Entry(podst.length() - 2, det.getInt("s_d_s")+40));
                    smin.add(new Entry(0, det.getInt("s_d_s")-40));
                    smin.add(new Entry(podst.length() - 2, det.getInt("s_d_s")-40));
                    tmax.add(new Entry(0, det.getInt("s_d_t")+3));
                    tmax.add(new Entry(podst.length() - 2, det.getInt("s_d_t")+3));
                    tmin.add(new Entry(0, det.getInt("s_d_t")-3));
                    tmin.add(new Entry(podst.length() - 2, det.getInt("s_d_t")-3));
                    wmax.add(new Entry(0, det.getInt("s_d_w")+10));
                    wmax.add(new Entry(podst.length() - 2, det.getInt("s_d_w")+10));
                    wmin.add(new Entry(0, det.getInt("s_d_w")-10));
                    wmin.add(new Entry(podst.length() - 2, det.getInt("s_d_w")-10));


                    //TODO: Nalezy tutaj dodac funkcje zczytujaca na poczatek podstawowe dane a nastepnie poszczegolne na godzine

                } catch (Exception e) {
                    Log.e("Ustawianie danych", "eeeeeee blad");
                }
                nawodnienie();
                naslonecznienie();
                wilgotnosc();
                temperatura();

            }, error -> {

            }) {
                @Override
                protected Map<String, String> getParams(){
                    Map<String, String> parms = new HashMap<>();
                    parms.put("id", id);
                    parms.put("nr", String.valueOf(nr));
                    return parms;
                }
            };
        } else {
            stringRequest = new StringRequest(Request.Method.POST, sql, response -> {
                try {
                    datanawodnienie = new ArrayList<>();
                    datanaslonecznienie = new ArrayList<>();
                    datatemperatura = new ArrayList<>();
                    datawilgotnosc = new ArrayList<>();
                    JSONArray podst = new JSONArray(response);
                    JSONObject det = podst.getJSONObject(0);
                    JSONObject foo;
                    title.setText(det.getString("name"));
                    subtitle.setText(" (" + det.getString("Nazwa") + ")");
                    for (int index = 1; index < podst.length(); index++) {
                        foo = podst.getJSONObject(index);
                        Log.e("Data", id + " " + foo.getString("sun") + " " + foo.getString("temperature") + " " + foo.getString("humidity"));
                        datanawodnienie.add(new BarEntry(index - 1, Integer.valueOf(foo.getString("soil"))));
                        datanaslonecznienie.add(new BarEntry(index - 1, Integer.valueOf(foo.getString("sun"))));
                        datatemperatura.add(new BarEntry(index - 1, Integer.valueOf(foo.getString("temperature"))));
                        datawilgotnosc.add(new BarEntry(index - 1, Integer.valueOf(foo.getString("humidity"))));
                        timeof.add(index - 1, foo.getString("time"));

                    }


                    if(det.getInt("a_w_g")>0) {
                        nmax.add(new Entry(0, det.getInt("a_w_g")));
                        nmax.add(new Entry(podst.length() - 2, det.getInt("a_w_g")));
                        ostatnie.setText("");

                    }else{
                        ostatnie.setText("PODLEWANIE");
                    }
                    smax.add(new Entry(0, det.getInt("s_d_s")+40));
                    smax.add(new Entry(podst.length() - 2, det.getInt("s_d_s")+40));
                    smin.add(new Entry(0, det.getInt("s_d_s")-40));
                    smin.add(new Entry(podst.length() - 2, det.getInt("s_d_s")-40));
                    tmax.add(new Entry(0, det.getInt("s_d_t")+3));
                    tmax.add(new Entry(podst.length() - 2, det.getInt("s_d_t")+3));
                    tmin.add(new Entry(0, det.getInt("s_d_t")-3));
                    tmin.add(new Entry(podst.length() - 2, det.getInt("s_d_t")-3));
                    wmax.add(new Entry(0, det.getInt("s_d_w")+10));
                    wmax.add(new Entry(podst.length() - 2, det.getInt("s_d_w")+10));
                    wmin.add(new Entry(0, det.getInt("s_d_w")-10));
                    wmin.add(new Entry(podst.length() - 2, det.getInt("s_d_w")-10));

                    Log.e("response", response);
                } catch (Exception e) {
                    Log.e("Ustawianie danych", String.valueOf(e) + response);
                }
                nawodnienie();
                naslonecznienie();
                wilgotnosc();
                temperatura();
            }, error -> {

            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> parms = new HashMap<>();
                    parms.put("id", ID);
                    parms.put("nr", String.valueOf(nr));
                    return parms;
                }
            };
        }
        RequestQueue q = new RequestQueue(new DiskBasedCache(getCacheDir(), 1024 * 1024), new BasicNetwork(new HurlStack()));
        q.add(stringRequest);
        q.start();

    }


    BarData setNawodnienie() { /* woda */
        nawo = new LineData();
        LineDataSet set = new LineDataSet(nmax, "Brak wody");
        set.setCircleRadius(1f);
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

        // barData.setBarWidth(barWidth);
        return new BarData(d);

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
        xAxis.setValueFormatter((value, axis) -> timeof.get((int) value));
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

    BarData setNaslonecznienie() {
        slo = new LineData();
        LineDataSet max = new LineDataSet(smax, "Max. naslonecznienie");
        LineDataSet min = new LineDataSet(smin, "Min. naslonecznienie");
        max.setCircleRadius(1f);
        max.setLineWidth(2.5f);
        max.setColor(Color.YELLOW);
        max.setFillColor(Color.WHITE);
        max.setDrawValues(false);
        slo.addDataSet(max);
        min.setCircleRadius(.1f);
        min.setLineWidth(2.5f);
        min.setColor(Color.RED);
        min.setFillColor(Color.WHITE);
        min.setDrawValues(false);
        slo.addDataSet(min);
        BarDataSet d = new BarDataSet(datanaslonecznienie, "lux");
        d.setColor(Color.BLUE);
        d.setBarBorderWidth(1f);
        d.setBarBorderColor(Color.WHITE);
        d.setAxisDependency(YAxis.AxisDependency.LEFT);
        d.setValueTextColor(Color.WHITE);

        // barData.setBarWidth(barWidth);
        return new BarData(d);

    }

    void naslonecznienie() {

        naslonecznienie = findViewById(R.id.naslonecznienie);

        naslonecznienie.setBackgroundColor(Color.TRANSPARENT);

        naslonecznienie.setDrawOrder(new CombinedChart.DrawOrder[]{
                CombinedChart.DrawOrder.BAR, CombinedChart.DrawOrder.LINE,CombinedChart.DrawOrder.LINE
        });
        Legend l = naslonecznienie.getLegend();
        l.setWordWrapEnabled(true);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setTextColor(Color.WHITE);
        l.setDrawInside(false);
        YAxis rightAxis = naslonecznienie.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setTextColor(Color.WHITE);
        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        YAxis leftAxis = naslonecznienie.getAxisLeft();
        leftAxis.setTextColor(Color.WHITE);
        leftAxis.setDrawGridLines(false);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        XAxis xAxis = naslonecznienie.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMinimum(0f);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setGranularity(0.5f);
        xAxis.setLabelRotationAngle(45);
        xAxis.setValueFormatter((value, axis) -> timeof.get((int) value));
        CombinedData data = new CombinedData();
        data.setData(setNaslonecznienie());
        data.setData(slo);
        data.setData(slo);
        //data.setData();
        xAxis.setAxisMaximum(data.getXMax() + 0.25f);
        naslonecznienie.setData(data);
        Description foo = new Description();
        foo.setText("");
        naslonecznienie.setDescription(foo);
        naslonecznienie.invalidate();
    }

    BarData setTemperatura() {
        temp = new LineData();
        LineDataSet max = new LineDataSet(tmax, "Max. temperatura");
        LineDataSet min = new LineDataSet(tmin, "Min. temperatura");
        max.setCircleRadius(1f);
        max.setLineWidth(2.5f);
        max.setColor(Color.RED);
        max.setFillColor(Color.WHITE);
        max.setDrawValues(false);
        temp.addDataSet(max);
        min.setCircleRadius(.1f);
        min.setLineWidth(2.5f);
        min.setColor(Color.RED);
        min.setFillColor(Color.WHITE);
        min.setDrawValues(false);
        temp.addDataSet(min);
        BarDataSet d = new BarDataSet(datatemperatura, "°C");
        d.setColor(Color.BLUE);
        d.setBarBorderWidth(1f);
        d.setBarBorderColor(Color.WHITE);
        d.setAxisDependency(YAxis.AxisDependency.LEFT);
        d.setValueTextColor(Color.WHITE);

        // barData.setBarWidth(barWidth);
        return new BarData(d);

    }

    void temperatura() {

        temperatura = findViewById(R.id.temperatura);

        temperatura.setBackgroundColor(Color.TRANSPARENT);

        temperatura.setDrawOrder(new CombinedChart.DrawOrder[]{
                CombinedChart.DrawOrder.BAR, CombinedChart.DrawOrder.LINE,CombinedChart.DrawOrder.LINE
        });
        Legend l = temperatura.getLegend();
        l.setWordWrapEnabled(true);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setTextColor(Color.WHITE);
        l.setDrawInside(false);
        YAxis rightAxis = temperatura.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setTextColor(Color.WHITE);
        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        YAxis leftAxis = temperatura.getAxisLeft();
        leftAxis.setTextColor(Color.WHITE);
        leftAxis.setDrawGridLines(false);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        XAxis xAxis = temperatura.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMinimum(0f);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setGranularity(0.5f);
        xAxis.setLabelRotationAngle(45);
        xAxis.setValueFormatter((value, axis) -> timeof.get((int) value));
        CombinedData data = new CombinedData();
        data.setData(setTemperatura());
        data.setData(temp);
        //data.setData();
        xAxis.setAxisMaximum(data.getXMax() + 0.25f);
        temperatura.setData(data);
        Description foo = new Description();
        foo.setText("");
        temperatura.setDescription(foo);
        temperatura.invalidate();
    }

    BarData setWilgotnosc() {
        wilg = new LineData();

        LineDataSet max = new LineDataSet(wmax, "Max. wilgotnosc");
        LineDataSet min = new LineDataSet(wmin, "Min. wilgotnosc");
        max.setCircleRadius(1f);
        max.setLineWidth(2.5f);
        max.setColor(Color.RED);
        max.setFillColor(Color.WHITE);
        max.setDrawValues(false);
        wilg.addDataSet(max);
        min.setCircleRadius(.1f);
        min.setLineWidth(2.5f);
        min.setColor(Color.RED);
        min.setFillColor(Color.WHITE);
        min.setDrawValues(false);
        wilg.addDataSet(min);
        BarDataSet d = new BarDataSet(datawilgotnosc, "%");
        d.setColor(Color.BLUE);
        d.setBarBorderWidth(1f);
        d.setBarBorderColor(Color.WHITE);
        d.setAxisDependency(YAxis.AxisDependency.LEFT);
        d.setValueTextColor(Color.WHITE);

        // barData.setBarWidth(barWidth);
        return new BarData(d);

    }

    void wilgotnosc() {

        wilgotnosc = findViewById(R.id.wilgotnosc);

        wilgotnosc.setBackgroundColor(Color.TRANSPARENT);

        wilgotnosc.setDrawOrder(new CombinedChart.DrawOrder[]{
                CombinedChart.DrawOrder.BAR, CombinedChart.DrawOrder.LINE,CombinedChart.DrawOrder.LINE
        });
        Legend l = wilgotnosc.getLegend();
        l.setWordWrapEnabled(true);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setTextColor(Color.WHITE);
        l.setDrawInside(false);
        YAxis rightAxis = wilgotnosc.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setTextColor(Color.WHITE);
        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        YAxis leftAxis = wilgotnosc.getAxisLeft();
        leftAxis.setTextColor(Color.WHITE);
        leftAxis.setDrawGridLines(false);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        XAxis xAxis = wilgotnosc.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMinimum(0f);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setGranularity(0.5f);
        xAxis.setLabelRotationAngle(45);
        xAxis.setValueFormatter((value, axis) -> timeof.get((int) value));
        CombinedData data = new CombinedData();
        data.setData(setWilgotnosc());
        data.setData(wilg);
        //data.setData();
        xAxis.setAxisMaximum(data.getXMax() + 0.25f);
        wilgotnosc.setData(data);
        Description foo = new Description();
        foo.setText("");
        wilgotnosc.setDescription(foo);
        wilgotnosc.invalidate();
    }
}
