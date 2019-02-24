package floda.pl.floda3.FORUM;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
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
import java.util.concurrent.locks.ReadWriteLock;

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
        String url = "http://serwer1727017.home.pl/2ti/floda/detail/forum_list.php";
        StringRequest request = new StringRequest(Request.Method.POST, url, response -> {
            data.clear();
            try {
                JSONArray o = new JSONArray(response);
                for (int i = 0; i < o.length(); i++) {
                    JSONObject foo = o.getJSONObject(i);
                    data.add(new fData(foo.getString("title"), foo.getString("ID"), foo.getString("score"), foo.getString("category")));
                }
                mAdapter = new ForumCRDV(data, item -> {
                    Fragment post = new FLODA_forum_post();
                    Bundle n = new Bundle();
                    n.putString("id_post", item.ID);
                    post.setArguments(n);
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    FragmentTransaction transaction = fm.beginTransaction();
                    transaction.replace(R.id.content_fram, post);
                    transaction.commit();
                    Log.e("duoa", "dupa");
                });

                mRecycleView.setAdapter(mAdapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            alertDialog5.hide();
        }, error -> {
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parms = new HashMap<>();
                parms.put("search", search.getText().toString());
                return parms;
            }
        };

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
        FloatingActionButton f = w.findViewById(R.id.forum_add_butt);
        f.setOnClickListener(v -> {
            LayoutInflater l4 = (LayoutInflater) getContext().getSystemService(getContext().LAYOUT_INFLATER_SERVICE);
            View v4 = l4.inflate(R.layout.floda_add_post, null);
            AlertDialog alertDialog4;
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setView(v4);
            alertDialog4 = builder.create();
            alertDialog4.show();
            Button anu = v4.findViewById(R.id.for_an);
            anu.setOnClickListener(v1 -> {
                alertDialog4.hide();
            });
            Button akc = v4.findViewById(R.id.for_dod);
            akc.setOnClickListener(v1 -> {

                TextView title, desc, cat;
                title = v4.findViewById(R.id.for_post);
                desc = v4.findViewById(R.id.for_desc);
                cat = v4.findViewById(R.id.for_cat);
                if (title.getText().length() > 4 && desc.getText().length() > 4) {
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());

                    String url1 = "http://serwer1727017.home.pl/2ti/floda/forum/add_post.php?ID=" + preferences.getString("ID", "0") + "&title=" + title.getText() + "&desc=" + desc.getText() + "&cat=" + cat.getText();
                    StringRequest request1 = new StringRequest(Request.Method.GET, url1, response -> {

                        alertDialog4.hide();
                        Toast.makeText(getContext(), "Zapostowano", Toast.LENGTH_SHORT).show();
                        Fragment post = new FLODA_forum_post();
                        Log.e("POST ID", response);
                        Bundle n = new Bundle();
                        n.putString("id_post", response);
                        post.setArguments(n);
                        FragmentManager fm = getActivity().getSupportFragmentManager();
                        FragmentTransaction transaction = fm.beginTransaction();
                        transaction.replace(R.id.content_fram, post);
                        transaction.commit();

                    }, error -> {
                    }
                    );
                    q.stop();
                    q.add(request1);
                    q.start();
                } else {
                    Toast.makeText(getContext(), "Brakuje opisu lub tytułu", Toast.LENGTH_SHORT).show();
                }
            });


        });
        return w;
    }

    public static class ForumCRDV extends RecyclerView.Adapter<ForumCRDV.MyViewHolder> {
        static List<fData> fData;
        OnItemClickListener listener;

        public ForumCRDV(List<fData> mDataset, OnItemClickListener listener) {
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
            myViewHolder.color.setBackgroundColor(Integer.valueOf(fData.get(i).score) < 0 ? Color.parseColor("#af2d34") : Color.parseColor("#307a00"));

            if (Integer.valueOf(fData.get(i).score) < 0) {
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

            public void bind(final fData item, final OnItemClickListener listener) {
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