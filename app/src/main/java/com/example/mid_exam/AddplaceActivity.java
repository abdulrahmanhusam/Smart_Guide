package com.example.mid_exam;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AddplaceActivity extends AppCompatActivity {

    EditText pname;
    EditText plocation;
    EditText pdetails;

    private Button chooseimgic;
    private Button choosepic;
    private Button choosevideo;
    private ProgressBar progressBaradd;

    private CheckBox gardenchk,nightchk,waterchk,walkingchk,mountainchk,fishingchk;

    private Uri imgicUri;
    private Uri picUri;
    private Uri videoUri;

    private static final int PICK_IMAGE_REQUEST=1;
    private static final int PICK_IMAGE_REQUEST2=2;
    private static final int PICK_VIDEO_REQUEST=3;

    private StorageReference storageRef;
    private DatabaseReference databaseReference;

    public String urlic;
    public String urlpic;
    public String urlvid;
    public String placename;
    public String activities="";
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addplace);

        gardenchk= findViewById(R.id.chkgarden_se);
        nightchk= findViewById(R.id.chknight_se);
        waterchk= findViewById(R.id.chkwater_se);
        walkingchk= findViewById(R.id.chkwalking_se);
        mountainchk= findViewById(R.id.chkmountain_se);
        fishingchk= findViewById(R.id.chkfishing_se);

        pname = (EditText) findViewById(R.id.txt_pname);
        plocation = (EditText) findViewById(R.id.txt_ploc);
        pdetails = (EditText) findViewById(R.id.txt_pdetails);
        progressBaradd= findViewById(R.id.progress_baradd);

        Button addplace = (Button) findViewById(R.id.btn_addplace);

        chooseimgic= findViewById(R.id.btn_chooseim_ic);
        choosepic= findViewById(R.id.btn_choosepic);
        choosevideo= findViewById(R.id.btn_choosevid);

        storageRef= FirebaseStorage.getInstance().getReference("uploads");
        databaseReference= FirebaseDatabase.getInstance().getReference("Places");


        chooseimgic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChooseImage();
            }

        });
        choosepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChooseImage2();
            }
        });
        choosevideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChooseVideo();
            }
        });


        addplace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check if fileds empty
                String placenamechekifempty=pname.getText().toString();
                String placelocchekifempty=plocation.getText().toString();
                String placedetchekifempty=pdetails.getText().toString();
                if (placenamechekifempty.isEmpty()) {
                    pname.setError("Place Name Is Required!");
                    pname.requestFocus();
                    return;
                }
                if (placelocchekifempty.isEmpty()) {
                    plocation.setError("Place Location Is Required!");
                    plocation.requestFocus();
                    return;
                }
                if (placedetchekifempty.isEmpty()) {
                    pdetails.setError("Place Details Is Required!");
                    pdetails.requestFocus();
                    return;
                }
                if (imgicUri ==null){
                    Toast.makeText(AddplaceActivity.this,"please Choose place image icon",Toast.LENGTH_LONG).show();
                    return;
                }
                if (picUri ==null){
                    Toast.makeText(AddplaceActivity.this,"please Choose place photo",Toast.LENGTH_LONG).show();
                    return;
                }
                if (videoUri ==null){
                    Toast.makeText(AddplaceActivity.this,"please Choose place video",Toast.LENGTH_LONG).show();
                    return;
                }

                //getting selected  activites
                if(gardenchk.isChecked()){
                    activities+=" "+gardenchk.getText().toString();
                }
                if(nightchk.isChecked()){
                    activities+=" "+nightchk.getText().toString();
                }
                if(waterchk.isChecked()){
                    activities+=" "+waterchk.getText().toString();
                }
                if(walkingchk.isChecked()){
                    activities+=" "+walkingchk.getText().toString();
                }
                if(mountainchk.isChecked()){
                    activities+=" "+mountainchk.getText().toString();
                }
                if(fishingchk.isChecked()){
                    activities+=" "+fishingchk.getText().toString();
                }


                    uploadFiles();


            }
        });

    }

    private void ChooseVideo() {
        Intent intent=new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_VIDEO_REQUEST);

    }

    private void ChooseImage2() {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST2);

    }

    private void ChooseImage() {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==PICK_IMAGE_REQUEST && resultCode==RESULT_OK
                && data != null && data.getData()!=null){
            imgicUri=data.getData();
            Toast.makeText(AddplaceActivity.this,"Icon Image Selected Successfully",Toast.LENGTH_SHORT).show();

        }
        if (requestCode==PICK_IMAGE_REQUEST2 && resultCode==RESULT_OK
                && data != null && data.getData()!=null){
            picUri=data.getData();
            Toast.makeText(AddplaceActivity.this,"place Image Selected Successfully",Toast.LENGTH_SHORT).show();
        }
        if (requestCode==PICK_VIDEO_REQUEST && resultCode==RESULT_OK
                && data != null && data.getData()!=null){
            videoUri=data.getData();
            Toast.makeText(AddplaceActivity.this,"Video Selected Successfully",Toast.LENGTH_SHORT).show();
        }

    }

    private String getFileExtension(Uri uri){
        ContentResolver cR=getContentResolver();
        MimeTypeMap mime=MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFiles() {
        progressBaradd.setVisibility(View.VISIBLE);
        if (imgicUri !=null){
            StorageReference reference=storageRef.child("images").child(System.currentTimeMillis()
            +"."+getFileExtension(imgicUri));

            UploadTask uploadTask=reference.putFile(imgicUri);
            Task<Uri> urlTask=uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if(!task.isSuccessful()){
                        throw task.getException();
                    }
                    return reference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()){
                        Uri downloadUri= task.getResult();
                        urlic=downloadUri.toString();
                    }
                }
            });


        }
        if (picUri !=null){
            StorageReference reference=storageRef.child("images").child(System.currentTimeMillis()
                    +"."+getFileExtension(picUri));

            UploadTask uploadTask=reference.putFile(picUri);
            Task<Uri> urlTask=uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if(!task.isSuccessful()){
                        throw task.getException();
                    }
                    return reference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()){
                        Uri downloadUri= task.getResult();
                        urlpic=downloadUri.toString();
                    }
                }
            });


        }
        if (videoUri !=null){
            StorageReference reference=storageRef.child("videos").child(System.currentTimeMillis()
                    +"."+getFileExtension(videoUri));

            UploadTask uploadTask=reference.putFile(videoUri);
            Task<Uri> urlTask=uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if(!task.isSuccessful()){
                        throw task.getException();
                    }
                    return reference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()){
                        Uri downloadUri= task.getResult();
                        urlvid=downloadUri.toString();

                        progressBaradd.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplicationContext(),"Place Added Successful",Toast.LENGTH_SHORT).show();
                        Uploadplace uploadplace=new Uploadplace(pname.getText().toString(),plocation.getText().toString(),
                                pdetails.getText().toString(),activities,urlic,urlpic,urlvid);

                        placename=pname.getText().toString();

                        FirebaseDatabase.getInstance().getReference("Places").child(placename).setValue(uploadplace);
                        Intent i=new Intent(AddplaceActivity.this,AdminActivity.class);
                        startActivity(i);

                    }else{
                        Toast.makeText(getApplicationContext(),"Something Wrong Happened!",Toast.LENGTH_SHORT).show();
                    }


                }
            });


        }
    }
}