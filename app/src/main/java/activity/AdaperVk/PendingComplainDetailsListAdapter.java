package activity.AdaperVk;

import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.shaktipumps.shakti.shaktiServiceCenter.R;

import java.util.List;

import activity.BeanVk.ComplainAllResponse;
import activity.BeanVk.ComplainDetailListResponse;
import activity.complainvk.InstReportImageActivity;
import activity.complainvk.PendingComplainDetailActivity;


public class PendingComplainDetailsListAdapter extends RecyclerView.Adapter<PendingComplainDetailsListAdapter.ViewHolder> {

    private Context mContext;
    private String mStatusValue;
    private String mMobileNumber;

    private List<ComplainDetailListResponse> mComplainDetailListResponse;

   /* public PendingComplainListAdapter(Context mContext, List<ComplainAllResponse> mComplainAllResponse) {
        // this.galleryModelsList = galleryModelsList;
        this.mContext = mContext;
        this.mComplainAllResponse = mComplainAllResponse;

    }*/
    public PendingComplainDetailsListAdapter(Context mContext, List<ComplainDetailListResponse> mComplainDetailListResponse, String mStatusValue, String mMobileNumber) {
        this.mContext = mContext;
        this.mComplainDetailListResponse = mComplainDetailListResponse;
        this.mStatusValue = mStatusValue;
        this.mMobileNumber = mMobileNumber;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view1 = LayoutInflater.from(mContext).inflate(R.layout.pending_comp_details_item_row, parent, false);
        ViewHolder viewHolder1 = new ViewHolder(view1);
        return viewHolder1;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

       holder.txtwarranteeValueID.setText(mComplainDetailListResponse.get(position).getWarrantee());

       holder.txtComplainDateValueID.setText(mComplainDetailListResponse.get(position).getWarDate());

       holder.txtWarrenteeConditionsValueID.setText(mComplainDetailListResponse.get(position).getWarrantyCondition());
       holder.txtMaterialDescValueID.setText(mComplainDetailListResponse.get(position).getMaktx());
       holder.txtMaterialNoValueID.setText(mComplainDetailListResponse.get(position).getMatnr());
       holder.txtComplainnoValueID.setText(mComplainDetailListResponse.get(position).getCmpno());
       holder.txtsernrValueID.setText(mComplainDetailListResponse.get(position).getSernr());
       holder.txtReasonValueID.setText(mComplainDetailListResponse.get(position).getReason());
       holder.txtMOBNumberValueID.setText(mMobileNumber);

       holder.imgShareID.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               String shareData = "Complain No.: "+mComplainDetailListResponse.get(position).getCmpno()+"\n\nMaterial Code: "+mComplainDetailListResponse.get(position).getMatnr()+"\n\nWarranty : "+mComplainDetailListResponse.get(position).getWarrantee()+"\n\nWarranty condition: "
                       +mComplainDetailListResponse.get(position).getWarrantyCondition()+"\n\nSerial no.:"+mComplainDetailListResponse.get(position).getSernr()+"\n\nMaterial Desc.: "
                       +mComplainDetailListResponse.get(position).getMaktx()+"\n\nReason: "+mComplainDetailListResponse.get(position).getReason();

               Intent sendIntent = new Intent();
               sendIntent.setAction(Intent.ACTION_SEND);
               sendIntent.putExtra(Intent.EXTRA_TEXT, shareData);
               sendIntent.setType("text/plain");

               Intent shareIntent = Intent.createChooser(sendIntent, null);
               mContext.startActivity(shareIntent);

           }
       });

    }

    @Override
    public int getItemCount() {
        // return galleryModelsList.size();
        if (mComplainDetailListResponse != null && mComplainDetailListResponse.size() > 0)
            return mComplainDetailListResponse.size();
        else
            return 0;
       //  return 5;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

public TextView txtwarranteeValueID, txtMaterialDescValueID,txtComplainDateValueID,txtMaterialNoValueID, txtComplainnoValueID, txtMOBNumberValueID, txtsernrValueID, txtReasonValueID, txtWarrenteeConditionsValueID;


        public RelativeLayout rlvNotifyItemMainViewID;
        public LinearLayout lvlMainItemViewID;
        public ImageView imgShareID;
        public TextView txtBTNActionID, txtBTNPendingID, txtBTNClodeID, txtBTNUploadID;

        public ViewHolder(View v) {

            super(v);

            imgShareID = (ImageView) v.findViewById(R.id.imgShareID);
            txtWarrenteeConditionsValueID = (TextView) v.findViewById(R.id.txtWarrenteeConditionsValueID);
            txtwarranteeValueID = (TextView) v.findViewById(R.id.txtwarranteeValueID);
            txtMaterialDescValueID = (TextView) v.findViewById(R.id.txtMaterialDescValueID);
            txtComplainDateValueID = (TextView) v.findViewById(R.id.txtComplainDateValueID);
            txtMaterialNoValueID = (TextView) v.findViewById(R.id.txtMaterialNoValueID);
            txtComplainnoValueID = (TextView) v.findViewById(R.id.txtComplainnoValueID);
            txtMOBNumberValueID = (TextView) v.findViewById(R.id.txtMOBNumberValueID);
            txtsernrValueID = (TextView) v.findViewById(R.id.txtsernrValueID);
            txtReasonValueID = (TextView) v.findViewById(R.id.txtReasonValueID);





            lvlMainItemViewID = (LinearLayout) v.findViewById(R.id.lvlMainItemViewID);

          //  txtWarrantyValueID =   (TextView) v.findViewById(R.id.txtWarrantyValueID);

          }
    }





    android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            String mString = (String) msg.obj;
            Toast.makeText(mContext, mString, Toast.LENGTH_LONG).show();
        }
    };


}


