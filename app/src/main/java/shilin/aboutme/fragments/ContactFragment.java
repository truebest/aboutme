package shilin.aboutme.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.protocol.HTTP;
import shilin.aboutme.R;
import shilin.aboutme.datas.AboutDataProvider;

/**
 * Created by beerko on 09.05.16.
 */
public class ContactFragment extends Fragment {
    Button button;
    View view;
    CoordinatorLayout mRoot;
    AboutDataProvider aboutDataProvider;
    DatePickerDialog picker;


    private boolean checkFillFields(){
        final EditText name = (EditText) view.findViewById(R.id.et_name);
        final EditText email = (EditText) view.findViewById(R.id.et_email);
        final EditText phone = (EditText) view.findViewById(R.id.et_phone);
        final EditText message = (EditText) view.findViewById(R.id.et_message);
        final EditText birthDate = (EditText) view.findViewById(R.id.et_age);

        if ((name.length() > 0) && (email.length() > 0) && (phone.length() > 0) && (message.length() > 0) && (birthDate.length() > 0)) return true;
        return false;
    }

    private void sendData(){

        aboutDataProvider.updateState();
        final EditText name = (EditText) view.findViewById(R.id.et_name);
        final EditText email = (EditText) view.findViewById(R.id.et_email);
        final EditText phone = (EditText) view.findViewById(R.id.et_phone);
        final EditText message = (EditText) view.findViewById(R.id.et_message);
        final EditText birthDate = (EditText) view.findViewById(R.id.et_age);

        final String macAddress = aboutDataProvider.getMacAddress();
        final String android_id = aboutDataProvider.getAndroidId();
        final String imei = aboutDataProvider.getImei();
        final String model = aboutDataProvider.getDeviceName();
        final double longitude = aboutDataProvider.getLon();
        final double latitude = aboutDataProvider.getLat();

        button.setEnabled(false);
        JSONObject json = new JSONObject();
        try {
            json.accumulate("user",name.getText().toString());
            json.accumulate("birth",birthDate.getText().toString());
            json.accumulate("email",email.getText().toString());
            json.accumulate("phone",phone.getText().toString());
            json.accumulate("model",model);
            json.accumulate("mac",macAddress);
            json.accumulate("android",android_id);
            json.accumulate("imei",imei);
            json.accumulate("lat", latitude);
            json.accumulate("lon", longitude);
            json.accumulate("message",message.getText().toString());
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            invokeWS(json);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
/////

    private void hideKeyboard(){
        view.clearFocus();
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(),0);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_contact, null);
        button = (Button) view.findViewById(R.id.button_send);
        mRoot = (CoordinatorLayout) view.findViewById(R.id.coord);
        aboutDataProvider = new AboutDataProvider(getContext());
        final EditText age = (EditText) view.findViewById(R.id.et_age);
        age.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                hideKeyboard();
                picker = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        age.setText(dayOfMonth + "." + monthOfYear + "." + year);
                    }
                }, year,month,day);
                if (hasFocus)
                picker.show();
            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkFillFields()) {
                    AlertDialog.Builder dlg = new AlertDialog.Builder(getContext());
                    dlg.setTitle(R.string.commit_send);
                    dlg.setMessage(R.string.are_you_ready);

                    dlg.setPositiveButton(R.string.dlg_btn_ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            sendData();
                        }
                    });

                    dlg.setNegativeButton(R.string.dlg_btn_cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                    AlertDialog dialog = dlg.create();
                    dialog.show();
                }else {
                    Toast.makeText(getActivity(),R.string.warn_fill_fields,Toast.LENGTH_SHORT).show();
                }

            }
        });

        return view;
    }



    private void invokeWS(JSONObject jsonObject) throws UnsupportedEncodingException {
        StringEntity entity = new StringEntity(jsonObject.toString(), HTTP.UTF_8);
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(getActivity(),"http://54.187.155.28:8888/postMessage",entity,"application/json",new JsonHttpResponseHandler() {
        //client.post(getActivity(),"http://176.53.244.82:8888/postMessage",entity,"application/json",new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.d("...","Успешный успех JSON");
                button.setEnabled(true);

                Snackbar snackbar = Snackbar.make(mRoot, R.string.send_ok, Snackbar.LENGTH_LONG);
                snackbar.show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                button.setEnabled(true);
                Snackbar snackbar = Snackbar.make(mRoot, R.string.send_fail, Snackbar.LENGTH_INDEFINITE).setAction(R.string.snack_retry, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendData();
                    }
                });
                snackbar.show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Snackbar snackbar = Snackbar.make(mRoot, R.string.send_fail, Snackbar.LENGTH_INDEFINITE).setAction(R.string.snack_retry, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendData();
                    }
                });
                snackbar.show();
            }
        });
    }
}
