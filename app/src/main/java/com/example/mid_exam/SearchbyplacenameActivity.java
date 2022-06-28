package com.example.mid_exam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SearchbyplacenameActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ImageAdapter mAdapter;

    private DatabaseReference mDatabaseRef;
    private List<Uploadplace> mUploads;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchbyplacename);

        EditText enteredtxtfilter=(EditText)findViewById(R.id.txt_pnfiltersearch);
        enteredtxtfilter.requestFocus();

        enteredtxtfilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String ss=s.toString();
                mRecyclerView= findViewById(R.id.recyclerview_pnse);
                mRecyclerView.setHasFixedSize(true);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(SearchbyplacenameActivity.this));
                mUploads= new ArrayList<>();

                mDatabaseRef= FirebaseDatabase.getInstance().getReference("Places");
                mDatabaseRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot postSnapshot: snapshot.getChildren()){
                            Uploadplace uploadplace=postSnapshot.getValue(Uploadplace.class);
                            String placename=uploadplace.getPlacename();
                            if (placename.toLowerCase().contains(ss.toLowerCase()) ) {

                                mUploads.add(uploadplace);
                            }
                        }
                        mAdapter=new ImageAdapter(SearchbyplacenameActivity.this,mUploads);

                        mRecyclerView.setAdapter(mAdapter);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(SearchbyplacenameActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();

                    }
                });

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }
}