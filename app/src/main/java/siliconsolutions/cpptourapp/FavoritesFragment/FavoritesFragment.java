package siliconsolutions.cpptourapp.FavoritesFragment;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;
import siliconsolutions.cpptourapp.Adapters.FavoritesListAdapter;
import siliconsolutions.cpptourapp.R;

public class FavoritesFragment extends DialogFragment {
    static ArrayList<Marker> markerArrayList;
    RecyclerView mRecyclerView;
    FavoritesListAdapter adapter;
    RecyclerView.LayoutManager mLayoutManager;
    int num;

    public static FavoritesFragment newInstance(int num, ArrayList<Marker> mList) {
        FavoritesFragment  fragment = new FavoritesFragment();
        markerArrayList = mList;

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("num", num);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        num = getArguments().getInt("num");
        setStyle(num);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.favorites_dialog, container, false);
        ListView lv = (ListView) v.findViewById(R.id.favoritesList);
        adapter = new FavoritesListAdapter(v.getContext(),R.id.favoritesList,markerArrayList);
        lv.setAdapter(adapter);
        return v;
    }

    public void setStyle(int num){
        int style = DialogFragment.STYLE_NORMAL, theme = 0;
        switch ((num-1)%6) {
            case 1: style = DialogFragment.STYLE_NO_TITLE; break;
            case 2: style = DialogFragment.STYLE_NO_FRAME; break;
            case 3: style = DialogFragment.STYLE_NO_INPUT; break;
            case 4: style = DialogFragment.STYLE_NORMAL; break;
            case 5: style = DialogFragment.STYLE_NORMAL; break;
            case 6: style = DialogFragment.STYLE_NO_TITLE; break;
            case 7: style = DialogFragment.STYLE_NO_FRAME; break;
            case 8: style = DialogFragment.STYLE_NORMAL; break;
        }
        switch ((num-1)%6) {
            case 4: theme = android.R.style.Theme_Holo; break;
            case 5: theme = android.R.style.Theme_Holo_Light_Dialog; break;
            case 6: theme = android.R.style.Theme_Holo_Light; break;
            case 7: theme = android.R.style.Theme_Holo_Light_Panel; break;
            case 8: theme = android.R.style.Theme_Holo_Light; break;
        }
        setStyle(style, theme);
    }
}
