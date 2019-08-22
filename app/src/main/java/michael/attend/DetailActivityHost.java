package michael.attend;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DetailActivityHost extends AppCompatActivity {

    Context context;
    Intent i;
    String time, date;
    String current_uid;
    ListView listView;
    TextView no_students;
    Button takeAttendance, stopAttendance, changeGPS;
    String[] names;
    String[] emails;
    ArrayList<User> users;
    DatabaseReference dbr,dbr1;
    String title,description, host;
    CustomListAdapter adapter;
    TextView event_title, event_description, event_host;
    String position;
    Event current;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_host);
        current_uid = LoginHome.user_uid;

        listView = (ListView) findViewById(R.id.attendees);
        no_students = (TextView) findViewById(R.id.no_students);

        context = this;

        i = getIntent();
        title = i.getStringExtra("title");
        description = i.getStringExtra("description");
        host = i.getStringExtra("host");
        Log.d("user_in_group_name", title);

        dbr1 = FirebaseDatabase.getInstance().getReference().child("total_groups").child(title);

        takeAttendance = findViewById(R.id.take_attendance);
        stopAttendance = findViewById(R.id.stop_attendance);
        changeGPS = findViewById(R.id.change_gps);

        // this sets takeAttendance to true
        takeAttendance.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss a");
                time = timeFormat.format(calendar.getTime());

                //date
                SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                date = dateFormat.format(calendar.getTime());

                //fetch numEvents from total_groups
                dbr1.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        current = new Event();
                        current.time = time;
                        current.date = date;

                        ListData ld = dataSnapshot.getValue(ListData.class);

                        position = ld.numEvents;

                        Log.d("position_log: ", position);

                        if(ld.inSession == false) {

                            dbr1.child("History").child(position).setValue(current);

                            int pos = Integer.parseInt(ld.numEvents);
                            pos++;
                            dbr1.child("numEvents").setValue(Integer.toString(pos));
                        }

                        dbr1.child("inSession").setValue(true);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                changeGPS.setEnabled(false);
                takeAttendance.setEnabled(false);
                stopAttendance.setEnabled(true);

            }
        });

        //  this sets takeAttendance to false
        stopAttendance.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dbr1.child("inSession").setValue(false);

                takeAttendance.setEnabled(true);
                changeGPS.setEnabled(true);
                stopAttendance.setEnabled(false);
            }
        });

        //changes group's gps coordinates to host's current coordinates
        changeGPS.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                int request_permission = 1;
                GPSLocator x = new GPSLocator(getApplicationContext());
                Location l = x.getLocation();
                if (l == null) {
                    ActivityCompat.requestPermissions(DetailActivityHost.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            request_permission);
                }

//
                String latitude = String.valueOf(l.getLatitude());
                String longitude = String.valueOf(l.getLongitude());

                dbr1.child("latitude").setValue(latitude);
                dbr1.child("longitude").setValue(longitude);

            }
        });



        event_title = (TextView) findViewById(R.id.group_name);
        event_description = (TextView) findViewById(R.id.group_description);
        event_host = (TextView) findViewById(R.id.host_name);

        event_title.setText(title);
        event_description.setText(description);
        event_host.setText(host);

        // populating listview with students

        dbr = FirebaseDatabase.getInstance().getReference().child("total_groups").child(title).child("Users");
        users = new ArrayList<User>();

         Log.d("user_in_group_name", dbr.child(current_uid).toString());


         dbr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
             public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    users.add(snapshot.getValue(User.class));
//                    Log.d("user_in_group_name", "inside ondatachange");

                            User user = snapshot.getValue(User.class);
//                    Log.d("user_in_group_name", user.toString());
                            users.add(user);
//                    Log.d("user_in_group_name", user.name);
                            names = new String[users.size()];
                            emails = new String[users.size()];
                        }


                        for (int q = 0; q < users.size(); q++) {
                            names[q] = users.get(q).name;
                            emails[q] = users.get(q).email;

                            Log.d("user_in_group_name", "for loop log: " + "name: " + names[q] + " email: "
                                    + emails[q] + q + " size:" + names.length);
                        }
                      try {

                          adapter = new CustomListAdapter(DetailActivityHost.this, names, emails);
                          listView.setAdapter(adapter);

                          listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                              @Override
                              public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                  if (position > -1) {
        //                            view's student's attendance history

                                      Intent detailIntent = new Intent(context, UserHistoryHostView.class);
                                      detailIntent.putExtra("name", names[position]);
                                      detailIntent.putExtra("title", title);
                                      detailIntent.putExtra("username",emails[position]);

                                      startActivity(detailIntent);

//                                      Toast.makeText(getApplicationContext(), names[position] + " - " + emails[position], Toast.LENGTH_LONG).show();
                                  }
                              }
                          });
                      }
                      catch (NullPointerException e) {

                          no_students.setVisibility(View.VISIBLE);
                          listView.setVisibility(View.INVISIBLE);
                      }
                    }



                @Override
                public void onCancelled(DatabaseError databaseError) { }
            });

    }
    public void onBackPressed(){
        finish();
    }
}
