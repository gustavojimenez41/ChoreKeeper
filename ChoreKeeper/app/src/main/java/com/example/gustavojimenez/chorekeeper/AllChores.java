package com.example.gustavojimenez.chorekeeper;

        import android.content.Intent;
        import android.os.Bundle;
        import android.support.annotation.NonNull;
        import android.support.annotation.Nullable;
        import android.support.v7.app.AppCompatActivity;
        import android.util.Log;
        import android.view.View;
        import android.widget.ArrayAdapter;
        import android.widget.Button;
        import android.widget.ListView;
        import android.widget.Toast;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.auth.FirebaseUser;
        import com.google.firebase.database.ChildEventListener;
        import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.database.ValueEventListener;

        import java.util.ArrayList;

        import static java.lang.Boolean.TRUE;


public class AllChores extends AppCompatActivity {

    String housecode = null;
    Button allChores, myChores, rewards,createchore;

    private static final String TAG = "AllChores:";

    DatabaseReference dref;
    ListView listview;
    ArrayList<String> list = new ArrayList<>();
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_chores);

        allChores = findViewById(R.id.all_chores2);
        myChores = findViewById(R.id.my_chores2);
        rewards = findViewById(R.id.rewards2);
        createchore = findViewById(R.id.createChore2);

        listview = findViewById(R.id.allChoresListView);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,list);
        listview.setAdapter(adapter);

        rewards.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(AllChores.this, Rewards.class);
                startActivity(intent);
                finish();
            }
        });
        myChores.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(AllChores.this, MyChores.class);
                startActivity(intent);
                finish();
            }
        });
        allChores.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(AllChores.this, AllChores.class);
                startActivity(intent);
                finish();
            }
        });
        createchore.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(AllChores.this, CreateChore.class);
                startActivity(intent);
                finish();
            }
        });




        //get the current housecode
        ValueEventListener sethousecode = new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                housecode  = dataSnapshot.getValue(String.class);



                //once added, is triggered for every child, then once every time a new child is added
                //then again every time a new child is added.
                dref = FirebaseDatabase.getInstance().getReference("Chores");
                dref.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        //the snapshot is the chore object
                        //if the chore belongs to the current house, then we want the data
                        //if not, ignore it


                        String chorehousecode = dataSnapshot.child("housecode").getValue(String.class);

                        Log.e(TAG, "checking housecode: " + chorehousecode);

                        if(chorehousecode != null && chorehousecode.equals(housecode))
                        {


                            //the datasnapshot is the child that was added, a chore object, the key of which is the chore Id

                            //alternate way to do it
                            //didnt test this
                            //Chore newchore = dataSnapshot.getValue(Chore.class);

                            String value = dataSnapshot.getKey();

                            //retrieve all the attributes
                            String name = (String) dataSnapshot.child("name").getValue();
                            String comments = (String) dataSnapshot.child("comments").getValue();
                            long points = (long)dataSnapshot.child("points").getValue();
                            String owner = (String) dataSnapshot.child("owner").getValue();
                            String stringPoints = Long.toString(points);



                            //add the Id to the list

                            list.add("\n"+name + "\n"+ stringPoints +"pts"+"\n"+comments+"\n");



                            adapter.notifyDataSetChanged();

                            //this is where you do whatever you need to do with the data
                        }
                        else
                        {
                            //Log.e(TAG, "housecode is null");
                        }

                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError Error)
            {

            }
        };




        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        dref = FirebaseDatabase.getInstance().getReference("Users/"+user.getUid()+"/houseCode");
        dref.addListenerForSingleValueEvent(sethousecode);



    }
}
