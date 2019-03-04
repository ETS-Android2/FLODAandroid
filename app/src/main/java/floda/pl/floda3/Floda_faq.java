package floda.pl.floda3;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Floda_faq#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Floda_faq extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    View v;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    WebView w;

    public Floda_faq() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Floda_faq.
     */
    // TODO: Rename and change types and number of parameters
    public static Floda_faq newInstance(String param1, String param2) {
        Floda_faq fragment = new Floda_faq();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         v = inflater.inflate(R.layout.fragment_floda_faq, container, false);
        w=v.findViewById(R.id.webview);
        w.getSettings().setJavaScriptEnabled(true);
        // register class containing methods to be exposed to JavaScript

        /*JSInterface = new JavaScriptInterface(this);
        wv.addJavascriptInterface(JSInterface, "JSInterface");
*/      w.setWebViewClient(new WebViewClient());
        w.loadUrl("http://www.serwer1727017.home.pl/2ti/floda/web/wp/index.php");
        return v;
    }

}
