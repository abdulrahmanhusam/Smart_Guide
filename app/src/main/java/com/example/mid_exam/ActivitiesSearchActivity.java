package com.example.mid_exam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ActivitiesSearchActivity extends AppCompatActivity {

    String receivedactiv;
    private RecyclerView mRecyclerView;
    private ImageAdapter mAdapter;

    private DatabaseReference mDatabaseRef;
    private List<Uploadplace> mUploads;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activities_search);

        receivedactiv= getIntent().getStringExtra("selectedactvi");

        mRecyclerView= findViewById(R.id.recyclerview_sea);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mUploads= new ArrayList<>();

        mDatabaseRef= FirebaseDatabase.getInstance().getReference("Places");
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot postSnapshot: snapshot.getChildren()){
                    Uploadplace uploadplace=postSnapshot.getValue(Uploadplace.class);
                    if (uploadplace.getSearch().contains(receivedactiv) || receivedactiv.contains(uploadplace.getSearch())) {

                        mUploads.add(uploadplace);
                    }
                }
                mAdapter=new ImageAdapter(ActivitiesSearchActivity.this,mUploads);

                mRecyclerView.setAdapter(mAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ActivitiesSearchActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent l=new Intent(ActivitiesSearchActivity.this, Search.class);
        startActivity(l);
        //super.onBackPressed();
    }
}