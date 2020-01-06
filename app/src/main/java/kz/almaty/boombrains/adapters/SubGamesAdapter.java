package kz.almaty.boombrains.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import kz.almaty.boombrains.R;
import kz.almaty.boombrains.ui.main_pages.GamesStartActivity;
import kz.almaty.boombrains.models.main_models.SubGames;

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

        holder.inLayout.setBackgroundResource(types.getIn());
        holder.outLayout.setBackgroundResource(types.getOut());
        holder.recordTxt.setText(types.getRecord());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, GamesStartActivity.class);
            intent.putExtra("position", position);
            intent.putExtra("gameName", types.getName());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return gameList.size();
    }

    class MySubGamesViewHolder extends RecyclerView.ViewHolder {

        TextView nameTxt, recordTxt;
        ImageView gameImage;
        ConstraintLayout outLayout, inLayout;

        MySubGamesViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTxt = itemView.findViewById(R.id.gameName);
            gameImage = itemView.findViewById(R.id.gameImg);
            recordTxt = itemView.findViewById(R.id.gameRecord);
            outLayout = itemView.findViewById(R.id.outLayout);
            inLayout = itemView.findViewById(R.id.inLayout);
        }
    }
}