package ru.zadli.action_plan.adapters;

import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import ru.zadli.action_plan.R;

public class MainViewPagerAdapter extends RecyclerView.Adapter<MainViewPagerAdapter.ViewHolder> {

    Context context;
    JSONArray response;

    public MainViewPagerAdapter(Context context, JSONArray response) {
        this.context = context;
        this.response = response;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.viewpager_main, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            Picasso.with(context).load(response.getJSONObject(position).getString("picture")).into(holder.main_image);
            holder.main_name.setText(response.getJSONObject(position).getString("name"));
            holder.main_description.setText(response.getJSONObject(position).getString("description"));
            holder.main_route.setText(response.getJSONObject(position).getString("route"));
            holder.main_time.setText(response.getJSONObject(position).getString("time"));
            holder.main_rating.setRating((float) response.getJSONObject(position).getDouble("rating"));
            switch (response.getJSONObject(position).getString("type")){
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
        ImageView main_image;
        TextView main_name;
        TextView main_description;
        TextView main_route;
        TextView main_type;
        TextView main_time;
        CardView main_cardview;
        RatingBar main_rating;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            main_image = itemView.findViewById(R.id.main_image);
            main_name = itemView.findViewById(R.id.main_name);
            main_description = itemView.findViewById(R.id.main_description);
            main_route = itemView.findViewById(R.id.main_route);
            main_type = itemView.findViewById(R.id.main_type);
            main_time = itemView.findViewById(R.id.main_time);
            main_cardview = itemView.findViewById(R.id.main_cardwiev);
            main_rating = itemView.findViewById(R.id.main_rating);
            main_rating.setNumStars(5);

            main_route.setOnClickListener(v -> {
                try {
                    context.startActivity(
                            new Intent(Intent.ACTION_VIEW,
                                    Uri.parse("http://maps.google.com/maps?daddr="+
                                            response.getJSONObject(getAdapterPosition())
                                                    .getString("route_cords"))));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });
            main_cardview.setOnClickListener(v -> {
                ObjectAnimator animationX = ObjectAnimator.ofFloat(itemView, "ScaleX", 0.85f);
                ObjectAnimator animationY = ObjectAnimator.ofFloat(itemView, "ScaleY", 0.85f);
                animationX.setDuration(300);
                animationY.setDuration(300);
                animationX.start();
                animationY.start();
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                try {
                    builder.setTitle("Комментарии")
                            .setMessage(response.getJSONObject(getAdapterPosition()).getString("comments"))
                            .setPositiveButton("Назад", (dialog, id) -> dialog.cancel())
                            .setOnCancelListener(dialog -> {
                                ObjectAnimator animationXX = ObjectAnimator.ofFloat(itemView, "ScaleX", 1f);
                                ObjectAnimator animationYY = ObjectAnimator.ofFloat(itemView, "ScaleY", 1f);
                                animationXX.setDuration(300);
                                animationYY.setDuration(300);
                                animationXX.start();
                                animationYY.start();
                            });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                builder.create().show();
            });

            main_cardview.setOnLongClickListener(v -> {
                return true;
            });
        }
    }
}
