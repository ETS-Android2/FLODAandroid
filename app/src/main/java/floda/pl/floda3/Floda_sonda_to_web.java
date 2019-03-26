package floda.pl.floda3;


import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import static android.support.v4.content.ContextCompat.getSystemService;


/**
 * A simple {@link Fragment} subclass.
 */
public class Floda_sonda_to_web extends Fragment {

    WebView w;
    public Floda_sonda_to_web() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_add_sonda_to_web, container, false);
        w=v.findViewById(R.id.webview2);
        w.getSettings().setJavaScriptEnabled(true);
        // register class containing methods to be exposed to JavaScript
        try {
            getActivity().runOnUiThread(() -> {
                WifiManager wifiManager = (WifiManager) getContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                WifiInfo info = wifiManager.getConnectionInfo();
                String ssid = info.getSSID();
                Log.e("d",ssid);
                isFloda(ssid);
            });
        }catch (Exception e){
                Log.e("e", e.toString());
        }
        return v;
    }
    void isFloda(String ssid){
        if(ssid.contains("FLODA")) {
        /*JSInterface = new JavaScriptInterface(this);
        wv.addJavascriptInterface(JSInterface, "JSInterface");*/
            w.setWebViewClient(new WebViewClient());
            w.loadUrl("http://192.168.4.1");
        }else{
            AlertDialog alertDialog;
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            LayoutInflater l = (LayoutInflater) getContext().getApplicationContext().getSystemService(getContext().LAYOUT_INFLATER_SERVICE);
            View dialog = l.inflate(R.layout.add_esp_info, null);
            builder.setView(dialog);
            alertDialog = builder.create();
            alertDialog.show();
            Button re =dialog.findViewById(R.id.add_refresh);
            re.setOnClickListener(v->{
                alertDialog.hide();
                isFloda(ssid);
            });
        }
    }
}
