package com.example.Chat_App;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class Registered_Contacts extends AppCompatActivity {
    RecyclerView rv;
    List<ContactModel> ls;
    RVAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registered__contacts);

        rv=findViewById(R.id.rvs);
        ls=new ArrayList<>();

        adapter=new RVAdapter(ls,Registered_Contacts.this,getIntent().getExtras().getString("Phone_Number"));
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(Registered_Contacts.this);
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(adapter);

        FirebaseDatabase.getInstance().getReference("Registered")
        .addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull final DataSnapshot dataSnapshot, @Nullable String s) {
                ContactModel cts = dataSnapshot.getValue(ContactModel.class);
                if(!cts.getPhone_Number().equals(getIntent().getExtras().getString("Phone_Number")))
                {
                    ls.add(cts);
                    adapter.notifyDataSetChanged();
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

    public void Search_Contacts(View view) {
        TextView Search_Count = findViewById(R.id.Search_Counts);
        ImageButton Search_Btn = findViewById(R.id.Search_Btn);
        ImageButton Hide_Btn = findViewById(R.id.Hide_Searches);
        EditText Search_String = findViewById(R.id.Search_String);

        Search_Btn.setVisibility(View.INVISIBLE);
        Hide_Btn.setVisibility(View.VISIBLE);
        Search_String.setVisibility(View.INVISIBLE);
        RecyclerView rv_search=findViewById(R.id.search_rvs);
        rv.setVisibility(View.INVISIBLE);

        List<ContactModel> ls_search;
        RVAdapter adapter_search;

        ls_search=new ArrayList<>();

        adapter_search=new RVAdapter(ls_search,Registered_Contacts.this,getIntent().getExtras().getString("Phone_Number"));
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(Registered_Contacts.this);
        rv_search.setLayoutManager(layoutManager);
        rv_search.setAdapter(adapter_search);

        for(int i=0;i<ls.size();i++)
        {
            if(ls.get(i).getName().contains(Search_String.getText().toString()) || ls.get(i).getPhone_Number().contains(Search_String.getText().toString()))
            {
                ls_search.add(ls.get(i));
            }
        }

        Search_Count.setText(ls_search.size()+" Contacts Found");
        Search_Count.setVisibility(View.VISIBLE);
        rv_search.setVisibility(View.VISIBLE);
        adapter_search.notifyDataSetChanged();
    }

    public void Cancel_Search(View view)
    {
        TextView Search_Count = findViewById(R.id.Search_Counts);
        ImageButton Search_Btn = findViewById(R.id.Search_Btn);
        ImageButton Hide_Btn = findViewById(R.id.Hide_Searches);
        EditText Search_String = findViewById(R.id.Search_String);
        RecyclerView rv_search=findViewById(R.id.search_rvs);

        Search_Count.setVisibility(View.INVISIBLE);
        Hide_Btn.setVisibility(View.INVISIBLE);
        Search_Btn.setVisibility(View.VISIBLE);
        Search_String.setVisibility(View.VISIBLE);
        rv_search.setVisibility(View.INVISIBLE);
        Search_String.setText("");
        rv.setVisibility(View.VISIBLE);
    }
}
