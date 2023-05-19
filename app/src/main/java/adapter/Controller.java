package adapter;

import android.content.Context;
import android.content.Intent;

import android.util.Log;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;


import com.shaktipumps.shakti.shaktiServiceCenter.R;

import java.util.ArrayList;

import model.ModelCart;
import model.ModelProducts;

public class Controller extends MultiDexApplication {

    private ArrayList<ModelProducts> myproducts = new ArrayList<ModelProducts>();
    private ModelCart myCart = new ModelCart();

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {

            @Override
            public void uncaughtException(Thread thread, Throwable ex) {
                handleUncaughtException(thread, ex);

            }
        });

    }

    public void handleUncaughtException (Thread thread, Throwable e)
    {
        String stackTrace = Log.getStackTraceString(e);
        String message = e.getMessage();

        Intent intent = new Intent (Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra (Intent.EXTRA_EMAIL, new String[] {"info.mobileapp@shaktipumps.com;mayank.singh@shaktipumps.com;vikas.gothi@shaktipumps.com"});
        intent.putExtra (Intent.EXTRA_SUBJECT, R.string.crash_toast_text);
        intent.putExtra (Intent.EXTRA_TEXT, stackTrace);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // required when starting from Application
        startActivity(intent);


    }

    public ModelProducts getProducts(final int pPosition) {
        return myproducts.get(pPosition);
    }

    public void setProducts(ModelProducts products) {
        myproducts.add(products);
    }

    public ModelCart getCart() {
        return myCart;
    }

    public int getProductArraylistsize() {
        return myproducts.size();
    }


}
