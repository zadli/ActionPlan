package ru.zadli.action_plan.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import ru.zadli.action_plan.R;
import ru.zadli.action_plan.adapters.MainRecyclerViewAdapter;

import static com.android.volley.Request.Method.GET;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ProgressBar main_viewpager_progressBar = findViewById(R.id.main_viewpager_progressBar);
        RecyclerView main_recyclerview = findViewById(R.id.main_recyclerview);
        ViewPager2 main_viewpager = findViewById(R.id.main_viewpager);
        Volley.newRequestQueue(this).add(new JsonArrayRequest(GET,
                "https://api.zadli.me/actionplan/",
                null,
                response -> {
                    MainRecyclerViewAdapter main_viewpager_adapter = new MainRecyclerViewAdapter(MainActivity.this, response, main_viewpager, main_recyclerview);
                    main_viewpager_progressBar.setVisibility(View.GONE);
                    main_recyclerview.setLayoutManager(new LinearLayoutManager(this));
                    main_recyclerview.setAdapter(main_viewpager_adapter);
                }, error -> {

        }));
    }
}