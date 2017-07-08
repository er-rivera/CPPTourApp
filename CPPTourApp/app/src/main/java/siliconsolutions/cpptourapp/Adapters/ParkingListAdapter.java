package siliconsolutions.cpptourapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;
import siliconsolutions.cpptourapp.Model.ParkingLots;
import siliconsolutions.cpptourapp.R;

public class ParkingListAdapter extends ArrayAdapter<ParkingLots> {
    List<ParkingLots> parkingLots;

    public ParkingListAdapter(Context context, int resource, List<ParkingLots> parkingLots) {
        super(context, resource, parkingLots);
        this.parkingLots = parkingLots;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.locations_list_item_layout,parent,false);
        }

        TextView parkingLotNameText = (TextView) convertView.findViewById(R.id.locations_list_building_text);
        TextView distanceText = (TextView) convertView.findViewById(R.id.locations_list_distance_text);
        TextView parkingLotNumberText = (TextView) convertView.findViewById(R.id.locations_list_building_number_text);

        parkingLotNameText.setText(parkingLots.get(position).getParkingLotsName());
        distanceText.setText("    ");
        parkingLotNumberText.setText(parkingLots.get(position).getParkingLotsNumber());

        convertView.setTag(position);
        return convertView;
    }
}
