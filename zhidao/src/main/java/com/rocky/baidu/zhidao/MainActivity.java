package com.rocky.baidu.zhidao;

import android.annotation.TargetApi;
import android.net.ProxyInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.rocky.baidu.zhidao.wifi.WifiAdmin;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    private Button ask_button;
    private Button answer_button;
    private Button stop_button;
    private TextView text_log;

    private WifiAdmin wifi;

    private void init_views() {
        ask_button = (Button) findViewById(R.id.ask_button);
        answer_button = (Button) findViewById(R.id.answer_button);
        stop_button = (Button) findViewById(R.id.stop_button);
        text_log = (TextView) findViewById(R.id.text_log);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // WIFI连接
        wifi  = new WifiAdmin(this);
        // 使用代理连接
        wifi.connectConfiguration("61.174.13.12",80);

        init_views();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
