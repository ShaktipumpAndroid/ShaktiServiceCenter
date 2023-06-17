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
import java.util.Locale;

import bean.SubordinateBean;
import database.DatabaseHelper;

public class SubordinateAdapter extends ArrayAdapter<SubordinateBean> {

    Context context;
    DatabaseHelper db;
    public ArrayList<SubordinateBean> arrayList;
    private final List<SubordinateBean> searchList;

    public SubordinateAdapter(Context context, ArrayList<SubordinateBean> arrayList) {
        super(context, R.layout.subordinate_list, arrayList);
        this.context = context;
        this.arrayList = arrayList;

        this.searchList = new ArrayList<>();
        this.searchList.addAll(arrayList);
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public SubordinateBean getItem(int position) {
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
        SubordinateBean subordinateBean = getItem(position);
        if (view == null) {

            view = LayoutInflater.from(getContext()).inflate(R.layout.subordinate_list, parent, false);
        }


        TextView mobile = (TextView) view.findViewById(R.id.mobile_no);
        TextView name = (TextView) view.findViewById(R.id.name_value);
        TextView aadhar = (TextView) view.findViewById(R.id.aadhar_value);
        TextView password = (TextView) view.findViewById(R.id.password_value);
        TextView state = (TextView) view.findViewById(R.id.state_value);
        TextView district = (TextView) view.findViewById(R.id.district_value);
        TextView from_date =(TextView) view.findViewById(R.id.from_date_value);
        TextView to_date =(TextView) view.findViewById(R.id.to_date_value);

        assert subordinateBean != null;
        mobile.setText(subordinateBean.getMobileNo());
        name.setText(subordinateBean.getName());
        state.setText(subordinateBean.getState());
        district.setText(subordinateBean.getDistrict());
        from_date.setText(subordinateBean.getFromDate());
        to_date.setText(subordinateBean.getToDate());
        aadhar.setText(subordinateBean.getAadharNo());
        password.setText(subordinateBean.getPassword());

        return view;
    }


    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());

        arrayList.clear();
        if (charText.length() == 0) {
            arrayList.addAll(searchList);
        } else {
            for (SubordinateBean cs : searchList) {
                if (cs.getMobileNo().toLowerCase(Locale.getDefault()).contains(charText) ||
                        cs.getName().toLowerCase(Locale.getDefault()).contains(charText) ||
                        cs.getFromDate().toLowerCase(Locale.getDefault()).contains(charText)) {
                    arrayList.add(cs);
                }
            }
        }
        notifyDataSetChanged();
    }
}
