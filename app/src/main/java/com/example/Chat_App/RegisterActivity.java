package com.example.Chat_App;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterActivity extends AppCompatActivity {
    CircleImageView Photo;
    EditText Name;
    EditText Phone_No;
    EditText Password;
    boolean Image_Selected;
    String Image_Url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Photo = findViewById(R.id.imageviewc);
        Name = findViewById(R.id.Name);
        Phone_No = findViewById(R.id.Phone_Number);
        Password = findViewById(R.id.Password);
        Image_Selected = false;
    }

    public void Register_User(View view) {

        if(!Name.getText().toString().isEmpty() && !Phone_No.getText().toString().isEmpty() && !Password.getText().toString().isEmpty() && Image_Selected)
        {
            Intent Verify =new Intent(RegisterActivity.this,VerificationActivity.class);

            Verify.putExtra("Name",Name.getText().toString());
            Verify.putExtra("Phone_No",Phone_No.getText().toString());
            Verify.putExtra("Password",Password.getText().toString());
            Verify.putExtra("Picture_Url", Image_Url);

            startActivity(Verify);
            finish();
        }
        else
        {
            String phNumber = Phone_No.getText().toString();
            if (TextUtils.isEmpty(phNumber)){
                Phone_No.setError("Invalid Phone Number.");
            }
            if (Name.getText().toString().isEmpty()){
                Name.setError("Invalid Name.");
            }
            if (Password.getText().toString().isEmpty()){
                Password.setError("Invalid Password.");
            }
            if(!Image_Selected)
            {
                TextView Photo_Text = findViewById(R.id.picture_text);
                Photo_Text.setError("Picture Not Selected");
            }
        }

    }

    public void Select_User_Photo(View view) {
        Intent Select_Photo = new Intent(Intent.ACTION_PICK);
        Select_Photo.setType("image/*");
        startActivityForResult(Select_Photo, 0);
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK)
        {
            final Uri ImageUri = data.getData();
            uploadImage(ImageUri);
        }
    }

    private void uploadImage(Uri filePath) {

        if(filePath != null)
        {
            final StorageReference ref = FirebaseStorage.getInstance().getReference().child("Images/"+ UUID.randomUUID().toString());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            String s2 = taskSnapshot.getUploadSessionUri().toString();
                            String[] s = s2.split("/o") ;

                            String image_name = taskSnapshot.getMetadata().getName();

                            String image_url = s[0]+"/o/Images%2F"+image_name+"?alt=media";

                            Picasso.with(RegisterActivity.this).load(image_url).into(Photo);

                            Image_Url = image_url;

                            Image_Selected = true;
                            TextView Photo_Text = findViewById(R.id.picture_text);
                            Photo_Text.setError(null);
                        }
                    });
        }
    }
}
