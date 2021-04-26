package com.demo.base.tool.data.android;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;

import androidx.core.app.ActivityCompat;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 * 创建时间： 2017/11/28.
 * 编写人：wuweimin
 * 功能描述：网络相关的工具类
 */

public class NetworkUtil {
    private static int wifiState = -1;
    private static String mobileNetType = "";

    /**
     * 判断网络是否连接
     *
     * @param context Context
     * @return 网络是否连接
     */
    public static boolean isConnected(Context context) {

        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (null != connectivity) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (null != info && info.isConnected()) {
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断是否是wifi连接
     *
     * @param context Context
     * @return 是否是wifi连接
     */
    public static boolean isWifiConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
            return false;
        }
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.getType() == ConnectivityManager.TYPE_WIFI;
    }

    /**
     * 获取当前连接网络类型 "none"/"WLAN"/...
     *
     * @return 网络类型
     */
    public static String getCurrentNetworkName(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        String netWorkName = "none";
        if (cm != null) {
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            if (activeNetwork != null) { // connected to the internet
                if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                    netWorkName = "WLAN";
                } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                    netWorkName = activeNetwork.getSubtypeName();
                }
            }
        }
        return netWorkName;
    }

    /**
     * 获取网络IP  eg：192.168.156.121
     *
     * @return 网络IP，没有的话返回""
     */
    public static String getIp() {
        String ip = null;
        try {
            Enumeration enumeration = NetworkInterface.getNetworkInterfaces();
            if (enumeration == null) {
                return "";
            }
            while (enumeration.hasMoreElements()) {
                NetworkInterface networkInterface = (NetworkInterface) enumeration.nextElement();
                Enumeration enumr = networkInterface.getInetAddresses();
                while (enumr.hasMoreElements()) {
                    InetAddress inetAddress = (InetAddress) enumr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        ip = inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return ip == null ? "" : ip;
    }


    /**
     * 获取网卡的Ma地址c，eg: 38:BC:1A:BD:98:63
     *
     * @return Mac 没有的话返回""
     */
    public static String getMac() {
        String mac;
        mac = getLocalMacAddressFromIp();
        if (mac == null) {
            mac = getNewMac();
        }
        if (mac == null) {
            mac = "";
        }
        return mac;
    }

    /**
     * 根据IP地址获取MAC地址
     *
     * @return MAC地址
     */
    private static String getLocalMacAddressFromIp() {
        String strMacAddr = null;
        try {
            //获得IpD地址
            InetAddress ip = getLocalNetIpAddress();
            if (ip == null) {
                return null;
            }
            byte[] b = NetworkInterface.getByInetAddress(ip).getHardwareAddress();
            if (b == null) {
                return null;
            }
            StringBuilder buffer = new StringBuilder();
            for (int i = 0; i < b.length; i++) {
                if (i != 0) {
                    buffer.append(':');
                }
                String str = Integer.toHexString(b[i] & 0xFF);
                buffer.append(str.length() == 1 ? 0 + str : str);
            }
            strMacAddr = buffer.toString().toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strMacAddr;
    }

    /**
     * 获取移动设备本地IP
     *
     * @return 本地IP
     */
    private static InetAddress getLocalNetIpAddress() {
        InetAddress ip = null;
        try {
            Enumeration<NetworkInterface> netInterface = NetworkInterface.getNetworkInterfaces();
            while (netInterface.hasMoreElements()) {
                NetworkInterface ni = netInterface.nextElement();
                Enumeration<InetAddress> enIp = ni.getInetAddresses();
                while (enIp.hasMoreElements()) {
                    ip = enIp.nextElement();
                    if (!ip.isLoopbackAddress()) {
                        break;
                    } else {
                        ip = null;
                    }
                }

                if (ip != null) {
                    break;
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return ip;
    }


    public static String getLocalIpAddress() {
        try {
            Enumeration enumeration = NetworkInterface.getNetworkInterfaces();

            while (enumeration.hasMoreElements()) {
                NetworkInterface networkInterface = (NetworkInterface) enumeration.nextElement();
                Enumeration enumr = networkInterface.getInetAddresses();

                while (enumr.hasMoreElements()) {
                    InetAddress inetAddress = (InetAddress) enumr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 通过网络接口取Mac
     *
     * @return Mac
     */
    private static String getNewMac() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!"wlan0".equalsIgnoreCase(nif.getName())) {
                    continue;
                }

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return null;
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:", b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static String getMcc(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager == null) {
            return null;
        }

        String operator = telephonyManager.getNetworkOperator();
        if (operator == null || operator.length() < 3) {
            return "";
        }

        return operator.substring(0, 3);
    }

    public static String getMnc(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager == null) {
            return null;
        }

        String operator = telephonyManager.getNetworkOperator();
        if (operator == null || operator.length() <= 3) {
            return "";
        }

        return operator.substring(3);
    }

    public static String getCid(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager == null) {
            return null;
        }

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }

        GsmCellLocation location = (GsmCellLocation) telephonyManager.getCellLocation();
        if (location == null) {
            return null;
        }

        return String.valueOf(location.getCid());
    }

    public static String getLac(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager == null) {
            return null;
        }

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }

        GsmCellLocation location = (GsmCellLocation) telephonyManager.getCellLocation();
        if (location == null) {
            return null;
        }

        return String.valueOf(location.getLac());
    }

    public static String getBSSID(Context context) {
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifiManager == null) {
            return null;
        }

        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        if (wifiInfo == null) {
            return null;
        }

        return wifiManager.getConnectionInfo().getBSSID();
    }

    /**
     * 获取网络类型
     * 1：CMNET
     * 2：CMWAP
     * 5：WIFI
     *
     * @param context
     * @return
     */
    public static String getNetType(Context context) {
        String netType = "";
        if (getWifiState(context) == 0) {
            //Wifi网络
            netType = "5";
        } else {
            netType = getMobileNetType(context);
        }

        return netType;
    }

    public static int getWifiState(Context context) {
        if (wifiState == -1) {
            ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE); // 获取网络服务
            NetworkInfo wifiInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (null != wifiInfo) {
                NetworkInfo.State state = wifiInfo.getState();
                if (null != state) {
                    if (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING) {
                        wifiState = 0;
                    } else {
                        wifiState = 1;
                    }
                } else {
                    wifiState = 1;
                }
            }
        }

        return wifiState;
    }

    private static String getMobileNetType(Context context) {
        try {
            ConnectivityManager conManager =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo ni = conManager.getActiveNetworkInfo();
            if (null != ni) {
                String mobileApn = ni.getExtraInfo();
                if ("CMWAP".equalsIgnoreCase(mobileApn)) {
                    mobileNetType = "2";
                } else if ("CMNET".equalsIgnoreCase(mobileApn)) {
                    mobileNetType = "1";
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return mobileNetType;
    }
}
