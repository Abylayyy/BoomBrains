package kz.almaty.boombrains.ui.main_fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import kz.almaty.boombrains.R;


public class DuelFragment extends Fragment {

    public DuelFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_duel, container, false);
    }

}
