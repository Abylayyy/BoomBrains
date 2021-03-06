package kz.almaty.boombrains.ui.game_pages.shulte_letter;

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

public class LetterAdapter extends RecyclerView.Adapter<LetterAdapter.ShulteViewHolder> {

    private List<String> numberList;
    private Context context;
    LetterListener listener;

    public LetterAdapter(List<String> numberList, Context context, LetterListener listener) {
        this.numberList = numberList;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ShulteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.letter_item, parent, false);
        return new ShulteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShulteViewHolder holder, int position) {
        String number = numberList.get(position);
        holder.textView.setText(number);

        listener.setSize(holder.itemView, holder.textView);

        holder.textView.setOnClickListener(v -> {
            listener.setNumber(holder.textView.getText().toString(), holder.itemView);
            listener.updateNumbers(holder.textView);
        });
    }

    @Override
    public int getItemCount() {
        return numberList.size();
    }



    class ShulteViewHolder extends RecyclerView.ViewHolder {

        TextView textView;
        ConstraintLayout layout;

        ShulteViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.numTxt);
            layout = itemView.findViewById(R.id.shulteConst);
        }
    }

    public interface LetterListener {
        void setNumber(String value, View view);
        void updateNumbers(TextView view);
        void setSize(View view, TextView text);
    }
}
