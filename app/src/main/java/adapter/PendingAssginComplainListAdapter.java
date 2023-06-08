package adapter;

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

import activity.AdaperVk.PendingComplainListAdapter;
import activity.AssginPendingComplainDetailActivity;
import bean.SubordinateAssginComplainBean;

public class PendingAssginComplainListAdapter extends RecyclerView.Adapter<PendingComplainListAdapter.ViewHolder>{

    private final Context mContext;
    private final List<SubordinateAssginComplainBean> mComplainAllResponse;

    public PendingAssginComplainListAdapter(Context mContext, List<SubordinateAssginComplainBean> mComplainAllResponse) {
        this.mContext = mContext;
        this.mComplainAllResponse = mComplainAllResponse;

    }

    @NonNull
    @Override
    public PendingComplainListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view1 = LayoutInflater.from(mContext).inflate(R.layout.pending_comp_item_row, parent, false);
        return new PendingComplainListAdapter.ViewHolder(view1);
    }

    @Override
    public void onBindViewHolder(@NonNull PendingComplainListAdapter.ViewHolder holder, int position) {

        holder.txtComplainDateValueID.setVisibility(View.GONE);
        holder.txtEngMobNoHeadID.setVisibility(View.GONE);
        holder.txtEngMobNoValueID.setVisibility(View.GONE);
        holder.txtDealerOEMValueID.setText(mComplainAllResponse.get(position).getDelname());
        holder.txtComplainnoValueID.setText(mComplainAllResponse.get(position).getCmpno());
        holder.txtMOBNumberValueID.setText(mComplainAllResponse.get(position).getCmblno());
        holder.txtCustomerNameValueID.setText(mComplainAllResponse.get(position).getCstname());
        holder.txtAddressValueID.setText(mComplainAllResponse.get(position).getCaddress());
        //holder.txtEngMobNoValueID.setText(mComplainAllResponse.get(position).getCmblno());


        holder.imgShareID.setOnClickListener(view -> {

            String shareData = "Complain No.:" + mComplainAllResponse.get(position).getCmpno(); //+"\nEng. Name:"+mComplainAllResponse.get(position).getEname()+"";

            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, shareData);
            sendIntent.setType("text/plain");

            Intent shareIntent = Intent.createChooser(sendIntent, null);
            mContext.startActivity(shareIntent);

        });

          holder.txtClickHereID.setOnClickListener(view -> {

                Intent mIntent = new Intent(mContext, AssginPendingComplainDetailActivity.class);
                mIntent.putExtra("Complain_number",mComplainAllResponse.get(position).getCmpno());
                mIntent.putExtra("mobile_number",mComplainAllResponse.get(position).getCmblno());
                mIntent.putExtra("StatusValue","01");
                mIntent.putExtra("complaint", "Pending Complaint");
                mContext.startActivity(mIntent);

        });

       /* holder.lvlMainItemViewID.setOnClickListener(view -> {

            //  view.getTag();

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

        });*/
    }

    @Override
    public int getItemCount() {
        // return galleryModelsList.size();
        if (mComplainAllResponse != null && mComplainAllResponse.size() > 0)
            return mComplainAllResponse.size();
        else
            return 0;
        //  return 5;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtDealerOEMValueID, txtComplainnoValueID, txtComplainDateValueID, txtMOBNumberValueID,txtCustomerNameValueID,txtClickHereID,
                txtAddressValueID,   txtEngMobNoHeadID, txtEngNameValueID, txtEngMobNoValueID;


        public ImageView imgShareID;

        public LinearLayout lvlMainItemViewID;

        public ViewHolder(View v) {

            super(v);
            txtClickHereID = (TextView) v.findViewById(R.id.txtClickHereID);
            imgShareID = (ImageView) v.findViewById(R.id.imgShareID);
            lvlMainItemViewID = (LinearLayout) v.findViewById(R.id.lvlMainItemViewID);
            txtEngMobNoHeadID = (TextView) v.findViewById(R.id.txtEngMobNoHeadID);
            txtDealerOEMValueID = (TextView) v.findViewById(R.id.txtDealerOEMValueID);
            txtComplainnoValueID =  (TextView) v.findViewById(R.id.txtComplainnoValueID);
            txtComplainDateValueID =  (TextView) v.findViewById(R.id.txtComplainDateValueID);
            txtMOBNumberValueID =   (TextView) v.findViewById(R.id.txtMOBNumberValueID);
            txtCustomerNameValueID =   (TextView) v.findViewById(R.id.txtCustomerNameValueID);
            txtAddressValueID =   (TextView) v.findViewById(R.id.txtAddressValueID);
            //  txtMaterialCodeValueID =   (TextView) v.findViewById(R.id.txtMaterialCodeValueID);
            //  txtSeriolNumberValueID =   (TextView) v.findViewById(R.id.txtSeriolNumberValueID);
            //   txtMaterialDescValueID =   (TextView) v.findViewById(R.id.txtMaterialDescValueID);
            txtEngNameValueID =   (TextView) v.findViewById(R.id.txtEngNameValueID);
            txtEngMobNoValueID =   (TextView) v.findViewById(R.id.txtEngMobNoValueID);
            //  txtWarrantyValueID =   (TextView) v.findViewById(R.id.txtWarrantyValueID);

        }
    }
}
