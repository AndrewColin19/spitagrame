package com.spitagram.Vue;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.spitagram.Controller.ApiController;
import com.spitagram.Modele.InstagramApi.Users.User;
import com.spitagram.R;

import java.util.ArrayList;

public class CustomPopAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<User> userList;
    private LayoutInflater inflater;
    private int id;
    private ApiController apiController;
    private static final String ACTION_BTN_FOLLOW = "suivre";
    private static final String ACTION_BTN_UNFOLLOW = "supp";

    public CustomPopAdapter(Activity activity, ArrayList<User> userList, int id, ApiController apiController){
        this.activity = activity;
        this.userList = userList;
        this.inflater = LayoutInflater.from(this.activity);
        this.id = id;
        this.apiController = apiController;
    }

    @Override
    public int getCount() {
        return userList.size();
    }

    @Override
    public User getItem(int position) {
        return userList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        view = inflater.inflate(R.layout.custom_pop_adapter,null);
        TextView username = view.findViewById(R.id.userName);
        ImageView imageView = view.findViewById(R.id.userImg);
        Button btnAction = view.findViewById(R.id.btn_action);

        username.setText(getItem(position).getUserName());
        imageView.setImageBitmap(getItem(position).getProfilImg());
        if (id == R.id.cardNewFollowers){
            //follow back
            btnAction.setTag(getItem(position).getId());
            btnAction.setText(ACTION_BTN_FOLLOW);
            btnAction.setOnClickListener(listenerFollow);
        }
        if (id == R.id.cardLoseFollowers || id == R.id.cardFollow){
            btnAction.setTag(getItem(position).getId());
            btnAction.setText(ACTION_BTN_UNFOLLOW);
            btnAction.setOnClickListener(listenerUnFollow);
        }
        if(id == R.id.cardMutual){
            btnAction.setAlpha((float) 0);
        }
        return view;
    }

    private View.OnClickListener listenerUnFollow = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Button b = (Button) v;
            apiController.unfollowUser((long) b.getTag(), id);
            v.setAlpha((float) 0);
            notifyDataSetChanged();
        }
    };

    private View.OnClickListener listenerFollow = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Button b = (Button) v;
            apiController.followUser((long) b.getTag());
            v.setAlpha((float) 0);
            notifyDataSetChanged();
        }
    };
}
