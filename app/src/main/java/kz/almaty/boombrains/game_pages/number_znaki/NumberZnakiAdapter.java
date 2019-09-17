package kz.almaty.boombrains.game_pages.number_znaki;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import kz.almaty.boombrains.R;

public class NumberZnakiAdapter extends RecyclerView.Adapter<NumberZnakiAdapter.NumZnakiViewHolder> {

    private List<Integer> numberList;
    private Context context;
    private ZnakiListener listener;

    public NumberZnakiAdapter(List<Integer> numberList, Context context, ZnakiListener listener) {
        this.numberList = numberList;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public NumberZnakiAdapter.NumZnakiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.znaki_item, parent, false);
        return new NumZnakiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NumberZnakiAdapter.NumZnakiViewHolder holder, int position) {
        holder.numTxt.setText(String.valueOf(numberList.get(position)));
        listener.getItem(holder.layout, holder.numTxt);

        holder.itemView.setOnClickListener(v -> listener.onItemSelected(holder.layout, holder.numTxt, holder.numTxt.getText().toString()));
    }

    @Override
    public int getItemCount() {
        return numberList.size();
    }

    class NumZnakiViewHolder extends RecyclerView.ViewHolder {
        TextView numTxt;
        ConstraintLayout layout;
        NumZnakiViewHolder(@NonNull View itemView) {
            super(itemView);
            numTxt = itemView.findViewById(R.id.numTxt);
            layout = itemView.findViewById(R.id.shulteConst);
        }
    }

    public interface ZnakiListener {
        void getItem(ConstraintLayout layout, TextView text);
        void onItemSelected(ConstraintLayout layout, TextView textView, String value);
    }
}
