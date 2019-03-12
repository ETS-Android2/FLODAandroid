package floda.pl.floda3;


import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
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
import java.util.concurrent.atomic.AtomicInteger;


/**
 * A simple {@link Fragment} subclass.
 */
public class ListOfPlants extends Fragment {
    View w;
    private RecyclerView mRecycleView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    StringRequest stringRequest;
    RequestQueue q;
    public ListOfPlants() {
        // Required empty public constructor
    }
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        List<pData> data;
        AtomicInteger width_screen = new AtomicInteger();
        w = inflater.inflate(R.layout.fragment_list_of_plants, container, false);
        mRecycleView = w.findViewById(R.id.listofplantrv);
        mRecycleView.setHasFixedSize(true);
        ProgressBar bar = w.findViewById(R.id.progressBar2);
        bar.setVisibility(View.VISIBLE);
        mLayoutManager = new LinearLayoutManager(getContext());
        mSwipeRefreshLayout = w.findViewById(R.id.swipeRefreshLayout);
        data = new ArrayList<>();
        if(getActivity().getResources().getConfiguration().orientation != Configuration.ORIENTATION_PORTRAIT && getActivity().getResources().getConfiguration().screenWidthDp>750){
            mRecycleView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        }
        else{
            mRecycleView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        }
        String url = "http://serwer1727017.home.pl/2ti/floda/floda_list.php";
        q = new RequestQueue(new DiskBasedCache(getActivity().getCacheDir(), 1024 * 1024), new BasicNetwork(new HurlStack()));

        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            // Refresh items
            q.stop();
            q.add(stringRequest);
            q.start();

        });
        Log.e("e", String.valueOf(getActivity().getResources().getConfiguration().screenWidthDp));
         stringRequest = new StringRequest(Request.Method.POST, url, response -> {
            bar.setVisibility(View.INVISIBLE);
            try {
                data.clear();
                JSONArray jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject o = jsonArray.getJSONObject(i);
                    /*if(getActivity().getResources().getConfiguration().orientation != Configuration.ORIENTATION_PORTRAIT && getActivity().getResources().getConfiguration().screenWidthDp>1500){
                        width_screen.set(getActivity().getResources().getConfiguration().screenWidthDp);
                    }else{
                        width_screen.set(getActivity().getResources().getConfiguration().screenWidthDp/2);
                    }*/
                    data.add(new pData(o.getString("Name"), o.getString("latin"), o.getString("ison").contains("1") ? "on" : "off", o.getString("ID"), o.getString("sonda"), o.getString("normwilg"), o.getString("normsun"), o.getString("normtemp"), o.getString("normpod")));
                    Log.e("cs", o.getString("normtemp"));

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            bar.setVisibility(View.INVISIBLE);
            mAdapter = new Listplants(data, item -> {
                Intent i = new Intent(getContext(), PlantDetail.class);
                i.putExtra("ID", item.ID);
                startActivity(i);
            });

            mRecycleView.setAdapter(mAdapter);
             mSwipeRefreshLayout.setRefreshing(false);
        }, error -> {
            Log.e("error", error.toString());
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
        Thread welcomeThread = new Thread() {

            @Override
            public void run() {
                try {
                    super.run();
                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    q.add(stringRequest);
                    q.start();
                } catch (Exception e) {
                    Log.e("e", e.toString());
                }
            }
        };
        welcomeThread.start();
        return w;
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if(getActivity().getResources().getConfiguration().orientation != Configuration.ORIENTATION_PORTRAIT && getActivity().getResources().getConfiguration().screenWidthDp>750){
            mRecycleView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        }
        else{
            mRecycleView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        }
    }
    public static class Listplants extends RecyclerView.Adapter<Listplants.MyViewHolder> {
        List<pData> pdata;
        OnItemClickListener listener;

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
            myViewHolder.cgenre.setText("(" + pdata.get(i).pgenre + ")");
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


    }

    public class pData {
        public String ID;
        String pname;
        String pgenre;
        String pstatus;
        String nr;
        boolean wilg;
        boolean sun;
        boolean temp;
        boolean pod;
        public pData( String pname, String pgenre, String pstatus, String ID, String nr, String wilg, String sun, String temp, String pod) {
            this.pname = pname;
            this.pgenre = pgenre;
            this.pstatus = pstatus;
            this.ID = ID;
            this.nr = nr;
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
