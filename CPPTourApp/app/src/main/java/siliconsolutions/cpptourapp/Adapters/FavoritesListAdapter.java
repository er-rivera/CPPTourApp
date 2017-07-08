package siliconsolutions.cpptourapp.Adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.Marker;

import java.util.List;

import siliconsolutions.cpptourapp.Model.Building;
import siliconsolutions.cpptourapp.R;

public class FavoritesListAdapter extends ArrayAdapter<Marker>{
    List<Marker> favorites;

    public FavoritesListAdapter(@NonNull Context context, int resource, @NonNull List<Marker> favorites) {
        super(context, resource, favorites);
        this.favorites = favorites;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.favorite_item_cell,parent,false);
        }

        TextView itemText = (TextView) convertView.findViewById(R.id.item_name_favorites);


        itemText.setText(favorites.get(position).getTitle());
        convertView.setTag(position);
        return convertView;
    }
}
