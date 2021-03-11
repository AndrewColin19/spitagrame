package com.spitagram.Vue;

import android.app.Activity;
import android.app.Dialog;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;

import com.spitagram.Controller.ApiController;
import com.spitagram.Modele.InstagramApi.Users.User;
import com.spitagram.R;

import java.util.ArrayList;


public class CustomPopList extends Dialog{

    private TextView title;
    private ListView listView;
    private Activity activity;
    private ApiController apiController;
    private final static String TAG = "popDialog";

    public CustomPopList(Activity activity, ApiController apiController) {
        super(activity, android.R.style.Theme_Light);
        this.activity = activity;
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.custom_pop_view);
        this.apiController = apiController;
        this.title = findViewById(R.id.title);
        this.listView = findViewById(R.id.list_item);
    }

    public void show(String title, ArrayList<User> userList, int id) {
        super.show();
        this.title.setText(title);
        CustomPopAdapter customPopAdapter = new CustomPopAdapter(this.activity, userList, id, apiController);
        listView.setAdapter(customPopAdapter);
    }
}
