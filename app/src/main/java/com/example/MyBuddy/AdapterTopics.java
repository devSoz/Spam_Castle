package com.example.MyBuddy;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.speech.tts.TextToSpeech;
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

import androidx.annotation.NonNull;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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
    private TextToSpeech textToSpeech;
    Context context;
    private String filterPattern="",myuid,topicUrl="";
    int left;

    public AdapterTopics(List<Topic> topicList, Context context, Integer rowLayout, String myuid)
    {
        this.myuid=myuid;
        this.topicList = topicList;
        this.context = context;
        this.rowLayout = rowLayout;
    }
    public static class topicViewHolder extends RecyclerView.ViewHolder
    {
        TextView txtName, txtTopicLasttime,txtUserCount, txtTopicCount;
        ImageView imageTopic;
        LinearLayout linearTopic;

        public topicViewHolder(View view)
        {
            super(view);
            txtName = (TextView) view.findViewById(R.id.txtTopicName);
           // txtUserCount = (TextView) view.findViewById(R.id.txtTopicUserCount);
            txtTopicCount = (TextView) view.findViewById(R.id.txtTopicCount);
            txtTopicLasttime = (TextView) view.findViewById(R.id.txtTopicLasttime);
            imageTopic=(ImageView) view.findViewById((R.id.imageTopic));
            linearTopic = (LinearLayout) view.findViewById(R.id.linearTopic);
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
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(Long.parseLong(topicList.get(position).getLastchat()));
        String dateTime = DateFormat.format("dd/MM/yyyy hh:mm aa", calendar).toString();
        holder.txtTopicLasttime.setText(dateTime);
        holder.txtName.setText(topicName);
        holder.txtTopicCount.setText("(30 New Messages)");
      /*  Picasso.get()
                .load(topicList.get(position).getImageUrl())
                .placeholder(R.color.white)
                .into(holder.imageTopic);*/
     //   holder.txtUserCount.setText(String.valueOf(topicList.get(position).getUserId().size()));
        holder.linearTopic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(context, MessageActivity.class);

                int pos=holder.getAdapterPosition();
             //   context.startActivity(new Intent(context, MessageActivity.class));
                intent.putExtra("userId", myuid);
                intent.putExtra("topicId", topicList.get(pos).getTopicId());
                intent.putExtra("topicUrl", topicList.get(pos).getImageUrl());
                intent.putExtra("topicName", topicList.get(pos).getTopicName());
                context.startActivity(intent);
            }
        });
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
