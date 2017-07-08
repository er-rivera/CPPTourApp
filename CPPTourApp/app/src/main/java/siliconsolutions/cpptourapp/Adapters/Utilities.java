package siliconsolutions.cpptourapp.Adapters;

import android.icu.text.DecimalFormat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import org.jsoup.Jsoup;


public class Utilities {
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    public static String htmlToText(String s){
        return Jsoup.parse(s).text();
    }

    public static String formatDistance(float v) {
        String pattern = "###,###";
        java.text.DecimalFormat decimalFormat = new java.text.DecimalFormat(pattern);
        String result = decimalFormat.format(v * 3.28084);
        return result;
    }
}
