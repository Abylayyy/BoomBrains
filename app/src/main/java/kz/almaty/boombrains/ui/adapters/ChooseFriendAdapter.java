package kz.almaty.boombrains.ui.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import kz.almaty.boombrains.R;
import kz.almaty.boombrains.data.models.profile_model.ProfileWorldRecord;

public class ChooseFriendAdapter extends ArrayAdapter<ProfileWorldRecord> {

    public ChooseFriendAdapter(@NonNull Context context, List<ProfileWorldRecord> friends) {
        super(context, 0, friends);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @SuppressLint("SetTextI18n")
    private View initView(int position, View view, ViewGroup parent) {
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.choose_friend, parent, false);
        }
        ProfileWorldRecord model = getItem(position);

        TextView friendName = view.findViewById(R.id.friendName);
        TextView friendRecord = view.findViewById(R.id.friendRecord);
        ImageView friendImg = view.findViewById(R.id.friendImg);

        if (position == 0) {
            friendName.setVisibility(View.INVISIBLE);
            friendRecord.setVisibility(View.INVISIBLE);
            friendImg.setVisibility(View.INVISIBLE);
        }

        friendName.setText(model.getUsername());
        friendRecord.setText(""+model.getTotalRecord());

        return view;
    }
}
