package com.example.Chat_App;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

public class VerificationActivity extends AppCompatActivity {
    CircleImageView User_Photo;
    EditText User_Phone_No;
    EditText Pass_Code;
    Button Verification_Btn,Resend_Btn;

    boolean Valid_Ph_No = true;

    boolean Already_Excist = false;

    private boolean mVerificationInProgress = false;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        User_Photo = findViewById(R.id.User_Image_Verify);
        User_Phone_No = findViewById(R.id.Phone_No_Verify);
        Pass_Code = findViewById(R.id.PassCode);
        Verification_Btn = findViewById(R.id.Verify_Phone_Number);
        Resend_Btn = findViewById(R.id.Resend_Code);

        /*User_Phone_No.setText("+929991234567");*/

        User_Phone_No.setText(getIntent().getExtras().getString("Phone_No"));

        Picasso.with(VerificationActivity.this).load(getIntent().getExtras().getString("Picture_Url")).into(User_Photo);

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                mVerificationInProgress = false;
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                mVerificationInProgress = false;
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    Valid_Ph_No = false;
                    User_Phone_No.setError("Invalid Phone Number.");
                }
                else if (e instanceof FirebaseTooManyRequestsException)
                {
                    // The SMS quota for the project has been exceeded
                }
                Toast.makeText(VerificationActivity.this,"Verification Failed"+e.getMessage(), (int) 100.0).show();
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Already_Excist = false;
                Check_Registration();
                Valid_Ph_No = true;
                mVerificationId = verificationId;
                mResendToken = token;

            }
        };

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                User_Phone_No.getText().toString(),
                60,
                TimeUnit.SECONDS,
                this,mCallbacks);

    }


    public void Verify_Phone_Number(View view) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                User_Phone_No.getText().toString(),
                60,
                TimeUnit.SECONDS,
                this,mCallbacks);

        if(Pass_Code.getText().toString().length()==6 && Valid_Ph_No)
        {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, Pass_Code.getText().toString());
            signInWithPhoneAuthCredential(credential);
        }
        else
        {
            Pass_Code.setError("Incomplete PassCode");
        }
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            //Add Contact in FireBase

                            if(!Already_Excist)
                            {
                                Toast.makeText(VerificationActivity.this, "Successful", (int) 100.0).show();
                                DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Registered").push();
                                dbRef.child("Phone_Number").setValue(User_Phone_No.getText().toString());
                                dbRef.child("Name").setValue(getIntent().getExtras().getString("Name"));
                                dbRef.child("Password").setValue(getIntent().getExtras().getString("Password"));
                                dbRef.child("Picture_Url").setValue(getIntent().getExtras().getString("Picture_Url"));

                                Intent LogInAgain =new Intent(VerificationActivity.this,Registered_Contacts.class);

                                LogInAgain.putExtra("Phone_Number",User_Phone_No.getText().toString());
                                startActivity(LogInAgain);

                                finish();
                            }
                            else
                            {
                                Toast.makeText(VerificationActivity.this, "Phone Number Already Registerd", (int) 500.0).show();
                            }

                        }
                        else
                        {
                            // Sign in failed, display a message
                            Pass_Code.setError("Invalid PassCode");
                        }
                    }
                });
    }


    public void Check_Registration()
    {
        FirebaseDatabase.getInstance().getReference("Registered")
                .addChildEventListener(new ChildEventListener()
                {
                    @Override
                    public void onChildAdded(@NonNull final DataSnapshot dataSnapshot, @Nullable String s) {
                        ContactModel User_Info = dataSnapshot.getValue(ContactModel.class);
                        if(User_Info.getPhone_Number().contains(User_Phone_No.getText().toString()))
                        {
                            Already_Excist = true;
                        }
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    public void Resnd_Code(View view) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                User_Phone_No.getText().toString(),
                60,
                TimeUnit.SECONDS,
                this,mCallbacks,mResendToken);
        Toast.makeText(VerificationActivity.this,"PassCode Resent", (int) 100.0).show();
    }
}