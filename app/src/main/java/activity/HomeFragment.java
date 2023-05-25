package activity;

// /**
// * Created by shakti on 10/3/2016.
// */

import static androidx.core.content.ContextCompat.checkSelfPermission;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.shaktipumps.shakti.shaktiServiceCenter.R;

import java.io.File;
import java.util.ArrayList;

import activity.complainvk.PendingComplainListActivity;
import database.DatabaseHelper;
import webservice.WebURL;


//import com.shaktipumps.shakti.material_design.R;



    @SuppressWarnings({"deprecation", "StatementWithEmptyBody"})
    public class HomeFragment extends Fragment implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {
    ProgressDialog progressDialog;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    Context context;


TextView approved_complaint, closer_complaint, open_complaint, pending_for_Approval, txtServiceCenterID;

    String start_photo_text,end_photo_text;

    protected Location location;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    private ArrayList<String> permissionsToRequest;
    private final ArrayList<String> permissionsRejected = new ArrayList<>();
    private final ArrayList<String> permissions = new ArrayList<>();


    private static final int ALL_PERMISSIONS_RESULT = 1011;

     TextView photo1,photo2;

     String mServiceCenterName;



        public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this.getActivity();
        progressDialog = new ProgressDialog(context);

        mServiceCenterName = CustomUtility.getSharedPreferences(context,"ServiceCenterName");
        // we add permissions we need to request location of the users
        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);

        permissionsToRequest = permissionsToRequest(permissions);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissionsToRequest.size() > 0) {
                requestPermissions(permissionsToRequest.
                        toArray(new String[0]), ALL_PERMISSIONS_RESULT);
            }
        }

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        if (!checkPlayServices()) {
            Toast.makeText(context, "You need to install Google Play Services to use the App properly", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        open_complaint = rootView.findViewById(R.id.open_complaint);
        txtServiceCenterID = rootView.findViewById(R.id.txtServiceCenterID);
        closer_complaint =rootView.findViewById(R.id.closer_complaint);
        pending_for_Approval = rootView.findViewById(R.id.pending_for_Approval);
        approved_complaint = rootView.findViewById(R.id.approved_complaint);
        txtServiceCenterID.setText(mServiceCenterName);

        deleteCache(context);

        open_complaint.setOnClickListener(view -> {

            WebURL.STATUS_CHECK_FOR_COMPLAIN = "01";
            Intent intent = new Intent(context, PendingComplainListActivity.class);

            intent.putExtra("complaint", "New Complaint");
            intent.putExtra("StatusValue", "01");
            startActivity(intent);
        });

        pending_for_Approval.setOnClickListener(view -> {
            WebURL.STATUS_CHECK_FOR_COMPLAIN = "02";
            Intent intent = new Intent(context, PendingComplainListActivity.class);
            intent.putExtra("complaint", "Pending for Approval");
            intent.putExtra("StatusValue", "02");
            startActivity(intent);
        });

        approved_complaint.setOnClickListener(view -> {

            WebURL.STATUS_CHECK_FOR_COMPLAIN = "03";
            Intent intent = new Intent(context, PendingComplainListActivity.class);
            intent.putExtra("complaint", "Approved Complain");
            intent.putExtra("StatusValue", "03");
            startActivity(intent);
        });

        closer_complaint.setOnClickListener(view -> {

           // getClosureComplaint();
            WebURL.STATUS_CHECK_FOR_COMPLAIN = "04";
            Intent intent = new Intent(context, PendingComplainListActivity.class);
            intent.putExtra("complaint", "Clouser Complain");
            intent.putExtra("StatusValue", "04");
            startActivity(intent);
        });

        // get unsync data count
        // Inflate the layout for this fragment
        return rootView;
    }


    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            if (children != null) {
                for (String child : children) {
                    boolean success = deleteDir(new File(dir, child));
                    if (!success) {
                        return false;
                    }
                }
            }
            return dir.delete();
        } else if(dir!= null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onResume() {

        // get unsync data count
        super.onResume();


    }


    private ArrayList<String> permissionsToRequest(ArrayList<String> wantedPermissions) {
        ArrayList<String> result = new ArrayList<>();

        for (String perm : wantedPermissions) {
            if (hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    private boolean hasPermission(String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED;
        }

        return false;
    }


    @Override
    public void onStart() {

        super.onStart();


    }

    @Override
    public void onPause() {
        super.onPause();

    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(context);

        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog((Activity) context, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST);
            } else {
                requireActivity().finish();
            }

            return false;
        }

        return true;
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }


    @Override
    public void onClick(View v) {

        int id = v.getId();
        switch (id) {

            case R.id.start:

            case R.id.end:

                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted


                } else {
                    // Permission Denied

                    getActivity().finish();
                    System.exit(0);

                }
                break;
            case ALL_PERMISSIONS_RESULT:
                for (String perm : permissionsToRequest) {
                    if (hasPermission(perm)) {
                        permissionsRejected.add(perm);
                    }
                }

                if (permissionsRejected.size() > 0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                            new AlertDialog.Builder(context).
                                    setMessage("These permissions are mandatory to get your location. You need to allow them.").
                                    setPositiveButton("OK", (dialogInterface, i) -> requestPermissions(permissionsRejected.
                                            toArray(new String[0]), ALL_PERMISSIONS_RESULT)).setNegativeButton("Cancel", null).create().show();

                            return;
                        }
                    }
                }

                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

        if ((ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                && (ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
        }

    }



    @Override
    public void onDestroy() {


        if (progressDialog!=null) {
            progressDialog.cancel();
        }
        super.onDestroy();
    }

    @Override
    public void onStop() {


        if (progressDialog!=null) {
            progressDialog.cancel();
        }
        super.onStop();
    }

    public void setIcon(String key) {

        switch (key) {

            case DatabaseHelper.KEY_PHOTO1:
                if (start_photo_text == null || start_photo_text.isEmpty()) {
                    photo1.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.red_icn, 0);
                } else {
                    photo1.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.right_mark_icn_green, 0);
                }
                break;


            case DatabaseHelper.KEY_PHOTO2:
                if (end_photo_text == null || end_photo_text.isEmpty()) {
                    photo2.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.red_icn, 0);
                } else {
                    photo2.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.right_mark_icn_green, 0);
                }
                break;


        }

    }

}