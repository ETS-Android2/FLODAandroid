package floda.pl.floda.dao;

import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class GetUserBasicDataDAO {
    final Boolean error = true;

    public Boolean isDone() {
        return !error;
    }

    public RequestQueue getBasicIdUserDataByCredentialsDAO(SharedPreferences preferences, RequestQueue q) {

        String serverURL = preferences.getString("serverURL", "http://192.168.1.101:8000");
        serverURL = serverURL + "/mobile/basic";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, serverURL, response -> {

            try {
                JSONObject o = new JSONObject(response);
                if (o.getBoolean("error")) {
                    preferences.edit().putString("username", "").apply();
                    preferences.edit().putString("userEmail", "").apply();
                    preferences.edit().putString("superuser", "").apply();

                } else {
                    preferences.edit().putString("username", o.getString("username")).apply();
                    preferences.edit().putString("userEmail", o.getString("email")).apply();
                    preferences.edit().putString("superuser", o.getString("superuser")).apply();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            Log.e("getting UserData", error.getMessage());
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<>();
                map.put("Token", Objects.requireNonNull(preferences.getString("Token", "0")));
                return map;
            }
        };
        q.add(stringRequest);
        return q;

    }
}
