package com.activeandroiddemo;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.apache.http.client.UserTokenHandler;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.List;

import model.WeatherDetailsTable;


public class MainActivity extends Activity implements View.OnClickListener {

    public static final String API_KEY = "7253c05e938f06aca10a8a26b2027685";
    public static final int NUMBER_OF_DAYS = 1;
    public static char DEGREE =  0x00B0;

    EditText etCityName;
    Button btnDownload;
    String mWeatherURL;
    TextView tvMain,tvDesc,tvDayTemp,tvMinTemp,tvMaxTemp;
    boolean isNetworkAvailable = false;
    ConnectionDetector mConnectionDetector;
    Dialog PleasWaitDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();
        createDialog();

        mConnectionDetector = new ConnectionDetector(getApplicationContext());

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    private void initUI(){
        etCityName = (EditText) findViewById(R.id.etcityname);
        btnDownload = (Button) findViewById(R.id.btndownload);
        tvDayTemp = (TextView) findViewById(R.id.tvdaytemp);
        tvMain = (TextView) findViewById(R.id.tvmain);
        tvDesc = (TextView) findViewById(R.id.tvdesc);
        tvMaxTemp = (TextView) findViewById(R.id.tvmaxtemp);
        tvMinTemp = (TextView) findViewById(R.id.tvmintemp);

        btnDownload.setOnClickListener(this);
    }

    private void prepareURL(String cityname) {
        mWeatherURL = "http://api.openweathermap.org/data/2.5/forecast/daily?q=" + cityname + "&cnt="+ NUMBER_OF_DAYS + "&APPID=" + API_KEY;
    }

    private void downloadWeatherData(String url) {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                PleasWaitDialog.hide();
                SaveWeatherData(jsonObject);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                Toast.makeText(MainActivity.this,""+volleyError,Toast.LENGTH_SHORT).show();
            }
        });

        ApplicationController.getInstance().addToRequestQueue(jsonObjectRequest);
    }


    private void SaveWeatherData(JSONObject weatherResponse){

            try{

                int cityID = weatherResponse.getJSONObject("city").optInt("id");
                String cityName = weatherResponse.getJSONObject("city").optString("name");

                JSONArray list = weatherResponse.getJSONArray("list");

                for(int i=0;i<list.length();i++){

                    WeatherDetailsTable weatherDetailsTable = new WeatherDetailsTable(); // creating new row 

                    weatherDetailsTable._cityID = cityID;
                    weatherDetailsTable._cityName = cityName;
                    weatherDetailsTable._date = Utils.getDate(list.getJSONObject(i).optLong("dt"));
                    weatherDetailsTable._dayTemp = Utils.KelvinToCelsius(list.getJSONObject(i).optJSONObject("temp").optDouble("day"));
                    weatherDetailsTable._minTemp = Utils.KelvinToCelsius(list.getJSONObject(i).optJSONObject("temp").optDouble("min"));
                    weatherDetailsTable._maxTemp = Utils.KelvinToCelsius(list.getJSONObject(i).optJSONObject("temp").optDouble("max"));

                    JSONArray weatherDetailsArray = list.getJSONObject(i).optJSONArray("weather");

                    for(int j=0;j<weatherDetailsArray.length();j++){

                        weatherDetailsTable._mainWeather = weatherDetailsArray.getJSONObject(j).optString("main");
                        weatherDetailsTable._desc = weatherDetailsArray.getJSONObject(j).optString("description");
                    }
                    weatherDetailsTable.save(); // saving the data 
                    displayData();
                }

            }catch (Exception e){
                e.printStackTrace();
            }
    }

    private void createDialog(){

        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        final DisplayMetrics displayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        int dialogHeight = (int) (displayMetrics.heightPixels*0.20);
        int dialogWidth =  (int) (displayMetrics.widthPixels*0.90);

        PleasWaitDialog = new Dialog(MainActivity.this);
        PleasWaitDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        PleasWaitDialog.setContentView(R.layout.please_wait_dialog);
        PleasWaitDialog.getWindow().setLayout(dialogWidth,dialogHeight);
        PleasWaitDialog.setCanceledOnTouchOutside(false);
    }

    private void displayData(){

        String cityName = etCityName.getText().toString();
        WeatherDetailsTable weatherDetails = WeatherDetailsTable.getWeatherDetailsForGivenCity(cityName);
        tvMain.setText("Main : "+weatherDetails._mainWeather);
        tvDesc.setText("Description : "+weatherDetails._desc);
        tvDayTemp.setText("Day Temperature : "+weatherDetails._dayTemp+ DEGREE + "C");
        tvMaxTemp.setText("Max. Temperature : "+weatherDetails._maxTemp+ DEGREE + "C");
        tvMinTemp.setText("Min. Temperature : "+weatherDetails._minTemp+ DEGREE + "C");
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.btndownload:

                isNetworkAvailable = mConnectionDetector.GetNetworkInfo();
                String cityName = etCityName.getText().toString();

                if(cityName.length()>0){

                    if(isNetworkAvailable){

                        PleasWaitDialog.show();
                        prepareURL(cityName);
                        downloadWeatherData(mWeatherURL);

                    }else{
                        Toast.makeText(MainActivity.this,"Network not available",Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(MainActivity.this,"Please enter city name",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
