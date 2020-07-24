package com.example.oorstory;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>{
    private ArrayList<RecyclerItem> mData = null ;

    RecyclerAdapter(ArrayList<RecyclerItem> list) {
        mData = list ;
    }

    public void empty_list(){
        mData.clear();
    }

    @NonNull
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        View view = inflater.inflate(R.layout.recycler_item, parent, false) ;
        RecyclerAdapter.ViewHolder vh = new RecyclerAdapter.ViewHolder(view) ;

        return vh ;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerAdapter.ViewHolder holder, int position) {
        final RecyclerItem item = mData.get(position) ;
        holder.title_story_iv.setImageDrawable(item.getTitle_story_iv());

//        if (item.getIsStarred()){
//            holder.is_starred.setImageResource(R.drawable.starred);
//        }
//        else{
//            holder.is_starred.setImageResource(R.drawable.star);
//        }

        int star_num = item.getStar_num();
        if (star_num >= 1){
            holder.diff1.setImageResource(R.drawable.starred);
        }
        if (star_num >= 2){
            holder.diff2.setImageResource(R.drawable.starred);
        }
        if (star_num >= 3){
            holder.diff3.setImageResource(R.drawable.starred);
        }
        if (star_num >= 4){
            holder.diff4.setImageResource(R.drawable.starred);
        }
        if (star_num >= 5){
            holder.diff5.setImageResource(R.drawable.starred);
        }

        if (item.getTitle_story().length() >= 15){
            holder.title_story.setTextSize(15f);
            holder.title_story.setText(item.getTitle_story()) ;
        }
        else{
            holder.title_story.setTextSize(19f);
            holder.title_story.setText(item.getTitle_story()) ;
        }

        holder.theme_story.setText(item.getTheme_story()) ;
        if(item.getTime_story()>60){
            int tmp = item.getTime_story();
            int hr = tmp/60;
            tmp = tmp%60;
            holder.time_story.setText(String.format(hr + "시간 " + tmp + "분")) ;
        }
        else{
            holder.time_story.setText(String.format(item.getTime_story() + "분")) ;
        }

        holder.is_starred.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean i = item.getIsStarred();
                item.setIsStarred(!i);

                if (!i) {
                    holder.is_starred.setImageResource(R.drawable.starred);
                }
                else{
                    holder.is_starred.setImageResource(R.drawable.star);
                }

            }
        });


    }

    @Override
    public int getItemCount() {
        return mData.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView title_story_iv, is_starred, diff1, diff2, diff3, diff4, diff5;
        private TextView title_story, theme_story, time_story;
        private CardView recycler1_card_view;

        ViewHolder(View itemView) {
            super(itemView) ;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(view.getContext(),"스토리 디테일 화면이얌", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(view.getContext(), StoryDetailActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    view.getContext().startActivity(intent);
                }
            });
            // 뷰 객체에 대한 참조. (hold strong reference)
            title_story_iv = itemView.findViewById(R.id.title_story_iv);
            is_starred = itemView.findViewById(R.id.is_starred) ;
            diff1 = itemView.findViewById(R.id.diff1) ;
            diff2 = itemView.findViewById(R.id.diff2) ;
            diff3 = itemView.findViewById(R.id.diff3) ;
            diff4 = itemView.findViewById(R.id.diff4) ;
            diff5 = itemView.findViewById(R.id.diff5) ;
            title_story = itemView.findViewById(R.id.title_story) ;
            theme_story = itemView.findViewById(R.id.theme_tv) ;
            time_story = itemView.findViewById(R.id.time_tv) ;
            recycler1_card_view = itemView.findViewById(R.id.recycler1_card_view);
        }
    }


}
