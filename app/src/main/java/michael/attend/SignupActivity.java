package michael.attend;

import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
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

import java.util.ArrayList;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    private EditText inputName, inputEmail, inputPassword;
    private Button btnSignIn, btnSignUp, btnResetPassword;
    private DatabaseReference mDatabase;
    private ProgressBar progressBar;
    String name, email, password;
    private FirebaseAuth auth;
    public User user1;
    ArrayList userGroupList = new ArrayList<>();
    ArrayList hostGroupList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        init_variables();

        btnResetPassword.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this, ResetPasswordActivity.class));
            }

        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {
                name = inputName.getText().toString().trim();
                email = inputEmail.getText().toString().trim();
                password = inputPassword.getText().toString().trim();
//                final ArrayList<String> groupList = new ArrayList<String>();

                if(check_registration_fields()){
                    authenticate_register_user();

                }

            }

        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }

    public void init_variables(){
        btnSignIn = (Button) findViewById(R.id.sign_in_button);
        btnSignUp = (Button) findViewById(R.id.sign_up_button);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnResetPassword = (Button) findViewById(R.id.btn_reset_password);
        inputName = (EditText) findViewById(R.id.name);
    }

    public boolean check_registration_fields(){
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
        if (password.length() < 6) {
            Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
            checkPassword = false;
        }
        if(checkEmail && checkPassword){
            result = true;
        }
        return result;

    }

    public void authenticate_register_user(){
        progressBar.setVisibility(View.VISIBLE);
        //create user
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {

                    @Override

                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Toast.makeText(SignupActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(SignupActivity.this, "Authentication failed." + task.getException(),
                                    Toast.LENGTH_LONG).show();
                        } else {
                            register_user();
//                                    ArrayList<ListData> userlist = new ArrayList<ListData>();
//                                    user1 = new User(auth.getCurrentUser().getUid(), name, email, userlist);
////                                    user1 = new User(auth.getUid(), name, email);   Create a user with id_name auth.getuid()"
////                                    DatabaseReference dbf = mDatabase.child("users").push();
//                                    auth = FirebaseAuth.getInstance();
//                                    mDatabase.child("users").child(auth.getCurrentUser().getUid()).setValue(user1);
////                                    dbf.setValue(user1);
//
////                                    mDatabase.child("users").removeValue();
//                                    startActivity(new Intent(SignupActivity.this, LoginActivity.class));
////                                    user1 = new User(name, email);
//
////                                    user1 = new User(email, groupList);
//                                    finish();
                        }
                    }

                });
    }

    public void register_user(){
        ArrayList<ListData> userlist = new ArrayList<ListData>();
        user1 = new User(auth.getCurrentUser().getUid(), name, email, userlist);
        auth = FirebaseAuth.getInstance();

        //      Create a user with id_name auth.getuid()
        mDatabase.child("users").child(auth.getCurrentUser().getUid()).setValue(user1);
        startActivity(new Intent(SignupActivity.this, LoginActivity.class));

        finish();

    }

}


