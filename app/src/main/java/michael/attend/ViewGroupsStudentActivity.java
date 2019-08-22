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
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewGroupsStudentActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_view_groups_student);
        init_variables();
        viewStudentGroupData();
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

    public void viewStudentGroupData(){
        GroupsList = new ArrayList<ListData>();
        mContext = this;

        //start at student_groups
        DatabaseReference db1 = FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("user_groups").child("student_groups");
        db1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try{
                    for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                        ListData ld = postSnapshot.getValue(ListData.class);
                        Log.d("ondatachange_student", ld.toString());

                        GroupsList.add(ld);

                        if(GroupsList != null){
                            Log.d("ondatachange_student", "not null" );
                        } else {
                            Log.d("ondatachange_student", "null");
                        }
                    }
                    for(int i = 0; i < GroupsList.size(); i++){
                        Log.d("ondatachange_student", GroupsList.get(i).title);
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
                Log.d("GroupList_size", String.valueOf(GroupsList.size()));

                if(GroupsList != null){
                    listItems = new String[GroupsList.size()];
                    Log.d("student gL_size", String.valueOf(GroupsList.size()));
                }

                Log.d("listitems_size", String.valueOf(listItems.length));

                if(GroupsList != null){
                    for(int i = 0; i < GroupsList.size(); i++){
                        listItems[i] = GroupsList.get(i).title;
                    }

                    ListAdapter eA = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, listItems);
                    group_list.setAdapter(eA);
                } else{
                    Log.d("studentgroups" , user1.groupList.toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("studentgroups error", "Error: " , databaseError.toException());
            }
        });
    }

    protected void onResume(){
        super.onResume();
        ListView group_list = findViewById(R.id.group_list);

        mContext = this;
        uid = LoginHome.user_uid;


        group_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Log.d("student_itemclick", uid);
                ListData selected = GroupsList.get(position);

                Toast.makeText(mContext, selected.title, Toast.LENGTH_LONG).show();
                Intent detailIntent = new Intent(mContext, DetailActivityStudent.class);
                detailIntent.putExtra("title", selected.title);
                detailIntent.putExtra("description", selected.description);
                detailIntent.putExtra("host", selected.hostName);
                detailIntent.putExtra("position", position);
                startActivity(detailIntent);
            }
        });


    }

    public void onBackPressed(){
        finish();
    }
}
