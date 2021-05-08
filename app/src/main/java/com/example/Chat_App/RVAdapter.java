package com.example.Chat_App;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

class RVAdapter extends RecyclerView.Adapter<RVAdapter.RVViewHolder> {
    List<ContactModel> ls;
    Context ctx;
    String User_No;
    File myDirectory;

    public RVAdapter(List<ContactModel> ls, Context Ctxs,String Users_No) {
        this.ctx =Ctxs;
        this.ls=ls;
        this.User_No = Users_No;
    }
    
    @NonNull
    @Override
    public RVViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row= LayoutInflater.from(parent.getContext()).inflate(R.layout.row,parent,false);

        return new RVViewHolder(row);


    }

    public void Last_Message(String Sender, String Receiver, final TextView message)
    {
        DatabaseReference dbRefs = FirebaseDatabase.getInstance().getReference(Sender).child("Messages").child(Receiver);

        dbRefs.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull final DataSnapshot dataSnapshot, @Nullable String s) {
                Message cts = dataSnapshot.getValue(Message.class);
                if(cts.getType().equals("Text"))
                {
                    message.setText(cts.getMsg());
                    if (cts.getStatus().contains("Sent")) {
                        message.setTextColor(Color.parseColor("#18E222"));
                    }
                    else
                    {
                        message.setTextColor(Color.parseColor("#18E3FD"));
                    }
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBindViewHolder(@NonNull final RVViewHolder holder, final int position) {

        holder.name.setText(ls.get(position).getName());
        //Picasso.with(ctx).load(ls.get(position).getPicture_Url()).networkPolicy(NetworkPolicy.OFFLINE).into(holder.im);
        Picasso.with(ctx).load(ls.get(position).getPicture_Url()).into(holder.im);
        Last_Message(User_No,ls.get(position).getPhone_Number(),holder.message);

        holder.Row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent User_Logged_In = new Intent(ctx,Contact_Messages.class);
                User_Logged_In.putExtra("Sender",User_No);
                User_Logged_In.putExtra("Receiver",ls.get(position).getPhone_Number());
                ctx.startActivity(User_Logged_In);
            }
        });
    }
    @Override
    public int getItemCount() {
        return ls.size();
    }

    public class RVViewHolder extends RecyclerView.ViewHolder {
        TextView name,message;
        ImageView im;
        LinearLayout Row;

        public RVViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.name);
            message=itemView.findViewById(R.id.last_message);
            im = itemView.findViewById(R.id.imgv);
            Row = itemView.findViewById(R.id.Row);
        }
    }
}
