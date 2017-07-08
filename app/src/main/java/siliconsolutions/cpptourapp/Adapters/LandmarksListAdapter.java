package siliconsolutions.cpptourapp.Adapters;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;
import siliconsolutions.cpptourapp.Model.Landmarks;
import siliconsolutions.cpptourapp.R;

public class LandmarksListAdapter extends ArrayAdapter<Landmarks> {
    List<Landmarks> landmarks;

    public LandmarksListAdapter(Context context, int resource, List<Landmarks> landmarks) {
        super(context, resource, landmarks);
        this.landmarks = landmarks;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.locations_list_item_layout,parent,false);
        }

        TextView landmarksNameText = (TextView) convertView.findViewById(R.id.locations_list_building_text);
        TextView distanceText = (TextView) convertView.findViewById(R.id.locations_list_distance_text);
        TextView landmarksNumberText = (TextView) convertView.findViewById(R.id.locations_list_building_number_text);

        landmarksNameText.setText(landmarks.get(position).getLandmarkName());
        distanceText.setText("   ");
        landmarksNumberText.setText(landmarks.get(position).getLandmarkNumber());

        convertView.setTag(position);
        return convertView;
    }
}
