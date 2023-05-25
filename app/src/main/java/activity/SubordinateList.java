package activity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.shaktipumps.shakti.shaktiServiceCenter.R;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

import adapter.SubordinateAdapter;
import bean.SubordinateBean;
import database.DatabaseHelper;

public class SubordinateList extends AppCompatActivity {

    Toolbar toolbar;
    Context context;
    DatabaseHelper db;
    ListView inst_list;
    String userid;
    SubordinateAdapter adapter;
    EditText editSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subordinate_list);

        context = this;

        toolbar = findViewById(R.id.toolbar);
        db = new DatabaseHelper(context);
        inst_list = findViewById(R.id.subordinate_list);
        userid = CustomUtility.getSharedPreferences(context,"username");

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.edit_subordinate));

        ArrayList<SubordinateBean> subordinateBeanArrayList ;
        subordinateBeanArrayList = db.getSubordinateList();

        adapter = new SubordinateAdapter(context, subordinateBeanArrayList);

        inst_list.setAdapter(adapter);

        inst_list.setOnItemClickListener((parent, view, position, id) -> {
            Intent i = new Intent(context, EditSubordinate.class);
            Bundle extras = new Bundle();
            extras.putParcelable("subordinateDetail",  adapter.arrayList.get(position));
            i.putExtras(extras);
            startActivity(i);
        });

        editSearch =  findViewById(R.id.search);

        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                String text = editSearch.getText().toString().toLowerCase(Locale.getDefault());
                adapter.filter(text);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub
            }
        });


    }



    // toolbar back button code.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_signout:
                return true;

        }
        return super.onOptionsItemSelected(item);
    }


}