package info.atiar.duvalcounty;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import adapter.ScheduleAdapter;
import bp.BP;
import info.atiar.duvalcounty.downloadforms.DownloadFormsActivity;
import model.ScheduleModel;

public class MainActivity extends AppCompatActivity {
    final String tag = getClass().getSimpleName() + "Atiar - ";

    ListView _listView;
    private DatabaseReference mDatabase;
    private FirebaseAuth auth;
    String userId,uniqueKey;

    ScheduleAdapter scheduleAdapter;
    private List<ScheduleModel> scheduleModelList = new ArrayList<>();

    @Nullable
    @Override
    public ActionBar getSupportActionBar() {
        return super.getSupportActionBar();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        _listView = findViewById(R.id.categoryList);
        mDatabase = FirebaseDatabase.getInstance().getReference("level0");

        if (BP.isAdmin){
            getSupportActionBar().setTitle("Duval County Admin");
        }else {
            getSupportActionBar().setTitle("Duval County");
        }
        //Firebase stuff
        auth = FirebaseAuth.getInstance();
        userId = auth.getUid();

        scheduleAdapter = new ScheduleAdapter(this, scheduleModelList);
        _listView.setAdapter(scheduleAdapter);
        scheduleAdapter.notifyDataSetChanged();

        websiteDataFromDB();
    }

    private void websiteDataFromDB(){
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                scheduleModelList.clear();

                for (DataSnapshot websiteData : dataSnapshot.getChildren()){
                    ScheduleModel scheduleModel = websiteData.getValue(ScheduleModel.class);
                    scheduleModelList.add(scheduleModel);
                }
                scheduleAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(tag, "Failed to read value.", error.toException());
            }
        });
    }

    private void popUpEditText() {
        uniqueKey = mDatabase.push().getKey();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Comments");

        final EditText input = new EditText(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        builder.setView(input);
        input.setText("");

        // Set up the buttons
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (input.getText().equals("")||input.getText().equals("")){
                    Log.e(tag, "No Input added.");
                }else {
                    //websiteLists.add(new WebsitesModel(input.getText().toString(),4+"",uniqueKey, BP.getCurrentDateTime()));
                    ScheduleModel scheduleModel = new ScheduleModel(input.getText().toString(),"0",uniqueKey, uniqueKey, BP.getCurrentDateTime());
                    mDatabase.child(uniqueKey).setValue(scheduleModel);
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }

    private MenuItem addItem;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add, menu);
        addItem = menu.findItem(R.id.menu_add_website);

        if (BP.isAdmin){
            addItem.setVisible(true);
        }else {
            addItem.setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.menu_add_website:
                popUpEditText();
                return true;
            case R.id.menu_home:
                startActivity(new Intent(MainActivity.this,MainActivity.class));
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void downloadForms(View  view){
        startActivity(new Intent(MainActivity.this, DownloadFormsActivity.class));
    }

}
