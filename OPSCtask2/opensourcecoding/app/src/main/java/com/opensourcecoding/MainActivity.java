package com.opensourcecoding;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
EditText txt_email, txt_password;
Button btn_register, btn_login,btn_sign_out;
String email,password;
FirebaseUser currentuser;
CardView loginCardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();

        txt_email= findViewById(R.id.txt_email);
        txt_password= findViewById(R.id.txt_password);
        btn_register = findViewById(R.id.btn_register);
        currentuser = mAuth.getCurrentUser();
        btn_login = findViewById(R.id.btn_login);
        mAuth = FirebaseAuth.getInstance();
        btn_sign_out = findViewById(R.id.btn_sign_out);
// if someone is logged in
        if(currentuser !=null){
            loginCardView .setVisibility((View.GONE));
            LoadFragment();
        }
        else
        {
            //if no one is logged in
            loginCardView.setVisibility(View.VISIBLE);
        }
btn_register.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view)
    {
      String   email = txt_email.getText().toString().trim();
       String password = txt_password.getText().toString().trim();
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if(task.isSuccessful())
                        {

                            Toast.makeText(MainActivity.this, "user created" + mAuth.getCurrentUser().getEmail(), Toast.LENGTH_SHORT).show();

                        }
                        else {
                            Toast.makeText(MainActivity.this, "Boo Boo Occurred no register", Toast.LENGTH_SHORT).show();

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "failed to create user", Toast.LENGTH_SHORT).show();

            }
        });

    }
});
btn_login.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
       String email = txt_email.getText().toString().trim();
        String password = txt_password.getText().toString().trim();
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                if(task.isSuccessful())
                {
                    loginCardView.setVisibility(View.GONE);
                    Toast.makeText(MainActivity.this, "Logged in" + mAuth.getCurrentUser().getEmail()+"Successfully", Toast.LENGTH_SHORT).show();
                    LoadFragment();
                }
                else
                {
                    Toast.makeText(MainActivity.this, "Boo Boo happened no log in for you", Toast.LENGTH_SHORT).show();
                }

            }
        }) .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
});
btn_sign_out.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        mAuth.signOut();
        Toast.makeText(MainActivity.this, "You have signed out", Toast.LENGTH_SHORT).show();
        loginCardView.setVisibility(View.VISIBLE);
    }
});

    }

    @Override
    protected void onStart()
    {
        super.onStart();
        //check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
        if (currentuser != null){
        Toast.makeText(this, "You are already logged in" + mAuth.getCurrentUser().getEmail(), Toast.LENGTH_SHORT).show();
    }
    }

    public void LoadFragment()
    {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.menu_fragment_placeHolder, MenuFragment.getInstance());
        transaction.commit();
    }
}