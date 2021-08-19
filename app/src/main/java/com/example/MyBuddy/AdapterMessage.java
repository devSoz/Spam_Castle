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
        import com.example.MyBuddy.Model.user;
        import com.squareup.picasso.Picasso;

        import java.io.File;
        import java.io.FileOutputStream;
        import java.util.Locale;

        import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterMessage extends RecyclerView.Adapter<AdapterMessage.chatViewHolder>
        //implements Filterable
{

    private List<Chat> chatList= new ArrayList<Chat>();
    private List<Chat> chatListFull= new ArrayList<Chat>();
    private String userId, topicId;
    Context context;
    private String filterPattern="";
    int left;

    public AdapterMessage(List<Chat> chatList, String userId, Context context, String topicId)
    {
        this.chatList = chatList;
        this.userId = userId;
        this.context = context;
        this.topicId = topicId;
    }
    public static class chatViewHolder extends RecyclerView.ViewHolder
    {
        TextView txtTime, txtMsg, txtIsSeen, txtName;
        CircleImageView imageProfile;
        ImageView chatImage;
        //ImageView images;
        //ImageButton imgBtnSend;

        public chatViewHolder(View view)
        {
            super(view);
            imageProfile = (CircleImageView) view.findViewById(R.id.imgProfile);
            txtMsg = (TextView) view.findViewById(R.id.txtMsg);
            txtIsSeen = (TextView) view.findViewById(R.id.txtIsSeen);
            txtTime = (TextView) view.findViewById(R.id.txtTime);
            txtName = (TextView) view.findViewById(R.id.txtName);
            chatImage=(ImageView) view.findViewById((R.id.images));
        }
    }

    @Override
    public AdapterMessage.chatViewHolder onCreateViewHolder(ViewGroup parent,
                                                               int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        Log.d("viewchumo", String.valueOf(viewType));
        //View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        if(viewType==0) {
           View view = inflater.inflate(R.layout.message_left, parent, false);
           return new chatViewHolder(view);
        }
        else {
            View view = inflater.inflate(R.layout.message_right, parent, false);
            return new chatViewHolder(view);
        }
      //  return new chatViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {

        if(chatList.get(position).getSender().equals(userId))
        {
            left=0;
        }
        else {
            left = 1;
        }
        return 1;
    }

    @Override
    public void onBindViewHolder(chatViewHolder holder, final int position)
    {
        String msg = chatList.get(position).getMessage();
        String timeStamp = chatList.get(position).getTimestamp();
        holder.txtMsg.setText(msg);

        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(Long.parseLong(timeStamp));
        String dateTime = DateFormat.format("dd/MM/yyyy hh:mm aa", calendar).toString();
        holder.txtTime.setText(dateTime);
       // if(left==1)
       // {holder.txtName.setText("testname");}
        holder.chatImage.setVisibility(View.GONE);
        Log.d("bindchumo",chatList.get(position).getSender()+" "+position);
        //String isSeen = chatList.get(position).get;

      /*  Picasso.get()
                .load(userList.get(position).getUrl())
                .placeholder(R.color.white)
                .into(holder.imageViewFav);*/


        /*holder.imgBtnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BitmapDrawable bitmapDrawable = (BitmapDrawable) holder.imageViewFav.getDrawable();
                Bitmap bitmap = bitmapDrawable.getBitmap();
                String heroDetails=new String("");
                Integer i = holder.getAdapterPosition();
               // heroDetails="Name :" + userList.get(i).ge() ;
              //  shareImageandText(bitmap,heroDetails);
            }
        });*/

    }
    @Override
    public int getItemCount () {
        return chatList.size();
    }

    /*
    @Override
    public Filter getFilter() {
        return searchHeroFilter;
    }

    private Filter searchHeroFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Chat> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(userListFull);
            } else {
                filterPattern = constraint.toString().toLowerCase().trim();
                for (user item : userList) {
                    if (item.getName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                    if (String.valueOf(item.getId()).contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            faveHeroList.clear();
            faveHeroList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

*/
    public void addAll(List<user> tempResults) {

        for (user result : tempResults) {
          //  chatList.add(result);
          //  chatListFull.add(result);

        }
        notifyDataSetChanged();
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
