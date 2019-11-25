package kz.almaty.boombrains.adapters.profile_adapters.profile_start;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import kz.almaty.boombrains.R;
import kz.almaty.boombrains.files.RememberWordsRu;
import kz.almaty.boombrains.models.profile_model.Achievement;

public class AchieveAdapter extends RecyclerView.Adapter<AchieveAdapter.AchieveViewHolder> {

    private List<Achievement> achievements;
    private Context context;
    private AchieveListener listener;

    public AchieveAdapter(List<Achievement> achievements, Context context, AchieveListener listener) {
        this.achievements = achievements;
        this.context = context;
        this.listener = listener;
    }

    public AchieveAdapter(List<Achievement> achievements, Context context) {
        this.achievements = achievements;
        this.context = context;
    }

    @NonNull
    @Override
    public AchieveAdapter.AchieveViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.achieve_item, parent, false);
        return new AchieveViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AchieveAdapter.AchieveViewHolder holder, int position) {
        Achievement achievement = achievements.get(position);
        listener.setViewSize(holder.itemView);

        if (!achievement.getVerity()) {
            achievement.setResource(RememberWordsRu.list_off.get(position));
        }

        holder.achieveImg.setImageResource(achievement.getResource());
        holder.achieveName.setText(achievement.getTranslate());
        holder.itemView.setOnClickListener(v -> listener.setViewClicked(achievement));
    }

    @Override
    public int getItemCount() {
        return achievements.size();
    }

    class AchieveViewHolder extends RecyclerView.ViewHolder {

        ImageView achieveImg;
        TextView achieveName;

        AchieveViewHolder(@NonNull View itemView) {
            super(itemView);

            achieveImg = itemView.findViewById(R.id.achieveImg);
            achieveName = itemView.findViewById(R.id.achieveTxt);
        }
    }

    public interface AchieveListener {
        void setViewSize(View view);
        void setViewClicked(Achievement model);
    }
}
