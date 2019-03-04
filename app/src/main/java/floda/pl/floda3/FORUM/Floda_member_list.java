package floda.pl.floda3.FORUM;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.List;

import floda.pl.floda3.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Floda_member_list extends Fragment {
    View w;
    private RecyclerView mRecycleView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public Floda_member_list() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View w =  inflater.inflate(R.layout.fragment_floda_member_list, container, false);
        List<Floda_member_list.uData> data;
        w = inflater.inflate(R.layout.fragment_floda_member_list, container, false);
        mRecycleView = w.findViewById(R.id.member_recycleview);
        mRecycleView.setHasFixedSize(true);
        AlertDialog alertDialog5;
        AlertDialog.Builder builder5 = new AlertDialog.Builder(getContext());
        LayoutInflater l5 = (LayoutInflater) getContext().getSystemService(getContext().LAYOUT_INFLATER_SERVICE);
        View v5 = l5.inflate(R.layout.loading, null);
        builder5.setView(v5);
        alertDialog5 = builder5.create();
        alertDialog5.show();
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecycleView.setLayoutManager(mLayoutManager);
        data = new ArrayList<>();
        String url = "http://serwer1727017.home.pl/2ti/floda/detail/member_list.php";
        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
            data.clear();
            try {
                JSONArray o = new JSONArray(response);
                for (int i = 0; i < o.length(); i++) {
                    JSONObject foo = o.getJSONObject(i);
                    data.add(new Floda_member_list.uData(foo.getString("name"), foo.getString("ID"), foo.getString("email")));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            mAdapter = new Floda_member_list.ForumCRDV(data);

            mRecycleView.setAdapter(mAdapter);
            alertDialog5.hide();
        }, error -> {
        });
        RequestQueue q = new RequestQueue(new DiskBasedCache(getActivity().getCacheDir(), 1024 * 1024), new BasicNetwork(new HurlStack()));
        q.add(request);
        q.start();
        return w;
    }






public static class ForumCRDV extends RecyclerView.Adapter<Floda_member_list.ForumCRDV.MyViewHolder> {
    List<Floda_member_list.uData> uData;

    public ForumCRDV(List<Floda_member_list.uData> mDataset) {
        uData = new ArrayList<>();
        this.uData = mDataset;
    }

    @NonNull
    @Override
    public Floda_member_list.ForumCRDV.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.for_list_cardviw, viewGroup, false);

        return new Floda_member_list.ForumCRDV.MyViewHolder(v);
    }



    @Override
    public void onBindViewHolder(@NonNull Floda_member_list.ForumCRDV.MyViewHolder myViewHolder, int i) {
        myViewHolder.email.setText(uData.get(i).email);
        myViewHolder.name.setText(uData.get(i).name);




    }

    @Override
    public int getItemCount() {
        return uData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView name;
        TextView email;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.membcv);
            name = itemView.findViewById(R.id.memb_name);
            email = itemView.findViewById(R.id.memb_email);

        }
    }
}

class uData {
    public String name, ID, email;

    uData(String name, String ID, String email) {
        this.ID = ID;
        this.name = name;
        this.email = email;
    }

}


}
