package me.waye.androidspeeder.util;
// <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;

/**
  * @ClassName: NetworkCheck
  * @Description: 网络连接检查和设置工具类
  * @author tangwei
  * @date Nov 11, 2013 2:07:45 PM
  *
  */ 
public class NetworkCheck {

	/**
	  * @Title: isConnected
	  * @Description: 检查网络是否连接
	  * @param ctx 上下文对象 
	  * @return  true if connected, otherwise false
	  */ 
	public static boolean isConnected(Context ctx) {
		ConnectivityManager manager = (ConnectivityManager) ctx
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		if (manager == null)
			return false;
		NetworkInfo networkInfo = manager.getActiveNetworkInfo();
		if (networkInfo == null || !networkInfo.isAvailable())
			return false;
		return true;
	}

	/**
	  * @Title: networkSetting
	  * @Description: 设置网络连接
	  * @param ctx  上下文对象 
	  */ 
	public static void networkSetting(final Context ctx) {

		AlertDialog.Builder builder = new AlertDialog.Builder(ctx)
				.setTitle("Network is not available,go to settings?")
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent networkIntent = new Intent(
								Settings.ACTION_WIRELESS_SETTINGS);
						networkIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						ctx.startActivity(networkIntent);
					}
				})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {

							}
						});

		builder.create().show();

	}
}
