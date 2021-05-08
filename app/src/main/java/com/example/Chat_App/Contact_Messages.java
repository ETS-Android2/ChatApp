package com.example.Chat_App;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class Contact_Messages extends AppCompatActivity {
    Uri filePath;
    String Sender,Receiver;

    TextView TextMessage;
    ImageButton Send_Photo;
    ImageButton Remove_Photo;
    ImageButton Send_Text;
    ImageView SPhoto;

    RecyclerView Messages_All;
    MessageAdapter adapter;

    List<Message> ls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact__messages);
        TextMessage = findViewById(R.id.Type_Message);
        Send_Photo = findViewById(R.id.Add_Image);
        Remove_Photo = findViewById(R.id.Remove_Image);
        Send_Text = findViewById(R.id.Send);
        Messages_All = findViewById(R.id.rv);
        SPhoto = findViewById(R.id.SendThisImage);

        ls = new ArrayList<>();

        adapter = new MessageAdapter(ls,Contact_Messages.this);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(Contact_Messages.this);
        Messages_All.setLayoutManager(layoutManager);
        Messages_All.setAdapter(adapter);

        Receiver = getIntent().getExtras().getString("Receiver");
        Sender  = getIntent().getExtras().getString("Sender");

        /*
        Receiver = "+929991234567";
        Sender  = "+929991234568";

        Sender  = "+929991234567";
        Receiver = "+929991234568";*/
        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        DatabaseReference dbRefs = FirebaseDatabase.getInstance().getReference(Sender).child("Messages").child(Receiver);

        dbRefs.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull final DataSnapshot dataSnapshot, @Nullable String s) {
                Message cts = dataSnapshot.getValue(Message.class);
                ls.add(cts);
                adapter.notifyDataSetChanged();
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

        Check_Registration(Receiver);

    }

    public void Send_Image(View view) {
        Send_Text.setVisibility(View.INVISIBLE);
        Send_Photo.setVisibility(View.INVISIBLE);
        TextMessage.setVisibility(View.INVISIBLE);
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 0);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 0 && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            SPhoto.setImageURI(filePath);
            SPhoto.setVisibility(View.VISIBLE);
            Send_Text.setVisibility(View.VISIBLE);
            Remove_Photo.setVisibility(View.VISIBLE);
        }
    }

    public void Send_Message(View view) {
        if(!TextMessage.getText().toString().isEmpty())
        {
            Message Temp = new Message("Text",TextMessage.getText().toString(),"Sent");
            Message Temp1 = new Message("Text",TextMessage.getText().toString(),"Received");
            DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference(Sender).child("Messages").child(Receiver).push();
            dbRef.setValue(Temp);
            DatabaseReference dbRef1 = FirebaseDatabase.getInstance().getReference(Receiver).child("Messages").child(Sender).push();
            dbRef1.setValue(Temp1);
            TextMessage.setText("");
        }
        else if(SPhoto.getVisibility()==View.VISIBLE)
        {
            uploadImage(filePath);
            Send_Photo.setVisibility(View.VISIBLE);
            TextMessage.setVisibility(View.VISIBLE);
            SPhoto.setVisibility(View.INVISIBLE);
            Remove_Photo.setVisibility(View.INVISIBLE);
        }
    }

    public void Remove_Image(View view) {
        Send_Photo.setVisibility(View.VISIBLE);
        TextMessage.setVisibility(View.VISIBLE);
        SPhoto.setVisibility(View.INVISIBLE);
        Remove_Photo.setVisibility(View.INVISIBLE);
    }

    private void uploadImage(Uri filePath) {

        if(filePath != null)
        {
            final StorageReference ref = FirebaseStorage.getInstance().getReference().child("Images/"+ UUID.randomUUID().toString());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            String s2 = taskSnapshot.getUploadSessionUri().toString();
                            String[] s = s2.split("/o") ;

                            String image_name = taskSnapshot.getMetadata().getName();

                            String Image_Url = s[0]+"/o/Images%2F"+image_name+"?alt=media";

                            Message Temp = new Message("Image",Image_Url,"Sent");
                            Message Temp1 = new Message("Image",Image_Url,"Received");
                            DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference(Sender).child("Messages").child(Receiver).push();
                            dbRef.setValue(Temp);
                            DatabaseReference dbRef1 = FirebaseDatabase.getInstance().getReference(Receiver).child("Messages").child(Sender).push();
                            dbRef1.setValue(Temp1);
                        }
                    });
        }
    }

    public void Check_Registration(final String User_Phone_No)
    {
        FirebaseDatabase.getInstance().getReference("Registered")
                .addChildEventListener(new ChildEventListener()
                {
                    @Override
                    public void onChildAdded(@NonNull final DataSnapshot dataSnapshot, @Nullable String s) {
                        ContactModel User_Info = dataSnapshot.getValue(ContactModel.class);
                        if(User_Info.getPhone_Number().equals(User_Phone_No))
                        {
                            TextView Receiver_Name = findViewById(R.id.Name);
                            TextView Receiver_No = findViewById(R.id.Status);
                            Receiver_Name.setText(User_Info.getName());
                            Receiver_No.setText(User_Info.getPhone_Number());
                            CircleImageView Img = findViewById(R.id.Photo);
                            Picasso.with(Contact_Messages.this).load(User_Info.getPicture_Url()).into(Img);
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
}
