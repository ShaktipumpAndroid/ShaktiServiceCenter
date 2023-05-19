package activity.complainvk;

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
import androidx.appcompat.app.AppCompatActivity;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import activity.AdaperVk.ImageAdapter;
import activity.GPSTracker;
import activity.ShowDocument;
import activity.utility.CameraUtils;
import activity.utility.CustomUtility;

import database.DatabaseHelper;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import webservice.CustomHttpClient;
import webservice.WebURL;


import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.os.Environment.getExternalStorageDirectory;
import static android.os.Environment.getExternalStoragePublicDirectory;


public class InstReportImageActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {
    public static final int RC_FILE_PICKER_PERM = 321;
    public static final int BITMAP_SAMPLE_SIZE = 4 ;
    public static final int MEDIA_TYPE_IMAGE = 1;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int GALLERY_IMAGE_REQUEST_CODE = 101;
    Context mContext;
    File file;
    DatabaseHelper dataHelper;
    double inst_latitude_double,
            inst_longitude_double;
   // String sync_data = "0",lat,lng,type="INST/";
    String sync_data = "0",lat,lng,type="SERVICE/";
    int PERMISSION_ALL = 1;
    Uri fileUri1;
    public static final String GALLERY_DIRECTORY_NAME = "ShaktiServiceCenter";
    String[] PERMISSIONS = {
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
    };

    String mUserID;
    TextView txtBTNSaveID;
    EditText edtRemarkAMTID;
    String imageStoragePath, enq_docno, status, photo1_text, photo2_text, photo3_text, photo4_text, photo5_text, photo6_text, photo7_text, photo8_text, photo9_text, photo10_text, photo11_text, photo12_text;
    TextView photo1, photo2, photo3, photo4, photo5, photo6, photo7, photo8, photo9, photo10, photo11, photo12;
    boolean photo1_flag = false,
            photo2_flag = false,
            photo3_flag = false,
            photo4_flag = false,
            photo5_flag = false,
            photo6_flag = false,
            photo7_flag = false,
            photo8_flag = false,
            photo9_flag = false,
            photo10_flag = false,
            photo11_flag = false,
            photo12_flag = false;

    String pendRemarkValue;
    private String mHomePath, PathHolder, Filename,cust_name,mStatusCheck;
    private ArrayList<String> photoPaths = new ArrayList<>();
    private ArrayList<String> docPaths = new ArrayList<>();
    private RecyclerView recyclerView;

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
        setContentView(R.layout.activity_instreport_image);
        mContext = this;

        mUserID = activity.CustomUtility.getSharedPreferences(mContext,"userID");
        dataHelper = new DatabaseHelper(mContext);

        getGpsLocation();

      //  CustomUtility.setSharedPreference(mContext, "INSTSYNC" + enq_docno, "");

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {

                mStatusCheck= null;
                cust_name= null;
                enq_docno= null;

            } else {

                //cust_name= extras.getString("StatusCheck");
                mStatusCheck= extras.getString("StatusCheck");
                cust_name= extras.getString("cust_name");
                enq_docno= extras.getString("inst_id");
                
            }
        } else {
            mStatusCheck= (String) savedInstanceState.getSerializable("StatusCheck");
            cust_name= (String) savedInstanceState.getSerializable("cust_name");
            enq_docno= (String) savedInstanceState.getSerializable("inst_id");
            
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


        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        txtBTNSaveID = (TextView) findViewById(R.id.txtBTNSaveID);
        photo1 = (TextView) findViewById(R.id.photo1);
        photo2 = (TextView) findViewById(R.id.photo2);
        photo3 = (TextView) findViewById(R.id.photo3);
        photo4 = (TextView) findViewById(R.id.photo4);
        photo5 = (TextView) findViewById(R.id.photo5);
        photo6 = (TextView) findViewById(R.id.photo6);

        photo7 = (TextView) findViewById(R.id.photo7);
        photo8 = (TextView) findViewById(R.id.photo8);
        photo9 = (TextView) findViewById(R.id.photo9);
        photo10 = (TextView) findViewById(R.id.photo10);
        photo11 = (TextView) findViewById(R.id.photo11);
        photo12 = (TextView) findViewById(R.id.photo12);
        edtRemarkAMTID =  findViewById(R.id.edtRemarkAMTID);



        recyclerView = (RecyclerView) findViewById(R.id.file_list);

        txtBTNSaveID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                pendRemarkValue = edtRemarkAMTID.getText().toString().trim();

              String  ph11 = activity.CustomUtility.getSharedPreferences(mContext, enq_docno + "PHOTO_1");
                String  ph22 = activity.CustomUtility.getSharedPreferences(mContext, enq_docno + "PHOTO_2");
                String ph33 = activity.CustomUtility.getSharedPreferences(mContext, enq_docno + "PHOTO_3");

                if(ph11 !=null && !ph11.equalsIgnoreCase(""))
                {
                    if(ph22 !=null && !ph22.equalsIgnoreCase(""))
                    {
                        if(ph33 !=null && !ph33.equalsIgnoreCase(""))
                        {
                            if(pendRemarkValue != null  && !pendRemarkValue.equalsIgnoreCase("")) {
                                new savePendingPhotoDataAPI().execute();
                            }
                            else
                            {
                                Toast.makeText(mContext, "Please enter remark. ", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            Toast.makeText(mContext, "Please take Cust./Deale/JCC Bill Photo ", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(mContext, "Please take Product Full Images Photo ", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(mContext, "Please take Falty Material/Mager meges Photo ", Toast.LENGTH_SHORT).show();
                }
            }
        });

        setData();

        photo1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //pickDocClicked();
              //  showConfirmationGallery(DatabaseHelper.KEY_PHOTO1, "PHOTO_1");
                if (photo1_text == null || photo1_text.isEmpty()) {

                    showConfirmationGallery(DatabaseHelper.KEY_PHOTO1, "PHOTO_1");
                } else {

                    showConfirmationAlert(DatabaseHelper.KEY_PHOTO1, photo1_text, "PHOTO_1");
                }
            }
        });

        photo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

             //   showConfirmationGallery(DatabaseHelper.KEY_PHOTO2, "PHOTO_2");
                if (photo2_text == null || photo2_text.isEmpty()) {
                    showConfirmationGallery(DatabaseHelper.KEY_PHOTO2, "PHOTO_2");
                } else {

                    showConfirmationAlert(DatabaseHelper.KEY_PHOTO2, photo2_text, "PHOTO_2");
                }
            }
        });

        photo3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               // showConfirmationGallery(DatabaseHelper.KEY_PHOTO3, "PHOTO_3");
                if (photo3_text == null || photo3_text.isEmpty()) {
                    showConfirmationGallery(DatabaseHelper.KEY_PHOTO3, "PHOTO_3");
                } else {

                    showConfirmationAlert(DatabaseHelper.KEY_PHOTO3, photo3_text, "PHOTO_3");

                }

            }
        });

        photo4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //showConfirmationGallery(DatabaseHelper.KEY_PHOTO4, "PHOTO_4");
                if (photo4_text == null || photo4_text.isEmpty()) {
                    showConfirmationGallery(DatabaseHelper.KEY_PHOTO4, "PHOTO_4");
                } else {

                    showConfirmationAlert(DatabaseHelper.KEY_PHOTO4, photo4_text, "PHOTO_4");

                }

            }
        });

        photo5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // showConfirmationGallery(DatabaseHelper.KEY_PHOTO5, "PHOTO_5");

                if (photo5_text == null || photo5_text.isEmpty()) {
                    showConfirmationGallery(DatabaseHelper.KEY_PHOTO5, "PHOTO_5");
                } else {
                    showConfirmationAlert(DatabaseHelper.KEY_PHOTO5, photo5_text, "PHOTO_5");


                }

            }
        });

        photo6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               // showConfirmationGallery(DatabaseHelper.KEY_PHOTO6, "PHOTO_6");
                if (photo6_text == null || photo6_text.isEmpty()) {
                    showConfirmationGallery(DatabaseHelper.KEY_PHOTO6, "PHOTO_6");
                } else {
                    showConfirmationAlert(DatabaseHelper.KEY_PHOTO6, photo6_text, "PHOTO_6");

                }

            }
        });

        photo7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

             //   showConfirmationGallery(DatabaseHelper.KEY_PHOTO7, "PHOTO_7");
                if (photo7_text == null || photo7_text.isEmpty()) {
                    showConfirmationGallery(DatabaseHelper.KEY_PHOTO7, "PHOTO_7");
                } else {

                    showConfirmationAlert(DatabaseHelper.KEY_PHOTO7, photo7_text, "PHOTO_7");

                }
            }
        });

        photo8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // showConfirmationGallery(DatabaseHelper.KEY_PHOTO8, "PHOTO_8");
                if (photo8_text == null || photo8_text.isEmpty()) {
                    showConfirmationGallery(DatabaseHelper.KEY_PHOTO8, "PHOTO_8");
                } else {

                    showConfirmationAlert(DatabaseHelper.KEY_PHOTO8, photo8_text, "PHOTO_8");

                }

            }
        });

        photo9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //showConfirmationGallery(DatabaseHelper.KEY_PHOTO9, "PHOTO_9");
                if (photo9_text == null || photo9_text.isEmpty()) {
                    showConfirmationGallery(DatabaseHelper.KEY_PHOTO9, "PHOTO_9");

                    openCamera("PHOTO_9");
                    setFlag(DatabaseHelper.KEY_PHOTO9);
                } else {
                    showConfirmationAlert(DatabaseHelper.KEY_PHOTO9, photo9_text, "PHOTO_9");

                }

            }
        });

       /* photo10.setOnClickListener(new View.OnClickListener() {
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


                if (photo12_text == null || photo12_text.isEmpty()) {
                    showConfirmationGallery(DatabaseHelper.KEY_PHOTO12, "PHOTO_12");
                } else {
                    showConfirmationAlert(DatabaseHelper.KEY_PHOTO12, photo12_text, "PHOTO_12");

                }

            }
        });
*/

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
          // i.putExtra("type", "INST/");
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

/*        File file10 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME+ "/SKAPP/SERVICE/" + enq_docno, "/IMG_PHOTO_11.jpg");
        if (file10.exists()) {
            photo11_text = file10.getAbsolutePath();
        }

        File file11 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME + "/SKAPP/SERVICE/" + enq_docno, "/IMG_PHOTO_12.jpg");
        if (file11.exists()) {
            photo12_text = file11.getAbsolutePath();
        }*/


        setIcon(DatabaseHelper.KEY_PHOTO1);
        setIcon(DatabaseHelper.KEY_PHOTO2);
        setIcon(DatabaseHelper.KEY_PHOTO3);
        setIcon(DatabaseHelper.KEY_PHOTO4);
        setIcon(DatabaseHelper.KEY_PHOTO5);
        setIcon(DatabaseHelper.KEY_PHOTO6);

        setIcon(DatabaseHelper.KEY_PHOTO7);
        setIcon(DatabaseHelper.KEY_PHOTO8);
        setIcon(DatabaseHelper.KEY_PHOTO9);
        setIcon(DatabaseHelper.KEY_PHOTO10);
        setIcon(DatabaseHelper.KEY_PHOTO11);
        setIcon(DatabaseHelper.KEY_PHOTO12);


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


                    if (photo1_flag == true) {
                        File file = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME + "/SKAPP/SERVICE/" + enq_docno, "/IMG_PHOTO_1.jpg");
                        if (file.exists())
                        {
                            photo1_text = Base64.encodeToString(byteArray, Base64.DEFAULT);
                            setIcon(DatabaseHelper.KEY_PHOTO1);
                            CustomUtility.setSharedPreference(mContext, enq_docno + "PHOTO_1", photo1_text);
                            Log.e("SIZE1", "&&&&" + CustomUtility.getSharedPreferences(mContext, enq_docno + "PHOTO_1"));

                        }

                    }

                    if (photo2_flag == true) {
                        File file1 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME + "/SKAPP/SERVICE/" + enq_docno, "/IMG_PHOTO_2.jpg");
                        if (file1.exists()) {
                            photo2_text = Base64.encodeToString(byteArray, Base64.DEFAULT);
                            setIcon(DatabaseHelper.KEY_PHOTO2);
                            CustomUtility.setSharedPreference(mContext, enq_docno + "PHOTO_2", photo2_text);
                            Log.e("SIZE2", "&&&&" + photo2_text);

                        }
                    }
                    if (photo3_flag == true) {
                        File file2 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME + "/SKAPP/SERVICE/" + enq_docno, "/IMG_PHOTO_3.jpg");
                        if (file2.exists()) {
                            photo3_text = Base64.encodeToString(byteArray, Base64.DEFAULT);
                            CustomUtility.setSharedPreference(mContext, enq_docno + "PHOTO_3", photo3_text);
                            Log.e("SIZE3", "&&&&" + photo3_text);
                            setIcon(DatabaseHelper.KEY_PHOTO3);
                        }
                    }

                    if (photo4_flag == true) {
                        File file3 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME + "/SKAPP/SERVICE/" + enq_docno, "/IMG_PHOTO_4.jpg");
                        if (file3.exists()) {
                            photo4_text = Base64.encodeToString(byteArray, Base64.DEFAULT);
                            CustomUtility.setSharedPreference(mContext, enq_docno + "PHOTO_4", photo4_text);
                            Log.e("SIZE4", "&&&&" + photo4_text);
                            setIcon(DatabaseHelper.KEY_PHOTO4);
                        }
                    }

                    if (photo5_flag == true) {
                        File file4 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME + "/SKAPP/SERVICE/" + enq_docno, "/IMG_PHOTO_5.jpg");
                        if (file4.exists()) {
                            photo5_text = Base64.encodeToString(byteArray, Base64.DEFAULT);
                            CustomUtility.setSharedPreference(mContext, enq_docno + "PHOTO_5", photo5_text);
                            Log.e("SIZE5", "&&&&" + photo5_text);
                            setIcon(DatabaseHelper.KEY_PHOTO5);
                        }
                    }

                    if (photo6_flag == true) {
                        File file5 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME+ "/SKAPP/SERVICE/" + enq_docno, "/IMG_PHOTO_6.jpg");
                        if (file5.exists()) {
                            photo6_text = Base64.encodeToString(byteArray, Base64.DEFAULT);
                            CustomUtility.setSharedPreference(mContext, enq_docno + "PHOTO_6", photo6_text);
                            Log.e("SIZE6", "&&&&" + photo6_text);
                            setIcon(DatabaseHelper.KEY_PHOTO6);
                        }
                    }

                    if (photo7_flag == true) {
                        File file6 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME + "/SKAPP/SERVICE/" + enq_docno, "/IMG_PHOTO_7.jpg");
                        if (file6.exists()) {
                            photo7_text = Base64.encodeToString(byteArray, Base64.DEFAULT);
                            CustomUtility.setSharedPreference(mContext, enq_docno + "PHOTO_7", photo7_text);
                            Log.e("SIZE7", "&&&&" + photo7_text);
                            setIcon(DatabaseHelper.KEY_PHOTO7);
                        }
                    }

                    if (photo8_flag == true) {
                        File file7 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME+ "/SKAPP/SERVICE/" + enq_docno, "/IMG_PHOTO_8.jpg");
                        if (file7.exists()) {
                            photo8_text = Base64.encodeToString(byteArray, Base64.DEFAULT);
                            CustomUtility.setSharedPreference(mContext, enq_docno + "PHOTO_8", photo8_text);
                            Log.e("SIZE8", "&&&&" + photo8_text);
                            setIcon(DatabaseHelper.KEY_PHOTO8);
                        }
                    }

                    if (photo9_flag == true) {
                        File file8 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME + "/SKAPP/SERVICE/" + enq_docno, "/IMG_PHOTO_9.jpg");
                        if (file8.exists()) {
                            photo9_text = Base64.encodeToString(byteArray, Base64.DEFAULT);
                            CustomUtility.setSharedPreference(mContext, enq_docno + "PHOTO_9", photo9_text);
                            Log.e("SIZE9", "&&&&" + photo9_text);
                            setIcon(DatabaseHelper.KEY_PHOTO9);
                        }

                    }

                  /*  if (photo10_flag == true) {
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
                    }*/


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

                                if (photo1_flag == true) {
                                    String destFile = getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME+ "/SKAPP/SERVICE/" + enq_docno + "/IMG_PHOTO_1.jpg";
                                    copyFile(selectedImagePath, destFile);
                                    File file = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME + "/SKAPP/SERVICE/" + enq_docno, "/IMG_PHOTO_1.jpg");
                                    if (file.exists()) {
                                        photo1_text = Base64.encodeToString(byteArray, Base64.DEFAULT);
                                        CustomUtility.setSharedPreference(mContext, enq_docno + "PHOTO_1", photo1_text);
                                        Log.e("SIZE1", "&&&&" + photo1_text);
                                        setIcon(DatabaseHelper.KEY_PHOTO1);
                                    }

                                }

                                if (photo2_flag == true) {
                                    String destFile = getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME + "/SKAPP/SERVICE/" + enq_docno + "/IMG_PHOTO_2.jpg";
                                    copyFile(selectedImagePath, destFile);
                                    File file1 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME + "/SKAPP/SERVICE/" + enq_docno, "/IMG_PHOTO_2.jpg");
                                    if (file1.exists()) {
                                        photo2_text = Base64.encodeToString(byteArray, Base64.DEFAULT);
                                        CustomUtility.setSharedPreference(mContext, enq_docno + "PHOTO_2", photo2_text);
                                        Log.e("SIZE2", "&&&&" + photo2_text);
                                        setIcon(DatabaseHelper.KEY_PHOTO2);
                                    }
                                }
                                if (photo3_flag == true) {
                                    String destFile = getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME + "/SKAPP/SERVICE/" + enq_docno + "/IMG_PHOTO_3.jpg";
                                    copyFile(selectedImagePath, destFile);
                                    File file2 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME + "/SKAPP/SERVICE/" + enq_docno, "/IMG_PHOTO_3.jpg");
                                    if (file2.exists()) {
                                        photo3_text = Base64.encodeToString(byteArray, Base64.DEFAULT);
                                        CustomUtility.setSharedPreference(mContext, enq_docno + "PHOTO_3", photo3_text);
                                        Log.e("SIZE3", "&&&&" + photo3_text);
                                        setIcon(DatabaseHelper.KEY_PHOTO3);
                                    }
                                }

                                if (photo4_flag == true) {
                                    String destFile =getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME+ "/SKAPP/SERVICE/" + enq_docno + "/IMG_PHOTO_4.jpg";
                                    copyFile(selectedImagePath, destFile);
                                    File file3 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME + "/SKAPP/SERVICE/" + enq_docno, "/IMG_PHOTO_4.jpg");
                                    if (file3.exists()) {
                                        photo4_text = Base64.encodeToString(byteArray, Base64.DEFAULT);
                                        CustomUtility.setSharedPreference(mContext, enq_docno + "PHOTO_4", photo4_text);
                                        Log.e("SIZE4", "&&&&" + photo4_text);
                                        setIcon(DatabaseHelper.KEY_PHOTO4);
                                    }
                                }

                                if (photo5_flag == true) {
                                    String destFile = getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME + "/SKAPP/SERVICE/" + enq_docno + "/IMG_PHOTO_5.jpg";
                                    copyFile(selectedImagePath, destFile);
                                    File file4 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME + "/SKAPP/SERVICE/" + enq_docno, "/IMG_PHOTO_5.jpg");
                                    if (file4.exists()) {
                                        photo5_text = Base64.encodeToString(byteArray, Base64.DEFAULT);
                                        CustomUtility.setSharedPreference(mContext, enq_docno + "PHOTO_5", photo5_text);
                                        Log.e("SIZE5", "&&&&" + photo5_text);
                                        setIcon(DatabaseHelper.KEY_PHOTO5);
                                    }
                                }

                                if (photo6_flag == true) {
                                    String destFile = getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME + "/SKAPP/SERVICE/" + enq_docno + "/IMG_PHOTO_6.jpg";
                                    copyFile(selectedImagePath, destFile);
                                    File file5 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME + "/SKAPP/SERVICE/" + enq_docno, "/IMG_PHOTO_6.jpg");
                                    if (file5.exists()) {
                                        photo6_text = Base64.encodeToString(byteArray, Base64.DEFAULT);
                                        CustomUtility.setSharedPreference(mContext, enq_docno + "PHOTO_6", photo6_text);
                                        Log.e("SIZE6", "&&&&" + photo6_text);
                                        setIcon(DatabaseHelper.KEY_PHOTO6);
                                    }
                                }

                                if (photo7_flag == true) {
                                    String destFile = getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME + "/SKAPP/SERVICE/" + enq_docno + "/IMG_PHOTO_7.jpg";
                                    copyFile(selectedImagePath, destFile);
                                    File file6 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME + "/SKAPP/SERVICE/" + enq_docno, "/IMG_PHOTO_7.jpg");
                                    if (file6.exists()) {
                                        photo7_text = Base64.encodeToString(byteArray, Base64.DEFAULT);
                                        CustomUtility.setSharedPreference(mContext, enq_docno + "PHOTO_7", photo7_text);
                                        Log.e("SIZE7", "&&&&" + photo7_text);
                                        setIcon(DatabaseHelper.KEY_PHOTO7);
                                    }
                                }

                                if (photo8_flag == true) {
                                    String destFile = getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME + "/SKAPP/SERVICE/" + enq_docno + "/IMG_PHOTO_8.jpg";
                                    copyFile(selectedImagePath, destFile);
                                    File file7 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME+ "/SKAPP/SERVICE/" + enq_docno, "/IMG_PHOTO_8.jpg");
                                    if (file7.exists()) {
                                        photo8_text = Base64.encodeToString(byteArray, Base64.DEFAULT);
                                        CustomUtility.setSharedPreference(mContext, enq_docno + "PHOTO_8", photo8_text);
                                        Log.e("SIZE8", "&&&&" + photo8_text);
                                        setIcon(DatabaseHelper.KEY_PHOTO8);
                                    }
                                }

                                if (photo9_flag == true) {
                                    String destFile = getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME + "/SKAPP/SERVICE/" + enq_docno + "/IMG_PHOTO_9.jpg";
                                    copyFile(selectedImagePath, destFile);
                                    File file8 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME+ "/SKAPP/SERVICE/" + enq_docno, "/IMG_PHOTO_9.jpg");
                                    if (file8.exists()) {
                                        photo9_text = Base64.encodeToString(byteArray, Base64.DEFAULT);
                                        CustomUtility.setSharedPreference(mContext, enq_docno + "PHOTO_9", photo9_text);
                                        Log.e("SIZE9", "&&&&" + photo9_text);
                                        setIcon(DatabaseHelper.KEY_PHOTO9);
                                    }

                                }

                               /* if (photo10_flag == true) {
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

                                if (photo11_flag == true) {
                                    String destFile = getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME  + "/SKAPP/SERVICE/" + enq_docno + "/IMG_PHOTO_11.jpg";
                                    copyFile(selectedImagePath, destFile);
                                    File file10 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME  + "/SKAPP/SERVICE/" + enq_docno, "/IMG_PHOTO_11.jpg");
                                    if (file10.exists()) {
                                        photo11_text = Base64.encodeToString(byteArray, Base64.DEFAULT);
                                        CustomUtility.setSharedPreference(mContext, enq_docno + "PHOTO_11", photo11_text);
                                        Log.e("SIZE11", "&&&&" + photo11_text);
                                        setIcon(DatabaseHelper.KEY_PHOTO11);
                                    }
                                }


                                if (photo12_flag == true) {
                                    String destFile = getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME  + "/SKAPP/SERVICE/" + enq_docno + "/IMG_PHOTO_12.jpg";
                                    copyFile(selectedImagePath, destFile);
                                    File file11 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME  + "/SKAPP/SERVICE/" + enq_docno, "/IMG_PHOTO_12.jpg");
                                    if (file11.exists()) {
                                        photo12_text = Base64.encodeToString(byteArray, Base64.DEFAULT);
                                        CustomUtility.setSharedPreference(mContext, enq_docno + "PHOTO_12", photo12_text);
                                        Log.e("SIZE11", "&&&&" + photo12_text);
                                        setIcon(DatabaseHelper.KEY_PHOTO12);
                                    }
                                }*/
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
        Intent i_display_image = new Intent(mContext, ShowPhotograph.class);
       /* Bundle extras = new Bundle();
        //saveData();
        extras.putString("docno", enq_docno);
        extras.putString("key", key);
        extras.putString("data", "INST");*/

       // CustomUtility.setSharedPreference(mContext, "data", data);

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
                if (!TextUtils.isEmpty(pht2)) {
                    if (!TextUtils.isEmpty(pht3)) {
                        CustomUtility.setSharedPreference(mContext, "INSTSYNC" + enq_docno, "1");
                    } else {

                        Toast.makeText(this, "Please Select Cust./Deale/JCC Bill Photo.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Please Select Product Full Images Photo.", Toast.LENGTH_SHORT).show();
                  //  Toast.makeText(this, "Please Select Controller Photo.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Please Select Falty Material/Mager meges Photo.", Toast.LENGTH_SHORT).show();
              //  Toast.makeText(this, "Please Select Panel Module Photo.", Toast.LENGTH_SHORT).show();
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


    private class savePendingPhotoDataAPI extends AsyncTask<String, String, String> {

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

                String ph1,ph2,ph3,ph4,ph5,ph6,ph7,ph8,ph9;

                ph1 = activity.CustomUtility.getSharedPreferences(mContext, enq_docno + "PHOTO_1");
                ph2 = activity.CustomUtility.getSharedPreferences(mContext, enq_docno + "PHOTO_2");
                ph3 = activity.CustomUtility.getSharedPreferences(mContext, enq_docno + "PHOTO_3");
                ph4 = activity.CustomUtility.getSharedPreferences(mContext, enq_docno + "PHOTO_4");
                ph5 = activity.CustomUtility.getSharedPreferences(mContext, enq_docno + "PHOTO_5");
                ph6 = activity.CustomUtility.getSharedPreferences(mContext, enq_docno + "PHOTO_6");
                ph7 = activity.CustomUtility.getSharedPreferences(mContext, enq_docno + "PHOTO_7");
                ph8 = activity.CustomUtility.getSharedPreferences(mContext, enq_docno + "PHOTO_8");
                ph9 = activity.CustomUtility.getSharedPreferences(mContext, enq_docno + "PHOTO_9");

                jsonObj.put("CMPNO", enq_docno);
                jsonObj.put("kunnr", mUserID);
                jsonObj.put("sc_status", mStatusCheck);
                jsonObj.put("ACTION", pendRemarkValue);



                System.out.println("only_text_jsonObj==>>"+jsonObj);

                jsonObj.put("PHOTO1", ph1);
                jsonObj.put("PHOTO2", ph2);
                jsonObj.put("PHOTO3", ph3);
                jsonObj.put("PHOTO4", ph4);
                jsonObj.put("PHOTO5", ph5);
                jsonObj.put("PHOTO6", ph6);
                jsonObj.put("PHOTO7", ph7);
                jsonObj.put("PHOTO8", ph8);
                jsonObj.put("PHOTO9", ph9);
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
            param1_invc.add(new BasicNameValuePair("pending_save", String.valueOf(ja_invc_data)));
            Log.e("DATA", "$$$$" + param1_invc.toString());

            System.out.println("param1_invc_vihu==>>"+param1_invc.toString());
            try {

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
                StrictMode.setThreadPolicy(policy);

                obj2 = CustomHttpClient.executeHttpPost1(WebURL.PENDING_COMPLAIN_PENDING_PHOTO_VK_PAGE, param1_invc);

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
                          /*   activity.CustomUtility.setSharedPreference(context,"borewellstatus"+enq_docno, "");
                            activity.CustomUtility.setSharedPreference(context,"borewellstatus", "");*/
                        activity.CustomUtility.setSharedPreference(mContext, enq_docno + "PHOTO_1", "");
                        activity.CustomUtility.setSharedPreference(mContext, enq_docno + "PHOTO_2", "");
                        activity.CustomUtility.setSharedPreference(mContext, enq_docno + "PHOTO_3", "");
                        activity.CustomUtility.setSharedPreference(mContext, enq_docno + "PHOTO_4", "");
                        activity.CustomUtility.setSharedPreference(mContext, enq_docno + "PHOTO_5", "");
                        activity.CustomUtility.setSharedPreference(mContext, enq_docno + "PHOTO_6", "");
                        activity.CustomUtility.setSharedPreference(mContext, enq_docno + "PHOTO_7", "");
                        activity.CustomUtility.setSharedPreference(mContext, enq_docno + "PHOTO_8", "");
                        activity.CustomUtility.setSharedPreference(mContext, enq_docno + "PHOTO_9", "");
                        activity.CustomUtility.setSharedPreference(mContext, enq_docno + "PHOTO_10", "");
                        activity.CustomUtility.setSharedPreference(mContext, enq_docno + "PHOTO_11", "");
                        activity.CustomUtility.setSharedPreference(mContext, enq_docno + "PHOTO_12", "");

                        activity.CustomUtility.setSharedPreference(mContext, "SYNCLIST", "1");

                        progressDialog.dismiss();
                        finish();

                        progressDialog.dismiss();
                        //  finish();
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


    public void setFlag(String key) {

        Log.e("FLAG", "&&&" + key);
        photo1_flag = false;
        photo2_flag = false;
        photo3_flag = false;
        photo4_flag = false;
        photo5_flag = false;
        photo6_flag = false;

        photo7_flag = false;
        photo8_flag = false;
        photo9_flag = false;
        photo10_flag = false;
        photo11_flag = false;
        photo12_flag = false;

        switch (key) {
            case DatabaseHelper.KEY_PHOTO1:
                photo1_flag = true;
                break;
            case DatabaseHelper.KEY_PHOTO2:
                photo2_flag = true;
                break;
            case DatabaseHelper.KEY_PHOTO3:
                photo3_flag = true;
                break;
            case DatabaseHelper.KEY_PHOTO4:
                photo4_flag = true;
                break;
            case DatabaseHelper.KEY_PHOTO5:
                photo5_flag = true;
                break;
            case DatabaseHelper.KEY_PHOTO6:
                photo6_flag = true;
                break;


            case DatabaseHelper.KEY_PHOTO7:
                photo7_flag = true;
                break;
            case DatabaseHelper.KEY_PHOTO8:
                photo8_flag = true;
                break;
            case DatabaseHelper.KEY_PHOTO9:
                photo9_flag = true;
                break;
            case DatabaseHelper.KEY_PHOTO10:
                photo10_flag = true;
                break;
            case DatabaseHelper.KEY_PHOTO11:
                photo11_flag = true;
                break;
            case DatabaseHelper.KEY_PHOTO12:
                photo12_flag = true;
                break;

        }

    }

    public void setIcon(String key) {


        switch (key) {

            case DatabaseHelper.KEY_PHOTO1:
                if (photo1_text == null || photo1_text.isEmpty()) {
                    photo1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mendotry, 0, R.drawable.red_icn, 0);
                } else {
                    photo1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mendotry, 0, R.drawable.right_mark_icn_green, 0);
                }
                break;


            case DatabaseHelper.KEY_PHOTO2:
                if (photo2_text == null || photo2_text.isEmpty()) {
                    photo2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mendotry, 0, R.drawable.red_icn, 0);
                } else {
                    photo2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mendotry, 0, R.drawable.right_mark_icn_green, 0);
                }
                break;

            case DatabaseHelper.KEY_PHOTO3:
                if (photo3_text == null || photo3_text.isEmpty()) {
                    photo3.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mendotry, 0, R.drawable.red_icn, 0);
                } else {
                    photo3.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mendotry, 0, R.drawable.right_mark_icn_green, 0);
                }
                break;

            case DatabaseHelper.KEY_PHOTO4:
                if (photo4_text == null || photo4_text.isEmpty()) {
                    photo4.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mendotry, 0, R.drawable.red_icn, 0);
                } else {
                    photo4.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mendotry, 0, R.drawable.right_mark_icn_green, 0);
                }
                break;
            case DatabaseHelper.KEY_PHOTO5:
                if (photo5_text == null || photo5_text.isEmpty()) {

                    if(CustomUtility.getSharedPreferences(mContext,"borewellstatus").equalsIgnoreCase("2")) {
                        if (!TextUtils.isEmpty(CustomUtility.getSharedPreferences(mContext, "borewellstatus" + enq_docno))) {
                            photo5.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mendotry, 0, R.drawable.red_icn, 0);
                            photo5.setText("Consent Letter of Warranty");
                        }
                    }
                    else if(CustomUtility.getSharedPreferences(mContext,"borewellstatus").equalsIgnoreCase("1")){
                        if (!TextUtils.isEmpty(CustomUtility.getSharedPreferences(mContext, "borewellstatus" + enq_docno))) {
                            photo5.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mendotry, 0, R.drawable.red_icn, 0);
                            photo5.setText("Borewell Before Photo");
                        }
                    }
                    else{
                            photo5.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.red_icn, 0);
                            //photo5.setText("Consent Letter of Warranty");
                    }

                } else {
                    if(CustomUtility.getSharedPreferences(mContext,"borewellstatus").equalsIgnoreCase("2")) {
                        if (!TextUtils.isEmpty(CustomUtility.getSharedPreferences(mContext, "borewellstatus" + enq_docno))) {
                            photo5.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mendotry, 0, R.drawable.right_mark_icn_green, 0);
                            photo5.setText("Consent Letter of Warranty");
                        }
                    }else if(CustomUtility.getSharedPreferences(mContext,"borewellstatus").equalsIgnoreCase("1")){
                        if (!TextUtils.isEmpty(CustomUtility.getSharedPreferences(mContext, "borewellstatus" + enq_docno))) {
                            photo5.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mendotry, 0, R.drawable.right_mark_icn_green, 0);
                            photo5.setText("Borewell Before Photo");
                        }
                    }
                    else{
                        photo5.setCompoundDrawablesWithIntrinsicBounds(0, 0,  R.drawable.right_mark_icn_green, 0);
                    }

                }
                break;
            case DatabaseHelper.KEY_PHOTO6:
                if (photo6_text == null || photo6_text.isEmpty()) {
                    if(CustomUtility.getSharedPreferences(mContext,"borewellstatus").equalsIgnoreCase("1")){
                        if (!TextUtils.isEmpty(CustomUtility.getSharedPreferences(mContext, "borewellstatus" + enq_docno))) {
                            photo6.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mendotry, 0, R.drawable.red_icn, 0);
                            photo6.setText("Borewell After Photo");
                        }
                    }
                    else{
                        photo6.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.red_icn, 0);
                        //photo5.setText("Consent Letter of Warranty");
                    }

                } else {
                    if(CustomUtility.getSharedPreferences(mContext,"borewellstatus").equalsIgnoreCase("1")){
                        if (!TextUtils.isEmpty(CustomUtility.getSharedPreferences(mContext, "borewellstatus" + enq_docno))) {
                            photo6.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mendotry, 0, R.drawable.right_mark_icn_green, 0);
                            photo6.setText("Borewell After Photo");
                        }
                    }
                    else{
                        photo6.setCompoundDrawablesWithIntrinsicBounds(0, 0,  R.drawable.right_mark_icn_green, 0);
                    }

                }
                break;

            case DatabaseHelper.KEY_PHOTO7:
                if (photo7_text == null || photo7_text.isEmpty()) {
                    photo7.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.red_icn, 0);
                   /* if(status.equals("1")){

                            photo7.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mendotry, 0, R.drawable.red_icn, 0);
                           // photo7.setText("Installation Delay Photo");
                    }
                    else{
                        photo7.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.red_icn, 0);
                        //photo5.setText("Consent Letter of Warranty");
                    }*/
                } else {
                    photo7.setCompoundDrawablesWithIntrinsicBounds(0, 0,  R.drawable.right_mark_icn_green, 0);
                   /* if(status.equals("1")){

                            photo7.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mendotry, 0, R.drawable.right_mark_icn_green, 0);
                          //  photo7.setText("Installation Delay Photo");
                    }
                    else{
                        photo7.setCompoundDrawablesWithIntrinsicBounds(0, 0,  R.drawable.right_mark_icn_green, 0);
                    }*/

                }
                break;


            case DatabaseHelper.KEY_PHOTO8:
                if (photo8_text == null || photo8_text.isEmpty()) {
                    photo8.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.red_icn, 0);
                } else {
                    photo8.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.right_mark_icn_green, 0);
                }
                break;

            case DatabaseHelper.KEY_PHOTO9:
                if (photo9_text == null || photo9_text.isEmpty()) {
                    photo9.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.red_icn, 0);
                } else {
                    photo9.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.right_mark_icn_green, 0);
                }
                break;


         /*   case DatabaseHelper.KEY_PHOTO10:
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
                break;*/
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


}
