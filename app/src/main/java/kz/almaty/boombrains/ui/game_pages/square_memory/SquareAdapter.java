package kz.almaty.boombrains.ui.game_pages.square_memory;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import kz.almaty.boombrains.R;
import kz.almaty.boombrains.models.game_models.SquareModel;

public class SquareAdapter extends RecyclerView.Adapter<SquareAdapter.SquareViewHolder> {

    private List<SquareModel> numberList;
    private Context context;
    private SquareListener listener;
    List<Integer> items = new ArrayList<>();

    public SquareAdapter(List<SquareModel> numberList, Context context, SquareListener listener) {
        this.numberList = numberList;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SquareViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shulte_item, parent, false);
        return new SquareViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SquareViewHolder holder, int position) {
        SquareModel number = numberList.get(position);
        holder.textView.setText(number.getNumber());
        holder.textView.setVisibility(View.INVISIBLE);

        listener.setSize(holder.itemView);

        if (number.isSelected()) {
            listener.setSelected(holder.itemView);
            items.add(position);
        }

        holder.itemView.setOnClickListener(v -> listener.updateNumbers(holder.itemView, position));
    }

    @Override
    public int getItemCount() {
        return numberList.size();
    }

    public List<Integer> selectedItems() {
        return items;
    }

    class SquareViewHolder extends RecyclerView.ViewHolder {

        TextView textView;
        ConstraintLayout layout;

        SquareViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.numTxt);
            layout = itemView.findViewById(R.id.shulteConst);
        }
    }

    public interface SquareListener {
        void updateNumbers(View view, int position);
        void setSize(View view);
        void setSelected(View view);
    }
}
