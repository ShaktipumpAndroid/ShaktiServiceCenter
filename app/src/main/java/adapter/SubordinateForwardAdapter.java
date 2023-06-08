package adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shaktipumps.shakti.shaktiServiceCenter.R;

import java.util.ArrayList;
import java.util.List;

import bean.SubordinateBean;

public class SubordinateForwardAdapter extends RecyclerView.Adapter<SubordinateForwardAdapter.ViewHolder>{

    private final Context mContext;
    private final ArrayList<SubordinateBean>  subordinateBeanArrayList;
    private final List<Boolean> subordinateSelectionCheckList;

    public SubordinateForwardAdapter(Context mContext, ArrayList<SubordinateBean> subordinateBeanArrayList, List<Boolean> subordinateSelectionCheck) {
        this.mContext = mContext;
        this.subordinateBeanArrayList = subordinateBeanArrayList;
        this.subordinateSelectionCheckList = subordinateSelectionCheck;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view1 = LayoutInflater.from(mContext).inflate(R.layout.submordinatemember, parent, false);
        return new ViewHolder(view1);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position)  {
        holder.mobile.setText(subordinateBeanArrayList.get(position).getMobileNo());
        holder.name.setText(subordinateBeanArrayList.get(position).getName());
        holder.aadhar.setText(subordinateBeanArrayList.get(position).getAadharNo());
        holder.fromDate.setText(subordinateBeanArrayList.get(position).getFromDate());
        holder.toDate.setText(subordinateBeanArrayList.get(position).getToDate());

        if (subordinateSelectionCheckList.get(position)) {
            ///true section
            holder.imgCheckBoxImageID.setImageResource(R.drawable.ic_select_icon);
        } else {
            holder.imgCheckBoxImageID.setImageResource(R.drawable.ic_unselect_icon);
            //else sections
        }

        holder.imgCheckBoxImageID.setOnClickListener(v -> {

            for (int kk = 0; kk < subordinateBeanArrayList.size(); kk++) {

                if (position == kk) {
                    subordinateSelectionCheckList.set(kk, true);
                } else {
                    subordinateSelectionCheckList.set(kk, false);
                }
            }
         //   WebURL.mSapBillNumber = mLrInvoiceResponse.get(position).getSapBillion();
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        if (subordinateBeanArrayList != null && subordinateBeanArrayList.size() > 0)
            return subordinateBeanArrayList.size();
        else
            return 0;

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgCheckBoxImageID;
        public TextView mobile,aadhar,name,fromDate,toDate;

        public ViewHolder(View v) {
            super(v);

            imgCheckBoxImageID =  v.findViewById(R.id.imgCheckBoxImageID);
            mobile =  v.findViewById(R.id.mobile_value);
            aadhar = v.findViewById(R.id.aadhar_value);
            name = v.findViewById(R.id.name_value);
            fromDate = v.findViewById(R.id.from_date_value);
            toDate = v.findViewById(R.id.to_date_value);
        }
    }
}
