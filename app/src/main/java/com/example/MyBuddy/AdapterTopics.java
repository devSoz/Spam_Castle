package com.example.MyBuddy;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.core.content.FileProvider;

import com.example.MyBuddy.Model.Chat;
import com.example.MyBuddy.Model.Topic;
import com.example.MyBuddy.Model.user;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterTopics extends RecyclerView.Adapter<AdapterTopics.topicViewHolder>
{
    private List<Topic> topicList= new ArrayList<Topic>();
    private List<Topic> topicListFull= new ArrayList<Topic>();
    private String userId, topicId;
    private Integer rowLayout;
    Context context;
    private String filterPattern="";
    int left;

    public AdapterTopics(List<Topic> topicList, String userId, Context context, Integer rowLayout)
    {
        this.topicList = topicList;
        this.userId = userId;
        this.context = context;
        this.rowLayout = rowLayout;
    }
    public static class topicViewHolder extends RecyclerView.ViewHolder
    {
        TextView txtTopicName, txtNewMsg;
        ImageView imageTopic;

        public topicViewHolder(View view)
        {
            super(view);
            txtNewMsg = (TextView) view.findViewById(R.id.txtNewMsg);
            txtTopicName = (TextView) view.findViewById(R.id.txtTopicName);
            imageTopic=(ImageView) view.findViewById((R.id.imageTopic));
        }
    }

    @Override
    public AdapterTopics.topicViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        Log.d("viewchumo", String.valueOf(viewType));
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new topicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(topicViewHolder holder, final int position)
    {
        String topicName = topicList.get(position).getTopicName();
        String topicImageUrl = topicList.get(position).getImageUrl();

        holder.txtTopicName.setText(topicName);
    }
    @Override
    public int getItemCount () {
        return topicList.size();
    }

    private void shareImageandText(Bitmap bitmap, String heroName) {
        Uri uri = getmageToShare(bitmap);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.putExtra(Intent.EXTRA_TEXT, heroName);
        intent.putExtra(Intent.EXTRA_SUBJECT, "Share");
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setType("image/*");

        context.startActivity(Intent.createChooser(intent, "Share Via"));
    }

    private Uri getmageToShare(Bitmap bitmap) {
        File imagefolder = new File(context.getCacheDir(), "images");
        Uri uri = null;
        try {
            imagefolder.mkdirs();
            File file = new File(imagefolder, "shared_image.png");
            FileOutputStream outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, outputStream);
            outputStream.flush();
            outputStream.close();
            uri = FileProvider.getUriForFile(context, "com.example.myapplication", file);
        } catch (Exception e) {

            Toast.makeText(context, "" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return uri;
    }
}
