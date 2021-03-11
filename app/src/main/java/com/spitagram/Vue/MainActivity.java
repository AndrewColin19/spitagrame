package com.spitagram.Vue;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.spitagram.Controller.ApiController;
import com.spitagram.Modele.AppConfig;
import com.spitagram.Modele.InstagramApi.InstagramApp;
import com.spitagram.Modele.InstagramApi.Users.User;
import com.spitagram.R;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "ActivityMain";
    private TextView username, follow, followers,
            textCardNewFollowers, textCardLoseFollowers, textCardFollow, textCardMutual;
    private ImageView imageView;
    private ApiController apiController;
    private RelativeLayout cardNewFollowers, cardLoseFollowers, cardFollow, cardMutual;
    private ProgressBar progressBar;
    private CustomPopList customPopList;
    private RelativeLayout content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //user test {wes.off__, pepperlapinebelier, marieamelie.wen, annickd06}

        this.findView();

        apiController = new ApiController(this);

        this.customPopList = new CustomPopList(this, apiController);
        addCardOnListenner();

        /*content = findViewById(R.id.content);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        Browser browser = apiController.instagramApp.browser;
        browser.setLayoutParams(params);
        content.addView(browser);*/

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
                if (AppConfig.DEBUG){
                    ApiController. currentUser.getWinFollowersList().add(new User(1, "pepito_enerver_qui_a_un_gros_pseudo"));
                    ApiController.currentUser.getLoseFollowersList().add(new User(1, "pepito1"));
                    ApiController.currentUser.getLoseFollowersList().add(new User(1, "pepito2"));

                    apiController.currentUser.getMutualList().add(new User(1, "pepito4"));
                    apiController.currentUser.getMutualList().add(new User(1, "pepito5"));
                }
                displayData();
                incrementProgressBar(20);
                while (ApiController.stats != true) {
                    try {
                        Thread.sleep(200);
                        incrementProgressBar(20 + (int) (InstagramApp.progressPurcent * 0.7));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                incrementProgressBar(100);
                displayStats();
            }
        });

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && ApiController.stats == true){
            displayStats();
        }
    }

    private void displayData(){
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (ApiController.currentUser != null){
                    username.setText("username : " + ApiController.currentUser.getUserName());
                    follow.setText("follow : " + ApiController.currentUser.getNbFollow());
                    followers.setText("followers : "+ ApiController.currentUser.getNbFollowers());
                    imageView.setImageBitmap(ApiController.currentUser.getUserImg());
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

    private View.OnClickListener clickCard = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(ApiController.stats == true && ApiController.currentUser != null || AppConfig.DEBUG == true){
                switch (v.getId()){
                    case R.id.cardNewFollowers:
                        Log.d(TAG, "onClick: card new followers");
                        customPopList.show("New Followers", ApiController.currentUser.getWinFollowersList(), R.id.cardNewFollowers);
                        break;
                    case R.id.cardLoseFollowers:
                        Log.d(TAG, "onClick: card lose followers");
                        customPopList.show("Lose followers", ApiController.currentUser.getLoseFollowersList(), R.id.cardLoseFollowers);
                        break;
                    case R.id.cardFollow:
                        Log.d(TAG, "onClick: card follow");
                        customPopList.show("New Follow", ApiController.currentUser.getNewFollowList(), R.id.cardFollow);
                        break;
                    case R.id.cardMutual:
                        Log.d(TAG, "onClick: card mutual");
                        customPopList.show("Mutual", ApiController.currentUser.getMutualList(), R.id.cardMutual);
                        break;
                }
            }
        }
    };

    private void addCardOnListenner(){
        cardNewFollowers.setOnClickListener(clickCard);
        cardLoseFollowers.setOnClickListener(clickCard);
        cardFollow.setOnClickListener(clickCard);
        cardMutual.setOnClickListener(clickCard);
    }

    private void findView(){
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
        imageView = findViewById(R.id.imageUser);
        progressBar = findViewById(R.id.progressBar);
    }
    public void incrementProgressBar(final int progress){
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setProgress(progress);
            }
        });
    }
}