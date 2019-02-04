package floda.pl.floda3.FORUM;



import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import floda.pl.floda3.R;

import static android.support.v7.widget.RecyclerView.Adapter;
import static android.support.v7.widget.RecyclerView.LayoutManager;
import static android.support.v7.widget.RecyclerView.OnItemTouchListener;
import static android.support.v7.widget.RecyclerView.ViewHolder;


/**
 * A simple {@link Fragment} subclass.
 */
public class FLODA_forum extends Fragment {
    private static Adapter adapter;
    private LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    //   private static ArrayList<forum_datamodel> data;
    static View.OnClickListener myOnClickListener;
    private static ArrayList<Integer> removedItems;
    //private static forum_datamodel contact[];
    public View view;
    public FloatingActionButton floatingActionButton;

    public FLODA_forum() {
        // Required empty public constructor
    }
}
    /*public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_floda_forum, container, false);
        final Context context = getContext();
        recyclerView = view.findViewById(R.id.forum_recycleview);
        recyclerView.setHasFixedSize(true);
        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.fff);
        EditText searchView = (EditText)view.findViewById(R.id.formumsearch);
        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        data = new ArrayList<forum_datamodel>();
        final Response.Listener<String> response = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray json = new JSONArray(response);
                    contact = new forum_datamodel[json.length()];
                    for (int i = 0; i < json.length(); i++) {
                        JSONObject c = json.getJSONObject(i);

                        String ID= c.getString("ID");
                        String Title= c.getString("Title");
                        String Score= c.getString("Score");
                        String category= c.getString("category");



                        // tmp hash map for single contact
                        contact[i] = new forum_datamodel(ID, Title, Score, category);
                        Log.i("Cardv", contact[i].date);
                        data.add(contact[i]);
                    }
                    adapter = new FrumCrdv(data);

                    recyclerView.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception ee) {
                    ee.printStackTrace();
                }
            }
        };
        Gettter getter = new Gettter(searchView.getText().toString() ,response);
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(getter);
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(getContext(),null);
                        intent.putExtra("data","");
                        intent.putExtra("dane",contact[position].getData());
                        Log.i("fupa","fu");
                        startActivity(intent);
                    }
                }) {

                    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
                        Toast.makeText(getContext(),"Fatal error",Toast.LENGTH_LONG);
                    }
                }
        );
        // removedItems = new ArrayList<Integer>();
        floatingActionButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),null);
                intent.putExtra("ID", "");
                intent.putExtra("data","");
                startActivity(intent);
            }
        });
        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                data.clear();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                Gettter getter = new Gettter(searchView.getText().toString() ,response);
                RequestQueue queue = Volley.newRequestQueue(getContext());
                queue.add(getter);
            }
        });


        return view;
    }

    private abstract static class RecyclerItemClickListener implements OnItemTouchListener {
        private OnItemClickListener mListener;

        public interface OnItemClickListener{
            public void onItemClick(View view, int position);
        }
        GestureDetector mGestureDetector;
        public RecyclerItemClickListener(Context context, OnItemClickListener listener) {
            mListener = listener;
            mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
            View childView = view.findChildViewUnder(e.getX(), e.getY());
            if (childView != null && this.mListener != null && mGestureDetector.onTouchEvent(e)) {
                this.mListener.onItemClick(childView, view.getChildAdapterPosition(childView));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView view, MotionEvent motionEvent) {
        }
    }
    class Gettter extends StringRequest {
        static final String REGISTER_REQUEST_URL = "http://serwer1727017.home.pl/2ti/ErasmusAPP/forum_list.php";
        Map<String, String> params;

        public Gettter(String search, Response.Listener<String> listener) {
            super(Method.POST, REGISTER_REQUEST_URL, listener, null);
            params = new HashMap<>();
            params.put("search", search);
        }

        public Map<String, String> getParams() {
            return params;
        }
    }


    class forum_datamodel {
        String ID;
        String Title;
        String Score;
        String owner_id;
        String category;
        String Descr;
        String date;
        String name;
        String surname;
        String team;

        forum_datamodel(String ID, String Title, String Score, String category) {
            this.ID = ID;
            this.Title = Title;
            this.Score = Score;
            this.category = category;

        }

        public String getID() {
            return ID;
        }

        public String getTitle() {
            return Title;
        }



        public String[] getData() {
            String[] str = new String[10];
            str[0] = ID;
            str[1] = Title;
            str[2] = Score;
            str[3] = owner_id;
            str[4] = category;
            str[5] = Descr;
            str[6] = date;
            str[7] = name;
            str[8] = surname;
            str[9] = team;
            return str;
        }

    }


    public class FrumCrdv extends RecyclerView.Adapter<> {

        private ArrayList<forum_datamodel> dataSet;

        public  class MyViewHolder extends ViewHolder {

            TextView textTitle;
            TextView textTeam;
            ImageView imageView;
            CardView mCardViewBottom;
            TextView count;

            public MyViewHolder(View itemView) {
                super(itemView);
                this.textTitle = (TextView) itemView.findViewById(R.id.crd_v_name);
                this.textC = (TextView) itemView.findViewById(R.id.crd_v_cat);
                this.mCardViewBottom = (CardView) itemView.findViewById(R.id.card_view2);
                this.count= (TextView) itemView.findViewById(R.id.count);
                this.imageView=(ImageView) itemView.findViewById(R.id.imageView4);

            }
        }


        public FrumCrdv(ArrayList<forum_datamodel> data) {
            this.dataSet = data;
        }

        @Override
        public FrumCrdv.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                        int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.for_list_cardviw, parent, false);

            view.setOnClickListener(myOnClickListener);

            FrumCrdv.MyViewHolder myViewHolder = new FrumCrdv.MyViewHolder(view);
            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
            TextView name = viewHolder.textTitle;
            TextView team = viewHolder.textTeam;
            TextView counter = viewHolder.count;
            ImageView cardView = viewHolder.imageView;
           /* if(dataSet.get(position).Title.length()>=23){
                name.setText(dat/aSet.get(position).Title.substring(0,23)+"...");
      /*      }else {*//*
            name.setText(dataSet.get(position).Title);
            RelativeLayout.LayoutParams params = new
                    RelativeLayout.LayoutParams(CardView.LayoutParams.MATCH_PARENT,
                    CardView.LayoutParams.WRAP_CONTENT);
            // Set the height by params
            //params.height=1700;
            // set height of RecyclerView
            viewHolder.mCardViewBottom.setLayoutParams(params);
            team.setText(dataSet.get(i).category);
            counter.setText(dataSet.get(i).Score);
            if(Integer.parseInt(contact[i].Score)<0){
                cardView.setBackgroundColor(Color.parseColor("#9b0000"));
                Log.i("dddd", String.valueOf(Integer.parseInt(contact[i].Score)));
            }else {
                if (Integer.parseInt(contact[i].Score) > 0) {
                    cardView.setBackgroundColor(Color.parseColor("#016b00"));
                }
            }
        }







        @Override
        public int getItemCount() {
            return dataSet.size();
        }
    }

}*/