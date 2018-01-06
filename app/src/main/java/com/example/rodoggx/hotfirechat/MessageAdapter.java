package com.example.rodoggx.hotfirechat;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by RodoggX on 11/24/2017.
 */

public class MessageAdapter extends ArrayAdapter<MessageItem> {
    public MessageAdapter(Context context, int resource, List<MessageItem> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.message, parent, false);
        }

        ImageView photoImageView = (ImageView) convertView.findViewById(R.id.photoImageView);
        TextView messageTextView = (TextView) convertView.findViewById(R.id.messageTextView);
        TextView authorTextView = (TextView) convertView.findViewById(R.id.nameTextView);

        MessageItem messageItem = getItem(position);

        boolean isPhoto = messageItem.getPhotoUrl() != null;
        if (isPhoto) {
            messageTextView.setVisibility(View.VISIBLE);
            photoImageView.setVisibility(View.VISIBLE);
            Glide.with(photoImageView.getContext())
                    .load(messageItem.getPhotoUrl())
                    .into(photoImageView);
        } else {
            messageTextView.setVisibility(View.VISIBLE);
            photoImageView.setVisibility(View.GONE);
            messageTextView.setText(messageItem.getText());
        }
        authorTextView.setText(messageItem.getName());
        return convertView;
    }
}
