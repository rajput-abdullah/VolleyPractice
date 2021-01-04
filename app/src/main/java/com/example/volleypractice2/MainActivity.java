package com.example.volleypractice2;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String url = "http://192.168.11.153:8082/api/departments";
        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
            Log.d("data", "onResponse: " + response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.has("status")) {
                    boolean status = jsonObject.getBoolean("status");
                    if (status) {
                        if (jsonObject.has("data")) {
                            JSONArray data = jsonObject.getJSONArray("data");
                            List<Department> list = new ArrayList<>();
                            for (int i = 1; i < data.length(); i++) {
                                Department department = new Department();
                                JSONObject singleData = data.getJSONObject(i);
                                if (singleData.has("Id")) {
                                    department.id = singleData.getInt("Id");
                                }
                                if (singleData.has("DepartmentName")) {
                                    department.name = singleData.getString("DepartmentName");
                                }
                                list.add(department);
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "Something Went Wrong 1", Toast.LENGTH_SHORT).show();
                        }
                    } else {Toast.makeText(MainActivity.this, "Something Went Wrong 2", Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, "Something Went Wrong 3", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        App.getInstance().getRequestQueue().add(request);
    }
}