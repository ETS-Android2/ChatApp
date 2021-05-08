package com.example.Chat_App;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.RVViewHolder> {
    List<Message> ls;
    Context ctx;
    public MessageAdapter(List<Message> ls, Context Ctxs) {
        this.ctx =Ctxs;
        this.ls=ls;
    }
    
    @NonNull
    @Override
    public RVViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row= LayoutInflater.from(parent.getContext()).inflate(R.layout.message_row,parent,false);

        return new RVViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull RVViewHolder holder, int position) {

        if (ls.get(position).getType().contains("Text"))
        {
            if (ls.get(position).getStatus().contains("Sent"))
            {
                holder.Messages.setVisibility(View.VISIBLE);
                holder.Messages1.setVisibility(View.GONE);
                holder.Shared_Photo.setVisibility(View.GONE);
                holder.Shared_Photo.setImageBitmap(null);
                holder.Shared_Photo1.setVisibility(View.GONE);
                holder.Shared_Photo1.setImageBitmap(null);


                holder.Messages.setText(ls.get(position).getMsg());

            }
            else if (ls.get(position).getStatus().contains("Received"))
            {
                holder.Messages1.setVisibility(View.VISIBLE);
                holder.Messages.setVisibility(View.GONE);
                holder.Shared_Photo.setVisibility(View.GONE);
                holder.Shared_Photo.setImageBitmap(null);
                holder.Shared_Photo1.setVisibility(View.GONE);
                holder.Shared_Photo1.setImageBitmap(null);

                holder.Messages1.setText(ls.get(position).getMsg());

            }
        }
        else if (ls.get(position).getType().contains("Image"))
        {
            if (!ls.get(position).getMsg().isEmpty())
            {
                if (ls.get(position).getStatus().contains("Sent"))
                {
                    holder.Messages.setVisibility(View.GONE);
                    holder.Messages1.setVisibility(View.GONE);
                    holder.Shared_Photo.setVisibility(View.VISIBLE);
                    holder.Shared_Photo1.setVisibility(View.GONE);
                    holder.Shared_Photo1.setImageBitmap(null);

                    //Picasso.with(ctx).load(ls.get(position).getMsg()).networkPolicy(NetworkPolicy.OFFLINE).into(holder.Shared_Photo);
                    Picasso.with(this.ctx).load(ls.get(position).getMsg()).into(holder.Shared_Photo);
                }
                else if (ls.get(position).getStatus().contains("Received"))
                {
                    holder.Messages.setVisibility(View.GONE);
                    holder.Messages1.setVisibility(View.GONE);
                    holder.Shared_Photo.setVisibility(View.GONE);
                    holder.Shared_Photo.setImageBitmap(null);
                    holder.Shared_Photo1.setVisibility(View.VISIBLE);

                    //Picasso.with(ctx).load(ls.get(position).getMsg()).networkPolicy(NetworkPolicy.OFFLINE).into(holder.Shared_Photo1);
                    Picasso.with(this.ctx).load(ls.get(position).getMsg()).into(holder.Shared_Photo1);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return ls.size();
    }

    public class RVViewHolder extends RecyclerView.ViewHolder {
        TextView Messages;
        TextView Messages1;
        ImageView Shared_Photo;
        ImageView Shared_Photo1;

        public RVViewHolder(@NonNull View itemView) {
            super(itemView);
            Messages = itemView.findViewById(R.id.Messages);
            Messages1 = itemView.findViewById(R.id.Messages1);
            Shared_Photo = itemView.findViewById(R.id.Shared_Image);
            Shared_Photo1 = itemView.findViewById(R.id.Shared_Image1);
        }
    }
}
