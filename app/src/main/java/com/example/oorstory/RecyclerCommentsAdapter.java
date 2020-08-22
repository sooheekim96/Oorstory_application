package com.example.oorstory;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class RecyclerCommentsAdapter extends RecyclerView.Adapter<RecyclerCommentsAdapter.ViewHolder> {
    private ArrayList<RecyclerComments> cData = null;
    private Context context;

    RecyclerCommentsAdapter(ArrayList<RecyclerComments> list) {
        cData = list ;
    }

    public void empty_list(){
        cData.clear();
    }

    @NonNull
    @Override
    public RecyclerCommentsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        View view = inflater.inflate(R.layout.recycler_comments, parent, false) ;
        RecyclerCommentsAdapter.ViewHolder vh = new RecyclerCommentsAdapter.ViewHolder(view) ;

        return vh ;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerCommentsAdapter.ViewHolder holder, int position) {
        final RecyclerComments item = cData.get(position) ;
        holder.CommentUserName.setText(item.getNickname());
        holder.CommentContent.setText(item.getComment());
        holder.CommentUserDate.setText(item.getDate());


    }

    @Override
    public int getItemCount() {
        return cData.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView CommentUserName, CommentContent, CommentUserDate;

        ViewHolder(View itemView) {
            super(itemView) ;

            CommentUserName = itemView.findViewById(R.id.CommentUserName);
            CommentContent = itemView.findViewById(R.id.CommentContent);
            CommentUserDate = itemView.findViewById(R.id.CommentUserDate);
        }
    }


}
