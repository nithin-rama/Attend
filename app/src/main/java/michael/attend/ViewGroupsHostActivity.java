package michael.attend;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewGroupsHostActivity extends AppCompatActivity{

    ArrayList<ListData> GroupsList;
    User user1 = LoginActivity.user1;
    private ArrayList<User> userList;
    DatabaseReference groupsRef;
    ArrayList<ListData> groups;
    FirebaseDatabase database;
    ListView group_list;
    String[] listItems;
    FirebaseAuth auth;
    Context mContext;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_groups_host);

        init_variables();
        get_view_groups_data();
    }

    public void init_variables(){
        group_list = findViewById(R.id.group_list);

        auth = FirebaseAuth.getInstance();
        uid = LoginHome.user_uid;
        database = FirebaseDatabase.getInstance();
        groupsRef = database.getReference("users");

        userList = new ArrayList<User>();
        groups = new ArrayList<ListData>();
    }

    public void get_view_groups_data(){
        GroupsList=new ArrayList<ListData>();
        mContext = this;

        //TODO: need to create userView

        //start at hostView and print out all hosted groups
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users")
                .child(uid).child("user_groups").child("host_groups");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        ListData listdata = postSnapshot.getValue(ListData.class);
                        Log.d("ondatachange_listdata", listdata.toString());

                        GroupsList.add(listdata);
                        if(GroupsList != null){
                            Log.d("ondatachange_usergroups", "groupslist not null !");

                        }else{
                            Log.d("ondatachange_usergroups", "groupslist null >:[");

                        }
                    }
                    for (int i = 0; i < GroupsList.size(); i++){
                        Log.d("ondatachange_groups", GroupsList.get(i).title);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.d("GroupList_size", String.valueOf(GroupsList.size()));


                if(GroupsList != null){
                    listItems = new String[GroupsList.size()];
                    Log.d("GroupList_size onCreate", String.valueOf(GroupsList.size()));

                }
                Log.d("listitems_size", String.valueOf(listItems.length));

                if(GroupsList != null){
                    for(int i = 0; i < GroupsList.size(); i++){
                        listItems[i] = GroupsList.get(i).title;
                    }
//            ListAdapter eventAdapter = new ArrayAdapter<ListData>(this,android.R.layout.simple_list_item_1, user1.groupList);
                    ListAdapter eventAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, listItems);
                    group_list.setAdapter(eventAdapter);
                }else{
                    Log.d("Viewgroups GroupList: ", user1.groupList.toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("ondata_error", "Error: ", databaseError.toException());
            }
        });
    }

    protected void onResume(){
        super.onResume();
        ListView group_list = findViewById(R.id.group_list);

        mContext = this;
        uid = LoginHome.user_uid;

//        GroupsList=new ArrayList<ListData>();
//        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("user_groups");
//        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                try {
//                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
//                        ListData listdata = postSnapshot.getValue(ListData.class);
//                        Log.d("ondatachange_listdata", listdata.toString());
//
//                        GroupsList.add(listdata);
//                        if(GroupsList != null){
//                            Log.d("ondatachange_usergroups", "groupslist not null !");
//
//                        }else{
//                            Log.d("ondatachange_usergroups", "groupslist null >:[");
//
//                        }
//                    }
//                    for (int i = 0; i < GroupsList.size(); i++){
//                        Log.d("ondatachange_groups", GroupsList.get(i).title);
//
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Log.d("ondata_error", "Error: ", databaseError.toException());
//            }
//        });

//        if(user1.groupList != null){
//            listItems = new String[user1.groupList.size()];
//
//        }
//        if(LoginActivity.user1.groupList != null){
//            Log.d("grouplist_not_null", "not null !");
//            Log.d("listsize", String.valueOf(LoginActivity.user1.groupList.size()));
//            for(int i = 0; i < user1.groupList.size(); i++){
//                listItems[i] = user1.groupList.get(i).title;
//                Log.d("ListItem", listItems[i]);
//            }
////            ListAdapter eventAdapter = new ArrayAdapter<ListData>(this,android.R.layout.simple_list_item_1, user1.groupList);
//            ListAdapter eventAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, listItems);
//            group_list.setAdapter(eventAdapter);
//        }else{
//            Log.d("grouplist_null", "null :((");
//            Log.d("Viewgroups GroupList: ", user1.groupList.toString());
//        }
//        if(GroupsList != null){
//            listItems = new String[GroupsList.size()];
//            Log.d("GroupList_size onResume", String.valueOf(GroupsList.size()));
//            for(int i = 0; i < GroupsList.size(); i++){
//                listItems[i] = GroupsList.get(i).title;
//                Log.d("ListItem", listItems[i]);
//            }
//            ListAdapter eventAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listItems);
//            group_list.setAdapter(eventAdapter);
//        }else{
//            Log.d("GroupList_status", "Empty");
//            Log.d("GroupList_size", String.valueOf(GroupsList.size()));
//
//        }

//        groupsRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()){
//                    User user = userSnapshot.getValue(User.class);
//                    userList.add(user);
//                    Log.d("ondatachange_key", userSnapshot.getKey());
//
//                    Log.d("ondatachange", userList.get(0).getName());
//
//                    if(userSnapshot.getKey().equals(uid)){
//                        current_user = user;
//                        groups = current_user.groupList;
//                        Log.d("onDataChange_set_user", uid);
//                        Log.d("datachange_set_user" ,"name " + current_user.getName());
////                        Log.d("ondatachange_group", user.getGroupList().toString());
//                        if(current_user.groupList != null){
//                            Log.d("ondatachange_group", "grouplist not null!");
//
//                        }else{
//                            Log.d("ondatachange_group", "grouplist null.... >:(");
//
//                        }
//
//                        break;
//                    }
////                            userList.add(user);
//                }
////                        groups = current_user.getGroupList();
////                        for(ListData group : current_user.getGroupList()){
////                            groups.add(group);
////                        }
////                        Toast.makeText(mContext, "ondatachange_group " + current_user.getGroupList().get(0).title, Toast.LENGTH_LONG).show();
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

        group_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("onDataChange_itemclick", uid);
                ListData selected = GroupsList.get(position);

                // TODO: IMPLEMENT USER_JOIN_GROUP TO POPULATE LISTDATA.ATTENDEES
//                String[] attendee_names = new String[selected.attendees.size()];
//                String[] attendee_emails = new String[selected.attendees.size()];
//                for (int i = 0; i < selected.attendees.size(); i++){
//                    attendee_names[i] = selected.attendees.get(i).name;
//                    attendee_emails[i] = selected.attendees.get(i).email;
//                }
//                Toast.makeText(mContext, selected.title, Toast.LENGTH_LONG).show(); // Toast Group Name

                Intent detailIntent = new Intent(mContext, DetailActivityHost.class);
                detailIntent.putExtra("title", selected.title);
                detailIntent.putExtra("description", selected.description);
                detailIntent.putExtra("host", selected.hostName);
                detailIntent.putExtra("position", position);
//                detailIntent.putExtra("attendee_names", attendee_names);
//                detailIntent.putExtra("attendee_emails", attendee_emails);
                startActivity(detailIntent);
//                Log.d("user" ,"name " + current_user.getName());

//                final int group_position = position;
//                groupsRef.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()){
//                            User user = userSnapshot.getValue(User.class);
//                            if(user.getName() == uid){
//                                current_user = user;
//                                Log.d("onDataChange", uid);
//                                Log.d("user" ,"name " + current_user.getName());
//
//                                break;
//                            }
////                            userList.add(user);
//                        }
////                        groups = current_user.getGroupList();
////                        for(ListData group : current_user.getGroupList()){
////                            groups.add(group);
////                        }
////                        Toast.makeText(mContext, "group " + groups.get(0).title, Toast.LENGTH_LONG).show();
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });
            }
        });
//        ListView group_list = findViewById(R.id.group_list);
//        TextView no_groups = findViewById(R.id.no_groups);

//        no_groups.setVisibility(View.VISIBLE);
//        group_list.setVisibility(View.VISIBLE);
//        if(user1.groupList != null){
//            ListAdapter eventAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, user1.groupList);
//            group_list.setAdapter(eventAdapter);
//        }else{
//            Log.d("Viewgroups GroupList: ", user1.groupList.toString());
//
//        }

    }

    public void onBackPressed(){
//        startActivity(new Intent(ViewGroupsActivity.this, LoginHome.class));
        finish();
    }
}