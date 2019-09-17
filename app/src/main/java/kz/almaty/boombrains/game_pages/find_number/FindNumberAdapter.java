package kz.almaty.boombrains.game_pages.find_number;

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

public class FindNumberAdapter extends RecyclerView.Adapter<FindNumberAdapter.FindViewHolder> {

    private List<Integer> numberList;
    private Context context;
    private FindListener listener;

    public FindNumberAdapter(List<Integer> numberList, Context context, FindListener listener) {
        this.numberList = numberList;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FindNumberAdapter.FindViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.find_item, parent, false);
        return new FindViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FindNumberAdapter.FindViewHolder holder, int position) {
        holder.numTxt.setText(String.valueOf(numberList.get(position)));
        listener.getItem(holder.layout, holder.numTxt);

        holder.itemView.setOnClickListener(v -> listener.onItemSelected(holder.layout, holder.numTxt, holder.numTxt.getText().toString()));
    }

    @Override
    public int getItemCount() {
        return numberList.size();
    }

    class FindViewHolder extends RecyclerView.ViewHolder {
        TextView numTxt;
        ConstraintLayout layout;
        FindViewHolder(@NonNull View itemView) {
            super(itemView);
            numTxt = itemView.findViewById(R.id.numTxt);
            layout = itemView.findViewById(R.id.shulteConst);
        }
    }

    public interface FindListener {
        void getItem(ConstraintLayout layout, TextView text);
        void onItemSelected(ConstraintLayout layout, TextView textView, String value);
    }
}
