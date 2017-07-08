package siliconsolutions.cpptourapp.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import siliconsolutions.cpptourapp.Model.Office;
import siliconsolutions.cpptourapp.R;

/**
 * Created by user on 5/24/17.
 */

public class OfficesListAdapter extends RecyclerView.Adapter<OfficesListAdapter.ViewHolder>{
    List<Office> offices;

    public OfficesListAdapter(List<Office> offices) {
        this.offices = offices;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sheet_building_list_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if((offices.get(position).getRoom()).equals("")){
            holder.roomNumber.setText(offices.get(position).getRoom());
        }
        else{
            holder.roomNumber.setText("Room " + offices.get(position).getRoom());
        }
        holder.phoneNumber.setText(offices.get(position).getPhoneNumber());
        holder.name.setText(offices.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return offices.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name;
        TextView phoneNumber;
        TextView roomNumber;
        public ViewHolder(View v) {
            super(v);
            name = (TextView) v.findViewById(R.id.sheet_building_list_office_name);
            phoneNumber = (TextView) v.findViewById(R.id.sheet_building_list_office_number);
            roomNumber = (TextView) v.findViewById(R.id.sheet_building_list_room);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

        }
    }
}
