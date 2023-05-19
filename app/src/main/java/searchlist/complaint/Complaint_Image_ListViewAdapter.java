package searchlist.complaint;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Message;
import android.provider.MediaStore;

import android.util.Base64;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import activity.complaint.DisplayComplaintImageActivity;
import bean.ComplaintImage;
import database.DatabaseHelper;
import other.PermissionsIntent;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

public class Complaint_Image_ListViewAdapter extends BaseAdapter {

    // Declare Variables

    public static final String IMAGE_EXTENSION = "jpg";
    private static final int FILE_SELECT_CODE = -10;
    private static final int GALLERY_IMAGE_REQUEST_CODE = 101;
    private static final int RESULT_OK = -1;
    LayoutInflater inflater;
    String str_cmp_no = "", image_name, image_item;
    SharedPreferences pref;
    Context mContext;
    String cmp_cat, cmp_no;
    int requestCode;
    File mediaFile;
    String audio_record = "";
    String file_path;
    // ViewHolder holder;
    Intent data;
    String flag = "null";
    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
    };
    android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            String mString = (String) msg.obj;
            Toast.makeText(mContext, mString, Toast.LENGTH_LONG).show();
        }
    };
    private List<Complaint_Image_Name> complaintSearchlist = null;
    private ArrayList<Complaint_Image_Name> arraylist;
    private ProgressDialog progressDialog;
    private ArrayList<ComplaintImage> arraylist_ImageTaken = null;

    public Complaint_Image_ListViewAdapter(Context context, List<Complaint_Image_Name> complaintSearchlist, String cmp_category, List<ComplaintImage> list_complaintImageTaken, String lv_cmpno) {
        mContext = context;
        cmp_no = lv_cmpno;
        cmp_cat = cmp_category;
        this.complaintSearchlist = complaintSearchlist;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<Complaint_Image_Name>();
        this.arraylist.addAll(complaintSearchlist);

        progressDialog = new ProgressDialog(mContext);
        this.arraylist_ImageTaken = new ArrayList<ComplaintImage>();
        this.arraylist_ImageTaken.addAll(list_complaintImageTaken);

    }

    public static void check_Permission(final Context context) {

        if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // For example if the user has previously denied the permission.

            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PermissionsIntent.WRITE_EXTERNAL_STORAGE_PERMISSION);


        } else {
            // permission has not been granted yet. Request it directly.
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PermissionsIntent.WRITE_EXTERNAL_STORAGE_PERMISSION);
        }
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public int getCount() {
        return complaintSearchlist.size();
    }

    @Override
    public Complaint_Image_Name getItem(int position) {
        return complaintSearchlist.get(position);
    }

    // Filter Class
//    public void filter(String charText) {
//        charText = charText.toLowerCase(Locale.getDefault());
//
//        complaintSearchlist.clear();
//        if (charText.length() == 0) {
//            complaintSearchlist.addAll(arraylist);
//        }
//        else
//        {
//            for (Complaint_Image_Name sc : arraylist)
//            {
//                if ( sc.getName().toLowerCase(Locale.getDefault()).contains(charText)
//
//                    )
//
//
//                {
//                    complaintSearchlist.add(sc);
//                }
//            }
//        }
//        notifyDataSetChanged();
//    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.search_listview_complaint_image, null);

            holder.image_name = (TextView) view.findViewById(R.id.image_name);
            holder.complete_icon = (TextView) view.findViewById(R.id.complete_icon);

            view.setTag(holder);


        } else {
            holder = (ViewHolder) view.getTag();
        }


        holder.image_name.setText(complaintSearchlist.get(position).getName());
        image_item = "101" + complaintSearchlist.get(position).getItem();


        for (int i = 0; i < arraylist_ImageTaken.size(); i++) {
            // image_item = arraylist_ImageTaken.get(i).getPosnr() ;
            Log.e("CAtegory", "&&&&&&" + cmp_cat);
            Log.e("SIZE", "&&&&&&" + arraylist_ImageTaken.size());
            Log.e("CMPNO", "&&&&&&" + cmp_no);
            Log.e("AUDIORECORD", "&&&&&&" + audio_record);
            Log.e("IMAGE_ITEM", "&&&&&&" + image_item);
            Log.e("IMAGE_ITEM12", "&&&&&&" + arraylist_ImageTaken.get(i).getPosnr());

            if (arraylist_ImageTaken.get(i).getPosnr().equalsIgnoreCase(image_item)) {
                holder.complete_icon.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.done, 0);

            }

        }


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                image_item = "101" + complaintSearchlist.get(position).getItem();
                image_name = complaintSearchlist.get(position).getName();
                cmp_cat = complaintSearchlist.get(position).getCategory();

                Log.e("IMAGEITEM","&&&&"+image_item);
                Log.e("IMAGENAME","&&&&"+image_name);


                for (int i = 0; i < arraylist_ImageTaken.size(); i++) {

                    //image_item =  arraylist_ImageTaken.get(i).getPosnr();

                    //  Log.d("flag",arraylist_ImageTaken.get(i).getPosnr()+"--"+image_item);

                    flag = "null";
                    if (arraylist_ImageTaken.get(i).getPosnr().equalsIgnoreCase(image_item)) {


                        flag = "X";
                        break;

                    }

                }


                Log.d("flag", flag);

                if (flag.equalsIgnoreCase("X")) {


                    Intent intent = new Intent(mContext, DisplayComplaintImageActivity.class);
                    //Log.d("cmp_category1",cmp_no+"--"+cmp_category);

                    intent.putExtra("cmp_no", cmp_no);
                    intent.putExtra("cmp_posnr", image_item);
                    intent.putExtra("cmp_category", cmp_cat);

                    intent.putExtra("image_name", image_name);

                    mContext.startActivity(intent);


                } else {
                    if (ContextCompat.checkSelfPermission(mContext,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        check_Permission(mContext);
                    } else {

                        image_name = complaintSearchlist.get(position).getName();
                        image_item = "101" + complaintSearchlist.get(position).getItem();


                        showFileChooser();


                    }

                }


            }
        });


        notifyDataSetChanged();

        return view;
    }

    private void showFileChooser() {


        if (ActivityCompat.checkSelfPermission(mContext, READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT <= 19) {
                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                ((Activity) mContext).startActivityForResult(Intent.createChooser(i, "Select Photo"), GALLERY_IMAGE_REQUEST_CODE);

            } else {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                ((Activity) mContext).startActivityForResult(Intent.createChooser(intent, "Select Photo"), GALLERY_IMAGE_REQUEST_CODE);
            }

        } else {
            if (!hasPermissions(mContext, PERMISSIONS)) {
                ActivityCompat.requestPermissions((Activity) mContext, PERMISSIONS, PERMISSION_ALL);
            }
        }


      /*  Intent intent = new Intent();
        intent.setType("image/*");
        intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.INTERNAL_CONTENT_URI);

        ((Activity)mContext).startActivityForResult(Intent.createChooser(intent, "Select Photo"),GALLERY_IMAGE_REQUEST_CODE);
*/
    }


    public String onActivityResult(int requestCode, int resultCode, Intent lv_data, String lv_cmp_category, String lv_cmp_no) {
        //Log.d("resultCode ", ""+resultCode);

        cmp_cat = lv_cmp_category;
        cmp_no = lv_cmp_no;
        data = lv_data;
        image_item = image_item;




        Log.e("ITEM_NUMBER1", "$$$$" + cmp_cat);// image item no send from file piker
        Log.e("ITEM_NUMBER2", "$$$$" + cmp_no);// image item no send from file piker
        Log.e("ITEM_NUMBER3", "$$$$" + data);// image item no send from file piker
        Log.e("ITEM_NUMBER4", "$$$$" + image_item);// image item no send from file piker

        //   Log.d("MyAdapter", "onActivityResult" +""+resultCode +""+requestCode);
        if (requestCode == GALLERY_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {


                try {


                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
                    alertDialog.setTitle("Data Save alert !");
                    alertDialog.setMessage("Do you want to save data ?");

                    // On pressing Settings button
                    alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {


                            try {

                                // holder.complete_icon.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.done, 0);

                                flag = "X";

                                if (data != null) {

                                    Uri selectedImageUri = data.getData();
                                    String selectedImagePath = getImagePath(selectedImageUri);
                                    BitmapFactory.Options options = new BitmapFactory.Options();


                                    final int REQUIRED_SIZE = 200;
                                    int scale = 1;
                                    while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                                            && options.outHeight / scale / 2 >= REQUIRED_SIZE)
                                        scale *= 2;
                                    Log.e("Scale", "&&&&" + scale);
                                    options.inSampleSize = scale;
                                    options.inJustDecodeBounds = false;
                                    Bitmap bitmap;

                                    try {
                                        Log.e("IMAGEURI", "&&&&" + selectedImageUri);
                                        if (selectedImageUri != null) {

                                            //Bitmap bm = BitmapFactory.decodeFile(selectedImagePath, options);

                                            //bitmap = BitmapFactory.decodeFile(selectedImagePath, options);
                                            // bitmap=BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));

                                            bitmap=BitmapFactory.decodeStream(mContext.getContentResolver().openInputStream(selectedImageUri));

                                            int count = bitmap.getByteCount();

                                            Log.e("Count", "&&&&&" + count);

                                            ByteArrayOutputStream byteArrayBitmapStream = new ByteArrayOutputStream();

                                            if (count <= 100000) {
                                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayBitmapStream);
                                            } else if (count <= 200000) {
                                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayBitmapStream);
                                            } else if (count <= 300000) {
                                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayBitmapStream);
                                            } else if (count <= 400000) {
                                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayBitmapStream);
                                            } else if (count <= 500000) {
                                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayBitmapStream);
                                            } else if (count <= 600000) {
                                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayBitmapStream);
                                            } else if (count <= 700000) {
                                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayBitmapStream);
                                            } else if (count <= 800000) {
                                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayBitmapStream);
                                            } else if (count <= 900000) {
                                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayBitmapStream);
                                            } else if (count <= 1000000) {
                                                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayBitmapStream);
                                            } else if (count <= 2000000) {
                                                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayBitmapStream);
                                            } else if (count <= 3000000) {
                                                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayBitmapStream);
                                            } else if (count <= 4000000) {
                                                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayBitmapStream);
                                            } else if (count <= 5000000) {
                                                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayBitmapStream);
                                            } else if (count <= 6000000) {
                                                bitmap.compress(Bitmap.CompressFormat.JPEG, 75, byteArrayBitmapStream);
                                            } else if (count <= 7000000) {
                                                bitmap.compress(Bitmap.CompressFormat.JPEG, 75, byteArrayBitmapStream);
                                            } else if (count <= 8000000) {
                                                bitmap.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayBitmapStream);
                                            } else if (count <= 9000000) {
                                                bitmap.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayBitmapStream);
                                            } else if (count <= 10000000) {
                                                bitmap.compress(Bitmap.CompressFormat.JPEG, 55, byteArrayBitmapStream);
                                            } else if (count <= 20000000) {
                                                bitmap.compress(Bitmap.CompressFormat.JPEG, 55, byteArrayBitmapStream);
                                            } else if (count <= 30000000) {
                                                bitmap.compress(Bitmap.CompressFormat.JPEG, 55, byteArrayBitmapStream);
                                            } else if (count <= 40000000) {
                                                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayBitmapStream);
                                            } else if (count <= 50000000) {
                                                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayBitmapStream);
                                            } else if (count <= 60000000) {
                                                bitmap.compress(Bitmap.CompressFormat.JPEG, 40, byteArrayBitmapStream);
                                            } else if (count <= 70000000) {
                                                bitmap.compress(Bitmap.CompressFormat.JPEG, 40, byteArrayBitmapStream);
                                            } else if (count <= 80000000) {
                                                bitmap.compress(Bitmap.CompressFormat.JPEG, 40, byteArrayBitmapStream);
                                            } else if (count <= 90000000) {
                                                bitmap.compress(Bitmap.CompressFormat.JPEG, 40, byteArrayBitmapStream);
                                            } else {
                                                bitmap.compress(Bitmap.CompressFormat.JPEG, 35, byteArrayBitmapStream);
                                            }
                                            // OutputStream out;

                                            byte[] byteArray = byteArrayBitmapStream.toByteArray();


                                            /*String root = Environment.getExternalStorageDirectory().getAbsolutePath()+"/";
                                            File createDir = new File(root + "SSales/" + cmp_no);
                                            if(!createDir.exists()) {
                                                createDir.mkdir();
                                            }
                                            File file = new File(root + "SSales/" + cmp_no + "/" + image_item + "." +IMAGE_EXTENSION);
                                            file.createNewFile();
                                            out = new FileOutputStream(file);

                                            out.write(byteArray);
                                            out.close();*/

                                            long size = byteArray.length;

                                            Log.e("SIZE1234", "&&&&" + size);

                                            Log.e("BYTEARRAY", "&&&&" + byteArray.toString());

                                            audio_record = Base64.encodeToString(byteArray, Base64.DEFAULT);

                                            Log.e("audio_record", "&&&&" + audio_record);

                                            DatabaseHelper dataHelper = new DatabaseHelper(mContext);
                                            dataHelper.insertComplaintImage
                                                    (cmp_no,
                                                            image_item,
                                                            cmp_cat,
                                                            audio_record);

                                            Log.e("CAtegory1", "&&&&&&" + cmp_cat);
                                            Log.e("CMPNO1", "&&&&&&" + cmp_no);
                                            Log.e("AUDIORECORD1", "&&&&&&" + audio_record);
                                            Log.e("IMAGE_ITEM1", "&&&&&&" + image_item);

                                        } else {
                                            Toast.makeText(mContext, "Invalid File Location", Toast.LENGTH_LONG).show();
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }


                                }
                                Toast.makeText(mContext, "Image Saved Successfully", Toast.LENGTH_LONG).show();
                                ((Activity) mContext).finish();

                            } catch (Exception e) {
                                Toast.makeText(mContext, "Invalid File Selection", Toast.LENGTH_LONG).show();

                            }
                        }
                    });

                    // on pressing cancel button

                    alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    // Showing Alert Message
                    alertDialog.show();


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }


        return audio_record;
    }



    public String getImagePath(Uri uri) {

        String s = null;

        File file = new File(uri.getPath());
        String[] filePath = file.getPath().split(":");
        String image_id = filePath[filePath.length - 1];

        if (uri == null) {
            // TODO perform some logging or show user feedback
            return null;
        } else {
            String[] projection = {String.valueOf(MediaStore.Images.Media.DATA)};

            Cursor cursor1 = ((Activity) mContext).getContentResolver().query(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, MediaStore.Images.Media._ID + " = ? ", new String[]{image_id}, null);
            Cursor cursor2 = ((Activity) mContext).getContentResolver().query(uri, projection, null, null, null);

            Log.e("CUR1", "&&&&" + cursor1);
            Log.e("CUR2", "&&&&" + cursor2);

            if (cursor1 == null && cursor2 == null) {
                return null;
            } else {

                int column_index = 0;
                if (cursor1 != null) {
                    column_index = cursor1.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    cursor1.moveToFirst();

                    if (cursor1.moveToFirst()) {
                        s = cursor1.getString(column_index);
                    }
                    cursor1.close();
                }
                int column_index1 = 0;
                if (cursor2 != null) {
                    column_index1 = cursor2.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    cursor2.moveToFirst();

                    if (cursor2.moveToFirst()) {
                        s = cursor2.getString(column_index1);
                    }
                    cursor2.close();
                }

                return s;
            }
        }
    }

    public class ViewHolder {

        TextView image_name, complete_icon;

    }
}