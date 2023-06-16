package adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.shaktipumps.shakti.shaktiServiceCenter.R;

import java.util.ArrayList;

import bean.VisitedSiteBean;
import database.DatabaseHelper;

public class VisitedSiteAdapter extends ArrayAdapter<VisitedSiteBean> {

    Context context;
    DatabaseHelper db;
    public ArrayList<VisitedSiteBean> arrayList;

    public VisitedSiteAdapter(Context context,  ArrayList<VisitedSiteBean> arrayList) {
        super(context, R.layout.visited_site_item_view, arrayList);
        this.context = context;
        this.arrayList = arrayList;

       /* this.SearchesList = new ArrayList<PendingSiteBean>();
        this.SearchesList.addAll(arrayList);*/
    }
    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public VisitedSiteBean getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, final ViewGroup parent) {

        db = new DatabaseHelper(context);
        // Get the data item for this position
        final VisitedSiteBean visitedSiteBean = getItem(position);
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.visited_site_item_view, parent, false);
        }

        TextView txtCmpNo =  view.findViewById(R.id.cmp_no_value);
        TextView txtCmpDate =  view.findViewById(R.id.cmp_date_value);
        TextView txtSubName =  view.findViewById(R.id.name_value);
        TextView txtSubMobile =  view.findViewById(R.id.sub_mobile_value);


        txtCmpNo.setText(String.valueOf(visitedSiteBean.getCmpNo()));
        txtCmpDate.setText(String.valueOf(visitedSiteBean.getCmpDate()));
        txtSubName.setText(String.valueOf(visitedSiteBean.getSubordinateName()));
        txtSubMobile.setText(String.valueOf(visitedSiteBean.getSubordinate()));



        return view;
    }


}
