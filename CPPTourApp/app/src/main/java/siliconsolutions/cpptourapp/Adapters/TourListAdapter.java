package siliconsolutions.cpptourapp.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;
import siliconsolutions.cpptourapp.Model.Tour;
import siliconsolutions.cpptourapp.R;

public class TourListAdapter extends ArrayAdapter<Tour> {
    List<Tour> tourList;
    Context c;

    public TourListAdapter(@NonNull Context context, int resource, @NonNull List<Tour> objects) {
        super(context, resource, objects);
        tourList = objects;
        c = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.start_tour_list_item,parent,false);
        }

        Tour current = tourList.get(position);
        TextView tourName = (TextView) convertView.findViewById(R.id.tour_name_list_item);
        TextView tourDistance = (TextView) convertView.findViewById(R.id.tour_distance_list_item);
        tourName.setText(current.getName());
        tourDistance.setText(current.getDistance());

        convertView.setTag(position);
        return convertView;
    }
}
