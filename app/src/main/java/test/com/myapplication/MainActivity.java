package test.com.myapplication;

import android.Manifest;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.MediaStore.Audio.Media;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.security.Permissions;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Handler handler = new Handler();


        CheckForPermissions();
        ListView listView = findViewById(R.id.listView);
        final ArrayList<String>audioName = new ArrayList<>();
        final ArrayList<String> names = new ArrayList<>();
        final String [] abc = {Media.DISPLAY_NAME , Media.DATA};
        final Bundle bundle = new Bundle();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            final Cursor cursor = getContentResolver().query(Media.EXTERNAL_CONTENT_URI,abc,null,null);
            if (cursor!= null)
            {
                if(cursor.moveToFirst())
                {
                    do {

                        audioName.add(cursor.getString(cursor.getColumnIndex(abc[1])));
                        names.add(cursor.getString(cursor.getColumnIndex(abc[0])));


                    }while(cursor.moveToNext());
                }
                cursor.close();

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,audioName);
                listView.setAdapter(arrayAdapter);


                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        Intent startActivityIntent = new Intent(MainActivity.this ,MusicServ.class);
                        startActivityIntent.putExtra("data",audioName.get(position));
                        startActivityIntent.putExtra("name", names.get(position));
                        startService(startActivityIntent);



                    }
                });
            }

        }










    }

    void CheckForPermissions(){

        if (ContextCompat.checkSelfPermission(this , Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED){
            //Do your work
            Log.d("", "onCreate: " );
        }else {

            String persmissions[] = {Manifest.permission.READ_EXTERNAL_STORAGE , Manifest.permission.FOREGROUND_SERVICE};

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ActivityCompat.requestPermissions(this , persmissions , 100);
            }
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 100){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this ,"Granted" , Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(this ,"Not granted" , Toast.LENGTH_LONG).show();
            }
        }else {}

    }


}
