package com.android.nam.plt_money;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UpdateCategory extends AppCompatActivity {
    View view;
    Helper helper = new Helper();
    public static final String JSON_ARRAY = "result";

    public static final String CATEGORY_ID = "category_id";
    public static final String CATEGORY_NAME = "category_name";
    public static final String STATUS = "status";

    private int category_id_v1;
    private String category_name;
    private String status;

    Categories category = new Categories();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_category);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final int category_id = getIntent().getExtras().getInt("category_id", 0);
        final EditText et_category_name = (EditText) findViewById(R.id.et_category);
        Button btn_cancel = (Button) findViewById(R.id.expensescancelBtn);
        Button btn_save = (Button) findViewById(R.id.expensesaveBtn);

        //Showing the progress dialog
        final ProgressBar bar = (ProgressBar) findViewById(R.id.progressBar);
        bar.setVisibility(View.VISIBLE);

        //display category name on edittext
        if(!helper.IsReachable(UpdateCategory.this, helper.get_display_category_url() + category_id)){
            Toast.makeText(UpdateCategory.this, "Please check your internet connection!", Toast.LENGTH_LONG).show();
            bar.setVisibility(View.GONE);
        }else {
            StringRequest stringRequest = new StringRequest(Request.Method.GET, helper.get_display_category_url() + category_id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray result = jsonObject.getJSONArray(JSON_ARRAY);
                            JSONObject stringData = result.getJSONObject(0);

                            category_id_v1 = stringData.getInt(CATEGORY_ID);
                            category_name = stringData.getString(CATEGORY_NAME);
                            status = stringData.getString(STATUS);

                            if(status.toString().trim().equals("success")){
                                et_category_name.setText(category_name + "");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        bar.setVisibility(View.GONE);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(UpdateCategory.this, "Failed in connecting the database!", Toast.LENGTH_LONG).show();
                        bar.setVisibility(View.GONE);
                    }
                }) {

            };

            RequestQueue requestQueue = Volley.newRequestQueue(UpdateCategory.this);
            requestQueue.add(stringRequest);
        }

        //set functions to buttons
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            //Showing the progress dialog
            bar.setVisibility(View.VISIBLE);

            if(!helper.IsReachable(UpdateCategory.this, helper.get_update_category_url() + category_id)){
                Toast.makeText(UpdateCategory.this, "Please check your internet connection!", Toast.LENGTH_LONG).show();
                bar.setVisibility(View.GONE);
            }else if(et_category_name.getText().toString().equals("")){
                Toast.makeText(UpdateCategory.this, "Category name is empty!", Toast.LENGTH_LONG).show();
                bar.setVisibility(View.GONE);
            }else {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, helper.get_update_category_url() + category_id,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if(response.toString().trim().equals("success")){
                                category.getData();
                                Toast.makeText(UpdateCategory.this, "Category was successfully updated!", Toast.LENGTH_LONG).show();

                                startActivity(new Intent(UpdateCategory.this, Categories.class));
                                finish();
                            }else{
                                Toast.makeText(UpdateCategory.this, "Failed in adding a category!", Toast.LENGTH_LONG).show();
                            }
                            bar.setVisibility(View.GONE);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(UpdateCategory.this, "Failed in connecting the database!", Toast.LENGTH_LONG).show();
                            bar.setVisibility(View.GONE);
                        }
                    }) {

                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("category_name", et_category_name.getText().toString().trim());

                        return params;
                    }

                };

                RequestQueue requestQueue = Volley.newRequestQueue(UpdateCategory.this);
                requestQueue.add(stringRequest);
            }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
