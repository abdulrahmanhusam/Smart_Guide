package com.example.mid_exam;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class ShowPlaceActivity extends AppCompatActivity {

    public String plname;
    private DatabaseReference Ref;
    private DatabaseReference Reftofavorite;
    private DatabaseReference mDatabaseRef;
    private Context nContext;

    public String pllocation;
    public String pldetail;
    public String plsearch;
    public String plimgiconurl;
    public String plplaceimgurl;
    public String plvideourl;
    public String fuserid;

    public String plexist="no";

    FirebaseAuth mAuth;

    TextView pdetails;
    MediaController mediaController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_place);

        plname = getIntent().getStringExtra("pname");
        ImageView imgplace=(ImageView)findViewById(R.id.rImage);
        pdetails=(TextView) findViewById(R.id.txtgetdetails);

        VideoView videoView= (VideoView) findViewById(R.id.vid1);

        Button removefromfavplace = (Button) findViewById(R.id.btnremovefavplace);

        //getting user id
        mAuth=FirebaseAuth.getInstance();
        FirebaseUser firebaseUser=mAuth.getCurrentUser();
        fuserid=firebaseUser.getUid();
        //check if place already on favorite places to show remove button
        mDatabaseRef=FirebaseDatabase.getInstance().getReference("Favoriteplaces").child(fuserid);
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot postSnapshot: snapshot.getChildren()){
                    Uploadplace uploadplace=postSnapshot.getValue(Uploadplace.class);
                    if (uploadplace.getPlacename().equalsIgnoreCase(plname)){
                        plexist="yes";
                    }

                }
                if (plexist.equals("yes")){
                    removefromfavplace.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ShowPlaceActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });


        //start add to favorite button
        Button addtofav = (Button) findViewById(R.id.btnaddtofavorite);
        addtofav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (plexist.equals("no")) {
                    Uploadplace uploadplaces = new Uploadplace(plname, pllocation, pldetail, plsearch, plimgiconurl, plplaceimgurl, plvideourl);
                    Reftofavorite = FirebaseDatabase.getInstance().getReference("Favoriteplaces");
                    Reftofavorite.child(fuserid).child(System.currentTimeMillis() + "").setValue(uploadplaces, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                            if (error == null) {
                                Toast.makeText(ShowPlaceActivity.this, "Added to favorite Successfully", Toast.LENGTH_SHORT).show();
                                removefromfavplace.setVisibility(View.VISIBLE);
                            } else {
                                Toast.makeText(ShowPlaceActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }

                    });
                }else{
                    Toast.makeText(ShowPlaceActivity.this, "Already in favorite places", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //remove functionality from fav places
        removefromfavplace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabaseRef=FirebaseDatabase.getInstance().getReference("Favoriteplaces").child(fuserid);
                mDatabaseRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot postSnapshot: snapshot.getChildren()){
                            Uploadplace uploadplace=postSnapshot.getValue(Uploadplace.class);
                            if (uploadplace.getPlacename().equalsIgnoreCase(plname)){
                                postSnapshot.getRef().removeValue();
                                plexist="no";
                            }

                        }
                        if (plexist.equals("no")){
                            removefromfavplace.setVisibility(View.INVISIBLE);
                            Toast.makeText(ShowPlaceActivity.this, "Removed successfully", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(ShowPlaceActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();

                    }
                });

            }
        });


        //show page content from firebase

        Ref= FirebaseDatabase.getInstance().getReference("Places");
        Ref.child(plname).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Uploadplace uploadplace= snapshot.getValue(Uploadplace.class);
                if (uploadplace !=null){

                    Picasso.with(nContext)
                            .load(uploadplace.getPlaceimguri())
                            .placeholder(R.drawable.ic_loading)
                            .fit()
                            .centerCrop()
                            .into(imgplace);

                    pdetails.setText(uploadplace.getDetails());
// for video show
                    mediaController=new MediaController(ShowPlaceActivity.this);
                    videoView.setMediaController(mediaController);
                    mediaController.setAnchorView(videoView);
                    String vids=uploadplace.getVideouri();
                    Uri videour=Uri.parse(vids);
                    videoView.setVideoURI(videour);

                    pllocation= uploadplace.getLocation();
                    pldetail= uploadplace.getDetails();
                    plsearch=uploadplace.getSearch();
                    plimgiconurl=uploadplace.getImgiconuri();
                    plplaceimgurl=uploadplace.getPlaceimguri();
                    plvideourl=uploadplace.getVideouri();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(ShowPlaceActivity.this,"Something wrong happened!",Toast.LENGTH_LONG).show();

            }
        });

    }
}