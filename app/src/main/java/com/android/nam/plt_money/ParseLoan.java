package com.android.nam.plt_money;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ParseLoan {
    public static String[] loan_id;
    public static String[] user_id;
    public static String[] loan_borrower;
    public static String[] loan_date_borrowed;
    public static String[] loan_due_date;
    public static String[] loan_amount;
    public static String[] loan_status;

    public static final String JSON_ARRAY = "result";
    public static final String LOAN_ID = "loan_id";
    public static final String USER_ID = "user_id";
    public static final String LOAN_BORROWER = "loan_borrower";
    public static final String LOAN_DATE_BORROWED = "loan_date_borrowed";
    public static final String LOAN_DUE_DATE = "loan_due_date";
    public static final String LOAN_AMOUNT = "loan_amount";
    public static final String LOAN_STATUS = "loan_status";

    private JSONArray loan = null;

    private String json;

    public ParseLoan(String json){
        this.json = json;
    }

    protected void parseLoan(){
        JSONObject jsonObject=null;
        try {
            jsonObject = new JSONObject(json);
            loan = jsonObject.getJSONArray(JSON_ARRAY);

            loan_id = new String[loan.length()];
            user_id = new String[loan.length()];
            loan_borrower = new String[loan.length()];
            loan_date_borrowed = new String[loan.length()];
            loan_due_date = new String[loan.length()];
            loan_amount = new String[loan.length()];
            loan_status = new String[loan.length()];

            for(int i=0;i<loan.length();i++){
                JSONObject jo = loan.getJSONObject(i);
                loan_id[i] = jo.getString(LOAN_ID);
                user_id[i] = jo.getString(USER_ID);
                loan_borrower[i] = jo.getString(LOAN_BORROWER);
                loan_date_borrowed[i] = jo.getString(LOAN_DATE_BORROWED);
                loan_due_date[i] = jo.getString(LOAN_DUE_DATE);
                loan_amount[i] = jo.getString(LOAN_AMOUNT);
                loan_status[i] = jo.getString(LOAN_STATUS);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}