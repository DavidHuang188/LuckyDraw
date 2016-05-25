package com.david.lucky;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.Random;


public class MainActivity extends AppCompatActivity {

    private ListView list;
    private Button button;
    private ArrayAdapter<String> adapter;
    Random random = new Random();
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Intent intent = new Intent(this,Main2Activity.class);
//        startActivity(intent);
        fineViews();
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1);
        list.setAdapter(adapter);
        Firebase.setAndroidContext(this);
        String url = "https://project-7931157122344089396.firebaseio.com/pool";
        new Firebase(url).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String name = (String) dataSnapshot.child("name").getValue();
                adapter.add(name);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String name = (String) dataSnapshot.child("name").getValue();
                adapter.remove(name);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }

    private void fineViews() {
        list = (ListView) findViewById(R.id.listView);
        button = (Button) findViewById(R.id.button);
    }

    public void btnclick(View v){
        Log.d("Button Test ", "Test-----------------++++");
        int counter = adapter.getCount();
        final int num = random.nextInt(counter);
        dialog = new AlertDialog.Builder(MainActivity.this)
                .setTitle("抽獎活動")
                .setMessage(adapter.getItem(num))
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        adapter.remove(adapter.getItem(num));
                    }
                })
                .show();
        new DrawingTask().execute( adapter.getCount());
    }

    class DrawingTask extends AsyncTask<Integer, Integer, Integer>{
        @Override
        protected Integer doInBackground(Integer... params) {
            int r = 0;
            for (int i=0;i<100;i++){
                r = random.nextInt(params[0]);
                publishProgress(r);
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return r;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            dialog.setTitle("中獎人");
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            dialog.setMessage(adapter.getItem(values[0]));
        }
    }
}


