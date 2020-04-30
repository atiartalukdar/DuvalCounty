package adapter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import bp.BP;
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


        TextView _number = convertView.findViewById(R.id.itemTV);
        ImageButton _addSub = convertView.findViewById(R.id.addSub);
        _number.setText(scheduleModel.getName());


        _number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e(tag, data.get(position).toString());
                p = data.get(position).getUniqueID();

                switch (scheduleModel.getLevel()){
                    case "1":
                        ((MainActivity)activity).getSupportActionBar().setTitle(data.get(position).getName());

                        Log.e(tag, "now  in DB ref level2");
                        mDatabase = FirebaseDatabase.getInstance().getReference("level2");
                        data.clear();
                        mDatabase.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot websiteData : dataSnapshot.getChildren()){
                                    ScheduleModel s = websiteData.getValue(ScheduleModel.class);
                                    if (s.getParent().equals(p)){
                                        data.add(s);
                                    }
                                }
                                notifyDataSetChanged();
                            }
                            @Override
                            public void onCancelled(DatabaseError error) {
                                // Failed to read value
                                Log.w(tag, "Failed to read value.", error.toException());
                            }
                        });

                        break;
                    case "2":
                        ((MainActivity)activity).getSupportActionBar().setTitle(data.get(position).getName());
                        Log.e(tag, "now  in DB ref level3");
                        mDatabase = FirebaseDatabase.getInstance().getReference("level3");
                        data.clear();
                        mDatabase.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot websiteData : dataSnapshot.getChildren()){
                                    ScheduleModel s = websiteData.getValue(ScheduleModel.class);
                                    if (s.getParent().equals(p)){
                                        data.add(s);
                                    }
                                }
                                notifyDataSetChanged();
                            }
                            @Override
                            public void onCancelled(DatabaseError error) {
                                // Failed to read value
                                Log.w(tag, "Failed to read value.", error.toException());
                            }
                        });

                        break;
                }

                notifyDataSetChanged();
            }
        });

        _addSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popUpEditText(scheduleModel);
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

    public void popUpEditText(ScheduleModel scheduleModel) {

        switch (scheduleModel.getLevel()){
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
        parent = scheduleModel.getParent();

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
}