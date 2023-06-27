package activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.widget.Toolbar;

import com.shaktipumps.shakti.shaktiServiceCenter.R;


public class PhotoViewerActivity extends BaseActivity {

     ImageView showImg;
     Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_viewer);

       Init();
       listner();
    }

    private void listner() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void Init() {
        showImg = findViewById(R.id.showImg);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setTitle(getResources().getString(R.string.photoGallery));

        Bitmap myBitmap = BitmapFactory.decodeFile(getIntent().getStringExtra("image_path"));

        showImg.setImageBitmap(myBitmap);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}