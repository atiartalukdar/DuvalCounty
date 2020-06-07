package adapter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import bp.BP;
import info.atiar.duvalcounty.Main1Activity;
import info.atiar.duvalcounty.Main2Activity;
import info.atiar.duvalcounty.Main3Activity;
import info.atiar.duvalcounty.MainActivity;
import info.atiar.duvalcounty.R;
import model.ScheduleModel;

public class ScheduleAdapter extends BaseAdapter {
    final String tag = getClass().getSimpleName() + "Atiar - ";

    private DatabaseReference mDatabase;
    private Context context;
    private Activity activity;
    private LayoutInflater inflater;
    private List<ScheduleModel> data;
    ScheduleModel scheduleModel = new ScheduleModel();
    String uniqueKey;
    String parent,p;
    String level;
    private final String TAG = getClass().getSimpleName() + " Atiar= ";

    public ScheduleAdapter(Activity activity, List<ScheduleModel> data) {
        this.activity = activity;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int location) {
        return data.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        context = activity.getApplicationContext();
        scheduleModel = new ScheduleModel();
        scheduleModel = data.get(position);

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_item, null);


        LinearLayout _lout = convertView.findViewById(R.id.lout);
        TextView _number = convertView.findViewById(R.id.itemTV);
        Button _editItem = convertView.findViewById(R.id.editItem);
        Button _deleteItem = convertView.findViewById(R.id.deleteItem);
        _number.setText(scheduleModel.getName());

        if (BP.isAdmin){
            _lout.setVisibility(View.VISIBLE);
            _editItem.setVisibility(View.VISIBLE);
        }else {
            _lout.setVisibility(View.GONE);
            _editItem.setVisibility(View.GONE);
        }

        _number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(tag, data.get(position).toString());
                p = data.get(position).getUniqueID();
                switch (scheduleModel.getLevel()){
                    case "0":
                        Log.e(tag, "now  in DB ref level1");
                        Intent intent0 = new Intent(context, Main1Activity.class);
                        intent0.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent0.putExtra("data", data.get(position));
                        context.startActivity(intent0);
                        break;
                    case "1":
                        Log.e(tag, "now  in DB ref level2");
                        Intent intent = new Intent(context, Main2Activity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("data", data.get(position));
                        context.startActivity(intent);
                        break;
                    case "2":
                        Log.e(tag, "now  in DB ref level3");
                        Intent intent1 = new Intent(context, Main3Activity.class);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent1.putExtra("data", data.get(position));
                        context.startActivity(intent1);
                        break;
                }
                notifyDataSetChanged();
            }
        });

        _editItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDataEditText(position);
            }
        });

        _deleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDataEditText(position);
            }
        });
        return convertView;

    }

    //To update the searchView items in TransportList Activity
    public void update(List<ScheduleModel> resuls){
        data = new ArrayList<>();
        data.addAll(resuls);
        notifyDataSetChanged();
    }

    public void popUpEditText(int position) {

        switch (data.get(position).getLevel()){
            case "1":
                mDatabase = FirebaseDatabase.getInstance().getReference("level2");
                level = "2";
                break;
            case "2":
                mDatabase = FirebaseDatabase.getInstance().getReference("level3");
                level = "3";
                break;
        }

        uniqueKey = mDatabase.push().getKey();;
        parent = data.get(position).getUniqueID();

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Enter the text");

        final EditText input = new EditText(activity);
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
                    ScheduleModel scheduleModel = new ScheduleModel(input.getText().toString(),level,uniqueKey,parent, BP.getCurrentDateTime());
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

    public void updateDataEditText(final int position) {

        switch (data.get(position).getLevel()){
            case "0":
                mDatabase = FirebaseDatabase.getInstance().getReference("level0");
                break;
            case "1":
                mDatabase = FirebaseDatabase.getInstance().getReference("level1");
                break;
            case "2":
                mDatabase = FirebaseDatabase.getInstance().getReference("level2");
                break;
            case "3":
                mDatabase = FirebaseDatabase.getInstance().getReference("level3");
                break;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Enter the text");

        final EditText input = new EditText(activity);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        builder.setView(input);
        input.setText(data.get(position).getName());

        // Set up the buttons
        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (input.getText().equals("")||input.getText().equals("")){
                    Log.e(tag, "No Input added.");
                }else {
                    //websiteLists.add(new WebsitesModel(input.getText().toString(),4+"",uniqueKey, BP.getCurrentDateTime()));
                    data.get(position).setName(input.getText().toString());
                    mDatabase.child(data.get(position).getUniqueID()).setValue(data.get(position));
                    data.clear();
                    notifyDataSetChanged();
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

    public void deleteDataEditText(final int position) {


        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Are you sure want to  delete");

        // Set up the buttons
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                switch (data.get(position).getLevel()){
                    case "0":
                        deleteLevel0(position);
                        break;
                    case "1":
                        deleteLevel1(position);
                        break;
                    case "2":
                        deleteLevel2(position);
                        break;
                    case "3":
                        deleteLevel3(position);
                        break;
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

    private void deleteLevel3(int  pos){
        mDatabase = FirebaseDatabase.getInstance().getReference("level3");

        //set what would happen when positive button is clicked
        Query applesQuery = mDatabase.orderByChild("uniqueID").equalTo(data.get(pos).getUniqueID());
        applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot appleSnapshot : dataSnapshot.getChildren()) {
                    appleSnapshot.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(tag, "onCancelled", databaseError.toException());
            }
        });


    }

    private void deleteLevel2(final int  pos){
        mDatabase = FirebaseDatabase.getInstance().getReference("level2");
        final DatabaseReference mDatabase3 = FirebaseDatabase.getInstance().getReference("level3");

        //set what would happen when positive button is clicked
        Query applesQuery = mDatabase3.orderByChild("parent").equalTo(data.get(pos).getUniqueID());
        applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot appleSnapshot : dataSnapshot.getChildren()) {
                    appleSnapshot.getRef().removeValue();
                }

                Query applesQuery1 = mDatabase.orderByChild("uniqueID").equalTo(data.get(pos).getUniqueID());
                applesQuery1.addListenerForSingleValueEvent( new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot appleSnapshot : dataSnapshot.getChildren()) {
                            appleSnapshot.getRef().removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(tag, "onCancelled", databaseError.toException());
            }
        });


    }

    private void deleteLevel1(final int  pos){
        mDatabase = FirebaseDatabase.getInstance().getReference("level1");
        final DatabaseReference mDatabase2 = FirebaseDatabase.getInstance().getReference("level2");
        final DatabaseReference mDatabase3 = FirebaseDatabase.getInstance().getReference("level3");

        //set what would happen when positive button is clicked
        Query applesQuery = mDatabase2.orderByChild("parent").equalTo(data.get(pos).getUniqueID());
        applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot appleSnapshot : dataSnapshot.getChildren()) {
                    //appleSnapshot.getRef().removeValue();
                    String uIDLevel2 = appleSnapshot.getValue(ScheduleModel.class).getUniqueID();

                    Query applesQuery1 = mDatabase3.orderByChild("parent").equalTo(uIDLevel2);
                    applesQuery1.addListenerForSingleValueEvent( new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot appleSnapshot : dataSnapshot.getChildren()) {
                                appleSnapshot.getRef().removeValue();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    appleSnapshot.getRef().removeValue();


                }

                Query applesQuery1 = mDatabase.orderByChild("uniqueID").equalTo(data.get(pos).getUniqueID());
                applesQuery1.addListenerForSingleValueEvent( new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot appleSnapshot : dataSnapshot.getChildren()) {
                            appleSnapshot.getRef().removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(tag, "onCancelled", databaseError.toException());
            }
        });


    }

    private void deleteLevel0(final int  pos){
        mDatabase = FirebaseDatabase.getInstance().getReference("level0");
        DatabaseReference mDatabase1 = FirebaseDatabase.getInstance().getReference("level1");
        final DatabaseReference mDatabase2 = FirebaseDatabase.getInstance().getReference("level2");
        final DatabaseReference mDatabase3 = FirebaseDatabase.getInstance().getReference("level3");

        //set what would happen when positive button is clicked
        Query applesQuery = mDatabase2.orderByChild("parent").equalTo(data.get(pos).getUniqueID());
        applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot appleSnapshot : dataSnapshot.getChildren()) {
                    //appleSnapshot.getRef().removeValue();
                    String uIDLevel2 = appleSnapshot.getValue(ScheduleModel.class).getUniqueID();

                    Query applesQuery1 = mDatabase3.orderByChild("parent").equalTo(uIDLevel2);
                    applesQuery1.addListenerForSingleValueEvent( new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot appleSnapshot : dataSnapshot.getChildren()) {
                                appleSnapshot.getRef().removeValue();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    appleSnapshot.getRef().removeValue();


                }

                Query applesQuery1 = mDatabase.orderByChild("uniqueID").equalTo(data.get(pos).getUniqueID());
                applesQuery1.addListenerForSingleValueEvent( new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot appleSnapshot : dataSnapshot.getChildren()) {
                            appleSnapshot.getRef().removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(tag, "onCancelled", databaseError.toException());
            }
        });


    }



}