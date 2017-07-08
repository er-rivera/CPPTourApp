package siliconsolutions.cpptourapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;
import siliconsolutions.cpptourapp.Model.Building;
import siliconsolutions.cpptourapp.R;

public class BuildingsListAdapter extends ArrayAdapter<Building> {

    List<Building> buildings;
    public BuildingsListAdapter(Context context, int resource, List<Building> buildings) {
        super(context, resource, buildings);
        this.buildings = buildings;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.locations_list_item_layout,parent,false);
        }

        TextView buildingText = (TextView) convertView.findViewById(R.id.locations_list_building_text);
        TextView distanceText = (TextView) convertView.findViewById(R.id.locations_list_distance_text);
        TextView buildingNumberText = (TextView) convertView.findViewById(R.id.locations_list_building_number_text);

        buildingText.setText(buildings.get(position).getBuildingName());
        distanceText.setText("   ");
        buildingNumberText.setText(buildings.get(position).getBuildingNumber());

        convertView.setTag(position);
        return convertView;
    }

}