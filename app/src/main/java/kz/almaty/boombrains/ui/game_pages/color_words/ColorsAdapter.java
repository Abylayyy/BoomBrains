package kz.almaty.boombrains.ui.game_pages.color_words;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import kz.almaty.boombrains.R;
import kz.almaty.boombrains.models.game_models.ColorModel;

public class ColorsAdapter extends RecyclerView.Adapter<ColorsAdapter.ColorViewHolder> {

    private ColorsListener listener;
    private List<ColorModel> slovoList;
    private Context context;

    public ColorsAdapter(List<ColorModel> slovoList, Context context, ColorsListener listener) {
        this.slovoList = slovoList;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ColorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.slovo_item, parent, false);
        return new ColorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ColorViewHolder holder, int position) {
        ColorModel slovo = slovoList.get(position);

        holder.slovo.setText(slovo.getName());
        holder.slovo.setTextColor(context.getResources().getColor(slovo.getColor()));
        holder.slovo.setTypeface(Typeface.DEFAULT_BOLD);

        listener.setSize(holder.itemView);

        holder.itemView.setOnClickListener(v -> {
            listener.getSlovo(holder.itemView, holder.slovo, slovo.getName());
            holder.itemView.setEnabled(false);
        });
    }

    @Override
    public int getItemCount() {
        return slovoList.size();
    }

    class ColorViewHolder extends RecyclerView.ViewHolder{
        TextView slovo;

        ColorViewHolder(@NonNull View itemView) {
            super(itemView);
            slovo = itemView.findViewById(R.id.slovoTxt);
        }
    }

    public interface ColorsListener {
        void setSize(View view);
        void getSlovo(View view, TextView slovo, String name);
    }
}
