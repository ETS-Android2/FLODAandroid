package floda.pl.floda3;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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
import com.warkiz.widget.IndicatorSeekBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PlantDetail extends AppCompatActivity {
    String id;
    String ID;
    CombinedChart nawodnienie, naslonecznienie, wilgotnosc, temperatura;
    LineData nawo, temp, wilg, slo;
    ArrayList<BarEntry> datanawodnienie;
    ArrayList<BarEntry> datanaslonecznienie;
    ArrayList<BarEntry> datawilgotnosc;
    ArrayList<BarEntry> datatemperatura;
    TextView title, subtitle;
    TextView ostatnie;
    int timeperiod;
    String timing = "";
    int tryb = 0;
    Button filtr_akceptuj;
    ArrayList<Entry> nmax, smax, smin, wmax, wmin, tmax, tmin; //nawodnienie min i max slonce max i min wilgotnosc min i max temperatura max i min
    ArrayList<String> timeof;
    String www = null;
    CheckBox filtr_bool;
    EditText od_edit, do_edit;
    String fod = "", fdo = ""; //dane wizualne
    RequestQueue q;
    StringRequest stringRequest;
    boolean filtrch = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_detail);
        Intent i = getIntent();
        ostatnie = findViewById(R.id.nw2);
        timeof = new ArrayList<>();
        id = i.getStringExtra("ID");
        q = new RequestQueue(new DiskBasedCache(getCacheDir(), 1024 * 1024), new BasicNetwork(new HurlStack()));
        Log.e("id", id);
        ID = id;
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

    void reset() {
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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_ssettings:
                if (tryb == 0) {
                    tryb = 1;
                } else {
                    tryb = 0;
                }
                reset();
                databaseGet(ID, tryb);
                break;
            case R.id.espsettings:
                LayoutInflater l4 = (LayoutInflater) getApplicationContext().getSystemService(getBaseContext().LAYOUT_INFLATER_SERVICE);
                View v4 = l4.inflate(R.layout.espsettings, null);
                AlertDialog alertDialog4;
                AlertDialog.Builder builder4 = new AlertDialog.Builder(this);
                builder4.setView(v4);
                TextView haslo = v4.findViewById(R.id.edithaslo);
                IndicatorSeekBar czas = v4.findViewById(R.id.indicatorSeekBar2);
                czas.setMax(1440);
                czas.setMin(10);
                czas.setProgress(0);
                czas.setMax(1440);
                czas.setMin(12);
                float timef = timeperiod;
                czas.setProgress(timef);
                Button akc = v4.findViewById(R.id.editakceptuj);
                Button back = v4.findViewById(R.id.editanuluj);
                alertDialog4 = builder4.create();

                akc.setOnClickListener(v -> {
                    String url = "http://serwer1727017.home.pl/2ti/floda/detail/espsettings.php?ID="+ID+"&";
                    if (czas.getProgress() != timef) {
                        url += "time=" + czas.getProgress()*60000 + "&";
                    }
                    if (!haslo.getText().toString().equals("")) {
                        url += "pass=" + haslo.getText().toString();
                    }
                    //blok tworzacy okno request
                    AlertDialog alertDialog5;
                    AlertDialog.Builder builder5 = new AlertDialog.Builder(this);
                    LayoutInflater l5 = (LayoutInflater) getApplicationContext().getSystemService(getBaseContext().LAYOUT_INFLATER_SERVICE);
                    View v5 = l5.inflate(R.layout.loading, null);
                    builder5.setView(v5);
                    alertDialog5 = builder5.create();
                    alertDialog5.show();
                    //---------------------
                    StringRequest stringRequest2 = new StringRequest(Request.Method.GET, url, response -> {
                        if(czas.getProgress()!=timef) Toast.makeText(getBaseContext(),"Nowy czas bedzie działał po przeładowaniu SONDY",Toast.LENGTH_LONG).show();
                        alertDialog5.hide();
                        alertDialog4.hide();
                    }, error -> {
                    });
                    q.add(stringRequest2);
                    q.start();

                });
                back.setOnClickListener(v -> {
                    alertDialog4.hide();
                });
                alertDialog4.show();
                czas.setProgress(timef);

                break;
            case R.id.poradnik:
                if (www != null && www.contains("http://www.")) {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(www));
                    startActivity(i);
                } else {
                    Toast.makeText(this, getString(R.string.no_guide), Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.espdet:
                LayoutInflater l3 = (LayoutInflater) getApplicationContext().getSystemService(getBaseContext().LAYOUT_INFLATER_SERVICE);
                View v3 = l3.inflate(R.layout.espinfo, null);
                AlertDialog alertDialog3;
                AlertDialog.Builder builder3 = new AlertDialog.Builder(this);
                builder3.setView(v3);
                alertDialog3 = builder3.create();
                alertDialog3.show();
                String url = "http://serwer1727017.home.pl/2ti/floda/detail/espdetail.php?ID=" + ID;

                //blok tworzacy okno request
                AlertDialog alertDialog5;
                AlertDialog.Builder builder5 = new AlertDialog.Builder(this);
                LayoutInflater l5 = (LayoutInflater) getApplicationContext().getSystemService(getBaseContext().LAYOUT_INFLATER_SERVICE);
                View v5 = l5.inflate(R.layout.loading, null);
                builder5.setView(v5);
                alertDialog5 = builder5.create();
                alertDialog5.show();
                //--------------------------

                StringRequest ss = new StringRequest(Request.Method.GET, url, response -> {
                    try {
                        JSONObject o = new JSONObject(response);
                        TextView ide, ipe, dele;
                        ide = v3.findViewById(R.id.sndid);
                        ipe = v3.findViewById(R.id.sndip);
                        dele = v3.findViewById(R.id.sndper);
                        Button next = v3.findViewById(R.id.sndok);
                        next.setOnClickListener(v -> {
                            alertDialog3.hide();
                        });
                        ide.setText(o.getString("id"));
                        ipe.setText(o.getString("ip"));
                        dele.setText("Every " + o.getInt("del") / 1000 + " seconds");
                        alertDialog5.hide();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
                });
                q.add(ss);
                q.start();

                break;
            case R.id.det_time:
                AlertDialog alertDialog;
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                LayoutInflater l = (LayoutInflater) getApplicationContext().getSystemService(getBaseContext().LAYOUT_INFLATER_SERVICE);
                View dialog = l.inflate(R.layout.det_time_layout, null);
                builder.setView(dialog);
                alertDialog = builder.create();
                alertDialog.show();

                Button anuluj = dialog.findViewById(R.id.fi_an);
                do_edit = dialog.findViewById(R.id.do_edit);
                od_edit = dialog.findViewById(R.id.od_edit);
                filtr_akceptuj = dialog.findViewById(R.id.filtr_akceptuj);
                filtr_bool = dialog.findViewById(R.id.filtr_bool);
                filtr_bool.setChecked(filtrch);
                do_edit.setText(fdo);
                od_edit.setText(fod);
                filtr_akceptuj.setOnClickListener(v -> {
                    if (!Objects.equals(do_edit.getText().toString(), "") && !Objects.equals(do_edit.getText().toString(), "")) {
                        if (filtr_bool.isChecked()) {
                            timing = "?od=" + od_edit.getText().toString() + "&do=" + do_edit.getText().toString();
                            Log.e("no", timing);
                            fod = od_edit.getText().toString();
                            fdo = do_edit.getText().toString();
                            filtrch = true;
                            reset();
                            databaseGet(ID, tryb);
                            alertDialog.hide();
                        } else {
                            timing = "";
                            fod = "";
                            fdo = "";
                            filtrch = false;
                            reset();
                            databaseGet(ID, tryb);
                            alertDialog.hide();
                        }
                    } else {
                        Toast.makeText(this, getString(R.string.pole_zakres), Toast.LENGTH_LONG).show();
                    }
                });
                do_edit.setOnClickListener(v -> {
                    AlertDialog alertDialog2;
                    AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
                    LayoutInflater l2 = (LayoutInflater) getApplicationContext().getSystemService(getBaseContext().LAYOUT_INFLATER_SERVICE);
                    View dialog2 = l2.inflate(R.layout.calendar_view, null);
                    builder2.setView(dialog2);
                    alertDialog2 = builder2.create();
                    alertDialog2.show();
                    CalendarView calendar_filter = dialog2.findViewById(R.id.calendar_filter);
                    Button filtr_akc = dialog2.findViewById(R.id.filtr_akc);
                    Button flitr_an = dialog2.findViewById(R.id.flitr_an);
                    filtr_akc.setOnClickListener(z -> {
                        long time = calendar_filter.getDate();
                        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
                        Date d = new Date(time);
                        do_edit.setText(f.format(d));
                        alertDialog2.hide();
                    });
                    flitr_an.setOnClickListener((z) -> alertDialog2.hide());
                });
                od_edit.setOnClickListener(v -> {
                    AlertDialog alertDialog2;
                    AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
                    LayoutInflater l2 = (LayoutInflater) getApplicationContext().getSystemService(getBaseContext().LAYOUT_INFLATER_SERVICE);
                    View dialog2 = l2.inflate(R.layout.calendar_view, null);
                    builder2.setView(dialog2);
                    alertDialog2 = builder2.create();
                    alertDialog2.show();
                    CalendarView calendar_filter = dialog2.findViewById(R.id.calendar_filter);
                    Button filtr_akc = dialog2.findViewById(R.id.filtr_akc);
                    Button flitr_an = dialog2.findViewById(R.id.flitr_an);
                    filtr_akc.setOnClickListener(z -> {
                        long time = calendar_filter.getDate();
                        SimpleDateFormat g = new SimpleDateFormat("yyyy-MM-dd");
                        Date h = new Date(time);
                        Log.e("g", g.format(h));
                        od_edit.setText(g.format(h));
                        alertDialog2.hide();
                    });
                    flitr_an.setOnClickListener((z) -> alertDialog2.hide());
                });
                anuluj.setOnClickListener(v -> alertDialog.hide());
                break;
            case R.id.del_plant_butt:
                AlertDialog aa4;
                AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
                LayoutInflater l2 = (LayoutInflater) getApplicationContext().getSystemService(getBaseContext().LAYOUT_INFLATER_SERVICE);
                View dialog2 = l2.inflate(R.layout.del_plant, null);
                builder2.setView(dialog2);
                aa4 = builder2.create();
                aa4.show();
                Button can = dialog2.findViewById(R.id.can_button);
                Button del = dialog2.findViewById(R.id.del_button);
                can.setOnClickListener(v -> aa4.hide());
                String sql4 = "http://serwer1727017.home.pl/2ti/floda/floda_del.php";
                del.setOnClickListener(v -> {
                    StringRequest ghg = new StringRequest(Request.Method.POST, sql4, response -> {
                        if (response.contains("1")) {
                            Log.e("Res", response);
                            Toast.makeText(this, getString(R.string.config_deleted), Toast.LENGTH_LONG).show();
                            aa4.hide();
                            finish();
                        } else {
                            Toast.makeText(this, getString(R.string.Fata_error), Toast.LENGTH_LONG).show();
                        }
                    }, error -> {

                    }
                    ) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> parms = new HashMap<>();
                            parms.put("ID", String.valueOf(ID));
                            Log.e("Res", String.valueOf(ID));
                            return parms;
                        }
                    };
                    q.add(ghg);
                    q.start();
                });

                break;
        }

        return super.onOptionsItemSelected(item);
    }

    void databaseGet(String id, int nr) {

        datanawodnienie = new ArrayList<>();
        datanaslonecznienie = new ArrayList<>();
        datatemperatura = new ArrayList<>();
        datawilgotnosc = new ArrayList<>();
        String sql = "http://serwer1727017.home.pl/2ti/floda/detail/data.php" + timing;
        if (nr == 0) {
            stringRequest = new StringRequest(Request.Method.POST, sql, response -> {
                try {

                    JSONArray podst = new JSONArray(response);
                    JSONObject det = podst.getJSONObject(0);

                    JSONObject foo;
                    title.setText(det.getString("name"));
                    subtitle.setText(" (" + det.getString("Nazwa") + ")");
                    timeperiod = det.getInt("czas");
                    Log.e("timeperiod", String.valueOf(timeperiod));
                    for (int index = 1; index < podst.length(); index++) {

                        foo = podst.getJSONObject(index);
                        Log.e("Data", id + " " + foo.getString("sun") + " " + foo.getString("temperature") + " " + foo.getString("humidity"));
                        datanawodnienie.add(new BarEntry(index - 1, Integer.valueOf(foo.getString("soil"))));
                        datanaslonecznienie.add(new BarEntry(index - 1, Integer.valueOf(foo.getString("sun"))));
                        datatemperatura.add(new BarEntry(index - 1, Integer.valueOf(foo.getString("temperature"))));
                        datawilgotnosc.add(new BarEntry(index - 1, Integer.valueOf(foo.getString("humidity"))));
                        timeof.add(index - 1, foo.getString("time"));

                    }
                    //timeof.add(podst.length(), podst.getJSONObject(podst.length()).getString("time"));

                    Log.e("ble", String.valueOf(response));
                    if (det.getInt("a_w_g") != 0) {
                        nmax.add(new Entry(0, det.getInt("a_w_g")));
                        nmax.add(new Entry(podst.length() - 2, det.getInt("a_w_g")));
                        ostatnie.setText("");

                    } else {

                        if (det.getInt("c_k_p") != 0) {
                            if (det.getInt("c_k_p") - det.getInt("watering") <= 0) {
                                ostatnie.setText(getString(R.string.plant_water));
                            } else {
                                if (det.getInt("watering") - det.getInt("c_k_p") < 2) {
                                    ostatnie.setText(getString(R.string.water_tomoro));
                                } else
                                    ostatnie.setText(getString(R.string.for_water_0) + (det.getInt("c_k_p") - det.getInt("watering")) + getString(R.string.for_water_1));
                            }
                        }
                    }
                    if (det.getInt("s_d_s_x") != 0) {
                        smax.add(new Entry(0, det.getInt("s_d_s_x")));
                        smax.add(new Entry(podst.length() - 2, det.getInt("s_d_s_x")));
                    }
                    if (det.getInt("s_d_s") != 0) {
                        smin.add(new Entry(0, det.getInt("s_d_s")));
                        smin.add(new Entry(podst.length() - 2, det.getInt("s_d_s")));
                    }
                    if (det.getInt("s_d_t_x") != 0) {
                        tmax.add(new Entry(0, det.getInt("s_d_t_x")));
                        tmax.add(new Entry(podst.length() - 2, det.getInt("s_d_t_x")));
                    }
                    if (det.getInt("s_d_t") != 0) {
                        tmin.add(new Entry(0, det.getInt("s_d_t")));
                        tmin.add(new Entry(podst.length() - 2, det.getInt("s_d_t")));
                    }
                    if (det.getInt("s_d_w_x") != 0) {
                        wmax.add(new Entry(0, det.getInt("s_d_w_x")));
                        wmax.add(new Entry(podst.length() - 2, det.getInt("s_d_w_x")));
                    }
                    if (det.getInt("s_d_w") != 0) {
                        wmin.add(new Entry(0, det.getInt("s_d_w")));
                        wmin.add(new Entry(podst.length() - 2, det.getInt("s_d_w")));
                    }
                    www = det.getString("www");


                } catch (Exception e) {
                    Log.e("Ustawianie danych", "eeeeeee blad" + e);
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

                    if (det.getInt("a_w_g") != 0) {
                        nmax.add(new Entry(0, det.getInt("a_w_g")));
                        nmax.add(new Entry(podst.length() - 2, det.getInt("a_w_g")));
                        ostatnie.setText("");

                    } else {
                        if (det.getInt("c_k_p") != 0) {
                            if (det.getInt("c_k_p") - det.getInt("watering") <= 0) {
                                ostatnie.setText(getString(R.string.plant_water));
                            } else {
                                if (det.getInt("watering") - det.getInt("c_k_p") < 2) {
                                    ostatnie.setText(getString(R.string.water_tomoro));
                                } else
                                    ostatnie.setText(getString(R.string.for_water_0) + (det.getInt("c_k_p") - det.getInt("watering")) + getString(R.string.for_water_1));
                            }

                        } else {
                            ostatnie.setText("");
                        }
                    }
                    if (det.getInt("s_d_s_x") != 0) {
                        smax.add(new Entry(0, det.getInt("s_d_s_x")));
                        smax.add(new Entry(podst.length() - 2, det.getInt("s_d_s_x")));
                    }
                    if (det.getInt("s_d_s") != 0) {
                        smin.add(new Entry(0, det.getInt("s_d_s")));
                        smin.add(new Entry(podst.length() - 2, det.getInt("s_d_s")));
                    }
                    if (det.getInt("s_d_t_x") != 0) {
                        tmax.add(new Entry(0, det.getInt("s_d_t_x")));
                        tmax.add(new Entry(podst.length() - 2, det.getInt("s_d_t_x")));
                    }
                    if (det.getInt("s_d_t") != 0) {
                        tmin.add(new Entry(0, det.getInt("s_d_t")));
                        tmin.add(new Entry(podst.length() - 2, det.getInt("s_d_t")));
                    }
                    if (det.getInt("s_d_w_x") != 0) {
                        wmax.add(new Entry(0, det.getInt("s_d_w_x")));
                        wmax.add(new Entry(podst.length() - 2, det.getInt("s_d_w_x")));
                    }
                    if (det.getInt("s_d_w") != 0) {
                        wmin.add(new Entry(0, det.getInt("s_d_w")));
                        wmin.add(new Entry(podst.length() - 2, det.getInt("s_d_w")));
                    }
                    www = det.getString("www");
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
        q = new RequestQueue(new DiskBasedCache(getCacheDir(), 1024 * 1024), new BasicNetwork(new HurlStack()));
        q.add(stringRequest);
        q.start();

    }


    BarData setNawodnienie() { /* woda */
        nawo = new LineData();
        LineDataSet set = new LineDataSet(nmax, getString(R.string.potrz_podl));
        set.setCircleRadius(1f);
        set.setLineWidth(2.5f);
        set.setColor(Color.RED);
        set.setFillColor(Color.WHITE);
        set.setDrawValues(false);
        nawo.addDataSet(set);
        BarDataSet d = new BarDataSet(datanawodnienie, getString(R.string.units));
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
        Description foo = new Description();
        foo.setText("");
        nawodnienie.setDescription(foo);
        nawodnienie.invalidate();
    }

    BarData setNaslonecznienie() {
        slo = new LineData();
        LineDataSet max = new LineDataSet(smax, getString(R.string.max_slon));
        LineDataSet min = new LineDataSet(smin, getString(R.string.min_slon));
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
        BarDataSet d = new BarDataSet(datanaslonecznienie, "LUX");
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
                CombinedChart.DrawOrder.BAR, CombinedChart.DrawOrder.LINE, CombinedChart.DrawOrder.LINE
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
        LineDataSet max = new LineDataSet(tmax, getString(R.string.max_temp));
        LineDataSet min = new LineDataSet(tmin, getString(R.string.min_temp));
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
                CombinedChart.DrawOrder.BAR, CombinedChart.DrawOrder.LINE, CombinedChart.DrawOrder.LINE
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

        LineDataSet max = new LineDataSet(wmax, getString(R.string.max_wilg));
        LineDataSet min = new LineDataSet(wmin, getString(R.string.min_wilg));
        max.setCircleRadius(1f);
        max.setLineWidth(2.5f);
        max.setColor(Color.YELLOW);
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
                CombinedChart.DrawOrder.BAR, CombinedChart.DrawOrder.LINE, CombinedChart.DrawOrder.LINE
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
