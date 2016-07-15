package project.bsts.semut.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import project.bsts.semut.R;
import project.bsts.semut.data.FriendData;

/**
 * Created by asepmoels on 5/28/15.
 */
public class FriendListAdapter extends ArrayAdapter<FriendData> {
    private List<FriendData> items;
    private int layoutResourceId;
    private Context context;

    public FriendListAdapter(Context context, int layoutResourceId, List<FriendData> items) {
        super(context, layoutResourceId, items);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        FriedDataHolder holder = null;

        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        row = inflater.inflate(layoutResourceId, parent, false);

        holder = new FriedDataHolder();
        holder.friendData = items.get(position);
        holder.acceptButton = (Button)row.findViewById(R.id.acceptButton);
        holder.acceptButton.setTag(holder.friendData);
        holder.addButton = (Button)row.findViewById(R.id.addButton);
        holder.addButton.setTag(holder.friendData);

        holder.name = (TextView)row.findViewById(R.id.text1);
        holder.email = (TextView)row.findViewById(R.id.text2);

        row.setTag(holder);

        setupItem(holder);

        return row;
    }

    private void setupItem(FriedDataHolder holder) {
        holder.name.setText(holder.friendData.getName());
        holder.email.setText(String.valueOf(holder.friendData.getEmail()));
        holder.acceptButton.setVisibility(holder.friendData.isRequest() ? View.VISIBLE : View.GONE);
        holder.addButton.setVisibility(holder.friendData.isNeedToAdd()?View.VISIBLE:View.GONE);
    }

    public static class FriedDataHolder {
        FriendData friendData;
        TextView name;
        TextView email;
        Button acceptButton;
        Button addButton;
    }
}
