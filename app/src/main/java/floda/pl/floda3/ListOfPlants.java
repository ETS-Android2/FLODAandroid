package floda.pl.floda3;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class ListOfPlants extends Fragment {
    private RecyclerView mRecycleView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    View w;


    public ListOfPlants() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        List<pData> data;
        w = inflater.inflate(R.layout.fragment_list_of_plants, container, false);
        mRecycleView = w.findViewById(R.id.listofplantrv);
        mRecycleView.setHasFixedSize(true);
        ProgressBar bar = w.findViewById(R.id.progressBar2);
        bar.setVisibility(View.VISIBLE);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecycleView.setLayoutManager(mLayoutManager);
        data = new ArrayList<>();
        String url = "http://serwer1727017.home.pl/2ti/floda/floda_list.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
            bar.setVisibility(View.INVISIBLE);
            try {
                data.clear();
                JSONArray jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject o = jsonArray.getJSONObject(i);
                    data.add(new pData(o.getString("Name"), o.getString("latin"), o.getString("ison").contains("1") ? "on" : "off", o.getString("ID"), o.getString("sonda"), o.getString("normwilg"), o.getString("normsun"), o.getString("normtemp"), o.getString("normpod")));
                    Log.e("cs", o.getString("normtemp"));

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            bar.setVisibility(View.INVISIBLE);
            mAdapter = new Listplants(data, item -> {
                Intent i = new Intent(getContext(),PlantDetail.class);
                i.putExtra("ID",item.ID);
                startActivity(i);
            });

            mRecycleView.setAdapter(mAdapter);
        }, error -> {
            Log.e("error",error.toString());
            //todo: zrobic snackbar ze bład z polaczeniem
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> h = new HashMap<>();
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                h.put("ID", Objects.requireNonNull(sharedPreferences.getString("ID", "0")));
                return h;
            }
        };
        RequestQueue q = new RequestQueue(new DiskBasedCache(getActivity().getCacheDir(), 1024 * 1024), new BasicNetwork(new HurlStack()));
        q.add(stringRequest);
        q.start();

        return w;
    }

    public static class Listplants extends RecyclerView.Adapter<Listplants.MyViewHolder> {
        List<pData> pdata;
        OnItemClickListener listener;

        public interface OnItemClickListener {
            void onItemClick(pData item);
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            CardView cv;
            TextView cname;
            TextView cgenre;
            TextView cstatus;
            ImageView cwilg;
            ImageView csun;
            ImageView ctemp;
            ImageView csoil;
            public MyViewHolder(View v) {
                super(v);
                cv = v.findViewById(R.id.card_view);
                cname = v.findViewById(R.id.plant_name);
                cgenre = v.findViewById(R.id.lgenre);
                cstatus = v.findViewById(R.id.esponoff);
                cwilg = v.findViewById(R.id.indicatort);
                csun = v.findViewById(R.id.indicatorh);
                ctemp = v.findViewById(R.id.indicatorw);
                csoil = v.findViewById(R.id.indicators);
            }

            public void bind(final pData item, final OnItemClickListener listener) {
                itemView.setOnClickListener(v -> listener.onItemClick(item));
            }
        }

        public Listplants(List<pData> mDataset, OnItemClickListener listener) {
            pdata = new ArrayList<>();
            this.listener = listener;
            this.pdata = mDataset;
        }

        @NonNull
        @Override
        public Listplants.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.plantcard, viewGroup, false);
            return new MyViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull Listplants.MyViewHolder myViewHolder, int i) {
            myViewHolder.cname.setText(pdata.get(i).pname);
            myViewHolder.cgenre.setText("("+pdata.get(i).pgenre+")");
            myViewHolder.cstatus.setText(pdata.get(i).pstatus);
            myViewHolder.cv.setCardBackgroundColor(pdata.get(i).pstatus.contains("on") ? Color.parseColor("#ff669900") : Color.parseColor("#af2d34"));
            myViewHolder.ctemp.setBackgroundColor(pdata.get(i).temp ? Color.parseColor("#ff669900") : Color.parseColor("#af2d34"));
            myViewHolder.csoil.setBackgroundColor(pdata.get(i).pod ? Color.parseColor("#ff669900") : Color.parseColor("#af2d34"));
            myViewHolder.csun.setBackgroundColor(pdata.get(i).sun ? Color.parseColor("#ff669900") : Color.parseColor("#af2d34"));
            myViewHolder.cwilg.setBackgroundColor(pdata.get(i).wilg ? Color.parseColor("#ff669900") : Color.parseColor("#af2d34"));
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

    public class pData {
        String pname;
        String pgenre;
        String pstatus;
        public String ID;
        String nr;
        boolean wilg;
        boolean sun;
        boolean temp;
        boolean pod;

        public pData(String pname, String pgenre, String pstatus, String ID, String nr, String wilg, String sun, String temp, String pod) {
            this.pname = pname;
            this.pgenre = pgenre;
            this.pstatus = pstatus;
            this.ID = ID;
            this.nr=nr;
            this.wilg = wilg.startsWith("1") ? true : false;
            this.sun = sun.startsWith("1") ? true : false;
            this.temp = temp.startsWith("1") ? true : false;
            this.pod = pod.startsWith("1") ? true : false;
        }

        public String getPgenre() {
            return pgenre;
        }

        public String getPname() {
            return pname;
        }

        public String getPstatus() {
            return pstatus;
        }
    }
}
