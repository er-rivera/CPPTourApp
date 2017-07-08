package siliconsolutions.cpptourapp.Tour;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import siliconsolutions.cpptourapp.Activity.TourActivity;
import siliconsolutions.cpptourapp.Adapters.TourListAdapter;
import siliconsolutions.cpptourapp.Model.Tour;
import siliconsolutions.cpptourapp.Model.TourMarkers;
import siliconsolutions.cpptourapp.R;

public class StartTourFragment extends DialogFragment {
    static  ArrayList<Tour> toursArrayList;
    ListView tourList;
    StringBuffer tourPostList;
    RelativeLayout startTourSelectedContainer;
    LinearLayout tourListContainer;
    String name;
    ArrayList<TourMarkers> markersList;
    //ListView markerList;
    int num;

    public static StartTourFragment newInstance(int num){
        StartTourFragment fragment = new StartTourFragment();
        Bundle args = new Bundle();
        args.putInt("num",num);
        fragment.setArguments(args);

        return fragment;
    }

    /*@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        num = getArguments().getInt("num");
        setStyle(num);
        Type tourlistType = new TypeToken<ArrayList<Tour>>(){}.getType();

        toursArrayList = new GsonBuilder().create().fromJson(loadTourJSONFromAsset(),tourlistType);
        tourPostList = new StringBuffer();
        for (Tour post : toursArrayList) {
            tourPostList.append("\n name: " + post.getName() + "\n description: " + post.getDescription() +
                    "\n distance: " + post.getDistance() + "\n markers " + post.getTourMarkersList() + "\n\n");
        }
        Log.i("comp","test");


    }*/

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        num = getArguments().getInt("num");
        setStyle(num);
        Type tourlistType = new TypeToken<ArrayList<Tour>>(){}.getType();

        toursArrayList = new GsonBuilder().create().fromJson(loadTourJSONFromAsset(),tourlistType);
        tourPostList = new StringBuffer();
        for (Tour post : toursArrayList) {
            tourPostList.append("\n name: " + post.getName() + "\n description: " + post.getDescription() +
                    "\n distance: " + post.getDistance() + "\n markers " + post.getTourMarkersList() + "\n\n");
        }

        return new Dialog(getActivity(),getTheme()){
            @Override
            public void onBackPressed() {
                if(tourListContainer != null && tourListContainer.getVisibility() == View.GONE){
                    tourListContainer.setVisibility(View.VISIBLE);
                    startTourSelectedContainer.setVisibility(View.GONE);
                }
                else{
                    super.onBackPressed();
                }
            }
        };
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.start_tour_dialog, container, false);
        tourListContainer = (LinearLayout) v.findViewById(R.id.tour_list_container);
        startTourSelectedContainer = (RelativeLayout) v.findViewById(R.id.start_tour_selected_container);
        ListView lv = (ListView) v.findViewById(R.id.tour_list);
        final TextView selectedTourText = (TextView) v.findViewById(R.id.start_tour_name_text);
        Button startButton = (Button) v.findViewById(R.id.start_tour_button);
        Button backButton = (Button) v.findViewById(R.id.start_tour_back);
        TourListAdapter adapter = new TourListAdapter(getActivity(),R.id.tour_list,toursArrayList);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                tourListContainer.setVisibility(View.GONE);
                startTourSelectedContainer.setVisibility(View.VISIBLE);
                selectedTourText.setText("Start tour of " + toursArrayList.get(i).getName() + "?");
                markersList = toursArrayList.get(i).getTourMarkersList();
                name = toursArrayList.get(i).getName();

            }
        });
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TourActivity.class);
                intent.putExtra("name",name);
                intent.putParcelableArrayListExtra("markers",markersList);
                getActivity().startActivity(intent);
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tourListContainer != null && tourListContainer.getVisibility() == View.GONE){
                    tourListContainer.setVisibility(View.VISIBLE);
                    startTourSelectedContainer.setVisibility(View.GONE);
                }
            }
        });
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

    public String loadTourJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getActivity().getAssets().open("tours.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }


}