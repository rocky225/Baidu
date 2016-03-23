package com.rocky.baidu.zhidao.wifi;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.ProxyInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * WIFI连接类
 *
 * Created by Administrator on 2016/3/22.
 */
public class WifiAdmin {

    private Context context;

    // 定义WifiManager对象
    private WifiManager mWifiManager;
    // 定义WifiInfo对象
    private WifiInfo mWifiInfo;
    // 扫描出的网络连接列表
    private List<ScanResult> mWifiList;
    // 网络连接列表
    private List<WifiConfiguration> mWifiConfiguration;
    // 定义一个WifiLock
    WifiManager.WifiLock mWifiLock;

    public WifiAdmin(Context context) {
        this.context = context;
        // 取得WifiManager对象
        mWifiManager = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        // 取得WifiInfo对象
        mWifiInfo = mWifiManager.getConnectionInfo();
    }

    /**
     * 打开WIFI
     */

    public void openWifi() {
        if (!mWifiManager.isWifiEnabled()) {
            mWifiManager.setWifiEnabled(true);
        }
    }

    /**
     * 关闭WIFI
     */
    public void closeWifi() {
        if (mWifiManager.isWifiEnabled()) {
            mWifiManager.setWifiEnabled(false);
        }
    }

    /**
     * 检查当前WIFI状态
     *
     * @return
     */
    public int checkState() {
        return mWifiManager.getWifiState();
    }

    /**
     * 锁定WifiLock
     */
    public void acquireWifiLock() {
        mWifiLock.acquire();
    }

    /**
     * 解锁WifiLock
     */
    public void releaseWifiLock() {
        // 判断时候锁定
        if (mWifiLock.isHeld()) {
            mWifiLock.acquire();
        }
    }

    /**
     * 创建一个WifiLock
     */
    public void creatWifiLock() {
        mWifiLock = mWifiManager.createWifiLock("Test");
    }

    /**
     * 得到配置好的网络
     *
     * @return
     */
    public List<WifiConfiguration> getConfiguration() {

        if(mWifiConfiguration != null && mWifiConfiguration.size() > 0) {
            return mWifiConfiguration;
        }
        return mWifiManager.getConfiguredNetworks();
    }

    /**
     * 指定配置好的网络进行连接
     *
     * @param index
     */
    public void connectConfiguration(int index) {
        // 索引大于配置好的网络索引返回
        if (index > mWifiConfiguration.size()) {
            return;
        }
        // 连接配置好的指定ID的网络
        mWifiManager.enableNetwork(mWifiConfiguration.get(index).networkId,
                true);
    }

    /**
     * 指定配置好的网络进行连接
     *
     */

    /**
     * 使用代理进行连接
     *
     * @param host IP地址
     * @param port IP端口
     */
    public void connectConfiguration(String host,int port) {
        WifiProxy proxy = new WifiProxy();
        proxy.setWifiProxySettings(mWifiManager,host,port);
    }


    /**
     * 开始扫描Wifi
     */
    public void startScan() {
        mWifiManager.startScan();
        // 得到扫描结果
        mWifiList = mWifiManager.getScanResults();
        // 得到配置好的网络连接
        mWifiConfiguration = mWifiManager.getConfiguredNetworks();
    }

    /**
     * 得到网络列表
     *
     * @return
     */
    public List<ScanResult> getWifiList() {
        return mWifiList;
    }

    /**
     * 查看扫描结果
     *
     * @return
     */
    public StringBuilder lookUpScan() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < mWifiList.size(); i++) {
            stringBuilder
                    .append("Index_" + new Integer(i + 1).toString() + ":");
            // 将ScanResult信息转换成一个字符串包
            // 其中把包括：BSSID、SSID、capabilities、frequency、level
            stringBuilder.append((mWifiList.get(i)).toString());
            stringBuilder.append("/n");
        }
        return stringBuilder;
    }

    /**
     * 得到MAC地址
     *
     * @return
     */
    public String getMacAddress() {
        return (mWifiInfo == null) ? "NULL" : mWifiInfo.getMacAddress();
    }

    /**
     * 得到接入点的BSSID
     *
     * @return
     */
    public String getBSSID() {
        return (mWifiInfo == null) ? "NULL" : mWifiInfo.getBSSID();
    }

    /**
     * 得到IP地址
     *
     * @return
     */
    public int getIPAddress() {
        return (mWifiInfo == null) ? 0 : mWifiInfo.getIpAddress();
    }

    /**
     * 得到连接的ID
     *
     * @return
     */
    public int getNetworkId() {
        return (mWifiInfo == null) ? 0 : mWifiInfo.getNetworkId();
    }

    /**
     * 得到WifiInfo的所有信息包
     *
     * @return
     */
    public String getWifiInfo() {
        return (mWifiInfo == null) ? "NULL" : mWifiInfo.toString();
    }

    /**
     * 添加一个网络并连接
     *
     * @param wcg
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void addNetwork(WifiConfiguration wcg) {
            //http://www.2cto.com/kf/201503/383906.html
 //           wcg.setHttpProxy(ProxyInfo.buildDirectProxy("", 8080));
            int wcgID = mWifiManager.addNetwork(wcg);
            boolean b = mWifiManager.enableNetwork(wcgID, true);
            System.out.println("a--" + wcgID);
            System.out.println("b--" + b);

    }

    /**
     * 断开指定ID的网络
     *
     * @param netId
     */
    public void disconnectWifi(int netId) {
        mWifiManager.disableNetwork(netId);
        mWifiManager.disconnect();
    }
}
