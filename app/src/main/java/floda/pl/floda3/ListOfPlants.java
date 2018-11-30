package floda.pl.floda3;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class ListOfPlants extends Fragment {
    private RecyclerView mRecycleView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    View w;
    List<pData> data;

    public ListOfPlants() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        w = inflater.inflate(R.layout.fragment_list_of_plants, container, false);
        mRecycleView = (RecyclerView) w.findViewById(R.id.listofplantrv);
        mRecycleView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecycleView.setLayoutManager(mLayoutManager);
        data = new ArrayList<>();
        String url = "";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pData d = new pData("1", "1", "!");

                data.add(d);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

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

        mAdapter = new Listplants(data);
        mRecycleView.setAdapter(mAdapter);


        return w;
    }

    public class Listplants extends RecyclerView.Adapter<Listplants.MyViewHolder> {
        List<pData> pdata;

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
        }

        public Listplants(List<pData> mDataset) {
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

        public pData(String pname, String pgenre, String pstatus) {
            this.pname = pname;
            this.pgenre = pgenre;
            this.pstatus = pstatus;
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

