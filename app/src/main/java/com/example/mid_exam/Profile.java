package com.example.mid_exam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Profile extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    EditText newemail;
    EditText newfname;
    EditText newlname;

    private FirebaseUser user;
    private DatabaseReference reference;
    FirebaseAuth mAuth;
    GoogleSignInClient googleSignInClient;

    private String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        final TextView viewemail = (TextView) findViewById(R.id.vemail);
        final TextView viewfname = (TextView) findViewById(R.id.vfname);
        final TextView viewlname = (TextView) findViewById(R.id.vlname);
        TextView showupdate = (TextView) findViewById(R.id.upinfo);

        newemail = (EditText) findViewById(R.id.txtvemail);
        newfname = (EditText) findViewById(R.id.txtvfname);
        newlname = (EditText) findViewById(R.id.txtvlname);
        Button update = (Button) findViewById(R.id.btnupinfo);
        Button soutt = (Button) findViewById(R.id.btout);
        mAuth=FirebaseAuth.getInstance();


        soutt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Profile.this,LoginActivity.class);
                startActivity(i);
                mAuth.signOut();
                //googleSignInClient.signOut();
            }
        });

        showupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newemail.setVisibility(View.VISIBLE);
                newfname.setVisibility(View.VISIBLE);
                newlname.setVisibility(View.VISIBLE);
                update.setVisibility(View.VISIBLE);
                soutt.setVisibility(View.INVISIBLE);
            }
        });

        //Show user info
        user= FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference("Users");
        userid=user.getUid();

        reference.child(userid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userprofile= snapshot.getValue(User.class);
                if (userprofile !=null){
                    String email=userprofile.email;
                    String fname=userprofile.firstname;
                    String lname=userprofile.lastname;

                    viewemail.setText(email);
                    viewfname.setText(fname);
                    viewlname.setText(lname);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Profile.this,"Something wrong happend!",Toast.LENGTH_LONG).show();

            }
        });



        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                newemail.setVisibility(View.INVISIBLE);
                newfname.setVisibility(View.INVISIBLE);
                newlname.setVisibility(View.INVISIBLE);
                update.setVisibility(View.INVISIBLE);
                soutt.setVisibility(View.VISIBLE);
                reference.child(userid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String newemailst=newemail.getText().toString();
                        String newfnamest=newfname.getText().toString();
                        String newlanmest=newlname.getText().toString();
                        User userprofile= snapshot.getValue(User.class);
                        if (userprofile !=null){
                            String emailn=userprofile.email;
                            String fname=userprofile.firstname;
                            String lname=userprofile.lastname;

                            if (!newemailst.equals(emailn) ){
                                if (!newemailst.isEmpty()){
                                reference.child(userid).child("email").setValue(newemailst);
                                Toast.makeText(Profile.this,"Updated Successfully ",Toast.LENGTH_SHORT).show();}
                            }else {
                                Toast.makeText(Profile.this,"already your current email ",Toast.LENGTH_LONG).show();
                            }
                            if (!newfnamest.equals(fname) ){
                                if(!newfnamest.isEmpty()){
                                reference.child(userid).child("firstname").setValue(newfnamest);
                                Toast.makeText(Profile.this,"Updated Successfully ",Toast.LENGTH_SHORT).show();}
                            }else {
                                Toast.makeText(Profile.this,"already your current first name ",Toast.LENGTH_LONG).show();
                            }
                            if (!newlanmest.equals(lname) ){
                                if(!newlanmest.isEmpty()){
                                reference.child(userid).child("lastname").setValue(newlanmest);
                                Toast.makeText(Profile.this,"Updated Successfully ",Toast.LENGTH_SHORT).show();}
                            }else {
                                Toast.makeText(Profile.this,"already your current first name ",Toast.LENGTH_LONG).show();
                            }

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(Profile.this,"Something wrong happend!",Toast.LENGTH_LONG).show();

                    }
                });


            }
        });

        //bottom navigation bar code
        bottomNavigationView= findViewById(R.id.bottom_navigator);
        bottomNavigationView.setSelectedItemId(R.id.profile);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.news:
                        startActivity(new Intent(getApplicationContext(), Search.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), AllPlacesActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.notification:
                        startActivity(new Intent(getApplicationContext(), FavoritePlaces.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.profile:
                        return true;
                }

                return false;
            }
        });

    }
}