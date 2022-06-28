package com.example.mid_exam;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.GoogleAuthProvider;

public class MainActivity extends AppCompatActivity {

    public static final int GOOGLE_SIGN_IN_CODE = 0;
    private FirebaseAuth mAuth;
     SignInButton signIn;
    GoogleSignInOptions gso;
    private GoogleSignInClient signInClient;



    EditText firstname;
    EditText lastname;
    EditText email;
    EditText password;
    EditText repeatpass;
    ProgressBar progressBar6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        TextView login = (TextView) findViewById(R.id.txt_login);
        Button signup = (Button) findViewById(R.id.btn_signup);

        signIn = findViewById(R.id.bt_sign_in);

        firstname = (EditText) findViewById(R.id.txt_fname);
        lastname = (EditText) findViewById(R.id.txt_lname);
        email = (EditText) findViewById(R.id.txt_email);
        password = (EditText) findViewById(R.id.txt_password);
        repeatpass = (EditText) findViewById(R.id.txt_repassword);
        progressBar6 = (ProgressBar) findViewById(R.id.progressBar6);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, LoginActivity.class);

                startActivity(i);
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String fname = firstname.getText().toString();
                String lname = lastname.getText().toString();
                String useremail = email.getText().toString();
                String pas1 = password.getText().toString();
                String pas2 = repeatpass.getText().toString();

                if (fname.isEmpty()) {
                    firstname.setError("First Name Is Required!");
                    firstname.requestFocus();
                    return;
                }
                if (lname.isEmpty()) {
                    lastname.setError("Last Name Is Required!");
                    lastname.requestFocus();
                    return;
                }
                if (useremail.isEmpty()) {
                    email.setError("Email Is Required!");
                    email.requestFocus();
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(useremail).matches()) {
                    email.setError("Please Enter A valid Email!");
                    email.requestFocus();
                    return;
                }
                if (pas1.isEmpty()) {
                    password.setError("Password Is Required!");
                    password.requestFocus();
                    return;
                }
                if (pas2.isEmpty()) {
                    repeatpass.setError("This field is required!");
                    repeatpass.requestFocus();
                    return;
                }
                if (pas1.length() < 6) {
                    password.setError("Minimum password length must be 6 characters!");
                    password.requestFocus();
                    return;
                }
                if(!pas1.equals(pas2)){
                    password.setError("Passwords don't match!");
                    password.requestFocus();
                    return;
                }
                progressBar6.setVisibility(View.VISIBLE);
                mAuth.createUserWithEmailAndPassword(useremail, pas1)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    User user = new User(fname, lname, useremail);

                                    FirebaseDatabase.getInstance().getReference("Users")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                                if (user.isEmailVerified()) {
                                                    Toast.makeText(MainActivity.this, "Email Already Exists Please Login", Toast.LENGTH_LONG).show();
                                                    progressBar6.setVisibility(View.GONE);
                                                    Intent i = new Intent(MainActivity.this, LoginActivity.class);
                                                    startActivity(i);
                                                } else {
                                                    user.sendEmailVerification();
                                                    progressBar6.setVisibility(View.GONE);
                                                    Toast.makeText(MainActivity.this, "Please Check your Email to verify your account", Toast.LENGTH_LONG).show();
                                                    Intent ii = new Intent(MainActivity.this, LoginActivity.class);
                                                    startActivity(ii);
                                                }
                                            }
                                        }
                                    });

                                } else {
                                    Toast.makeText(MainActivity.this, "Failed to register try again!", Toast.LENGTH_SHORT).show();
                                    progressBar6.setVisibility(View.GONE);
                                }
                            }
                        });

            }
        });
        //start sign google
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("501611323963-jfttl970bevis093hslv78f08irf5kfh.apps.googleusercontent.com")
                .requestEmail()
                .build();
        signInClient = GoogleSignIn.getClient(this, gso);

        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (signInAccount !=null || mAuth.getCurrentUser() != null){
                    startActivity(new Intent(MainActivity.this, AllPlacesActivity.class));

                }
                Intent sign =signInClient.getSignInIntent();
                startActivityForResult(sign, GOOGLE_SIGN_IN_CODE);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode== GOOGLE_SIGN_IN_CODE){
            Task<GoogleSignInAccount> signInTask = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                GoogleSignInAccount signInAcc = signInTask.getResult(ApiException.class);
                AuthCredential authCredential= GoogleAuthProvider.getCredential(signInAcc.getIdToken(),null);

                mAuth.signInWithCredential(authCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Toast.makeText(getApplicationContext(),"Your Google Account Connected To our Application",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), AllPlacesActivity.class));

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

            } catch (ApiException e) {
                e.printStackTrace();
            }

        }
    }
}