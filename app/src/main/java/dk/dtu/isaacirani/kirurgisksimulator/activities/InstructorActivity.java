package dk.dtu.isaacirani.kirurgisksimulator.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import dk.dtu.isaacirani.kirurgisksimulator.NetworkChangeReceiver;
import dk.dtu.isaacirani.kirurgisksimulator.R;
import dk.dtu.isaacirani.kirurgisksimulator.adapters.Adapter;
import dk.dtu.isaacirani.kirurgisksimulator.adapters.ScenarioPickerAdapter;
import dk.dtu.isaacirani.kirurgisksimulator.models.Group;
import dk.dtu.isaacirani.kirurgisksimulator.models.Instructor;
import dk.dtu.isaacirani.kirurgisksimulator.models.Scenario;
import dk.dtu.isaacirani.kirurgisksimulator.repositories.GroupRepository;

public class InstructorActivity extends AppCompatActivity {
    LinearLayout l;
    RecyclerView recyclerView, scenarioPicker;
    private DrawerLayout drawer;
    Adapter adapter;
    ArrayList<Scenario> scenarioList = new ArrayList<>();
    ScenarioPickerAdapter spAdapter;
    TextView scenariosavaliable;
    String scenariosavailableString;
    String groupID;

    View view;
    Snackbar snackbarnotconnected;
    Snackbar snackbarisconnected;
    ProgressBar loadingIcon;


    public static TextView ratePreview, pressurePreview, volumePreview, nozzlePreview, airPreview, pressurePreview1, pressurePreview2, ratePreview1, ratePreview2;
    GroupRepository groupRepository = new GroupRepository();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor);
        recyclerView = findViewById(R.id.recyclerView);
        setSupportActionBar(findViewById(R.id.toolbar));

        Log.e("Instructor NAme", getIntent().getStringExtra("instructorName"));

        groupRepository.createGroupWithoutStudents(new Instructor(getIntent().getStringExtra("instructorName")), (String groupId) -> {
            groupRepository.loadGroup(groupId, group -> {
                groupID = groupId;
                createAdapter(group);
                return null;
            });
            return null;
        });


        airPreview = findViewById(R.id.airPreview);
        ratePreview = findViewById(R.id.ratePreview);
        pressurePreview = findViewById(R.id.pressurePreview);
        volumePreview = findViewById(R.id.volumePreview);
        nozzlePreview = findViewById(R.id.nozzlePreview);
        pressurePreview1 = findViewById(R.id.pressureBar1Preview);
        pressurePreview2 = findViewById(R.id.pressureBar2Preview);
        ratePreview1 = findViewById(R.id.rateBar1Preview);
        ratePreview2 = findViewById(R.id.rateBar2Preview);

//        loadingIcon = findViewById(R.id.instructorLoadingIcon);

        scenariosavaliable = findViewById(R.id.scenariosavaliable);
        scenariosavailableString = "  Avaliable Scenarios  ";
        SpannableString spannableString = new SpannableString(scenariosavailableString);
        spannableString.setSpan(new RelativeSizeSpan(2f), 0, 1, 0);
        spannableString.setSpan(new RelativeSizeSpan(2f), scenariosavailableString.length() - 1, scenariosavailableString.length() - 0, 0);
        scenariosavaliable.setText(spannableString);


        l = findViewById(R.id.lin);

        //nyt BR
        registerReceiver();
        view = findViewById(android.R.id.content);
        snackbarnotconnected = Snackbar.make(view, "Device is not connected to internet", Snackbar.LENGTH_INDEFINITE);
        snackbarisconnected = Snackbar.make(view, "Device is connected to internet", Snackbar.LENGTH_SHORT);
        View snacknotconnectedview = snackbarnotconnected.getView();
        TextView textView = snacknotconnectedview.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.RED);


        /**
         * Den her skal ikke være her, flyttes til scenarioRepository
         */
        FirebaseDatabase.getInstance().getReference().child("Scenarios").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                scenarioList.clear();
                for (DataSnapshot scenario : dataSnapshot.getChildren()) {
                    scenarioList.add(scenario.getValue(Scenario.class));

                }
                spAdapter = new ScenarioPickerAdapter(scenarioList);
                loadRec();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void loadRec() {
        scenarioPicker = findViewById(R.id.scenarioPicker);
        scenarioPicker.setAdapter(spAdapter);
        scenarioPicker.setHasFixedSize(false);
        scenarioPicker.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
//        loadingIcon.setVisibility(View.GONE);
        Log.e("tæst", scenarioList.size() + "");
    }


    private void registerReceiver() {

        try {
            IntentFilter filter = new IntentFilter();
            filter.addAction(NetworkChangeReceiver.NETWORK_CHANGE_ACTION);
            registerReceiver(networkChangeReceiver, filter);

        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    @Override
    protected void onDestroy() {
        /**
         *
         * VIGTIGT by isaac, skal implementeres senere
         *
         **/

//        if(!groupID.isEmpty()){
//            groupRepository.deleteGroup(groupID);
//        }

        try {
            unregisterReceiver(networkChangeReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }


    void createAdapter(Group group) {
        adapter = new Adapter(group.getStudents());
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    InternalNetworkChangeReceiver networkChangeReceiver = new InternalNetworkChangeReceiver();

    class InternalNetworkChangeReceiver extends BroadcastReceiver {


        @Override
        public void onReceive(Context c, Intent i) {


            if (i.getBooleanExtra("networkstatus", false) == false) {
                snackbarnotconnected.show();

            } else {
                if (snackbarnotconnected.isShown()) {
                    snackbarnotconnected.dismiss();
                    snackbarisconnected.show();
                }
            }
        }
    }
}
