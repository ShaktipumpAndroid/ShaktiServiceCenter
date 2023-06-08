package activity.retrofit.otp;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.View;
import android.view.Window;

import com.google.gson.JsonElement;
import com.shaktipumps.shakti.shaktiServiceCenter.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import rest.ApiClient;
import rest.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class BaseRequest extends BaseRequestParser {
    private Context mContext;
    private ApiInterface apiInterface;
    private RequestReciever requestReciever;
    private boolean runInBackground = false;
    private Dialog dialog;
    private View loaderView = null;
    private int APINumber_ = 1;

    public BaseRequest(Context context) {
        mContext = context;

        apiInterface =
                ApiClient.getClientversion().create(ApiInterface.class);
        dialog = getProgressesDialog(context);

    }
    public void setBaseRequestListner(RequestReciever requestListner) {
        this.requestReciever = requestListner;

    }

    public Callback<JsonElement> responseCallback = new Callback<JsonElement>() {
        @Override
        public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
            String responseServer = "";
            if (null != response.body()) {
                JsonElement jsonElement = (JsonElement) response.body();
                if (null != jsonElement) {
                    responseServer = jsonElement.toString();
                }
                System.out.println("responseServer==>>"+responseServer);
            } else if (response.errorBody() != null) {
                responseServer = readStreamFully(response.errorBody().contentLength(),
                        response.errorBody().byteStream());
            }
            logFullResponse(responseServer, "OUTPUT");
            if (parseJson(responseServer)) {
                if (mResponseCode.equals("true")) {
                    if (null != requestReciever) {
                        requestReciever.onSuccess(APINumber_, responseServer, getDataArray());
                    }
                }else if (mResponseCode.equals("001")) {
                    if (null != requestReciever) {
                        requestReciever.onSuccess(APINumber_, responseServer, getDataArray());
                    }
                }  else if (mResponseCode.equals("false")) {
                    if (null != requestReciever) {
                        requestReciever.onFailure(1, "" + mResponseCode, message);
                    }
                } else {
                    if (null != requestReciever) {
                        requestReciever.onFailure(1, "" + mResponseCode, message);
                    }
                }
            } else {
                if (null != requestReciever) {
                    requestReciever.onFailure(1, mResponseCode, responseServer);
                }
            }
           // hideLoader();
        }

        @Override
        public void onFailure(Call<JsonElement> call, Throwable t) {
            if (getConnectivityStatus(mContext) == TYPE_NOT_CONNECTED) {
                if (null != requestReciever) {
                    requestReciever.onNetworkFailure(APINumber_, "Network Error");
                }
            }
            hideLoader();
        }
    };

    public Callback<JsonElement> responseCallbackIMEI = new Callback<JsonElement>() {
        @Override
        public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
            String responseServer = "";
            if (null != response.body()) {
                JsonElement jsonElement = (JsonElement) response.body();
                if (null != jsonElement) {
                    responseServer = jsonElement.toString();
                }
                System.out.println("responseServer==>>"+responseServer);
            } else if (response.errorBody() != null) {
                responseServer = readStreamFully(response.errorBody().contentLength(),
                        response.errorBody().byteStream());
            }
            logFullResponse(responseServer, "OUTPUT");

            if (parseJson(responseServer)) {
                requestReciever.onSuccess(APINumber_, responseServer, getDataArray());
            } else {
                if (null != requestReciever) {


                    requestReciever.onFailure(1, mResponseCode, responseServer);
                }
            }
            // hideLoader();
        }

        @Override
        public void onFailure(Call<JsonElement> call, Throwable t) {
            if (getConnectivityStatus(mContext) == TYPE_NOT_CONNECTED) {
                if (null != requestReciever) {
                    requestReciever.onNetworkFailure(APINumber_, "Network Error");
                }
            }
            hideLoader();
        }
    };



    public Dialog getProgressesDialog(Context ct)
    {
        Dialog dialog = null;
        try {
            dialog = new Dialog(ct);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.progress_dialog_loader);
//            // Set the progress dialog background color transparent
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            //  GifView pGif = (GifView) dialog.findViewById(R.id.progressBar);
            // pGif.setImageResource(R.drawable.loadingvk);
//            dialog.setIndeterminate(false);
            dialog.setCanceledOnTouchOutside(false);


        } catch (Exception e) {
            e.printStackTrace();
        }

        //loader_tv = (TextView)dialog.findViewById(R.id.loader_tv);
      /*  WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);*/
        return dialog;
    }


    public void logFullResponse(String response, String inout) {
        final int chunkSize = 3000;
        if (null != response && response.length() > chunkSize) {
            int chunks = (int) Math.ceil((double) response.length()
                    / (double) chunkSize);

            for (int i = 1; i <= chunks; i++) {
                if (i != chunks) {
                    Log.i("BaseReq",
                            inout + " : "
                                    + response.substring((i - 1) * chunkSize, i
                                    * chunkSize));
                } else {
                    Log.i("BaseReq",
                            inout + " : "
                                    + response.substring((i - 1) * chunkSize,
                                    response.length()));
                }
            }
        } else {

            try {
                JSONObject jsonObject = new JSONObject(response);
                Log.d("BaseReq", inout + " : " + jsonObject.toString(jsonObject.length()));

            } catch (JSONException e) {
                e.printStackTrace();
                Log.d("BaseReq", " logFullResponse=>  " + response);
            }
        }
    }

    private String readStreamFully(long len, InputStream inputStream) {
        if (inputStream == null) {
            return null;
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    public void callAPIGETDirectURL(final int APINumber,String remainingURL) {
        APINumber_ = APINumber;
        showLoader();
        System.out.println("BaseReq INPUT URL : " + remainingURL);
        Call<JsonElement> call = apiInterface.postDataGET(remainingURL);
        call.enqueue(responseCallback);
    }

    public void showLoader() {
        try {
            if (!runInBackground) {
                if (null != loaderView) {
                    loaderView.setVisibility(View.VISIBLE);
                } else if (null != dialog) {
                    dialog.show();
                   // progress.show();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void hideLoader() {
        try {
            if (!runInBackground) {
                if (null != loaderView) {
                    loaderView.setVisibility(View.GONE);
                } else if (null != dialog) {
                   dialog.dismiss();
                    //progress.dismiss();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int TYPE_NOT_CONNECTED = 0;
    public int TYPE_WIFI = 1;
    public int TYPE_MOBILE = 2;

    public int getConnectivityStatus(Context context) {
        if (null == context) {
            return TYPE_NOT_CONNECTED;
        }
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        if (null != activeNetwork && activeNetwork.isConnected()) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                return TYPE_WIFI;
            }
            if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                return TYPE_MOBILE;
            }
        }
        return TYPE_NOT_CONNECTED;
    }
}
