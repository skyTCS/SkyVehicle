package com.cc.skyvehicle;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    Button car_start,car_stop,car_work,car_sync;
    EditText car_name,car_energy,car_state,car_point;
    static TextView car_t;
    ButtonOnclikLister buttonOnclikLister;
    MqttManager mqttManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();
        buttonOnclikLister = new ButtonOnclikLister();
        setOnclikListener();

        mqttManager = new MqttManager(this);


    }

    private void setOnclikListener() {
        car_start.setOnClickListener(buttonOnclikLister);
        car_stop.setOnClickListener(buttonOnclikLister);
        car_work.setOnClickListener(buttonOnclikLister);
        car_sync.setOnClickListener(buttonOnclikLister);
    }


    private void findViews() {
        car_start = (Button) findViewById(R.id.car_start);
        car_stop = (Button) findViewById(R.id.car_stop);
        car_work = (Button) findViewById(R.id.car_work);
        car_sync = (Button) findViewById(R.id.car_sync);
        car_name = (EditText) findViewById(R.id.car_name);
        car_energy = (EditText) findViewById(R.id.car_energy);
        car_state = (EditText) findViewById(R.id.car_state);
        car_point = (EditText) findViewById(R.id.car_point);
        car_t = (TextView) findViewById(R.id.car_t);
    }

    private class ButtonOnclikLister implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.car_start :
                    System.out.println("--准备链接");
                    mqttManager.connect();
                    mqttManager.subscribe("order",0);
                    System.out.println("--订阅成功");
                    break;
                case R.id.car_stop :

                    break;
                case R.id.car_work :

                    break;
                case R.id.car_sync :
                    String name = car_name.getText().toString();
                    String energy = car_energy.getText().toString();
                    String state = car_state.getText().toString();
                    String point = car_point.getText().toString();
                    Map<String,String> map = new HashMap<>();
                    map.put("name",car_name.getText().toString());
                    map.put("energy",car_energy.getText().toString());
                    map.put("state",car_state.getText().toString());
                    map.put("point",car_point.getText().toString());
                    Gson gson1=new Gson();
                    String pay=gson1.toJson(map);
                    mqttManager.publish("vehicle01",pay,false,0);
                    break;
            }
        }
    }

    public static void show (String pay){
        car_t.setText(pay);
    }

    public static Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            String payload = bundle.getString("payload");
            //float pay = Float.parseFloat(payload);

            show(payload);
            //dynamicLineChartManager1.addEntry((int) (Math.random() * 100));
            //cs.setText(payload);
        }
    };
}
