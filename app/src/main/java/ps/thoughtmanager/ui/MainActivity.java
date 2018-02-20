package ps.thoughtmanager.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import ps.thoughtmanager.R;
import ps.thoughtmanager.models.User;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton negative, positive;
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    FirebaseAuth psAuth;
    FirebaseUser currentUser;
    DatabaseReference dbRef;
    int pCount, nCount;
    private StorageReference mStr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        psAuth = FirebaseAuth.getInstance();
        negative = findViewById(R.id.negativeBtn);
        positive = findViewById(R.id.positiveBtn);
        negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbRef.getRef().child("negative").setValue(++nCount);
                if (nCount >= pCount) {
                    mStr = FirebaseStorage.getInstance().getReference();
                    Random r = new Random(5);
                    int n = r.nextInt(5);
                    Log.d(getLocalClassName(), "the value of n is " + n );
                    StorageReference cRef = mStr.child("quotes/" + n + ".jpeg");
                    try {
                        final File localFile = File.createTempFile("images", "jpeg");
                        cRef.getFile(localFile)
                                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                                        if(localFile.exists()){

                                            Bitmap myBitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());

                                            final ImageView myImage = (ImageView) findViewById(R.id.image);

                                            myImage.setImageBitmap(myBitmap);
                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    myImage.setImageBitmap(null);
                                                }
                                            },1000);


                                        }


                                        // Successfully downloaded data to local file
                                        // ...
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle failed download
                                // ...
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    Toast.makeText(MainActivity.this, "Smile na Bro..!!", Toast.LENGTH_LONG).show();
                }
            }
        });
        positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbRef.getRef().child("positive").setValue(++pCount);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        currentUser = psAuth.getCurrentUser();
        if (currentUser == null) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        } else {
            String email = currentUser.getEmail();
            String user_email = email.substring(0, email.indexOf('.'));
            dbRef = db.getReference(user_email);
            dbRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    pCount = user.getPositive();
                    nCount = user.getNegative();
                    Log.d("positive", Integer.toString(pCount));
                    Log.d("negative", Integer.toString(nCount));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d("dataChanged", "failed ");
                }
            });
        }
    }
}
