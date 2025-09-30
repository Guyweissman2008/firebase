package com.example.firebase;

import static com.example.firebase.FBRef.refItems;
import static com.example.firebase.FBRef.refStudents;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RegisterLoginActivity extends AppCompatActivity {

    EditText emailEditText;
    EditText passwordEditText;
    TextView tVMsg;
    Button btnEdit;
    Button btnDelete;
    Button btnre;
    Button additem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
    }

    private void initViews() {
        emailEditText=findViewById(R.id.edittext_email);

        passwordEditText=findViewById(R.id.edittext_password);
        Students student=new Students(12,2,"Guy","331450486");
        refStudents.child("StuID").setValue(student);

        btnEdit = findViewById(R.id.button_edit);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                student.setGradeClass(10);
                refStudents.child("StuID").setValue(student);
            }
        });

        btnDelete = findViewById(R.id.button_delete);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refStudents.child("StuID").removeValue();
            }
        });
        additem=findViewById(R.id.button_item);
        additem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Item item = new Item("Milk", 1.0, 8.3);
                String keyID = refItems.push().getKey();
                item.setKeyID(keyID);
                refItems.child(item.getKeyID()).setValue(item);
            }
        });
        btnre =findViewById(R.id.button_read);
        ListView lv;
        lv= findViewById(R.id.listview);
        ArrayList<String> stuList = new ArrayList<String>();
        ArrayList<Students> stuValues = new ArrayList<Students>();

        btnre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refStudents.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dS) {
                        // Get the data…
                        stuList.clear();
                        stuValues.clear();
                        for(DataSnapshot data : dS.getChildren()) {
                           String str1 = (String) data.getKey();
                            Students stuTmp = data.getValue(Students.class);
                            stuValues.add(stuTmp);
                            String str2 = stuTmp.getStuName();
                            stuList.add(str1+" "+str2);
                        }
                      ArrayAdapter adp = new ArrayAdapter<String>(RegisterLoginActivity.this,
                                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, stuList);
                        lv.setAdapter(adp);

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }
        });
    }

    public void register(View view) {

        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        FBRef.mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            Log.d("guy","ok");
                            Toast.makeText(RegisterLoginActivity.this,"register isSuccessful:)",Toast.LENGTH_LONG).show();
                            startActivity(new Intent(RegisterLoginActivity.this,WelcomeActivity.class));
                        } else {
                            Toast.makeText(RegisterLoginActivity.this,"register failed:(",Toast.LENGTH_LONG).show();
                            Log.d("guy",task.toString());

                        }
                        Log.d("guy","after");
                    }
                });
    }

    public void login(View view) {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        FBRef.mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(RegisterLoginActivity.this,"login isSuccessful:)",Toast.LENGTH_LONG).show();
                            startActivity(new Intent(RegisterLoginActivity.this,WelcomeActivity.class));
                        } else {
                            Toast.makeText(RegisterLoginActivity.this,"login failed:(",Toast.LENGTH_LONG).show();
                            Log.d("AAA",task.toString());
                            Exception exp = task.getException();
                            if (exp instanceof FirebaseAuthInvalidUserException){
                                tVMsg.setText("Invalid email address.");
                            } else if (exp instanceof FirebaseAuthWeakPasswordException) {
                                tVMsg.setText("Password too weak.");
                            } else if (exp instanceof FirebaseAuthUserCollisionException) {
                                tVMsg.setText("User already exists.");
                            } else if (exp instanceof FirebaseAuthInvalidCredentialsException) {
                                tVMsg.setText("General authentication failure.");
                            } else if (exp instanceof FirebaseNetworkException) {
                                tVMsg.setText("Network error. Please check your connection.");
                            } else {
                                tVMsg.setText("An error occurred. Please try again later.");
                            }
                        }
                    }
                });
    }
    @Override
    protected void onStart() {
        super.onStart();
        //Boolean isChecked = sharedPref.getBoolean("stayConnect",false);
        Intent si = new Intent(RegisterLoginActivity.this,WelcomeActivity.class);
        if (FBRef.mAuth.getCurrentUser()!=null) {
            FirebaseUser user = FBRef.mAuth.getCurrentUser();
            startActivity(si);
        }
    }

}