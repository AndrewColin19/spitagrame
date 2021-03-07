package com.spitagram.Vue;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.spitagram.Controller.ApiController;
import com.spitagram.Modele.AppConfig;
import com.spitagram.R;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "ActivityMain";
    private TextView username, follow, followers,
            textCardNewFollowers, textCardLoseFollowers, textCardFollow, textCardMutual;
    private ApiController apiController;
    private RelativeLayout cardNewFollowers, cardLoseFollowers, cardFollow, cardMutual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //user test {wes.off__, pepperlapinebelier, marieamelie.wen, annickd06}

        username = findViewById(R.id.userName);
        follow = findViewById(R.id.userFollow);
        followers = findViewById(R.id.userFollowers);
        textCardLoseFollowers = findViewById(R.id.textCardLoseFollowers);
        textCardFollow = findViewById(R.id.textCardFollow);
        textCardNewFollowers = findViewById(R.id.textCardNewFollowers);
        textCardMutual = findViewById(R.id.textCardMutual);
        cardNewFollowers = findViewById(R.id.cardNewFollowers);
        cardLoseFollowers = findViewById(R.id.cardLoseFollowers);
        cardFollow = findViewById(R.id.cardFollow);
        cardMutual = findViewById(R.id.cardMutual);

        addCardOnListenner();

        if (AppConfig.DEBUG){

        }else {
            apiController = new ApiController(this);
            apiController.init();

            ExecutorService pool = Executors.newSingleThreadExecutor();
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    while (ApiController.currentUser == null) {
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    displayData();
                    while (ApiController.stats != true) {
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    displayStats();
                }
            });
        }
    }
    private void displayData(){
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
    private void displayStats(){
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int[] stats = apiController.getStats();
                textCardNewFollowers.setText(String.valueOf(stats[0]));
                textCardLoseFollowers.setText(String.valueOf(stats[1]));
                textCardFollow.setText(String.valueOf(stats[2]));
                textCardMutual.setText(String.valueOf(stats[3]));
            }
        });
    }
    private Runnable display = new Runnable() {
        @Override
        public void run() {
            displayStats();
        }
    };

    private View.OnClickListener clickCard = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.cardNewFollowers:
                    Log.d(TAG, "onClick: card new followers");
                    break;
                case R.id.cardLoseFollowers:
                    Log.d(TAG, "onClick: card lose followers");
                    break;
                case R.id.cardFollow:
                    Log.d(TAG, "onClick: card follow");
                    break;
                case R.id.cardMutual:
                    Log.d(TAG, "onClick: card mutual");
                    break;
            }
        }
    };
    private void addCardOnListenner(){
        cardNewFollowers.setOnClickListener(clickCard);
        cardLoseFollowers.setOnClickListener(clickCard);
        cardFollow.setOnClickListener(clickCard);
        cardMutual.setOnClickListener(clickCard);
    }
}