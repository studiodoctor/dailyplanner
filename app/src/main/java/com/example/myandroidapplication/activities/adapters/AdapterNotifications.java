package com.example.myandroidapplication.activities.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myandroidapplication.R;
import com.example.myandroidapplication.models.Notifications.Notification;

import java.util.ArrayList;

public class AdapterNotifications extends RecyclerView.Adapter<AdapterNotifications.ViewHolder>
{
    private ArrayList<Notification> notificationList;

    public AdapterNotifications(ArrayList<Notification> notificationList)
    {
        this.notificationList = notificationList;
    }

    //Creates a new RecyclerView.ViewHolder and initializes some private fields to be used by RecyclerView
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_notification, viewGroup, false);
        return new ViewHolder(view);
    }

    // Updates the RecyclerView.ViewHolder contents with the item at the given position and also sets up private fields
    //to be used by RecyclerView
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i)
    {
        Notification data = notificationList.get(i);
        viewHolder.txt_notification_title.setText(data.getNotificationTitle());
        viewHolder.txt_notification_desc.setText(data.getNotificationBody());
        viewHolder.txt_notification_category.setText(data.getNotificationCategory());
    }

    // Returns the total number of items in the data set held by the adapter.
    @Override
    public int getItemCount()
    {
        return notificationList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView txt_notification_title, txt_notification_desc, txt_notification_category;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_notification_title = itemView.findViewById(R.id.txt_notification_title);
            txt_notification_desc = itemView.findViewById(R.id.txt_notification_desc);
            txt_notification_category = itemView.findViewById(R.id.txt_notification_category);
        }
    }
}