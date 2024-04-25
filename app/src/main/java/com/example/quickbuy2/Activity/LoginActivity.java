package com.example.quickbuy2.Activity;

import static com.example.quickbuy2.R.id.editTextEmailAddress2;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quickbuy2.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private EditText loginEmail, loginPassword;
    private FirebaseAuth authProfile;
    private  static final String TAG = "Login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);


        loginEmail=findViewById(editTextEmailAddress2);
        loginPassword=findViewById(R.id.pass_2);

        authProfile=FirebaseAuth.getInstance();

        //Register
        Button regButton = findViewById(R.id.regButton);
        regButton.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
            startActivity(intent);
        });

        //login user
        Button buttonLogin=findViewById(R.id.button_login);
        buttonLogin.setOnClickListener(v -> {
            String textEmail = loginEmail.getText().toString();
            String textPassword = loginPassword.getText().toString();

            if(TextUtils.isEmpty(textEmail)){
                Toast.makeText(LoginActivity.this,"Please enter your email",Toast.LENGTH_LONG).show();
                loginEmail.setError("Email is required");
                loginEmail.requestFocus();
            } else if (!Patterns.EMAIL_ADDRESS.matcher(textEmail).matches()) {
                Toast.makeText(LoginActivity.this,"Please enter valid email",Toast.LENGTH_LONG).show();
                loginEmail.setError("Valid is required");
                loginEmail.requestFocus();
            } else if (TextUtils.isEmpty(textPassword)) {
                Toast.makeText(LoginActivity.this,"Please enter your password",Toast.LENGTH_LONG).show();
                loginPassword.setError("Password is required");
                loginPassword.requestFocus();
            }else {
                loginUser(textEmail,textPassword);
            }
        });
    }

    private void loginUser(String email, String password) {
        authProfile.signInWithEmailAndPassword(email,password).addOnCompleteListener(task -> {
            if(task.isSuccessful()){


                //Get instance of the current user
                FirebaseUser firebaseUser = authProfile.getCurrentUser();

                //Check if email is verified
                if(firebaseUser.isEmailVerified()){
                    Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    //Open User Profile
                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                    finish();

                }else {
                    firebaseUser.sendEmailVerification();
                    authProfile.signOut(); //sign out user
                    showAlertDialog();
                }
            }else{
                try {
                    throw task.getException();
                }catch(FirebaseAuthInvalidUserException e){
                    loginEmail.setError("User does not exist, please register");
                    loginEmail.requestFocus();
                }catch (FirebaseAuthInvalidCredentialsException e){
                    loginEmail.setError("Invalid credentials, kindly re-enter");
                    loginEmail.requestFocus();
                }catch (Exception e){
                    Log.e(TAG, e.getMessage());
                    Toast.makeText(LoginActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void showAlertDialog() {
        //set u p alert builder
        AlertDialog.Builder builder  = new AlertDialog.Builder(LoginActivity.this);
        builder.setTitle("Email is not verified");
        builder.setMessage("Please verify your email now. You cannot login without email verification.");

        //Open Email App if user clicks/taps Continue.
        builder.setPositiveButton("Continue", (dialog, which) -> {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_APP_EMAIL);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //OPENS EMAIL IN NEW WINDOW
            startActivity(intent);
        });
        //create the AlertDialog
        AlertDialog alertDialog = builder.create();

        //Show the AlertDialog
        alertDialog.show();

    }
    //Check if User is already logged in
    @Override
    protected void onStart() {
        super.onStart();
        if (authProfile.getCurrentUser() !=null){
            Toast.makeText(LoginActivity.this,"Already Logged In!",Toast.LENGTH_SHORT).show();

            //Start the UserProfileActivity
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            finish();
        }else {
            Toast.makeText(LoginActivity.this,"You cam login now!",Toast.LENGTH_SHORT).show();
        }
    }
}