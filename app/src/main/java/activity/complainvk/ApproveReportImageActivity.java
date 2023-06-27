package activity.complainvk;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.os.Environment.getExternalStorageDirectory;
import static android.os.Environment.getExternalStoragePublicDirectory;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.shaktipumps.shakti.shaktiServiceCenter.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import activity.AdaperVk.ImageAdapter;
import activity.BaseActivity;
import activity.BeanVk.ComplainDetailListResponse;
import activity.GPSTracker;
import activity.MainActivity1;
import activity.utility.CameraUtils;
import activity.utility.CustomUtility;
import database.DatabaseHelper;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import webservice.CustomHttpClient;
import webservice.WebURL;


public class ApproveReportImageActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks {
    public static final int RC_FILE_PICKER_PERM = 321;
    public static final int BITMAP_SAMPLE_SIZE = 4 ;
    public static final int MEDIA_TYPE_IMAGE = 1;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int GALLERY_IMAGE_REQUEST_CODE = 101;
    Context mContext;
    File file;
    String mUserID;

    private List<ComplainDetailListResponse> mComplainDetailListResponse;
    DatabaseHelper dataHelper;
    double inst_latitude_double,
            inst_longitude_double;
    String sync_data = "0",lat,lng,type="SERVICE/";
    int PERMISSION_ALL = 1;
    Uri fileUri1;
    public static final String GALLERY_DIRECTORY_NAME = "ShaktiServiceCenter";
    String[] PERMISSIONS = {
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
    };
    String imageStoragePath, enq_docno, status, photo1_text, photo2_text, photo3_text, photo4_text, photo5_text, photo6_text, photo7_text, photo8_text, photo9_text, photo10_text, photo11_text, photo12_text,photo13_text;
    TextView  photo10,photo11,photo12,photo13, txtBTNApproveSaveID;
    boolean photo10_flag = false;
    boolean photo11_flag = false;
    boolean photo12_flag = false;
    boolean photo13_flag = false;

    private String mHomePath, PathHolder, Filename,cust_name, mStatusCheck;
    int mListSize;
    String item1, item2, item3;
    private ArrayList<String> photoPaths = new ArrayList<>();
    private ArrayList<String> docPaths = new ArrayList<>();
    private RecyclerView recyclerView;

    private String mAMT1, mAMT2, mAMT3, mRMK;

    private EditText edtAmountId1, edtAmountId2, edtAmountId3, edtRemarkAMTID;



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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approve_image);
        mContext = this;


        dataHelper = new DatabaseHelper(mContext);
        mUserID = activity.CustomUtility.getSharedPreferences(mContext,"userID");

        getGpsLocation();

      //  CustomUtility.setSharedPreference(mContext, "INSTSYNC" + enq_docno, "");

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {


                mStatusCheck= null;
                cust_name= null;
                enq_docno= null;
                item1= null;
                item2= null;
                item3= null;

            } else {


                mStatusCheck= extras.getString("StatusCheck");
                cust_name= extras.getString("cust_name");
                enq_docno= extras.getString("inst_id");
                item1= extras.getString("item1");
                item2= extras.getString("item2");
                item3= extras.getString("item3");

            }
        } else {

            mStatusCheck= (String) savedInstanceState.getSerializable("StatusCheck");
            cust_name= (String) savedInstanceState.getSerializable("cust_name");
            enq_docno= (String) savedInstanceState.getSerializable("inst_id");
            item1= (String) savedInstanceState.getSerializable("item1");
            item2= (String) savedInstanceState.getSerializable("item2");
            item3= (String) savedInstanceState.getSerializable("item3");

        }

        Log.e("STATUS","&&&&"+status);

        File root = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),GALLERY_DIRECTORY_NAME);

        File dir = new File(root.getAbsolutePath() + "/SKAPP/SERVICE/"); //it is my root directory

        File billno = new File(root.getAbsolutePath() + "/SKAPP/SERVICE/" + enq_docno); // it is my sub folder directory .. it can vary..

        try {
            if (!dir.exists()) {
                dir.mkdirs();
            }
            if (!billno.exists()) {
                billno.mkdirs();
            }

        } catch (Exception e) {
            e.printStackTrace();

        }


        Toolbar mToolbar =  findViewById(R.id.toolbar);
        mToolbar.setTitle("Complaint:- "+enq_docno);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        edtAmountId1 = findViewById(R.id.edtAmountId1);
        edtAmountId2 = findViewById(R.id.edtAmountId2);
        edtAmountId3 = findViewById(R.id.edtAmountId3);
        edtRemarkAMTID = findViewById(R.id.edtRemarkAMTID);
        txtBTNApproveSaveID =  findViewById(R.id.txtBTNApproveSaveID);
        photo10 =  findViewById(R.id.photo10);
        photo11 =  findViewById(R.id.photo11);
        photo12 =  findViewById(R.id.photo12);
        photo13 =  findViewById(R.id.photo13);

        mListSize= WebURL.EDITTEXT_SHOW_SIZE;

        System.out.println("mListSize==>>"+mListSize);

        if(mListSize==1)
        {
            edtAmountId1.setVisibility(View.VISIBLE);
            edtAmountId2.setVisibility(View.GONE);
            edtAmountId3.setVisibility(View.GONE);
        }
        else if(mListSize==2)
        {
            edtAmountId1.setVisibility(View.VISIBLE);
            edtAmountId2.setVisibility(View.VISIBLE);
            edtAmountId3.setVisibility(View.GONE);

        }
        else if(mListSize==3)
        {
            edtAmountId1.setVisibility(View.VISIBLE);
            edtAmountId2.setVisibility(View.VISIBLE);
            edtAmountId3.setVisibility(View.VISIBLE);
        }


        recyclerView =  findViewById(R.id.file_list);

        txtBTNApproveSaveID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mRMK =edtRemarkAMTID.getText().toString().trim();

                if(activity.CustomUtility.getSharedPreferences(mContext, enq_docno + "PHOTO_10")!=null && !activity.CustomUtility.getSharedPreferences(mContext, enq_docno + "PHOTO_10").equalsIgnoreCase(""))
                {
                    if(mRMK != null && !mRMK.equalsIgnoreCase(""))
                    {
                        initAmtValidation();
                    }
                    else
                    {
                        Toast.makeText(mContext, "Please enter remark", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(mContext, "Please take Falty Material/Mager meges Photo ", Toast.LENGTH_SHORT).show();
                }




            }
        });


        setData();


        photo10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (photo10_text == null || photo10_text.isEmpty()) {
                    showConfirmationGallery(DatabaseHelper.KEY_PHOTO10, "PHOTO_10");
                } else {
                    showConfirmationAlert(DatabaseHelper.KEY_PHOTO10, photo10_text, "PHOTO_10");

                }
            }
        });

        photo11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (photo11_text == null || photo11_text.isEmpty()) {
                    showConfirmationGallery(DatabaseHelper.KEY_PHOTO11, "PHOTO_11");
                } else {
                    showConfirmationAlert(DatabaseHelper.KEY_PHOTO11, photo11_text, "PHOTO_11");
                }
            }
        });

        photo12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              //  showConfirmationGallery(DatabaseHelper.KEY_PHOTO12, "PHOTO_12");
                if (photo12_text == null || photo12_text.isEmpty()) {
                    showConfirmationGallery(DatabaseHelper.KEY_PHOTO12, "PHOTO_12");
                } else {
                    showConfirmationAlert(DatabaseHelper.KEY_PHOTO12, photo12_text, "PHOTO_12");

                }

            }
        });

        photo13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               // showConfirmationGallery(DatabaseHelper.KEY_PHOTO13, "PHOTO_13");
                if (photo13_text == null || photo13_text.isEmpty()) {
                    showConfirmationGallery(DatabaseHelper.KEY_PHOTO13, "PHOTO_13");
                } else {
                    showConfirmationAlert(DatabaseHelper.KEY_PHOTO13, photo13_text, "PHOTO_13");

                }

            }
        });

    }

    private void initAmtValidation() {

        if(mListSize == 2)
        {
            mAMT1 =edtAmountId1.getText().toString().trim();
            mAMT2 =edtAmountId2.getText().toString().trim();

            if(!mAMT1.equalsIgnoreCase(""))
            {
                if(!mAMT2.equalsIgnoreCase(""))
                {
                    new saveApprovbPhotoDataAPI().execute();
                }
                else
                {
                    Toast.makeText(mContext, "Please enter amount2", Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                Toast.makeText(mContext, "Please enter amount1", Toast.LENGTH_SHORT).show();
            }

            if(mAMT2.equalsIgnoreCase(null))
                mAMT2 = "0";

            mAMT3 = "0";
        }
        else  if(mListSize == 3)
        {
            mAMT1 =edtAmountId1.getText().toString().trim();
            mAMT2 =edtAmountId2.getText().toString().trim();
            mAMT3 =edtAmountId3.getText().toString().trim();

            if(!mAMT1.equalsIgnoreCase(""))
            {
                if(!mAMT2.equalsIgnoreCase(""))
                {
                    if(!mAMT3.equalsIgnoreCase(""))
                    {
                        new saveApprovbPhotoDataAPI().execute();
                    }else
                    {
                        Toast.makeText(mContext, "Please enter amount3", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(mContext, "Please enter amount2", Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                Toast.makeText(mContext, "Please enter amount1", Toast.LENGTH_SHORT).show();
            }
            if(mAMT3.equalsIgnoreCase(null))
                mAMT3 = "0";

        }
        else
        {
            mAMT1 =edtAmountId1.getText().toString().trim();


            if(!mAMT1.equalsIgnoreCase(""))
            {
                new saveApprovbPhotoDataAPI().execute();
            }
            else
            {
                Toast.makeText(mContext, "Please enter amount1", Toast.LENGTH_SHORT).show();
            }

        }

        mAMT3 ="0";
        mAMT2 = "0";
    }


    public void openCamera(String name) {

        if (CameraUtils.checkPermissions(mContext)) {

           /* Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            String from = "INST/";

            File file = CameraUtils.getOutputMediaFile(MEDIA_TYPE_IMAGE, enq_docno, name, from);

            if (file != null) {
                imageStoragePath = file.getAbsolutePath();
                Log.e("PATH", "&&&" + imageStoragePath);
            }

            fileUri1 = CameraUtils.getOutputMediaFileUri(mContext, file);

            Log.e("fileUri", "&&&" + fileUri1);

            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri1);

            // start the image capture Intent
            startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
*/

           file = new File(ImageManager.getMediaFilePath(type,name, enq_docno));

            imageStoragePath = file.getAbsolutePath();
            Log.e("PATH", "&&&" + imageStoragePath);

            Intent i = new Intent(mContext, CameraActivity.class);
            i.putExtra("lat", String.valueOf(inst_latitude_double));
            i.putExtra("lng", String.valueOf(inst_longitude_double));
           // i.putExtra("cust_name", cust_name);
            i.putExtra("cust_name", cust_name);
            i.putExtra("inst_id", enq_docno);
            i.putExtra("type", "SERVICE/");
            i.putExtra("name", name);

            startActivityForResult(i, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
        }


    }

    @AfterPermissionGranted(RC_FILE_PICKER_PERM)
    public void pickDocClicked() {
        if (EasyPermissions.hasPermissions(mContext, CustomUtility.PERMISSIONS_FILE_PICKER)) {
            onPickDoc();
        } else {
            // Ask for one permission
            EasyPermissions.requestPermissions(this, getString(R.string.rationale_doc_picker),
                    RC_FILE_PICKER_PERM, CustomUtility.PERMISSIONS_FILE_PICKER);
        }
    }

    private void addThemToView(ArrayList<String> imagePaths, ArrayList<String> docPaths) {
        ArrayList<String> filePaths = new ArrayList<>();
        if (imagePaths != null) filePaths.addAll(imagePaths);

        if (docPaths != null) filePaths.addAll(docPaths);


        if (recyclerView != null) {
            StaggeredGridLayoutManager layoutManager =
                    new StaggeredGridLayoutManager(3, OrientationHelper.VERTICAL);
            layoutManager.setGapStrategy(
                    StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
            recyclerView.setLayoutManager(layoutManager);

            ImageAdapter imageAdapter = new ImageAdapter(mContext, filePaths);

            recyclerView.setAdapter(imageAdapter);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
        }

        // Toast.makeText(mActivity, "Num of files selected: " + filePaths.size(), Toast.LENGTH_SHORT).show();
    }

    public void onPickDoc() {

        File file = new File(Environment.getExternalStorageDirectory(),
                "/");

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setDataAndType(Uri.fromFile(file), "application/pdf");
        //intent.setType("application/pdf");
        startActivityForResult(intent, 1);
    }

    public void openGallery(String name) {

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

    }

    private void setData() {


        File file = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME + "/SKAPP/SERVICE/" + enq_docno, "/IMG_PHOTO_1.jpg");
        if (file.exists()) {
            photo1_text = file.getAbsolutePath();
        }

        File file1 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME + "/SKAPP/SERVICE/" + enq_docno, "/IMG_PHOTO_2.jpg");
        if (file1.exists()) {
            photo2_text = file1.getAbsolutePath();
        }

        File file2 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME + "/SKAPP/SERVICE/" + enq_docno, "/IMG_PHOTO_3.jpg");
        if (file2.exists()) {
            photo3_text = file2.getAbsolutePath();
        }

        File file3 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME + "/SKAPP/SERVICE/" + enq_docno, "/IMG_PHOTO_4.jpg");
        if (file3.exists()) {
            photo4_text = file3.getAbsolutePath();
        }

        File file4 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME + "/SKAPP/SERVICE/" + enq_docno, "/IMG_PHOTO_5.jpg");
        if (file4.exists()) {
            photo5_text = file4.getAbsolutePath();
        }

        File file5 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME + "/SKAPP/SERVICE/" + enq_docno, "/IMG_PHOTO_6.jpg");
        if (file5.exists()) {
            photo6_text = file5.getAbsolutePath();
        }

        File file6 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME + "/SKAPP/SERVICE/" + enq_docno, "/IMG_PHOTO_7.jpg");
        if (file6.exists()) {
            photo7_text = file6.getAbsolutePath();
        }

        File file7 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME + "/SKAPP/SERVICE/" + enq_docno, "/IMG_PHOTO_8.jpg");
        if (file7.exists()) {
            photo8_text = file7.getAbsolutePath();
        }

        File file8 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME + "/SKAPP/SERVICE/" + enq_docno, "/IMG_PHOTO_9.jpg");
        if (file8.exists()) {
            photo9_text = file8.getAbsolutePath();
        }

        File file9 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME + "/SKAPP/SERVICE/" + enq_docno, "/IMG_PHOTO_10.jpg");
        if (file9.exists()) {
            photo10_text = file9.getAbsolutePath();
        }

        File file10 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME + "/SKAPP/SERVICE/" + enq_docno, "/IMG_PHOTO_11.jpg");
        if (file10.exists()) {
            photo11_text = file10.getAbsolutePath();
        }

        File file11 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME + "/SKAPP/SERVICE/" + enq_docno, "/IMG_PHOTO_12.jpg");
        if (file11.exists()) {
            photo12_text = file11.getAbsolutePath();
        }
        File file12 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME + "/SKAPP/SERVICE/" + enq_docno, "/IMG_PHOTO_13.jpg");
        if (file12.exists()) {
            photo13_text = file12.getAbsolutePath();
        }



/*        File file10 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME+ "/SKAPP/SERVICE/" + enq_docno, "/IMG_PHOTO_11.jpg");
        if (file10.exists()) {
            photo11_text = file10.getAbsolutePath();
        }

        File file11 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME + "/SKAPP/SERVICE/" + enq_docno, "/IMG_PHOTO_12.jpg");
        if (file11.exists()) {
            photo12_text = file11.getAbsolutePath();
        }*/


    /*    setIcon(DatabaseHelper.KEY_PHOTO1);
        setIcon(DatabaseHelper.KEY_PHOTO2);
        setIcon(DatabaseHelper.KEY_PHOTO3);
        setIcon(DatabaseHelper.KEY_PHOTO4);
        setIcon(DatabaseHelper.KEY_PHOTO5);
        setIcon(DatabaseHelper.KEY_PHOTO6);

        setIcon(DatabaseHelper.KEY_PHOTO7);
        setIcon(DatabaseHelper.KEY_PHOTO8);
        setIcon(DatabaseHelper.KEY_PHOTO9);*/
        setIcon(DatabaseHelper.KEY_PHOTO10);
        setIcon(DatabaseHelper.KEY_PHOTO11);
        setIcon(DatabaseHelper.KEY_PHOTO12);
        setIcon(DatabaseHelper.KEY_PHOTO13);


    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("Range")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {

                try {

                    Log.e("Count", "&&&&&" + imageStoragePath);

                   Bitmap bitmap = CameraUtils.optimizeBitmap(BITMAP_SAMPLE_SIZE, imageStoragePath);

                    int count = bitmap.getByteCount();

                    Log.e("Count", "&&&&&" + count);

                    Log.e("IMAGEURI", "&&&&" + imageStoragePath);

                    ByteArrayOutputStream byteArrayBitmapStream = new ByteArrayOutputStream();

                    bitmap.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayBitmapStream);

                    byte[] byteArray = byteArrayBitmapStream.toByteArray();

                    long size = byteArray.length;

                    Log.e("SIZE1234", "&&&&" + size);

                    Log.e("SIZE1234", "&&&&" + Arrays.toString(byteArray));



                    if (photo10_flag == true) {
                        File file9 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME + "/SKAPP/SERVICE/" + enq_docno, "/IMG_PHOTO_10.jpg");
                        if (file9.exists()) {
                            photo10_text = Base64.encodeToString(byteArray, Base64.DEFAULT);
                            CustomUtility.setSharedPreference(mContext, enq_docno + "PHOTO_10", photo10_text);
                            Log.e("SIZE10", "&&&&" + photo10_text);
                            setIcon(DatabaseHelper.KEY_PHOTO10);
                        }
                    }

                    if (photo11_flag == true) {
                        File file10 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME + "/SKAPP/SERVICE/" + enq_docno, "/IMG_PHOTO_11.jpg");
                        if (file10.exists()) {
                            photo11_text = Base64.encodeToString(byteArray, Base64.DEFAULT);
                            CustomUtility.setSharedPreference(mContext, enq_docno + "PHOTO_11", photo11_text);
                            Log.e("SIZE11", "&&&&" + photo11_text);
                            setIcon(DatabaseHelper.KEY_PHOTO11);
                        }
                    }

                    if (photo12_flag == true) {
                        File file11 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME + "/SKAPP/SERVICE/" + enq_docno, "/IMG_PHOTO_12.jpg");
                        if (file11.exists()) {
                            photo12_text = Base64.encodeToString(byteArray, Base64.DEFAULT);
                            CustomUtility.setSharedPreference(mContext, enq_docno + "PHOTO_12", photo12_text);
                            Log.e("SIZE11", "&&&&" + photo12_text);
                            setIcon(DatabaseHelper.KEY_PHOTO12);
                        }
                    }
                    if (photo13_flag == true) {
                        File file12 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME + "/SKAPP/SERVICE/" + enq_docno, "/IMG_PHOTO_13.jpg");
                        if (file12.exists()) {
                            photo13_text = Base64.encodeToString(byteArray, Base64.DEFAULT);
                            CustomUtility.setSharedPreference(mContext, enq_docno + "PHOTO_13", photo13_text);
                            Log.e("SIZE11", "&&&&" + photo13_text);
                            setIcon(DatabaseHelper.KEY_PHOTO13);
                        }
                    }


                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

             /*   File file = newworkorder File(imageStoragePath);
                if (file.exists()) {
                    file.delete();
                }*/

        }
        else {
            if (requestCode == GALLERY_IMAGE_REQUEST_CODE) {

                if (resultCode == RESULT_OK) {

                    if (data != null) {


                        Uri selectedImageUri = data.getData();

                        String selectedImagePath = getImagePath(selectedImageUri);

                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inJustDecodeBounds = true;
                        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                        options.inSampleSize = 2;
                        options.inJustDecodeBounds = false;

                        try {
                            Log.e("IMAGEURI", "&&&&" + selectedImageUri);
                            if (selectedImageUri != null) {

                                Bitmap bitmap  = BitmapFactory.decodeFile(selectedImagePath, options);

                                //Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImageUri));


                                int count = bitmap.getByteCount();

                                Log.e("Count", "&&&&&" + count);
                                ByteArrayOutputStream byteArrayBitmapStream = new ByteArrayOutputStream();

                                bitmap.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayBitmapStream);

                                byte[] byteArray = byteArrayBitmapStream.toByteArray();

                                long size = byteArray.length;


                                Log.e("SIZE1234", "&&&&" + size);

                                Log.e("SIZE1234", "&&&&" + Arrays.toString(byteArray));



                                if (photo10_flag == true) {
                                    String destFile = getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME + "/SKAPP/SERVICE/" + enq_docno + "/IMG_PHOTO_10.jpg";
                                    copyFile(selectedImagePath, destFile);
                                    File file9 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME + "/SKAPP/SERVICE/" + enq_docno, "/IMG_PHOTO_10.jpg");
                                    if (file9.exists()) {
                                        photo10_text = Base64.encodeToString(byteArray, Base64.DEFAULT);
                                        CustomUtility.setSharedPreference(mContext, enq_docno + "PHOTO_10", photo10_text);
                                        Log.e("SIZE10", "&&&&" + photo10_text);
                                        setIcon(DatabaseHelper.KEY_PHOTO10);
                                    }
                                }

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

            }
        }
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                // Get the Uri of the selected file
                Uri uri = data.getData();
                String uriString = null;
                if (uri != null) {
                    uriString = uri.toString();
                }
                File myFile = new File(uriString);
                //PathHolder = myFile.getPath();
                Filename = null;


                if (uriString.startsWith("content://")) {
                    Cursor cursor = null;
                    try {
                        cursor = getContentResolver().query(uri, null, null, null, null);
                        if (cursor != null && cursor.moveToFirst()) {

                            Filename = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                            PathHolder = getExternalStorageDirectory() + "/Download/" + Filename;
                            Log.e("&&&&", "DDDDD" + Filename);

                            if (PathHolder != null && !PathHolder.equals("")) {

                            } else {

                            }
                        }
                    } finally {
                        if (cursor != null) {
                            cursor.close();
                        }
                    }
                } else if (uriString.startsWith("file://")) {
                    Filename = myFile.getName();
                    PathHolder = getExternalStorageDirectory() + "/Download/" + Filename;
                    Log.e("&&&&", "DDDDD" + Filename);
                    if (PathHolder != null && !PathHolder.equals("")) {

                    } else {

                    }
                }
            }
        }

        addThemToView(photoPaths, docPaths);
        if (resultCode == Activity.RESULT_OK) {
            super.onActivityResult(requestCode, resultCode, data);


            if (requestCode == 301) {

                String year = data.getStringExtra("year");
                String month = data.getStringExtra("month");
                String date = data.getStringExtra("date");
                String finaldate = year + "-" + month + "-" + date;
                finaldate = CustomUtility.formateDate(finaldate);
            }
        }
    }


    private void copyFile(String sourceFilePath, String destinationFilePath) {

        Log.e("Source", "&&&&" + sourceFilePath);
        Log.e("Destination", "&&&&" + destinationFilePath);

        File sourceLocation = new File(sourceFilePath);
        File targetLocation = new File(destinationFilePath);
        Log.e("&&&&&", "sourceLocation: " + sourceLocation);
        Log.e("&&&&&", "targetLocation: " + targetLocation);
        try {
            int actionChoice = 2;
            if (actionChoice == 1) {
                if (sourceLocation.renameTo(targetLocation)) {
                    Log.e("&&&&&", "Move file successful.");
                } else {
                    Log.e("&&&&&", "Move file failed.");
                }
            } else {
                if (sourceLocation.exists()) {
                    InputStream in = new FileInputStream(sourceLocation);
                    OutputStream out = new FileOutputStream(targetLocation);
                    byte[] buf = new byte[1024];
                    int len;
                    while ((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                    in.close();
                    out.close();
                    Log.e("&&&&&", "Copy file successful.");
                } else {
                    Log.e("&&&&&", "Copy file failed. Source file missing.");
                }
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void showConfirmationAlert(final String keyimage, final String data, final String name) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext,R.style.MyDialogTheme);
        // Setting Dialog Title
        alertDialog.setTitle("Confirmation");
        // Setting Dialog Message
        alertDialog.setMessage("Image already saved, Do you want to change it or display?");
        // On pressing Settings button
        alertDialog.setPositiveButton("Display", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {

                Log.e("KEY", "&&&&" + keyimage);
                Log.e("DATA", "&&&&" + data);

                String  dataTest = activity.CustomUtility.getSharedPreferences(mContext, enq_docno + name);

                displayImage(keyimage, dataTest);


            }
        });

        alertDialog.setNegativeButton("Change", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                showConfirmationGallery(keyimage, name);
               // openCamera(name);
               // setFlag(keyimage);

            }
        });

        // Showing Alert Message
        alertDialog.show();
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

            Cursor cursor1 = ((Activity) mContext).getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, MediaStore.Images.Media._ID + " = ? ", new String[]{image_id}, null);
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

    private void displayImage(String key, String data) {
       // Intent i_display_image = new Intent(mContext, ShowDocument.class);
        Intent i_display_image = new Intent(mContext, ShowPhotograph.class);
      /*  Bundle extras = new Bundle();
        //saveData();
        extras.putString("docno", enq_docno);
        extras.putString("key", key);
        extras.putString("data", "INST");

        CustomUtility.setSharedPreference(mContext, "data", data);*/

        WebURL.mViewPhotoGraph = data;

      //  i_display_image.putExtras(extras);
        startActivity(i_display_image);
    }

    public void showConfirmationGallery(final String keyimage, final String name) {

        final CustomUtility customUtility = new CustomUtility();

      //  final CharSequence[] items = {"Take Photo", "Choose from Gallery", "Cancel"};
        final CharSequence[] items = {"Take Photo", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext,R.style.MyDialogTheme);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = customUtility.checkPermission(mContext);
                if (items[item].equals("Take Photo")) {

                    if (result) {
                        openCamera(name);
                        setFlag(keyimage);
                    }

                }/* else if (items[item].equals("Choose from Gallery")) {
                    if (result) {
                        openGallery(name);
                        setFlag(keyimage);


                    }

                }*/ else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    public void Save() {


        String pht1 = null, pht2 = null, pht3 = null, pht4 = null, pht5 = null, pht6 = null, pht7 = null, pht8 = null, pht9 = null, pht10 = null, pht11 = null, pht12 = null;


        File file = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME  + "/SKAPP/SERVICE/" + enq_docno, "/IMG_PHOTO_1.jpg");
        if (file.exists()) {
            pht1 = file.getAbsolutePath();
        }

        File file1 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME  + "/SKAPP/SERVICE/" + enq_docno, "/IMG_PHOTO_2.jpg");
        if (file1.exists()) {
            pht2 = file1.getAbsolutePath();
        }

        File file2 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME  + "/SKAPP/SERVICE/" + enq_docno, "/IMG_PHOTO_3.jpg");
        if (file2.exists()) {
            pht3 = file2.getAbsolutePath();
        }

        File file3 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME  + "/SKAPP/SERVICE/" + enq_docno, "/IMG_PHOTO_4.jpg");
        if (file3.exists()) {
            pht4 = file3.getAbsolutePath();
        }

        File file4 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME  + "/SKAPP/SERVICE/" + enq_docno, "/IMG_PHOTO_5.jpg");
        if (file4.exists()) {
            pht5 = file4.getAbsolutePath();
        }

        File file5 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME  + "/SKAPP/SERVICE/" + enq_docno, "/IMG_PHOTO_6.jpg");
        if (file5.exists()) {
            pht6 = file5.getAbsolutePath();
        }

        File file6 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME  + "/SKAPP/SERVICE/" + enq_docno, "/IMG_PHOTO_7.jpg");
        if (file6.exists()) {
            pht7 = file6.getAbsolutePath();
        }

        File file7 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME  + "/SKAPP/SERVICE/" + enq_docno, "/IMG_PHOTO_8.jpg");
        if (file7.exists()) {
            pht8 = file7.getAbsolutePath();
        }

        File file8 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME  + "/SKAPP/SERVICE/" + enq_docno, "/IMG_PHOTO_9.jpg");
        if (file8.exists()) {
            pht9 = file8.getAbsolutePath();
        }

        File file9 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME  + "/SKAPP/SERVICE/" + enq_docno, "/IMG_PHOTO_10.jpg");
        if (file9.exists()) {
            pht10 = file9.getAbsolutePath();
        }

        File file10 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME  + "/SKAPP/SERVICE/" + enq_docno, "/IMG_PHOTO_11.jpg");
        if (file10.exists()) {
            pht11 = file10.getAbsolutePath();
        }

        File file11 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME  + "/SKAPP/SERVICE/" + enq_docno, "/IMG_PHOTO_12.jpg");
        if (file11.exists()) {
            pht12 = file11.getAbsolutePath();
        }


            if (!TextUtils.isEmpty(pht1)) {

            } else {

                Toast.makeText(this, "Please Select Bill Photo Photo.", Toast.LENGTH_SHORT).show();
            }


    }


    public void setFlag(String key) {

        Log.e("FLAG", "&&&" + key);

        photo10_flag = false;
        photo11_flag = false;
        photo12_flag = false;
        photo13_flag = false;


        switch (key) {
            case DatabaseHelper.KEY_PHOTO10:
                photo10_flag = true;
                break;
                case DatabaseHelper.KEY_PHOTO11:
                photo11_flag = true;
                break;
                case DatabaseHelper.KEY_PHOTO12:
                photo12_flag = true;
                break;
                case DatabaseHelper.KEY_PHOTO13:
                photo13_flag = true;
                break;


        }

    }

    public void setIcon(String key) {


        switch (key) {

               case DatabaseHelper.KEY_PHOTO10:
                if (photo10_text == null || photo10_text.isEmpty()) {
                    photo10.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.red_icn, 0);
                } else {
                    photo10.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.right_mark_icn_green, 0);
                }
                break;

                case DatabaseHelper.KEY_PHOTO11:
                if (photo11_text == null || photo11_text.isEmpty()) {
                    photo11.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.red_icn, 0);
                } else {
                    photo11.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.right_mark_icn_green, 0);
                }
                break;
                case DatabaseHelper.KEY_PHOTO12:
                if (photo12_text == null || photo12_text.isEmpty()) {
                    photo12.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.red_icn, 0);
                } else {
                    photo12.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.right_mark_icn_green, 0);
                }
                break;
                case DatabaseHelper.KEY_PHOTO13:
                if (photo13_text == null || photo13_text.isEmpty()) {
                    photo13.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.red_icn, 0);
                } else {
                    photo13.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.right_mark_icn_green, 0);
                }
                break;



        }
    }

    @Override
    public void onBackPressed() {
        Save();
        super.onBackPressed();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    public void getGpsLocation() {
        GPSTracker gps = new GPSTracker(mContext);

        if (gps.canGetLocation()) {
            inst_latitude_double = gps.getLatitude();
            inst_longitude_double = gps.getLongitude();
          /*  if (inst_latitude_double == 0.0) {
                CustomUtility.ShowToast("Lat Long not captured, Please try again.", mContext);
            } else {
                //CustomUtility.ShowToast("Latitude:-" + inst_latitude_double + "     " + "Longitude:-" + inst_longitude_double, mContext);
            }*/
        } else {
            gps.showSettingsAlert();
        }
    }


    private class saveApprovbPhotoDataAPI extends AsyncTask<String, String, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {

            progressDialog = new ProgressDialog(mContext);
            progressDialog = ProgressDialog.show(mContext, "", "Sending Data to server..please wait !");

        }

        @Override
        protected String doInBackground(String... params) {
            String docno_sap = null;
            String invc_done = null;
            String obj2 = null;



            JSONArray ja_invc_data = new JSONArray();

            JSONObject jsonObj = new JSONObject();

            try {
                String mALLAmount = "";

                mALLAmount = item1+"RAM"+mAMT1+"SITA"+item2+"RAM"+mAMT2+"SITA"+item3+"RAM"+mAMT3;

                System.out.println("mALLAmount==>>"+mALLAmount);

/*if(mListSize == 1)
{
    mALLAmount = "item1RAM"+mAMT1;
}
else if(mListSize == 2)
{
    mALLAmount = "item1RAM"+mAMT1+"SITAitem2RAM"+mAMT2;
}
else if(mListSize == 3)
{

   // mALLAmount = "item1RAM"+mAMT1+"SITAitem2RAM"+mAMT2+"SITAitem3RAM"+mAMT3;
    mALLAmount = item1+"RAM"+mAMT1+"SITA"+item2+"RAM"+mAMT2+"SITA"+item3+"RAM"+mAMT3;
}*/

                jsonObj.put("CMPNO", enq_docno);
                jsonObj.put("kunnr", mUserID);
                jsonObj.put("sc_status", mStatusCheck);
                jsonObj.put("ACTION", mRMK);
                jsonObj.put("amount", mALLAmount);

                System.out.println("only_text_jsonObj==>>"+jsonObj);


                jsonObj.put("PHOTO1", activity.CustomUtility.getSharedPreferences(mContext, enq_docno + "PHOTO_10"));
                jsonObj.put("PHOTO2", activity.CustomUtility.getSharedPreferences(mContext, enq_docno + "PHOTO_11"));
                jsonObj.put("PHOTO3", activity.CustomUtility.getSharedPreferences(mContext, enq_docno + "PHOTO_12"));
                jsonObj.put("PHOTO4", activity.CustomUtility.getSharedPreferences(mContext, enq_docno + "PHOTO_13"));
               /* jsonObj.put("PHOTO10", activity.CustomUtility.getSharedPreferences(mContext, billno + "PHOTO_10"));
                jsonObj.put("PHOTO11", activity.CustomUtility.getSharedPreferences(mContext, billno + "PHOTO_11"));
                jsonObj.put("PHOTO12", activity.CustomUtility.getSharedPreferences(mContext, billno + "PHOTO_12"));
*/
                ja_invc_data.put(jsonObj);

            } catch (Exception e) {
                e.printStackTrace();
            }

            final ArrayList<NameValuePair> param1_invc = new ArrayList<NameValuePair>();
            //  param1_invc.add(new BasicNameValuePair("installation", String.valueOf(ja_invc_data)));
            param1_invc.add(new BasicNameValuePair("approved_save", String.valueOf(ja_invc_data)));
            Log.e("DATA", "$$$$" + param1_invc.toString());

            System.out.println("param1_invc_vihu==>>"+param1_invc.toString());
            try {

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
                StrictMode.setThreadPolicy(policy);

                obj2 = CustomHttpClient.executeHttpPost1(WebURL.PENDING_COMPLAIN_APPROVED_PHOTO_VK_PAGE, param1_invc);

                Log.e("OUTPUT1", "&&&&" + obj2);

                if (obj2 != "") {

                    JSONObject object = new JSONObject(obj2);
                    String mStatus = object.getString("status");
                    final String mMessage = object.getString("message");
                    String jo11 = object.getString("response");
                    System.out.println("jo11==>>"+jo11);
                    if (mStatus.equalsIgnoreCase("true")) {
                        Message msg = new Message();
                        msg.obj = "Data Submitted Successfully...";
                        mHandler2.sendMessage(msg);

                        deleteDirectory(new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/" + GALLERY_DIRECTORY_NAME + "/SKAPP/SERVICE/" + enq_docno));

                        activity.CustomUtility.setSharedPreference(mContext, "INSTSYNC"+enq_docno, "");


                        activity.CustomUtility.setSharedPreference(mContext, enq_docno + "PHOTO_10", "");
                        activity.CustomUtility.setSharedPreference(mContext, "SYNCLIST", "1");

                        progressDialog.dismiss();

                        Intent intent = new Intent(ApproveReportImageActivity.this, MainActivity1.class);
                        startActivity(intent);
                        finish();


                    }
                    else
                    {

                        Message msg = new Message();
                        msg.obj = "Data Not Submitted, Please try After Sometime.";
                        mHandler2.sendMessage(msg);

                        progressDialog.dismiss();
                        //  finish();
                    }

                }

            } catch (Exception e) {
                e.printStackTrace();
                progressDialog.dismiss();
            }

            return obj2;
        }

        @Override
        protected void onPostExecute(String result) {

            // write display tracks logic here
            onResume();
            progressDialog.dismiss();  // dismiss dialog


        }
    }


    private boolean deleteDirectory(File path) {
        if( path.exists() ) {
            File[] files = path.listFiles();
            if (files == null) {
                return false;
            }
            for(File file : files) {
                if(file.isDirectory()) {
                    deleteDirectory(file);
                }
                else {
                    file.delete();
                }
            }
        }
        return path.exists() && path.delete();
    }


    android.os.Handler mHandler2 = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            String mString = (String) msg.obj;
            Toast.makeText(mContext, mString, Toast.LENGTH_LONG).show();


        }
    };


}
