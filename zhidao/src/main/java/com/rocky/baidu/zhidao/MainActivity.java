package com.rocky.baidu.zhidao;

import android.annotation.TargetApi;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.rocky.baidu.zhidao.webview.WebActivity;
import com.rocky.baidu.zhidao.wifi.WifiAdmin;


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
//        wifi  = new WifiAdmin(this);
        // 使用代理连接
//        wifi.connectConfiguration("61.174.13.12",80);

//        init_views();
        Intent intent = new Intent();
        intent.setClass(this, WebActivity.class);
        startActivity(intent);

//        text_log.setText("网络连接: " + netIsConnected());
    }

    private boolean netIsConnected() {
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        // 获取NetworkInfo对象
        NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();
        for(NetworkInfo info : networkInfo) {
            if (info.getState() == NetworkInfo.State.CONNECTED) {
                return true;
            }
        }
        return false;
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
