package floda.pl.floda3.FORUM;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
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

import floda.pl.floda3.ListOfPlants;
import floda.pl.floda3.PlantDetail;
import floda.pl.floda3.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class FLODA_forum extends Fragment {
    View w;
    private RecyclerView mRecycleView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
EditText search;
    public FLODA_forum() {
        // Required empty public constructor
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        List<fData> data;
        w = inflater.inflate(R.layout.fragment_floda_forum, container, false);
        search = w.findViewById(R.id.formumsearch);
        mRecycleView = w.findViewById(R.id.forum_recycleview);
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
        String url = "http://serwer1727017.home.pl/2ti/floda/detail/forum_list.php?search=";
        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
            data.clear();
            try {
                JSONArray o = new JSONArray(response);
                for(int i=0;i<o.length();i++){
                    JSONObject foo = o.getJSONObject(i);
                    data.add(new fData(foo.getString("title"),foo.getString("ID"),foo.getString("score"),foo.getString("category")));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mAdapter = new ForumCRDV(data, item -> {
                Intent i = new Intent(getContext(), null);
                i.putExtra("ID", item.ID);
                startActivity(i);
            });

            mRecycleView.setAdapter(mAdapter);
            alertDialog5.hide();
        }, error -> {
        });
        RequestQueue q = new RequestQueue(new DiskBasedCache(getActivity().getCacheDir(), 1024 * 1024), new BasicNetwork(new HurlStack()));
        q.add(request);
        q.start();
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                q.add(request);
                q.start();
            }
        });
        return w;
    }

    public static class ForumCRDV extends RecyclerView.Adapter<ForumCRDV.MyViewHolder> {
        static List<fData> fData;
        OnItemClickListener listener;
        ForumCRDV(List<fData> mDataset, OnItemClickListener listener) {
            fData = new ArrayList<>();
            this.listener = listener;
            this.fData = mDataset;
        }
        @NonNull
        @Override
        public ForumCRDV.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.for_list_cardviw, viewGroup, false);
            return new MyViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ForumCRDV.MyViewHolder myViewHolder, int i) {
            myViewHolder.title.setText(fData.get(i).title);
            myViewHolder.category.setText(fData.get(i).category);
            myViewHolder.score.setText(fData.get(i).score);
            if(Integer.valueOf(fData.get(i).score)<0){
                myViewHolder.color.setBackgroundColor(Color.RED);
            } else {
                if (Integer.valueOf(fData.get(i).score) > 0) {
                    myViewHolder.color.setBackgroundColor(Color.GREEN);
                }
            }

            myViewHolder.bind(fData.get(i), listener);
        }

        @Override
        public int getItemCount() {
            return fData.size();
        }
        public interface OnItemClickListener {
            void onItemClick(fData item);
        }
        public class MyViewHolder extends RecyclerView.ViewHolder {
            CardView cv;
            TextView title;
            TextView score;
            TextView category;
            View color;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                cv = itemView.findViewById(R.id.forumcv);
                title = itemView.findViewById(R.id.forum_title);
                category = itemView.findViewById(R.id.forum_category);
                score = itemView.findViewById(R.id.count);
                color = itemView.findViewById(R.id.forum_color);
            }
            public void bind(final fData item, final OnItemClickListener listener){
                itemView.setOnClickListener(v -> listener.onItemClick(item));
            }
        }
    }

    class fData {
        public String title, ID, score, category;

        fData(String title, String ID, String score, String category) {
            this.category = category;
            this.ID = ID;
            this.score = score;
            this.title = title;
        }

    }
}