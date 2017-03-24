package msc.project.sticktoyourbudget;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class AnalysisListAdapter extends ArrayAdapter<DataModel> {
    private ArrayList<DataModel> dataModel;
    Context context;

    private static class ViewHolder {
        TextView txtPrice;
        TextView txtDate;
        TextView txtDescription;
        TextView txtCategory;
        ImageView delete;
    }
    public AnalysisListAdapter(ArrayList<DataModel> data, Context context) {
        super(context, R.layout.row_analysis, data);
        this.dataModel = data;
        this.context = context;
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position
        DataModel dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_analysis, parent, false);
            viewHolder.txtPrice = (TextView) convertView.findViewById(R.id.rowPrice);
            viewHolder.txtDescription = (TextView) convertView.findViewById(R.id.rowDescription);
            viewHolder.txtDate = (TextView) convertView.findViewById(R.id.rowDate);
            viewHolder.txtCategory = (TextView) convertView.findViewById(R.id.rowCategory);
            viewHolder.delete = (ImageView) convertView.findViewById(R.id.item_delete);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        lastPosition = position;

        viewHolder.txtPrice.setText(dataModel.price);
        viewHolder.txtCategory.setText(dataModel.category);
        viewHolder.txtDate.setText(dataModel.date);
        viewHolder.txtDescription.setText(dataModel.description);

        // Return the completed view to render on screen
        return convertView;
    }
}
