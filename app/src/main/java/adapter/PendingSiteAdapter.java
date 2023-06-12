package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.shaktipumps.shakti.shaktiServiceCenter.R;

import java.util.ArrayList;
import java.util.List;

import bean.PendingSiteBean;
import database.DatabaseHelper;

public class PendingSiteAdapter extends ArrayAdapter<PendingSiteBean> {

    Context context;
    DatabaseHelper db;
    ArrayList<PendingSiteBean> arrayList;

    private List<PendingSiteBean> SearchesList;

    public PendingSiteAdapter(Context context,  ArrayList<PendingSiteBean> arrayList) {
        super(context, R.layout.pending_site_item_view, arrayList);
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
    public PendingSiteBean getItem(int position) {
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
        final PendingSiteBean pendingSiteBean = getItem(position);
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.pending_site_item_view, parent, false);
        }

        TextView txtCmpNo = (TextView) view.findViewById(R.id.cmp_no_value);
        TextView txtCmpDate = (TextView) view.findViewById(R.id.cmp_date_value);
        TextView txtSubName = (TextView) view.findViewById(R.id.name_value);
        TextView txtSubMobile = (TextView) view.findViewById(R.id.sub_mobile_value);

        txtCmpNo.setText(String.valueOf(pendingSiteBean.getCmpNo()));
        txtCmpDate.setText(String.valueOf(pendingSiteBean.getCmpDate()));
        txtSubName.setText(String.valueOf(pendingSiteBean.getSubordinateName()));
        txtSubMobile.setText(String.valueOf(pendingSiteBean.getSubordinate()));

        return view;
    }


}
