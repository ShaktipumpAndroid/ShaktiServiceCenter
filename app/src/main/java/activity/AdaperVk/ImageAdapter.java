package activity.AdaperVk;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.recyclerview.widget.RecyclerView;

import com.shaktipumps.shakti.shaktiServiceCenter.R;

import java.util.ArrayList;


/**
 * Created by droidNinja on 29/07/16.
 */
public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.FileViewHolder> {

    private final ArrayList<String> paths;

    public ImageAdapter(Context context, ArrayList<String> paths) {
        this.paths = paths;
        setColumnNumber(context, 3);
    }

    private void setColumnNumber(Context context, int columnNum) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        int widthPixels = metrics.widthPixels;
        int imageSize = widthPixels / columnNum;
    }


    @Override
    public FileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);

        return new FileViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FileViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return paths.size();
    }

    public static class FileViewHolder extends RecyclerView.ViewHolder {

        public FileViewHolder(View itemView) {
            super(itemView);
            // imageView = itemView.findViewById(R.id.iv_photo);
        }
    }
}
