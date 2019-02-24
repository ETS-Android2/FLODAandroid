package floda.pl.floda3.FORUM;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import floda.pl.floda3.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FLODA_forum_post extends Fragment {


    public FLODA_forum_post() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_floda_forum_post, container, false);
    }

}
