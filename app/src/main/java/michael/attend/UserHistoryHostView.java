package michael.attend;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserHistoryHostView extends AppCompatActivity {

    String title, username;
    Intent i;
    ListView listView;
    DatabaseReference dbr;
    ArrayList<User> userList;
    String userID;
    ArrayList<Event> eventListGroup, eventListUser, finalList;
    Context mContext;
    String[] dates, times, records;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_history_host_view);
        listView = findViewById(R.id.listView);

        mContext = this;
        i = getIntent();
        title = i.getStringExtra("title");
        username = i.getStringExtra("username");
        userList = new ArrayList<User>();
        eventListGroup = new ArrayList<Event>();
        eventListUser = new ArrayList<Event>();
        finalList = new ArrayList<Event>();

        Log.d("uid_user", "username: " + username);

        dbr = FirebaseDatabase.getInstance().getReference().child("total_groups").child(title);

        dbr.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                        User user = postSnapshot.getValue(User.class);
                        userList.add(user);
                        Log.d("test123", "user: " + user);

                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                    Log.d("uid_user", "Error");
                }

                Log.d("uid_user", "user list: " + userList.toString());

                for(int i = 0; i < userList.size(); i++) {

                    if (userList.get(i).email.equals(username))
                        userID = userList.get(i).uid;
                }

                Log.d("uid_user", "user id: " + userID);

                DatabaseReference dbr1 = FirebaseDatabase.getInstance().getReference().child("users").child(userID)
                        .child("user_groups").child("student_groups").child(title).child("History");

                dbr1.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            Event event = postSnapshot.getValue(Event.class);
                            eventListUser.add(event);

                        }

                        dbr.child("History").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                try {
                                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                                        Event event = postSnapshot.getValue(Event.class);
                                        eventListGroup.add(event);

                                    }
                                }
                                catch (Exception e) {
                                    e.printStackTrace();
                                }
                                try {
                                    for (int i = 0; i < eventListGroup.size(); i++) {

                                        int q = 0;
                                        Event temp = new Event();
                                        if (eventListUser.get(q).pos.equals(String.valueOf(i))) {
                                            temp = eventListGroup.get(i);
                                            temp.record = "present";
                                            finalList.add(temp);
                                            q++;
                                        } else {
                                            temp = eventListGroup.get(i);
                                            temp.record = "absent";
                                            finalList.add(temp);
                                        }
                                        dates = new String[finalList.size()];
                                        times = new String[finalList.size()];
                                        records = new String[finalList.size()];

                                        for (int j = 0; j < finalList.size(); j++) {
                                            dates[j] = finalList.get(j).date;
                                            times[j] = finalList.get(j).time;
                                            records[j] = finalList.get(j).record;
                                        }

                                        ListViewAdaptor eventAdapter = new ListViewAdaptor(UserHistoryHostView.this, dates, times, records);
                                        listView.setAdapter(eventAdapter);
                                    }
                                }
                                catch (Exception e) {

                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    public void onBackPressed(){
        finish();
    }
}
