package floda.pl.floda3;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;


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
        mRecycleView = (RecyclerView) w.findViewById(R.id.listofplantrv);
        mRecycleView.setHasFixedSize(true);
        ProgressBar bar = w.findViewById(R.id.progressBar2);
        bar.setVisibility(View.VISIBLE);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecycleView.setLayoutManager(mLayoutManager);
        data = new ArrayList<>();
        String url = "http://serwer1727017.home.pl/2ti/floda/floda_list.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                bar.setVisibility(View.INVISIBLE);
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject o = jsonArray.getJSONObject(i);
                        data.add(new pData(o.getString("Name"), o.getString("latin"), o.getString("ison").contains("1")?"on":"off",o.getString("ID")));
                        Log.e("cs", o.getString("Name"));

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                bar.setVisibility(View.INVISIBLE);
                mAdapter = new Listplants(data, new Listplants.OnItemClickListener() {
                    @Override
                    public void onItemClick(pData item) {
                        Intent i = new Intent(getContext(),PlantDetail.class);
                        i.putExtra("ID",item.ID);
                        startActivity(i);
                    }
                });

                mRecycleView.setAdapter(mAdapter);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error",error.toString());
                //todo: zrobic snackbar ze bład z polaczeniem
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> h = new HashMap<>();
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                h.put("ID", sharedPreferences.getString("ID", "0"));
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

            public MyViewHolder(View v) {
                super(v);
                cv = (CardView) v.findViewById(R.id.card_view);
                cname = (TextView) v.findViewById(R.id.plant_name);
                cgenre = (TextView) v.findViewById(R.id.plant_genre);
                cstatus = (TextView) v.findViewById(R.id.esponoff);
            }

            public void bind(final pData item, final OnItemClickListener listener) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onItemClick(item);
                    }
                });
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
            MyViewHolder phv = new MyViewHolder(v);
            return phv;
        }

        @Override
        public void onBindViewHolder(@NonNull Listplants.MyViewHolder myViewHolder, int i) {
            myViewHolder.cname.setText(pdata.get(i).pname);
            myViewHolder.cgenre.setText(pdata.get(i).pgenre);
            myViewHolder.cstatus.setText(pdata.get(i).pstatus);
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
        String pname;
        String pgenre;
        String pstatus;
        String ID;

        public pData(String pname, String pgenre, String pstatus, String ID) {
            this.pname = pname;
            this.pgenre = pgenre;
            this.pstatus = pstatus;
            this.ID = ID;
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
