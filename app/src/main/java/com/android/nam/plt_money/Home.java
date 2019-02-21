package com.android.nam.plt_money;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
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

import java.text.DecimalFormat;

import static android.content.Context.MODE_PRIVATE;

public class Home extends Fragment{
    View view;
    String[] date_category = new String[]{"Daily", "Weekly", "Monthly"};

    public static final String JSON_ARRAY = "result";

    public static final String BUDGET_ID = "budget_id";
    public static final String USER_ID = "user_id";
    public static final String BUDGET_AMOUNT = "budget_amount";
    public static final String BUDGET_CATEGORY = "budget_category";
    public static final String BUDGET_STATUS = "budget_status";
    public static final String STATUS = "status";

    private String budget_id;
    private String userid;
    private String budget_amount;
    private String budget_category;
    private String budget_status;
    private String status;

    Helper helper = new Helper();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.homepage, container, false);

        Button btn_edit_budget = (Button) view.findViewById(R.id.editBudgetBtn);

        Spinner sp_date = (Spinner) view.findViewById(R.id.txtDate);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, date_category);
        sp_date.setAdapter(adapter);

        btn_edit_budget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), UpdateBudget.class);
                intent.putExtra("budget_id", Integer.parseInt(budget_id));
                startActivity(intent);
            }
        });

        sp_date.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    getBudget("Daily");
                }else if (position == 1){
                    getBudget("Weekly");
                }else{
                    getBudget("Monthly");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return view;
    }

    private void getBudget(String date_category){
        SharedPreferences prefs = getActivity().getSharedPreferences("MyPrefs", MODE_PRIVATE);
        final int user_id = prefs.getInt("user_id", 0);
        String url = "";

        if(date_category == "Daily"){
            url = helper.get_active_daily_budget_url();
        }else if(date_category == "Weekly"){
            url = helper.get_active_weekly_budget_url();
        }else{
            url = helper.get_active_monthly_budget_url();
        }

        //Showing the progress dialog
        final ProgressBar bar = (ProgressBar) view.findViewById(R.id.progressBar);
        bar.setVisibility(View.VISIBLE);

        //SET DISPLAY FOR BUDGET DETAILS
        if(!helper.IsReachable(getActivity(), url + user_id)){
            Toast.makeText(getActivity(), "Please check your internet connection!", Toast.LENGTH_LONG).show();
            bar.setVisibility(View.GONE);
        }else {
            StringRequest stringRequest = new StringRequest(Request.Method.GET, "" + url + user_id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray result = jsonObject.getJSONArray(JSON_ARRAY);
                            JSONObject stringData = result.getJSONObject(0);

                            budget_id = stringData.getString(BUDGET_ID);
                            userid = stringData.getString(USER_ID);
                            budget_amount = stringData.getString(BUDGET_AMOUNT);
                            budget_category = stringData.getString(BUDGET_CATEGORY);
                            budget_status = stringData.getString(BUDGET_STATUS);
                            status = stringData.getString(STATUS);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        try{
                            SharedPreferences settings = getActivity().getSharedPreferences("MyPrefs", MODE_PRIVATE);
                            SharedPreferences.Editor prefEditor = settings.edit();

                            prefEditor.remove("budget_id");
                            prefEditor.commit();

                            prefEditor.putInt("budget_id", Integer.parseInt(budget_id));
                            prefEditor.commit();

                            TextView tv_budget = (TextView) view.findViewById(R.id.txtBudget);
                            DecimalFormat df = new DecimalFormat("#,#00.0#");

                            getData();

                            tv_budget.setText(df.format(Double.parseDouble(budget_amount)));

                            bar.setVisibility(View.GONE);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        bar.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), "Error in connecting the server!", Toast.LENGTH_LONG).show();
                    }
                }) {
            };

            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            requestQueue.add(stringRequest);
        }
    }

    private void getData() {
        SharedPreferences prefs = getActivity().getSharedPreferences("MyPrefs", MODE_PRIVATE);
        int budget_id = prefs.getInt("budget_id", 0);

        if(!helper.IsReachable(getActivity(), helper.get_expenses_url() + budget_id)){
            Toast.makeText(getActivity(), "Please check your internet connection!", Toast.LENGTH_LONG).show();
        }else {
            StringRequest stringRequest = new StringRequest(helper.get_expenses_url() + budget_id, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        showJSON(response);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getActivity(), error.getMessage().toString(), Toast.LENGTH_LONG).show();
                        }
                    });

            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            requestQueue.add(stringRequest);
        }
    }

    private void showJSON(String json){
        ListView listView = (ListView) view.findViewById(R.id.expense_listview);
        ParseExpense parse = new ParseExpense(json);
        parse.parseExpense();

        ExpenseAdapter expenseAdapter = new ExpenseAdapter(getActivity(), ParseExpense.expense_id, ParseExpense.budget_id, ParseExpense.category_name, ParseExpense.expense_date, ParseExpense.expense_amount);
        listView.setAdapter(expenseAdapter);
    }
}