package com.example.Chat_App;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class LogInActivity extends AppCompatActivity {

    List<ContactModel> contacts_list;
    TextView Forgot_Password;

    EditText Phone_Number,Password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

//        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        Forgot_Password = findViewById(R.id.Forgot_Password);
        Phone_Number = findViewById(R.id.Phone_No_Edit);
        Password = findViewById(R.id.Password_Edit);

        contacts_list = new ArrayList<>();

        Check_Registration();
    }

    public void Change_Colour(View view) {
        Forgot_Password.setTextColor(Color.BLUE);
    }


    public void Register_New_User(View view) {
        Intent New_User = new Intent(LogInActivity.this,RegisterActivity.class);

        startActivity(New_User);
        finish();
    }

    public void LogIn_User(View view) {
        if (!Phone_Number.getText().toString().isEmpty() && !Password.getText().toString().isEmpty())
        {
            boolean Found = false;

            for (int i = 0;i < contacts_list.size() ;i++)
            {
                if(contacts_list.get(i).getPhone_Number().equals(Phone_Number.getText().toString()))
                {
                    Found = true;
                    if(contacts_list.get(i).getPassword().equals(Password.getText().toString()))
                    {
                        Toast.makeText(LogInActivity.this,"Log In Success", (int) 100.0).show();
                        Intent User_Logged_In = new Intent(LogInActivity.this,Registered_Contacts.class);
                        User_Logged_In.putExtra("Phone_Number",Phone_Number.getText().toString());
                        startActivity(User_Logged_In);
                        finish();
                    }
                    else
                    {
                        Toast.makeText(LogInActivity.this,"Incorrect Password", (int) 100.0).show();
                    }
                    break;
                }
            }
            if(!Found)
            {
                Toast.makeText(LogInActivity.this,"Contact Not Found", (int) 100.0).show();
            }
        }

    }

    public void Check_Registration()
    {
        FirebaseDatabase.getInstance().getReference("Registered")
                .addChildEventListener(new ChildEventListener()
                {
                    @Override
                    public void onChildAdded(@NonNull final DataSnapshot dataSnapshot, @Nullable String s) {
                        ContactModel User_Info = dataSnapshot.getValue(ContactModel.class);
                        contacts_list.add(User_Info);
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