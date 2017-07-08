package siliconsolutions.cpptourapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;
import java.util.List;

import siliconsolutions.cpptourapp.Directions.Step;
import siliconsolutions.cpptourapp.Model.Landmarks;
import siliconsolutions.cpptourapp.R;

public class NavigationListAdapter extends ArrayAdapter<Step> {
    private List<Marker> markers;
    private List<Step> steps;

    public NavigationListAdapter(Context context, int resource, List<Step> steps, List<Marker> markers) {
        super(context, resource, steps);
        this.steps =  steps;
        this.markers = markers;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.navigation_steps_items,parent,false);
        }
        TextView stepInstructionsText = (TextView) convertView.findViewById(R.id.navigation_step_text);
        TextView distanceText = (TextView) convertView.findViewById(R.id.navigation_step_distance_text);

        String html = steps.get(position).getHtmlInstructions();

        stepInstructionsText.setText(html);
        distanceText.setText(steps.get(position).getDistance().getText());

        return convertView;
    }
}
