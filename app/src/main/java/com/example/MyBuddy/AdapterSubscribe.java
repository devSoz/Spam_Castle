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
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import com.example.MyBuddy.Model.Model3;
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

public class AdapterSubscribe extends RecyclerView.Adapter<AdapterSubscribe.subscribeViewHolder>
{
    private List<Topic> topicList= new ArrayList<Topic>();
    private List<Topic> topicListFull= new ArrayList<Topic>();
    public List<Model3> hashMap1=new ArrayList<Model3>();
    private String userId, topicId;
    private Integer rowLayout;
    private TextToSpeech textToSpeech;
    Context context;
    private String filterPattern="",myuid,topicUrl="";
    int left;

    public AdapterSubscribe(List<Topic> topicList,List<Model3> hashMap1, Context context, Integer rowLayout, String myuid)
    {
        this.myuid=myuid;
        this.topicList = topicList;
        this.hashMap1 = hashMap1;
        this.context = context;
        this.rowLayout = rowLayout;
    }
    public static class subscribeViewHolder extends RecyclerView.ViewHolder
    {
        TextView txtName, txtTopicLasttime,txtUserCount, txtTopicCount,txtDesc;
        ImageView imageSubscribe;
        CheckBox chkTopic;
        LinearLayout linearTopic;

        public subscribeViewHolder(View view)
        {
            super(view);
            txtName = (TextView) view.findViewById(R.id.txtTopicName);
            txtDesc = (TextView) view.findViewById(R.id.txtTopicDesc);
            txtUserCount = (TextView) view.findViewById(R.id.txtTopicUserCount);
            imageSubscribe = (ImageView) view.findViewById(R.id.imageTopic);
            txtTopicLasttime = (TextView) view.findViewById(R.id.txtTopicLasttime);
            chkTopic=(CheckBox) view.findViewById((R.id.chkTopic));
            linearTopic = (LinearLayout) view.findViewById(R.id.linearTopic);
        }
    }

    @Override
    public AdapterSubscribe.subscribeViewHolder onCreateViewHolder(ViewGroup parent,
                                                                   int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        Log.d("viewchumo", String.valueOf(viewType));
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new subscribeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(subscribeViewHolder holder, final int position)
    {
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(Long.parseLong(topicList.get(position).getLastchat()));
        String dateTime = DateFormat.format("dd/MM/yyyy hh:mm aa", calendar).toString();


        holder.txtName.setText(topicList.get(position).getTopicName());
        holder.txtDesc.setText(topicList.get(position).getDescription());
        holder.txtUserCount.setText(hashMap1.get(position).getValue2().toString());
        holder.txtTopicLasttime.setText(dateTime);
        if(hashMap1.get(position).getValue1().equals("Yes"))
            holder.chkTopic.setChecked(true);
        else
            holder.chkTopic.setChecked(false);

        String topicImageUrl = topicList.get(position).getImageUrl();
        Picasso.get()
                .load(topicList.get(position).getImageUrl())
                .placeholder(R.color.white)
                .into(holder.imageSubscribe);
        holder.txtUserCount.setText(String.valueOf(topicList.get(position).getUserId().size()));
        holder.chkTopic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.chkTopic.isChecked())
                    hashMap1.get(holder.getAdapterPosition()).setValue3("Yes");
                else
                    hashMap1.get(holder.getAdapterPosition()).setValue3("No");
            }
        }) ;


    }
    @Override
    public int getItemCount () {
        return topicList.size();
    }

    public void refreshData()
    {
        notifyDataSetChanged();
    }
    public List<Model3> SubscribeTopic() {
        return hashMap1;

    }


}
