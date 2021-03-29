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
import org.json.JSONObject;

import ru.zadli.action_plan.R;

import static com.android.volley.Request.Method.GET;

public class MainPagerViewAdapter extends RecyclerView.Adapter<MainPagerViewAdapter.ViewHolder> {

    Context context;
    JSONObject response;
    int pos;

    public MainPagerViewAdapter(Context context, JSONObject response, int pos) {
        this.context = context;
        this.response = response;
        this.pos = pos;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.viewpager_main, parent, false));
    }

    @Override
    public int getItemCount() {
        return response.length();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            Picasso.with(context).load(response.getString("picture")).into(holder.main_image);
            holder.main_name.setText(response.getString("name"));
            holder.main_description.setText(response.getString("description"));
            holder.main_route.setText(response.getString("route"));
            holder.main_time.setText(response.getString("time"));
            holder.main_rating.setRating((float) response.getDouble("rating"));
            switch (response.getString("type")){
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView main_image;
        TextView main_name;
        TextView main_description;
        TextView main_route;
        TextView main_type;
        TextView main_time;
        CardView main_cardview;
        RatingBar main_rating;
        Button main_rating_button;
        public ViewHolder(@NonNull  View itemView) {
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
            main_rating_button = itemView.findViewById(R.id.main_rating_button);

            main_route.setOnClickListener(v -> {
                try {
                    context.startActivity(
                            new Intent(Intent.ACTION_VIEW,
                                    Uri.parse("http://maps.google.com/maps?daddr="+
                                            response.getString("route_cords"))));
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
                            .setMessage(response.getString("comments"))
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

            main_rating_button.setOnClickListener(v -> {
                ObjectAnimator animationX = ObjectAnimator.ofFloat(itemView, "ScaleX", 0.85f);
                ObjectAnimator animationY = ObjectAnimator.ofFloat(itemView, "ScaleY", 0.85f);
                animationX.setDuration(300);
                animationY.setDuration(300);
                animationX.start();
                animationY.start();
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Оставьте комментарий")
                        .setOnCancelListener(dialog -> {
                            ObjectAnimator animationXX = ObjectAnimator.ofFloat(itemView, "ScaleX", 1f);
                            ObjectAnimator animationYY = ObjectAnimator.ofFloat(itemView, "ScaleY", 1f);
                            animationXX.setDuration(300);
                            animationYY.setDuration(300);
                            animationXX.start();
                            animationYY.start();
                        });
                final EditText input = new EditText(context);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                builder.setView(input);
                builder.setPositiveButton("Отправить", (dialog, which) -> {
                    try {
                        Volley.newRequestQueue(context).add(new JsonObjectRequest(GET,
                                "https://api.zadli.me/actionplan/add_comment.php?id="
                                        +response.getInt("id")
                                        +"&comment="
                                        +input.getText(),
                                null,
                                response -> {
                                    Toast.makeText(context,"Отправленно",Toast.LENGTH_SHORT).show();
                                }, error -> {
                            Toast.makeText(context,"Ошибка\n" + error,Toast.LENGTH_SHORT).show();
                        }));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    ObjectAnimator animationXX = ObjectAnimator.ofFloat(itemView, "ScaleX", 1f);
                    ObjectAnimator animationYY = ObjectAnimator.ofFloat(itemView, "ScaleY", 1f);
                    animationXX.setDuration(300);
                    animationYY.setDuration(300);
                    animationXX.start();
                    animationYY.start();
                });
                builder.create().show();
            });
        }
    }
}
