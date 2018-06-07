package cn.vinotec.app.android.comm;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
//import cn.jpush.android.api.JPushInterface;
import cn.vinotec.app.android.comm.utils.StringUtil;

/**
 * 自定义接收器
 * 
 * 如果不定义这个 Receiver，则： 1) 默认用户会打开主界面 2) 接收不到自定义消息
 */
public class JPushReceiver extends BroadcastReceiver {
	private static final String TAG = "JPush";
	private String url = "";

	@Override
	public void onReceive(Context context, Intent intent) {
//        Bundle bundle = intent.getExtras();
//		Log.d(TAG, "[JPushReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));
//
//        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
//            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
//			Log.d(TAG, "[JPushReceiver] 接收Registration Id : " + regId);
//            //send the Registration Id to your server...
//
//		} else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {// 自定义消息
//			Log.d(TAG, "[JPushReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
//        	processCustomMessage(context, bundle);
//
//		} else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {// 通知
//			Log.d(TAG, "[JPushReceiver] 接收到推送下来的通知");
//            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
//			Log.d(TAG, "[JPushReceiver] 接收到推送下来的通知的ID: " + notifactionId);
//
//        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
//			Log.d(TAG, "[JPushReceiver] 用户点击打开了通知");
//
//            JPushInterface.reportNotificationOpened(context, bundle.getString(JPushInterface.EXTRA_MSG_ID));
//
//			// 打开自定义的Activity
//            String type = bundle.getString("type");
////            if("news".equalsIgnoreCase(type))
////            {
////    			Intent i = new Intent(context, ArticleDetailActivity.class);
////            	i.putExtras(bundle);
////            	i.putExtra("url", bundle.getString("url"));
////            	i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////            	context.startActivity(i);
////            }else if("trouble".equalsIgnoreCase(type))
////            {
////				Intent i = new Intent(context,FaultDetailActivity.class);
////				try
////				{
////					long id  = Long.parseLong(bundle.getString("url"));
////					intent.putExtra("detail_id", id);
////				}
////				catch(Exception ex)
////				{
////					intent.putExtra("detail_id", 0);
////				}
////            	i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////            	context.startActivity(i);
////            }
//        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
//			Log.d(TAG, "[JPushReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
//			// 在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity，
//			// 打开一个网页等..
//
//        } else if(JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
//        	boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
//			Log.d(TAG, "[JPushReceiver]" + intent.getAction() + " connected state change to " + connected);
//        } else {
//			Log.d(TAG, "[JPushReceiver] Unhandled intent - " + intent.getAction());
//        }
	}

	// 打印所有的 intent extra 数据
	private static String printBundle(Bundle bundle) {
		StringBuilder sb = new StringBuilder();
//		for (String key : bundle.keySet()) {
//			if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
//				sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
//			}else if(key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)){
//				sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
//			}
//			else {
//				sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
//			}
//		}
		return sb.toString();
	}
	
	//send msg to MainActivity
	@SuppressLint("NewApi")
	private void processCustomMessage(Context context, Bundle bundle) {
//		String title = bundle.getString(JPushInterface.EXTRA_TITLE);
//		String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
//		String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
//		String id = "";
//		String type = "";
//		boolean show = true;
//		try
//		{
//			if (!StringUtil.isBlank(extras))
//			{
//				JSONObject extraJson = new JSONObject(extras);
//				if (extraJson != null)
//				{
//					type = extraJson.getString("type");
//					if("news".equalsIgnoreCase(type))
//					{
//						id = extraJson.getString("id");
//						url = extraJson.getString("url");
//
//					}else if("trouble".equalsIgnoreCase(type))
//					{
//						id = extraJson.getString("id");
//					}
//					if (!StringUtil.isBlank(type))
//					{
//						Log.d(TAG, "[JPushReceiver] message type - " + type);
//						// show = checkShowNotification(context, type);
//					}
//				}
//			}
//		}
//		catch (JSONException e)
//		{
//		}
//
//		if (!show)
//		{
//			return;
//		}
//		NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//		Notification.Builder builder;
//		builder=new Notification.Builder(context);
//		builder.setContentTitle(title);
//		builder.setContentText(message);
//		builder.setAutoCancel(true);
//		// builder.setSmallIcon(R.drawable.ic_launcher);
//		nm.notify(1001, builder.build());
//
//		//Notification n = new Notification(R.drawable.ic_launcher, Html.fromHtml(message), System.currentTimeMillis());
////		n.defaults = Notification.DEFAULT_ALL;
////		n.flags |= Notification.FLAG_AUTO_CANCEL;
////		n.flags |= Notification.FLAG_SHOW_LIGHTS;
//		//Intent notificationIntent = null;
////        if("news".equalsIgnoreCase(type))
////        {
////    		notificationIntent = new Intent(context, ArticleDetailActivity.class);
////    		notificationIntent.putExtra("url", url);
////        }else if("trouble".equalsIgnoreCase(type))
////        {
////    		notificationIntent = new Intent(context, PatrolDetailOnlineActivity.class);
////			try
////			{
////				long detailid  = Long.parseLong(id);
////				notificationIntent.putExtra("id", detailid);
////				notificationIntent.putExtra("type", 1);
////			}
////			catch(Exception ex)
////			{
////				notificationIntent.putExtra("id", 0);
////			}
////        }else
////        {
////        	notificationIntent = new Intent(context, SplashActivity.class);
////        }
//		//PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, Intent.FLAG_ACTIVITY_NEW_TASK);
//		//n.setLatestEventInfo(context, title, Html.fromHtml(message), contentIntent);
//		//nm.notify(0, n);
	}
}
