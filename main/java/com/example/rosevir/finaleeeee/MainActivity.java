package com.example.rosevir.finaleeeee;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.AsyncTask;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.SensorEvent;

public class MainActivity extends AppCompatActivity implements SensorEventListener{

    private Sensor Accelerometer_Sensor;
    private SensorManager SM;

    private static final String TAG = "IPCamView";
    private MjpegView mv;
    private String CameraURL;
    public static String textSourceCar, textSourceCam;

    public int newDirection = 2, currDirection=2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        textSourceCam = "http://192.168.0.102:88/cgi-bin/CGIProxy.fcgi?cmd=setSubVideoStreamType&format=1&usr=admin&pwd=admin1";
        new CamTask().execute();

        CameraURL = "http://192.168.0.102:88/cgi-bin/CGIStream.cgi?cmd=GetMJStream&usr=admin&pwd=admin1";

        mv = new MjpegView(this);
        View stolenView = mv;

        View view =(findViewById(R.id.Vid));
        ((ViewGroup) view).addView(stolenView);

        new DoRead().execute(CameraURL);

        Button btnBackMenu = (Button) findViewById(R.id.btnBackMenu);

        //Car Control
        ImageButton btnForward = (ImageButton) findViewById(R.id.btnForward);
        ImageButton btnBackward = (ImageButton) findViewById(R.id.btnBackward);

        //Camera Control
        ImageButton btnCamUp = (ImageButton) findViewById(R.id.btnCamUp);
        ImageButton btnCamDown = (ImageButton) findViewById(R.id.btnCamDown);
        ImageButton btnCamLeft = (ImageButton) findViewById(R.id.btnCamLeft);
        ImageButton btnCamRight = (ImageButton) findViewById(R.id.btnCamRight);
        ImageButton btnCamUpLeft = (ImageButton) findViewById(R.id.btnCamUpLeft);
        ImageButton btnCamCenter = (ImageButton) findViewById(R.id.btnCamCenter);
        ImageButton btnCamUpRight = (ImageButton) findViewById(R.id.btnCamUpRight);
        ImageButton btnCamDownLeft = (ImageButton) findViewById(R.id.btnCamDownLeft);
        ImageButton btnCamDownRight = (ImageButton) findViewById(R.id.btnCamDownRight);

        final TextView txtCarDirection = (TextView) findViewById(R.id.txtCarDirection);
        final TextView txtCameraDirection = (TextView) findViewById(R.id.txtCameraDirection);

        btnBackMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, StartUpActivity.class);
                MainActivity.this.startActivity(myIntent);
            }
        });

        // Car Control Execution
        btnForward.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    textSourceCar = "http://192.168.0.14/?forward";
                    txtCarDirection.setText("Car: FORWARD");
                }
                else if(event.getAction() == MotionEvent.ACTION_UP){
                    textSourceCar = "http://192.168.0.14/?stop";
                    txtCarDirection.setText("Car: FREE WHEEL");
                }
               new CarTask().execute();
                return true;
            }
        });

        btnBackward.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    textSourceCar = "http://192.168.0.14/?backward";
                    txtCarDirection.setText("Car: BACKWARD");
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    textSourceCar = "http://192.168.0.14/?stop";
                    txtCarDirection.setText("Car: FREE WHEEL");
                }
                new CarTask().execute();
                return true;
            }
        });

        //Camera Control Execution
        btnCamUp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    textSourceCam = "http://192.168.0.102:88/cgi-bin/CGIProxy.fcgi?cmd=ptzMoveUp&usr=admin&pwd=admin1";
                    txtCameraDirection.setText("UP");
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    textSourceCam = "http://192.168.0.102:88/cgi-bin/CGIProxy.fcgi?cmd=ptzStopRun&usr=admin&pwd=admin1";
                    txtCameraDirection.setText("STOP");
                }
                new CamTask().execute();
                return true;
            }
        });

        btnCamDown.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    textSourceCam = "http://192.168.0.102:88/cgi-bin/CGIProxy.fcgi?cmd=ptzMoveDown&usr=admin&pwd=admin1";
                    txtCameraDirection.setText("DOWN");
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    textSourceCam = "http://192.168.0.102:88/cgi-bin/CGIProxy.fcgi?cmd=ptzStopRun&usr=admin&pwd=admin1";
                    txtCameraDirection.setText("STOP");
                }
                new CamTask().execute();
                return true;
            }
        });

        btnCamLeft.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    textSourceCam = "http://192.168.0.102:88/cgi-bin/CGIProxy.fcgi?cmd=ptzMoveLeft&usr=admin&pwd=admin1";
                    txtCameraDirection.setText("LEFT");
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    textSourceCam = "http://192.168.0.102:88/cgi-bin/CGIProxy.fcgi?cmd=ptzStopRun&usr=admin&pwd=admin1";
                    txtCameraDirection.setText("STOP");
                }
                new CamTask().execute();
                return true;
            }
        });

        btnCamRight.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    textSourceCam = "http://192.168.0.102:88/cgi-bin/CGIProxy.fcgi?cmd=ptzMoveRight&usr=admin&pwd=admin1";
                    txtCameraDirection.setText("RIGHT");
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    textSourceCam = "http://192.168.0.102:88/cgi-bin/CGIProxy.fcgi?cmd=ptzStopRun&usr=admin&pwd=admin1";
                    txtCameraDirection.setText("STOP");
                }
                new CamTask().execute();
                return true;
            }
        });

        btnCamUpLeft.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    textSourceCam = "http://192.168.0.102:88/cgi-bin/CGIProxy.fcgi?cmd=ptzMoveTopLeft&usr=admin&pwd=admin1";
                    txtCameraDirection.setText("UP LEFT");
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    textSourceCam = "http://192.168.0.102:88/cgi-bin/CGIProxy.fcgi?cmd=ptzStopRun&usr=admin&pwd=admin1";
                    txtCameraDirection.setText("STOP");
                }
                new CamTask().execute();
                return true;
            }
        });

        btnCamUpRight.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    textSourceCam = "http://192.168.0.102:88/cgi-bin/CGIProxy.fcgi?cmd=ptzMoveTopRight&usr=admin&pwd=admin1";
                    txtCameraDirection.setText("UP RIGHT");
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    textSourceCam = "http://192.168.0.102:88/cgi-bin/CGIProxy.fcgi?cmd=ptzStopRun&usr=admin&pwd=admin1";
                    txtCameraDirection.setText("STOP");
                }
                new CamTask().execute();
                return true;
            }
        });

        btnCamDownLeft.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    textSourceCam = "http://192.168.0.102:88/cgi-bin/CGIProxy.fcgi?cmd=ptzMoveBottomLeft&usr=admin&pwd=admin1";
                    txtCameraDirection.setText("DOWN LEFT");
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    textSourceCam = "http://192.168.0.102:88/cgi-bin/CGIProxy.fcgi?cmd=ptzStopRun&usr=admin&pwd=admin1";
                    txtCameraDirection.setText("STOP");
                }
                new CamTask().execute();
                return true;
            }
        });

        btnCamDownRight.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    textSourceCam = "http://192.168.0.102:88/cgi-bin/CGIProxy.fcgi?cmd=ptzMoveBottomRight&usr=admin&pwd=admin1";
                    txtCameraDirection.setText("DOWN RIGHT");
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    textSourceCam = "http://192.168.0.102:88/cgi-bin/CGIProxy.fcgi?cmd=ptzStopRun&usr=admin&pwd=admin1";
                    txtCameraDirection.setText("STOP");
                }
                new CamTask().execute();
                return true;
            }
        });

        btnCamCenter.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    textSourceCam = "http://192.168.0.102:88/cgi-bin/CGIProxy.fcgi?cmd=ptzReset&usr=admin&pwd=admin1";
                    txtCameraDirection.setText("Center");
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    //do nothing
                }
                new CamTask().execute();
                return true;
            }
        });

        SM = (SensorManager)getSystemService(SENSOR_SERVICE); // Create the Sensor Manager
        Accelerometer_Sensor = SM.getDefaultSensor(Sensor.TYPE_ACCELEROMETER); // Accelerometer Sensor
        SM.registerListener(this, Accelerometer_Sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onResume() {
        SM = (SensorManager) (getSystemService(SENSOR_SERVICE));
        SM.registerListener(this, SM.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        super.onResume();
        mv.startPlayback();
    }

    @Override
    public void onPause() {
        super.onPause();
        SM.unregisterListener(this);
        mv.stopPlayback();
    }

    @Override
    protected void onStop() {
        super.onStop();
        SM.unregisterListener(this);
    }

    public class DoRead extends AsyncTask<String, Void, MjpegInputStream> {
        protected MjpegInputStream doInBackground(String... url) {
            //TODO: if camera has authentication deal with it and don't just not work
            HttpResponse res = null;
            DefaultHttpClient httpclient = new DefaultHttpClient();
            Log.d(TAG, "1. Sending http request");
            try {
                res = httpclient.execute(new HttpGet(URI.create(url[0])));
                Log.d(TAG, "2. Request finished, status = " + res.getStatusLine().getStatusCode());
                if(res.getStatusLine().getStatusCode()==401){
                    //You must turn off camera User Access Control before this will work
                    return null;
                }
                return new MjpegInputStream(res.getEntity().getContent());
            } catch (ClientProtocolException e) {
                e.printStackTrace();
                Log.d(TAG, "Request failed-ClientProtocolException", e);
                //Error connecting to camera
            } catch (IOException e) {
                e.printStackTrace();
                Log.d(TAG, "Request failed-IOException", e);
                //Error connecting to camera
            }
            return null;
        }

        protected void onPostExecute(MjpegInputStream result) {
            mv.setSource(result);
            mv.setDisplayMode(MjpegView.SIZE_FULLSCREEN);   //SIZE_BEST_FIT
            mv.showFps(true);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.values[1] < -2){
            textSourceCar = "http://192.168.0.14/?left";
            newDirection = 1;
        }else if(event.values[1] > 2){
            textSourceCar = "http://192.168.0.14/?right";
            newDirection = 3;
        }else{
            textSourceCar = "http://192.168.0.14/?straight";
            newDirection = 2;
        }

        if(currDirection != newDirection){
            new CarTask().execute();
            currDirection = newDirection;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private class CarTask extends AsyncTask<Void,Void,Void>{
        String textResult;
        String textSource_myTask = new MainActivity().textSourceCar;

        @Override
        protected Void doInBackground(Void... params) {
            URL textUrl;

            try {
                textUrl = new URL(textSource_myTask);
                System.out.println(textUrl);
                BufferedReader bufferReader = new BufferedReader(
                        new InputStreamReader(textUrl.openStream()));

                String stringBuffer;
                String stringText = "";
                while((stringBuffer = bufferReader.readLine()) != null) {
                    stringText += stringBuffer;
                }

                bufferReader.close();
                textResult = stringText;

            } catch(MalformedURLException e) {
                e.printStackTrace();
                textResult = e.toString();
            } catch(IOException e) {
                e.printStackTrace();
                textResult = e.toString();
            }
            return null;
        }
    }

    private class CamTask extends AsyncTask<Void,Void,Void>{
        String textResult;
        String textSource_myTask = new MainActivity().textSourceCam;

        @Override
        protected Void doInBackground(Void... params) {
            URL textUrl;

            try {
                textUrl = new URL(textSource_myTask);
                System.out.println(textUrl);
                BufferedReader bufferReader = new BufferedReader(
                        new InputStreamReader(textUrl.openStream()));

                String stringBuffer;
                String stringText = "";
                while((stringBuffer = bufferReader.readLine()) != null) {
                    stringText += stringBuffer;
                }

                bufferReader.close();
                textResult = stringText;

            } catch(MalformedURLException e) {
                e.printStackTrace();
                textResult = e.toString();
            } catch(IOException e) {
                e.printStackTrace();
                textResult = e.toString();
            }
            return null;
        }
    }
}
