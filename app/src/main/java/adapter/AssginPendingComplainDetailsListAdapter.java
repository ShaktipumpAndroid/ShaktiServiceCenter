package adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.shaktipumps.shakti.shaktiServiceCenter.R;
import java.util.List;
import bean.SubordinateAssginComplainBean;

public class AssginPendingComplainDetailsListAdapter extends RecyclerView.Adapter<AssginPendingComplainDetailsListAdapter.ViewHolder> {

private final Context mContext;

private final List<SubordinateAssginComplainBean> mComplainDetailListResponse;

    public AssginPendingComplainDetailsListAdapter(Context mContext,  List<SubordinateAssginComplainBean> mComplainDetailListResponse) {
        this.mContext = mContext;
        this.mComplainDetailListResponse = mComplainDetailListResponse;
    }


    @Override
public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view1 = LayoutInflater.from(mContext).inflate(R.layout.pending_comp_details_item_row, parent, false);
        return new ViewHolder(view1);
        }

    @Override
public void onBindViewHolder(final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        holder.txtwarranteeValueID.setText(mComplainDetailListResponse.get(position).getWarranty());
        holder.txtComplainDateValueID.setVisibility(View.GONE);
        holder.txtWarrenteeConditionsValueID.setText(mComplainDetailListResponse.get(position).getW_waranty());
        holder.txtMaterialDescValueID.setText(mComplainDetailListResponse.get(position).getMaktx());
        holder.txtMaterialNoValueID.setText(mComplainDetailListResponse.get(position).getMatnr());
        holder.txtComplainnoValueID.setText(mComplainDetailListResponse.get(position).getCmpno());
        holder.txtsernrValueID.setText(mComplainDetailListResponse.get(position).getSernr());
        holder.txtReasonValueID.setText(mComplainDetailListResponse.get(position).getReason());
        holder.txtMOBNumberValueID.setText(mComplainDetailListResponse.get(position).getCmblno());
        holder.imgShareID.setVisibility(View.GONE);
        holder.txtComplainDateHeadID.setVisibility(View.GONE);
       /* holder.imgShareID.setOnClickListener(view -> {

        String shareData = "Complain No.: "+mComplainDetailListResponse.get(position).getCmpno()+"\n\nMaterial Code: "+mComplainDetailListResponse.get(position).getMatnr()+"\n\nWarranty : "+mComplainDetailListResponse.get(position).getWarrantee()+"\n\nWarranty condition: "
        +mComplainDetailListResponse.get(position).getWarrantyCondition()+"\n\nSerial no.:"+mComplainDetailListResponse.get(position).getSernr()+"\n\nMaterial Desc.: "
        +mComplainDetailListResponse.get(position).getMaktx()+"\n\nReason: "+mComplainDetailListResponse.get(position).getReason();

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, shareData);
        sendIntent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, null);
        mContext.startActivity(shareIntent);

        });*/
        holder.txtForwordID.setVisibility(View.GONE);
     /*   holder.txtForwordID.setOnClickListener(v -> {
        Intent mIntent = new Intent(mContext, ForwardList.class);
        mIntent.putExtra("comNo",mComplainDetailListResponse.get(position).getCmpno());
        mContext.startActivity(mIntent);
        });*/

        }

@SuppressLint("SuspiciousIndentation")
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

    public TextView txtwarranteeValueID,txtComplainDateHeadID, txtMaterialDescValueID,txtComplainDateValueID,txtMaterialNoValueID, txtComplainnoValueID, txtMOBNumberValueID, txtsernrValueID, txtReasonValueID, txtWarrenteeConditionsValueID,txtForwordID;


    public LinearLayout lvlMainItemViewID;
    public ImageView imgShareID;


    public ViewHolder(View v) {

        super(v);

        imgShareID =  v.findViewById(R.id.imgShareID);
        txtWarrenteeConditionsValueID =  v.findViewById(R.id.txtWarrenteeConditionsValueID);
        txtwarranteeValueID =  v.findViewById(R.id.txtwarranteeValueID);
        txtMaterialDescValueID =  v.findViewById(R.id.txtMaterialDescValueID);
        txtComplainDateValueID =  v.findViewById(R.id.txtComplainDateValueID);
        txtMaterialNoValueID =  v.findViewById(R.id.txtMaterialNoValueID);
        txtComplainnoValueID =  v.findViewById(R.id.txtComplainnoValueID);
        txtMOBNumberValueID =  v.findViewById(R.id.txtMOBNumberValueID);
        txtsernrValueID =  v.findViewById(R.id.txtsernrValueID);
        txtReasonValueID =  v.findViewById(R.id.txtReasonValueID);
        txtComplainDateHeadID = v.findViewById(R.id.txtComplainDateHeadID);
        txtForwordID = v.findViewById(R.id.txtForwordID);



        lvlMainItemViewID = (LinearLayout) v.findViewById(R.id.lvlMainItemViewID);

        //  txtWarrantyValueID =   (TextView) v.findViewById(R.id.txtWarrantyValueID);

    }

}


}


