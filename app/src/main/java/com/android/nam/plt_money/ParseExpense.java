package com.android.nam.plt_money;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ParseExpense {
    public static String[] expense_id;
    public static String[] expense_name;
    public static String[] budget_id;
    public static String[] category_name;
    public static String[] expense_date;
    public static String[] expense_amount;

    public static final String JSON_ARRAY = "result";
    public static final String EXPENSE_ID = "expense_id";
    public static final String EXPENSE_NAME = "expense_name";
    public static final String BUDGET_ID = "budget_id";
    public static final String CATEGORY_NAME = "category_name";
    public static final String EXPENSE_DATE = "expense_date";
    public static final String EXPENSE_AMOUNT = "expense_amount";

    private JSONArray expense = null;

    private String json;

    public ParseExpense(String json){
        this.json = json;
    }

    protected void parseExpense(){
        JSONObject jsonObject=null;
        try {
            jsonObject = new JSONObject(json);
            expense = jsonObject.getJSONArray(JSON_ARRAY);

            expense_id = new String[expense.length()];
            expense_name = new String[expense.length()];
            budget_id = new String[expense.length()];
            category_name = new String[expense.length()];
            expense_date = new String[expense.length()];
            expense_amount = new String[expense.length()];

            for(int i=0;i<expense.length();i++){
                JSONObject jo = expense.getJSONObject(i);
                expense_id[i] = jo.getString(EXPENSE_ID);
                expense_name[i] = jo.getString(EXPENSE_NAME);
                budget_id[i] = jo.getString(BUDGET_ID);
                category_name[i] = jo.getString(CATEGORY_NAME);
                expense_date[i] = jo.getString(EXPENSE_DATE);
                expense_amount[i] = jo.getString(EXPENSE_AMOUNT);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}