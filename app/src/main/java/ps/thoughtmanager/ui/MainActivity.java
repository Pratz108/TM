package ps.thoughtmanager.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ps.thoughtmanager.R;
import ps.thoughtmanager.models.User;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton negative, positive;
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    FirebaseAuth psAuth;
    FirebaseUser currentUser;
    DatabaseReference dbRef;
    int pCount, nCount;

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
