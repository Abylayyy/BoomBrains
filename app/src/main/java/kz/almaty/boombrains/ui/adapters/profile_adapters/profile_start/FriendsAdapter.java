package kz.almaty.boombrains.ui.adapters.profile_adapters.profile_start;

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
import kz.almaty.boombrains.util.helpers.preference.SharedPrefManager;
import kz.almaty.boombrains.data.models.profile_model.ProfileWorldRecord;

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.FriendsViewHolder> {

    private Context context;
    private List<ProfileWorldRecord> list;
    private FriendsListener listener;

    public FriendsAdapter(Context context, List<ProfileWorldRecord> list, FriendsListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FriendsAdapter.FriendsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.world_rating, parent, false);
        return new FriendsViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull FriendsAdapter.FriendsViewHolder holder, int position) {
        ProfileWorldRecord record = list.get(position);

        listener.setFriendSize(holder.itemView);

        holder.nameTxt.setText(record.getPosition() + ". " + record.getUsername());
        holder.recordTxt.setText("" + record.getTotalRecord());

        if (position < 5) {
            if (record.isMe()) {
                boldFeatures(holder.nameTxt, holder.recordTxt, holder.recordImg);
                holder.itemView.setEnabled(false);
            }
        } else {
            normFeatures(holder.nameTxt, holder.recordTxt, holder.recordImg);
        }

        if (record.isMe()) {
            holder.itemView.setEnabled(false);
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

    public interface FriendsListener {
        void setFriendSize(View view);
        void setFriendClicked(ProfileWorldRecord record);
    }
}
