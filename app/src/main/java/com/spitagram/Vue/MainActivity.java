package com.spitagram.Vue;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import com.spitagram.Controller.ApiController;
import com.spitagram.R;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private TextView username, follow, followers,
            textCardNewFollowers, textCardLoseFollowers, textCardFollow, textCardMutual;
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.activity = this;
        //user test {wes.off__, pepperlapinebelier, marieamelie.wen, annickd06}

        username = findViewById(R.id.userName);
        follow = findViewById(R.id.userFollow);
        followers = findViewById(R.id.userFollowers);
        textCardLoseFollowers = findViewById(R.id.textCardLoseFollowers);
        textCardFollow = findViewById(R.id.textCardFollow);
        textCardNewFollowers = findViewById(R.id.textCardNewFollowers);
        textCardMutual = findViewById(R.id.textCardMutual);

        ApiController apiController = new ApiController(this);
        apiController.init();

        ExecutorService pool = Executors.newSingleThreadExecutor();
        pool.submit(new Runnable() {
            @Override
            public void run() {
                while(ApiController.currentUser == null){
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                drawData();
            }
        });
    }
    public void drawData(){
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (ApiController.currentUser != null){
                    username.setText("username : " + ApiController.currentUser.getUserName());
                    follow.setText("follow : " + ApiController.currentUser.getFollow());
                    followers.setText("followers : "+ ApiController.currentUser.getFollowers());
                }
            }
        });
    }
}