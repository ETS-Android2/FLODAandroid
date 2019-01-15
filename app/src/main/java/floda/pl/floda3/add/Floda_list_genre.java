package floda.pl.floda3.add;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import floda.pl.floda3.R;


public class Floda_list_genre extends AppCompatActivity {
    private RecyclerView mRecycleView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floda_list_genre);
        List<Floda_list_genre.pData> data;
        mRecycleView = findViewById(R.id.genrelist);
        mRecycleView.setHasFixedSize(true);
        mRecycleView.setLayoutManager(mLayoutManager);
        data = new ArrayList<>();
        mLayoutManager = new LinearLayoutManager(this);
        mRecycleView.setLayoutManager(mLayoutManager);

        String sql = "http://serwer1727017.home.pl/2ti/floda/add/genrelist.php";
        StringRequest f = new StringRequest(Request.Method.POST, sql, response -> {
            try {
                JSONArray z = new JSONArray(response);

                for (int i = 0; i < z.length(); i++) {
                    JSONObject o = z.getJSONObject(i);
                    data.add(new pData(o.getString("ID"), o.getString("Nazwa"),"mix: "+o.getString("mintemp")+" max:"+o.getString("maxtemp") , o.getString("humid"), o.getString("soil_alert"), o.getString("sun")));


                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mAdapter = new Floda_list_genre.Listplants(data, item -> {
                Intent intent = new Intent();
                intent.putExtra("ID", item.ID);
                setResult(RESULT_OK, intent);
                finish();
            });
            mRecycleView.setAdapter(mAdapter);
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return super.getParams();
            }
        };
        RequestQueue q = new RequestQueue(new DiskBasedCache(getCacheDir(), 1024 * 1024), new BasicNetwork(new HurlStack()));
        q.add(f);
        q.start();

    }
    public static class Listplants extends RecyclerView.Adapter<Floda_list_genre.Listplants.MyViewHolder> {
        List<Floda_list_genre.pData> pdata;
        Floda_list_genre.Listplants.OnItemClickListener listener;

        public interface OnItemClickListener {
            void onItemClick(Floda_list_genre.pData item);
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            CardView cv;
            TextView cgenre,ctemp,chumid,cwater,csun;


            public MyViewHolder(View v) {
                super(v);
                cv = v.findViewById(R.id.card_view);
                cgenre = v.findViewById(R.id.lgenre);
                ctemp = v.findViewById(R.id.tindi);
                chumid = v.findViewById(R.id.humidindi);
                cwater = v.findViewById(R.id.waterindi);
                csun = v.findViewById(R.id.sunindi);
            }

            public void bind(final Floda_list_genre.pData item, final Floda_list_genre.Listplants.OnItemClickListener listener) {
                itemView.setOnClickListener(v -> listener.onItemClick(item));
            }
        }

        public Listplants(List<Floda_list_genre.pData> mDataset, Floda_list_genre.Listplants.OnItemClickListener listener) {
            pdata = new ArrayList<>();
            this.listener = listener;
            this.pdata = mDataset;
        }

        @NonNull
        @Override
        public Floda_list_genre.Listplants.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.genrecard, viewGroup, false);
            return new Floda_list_genre.Listplants.MyViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull Floda_list_genre.Listplants.MyViewHolder myViewHolder, int i) {
            myViewHolder.cgenre.setText(pdata.get(i).genre);
            myViewHolder.ctemp.setText(pdata.get(i).temp);
            myViewHolder.chumid.setText(pdata.get(i).humid);
            myViewHolder.cwater.setText(pdata.get(i).soil);
            myViewHolder.csun.setText(pdata.get(i).sun);

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
        String ID,genre,temp,soil,sun,humid;


        public pData(String ID,String genre,String humid,String temp,String soil,String sun) {
           this.ID=ID;
            this.genre = genre;
            this.soil=soil;
            this.sun=sun;
            this.temp=temp;
            this.humid=humid;

        }
    }
}

