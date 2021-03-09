package ru.zadli.action_plan.activities;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import ru.zadli.action_plan.R;
import ru.zadli.action_plan.adapters.MainViewPagerAdapter;

import static com.android.volley.Request.Method.GET;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ProgressBar main_viewpager_progressBar = findViewById(R.id.main_viewpager_progressBar);
        ViewPager2 main_viewpager = findViewById(R.id.main_viewpager);
        Volley.newRequestQueue(this).add(new JsonArrayRequest(GET,
                "https://api.zadli.me/actionplan/",
                null,
                response -> {
                    MainViewPagerAdapter main_viewpager_adapter = new MainViewPagerAdapter(MainActivity.this, response);
                    main_viewpager_progressBar.setVisibility(View.GONE);
                    main_viewpager.setAdapter(main_viewpager_adapter);
                }, error -> {

        }));
    }
}