package com.android.nam.plt_money;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Categories extends AppCompatActivity{
    Helper helper = new Helper();
    
    public static final String CATEGORY = "category_name";

    private String category_name;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.categories);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button btn_add_category = (Button) findViewById(R.id.categoriesaddBtn);
        final EditText et_category = (EditText) findViewById(R.id.newCategory);

        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        final int user_id = prefs.getInt("user_id", 0);

        getData();

        btn_add_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            if (!helper.IsReachable(Categories.this, helper.get_add_category_url() + user_id)) {
                Toast.makeText(Categories.this, "Please check your internet connection!", Toast.LENGTH_LONG).show();
            } else {
                if (et_category.getText().toString().trim().equals("")) {
                    Toast.makeText(Categories.this, "Category is empty!", Toast.LENGTH_LONG).show();
                } else {
                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(Categories.this);

                    alertDialogBuilder.setMessage("Are you sure you want to add this as a category?");
                    alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            StringRequest stringRequest = new StringRequest(Request.Method.POST, helper.get_add_category_url() + user_id,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            Log.e("Check", response);
                                            if(response.toString().trim().equals("success")){
                                                Toast.makeText(Categories.this, "Category was successfully added!", Toast.LENGTH_LONG).show();

                                                et_category.setText("");
                                                getData();
                                            }else if(response.toString().trim().equals("duplicate")){
                                                Toast.makeText(Categories.this, "Category already exists!", Toast.LENGTH_LONG).show();
                                            }else{
                                                Toast.makeText(Categories.this, "Failed in adding a category!", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Toast.makeText(Categories.this, "Failed in connecting the database!", Toast.LENGTH_LONG).show();
                                        }
                                    }) {

                                @Override
                                protected Map<String, String> getParams() {
                                    Map<String, String> params = new HashMap<String, String>();
                                    params.put("category_name", et_category.getText().toString().trim());

                                    return params;
                                }
                            };

                            RequestQueue requestQueue = Volley.newRequestQueue(Categories.this);
                            requestQueue.add(stringRequest);
                        }
                    });
                    alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {

                        }
                    });

                    android.app.AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
            }
            }
        });
    }

    public void getData() {
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        int user_id = prefs.getInt("user_id", 0);

        //Showing the progress dialog
        final ProgressBar bar = (ProgressBar) findViewById(R.id.progressBar);
        bar.setVisibility(View.VISIBLE);

        if(!helper.IsReachable(Categories.this, helper.get_categories_url() + user_id)){
            Toast.makeText(Categories.this, "Please check your internet connection!", Toast.LENGTH_LONG).show();
            bar.setVisibility(View.GONE);
        }else {
            StringRequest stringRequest = new StringRequest(helper.get_categories_url() + user_id, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        showJSON(response);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    bar.setVisibility(View.GONE);
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(Categories.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
                            bar.setVisibility(View.GONE);
                        }
                    });

            RequestQueue requestQueue = Volley.newRequestQueue(Categories.this);
            requestQueue.add(stringRequest);
        }
    }

    public void showJSON(String json){
        ListView listView = (ListView) findViewById(R.id.categoriesLV);
        ParseCategory parse = new ParseCategory(json);
        parse.parseCategory();
        
        final CategoriesAdapter categoriesAdapter = new CategoriesAdapter(this, ParseCategory.category_id, ParseCategory.user_id, ParseCategory.category_name, ParseCategory.category_status);
        listView.setAdapter(categoriesAdapter);
    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
