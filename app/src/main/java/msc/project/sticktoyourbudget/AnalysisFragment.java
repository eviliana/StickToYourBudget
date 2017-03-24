package msc.project.sticktoyourbudget;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import android.util.Log;

public class AnalysisFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.fragment_analysis, container, false);
        Button analyze = (Button)v.findViewById(R.id.buttonAnalyze);
        analyze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView dateStart = (TextView)v.findViewById(R.id.dateStart);
                TextView dateEnd = (TextView)v.findViewById(R.id.dateEnd);
                SimpleDateFormat format = new SimpleDateFormat("dd/mm/yyyy");
                Date start, end;
                ListView listView = (ListView) v.findViewById(R.id.analysisList);
                try {
                    start = format.parse(dateStart.getText().toString());
                    end = format.parse(dateEnd.getText().toString());
                } catch (ParseException pe) {
                    Toast.makeText(getContext(), "The proper date format is DD/ΜΜ/YYYY ", Toast.LENGTH_SHORT).show();
                    listView.setAdapter(null);
                    return;
                }
                //Μετατροπή των ημερομηνιών του χρήστη στη μορφή που είναι αποθηκευμενες στη βαση
                long dStart = start.getTime();
                //Log.d("dd", toString().valueOf(dStart));
                long dEnd = end.getTime();
                Log.d("before", toString().valueOf(dEnd));
                //giati i getTime to metatrepei se 00:00:00 kai emeis theloume oli tin imera, opote to midenizw stin epomeni
                //kai sto query tha ginei <dEnd giati alliws den tin simperilamvanei
                dEnd +=(24*60*60*1000);
                Log.d("after", toString().valueOf(dEnd));


                ArrayList<DataModel> data = Expense.getAnalysisData(getContext(), dStart, dEnd);
                //Log.d("test", String.valueOf(data.size()));
                AnalysisListAdapter adapter = new AnalysisListAdapter(data, getContext());
                if (data.size()==0) {
                    Toast.makeText(getContext(), "There are no expenses for selected dates", Toast.LENGTH_SHORT).show();
                    //Καθαρισμός της λίστας εάν έχω αποτελέσματα απο προηγούμενη αναζήτηση
                    listView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    dateStart.setText("");
                    dateEnd.setText("");
                    return;
                }
                listView.setAdapter(adapter);
                dateStart.setText("");
                dateEnd.setText("");
            }
        });

        return v;


    }
}