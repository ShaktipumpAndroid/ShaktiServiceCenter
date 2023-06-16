package activity.AdaperVk;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shaktipumps.shakti.shaktiServiceCenter.R;

import java.util.List;

import activity.BeanVk.ComplainAllResponse;
import activity.complainvk.ApproveComplainDetailActivity;
import activity.complainvk.PendingComplainDetailActivity;
import activity.complainvk.VisitedComplainDetailActivity;

public class SubordinateVisitedListAdapter extends RecyclerView.Adapter<SubordinateVisitedListAdapter.ViewHolder>  {
    private final Context mContext;
    private final String mStatusValue;
    private final List<ComplainAllResponse> mComplainAllResponse;

    public SubordinateVisitedListAdapter(Context mContext, List<ComplainAllResponse> mComplainAllResponse, String mStatusValue) {
        this.mContext = mContext;
        this.mComplainAllResponse = mComplainAllResponse;
        this.mStatusValue = mStatusValue;
    }

    @NonNull
    @Override
    public SubordinateVisitedListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view1 = LayoutInflater.from(mContext).inflate(R.layout.pending_comp_item_row, parent, false);
        return new SubordinateVisitedListAdapter.ViewHolder(view1);
    }

    @Override
    public void onBindViewHolder(final SubordinateVisitedListAdapter.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {


        holder.txtDealerOEMValueID.setText(mComplainAllResponse.get(position).getName1());
        holder.txtComplainnoValueID.setText(mComplainAllResponse.get(position).getCmpno());
        holder.txtComplainDateValueID.setText(mComplainAllResponse.get(position).getCmpdt());
        holder.txtMOBNumberValueID.setText(mComplainAllResponse.get(position).getMblno());
        holder.txtCustomerNameValueID.setText(mComplainAllResponse.get(position).getCstname());
        holder.txtAddressValueID.setText(mComplainAllResponse.get(position).getCaddress());

        holder.txtEngNameValueID.setText(mComplainAllResponse.get(position).getEname());
        holder.txtEngMobNoValueID.setText(mComplainAllResponse.get(position).getSengno());

        holder.imgShareID.setOnClickListener(view -> {

            String shareData = "Complain No.:"+mComplainAllResponse.get(position).getCmpno()+"\nEng. Name:"+mComplainAllResponse.get(position).getEname()+"";
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, shareData);
            sendIntent.setType("text/plain");

            Intent shareIntent = Intent.createChooser(sendIntent, null);
            mContext.startActivity(shareIntent);

        });

        holder.txtClickHereID.setOnClickListener(view -> {

                Intent mIntent = new Intent(mContext, VisitedComplainDetailActivity.class);
                mIntent.putExtra("Complain_number",mComplainAllResponse.get(position).getCmpno());
                mIntent.putExtra("mobile_number",mComplainAllResponse.get(position).getMblno());
                mIntent.putExtra("StatusValue","01");
                mIntent.putExtra("complaint", "Visited Complaint");
                mContext.startActivity(mIntent);

        });

        holder.lvlMainItemViewID.setOnClickListener(view -> {

            if(mStatusValue.equalsIgnoreCase("01"))
            {
                Intent mIntent = new Intent(mContext, PendingComplainDetailActivity.class);
                mIntent.putExtra("Complain_number",mComplainAllResponse.get(position).getCmpno());
                mIntent.putExtra("mobile_number",mComplainAllResponse.get(position).getMblno());
                mIntent.putExtra("StatusValue","01");
                mIntent.putExtra("complaint", "Pending Complaint");
                mContext.startActivity(mIntent);
            }
            else  if(mStatusValue.equalsIgnoreCase("02"))
            {
                Intent mIntent = new Intent(mContext, PendingComplainDetailActivity.class);
                mIntent.putExtra("Complain_number",mComplainAllResponse.get(position).getCmpno());
                mIntent.putExtra("mobile_number",mComplainAllResponse.get(position).getMblno());
                mIntent.putExtra("StatusValue","02");
                mIntent.putExtra("complaint", "Pending for Approval");
                mContext.startActivity(mIntent);
            }
            else  if(mStatusValue.equalsIgnoreCase("03"))
            {
                Intent mIntent = new Intent(mContext, ApproveComplainDetailActivity.class);
                mIntent.putExtra("Complain_number",mComplainAllResponse.get(position).getCmpno());
                mIntent.putExtra("mobile_number",mComplainAllResponse.get(position).getMblno());
                mIntent.putExtra("complaint", "Approval Complain");
                mIntent.putExtra("StatusValue","03");
                mContext.startActivity(mIntent);
            }
            else  if(mStatusValue.equalsIgnoreCase("04"))
            {
                Intent mIntent = new Intent(mContext, PendingComplainDetailActivity.class);
                mIntent.putExtra("Complain_number",mComplainAllResponse.get(position).getCmpno());
                mIntent.putExtra("mobile_number",mComplainAllResponse.get(position).getMblno());
                mIntent.putExtra("complaint", "Pending for Clouser");
                mIntent.putExtra("StatusValue","04");
                mContext.startActivity(mIntent);
            }
        });
    }


    @Override
    public int getItemCount() {

        if (mComplainAllResponse != null && mComplainAllResponse.size() > 0)
            return mComplainAllResponse.size();
        else
            return 0;

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {



        public TextView txtDealerOEMValueID, txtComplainnoValueID, txtComplainDateValueID, txtMOBNumberValueID,txtCustomerNameValueID,txtClickHereID,
                txtAddressValueID,txtEngNameValueID, txtEngMobNoValueID,txtEngMobNoHeadID;


        public ImageView imgShareID;

        public LinearLayout lvlMainItemViewID;

        public ViewHolder(View v) {

            super(v);
            txtClickHereID =  v.findViewById(R.id.txtClickHereID);
            imgShareID =  v.findViewById(R.id.imgShareID);
            lvlMainItemViewID =  v.findViewById(R.id.lvlMainItemViewID);
            txtEngMobNoHeadID = v.findViewById(R.id.txtEngMobNoHeadID);
            txtDealerOEMValueID =  v.findViewById(R.id.txtDealerOEMValueID);
            txtComplainnoValueID =   v.findViewById(R.id.txtComplainnoValueID);
            txtComplainDateValueID =   v.findViewById(R.id.txtComplainDateValueID);
            txtMOBNumberValueID =    v.findViewById(R.id.txtMOBNumberValueID);
            txtCustomerNameValueID =    v.findViewById(R.id.txtCustomerNameValueID);
            txtAddressValueID =    v.findViewById(R.id.txtAddressValueID);
            txtEngNameValueID =    v.findViewById(R.id.txtEngNameValueID);
            txtEngMobNoValueID =    v.findViewById(R.id.txtEngMobNoValueID);

        }
    }

}
