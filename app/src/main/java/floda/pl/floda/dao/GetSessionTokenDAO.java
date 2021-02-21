package floda.pl.floda.dao;

import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class GetSessionTokenDAO {
    final String[] data = {"0", "pl"};

    public String[] getData() {
        return data;
    }

    public RequestQueue getBasicIdUserDataByCredentialsDAO(String login, String password, SharedPreferences preferences, RequestQueue q) {

        String serverURL = preferences.getString("serverURL", "http://www.serwer1727017.home.pl");
        serverURL =  serverURL + "/mobile/login";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, serverURL, response -> {

            try {
                //JSONArray j = new JSONArray(response);
                JSONObject o = new JSONObject(response);
                data[0] = o.getString("Token");
                data[1] = "pl";
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }, error -> {
            Log.e("logowanie", error.getMessage());
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<>();
                map.put("username", login);
                map.put("password", password);
                return map;
            }
        };
        q.add(stringRequest);
        return q;

    }
}
