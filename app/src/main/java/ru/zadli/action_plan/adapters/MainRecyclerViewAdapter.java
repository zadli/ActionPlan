package ru.zadli.action_plan.adapters;

import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import ru.zadli.action_plan.R;
import ru.zadli.action_plan.activities.MainActivity;

import static com.android.volley.Request.Method.GET;

public class MainRecyclerViewAdapter extends RecyclerView.Adapter<MainRecyclerViewAdapter.ViewHolder> {

    Context context;
    JSONArray response;
    ViewPager2 main_viewpager;
    RecyclerView main_recyclerview;

    public MainRecyclerViewAdapter(Context context, JSONArray response, ViewPager2 main_viewpager, RecyclerView main_recyclerview) {
        this.context = context;
        this.response = response;
        this.main_viewpager = main_viewpager;
        this.main_recyclerview = main_recyclerview;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_main, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            holder.main_name.setText(response.getJSONObject(position).getString("name"));
            holder.main_rating.setRating((float) response.getJSONObject(position).getDouble("rating"));
            switch (response.getJSONObject(position).getString("type")) {
                case "museum":
                    holder.main_type.setText("Музей");
                    break;
                case "park":
                    holder.main_type.setText("Парк");
                    break;
                case "nice place":
                    holder.main_type.setText("Красивое место");
                    break;
                case "monument":
                    holder.main_type.setText("Памятник");
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return response.length();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView main_name;
        TextView main_type;
        RatingBar main_rating;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            main_name = itemView.findViewById(R.id.main_recycler_name);
            main_type = itemView.findViewById(R.id.main_recycler_type);
            main_rating = itemView.findViewById(R.id.main_recycler_rating);
            main_rating.setNumStars(5);

            itemView.setOnClickListener(v -> {
                MainPagerViewAdapter main_viewpager_adapter = null;
                try {
                    main_viewpager_adapter = new MainPagerViewAdapter(context, response.getJSONObject(getAdapterPosition()), getAdapterPosition());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                main_recyclerview.setVisibility(View.GONE);
                main_viewpager.setAdapter(main_viewpager_adapter);
            });
        }
    }
}
