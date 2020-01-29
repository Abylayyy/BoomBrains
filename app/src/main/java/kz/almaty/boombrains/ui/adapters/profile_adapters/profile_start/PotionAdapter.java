package kz.almaty.boombrains.ui.adapters.profile_adapters.profile_start;

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
import kz.almaty.boombrains.data.models.profile_model.PotionModel;

public class PotionAdapter extends RecyclerView.Adapter<PotionAdapter.PotionViewHolder> {

    private List<PotionModel> list;
    private PotionListener listener;

    public PotionAdapter(List<PotionModel> list, PotionListener listener) {
        this.listener = listener;
        this.list = list;
    }

    @NonNull
    @Override
    public PotionAdapter.PotionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.potion_item, parent, false);
        return new PotionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PotionAdapter.PotionViewHolder holder, int position) {
        PotionModel model = list.get(position);
        holder.bind(model);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class PotionViewHolder extends RecyclerView.ViewHolder {

        TextView nameTxt;
        ImageView img;

        PotionViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTxt = itemView.findViewById(R.id.achieveTxt);
            img = itemView.findViewById(R.id.achieveImg);
        }

        void bind(PotionModel model) {
            nameTxt.setText(model.getName());
            img.setImageResource(model.getImage());

            if (!model.isEnabled()) {
                img.setColorFilter(Color.parseColor("#C4C4C4"), android.graphics.PorterDuff.Mode.MULTIPLY);
            }

            listener.setPotionSize(itemView);
        }
    }

    public interface PotionListener {
        void setPotionSize(View view);
    }
}
