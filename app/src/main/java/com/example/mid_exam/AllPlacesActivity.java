package com.example.mid_exam;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AllPlacesActivity extends AppCompatActivity {

    TextView welcome;
    String userID;

    private RecyclerView mRecyclerView;
    private ImageAdapter mAdapter;

    private DatabaseReference mDatabaseRef;
    private List<Uploadplace> mUploads;

    FirebaseAuth mAuth;
    private DatabaseReference myRef;
    GoogleSignInClient googleSignInClient;

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allplaces);

        mRecyclerView= findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mUploads= new ArrayList<>();

        mDatabaseRef=FirebaseDatabase.getInstance().getReference("Places");
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot postSnapshot: snapshot.getChildren()){
                    Uploadplace uploadplace=postSnapshot.getValue(Uploadplace.class);
                    mUploads.add(uploadplace);

                }
                mAdapter=new ImageAdapter(AllPlacesActivity.this,mUploads);

                mRecyclerView.setAdapter(mAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AllPlacesActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });

       welcome=(TextView)findViewById(R.id.txt_welcome);

        // Initialize firebase auth for google sign in and for get user data
        mAuth=FirebaseAuth.getInstance();
        // Initialize firebase user
        FirebaseUser firebaseUser=mAuth.getCurrentUser();
        userID=firebaseUser.getUid();
        //get user f name last name
        myRef= FirebaseDatabase.getInstance().getReference("Users");


        myRef.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userprofile= snapshot.getValue(User.class);
                if (userprofile !=null){
                    String aaemail=userprofile.email;
                    String aafname=userprofile.firstname;
                    String aalname=userprofile.lastname;

                    welcome.setText("Welcome, "+aafname+" "+aalname);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AllPlacesActivity.this,"Something wrong happend!",Toast.LENGTH_LONG).show();

            }
        });

         String unamegoogle=mAuth.getCurrentUser().getDisplayName();
         welcome.setText(unamegoogle);
        // Initialize sign in client
        googleSignInClient= GoogleSignIn.getClient(AllPlacesActivity.this
                , GoogleSignInOptions.DEFAULT_SIGN_IN);

        //bottom navigation bar code
        bottomNavigationView= findViewById(R.id.bottom_navigator);
        bottomNavigationView.setSelectedItemId(R.id.home);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.news:
                        startActivity(new Intent(getApplicationContext(), Search.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.home:
                        return true;
                    case R.id.notification:
                        startActivity(new Intent(getApplicationContext(), FavoritePlaces.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(),Profile.class));
                        overridePendingTransition(0,0);
                        return true;
                }

                return false;
            }
        });

    }

}