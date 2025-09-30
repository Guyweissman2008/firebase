package com.example.firebase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class
FBRef {
    // Comments
    public static FirebaseAuth mAuth = FirebaseAuth.getInstance();
    public static FirebaseDatabase FBDB= FirebaseDatabase.getInstance();
    public static DatabaseReference refStudents= FBDB.getReference("Students");
    public static DatabaseReference refItems= FBDB.getReference("Items");


}
