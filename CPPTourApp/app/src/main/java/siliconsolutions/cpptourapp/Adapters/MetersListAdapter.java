package siliconsolutions.cpptourapp.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import siliconsolutions.cpptourapp.Model.Meter;
import siliconsolutions.cpptourapp.R;


public class MetersListAdapter extends RecyclerView.Adapter<MetersListAdapter.ViewHolder>{
    List<Meter> meters;

    public MetersListAdapter(List<Meter> meters) {
        this.meters = meters;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sheet_parking_list_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.locationText.setText(meters.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return meters.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView locationText;

        public ViewHolder(View v) {
            super(v);
            locationText = (TextView) v.findViewById(R.id.sheet_parking_list_meter_location);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

        }
    }
}
