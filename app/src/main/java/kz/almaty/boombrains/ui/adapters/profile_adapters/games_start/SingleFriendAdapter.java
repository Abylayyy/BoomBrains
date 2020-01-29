package kz.almaty.boombrains.ui.adapters.profile_adapters.games_start;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import kz.almaty.boombrains.R;
import kz.almaty.boombrains.util.helpers.SharedPrefManager;
import kz.almaty.boombrains.data.models.rating_model.WorldRecord;

public class SingleFriendAdapter extends RecyclerView.Adapter<SingleFriendAdapter.FriendsViewHolder> {

    private Context context;
    private List<WorldRecord> list;
    private SingleFriendsListener listener;

    public SingleFriendAdapter(Context context, List<WorldRecord> list, SingleFriendsListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SingleFriendAdapter.FriendsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.world_rating, parent, false);
        return new FriendsViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull SingleFriendAdapter.FriendsViewHolder holder, int position) {
        WorldRecord record = list.get(position);

        listener.setFriendSize(holder.itemView);

        holder.nameTxt.setText(record.getPosition() + ". " + record.getUsername());
        holder.recordTxt.setText("" + record.getRecord());

        if (position < 4) {
            if (record.isMe()) {
                boldFeatures(holder.nameTxt, holder.recordTxt, holder.recordImg);
                holder.itemView.setEnabled(false);
            }
        } else {
            normFeatures(holder.nameTxt, holder.recordTxt, holder.recordImg);
        }

        holder.itemView.setOnClickListener(v -> {
            if (SharedPrefManager.isNetworkOnline(context)) {
                listener.setFriendClicked(record);
            }
        });
    }

    private void boldFeatures(TextView text, TextView text2, ImageView img) {
        int color = context.getResources().getColor(R.color.profBoldColors);
        text.setTypeface(text.getTypeface(), Typeface.BOLD);
        text2.setTypeface(text2.getTypeface(), Typeface.BOLD);
        text.setTextColor(color);text2.setTextColor(color);
        img.setColorFilter(color);
    }

    private void normFeatures(TextView text, TextView text2, ImageView img) {
        int color = context.getResources().getColor(R.color.friendRatings);
        text.setTypeface(text.getTypeface(), Typeface.NORMAL);
        text2.setTypeface(text2.getTypeface(), Typeface.NORMAL);
        text.setTextColor(color);text2.setTextColor(color);
        img.setColorFilter(color);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class FriendsViewHolder extends RecyclerView.ViewHolder {

        TextView nameTxt, recordTxt;
        ImageView recordImg;

        FriendsViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTxt = itemView.findViewById(R.id.friendName);
            recordTxt = itemView.findViewById(R.id.friendRecord);
            recordImg = itemView.findViewById(R.id.friendImg);
        }
    }

    public interface SingleFriendsListener {
        void setFriendSize(View view);
        void setFriendClicked(WorldRecord record);
    }
}
