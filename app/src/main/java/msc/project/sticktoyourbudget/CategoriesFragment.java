package msc.project.sticktoyourbudget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.view.LayoutInflater;
import android.view.View.OnClickListener;
import android.widget.Button;


public class CategoriesFragment extends Fragment {
    private final static String TAG = "CategoryFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.fragment_categories, container, false);
        Button button = (Button) v.findViewById(R.id.button2);
        button.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                /*Button b = (Button)v;
                String buttonText = b.getText().toString();
                Log.d("Mantalakia", "You pressed the button!"); */
                TextView name = (TextView)v.findViewById(R.id.textCatName);
                TextView desc = (TextView)v.findViewById(R.id.textCatDesc);
                if (name.getText().toString().isEmpty()) {
                    Log.d(TAG, "Empty name");
                    Toast.makeText(getContext(), "Category name can't be null", Toast.LENGTH_LONG).show();
                    return;
                }
                Log.d(TAG, "Name is not empty");
                if (!Category.addCategory(name.getText().toString(), desc.getText().toString(), getContext())) {
                    Log.d(TAG, "Error when saving category!!1");
                    Toast.makeText(getContext(), "Error during category creation!", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    Log.d(TAG, "Successful insertion");
                    Toast.makeText(getContext(), "Category " + name.getText().toString() + " created successfully", Toast.LENGTH_SHORT).show();
                    name.setText("");
                    desc.setText("");
                }
            }
        });
        return v;

    }
    @Override
    public void onInflate(Context context, AttributeSet attrs, Bundle savedInstanceState) {
        super.onInflate(context, attrs, savedInstanceState);


    }

}