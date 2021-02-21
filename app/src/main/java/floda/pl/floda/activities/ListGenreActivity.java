package floda.pl.floda.activities;

import android.content.Intent;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import floda.pl.floda.R;


public class ListGenreActivity extends AppCompatActivity {
    private RecyclerView mRecycleView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    TextView title_list_genre;
    StringRequest f;
    String[] foo;
    ImageButton back;
    static String textnoset, lux, unit, days, co;
    int tryb = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_genre);
        List<ListGenreActivity.pData> data;
        mRecycleView = findViewById(R.id.genrelist);
        mRecycleView.setHasFixedSize(true);
        mRecycleView.setLayoutManager(mLayoutManager);
        data = new ArrayList<>();
        Intent h = getIntent();


        textnoset = getString(R.string.no_set);
        lux = getString(R.string.luxi);
        unit = getString(R.string.units);
        co = getString(R.string.coi);
        days = getString(R.string.days);
        title_list_genre = findViewById(R.id.title_list_genre);
        if (Objects.equals(h.getExtras().getString("NR"), "0")) {
            title_list_genre.setText(getString(R.string.genre_list));
            tryb = 0;
        }
        mLayoutManager = new LinearLayoutManager(this);
        mRecycleView.setLayoutManager(mLayoutManager);
        back = findViewById(R.id.back_list);
        AutoCompleteTextView szukaj = findViewById(R.id.szukajgat);
        back.setOnClickListener(v -> super.onBackPressed());
        String sql = "http://serwer1727017.home.pl/2ti/floda/add/genrelist.php";
        f = new StringRequest(Request.Method.POST, sql, response -> {
            try {
                JSONArray z = new JSONArray(response);
                data.clear();
                for (int i = 0; i < z.length(); i++) {
                    JSONObject o = z.getJSONObject(i);

                    data.add(new pData(o.getString("ID"), o.getString("Nazwa"), o.getString("humid"), o.getString("mintemp"), o.getString("soil_alert"), o.getString("sun"), o.getString("www")));

                }
                foo = new String[data.size()];
                for (int i = 0; i < data.size(); i++) {
                    foo[i] = data.get(i).genre;
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                        android.R.layout.select_dialog_item, foo);
                szukaj.setAdapter(adapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            mAdapter = new ListGenreActivity.Listplants(data, item -> {
                if (tryb == 1) {
                    Intent intent = new Intent();
                    intent.putExtra("ID", item.ID);
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    if (item.www != null && item.www.contains("http://www.")) {
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(item.www));
                        startActivity(i);
                    } else {
                        Toast.makeText(this, getString(R.string.no_guide), Toast.LENGTH_LONG).show();
                    }
                }
            });
            mRecycleView.setAdapter(mAdapter);
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parms = new HashMap<>();
                parms.put("szukaj", szukaj.getText().toString());
                return parms;
            }
        };
        RequestQueue q = new RequestQueue(new DiskBasedCache(getCacheDir(), 1024 * 1024), new BasicNetwork(new HurlStack()));
        q.add(f);
        q.start();

        szukaj.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                q.add(f);
                q.start();
            }
        });
    }

    public static class Listplants extends RecyclerView.Adapter<ListGenreActivity.Listplants.MyViewHolder> {
        List<ListGenreActivity.pData> pdata;
        ListGenreActivity.Listplants.OnItemClickListener listener;

        public interface OnItemClickListener {
            void onItemClick(ListGenreActivity.pData item);
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            CardView cv;
            TextView cgenre, ctemp, chumid, cwater, csun;


            public MyViewHolder(View v) {
                super(v);
                cv = v.findViewById(R.id.card_view);
                cgenre = v.findViewById(R.id.lgenre);
                ctemp = v.findViewById(R.id.tindi);
                chumid = v.findViewById(R.id.humidindi);
                cwater = v.findViewById(R.id.waterindi);
                csun = v.findViewById(R.id.sunindi);
            }

            public void bind(final ListGenreActivity.pData item, final ListGenreActivity.Listplants.OnItemClickListener listener) {
                itemView.setOnClickListener(v -> listener.onItemClick(item));
            }
        }

        public Listplants(List<ListGenreActivity.pData> mDataset, ListGenreActivity.Listplants.OnItemClickListener listener) {
            pdata = new ArrayList<>();
            this.listener = listener;
            this.pdata = mDataset;
        }

        @NonNull
        @Override
        public ListGenreActivity.Listplants.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.genrecard, viewGroup, false);
            return new ListGenreActivity.Listplants.MyViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ListGenreActivity.Listplants.MyViewHolder myViewHolder, int i) {
            myViewHolder.cgenre.setText(pdata.get(i).genre);
            if (!pdata.get(i).temp.contains("0-0°C")) {
                myViewHolder.ctemp.setText(pdata.get(i).temp);
            } else {
                myViewHolder.ctemp.setText(textnoset);
            }

            if (!pdata.get(i).humid.contains("0-0%")) {
                myViewHolder.chumid.setText(pdata.get(i).humid);
            } else {
                myViewHolder.chumid.setText(textnoset);
            }

            if (!pdata.get(i).soil.contains("0") && !pdata.get(i).soil.contains(".0")) {
                if (pdata.get(i).soil.contains(".")) {
                    myViewHolder.cwater.setText("min. " + pdata.get(i).soil + unit);
                } else {
                    pdata.get(i).soil.replaceAll("[^0-9]", "");
                    myViewHolder.cwater.setText(co + pdata.get(i).soil + days);
                }

            } else {
                myViewHolder.cwater.setText(textnoset);
            }

            if (!pdata.get(i).sun.contains("0-0")) {
                myViewHolder.csun.setText(pdata.get(i).sun + lux);
            } else {
                myViewHolder.csun.setText(textnoset);
            }


            myViewHolder.bind(pdata.get(i), listener);
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }

        @Override
        public int getItemCount() {
            return pdata.size();
        }


    }

    class pData {
        String ID, genre, temp, soil, sun, humid, www;


        public pData(String ID, String genre, String humid, String temp, String soil, String sun, String www) {
            this.ID = ID;
            this.genre = genre;
            this.soil = soil;
            this.sun = sun;
            this.temp = temp;
            this.humid = humid;
            this.www = www;
        }
    }
}

