package singareddy.productionapps.gymiemanagement.attendance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

import singareddy.productionapps.gymiemanagement.R;

/**
 * This activity shows full attendance data of the
 * current gym being managed. In the beginning when the
 * user chooses the gym they are managing, the gym id
 * is changed accordingly in the cache. For every succeeding
 * functionality, this gym id is used to fetch and show the
 * data to the user. For example, if chosen gym was GYM_1 previously
 * and is now changed to GYM_2, attendance of GYM_2 is alone shown
 * to the user now.
 * Temporary use: GYM ID = 456
 */
public class ManageAttendanceScreen extends AppCompatActivity {

    LineChart lineChart;
    CheckBox staffCheckBox, membersCheckBox;
    LineDataSet membersDataSet, staffDataset;
    LineData lineData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_attendance);
        initialiseUI();
        lineChart = findViewById(R.id.manage_attendance_mpchart_line);
        List<Entry> entries = new ArrayList<>();
        entries.add(new Entry(1,45));
        entries.add(new Entry(2,27));
        entries.add(new Entry(3,34));
        entries.add(new Entry(4,40));
        entries.add(new Entry(5,43));
        entries.add(new Entry(6,76));
        entries.add(new Entry(7,62));
        entries.add(new Entry(8,45));
        entries.add(new Entry(9,27));
        entries.add(new Entry(10,34));
        entries.add(new Entry(11,40));
        entries.add(new Entry(12,43));
        entries.add(new Entry(13,76));
        entries.add(new Entry(14,62));
        membersDataSet = new LineDataSet(entries, "Members present out of 100");
        membersDataSet.setMode(LineDataSet.Mode.LINEAR);
        membersDataSet.setLineWidth(2);
        membersDataSet.setColor(getResources().getColor(android.R.color.holo_red_light));
        lineData = new LineData();
        lineData.addDataSet(membersDataSet);

        List<Entry> staffEntries = new ArrayList<>();
        staffEntries.add(new Entry(1, 40));
        staffEntries.add(new Entry(2, 32));
        staffEntries.add(new Entry(3, 38));
        staffEntries.add(new Entry(4, 39));
        staffEntries.add(new Entry(5, 40));
        staffEntries.add(new Entry(6, 31));
        staffEntries.add(new Entry(7, 37));
        staffEntries.add(new Entry(8, 33));
        staffEntries.add(new Entry(9, 30));
        staffEntries.add(new Entry(10, 39));
        staffEntries.add(new Entry(11, 36));
        staffEntries.add(new Entry(12, 27));
        staffEntries.add(new Entry(13, 30));
        staffEntries.add(new Entry(14, 31));
        staffDataset = new LineDataSet(staffEntries, "Staff present out of 40");
        staffDataset.setMode(LineDataSet.Mode.LINEAR);
        staffDataset.setLineWidth(2);membersDataSet.setColor(getResources().getColor(android.R.color.holo_blue_light));
        lineData.addDataSet(staffDataset);


        lineChart.setData(lineData);
        lineChart.animateX(1000);
    }

    private void initialiseUI() {
        staffCheckBox = findViewById(R.id.manage_attendance_cb_staff);
        membersCheckBox = findViewById(R.id.manage_attendance_cb_members);
        staffCheckBox.setChecked(true);
        membersCheckBox.setChecked(true);
        staffCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    lineData.addDataSet(staffDataset);
                }
                else {
                    lineData.removeDataSet(staffDataset);
                }
                lineChart.invalidate();
            }
        });
        membersCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    lineData.addDataSet(membersDataSet);
                }
                else {
                    lineData.removeDataSet(membersDataSet);
                }
                lineChart.invalidate();
            }
        });
    }

    /**
     * Launches QR code scanning activity to collect
     * the attendance.
     * @param view
     */
    public void scanAttendance (View view) {
        Intent scanAttendanceIntent = new Intent(this, ScanAttendanceScreen.class);
        startActivity(scanAttendanceIntent);
    }
}
