package kz.almaty.boombrains.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import kz.almaty.boombrains.R;
import kz.almaty.boombrains.main_pages.GamesStartActivity;
import kz.almaty.boombrains.models.SubGames;

public class SubGamesAdapter extends RecyclerView.Adapter<SubGamesAdapter.MySubGamesViewHolder> {

    private List<SubGames> gameList;
    private Context context;

    public SubGamesAdapter(List<SubGames> gameList, Context context) {
        this.gameList = gameList;
        this.context = context;
    }

    @NonNull
    @Override
    public MySubGamesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sub_layout, parent, false);
        return new MySubGamesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MySubGamesViewHolder holder, int position) {
        SubGames types = gameList.get(position);

        holder.nameTxt.setText(types.getName());
        holder.gameImage.setImageResource(types.getImage());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, GamesStartActivity.class);
            intent.putExtra("position", position);
            intent.putExtra("gameName", types.getName());
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });

        setColors(holder.nameTxt, position);
    }

    private void setColors(TextView view, int index) {
        switch (index) {
            case 0:
                view.setTextColor(Color.parseColor("#603A9E"));
                break;
            case 1: case 2:
                view.setTextColor(Color.parseColor("#EB9500"));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return gameList.size();
    }

    class MySubGamesViewHolder extends RecyclerView.ViewHolder {

        TextView nameTxt;
        ImageView gameImage;

        MySubGamesViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTxt = itemView.findViewById(R.id.gameName);
            gameImage = itemView.findViewById(R.id.gameImage);
        }
    }
}