package michael.attend;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.google.firebase.FirebaseException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class JoinGroupActivity extends AppCompatActivity {

    TextInputEditText groupName;
    String current_uid;
    User current_user;
    DatabaseReference databaseRef;
    ArrayList<User> userList;
    User credentials;

    ArrayList<ListData> studentGroupList;
    ListData studentGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_group);
        if (current_uid == null) {
            current_uid = LoginHome.user_uid;
            Log.d("user_uid", current_uid);
        }
        Log.d("current_uid", current_uid);

    }

    public void onClick_JoinButton(View view) {
        current_user = LoginActivity.user1;
        groupName = findViewById(R.id.group_name);
        credentials = new User();

        DatabaseReference dbr = FirebaseDatabase.getInstance().getReference().child("users").child(current_uid);
        dbr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                credentials.name = user.name;
                credentials.uid = current_user.getUid();
                credentials.email = current_user.getEmail();


                databaseRef = FirebaseDatabase.getInstance().getReference();
                databaseRef.child("total_groups").child(groupName.getText().toString()).child("Users").child(current_uid).setValue(credentials);

                finish();
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        DatabaseReference db1 = FirebaseDatabase.getInstance().getReference()
                .child("Users").child(current_uid).child("user_groups").child("student_groups");

        db1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                studentGroup = new ListData();

                //add to Users at first index of 'group name' input
                DatabaseReference dbr1 = FirebaseDatabase.getInstance().getReference().child("total_groups").
                        child(groupName.getText().toString());

                dbr1.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ListData listData = dataSnapshot.getValue(ListData.class);

                        studentGroup.inSession = listData.inSession;
                        studentGroup.latitude = listData.latitude;
                        studentGroup.longitude = listData.longitude;
                        studentGroup.title = listData.title;
                        studentGroup.description = listData.description;

                        databaseRef.child("users").child(current_uid).child("user_groups")
                                .child("student_groups").child(studentGroup.title).setValue(studentGroup);


                        Log.d("student", studentGroup.toString());

                        Log.d("studentGroup", "here they are: " + listData.inSession + " " +listData.latitude + " "
                                + studentGroup.longitude);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                finish();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        // adds group to user's student groups


//        mDatabase.child("total_groups").child("wadu").setValue(userList);
    }
}

/*

totalGroups

        group 1:
        x | x@x.com
            y | y@y.com
            z | z@z.com

    group 2:
            x | x@x.com
            y | y@y.com
            z | z@z.com



Every time we make a group, we use group ID as group name (group 1) and fill it with user objects where
        - we can get user name
        - we can get user ID

        We only fill objects when users have "joined" a group ID

        This is done by searching totalGroups for a group ID, if there is a match, we will add user object into that class


*/