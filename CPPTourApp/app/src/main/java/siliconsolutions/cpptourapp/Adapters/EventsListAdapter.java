package siliconsolutions.cpptourapp.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import siliconsolutions.cpptourapp.Model.Event;
import siliconsolutions.cpptourapp.R;

public class EventsListAdapter extends RecyclerView.Adapter<EventsListAdapter.ViewHolder>{
    List<Event> events;

    public EventsListAdapter(List<Event> events) {
        this.events = events;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sheet_landmark_list_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.name.setText(events.get(position).getName());
        holder.description.setText(events.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name;
        TextView description;

        public ViewHolder(View v) {
            super(v);
            name = (TextView) v.findViewById(R.id.sheet_landmark_list_event_name);
            description = (TextView) v.findViewById(R.id.sheet_landmark_list_event_description);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

        }
    }
}
