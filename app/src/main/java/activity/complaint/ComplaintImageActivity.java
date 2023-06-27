package activity.complaint;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.widget.Toolbar;

import com.shaktipumps.shakti.shaktiServiceCenter.R;

import java.util.ArrayList;

import activity.BaseActivity;
import bean.ComplaintImage;
import database.DatabaseHelper;
import other.PathUtil;
import searchlist.complaint.Complaint_Image_ListViewAdapter;
import searchlist.complaint.Complaint_Image_Name;

public class ComplaintImageActivity extends BaseActivity {
    private static final int GALLERY_IMAGE_REQUEST_CODE = 101;
    private static final int FILE_SELECT_CODE = -1;
    Context mContext;
    ListView list;
    ArrayList<Complaint_Image_Name> arraylist = new ArrayList<Complaint_Image_Name>();
    String cmp_no = "", cmp_category = "", category_name = "";
    EditText editsearch;
    Complaint_Image_ListViewAdapter adapter;
    String file_path;
    String audio_record = "", image_item = "";
    ArrayList<ComplaintImage> list_complaintImageTaken;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint_image);

        mContext = this;
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        list = (ListView) findViewById(R.id.listview);

        setSupportActionBar(mToolbar);

        list_complaintImageTaken = new ArrayList<ComplaintImage>();

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setTitle("Complaint Image");


        cmp_no = PathUtil.getSharedPreferences(mContext, "cmp_no");
        cmp_category = PathUtil.getSharedPreferences(mContext, "cmp_category");


        // Log.d("cmp_category2",cmp_category);

        String CurrentString = cmp_category;
        String[] separated;
        separated = CurrentString.split("--");
        category_name = separated[0];
        cmp_category = separated[1];


        check_complaint_image();


        getComplaint_Image();


//        // Locate the EditText in listview_main.xml
//        editsearch = (EditText) findViewById(R.id.search);
//
//        // Capture Text in EditText
//        editsearch.addTextChangedListener(new TextWatcher() {
//
//            @Override
//            public void afterTextChanged(Editable arg0) {
//                // TODO Auto-generated method stub
//                String text = editsearch.getText().toString().toLowerCase(Locale.getDefault());
//                adapter.filter(text);
//            }
//
//            @Override
//            public void beforeTextChanged(CharSequence arg0, int arg1,
//                                          int arg2, int arg3) {
//                // TODO Auto-generated method stub
//            }
//
//            @Override
//            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
//                                      int arg3) {
//                // TODO Auto-generated method stub
//            }
//        });


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


    public void getComplaint_Image() {

        DatabaseHelper dataHelper = new DatabaseHelper(this);
        Cursor cursor = null;
        arraylist.clear();
        SQLiteDatabase db = dataHelper.getReadableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();

        String lv_category, lv_image_item;
        try {
//           String selectQuery = "SELECT * FROM "+ DatabaseHelper.TABLE_COMPLAINT_IMAGE_NAME
//                   +  " WHERE " +  DatabaseHelper. KEY_CATEGORY   +  " = '" + category + "'" ;
//
//


            String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_COMPLAINT_IMAGE_NAME
                    + " WHERE " + DatabaseHelper.KEY_CATEGORY + " = '" + cmp_category + "'";


            cursor = db.rawQuery(selectQuery, null);

            // Log.d("comp",""+cursor.getCount());

            if (cursor.getCount() > 0) {


                while (cursor.moveToNext()) {

                    Complaint_Image_Name image_name = new Complaint_Image_Name();

                    image_name.setCategory(cursor.getString(cursor.getColumnIndex("category")));
                    image_name.setItem(cursor.getString(cursor.getColumnIndex("image_item")));
                    image_name.setName(cursor.getString(cursor.getColumnIndex("cmp_image")));
                    arraylist.add(image_name);

                    Log.e("comp1", "&&&&&" + cursor.getString(cursor.getColumnIndex("category")));
                    Log.e("comp2", "&&&&&" + cursor.getString(cursor.getColumnIndex("image_item")));
                    Log.e("comp3", "&&&&&" + cursor.getString(cursor.getColumnIndex("cmp_image")));


                    // Pass results to ListViewAdapter Class
                    adapter = new Complaint_Image_ListViewAdapter(this, arraylist, cmp_category, list_complaintImageTaken, cmp_no);


                    // Binds the Adapter to the ListView
                    list.setAdapter(adapter);


                    lv_category = cursor.getString(cursor.getColumnIndex("category"));
                    lv_image_item = cursor.getString(cursor.getColumnIndex("image_item"));


                }

            }

            db.setTransactionSuccessful();

        } catch (SQLiteException e) {
            e.printStackTrace();

        } finally {

            db.endTransaction();
            if (cursor != null) {
                cursor.close();
            }

            // End the transaction.
            db.close();
            // Close database

        }


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        audio_record = adapter.onActivityResult(requestCode, resultCode, data, cmp_category, cmp_no);

    }


    public void check_complaint_image() {


        list_complaintImageTaken.clear();

        DatabaseHelper database = new DatabaseHelper(mContext);


        SQLiteDatabase db = database.getReadableDatabase();

        String selectQuery = null;
        Cursor cursor = null;
        try {


            selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_ZCMPLN_IMAGE
                    + " WHERE " + DatabaseHelper.KEY_CMPNO + " = '" + cmp_no + "'" + " AND " +
                    DatabaseHelper.KEY_CATEGORY + " = '" + cmp_category + "'";


            cursor = db.rawQuery(selectQuery, null);

            // Log.d("cmp_image_dtl",""+cursor.getCount());

            cursor = db.rawQuery(selectQuery, null);

            if (cursor.getCount() > 0) {


                while (cursor.moveToNext()) {

                    ComplaintImage complaintImage = new ComplaintImage();

                    complaintImage.setKey_id(cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_ID)));
                    complaintImage.setCmpno(cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_CMPNO)));
                    complaintImage.setPosnr(cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_POSNR)));
                    complaintImage.setCategory(cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_CATEGORY)));

                    //  Log.d("image_taken5",""+ cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_POSNR)));

                    Log.e("checkcomp1", "&&&&&" + cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_ID)));
                    Log.e("checkcomp2", "&&&&&" + cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_CMPNO)));
                    Log.e("checkcomp3", "&&&&&" + cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_POSNR)));
                    Log.e("checkcomp4", "&&&&&" + cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_CATEGORY)));

                    list_complaintImageTaken.add(complaintImage);


                }

            }
        } catch (SQLiteException e) {
            e.printStackTrace();

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

    }


}
