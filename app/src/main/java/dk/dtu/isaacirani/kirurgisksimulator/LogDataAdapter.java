package dk.dtu.isaacirani.kirurgisksimulator;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import dk.dtu.isaacirani.kirurgisksimulator.models.LogEntry;
import dk.dtu.isaacirani.kirurgisksimulator.models.Scenario;
import dk.dtu.isaacirani.kirurgisksimulator.models.Student;

public class LogDataAdapter extends RecyclerView.Adapter<LogDataViewHolder> {
    ArrayList<LogEntry> logEntries;

    public LogDataAdapter(ArrayList<LogEntry> logEntries) {
        this.logEntries = logEntries;
        Log.e("gøgl", String.valueOf(logEntries.size()));
    }

    @NonNull
    @Override
    public LogDataViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i){
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.log_data, viewGroup, false);
        LogDataViewHolder sh = new LogDataViewHolder(view);
        sh.logData_name = view.findViewById(R.id.dataName);
        sh.logData_scenarioName = view.findViewById(R.id.dataScenarioName);
        sh.logData_date = view.findViewById(R.id.dataDate);
        sh.logData_time = view.findViewById(R.id.dataTime);
        sh.logData_failures = view.findViewById(R.id.dataFailures);
        sh.logTableRow = view.findViewById(R.id.LogTableRow);
        return sh;
    }

    @Override
    public void onBindViewHolder(@NonNull LogDataViewHolder logDataViewHolder, int i) {
        LogEntry logEntry = logEntries.get(i);
        logDataViewHolder.logTableRow.setId(i);
        logDataViewHolder.logData_name.setText(logEntry.getName());
        logDataViewHolder.logData_scenarioName.setText(logEntry.getScenarioName());
        logDataViewHolder.logData_date.setText(logEntry.getDate().toString());
        logDataViewHolder.logData_time.setText(String.valueOf(logEntry.getTime()));
        logDataViewHolder.logData_failures.setText(String.valueOf(logEntry.getFailures()));
    }


    @Override
    public int getItemCount() {
        return logEntries.size();
    }
}

