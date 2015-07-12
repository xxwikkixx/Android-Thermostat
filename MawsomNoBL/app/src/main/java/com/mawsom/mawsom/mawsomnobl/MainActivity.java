package com.mawsom.mawsom.mawsomnobl;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.mawsom.mawsom.mawsomnobl.controls.HomeSchedule;
import com.mawsom.mawsom.mawsomnobl.data.Channel;
import com.mawsom.mawsom.mawsomnobl.data.Conditions;
import com.mawsom.mawsom.mawsomnobl.data.Item;
import com.mawsom.mawsom.mawsomnobl.data.Schedules;
import com.mawsom.mawsom.mawsomnobl.data.Servers;
import com.mawsom.mawsom.mawsomnobl.service.WeatherServiceCallBack;
import com.mawsom.mawsom.mawsomnobl.service.YahooWeatherService;
import com.mawsom.mawsom.mawsomnobl.utils.Utils;

/**
 * Created by Waqas
 */

public class MainActivity extends Activity implements OnClickListener, WeatherServiceCallBack {

    private static final String TAG = "MainActivity";
    private int mMaxChars = 50000;//Default
    private UUID mDeviceUUID;
    private BluetoothSocket mBTSocket;
   //private ReadInput mReadThread = null;
   //private SendOut mSendThread = null;

    private boolean mIsUserInitiatedDisconnect = false;
    private boolean mIsBluetoothConnected = false;
    private BluetoothDevice mDevice;
    private ProgressDialog progressDialog;

    Handler refreshHandler;

    Timer conditionsTimer;
    Timer settingsTimer;

    String previousConditionsJson = "";
    String previousSettingsJson = "";
    //String previousDebugText = "";
    String previousDisplayTime = "";
    private static final int ACTIVITY_SETTEMP=100;
    private static final int ACTIVITY_SCHEDULE=101;
    private static final int ACTIVITY_SETTINGS=102;
    private static final int ACTIVITY_SELECT_SERVER=103;

    SimpleDateFormat formatter = new SimpleDateFormat("h:mma");

    //buttons
    Button butUp;
    Button butDown;
    Button butConnectBlue;

    //text
    TextView textUser;
    private TextView mTxtReceive;

    //data from the arduino stored
    int targetTemp = 70;
    int RoomTemp;
    int maxTemp = 88;
    int minTemp = 46;
    //char degree = '\u00B0'; //degree sign for the temperature
    String data;

    public static final int MESSAGE_READ = 1;

    BluetoothAdapter btAdapter = null;
    BluetoothSocket btSocket = null;
    BluetoothServerSocket ServerSocket;
    OutputStream outStream = null;
    InputStream inStream = null;

    private ImageView weatherIconImageView;
    private TextView OutTempView;
    private TextView cityTextView;
    private YahooWeatherService service;
    private ProgressDialog dialog;

    //client.thermostat.client
    TextView insideTempText;
    TextView outsideTempText;
    TextView targetTempText;
    //TextView debugText;
    TextView currentTime;
    ImageView weatherImage;
    Button settingsButton;
    ImageView serverButton;
    LinearLayout screenLayout;
    HomeSchedule homeSchedule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        //insideTempText  = (TextView) findViewById(R.id.insideTempText);
        //outsideTempText  = (TextView) findViewById(R.id.outsideTempText);
        //targetTempText  = (TextView) findViewById(R.id.targetTempText);
        //debugText  = (TextView) findViewById(R.id.debugText);
        //weatherImage = (ImageView) findViewById(R.id.weatherImage);
        settingsButton = (Button) findViewById(R.id.settingsButton);
        //serverButton = (ImageView) findViewById(R.id.serverButton);
        screenLayout = (LinearLayout) findViewById(R.id.screenLayout);
        //homeSchedule = (HomeSchedule) findViewById(R.id.homeSchedule);

        //Intent intent = getIntent();
        //Bundle b = intent.getExtras();
        //mDevice = b.getParcelable(BluetoothActivity.DEVICE_EXTRA);
        //mDeviceUUID = UUID.fromString(b.getString(BluetoothActivity.DEVICE_UUID));
        //mMaxChars = b.getInt(BluetoothActivity.BUFFER_SIZE);

        //finding widgets in the activity
        butUp = (Button) findViewById(R.id.butUp);
        butDown = (Button) findViewById(R.id.butDown);
        butConnectBlue = (Button) findViewById(R.id.butConnectBlue);

        //toggle buttons
        //Toggle toggleCool = (ToggleButton) findViewById(R.id.toggleCool);
        //Toggle toggleHeat= (ToggleButton) findViewById(R.id.toggleHeat);

        //text view to show the tempreature
        mTxtReceive = (TextView) findViewById(R.id.textRoomTemp);
        textUser = (TextView) findViewById(R.id.textUserTemp);

        textUser.setText(targetTemp + "\u00B0");

        //initilizing the bluetooth adapter
        //btAdapter = BluetoothAdapter.getDefaultAdapter();

        //checking bluetooth state
        //checkBTState();

        weatherIconImageView = (ImageView) findViewById(R.id.weatherIcon);
        OutTempView = (TextView) findViewById(R.id.textOutTemp);
        cityTextView = (TextView) findViewById(R.id.textCIty);
        service = new YahooWeatherService(this);
        //dialog.setMessage("Loading...");
        //need to send in location to this service by geolocation in android service
        service.refreshWeather("San Diego, CA");

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Servers.load(this);

        Utils.debugText = "Loading";
        initScreen();

        new Thread(new Runnable() {
            public void run() {
                Schedules.load();
                Settings.load();
            }
        }).start();

        refreshHandler= new Handler();
        refreshHandler.postDelayed(refreshRunnable, 1000);

        conditionsTimer = new Timer();
        conditionsTimer.schedule(new ConditionsTimerTask(), 1000, 3000);

        settingsTimer = new Timer();
        settingsTimer.schedule(new SettingsTimerTask(), 1200, 5000);

        //settings button to start settings activity
        settingsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                showSettings();
            }
        });

        //button Up to send data to arduino
        butUp.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if(targetTemp != RoomTemp){
                    //data = "2";
                }
                else{
                    //data = "2";
                }
            }
        });

        butUp.setOnTouchListener(new OnTouchListener(){
            public boolean onTouch(View v, MotionEvent event){
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    butUp.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFAA0000));
                    targetTemp++;
                    textUser.setText(targetTemp + "\u00B0");
                    if(targetTemp > maxTemp){
                        targetTemp = maxTemp;
                    }
                }
                else if(event.getAction() == MotionEvent.ACTION_UP){
                    butUp.getBackground().setColorFilter(null);
                }
                return true;
            }
        });

        //button down to send data to arduino
        butDown.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {

                if(targetTemp != RoomTemp){
                    //
                }
                else{
                    //
                }
            }
        });

        butDown.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event){
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    butDown.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xff0000ff));
                    targetTemp--;
                    textUser.setText(targetTemp + "\u00B0");
                    if(targetTemp < minTemp){
                        targetTemp = minTemp;
                    }
                }
                else if(event.getAction() == MotionEvent.ACTION_UP){
                    butDown.getBackground().setColorFilter(null);
                }
                return true;
            }
        });
    }

    private void showSettings(){
        Intent i = new Intent(this, SettingsActivity.class);
        startActivityForResult(i, ACTIVITY_SETTINGS);
    }

    //error for bluetooth
    private void errorExit(String title, String message){
        Toast.makeText(getBaseContext(), title + " - " + message, Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menue_main, menu);
        return true;
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (v == butUp){
            butUp.setBackgroundResource(R.drawable.arrowup);
        }
        if (v == butDown){
            butDown.setBackgroundResource(R.drawable.arrowdown);
        }
    }

    @Override
    protected void onResume() {
        /*if (mBTSocket == null || !mIsBluetoothConnected) {
            new ConnectBT().execute();
            //need to setup sending strings again once the activity resumes for bluetooth
        }*/
        Log.d(TAG, "Resumed");
        super.onResume();
        updateScreen(true);
    }

    @Override
    public void serviceSucess(Channel channel) {
        //dialog.hide();
        Item item = channel.getItem();
        int resourceId = getResources().getIdentifier("drawable/icon_" + item.getCondition().getCode(), null, getPackageName());
        @SuppressWarnings("deprecation")
        Drawable weatherIconDrawble = getResources().getDrawable(resourceId);
        weatherIconImageView.setImageDrawable(weatherIconDrawble);
        OutTempView.setText(item.getCondition().getTemperature() + "\u00B0" + channel.getUnits().getTemperature());
        cityTextView.setText(service.getLocation());
    }

    @Override
    public void serviceFailure(Exception exception) {
        //Toast.makeText(this, exception.getMessage(), Toast.LENGTH_LONG).show();
    }

    public void showWeatherDetails()
    {
        try{
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(Conditions.getCurrent().getWeatherForecastUrl()));
            startActivity(i);
        } catch (Exception ex) {}
    }

    private void updateSettings()
    {
        Settings.load();
    }

    public void initScreen()
    {
        insideTempText.setText( "" );
        outsideTempText.setText( "" );
        targetTempText.setText( "Not Connected to Server" );
        //debugText.setText(Utils.debugText);
    }

    public void updateScreen(boolean forceRefresh)
    {

        Conditions conditions = Conditions.getCurrent();
        Settings settings = Settings.getCurrent();

        //Updating these fields every second creates unnecessary processor usage.  Only update the fields
        //if the values have changed.

        if (forceRefresh || !conditions.getJson().equals(previousConditionsJson) || !settings.getJson().equals(previousSettingsJson))
        {

            //insideTempText.setText( String.valueOf(conditions.getInsideTemperature()) + "° F" );
            //outsideTempText.setText( String.valueOf(conditions.getOutsideTemperature()) + "° F" );
            insideTempText.setText( conditions.getDisplayInsideTemperature() );
            outsideTempText.setText( conditions.getDisplayOutsideTemperature() );

            targetTempText.setText(settings.getSummary());
            if (conditions.getWeatherImage()!=null) weatherImage.setImageBitmap(conditions.getWeatherImage());

            previousConditionsJson = conditions.getJson();
            previousSettingsJson = settings.getJson();
            //previousDebugText = Utils.debugText;
            homeSchedule.refresh();

            if (conditions.getState().equals("Cool"))
            {
                screenLayout.setBackgroundResource(R.drawable.background_blue);
            } else if (conditions.getState().equals("Heat"))
            {
                screenLayout.setBackgroundResource(R.drawable.background_red);
            } else
            {
                screenLayout.setBackgroundResource(R.drawable.background_black);
            }


        }


        String displayTime = formatter.format(new Date()).toLowerCase().replace("m", "");
        if (!displayTime.equals(previousDisplayTime))
        {
            currentTime.setText(displayTime);
            previousDisplayTime=displayTime;
        }

    }

    class SettingsTimerTask extends TimerTask {
        @Override
        public void run() {
            updateSettings();
        }
    };

    class ConditionsTimerTask extends TimerTask {
        @Override
        public void run() {
            Conditions.getCurrent().load();
        }
    };

    private Runnable refreshRunnable = new Runnable() {
        public void run() {
            try {
                updateScreen(false);
            } catch (Exception e) {}
            refreshHandler.postDelayed(this, 1000);
        }
    };

/*
    //Starts bluetooth activity to connect to arduino
    public void ConnectBlue(View view)
    {
        //Intent Bluebutton = new Intent(MainActivity.this, BluetoothActivity.class);
        //finish(); //stops the main activity and starts the bluetooth activity
        //startActivity(Bluebutton);
    }

    //checking if bluetooth is on or off
    private void checkBTState() {
        // Check for Bluetooth support and then check to make sure it is turned on
        if(btAdapter==null) {
            errorExit("Error", "Bluetooth not support");
        } else {
            if (btAdapter.isEnabled()) {
                Log.d(TAG, "...Bluetooth ON...");
            } else {
                //Prompt user to turn on Bluetooth
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);
            }
        }
    }
*/
    /*
     * Sending the strings out to arduino via bluetooth
     *
     * still need to setup up string changing while doing the tempcheck.
     * run it in a thread?
     *
     */
   /* public class SendOut implements Runnable{

        private boolean sStop = false;
        private Thread I;

         String temp = "t";

        public SendOut(){
            I = new Thread(this, "Output Thread");
            I.start();
        }

        public boolean isRunning(){
            return I.isAlive();
        }

        @Override
        public void run() {
            // TODO Auto-generated method stub

            try {
                while(true){
                    mBTSocket.getOutputStream().write(temp.getBytes());
                    Thread.sleep(5000);
                    mBTSocket.getOutputStream().write(data.getBytes());
                }

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        public void stop() {
            // TODO Auto-generated method stub
            sStop = true;
        }
    }
*/

    /*
     * Reading input from the arduino string.
     * should have multiple strings to recieve multiple inputs.
     *
     */
  /*  public class ReadInput implements Runnable {

        private boolean bStop = false;
        private Thread t;

        public ReadInput() {
            t = new Thread(this, "Input Thread");
            t.start();
        }

        public boolean isRunning() {
            return t.isAlive();
        }

        @Override
        public void run() {

            InputStream inputStream;

            try {
                inputStream = mBTSocket.getInputStream();
                while (!bStop) {
                    byte[] buffer = new byte[1024];
                    if (inputStream.available() > 0) {
                        inputStream.read(buffer);
                        int i = 0;

                        for (i = 0; i < buffer.length && buffer[i] != 0; i++) {
                        }
                        final String strInput = new String(buffer, 0, i);
                        mTxtReceive.post(new Runnable() {
                            @Override
                            public void run() {
                                RoomTemp = Integer.parseInt(strInput); //put it in an array and update it for average temperature
                                mTxtReceive.setText(strInput + "\u00B0");
                                //mTxtReceive.setText(RoomTemp + "\u00B0");
                            }
                        });
                    }
                    //Thread.sleep(500);
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        public void stop() {
            bStop = true;
        }
    }
*/

    /*
     * Disconnecting bluetooth connection and stopping the threads.
     *
     */
 /*   public class DisConnectBT extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Void doInBackground(Void... params) {

            if (mReadThread != null) {
                mReadThread.stop();
                while (mReadThread.isRunning())
                    ; // Wait until it stops
                mReadThread = null;

            }
            if(mSendThread != null){
                mSendThread.stop();
                while(mSendThread.isRunning());
                mSendThread = null;

            }

            try {
                mBTSocket.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            mIsBluetoothConnected = false;
            if (mIsUserInitiatedDisconnect) {
                finish();
            }
        }

    }

    private void msg(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause() {
        if (mBTSocket != null && mIsBluetoothConnected) {
            new DisConnectBT().execute();

            //need to setup sending strings again once the activity resumes.
        }
        Log.d(TAG, "Paused");
        super.onPause();
    }



    @Override
    protected void onStop() {
        Log.d(TAG, "Stopped");
        super.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        super.onSaveInstanceState(outState);
    }

    public class ConnectBT extends AsyncTask<Void, Void, Void> {
        private boolean mConnectSuccessful = true;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(MainActivity.this, "", "Connecting");
        }

        @Override
        protected Void doInBackground(Void... devices) {

            try {
                if (mBTSocket == null || !mIsBluetoothConnected) {
                    mBTSocket = mDevice.createInsecureRfcommSocketToServiceRecord(mDeviceUUID);
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    mBTSocket.connect();
                }
            } catch (IOException e) {
                // Unable to connect to device
                e.printStackTrace();
                mConnectSuccessful = false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if (!mConnectSuccessful) {
                Toast.makeText(getApplicationContext(), "Could not connect to device.", Toast.LENGTH_LONG).show();
                finish();
            } else {
                msg("Connected to device");
                mIsBluetoothConnected = true;
                mReadThread = new ReadInput();
                mSendThread = new SendOut();
            }
            progressDialog.dismiss();
        }
    } */
}