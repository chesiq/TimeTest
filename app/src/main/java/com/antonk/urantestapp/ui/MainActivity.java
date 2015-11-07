package com.antonk.urantestapp.ui;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.antonk.urantestapp.ApiMethods;
import com.antonk.urantestapp.R;
import com.antonk.urantestapp.database.DatabaseOpenHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity {

    private static final String API_URL = "http://android-logs.uran.in.ua";

    private TextView mTextReceivedData;
    private RecyclerView mListPrevious;
    private TimesAdapter mTimesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        loadDataFromDatabase();
    }

    private void initViews(){
        View mButtonChangeBackground = findViewById(R.id.button_change_background);
        mButtonChangeBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateRandomColor();
            }
        });

        View mButtonLoadData = findViewById(R.id.button_load_data);
        mButtonLoadData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCurrentTime();
            }
        });

        mTextReceivedData = (TextView) findViewById(R.id.text_received_data);
        mListPrevious = (RecyclerView) findViewById(R.id.list_previous);
        mListPrevious.setLayoutManager(new LinearLayoutManager(this));
    }

    private void generateRandomColor(){
        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        getWindow().getDecorView().setBackgroundColor(color);
    }

    private void getCurrentTime(){
        final Context context = this;
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(API_URL)
                .build();
        ApiMethods methods = restAdapter.create(ApiMethods.class);
        Callback callback = new Callback() {
            @Override
            public void success(Object o, Response response) {
                try {
                    long currentTime = Long.parseLong(o.toString()) * 1000; // in millis
                    Calendar c = Calendar.getInstance();
                    c.setTimeInMillis(currentTime);

                    //Format date
                    String formattedDate = getFormattedDate(c.getTime());
                    mTextReceivedData.setText(formattedDate);

                    //Add time to list
                    mTimesAdapter.add(formattedDate);
                    mListPrevious.scrollToPosition(0);

                    //Save data to database
                    DatabaseOpenHelper.getInstance(context).add(formattedDate);

                }
                catch (NumberFormatException e){
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                Toast.makeText(context, retrofitError.toString(), Toast.LENGTH_LONG).show();
            }
        };

        //Get time from server
        methods.getCurrentTime(callback);
    }

    private String getFormattedDate(Date date){
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        return format.format(date);
    }

    private void loadDataFromDatabase(){
        List<String> data = DatabaseOpenHelper.getInstance(this).getData();
        Collections.reverse(data);
        updateAdapter(data);
    }

    private void updateAdapter(List<String> data){
        mTimesAdapter = new TimesAdapter(data);
        mListPrevious.setAdapter(mTimesAdapter);
        mTimesAdapter.notifyDataSetChanged();
    }
}
