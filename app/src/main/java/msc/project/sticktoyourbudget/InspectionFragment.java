package msc.project.sticktoyourbudget;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class InspectionFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_inspection, container, false);

        ListView listView = (ListView) v.findViewById(R.id.inspectionList);
        ArrayList<DataModel> data = Expense.getData(getContext());
        //Log.d("test", String.valueOf(data.size()));
        InspectionListAdapter adapter = new InspectionListAdapter(data, getContext());
        listView.setAdapter(adapter);
        return v;
    }
}