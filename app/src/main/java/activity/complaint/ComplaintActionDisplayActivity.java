package activity.complaint;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;


import com.shaktipumps.shakti.shaktiServiceCenter.R;

import java.util.ArrayList;

import database.DatabaseHelper;
import searchlist.complaint.SearchComplaintAction;
import searchlist.complaint.SearchComplaintActionListViewAdapter;

public class ComplaintActionDisplayActivity extends AppCompatActivity {
    Context mContex;
    ListView list;
    String cmp_no,pendingid;
    EditText editsearch;
    SearchComplaintActionListViewAdapter adapter;
    ArrayList<SearchComplaintAction> arraylist = new ArrayList<SearchComplaintAction>();
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint_action_display);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        list = (ListView) findViewById(R.id.listview);
        mContex = this;


        Bundle bundle = getIntent().getExtras();

        cmp_no = bundle.getString("cmpno");

        getSupportActionBar().setTitle("Complaint No :" + cmp_no + " " + "Action");


        getComplaint();

        //getPendingReasonname();
//

        getComplaintAction();

        adapter = new SearchComplaintActionListViewAdapter(this, arraylist);


        // Binds the Adapter to the ListView
        list.setAdapter(adapter);


//        editsearch = (EditText) findViewById(R.id.search);
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
//

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        switch (id) {

            case android.R.id.home:
                onBackPressed();
                //callWebPage();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }


    public void getComplaint() {

        DatabaseHelper dataHelper = new DatabaseHelper(this);
        Cursor cursor = null;
        arraylist.clear();

        SQLiteDatabase db = dataHelper.getReadableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        try {
            String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_ZINPROCESS_COMPLAINT
                    + " WHERE " + DatabaseHelper.KEY_CMPNO + " = '" + cmp_no + "'";

            cursor = db.rawQuery(selectQuery, null);

            Log.d("comp_inprocess", "" + cursor.getCount());

            if (cursor.getCount() > 0) {


                while (cursor.moveToNext()) {

                    // Log.d("cmp_person",""+cursor.getString(cursor.getColumnIndex("ename")));

                    SearchComplaintAction sc = new SearchComplaintAction();
                    sc.setPernr(cursor.getString(cursor.getColumnIndex("pernr")));
                    sc.setEname(cursor.getString(cursor.getColumnIndex("ename")));
                    sc.setPending_reason(cursor.getString(cursor.getColumnIndex("name")));
                    pendingid = cursor.getString(cursor.getColumnIndex("name"));
                    sc.setFollow_up_date(cursor.getString(cursor.getColumnIndex("follow_up_date")));
                    sc.setDate(cursor.getString(cursor.getColumnIndex("cr_date")));
                    sc.setReason(cursor.getString(cursor.getColumnIndex("reason")));
                    sc.setStatus(cursor.getString(cursor.getColumnIndex("cmpln_status")));

                    arraylist.add(sc);

                }

            }


            db.setTransactionSuccessful();

        } catch (SQLiteException e) {
            e.printStackTrace();

        } finally {
            if(db!=null) {
                db.endTransaction();
            }
            if (cursor != null) {
                cursor.close();
            }

            // End the transaction.
            if(db!=null) {
                db.close();
            }
            // Close database

        }


    }


    public void getComplaintAction() {

        DatabaseHelper dataHelper = new DatabaseHelper(this);
        Cursor cursor = null;

        SQLiteDatabase db = dataHelper.getReadableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        try {
            String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_COMPLAINT_ACTION
                    + " WHERE " + DatabaseHelper.KEY_CMPNO + " = '" + cmp_no + "'";


            cursor = db.rawQuery(selectQuery, null);

            //Log.d("comp_action",""+cursor.getCount());

            if (cursor.getCount() > 0) {


                while (cursor.moveToNext()) {

                    //    Log.d("cmp_person",""+cursor.getString(cursor.getColumnIndex("ename")));

                    SearchComplaintAction sc = new SearchComplaintAction();
                    sc.setPernr(cursor.getString(cursor.getColumnIndex("pernr")));
                    sc.setEname(cursor.getString(cursor.getColumnIndex("ename")));
                    sc.setFollow_up_date(cursor.getString(cursor.getColumnIndex("follow_up_date")));
                    sc.setDate(cursor.getString(cursor.getColumnIndex("cr_date")));
                    sc.setReason(cursor.getString(cursor.getColumnIndex("reason")));
                    sc.setStatus(cursor.getString(cursor.getColumnIndex("cmpln_status")));

                    arraylist.add(sc);

                }

            }


            db.setTransactionSuccessful();

        } catch (SQLiteException e) {
            e.printStackTrace();

        } finally {
            if(db!=null) {
                db.endTransaction();
            }
            if (cursor != null) {
                cursor.close();
            }

            // End the transaction.
            if(db!=null) {
                db.close();
            }
            // Close database

        }


    }
}
