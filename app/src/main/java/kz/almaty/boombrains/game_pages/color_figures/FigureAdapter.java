package kz.almaty.boombrains.game_pages.color_figures;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import kz.almaty.boombrains.R;
import kz.almaty.boombrains.models.game_models.FigureModel;

public class FigureAdapter extends RecyclerView.Adapter<FigureAdapter.FigureViewHolder> {

    private List<FigureModel> numberList;
    private Context context;
    private FigureListener listener;
    private int countRed = 0, countOrange = 0, countGreen = 0, countBlue = 0;

    FigureAdapter(List<FigureModel> numberList, Context context, FigureListener listener) {
        this.numberList = numberList;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FigureViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.figure_item, parent, false);
        return new FigureViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FigureViewHolder holder, int position) {
        FigureModel number = numberList.get(position);

        holder.shape.setImageResource(number.getResource());
        holder.shape.setColorFilter(context.getResources().getColor(number.getTint()));
        listener.setSize(holder.itemView);

        if (number.isSelected()) {
            holder.shape.setVisibility(View.INVISIBLE);
        }

        countNumbers(number.getTint());
        holder.itemView.setOnClickListener(v -> {
            listener.updateNumbers(holder.itemView, number.getTint());
        });
    }

    public List<FigureModel> getList() {
        return numberList;
    }

    private void countNumbers(int type) {
        switch (type) {
            case R.color.redFigure: countRed += 1; break;
            case R.color.blueFigure: countBlue += 1; break;
            case R.color.greenFigure: countGreen += 1; break;
            case R.color.orangeFigure: countOrange += 1; break;
        }
    }

    public int getCountOrange() {
        return countOrange;
    }

    public int getCountGreen() {
        return countGreen;
    }

    public int getCountBlue() {
        return countBlue;
    }

    public int getCountRed() {
        return countRed;
    }

    @Override
    public int getItemCount() {
        return numberList.size();
    }



    class FigureViewHolder extends RecyclerView.ViewHolder {

        ImageView shape;

        FigureViewHolder(@NonNull View itemView) {
            super(itemView);
            shape = itemView.findViewById(R.id.shape);
        }
    }

    public interface FigureListener {
        void updateNumbers(View view, int tint);
        void setSize(View view);
    }
}
