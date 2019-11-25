package kz.almaty.boombrains.adapters.profile_adapters.profile_start;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import kz.almaty.boombrains.R;
import kz.almaty.boombrains.models.add_friend_models.RequestListModel;

public class FriendRequestsAdapter extends RecyclerView.Adapter<FriendRequestsAdapter.RequestsViewHolder> {

    private List<RequestListModel> requestList;
    private Context context;
    private RequestsListener listener;

    public FriendRequestsAdapter(List<RequestListModel> requestList, Context context, RequestsListener listener) {
        this.requestList = requestList;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RequestsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.request_items, parent, false);
        return new RequestsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestsViewHolder holder, int position) {
        RequestListModel model = requestList.get(position);
        holder.username.setText(model.getUsername());
        holder.email.setText(model.getUid());
        holder.addBtn.setOnClickListener(v -> listener.onAddBtnClicked(model.getRequesterId()));
        holder.rejectBtn.setOnClickListener(v -> listener.onRejectBtnClicked(model.getRequesterId()));
    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }

    class RequestsViewHolder extends RecyclerView.ViewHolder {

        TextView username, email;
        Button addBtn, rejectBtn;

        RequestsViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.req_username);
            email = itemView.findViewById(R.id.req_email);
            addBtn = itemView.findViewById(R.id.addRequestBtn);
            rejectBtn = itemView.findViewById(R.id.rejectRequestBtn);
        }
    }

    public interface RequestsListener {
        void onAddBtnClicked(String userId);
        void onRejectBtnClicked(String userId);
    }
}
