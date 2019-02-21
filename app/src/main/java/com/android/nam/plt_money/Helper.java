package com.android.nam.plt_money;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class Helper {

    // Checking if there's internet connection
    public boolean IsReachable(Context context, String link_url) {

        // First, check we have any sort of connectivity
        final ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo netInfo = connMgr.getActiveNetworkInfo();
        boolean isReachable = false;

        if (netInfo != null && netInfo.isConnected()) {

            try {
                URL url = new URL(link_url);

                HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                urlc.setConnectTimeout(1000);

                return true;

            } catch (IOException e) {

            }
        }
        return isReachable;
    }

    //Get login url
    public String get_login_url(){
        return "https://social-fitness.000webhostapp.com/plt_money/login.php";
    }
    public String change_password_url(){
        return "https://social-fitness.000webhostapp.com/plt_money/change_password.php";
    }
    public String forgot_password_url(){
        return "https://social-fitness.000webhostapp.com/plt_money/check_username.php";
    }
    public String get_register_url(){
        return "https://social-fitness.000webhostapp.com/plt_money/register.php";
    }
    public String get_check_username_url(){
        return "https://social-fitness.000webhostapp.com/plt_money/check_username.php";
    }
    public String get_active_budget_url(){
        return "https://social-fitness.000webhostapp.com/plt_money/get_active_budget.php?user_id=";
    }
    public String get_active_daily_budget_url(){
        return "https://social-fitness.000webhostapp.com/plt_money/get_active_daily_budget.php?user_id=";
    }
    public String get_active_weekly_budget_url(){
        return "https://social-fitness.000webhostapp.com/plt_money/get_active_weekly_budget.php?user_id=";
    }
    public String get_active_monthly_budget_url(){
        return "https://social-fitness.000webhostapp.com/plt_money/get_active_monthly_budget.php?user_id=";
    }
    public String get_add_budget_url(){
        return "https://social-fitness.000webhostapp.com/plt_money/add_budget.php?user_id=";
    }
    public String get_add_expense_url(){
        return "https://social-fitness.000webhostapp.com/plt_money/add_expense.php?budget_id=";
    }
    public String get_add_category_url(){
        return "https://social-fitness.000webhostapp.com/plt_money/add_category.php?user_id=";
    }
    public String get_categories_url(){
        return "https://social-fitness.000webhostapp.com/plt_money/get_categories.php?user_id=";
    }
    public String get_delete_category_url(){
        return "https://social-fitness.000webhostapp.com/plt_money/delete_category.php?category_id=";
    }
    public String get_update_category_url(){
        return "https://social-fitness.000webhostapp.com/plt_money/update_category.php?category_id=";
    }
    public String get_display_category_url(){
        return "https://social-fitness.000webhostapp.com/plt_money/get_category_by_id.php?category_id=";
    }
    public String get_display_budget_url(){
        return "https://social-fitness.000webhostapp.com/plt_money/get_budget_by_id.php?budget_id=";
    }
    public String get_expenses_url(){
        return "https://social-fitness.000webhostapp.com/plt_money/get_expenses.php?budget_id=";
    }
}
