package com.rocky.baidu.zhidao.wifi;

import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.rocky.baidu.zhidao.reflect.ReflectTools;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 使用代理连接WIFI
 *
 * Created by Administrator on 2016/3/23.
 */
public class WifiProxy {

    /**
     * 使用代理IP连接WIFI
     *
     * @param manager
     * @param host
     * @param port
     */
   public void setWifiProxySettings(WifiManager manager,String host,int port)
    {
        //get the current wifi configuration
        WifiConfiguration config = GetCurrentWifiConfiguration(manager);
        if(null == config)
            return;

        try
        {
            //get the link properties from the wifi configuration
            Object linkProperties = ReflectTools.getField(config, "linkProperties");
            if(null == linkProperties)
                return;

            //get the setHttpProxy method for LinkProperties
            Class proxyPropertiesClass = Class.forName("android.net.ProxyProperties");
            Class[] setHttpProxyParams = new Class[1];
            setHttpProxyParams[0] = proxyPropertiesClass;
            Class lpClass = Class.forName("android.net.LinkProperties");
            Method setHttpProxy = lpClass.getDeclaredMethod("setHttpProxy", setHttpProxyParams);
            setHttpProxy.setAccessible(true);

            //get ProxyProperties constructor
            Class[] proxyPropertiesCtorParamTypes = new Class[3];
            proxyPropertiesCtorParamTypes[0] = String.class;
            proxyPropertiesCtorParamTypes[1] = int.class;
            proxyPropertiesCtorParamTypes[2] = String.class;

            Constructor proxyPropertiesCtor = proxyPropertiesClass.getConstructor(proxyPropertiesCtorParamTypes);

            //create the parameters for the constructor
            Object[] proxyPropertiesCtorParams = new Object[3];
            proxyPropertiesCtorParams[0] = host;
            proxyPropertiesCtorParams[1] = port;
            proxyPropertiesCtorParams[2] = null;

            //create a new object using the params
            Object proxySettings = proxyPropertiesCtor.newInstance(proxyPropertiesCtorParams);

            //pass the new object to setHttpProxy
            Object[] params = new Object[1];
            params[0] = proxySettings;
            setHttpProxy.invoke(linkProperties, params);

            setProxySettings("STATIC", config);

            Log.d("Rocky", config.toString());

            //save the settings
            manager.updateNetwork(config);
            manager.disconnect();
            manager.reconnect();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 获取当前WIFI的配置信息
     *
     * @param manager
     * @return
     */
    public static  WifiConfiguration GetCurrentWifiConfiguration(WifiManager manager)
    {
        if (!manager.isWifiEnabled())
            return null;

        List<WifiConfiguration> configurationList = manager.getConfiguredNetworks();
        WifiConfiguration configuration = null;
        int cur = manager.getConnectionInfo().getNetworkId();
        for (int i = 0; i < configurationList.size(); ++i)
        {
            WifiConfiguration wifiConfiguration = configurationList.get(i);
            if (wifiConfiguration.networkId == cur)
                configuration = wifiConfiguration;
        }

        return configuration;
    }

    /**
     * 为WifiConfiguration配置proxySettings为assign
     *
     * @param assign
     * @param wifiConf
     * @throws SecurityException
     * @throws IllegalArgumentException
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    public static void setProxySettings(String assign , WifiConfiguration wifiConf)
            throws SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException{
        ReflectTools.setEnumField(wifiConf, assign, "proxySettings");
    }
}
