package kz.almaty.boombrains.ui.game_pages.rem_words;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import kz.almaty.boombrains.R;

public class SlovoAdapter extends RecyclerView.Adapter<SlovoAdapter.SlovoViewHolder> {

    private SlovoListener listener;
    private List<String> slovoList;
    private Context context;
    private String correctResult = "";

    public SlovoAdapter(List<String> slovoList, Context context, SlovoListener listener) {
        this.slovoList = slovoList;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SlovoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.slovo_item, parent, false);
        return new SlovoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SlovoViewHolder holder, int position) {
        String slovo = slovoList.get(position);

        holder.slovo.setText(slovo);
        listener.setSize(holder.itemView);

        holder.itemView.setOnClickListener(v -> {
            listener.setSlovo(holder.layout, holder.slovo);
            holder.itemView.setEnabled(false);
        });
    }

    @Override
    public int getItemCount() {
        return slovoList.size();
    }

    class SlovoViewHolder extends RecyclerView.ViewHolder{
        TextView slovo;
        LinearLayout layout;

        SlovoViewHolder(@NonNull View itemView) {
            super(itemView);
            slovo = itemView.findViewById(R.id.slovoTxt);
            layout = itemView.findViewById(R.id.slovoLinear);
        }
    }

    public interface SlovoListener {
        void setSlovo(LinearLayout view, TextView text);
        void setSize(View view);
    }
}
