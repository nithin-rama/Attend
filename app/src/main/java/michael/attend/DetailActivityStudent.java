package michael.attend;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static java.lang.Math.atan2;

public class DetailActivityStudent extends AppCompatActivity {

    Context context;
    int position;
    Intent i;
    String current_uid;
    ListView listView;
    Button recordAttendance;
    Context mContext = this;
    DatabaseReference mDataBase;
    String title;
    String time;
    String date;
    ArrayList<ListData> studentLog;
    ArrayList<ListData> displayLog;
    DatabaseReference dbr;
    ArrayList<User> userList;
    String userID;
    ArrayList<Event> eventListGroup, eventListUser, finalList;
    String[] dates, times, records;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_student);

        final Context c = this.context;
        current_uid = LoginHome.user_uid;
        i = getIntent();

        title = i.getStringExtra("title");
        String description = i.getStringExtra("description");
        String host = i.getStringExtra("host");
        position = i.getIntExtra("position", 0);

        recordAttendance = findViewById(R.id.student_take_attendance);
        studentLog = new ArrayList<ListData>();

        eventListGroup = new ArrayList<Event>();
        eventListUser = new ArrayList<Event>();
        finalList = new ArrayList<Event>();

        listView = findViewById(R.id.student_attendees);



        recordAttendance.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                DatabaseReference dbr = FirebaseDatabase.getInstance().getReference().child("total_groups").child(title);

                dbr.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ListData ld = new ListData();
                        ld = dataSnapshot.getValue(ListData.class);


                        // fetches student's current location
                        int request_permission = 1;
                        GPSLocator x = new GPSLocator(getApplicationContext());
                        Location l = x.getLocation();
                        if (l == null) {
                            ActivityCompat.requestPermissions(DetailActivityStudent.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, request_permission);
                        }

//
                        double studentLatitude = l.getLatitude();
                        double studentLongitude = l.getLongitude();
                        double groupLatitude = Double.valueOf(ld.latitude);
                        double groupLongitude = Double.valueOf(ld.longitude);

                        //distance formula to convert coordinates to miles
                        double dlat = studentLatitude - groupLatitude;
                        double dlon = studentLongitude - groupLongitude;

                        double a = Math.pow(Math.sin(dlat/2),2) + Math.cos(groupLatitude)
                                * Math.cos(studentLatitude) * Math.pow(Math.sin(dlon/2),2);
                        double c = 2 * a * Math.atan2( Math.sqrt(a), Math.sqrt(1-a) );
                        double d =  c * 3959;

                        Log.d("Nithy", "distance in miles: " + d);
                        if(ld.inSession && d < 5 ) {
                            //time
                            Calendar calendar = Calendar.getInstance();
                            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss a");
                            time = timeFormat.format(calendar.getTime());

                            //date
                            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                            date = dateFormat.format(calendar.getTime());

                            Toast.makeText(mContext,"Your attendance was recorded successfully", Toast.LENGTH_LONG).show();
                            Event event = new Event();
                            event.time = time;
                            event.date = date;
                            event.pos = ld.numEvents;

                            int i = Integer.valueOf(ld.numEvents);
                            i--;
                            String s = Integer.toString(i);

                            event.pos = s;

                            DatabaseReference dbr2 = FirebaseDatabase.getInstance().getReference().child("users").child(current_uid).
                                    child("user_groups").child("student_groups").child(title).child("History").child(s);
                            dbr2.setValue(event);

                            Log.d("uid_user","User distance: " + d);
                            Log.d("uid_user","User lat: " + studentLatitude + " User lon: " + studentLongitude);
                            Log.d("uid_user","group lat: " + groupLatitude + " group lon: " + groupLongitude);


//                            studentLog.add(logIn);
                        }
                        else if(d < 5)
                            Toast.makeText(mContext,"Class is not in session", Toast.LENGTH_LONG).show();
                        else {
                            Toast.makeText(mContext,"Too far away", Toast.LENGTH_LONG).show();
                            Log.d("uid_user","User distance: " + d);
                            Log.d("uid_user","User lat: " + studentLatitude + " User lon: " + studentLongitude);
                            Log.d("uid_user","group lat: " + groupLatitude + " group lon: " + groupLongitude);
                        }
                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });





            }
        });

        TextView event_title = (TextView) findViewById(R.id.student_group_name);
        TextView event_description = (TextView) findViewById(R.id.student_group_description);
        TextView event_host = (TextView) findViewById(R.id.student_host_name);
        current_uid = LoginHome.user_uid;

        event_title.setText(title);
        event_description.setText(description);
        event_host.setText(host);

        dbr = FirebaseDatabase.getInstance().getReference().child("total_groups").child(title);

        DatabaseReference dbr1 = FirebaseDatabase.getInstance().getReference().child("users").child(current_uid)
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

                                ListViewAdaptor eventAdapter = new ListViewAdaptor(DetailActivityStudent.this, dates, times, records);
                                listView.setAdapter(eventAdapter);
                            }
                        }
                        catch(Exception e) {

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


    public void onBackPressed(){
        finish();
    }
}
