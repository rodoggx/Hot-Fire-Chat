package com.example.rodoggx.hotfirechat;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String ANONYMOUS = "anonymous";
    public static final int DEFAULT_MSG_LENGTH_LIMIT = 1000;
    public static final int RC_SIGN_IN = 1;

    private ListView messageListView;
    private MessageAdapter messageAdapter;
    private ProgressBar progressBar;
    private ImageButton photoPickerButton;
    private EditText messageEditText;
    private Button sendButton;
    private FirebaseDatabase firebaseDb;
    private DatabaseReference dbReference;
    private ChildEventListener childEventListener;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userName = ANONYMOUS;

        firebaseDb = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        dbReference = firebaseDb.getReference().child("messageItems");

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        messageListView = (ListView) findViewById(R.id.messageListView);
        photoPickerButton = (ImageButton) findViewById(R.id.photoPickerButton);
        messageEditText = (EditText) findViewById(R.id.messageEditText);
        sendButton = (Button) findViewById(R.id.sendButton);

        final List<MessageItem> messageItems = new ArrayList<>();
        messageAdapter = new MessageAdapter(this, R.layout.message, messageItems);
        messageListView.setAdapter(messageAdapter);

        progressBar.setVisibility(ProgressBar.INVISIBLE);

        photoPickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        messageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.toString().trim().length() > 0) {
                    sendButton.setEnabled(true);
                } else {
                    sendButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        messageEditText.setFilters(new InputFilter[] {
                new InputFilter.LengthFilter(DEFAULT_MSG_LENGTH_LIMIT)
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessageItem newMessageItem = new MessageItem(messageEditText.getText().toString(), userName, null);
                dbReference.push().setValue(newMessageItem);
                messageEditText.setText("");
            }
        });
        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                MessageItem newMessageItem = dataSnapshot.getValue(MessageItem.class);
                messageAdapter.add(newMessageItem);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        dbReference.addChildEventListener(childEventListener);
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    //user sign in
                    Toast.makeText(MainActivity.this, "Welcome to Hot Fire Chat", Toast.LENGTH_SHORT).show();
                } else {
                    //user sign out
                    startActivityForResult(
                            // Get an instance of AuthUI based on the default app
                            AuthUI.getInstance().createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setAvailableProviders(
                                            Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                                          new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()))
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        firebaseAuth.removeAuthStateListener(authStateListener);
    }
}
