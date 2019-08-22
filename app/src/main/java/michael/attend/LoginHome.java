package michael.attend;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class LoginHome extends AppCompatActivity {
    private FirebaseAuth.AuthStateListener authListener;
    public static FirebaseAuth auth;
    public static String user_uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_home);

        //get firebase auth instance
        auth = FirebaseAuth.getInstance();
        user_uid = auth.getCurrentUser().getUid();

        //get current user
//        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                collectNames((Map<String, Object>) dataSnapshot.getValue());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu){
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.user_menu, menu);
//        return true;
//    }

    private void collectNames(Map<String, Object> users){
        ArrayList<String> names = new ArrayList<>();

        for(Map.Entry<String,Object> entry : users.entrySet()){
            Map singleUser = (Map) entry.getValue();
            names.add((String)singleUser.get("name"));
        }
        Log.d("user_names", names.toString());
    }

//    public boolean onOptionsItemSelected(MenuItem item){
//        int id = item.getItemId();
//        if(id == R.id.menu_profile){
//            Intent intent = new Intent(LoginHome.this, ProfileActivity.class);
//            startActivity(intent);
//        }
//        return true;
//    }

    public void onClick_ViewGroups(View view) {
        startActivity(new Intent(LoginHome.this, ChooseGroupActivity.class));
    }

    public void onClick_CreateGroup(View view) {
        startActivity(new Intent(LoginHome.this, CreateGroupActivity.class));
    }

    public void onClick_JoinGroup(View view) {
        startActivity(new Intent(LoginHome.this, JoinGroupActivity.class));
    }

    public void onStop(){
        super.onStop();
        if (auth.getCurrentUser() != null) {
            FirebaseAuth.getInstance().signOut();

        }
    }
}
