package michael.attend;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
//    private String email, password;
    private EditText inputName, inputEmail, inputPassword;
    public static FirebaseAuth auth;
    private ProgressBar progressBar;
    private Button btnSignup, btnLogin, btnReset;
    private CheckBox saveLoginCheckBox;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private Boolean saveLogin;
    public static User user1;
    ArrayList<ListData> GroupsList;
    InputMethodManager imm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set the view now
        setContentView(R.layout.activity_login);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        init_variables();

        check_remember_login();

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            }

        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ResetPasswordActivity.class));
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remember_login(v);

                if(check_login_fields()){
                    authenticate_login();

                }
            }
        });
    }

    public void init_variables(){
        inputName = (EditText) findViewById(R.id.name);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnSignup = (Button) findViewById(R.id.btn_signup);
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnReset = findViewById(R.id.btn_reset_password);
        saveLoginCheckBox = (CheckBox) findViewById(R.id.save_login);
        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();
        saveLogin = loginPreferences.getBoolean("saveLogin", false);
    }

    public void check_remember_login(){
        if(saveLogin){
            inputEmail.setText(loginPreferences.getString("email", ""));
            inputPassword.setText(loginPreferences.getString("password", ""));
            saveLoginCheckBox.setChecked(true);
        }
    }

    public void remember_login(View v){
        final String email = inputEmail.getText().toString();
        final String password = inputPassword.getText().toString();
        if(v == btnLogin){
            imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(inputEmail.getWindowToken(),0);

            if(saveLoginCheckBox.isChecked()){
                loginPrefsEditor.putBoolean("saveLogin", true);
                loginPrefsEditor.putString("email", email);
                loginPrefsEditor.putString("password", password);
                loginPrefsEditor.commit();
            }else{
                loginPrefsEditor.clear();
                loginPrefsEditor.commit();
            }
        }
    }

    public void login_get_userdata(String uid){
        GroupsList = new ArrayList<ListData>();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("user_groups");
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
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("ondata_error", "Error: ", databaseError.toException());
            }
        });
    }

    public void authenticate_login(){
        final String email = inputEmail.getText().toString();
        final String password = inputPassword.getText().toString();
        progressBar.setVisibility(View.VISIBLE);
        //authenticate user
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        progressBar.setVisibility(View.GONE);
                        if (!task.isSuccessful()) {
                            // there was an error
                            if (password.length() < 6) {
                                inputPassword.setError(getString(R.string.minimum_password));
                            } else {
                                Toast.makeText(LoginActivity.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                            }
                        } else {

                            // TODO: GET GROUP_LIST FROM DATA BASE AND ASSIGN TO USER1'S GROUPLIST WHEN CREATING USER INSTANCE
                            login_get_userdata(auth.getUid());

                            // TODO: ==========================================================
                            user1 = new User(auth.getUid(), email, GroupsList);
//                                    mDatabase.child("users").removeValue();
                            Intent intent = new Intent(LoginActivity.this, LoginHome.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
    }

    public boolean check_login_fields(){
        final String email = inputEmail.getText().toString();
        final String password = inputPassword.getText().toString();
        boolean checkEmail = true;
        boolean checkPassword = true;
        boolean result = false;
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
            checkEmail = false;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
            checkPassword = false;
        }
        if (checkEmail && checkPassword){
            result = true;
        }
        return result;

    }
}
