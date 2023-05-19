package searchlist.complaint;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import com.shaktipumps.shakti.shaktiServiceCenter.R;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import activity.CustomUtility;
import activity.GPSTracker;
import activity.complaint.IssueMaterialListComplaintActivity;
import activity.complaint.PendAwtComplaintDetailActivity;
import database.DatabaseHelper;

public class PendAppComplaintListViewAdapter extends BaseAdapter {

    // Declare Variables
    Context mContext;
    LayoutInflater inflater;
    String str_cmp_no = "";
    String lv_cmp_no = "null";
    SharedPreferences pref;
    android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            String mString = (String) msg.obj;
            Toast.makeText(mContext, mString, Toast.LENGTH_LONG).show();
        }
    };
    private List<SearchComplaint> complaintSearchlist = null;
    private ArrayList<SearchComplaint> arraylist;
    private ProgressDialog progressDialog;
    private DatabaseHelper databaseHelper;
    String from1;

    public PendAppComplaintListViewAdapter(Context context, List<SearchComplaint> complaintSearchlist,String from) {
        mContext = context;
        from1 = from;
        this.complaintSearchlist = complaintSearchlist;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<SearchComplaint>();
        this.arraylist.addAll(complaintSearchlist);
        progressDialog = new ProgressDialog(mContext);
        databaseHelper = new DatabaseHelper(mContext);
    }

    @Override
    public int getCount() {
        return complaintSearchlist.size();
    }

    @Override
    public SearchComplaint getItem(int position) {
        return complaintSearchlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("SetTextI18n")
    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.search_listview_complaint, null);

            holder.cmpno = (TextView) view.findViewById(R.id.cmpno);
            holder.cmpdt = (TextView) view.findViewById(R.id.cmpdt);
            holder.cmppnddy = (TextView) view.findViewById(R.id.cmppnddy);
            holder.customer_name = (TextView) view.findViewById(R.id.customer_name);
            holder.address = (TextView) view.findViewById(R.id.address);
            holder.mobile_no = (TextView) view.findViewById(R.id.mobile_no);
            holder.dealer_name = (TextView) view.findViewById(R.id.dealer_name);
            holder.employee_name = (TextView) view.findViewById(R.id.employee_name);
            holder.mobile_image = (TextView) view.findViewById(R.id.mobile_image);
            holder.address_image = (TextView) view.findViewById(R.id.address_image);
            holder.comp_dtl = (TextView) view.findViewById(R.id.comp_dtl);
            holder.comp_start = (TextView) view.findViewById(R.id.comp_start);
            holder.pend_frwd_app = (TextView) view.findViewById(R.id.pend_frwd_app);
            holder.frwd_app_rmrk = (TextView) view.findViewById(R.id.frwd_app_rmrk);
            holder.epc_txt = (TextView) view.findViewById(R.id.epc_txt);
            holder.comp_iss_mat_txt = (TextView) view.findViewById(R.id.comp_iss_mat);

            holder.comp_start.setVisibility(View.GONE);
            holder.comp_iss_mat_txt.setVisibility(View.GONE);
            holder.frwd_app_rmrk.setVisibility(View.VISIBLE);

            view.setTag(holder);


        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews


        str_cmp_no = "Comp.No : " + complaintSearchlist.get(position).getCmpno();


        holder.cmpno.setText(str_cmp_no);
        holder.cmpdt.setText(complaintSearchlist.get(position).getCmpdt());
        holder.pend_frwd_app.setVisibility(View.VISIBLE);
        if(from1.equalsIgnoreCase("P")) {
            holder.pend_frwd_app.setText("Froward for Approval--- "+complaintSearchlist.get(position).getAwait_apr_pernr_nm());
        }
        else{
            holder.pend_frwd_app.setText("Pending for Approval--- "+complaintSearchlist.get(position).getPend_apr_pernr_nm());
        }

        holder.frwd_app_rmrk.setText("Forward for Approval Remark--- "+complaintSearchlist.get(position).getAwait_apr_remark());

        holder.cmppnddy.setText("Pending "+complaintSearchlist.get(position).getPenday()+" Days");

        holder.customer_name.setText(complaintSearchlist.get(position).getCustomer_name());
        holder.address.setText(complaintSearchlist.get(position).getAddress());
        holder.mobile_no.setText(complaintSearchlist.get(position).getMob_no());
        holder.dealer_name.setText(complaintSearchlist.get(position).getDistributor_name());
        holder.employee_name.setText(complaintSearchlist.get(position).getEname());

        if(complaintSearchlist.get(position).getEpc().equalsIgnoreCase("X"))
        {
            holder.epc_txt.setText("EPC");
        }
        else if(complaintSearchlist.get(position).getEpc().equalsIgnoreCase("Z")){
            holder.epc_txt.setText("EPC Forwarded");
        }
        else if(complaintSearchlist.get(position).getEpc().equalsIgnoreCase("Y")){
            holder.epc_txt.setText("Forwarded");
        }
        else{
            holder.epc_txt.setText("");
        }

        holder.mobile_image.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.call, 0);

        holder.address_image.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.map, 0);

        holder.comp_iss_mat_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(mContext, IssueMaterialListComplaintActivity.class);
                intent.putExtra("cmpno", complaintSearchlist.get(position).getCmpno());
                mContext.startActivity(intent);
            }
        });

        holder.mobile_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!TextUtils.isEmpty(complaintSearchlist.get(position).getMob_no())) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.CALL_PHONE}, 222);
                        } else {
                            Make_Call(complaintSearchlist.get(position).getMob_no());
                        }

                    } else {
                        Make_Call(complaintSearchlist.get(position).getMob_no());
                    }
                }
            }
        });

        holder.mobile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!TextUtils.isEmpty(complaintSearchlist.get(position).getMob_no())) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.CALL_PHONE}, 222);
                        } else {
                            Make_Call(complaintSearchlist.get(position).getMob_no());
                        }

                    } else {
                        Make_Call(complaintSearchlist.get(position).getMob_no());
                    }
                }
            }
        });


        holder.address_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressDialog = ProgressDialog.show(mContext, "", "Checking Internet Connection.");

                new Thread(new Runnable() {
                    //
                    @Override
                    public void run() {

                        if (CustomUtility.isOnline(mContext)) {
                              if ((progressDialog != null) && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                            progressDialog = null;
                        };
                            String str = complaintSearchlist.get(position).getAddress();
                            String url = "http://maps.google.co.in/maps?q=" + str;
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                            mContext.startActivity(intent);
                        } else {
                              if ((progressDialog != null) && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                            progressDialog = null;
                        };
                            Message msg = new Message();
                            msg.obj = "Please ON internet Connection for this function.";
                            mHandler.sendMessage(msg);
                        }
                    }
                }).start();


            }
        });


        holder.address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressDialog = ProgressDialog.show(mContext, "", "Checking Internet Connection.");

                new Thread(new Runnable() {
                    //
                    @Override
                    public void run() {

                        if (CustomUtility.isOnline(mContext)) {
                              if ((progressDialog != null) && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                            progressDialog = null;
                        };
                            String str = complaintSearchlist.get(position).getAddress();
                            String url = "http://maps.google.co.in/maps?q=" + str;
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                            mContext.startActivity(intent);
                        } else {
                              if ((progressDialog != null) && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                            progressDialog = null;
                        };
                            Message msg = new Message();
                            msg.obj = "Please ON internet Connection for this function.";
                            mHandler.sendMessage(msg);
                        }
                    }
                }).start();


            }
        });


        holder.comp_dtl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(mContext, PendAwtComplaintDetailActivity.class);
                intent.putExtra("cmpno", complaintSearchlist.get(position).getCmpno());
                if(from1.equalsIgnoreCase("P")) {
                    intent.putExtra("complaint_display", "Pending for Approval");
                }else{
                    intent.putExtra("complaint_display", "Awaiting for Approval");
                }
                //databaseHelper.deleteImage();
                mContext.startActivity(intent);


            }
        });


        //** set complaint distance button


        holder.comp_start.setBackgroundResource(R.drawable.rounded_corner_blue);
        ;

        holder.comp_start.setEnabled(true);

//        holder.comp_start.setEnabled(true);
//        holder.comp_start.setBackgroundColor(Color.parseColor("#0179b6"));
//        holder.comp_start.setTextColor(Color.parseColor("#ffffff"));


//        if ( complaintSearchlist.get(position).getComplaint_distance())
//        {
//            holder.comp_start.setEnabled(true);
//            holder.comp_start.setBackgroundColor(Color.parseColor("#0179b6"));
//            holder.comp_start.setTextColor(Color.parseColor("#ffffff"));
//
//        }
//       else
//        {
//            holder.comp_start.setEnabled(false);
//            holder.comp_start.setBackgroundColor(Color.parseColor("#8b8b8c"));
//            holder.comp_start.setTextColor(Color.parseColor("#ffffff"));
//        }

        //** set complaint distance button


        holder.comp_start.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                DatabaseHelper dataHelper = new DatabaseHelper(mContext);


                if (dataHelper.getTableComplaintDistance(complaintSearchlist.get(position).getCmpno())) {

                    holder.comp_start.setEnabled(false);
                    holder.comp_start.setBackgroundColor(Color.parseColor("#8b8b8c"));
                    holder.comp_start.setTextColor(Color.parseColor("#ffffff"));

                    CustomUtility.ShowToast("You have Already Click on Start Complaint" + " " + complaintSearchlist.get(position).getCmpno(), mContext);
                } else {

                    //  CustomUtility.ShowToast("Click"+"--"+  complaintSearchlist.get(position).getCmpno(), mContext);

                    if (CustomUtility.CheckGPS(mContext)) {
                        GPSTracker gps = new GPSTracker(mContext);
                        double latitude = Double.parseDouble(new DecimalFormat("##.#####").format(gps.getLatitude()));
                        double longitude = Double.parseDouble(new DecimalFormat("##.#####").format(gps.getLongitude()));

                        String.valueOf(latitude);
                        String.valueOf(longitude);

                        if (latitude != 0.0) {


                            CustomUtility.ShowToast(String.valueOf(latitude) + "--" + String.valueOf(longitude), mContext);


                            pref = mContext.getSharedPreferences("MyPref", 0);

                            dataHelper.insertComplaintDistance
                                    (complaintSearchlist.get(position).getCmpno(),
                                            pref.getString("key_username", "userid"),
                                            String.valueOf(latitude) + "," + String.valueOf(longitude)
                                    );


                            holder.comp_start.setEnabled(false);
                            holder.comp_start.setBackgroundColor(Color.parseColor("#8b8b8c"));
                            holder.comp_start.setTextColor(Color.parseColor("#ffffff"));

                        }
                    }

                }

            }
        });

//
//        // Listen for ListView Item Click
//
//        view.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View arg0) {
//
//                Log.d("arg",""+ arg0 +"--" + position);
//
////                double latitude = 40.714728;
////                double longitude = -73.998672;
////                String label = "ABC Label";
////                String uriBegin = "geo:" + latitude + "," + longitude;
////                String query = latitude + "," + longitude + "(" + label + ")";
////                String encodedQuery = Uri.encode(query);
////                String uriString = uriBegin + "?q=" + encodedQuery + "&z=16";
////                Uri uri = Uri.parse(uriString);
////                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, uri);
////                mContext.startActivity(intent);
//
//
//
//
//
//              //  String url = "http://maps.google.com/maps?daddr="+ complaintSearchlist.get(position).getAddress();
////                String str = complaintSearchlist.get(position).getAddress() ;
////                String url = "http://maps.google.co.in/maps?q="+ str;
////                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,  Uri.parse(url));
////                mContext.startActivity(intent);
//
//
//
//
//            }
//        });

        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());

        complaintSearchlist.clear();
        if (charText.length() == 0) {
            complaintSearchlist.addAll(arraylist);
        } else {
            for (SearchComplaint sc : arraylist) {

                if (sc.getCmpno().toLowerCase(Locale.getDefault()).contains(charText) ||
                        sc.getAddress().toLowerCase(Locale.getDefault()).contains(charText) ||
                        sc.getCustomer_name().toLowerCase(Locale.getDefault()).contains(charText)
                ) {
                    complaintSearchlist.add(sc);
                }


            }
        }
        notifyDataSetChanged();


    }

    private void Make_Call(String mobile) {
        try {
            Intent intent = new Intent(Intent.ACTION_CALL);

            intent.setData(Uri.parse("tel:" + mobile));
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mContext.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class ViewHolder {

        TextView cmpno, cmpdt,cmppnddy, customer_name,epc_txt, address, mobile_no, dealer_name,
                employee_name, mobile_image, address_image, comp_dtl, edit, comp_start,pend_frwd_app,
                frwd_app_rmrk,comp_iss_mat_txt;

    }

    public static String formateDate(String date) {
        String formatedDate = "";
        try {
            SimpleDateFormat formate = new SimpleDateFormat("dd.MM.yyyy");
            Date mDate = formate.parse(date);
//            SimpleDateFormat appFormate = newworkorder SimpleDateFormat("dd MMM, yyyy");
            SimpleDateFormat appFormate = new SimpleDateFormat("MM/dd/yyyy");

            formatedDate = appFormate.format(mDate);
            Log.i("Result", "mDate " + formatedDate);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return formatedDate;
    }
}