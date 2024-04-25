package com.example.quickbuy2.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quickbuy2.Helper.ReadWriteUserDetails;
import com.example.quickbuy2.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationActivity extends AppCompatActivity {

    private EditText editTextFullName,editTextEmailAddress,editTextPhoneNumber,editTextPassword,editTextRepeatPassword;
    private static final String TAG ="Registration";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_registration);



        editTextFullName = findViewById(R.id.editTextFullName);
        editTextEmailAddress = findViewById(R.id.editTextEmailAddress);
        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextRepeatPassword = findViewById(R.id.editTextRepeatPassword);

        //Login
        Button logButton = findViewById(R.id.logButton);
        logButton.setOnClickListener(v -> {
            Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        Button registerButton = findViewById(R.id.registerButton);
        registerButton.setOnClickListener(v -> {
            //obtain entered data
            String textFullName = editTextFullName.getText().toString();
            String textEmail = editTextEmailAddress.getText().toString();
            String textMobile = editTextPhoneNumber.getText().toString();
            String textPassword = editTextPassword.getText().toString();
            String textConfirmPassword = editTextRepeatPassword.getText().toString();

            //Validate Mobile number using matcher and pattern(RegEx)
            /*String mobileRegex="^(0|\\\\+?254)7(\\\\d){8}$";
            Matcher mobileMatcher;
            Pattern mobilePattern = Pattern.compile(mobileRegex);
            mobileMatcher = mobilePattern.matcher(textMobile);*/

            if (TextUtils.isEmpty(textFullName)){
                Toast.makeText(RegistrationActivity.this,"Please enter your full name", Toast.LENGTH_LONG).show();
                editTextFullName.setError("Full Name is required");
                editTextFullName.requestFocus();
            } else if (TextUtils.isEmpty(textEmail)) {
                Toast.makeText(RegistrationActivity.this,"Please enter your email address", Toast.LENGTH_LONG).show();
                editTextEmailAddress.setError("Email is required");
                editTextEmailAddress.requestFocus();
            } else if (!Patterns.EMAIL_ADDRESS.matcher(textEmail).matches()) {
                Toast.makeText(RegistrationActivity.this,"Please re-enter your email address", Toast.LENGTH_LONG).show();
                editTextEmailAddress.setError("Enter Valid Email");
                editTextEmailAddress.requestFocus();

            } else if (TextUtils.isEmpty(textMobile)) {
                Toast.makeText(RegistrationActivity.this,"Please enter your Phone Number", Toast.LENGTH_LONG).show();
                editTextPhoneNumber.setError("Phone Number is required");
                editTextPhoneNumber.requestFocus();
            } else if (textMobile.length()!=10) {
                Toast.makeText(RegistrationActivity.this,"Please enter a valid phone number.", Toast.LENGTH_LONG).show();
                editTextPhoneNumber.setError("Phone Number should have 10 digits");
                editTextPhoneNumber.requestFocus();
            /*} else if (!mobileMatcher.find()) {
                Toast.makeText(Registration.this,"Please enter a valid phone number.", Toast.LENGTH_LONG).show();
                editTextPhoneNumber.setError("Phone Number is not valid");
                editTextPhoneNumber.requestFocus();*/
            } else if (TextUtils.isEmpty(textPassword)) {
                Toast.makeText(RegistrationActivity.this,"Please enter your Password", Toast.LENGTH_LONG).show();
                editTextPassword.setError("Password is required");
                editTextPassword.requestFocus();
            } else if (textPassword.length()<8) {
                Toast.makeText(RegistrationActivity.this,"Password should have at least 8 digits",Toast.LENGTH_LONG).show();
                editTextPassword.setError("Password too weak");
                editTextPassword.requestFocus();
            }else if (TextUtils.isEmpty(textConfirmPassword)) {
                Toast.makeText(RegistrationActivity.this,"Please confirm your Password", Toast.LENGTH_LONG).show();
                editTextRepeatPassword.setError("Password is required");
                editTextRepeatPassword.requestFocus();
            } else if (!textPassword.equals(textConfirmPassword)) {
                Toast.makeText(RegistrationActivity.this,"Password Does Not Match",Toast.LENGTH_LONG).show();
                editTextRepeatPassword.setError("Password does not match");
                editTextRepeatPassword.requestFocus();
                //Clear the entered Passwords
                editTextPassword.clearComposingText();
                editTextRepeatPassword.clearComposingText();
            } else {
                registerUser(textFullName,textEmail,textMobile,textPassword);
            }


         });
    }

    //register user using credentials given
    private void registerUser(String textFullName, String textEmail, String textMobile, String textPassword) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(textEmail,textPassword).addOnCompleteListener(RegistrationActivity.this, task -> {
            if(task.isSuccessful()){

                FirebaseUser firebaseUser = auth.getCurrentUser();

                //update Display Name of User
                UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(textFullName).build();
                firebaseUser.updateProfile(profileChangeRequest);

                //Enter User Data into Firebase Realtime Database;
                ReadWriteUserDetails writeUserDetails = new ReadWriteUserDetails(textFullName,textEmail,textMobile);


                //Extracting user reference from Database for "Registered Users"
                DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered Users");

                referenceProfile.child(firebaseUser.getUid()).setValue(writeUserDetails).addOnCompleteListener(task1 -> {
                    if (task.isSuccessful()){
                        //Send verification email
                        firebaseUser.sendEmailVerification();
                        Toast.makeText(RegistrationActivity.this,"User Registration Successful. Please verify your email",Toast.LENGTH_LONG).show();

                        //Open Dashboard/Home after successful registration
                        Intent intent = new Intent(RegistrationActivity.this, HomeActivity.class);
                        //noinspection BlockingMethodInNonBlockingContext
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }else {
                        Toast.makeText(RegistrationActivity.this,"User Registration Failed. Please try again",Toast.LENGTH_LONG).show();
                    }

                });

            }else {
                try {
                    throw task.getException();
                }catch (FirebaseAuthWeakPasswordException e){
                    editTextPassword.setError("Your Password is too weak. Kindly use alphanumeric and characters");
                    editTextPassword.requestFocus();
                }catch (FirebaseAuthInvalidCredentialsException e){
                    editTextPassword.setError("Your email is invalid or already in use. Kindly re-enter");
                    editTextPassword.requestFocus();
                }catch (FirebaseAuthUserCollisionException e){
                    editTextEmailAddress.setError("User already registered with this email. Use another email");
                    editTextEmailAddress.requestFocus();
                }catch (Exception e){
                    Log.e(TAG,e.getMessage());
                    Toast.makeText(RegistrationActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}