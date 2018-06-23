package com.weilay.mqtt;
import com.framework.utils.L;
import com.framework.utils.NetworkUtils;
import com.ibm.micro.client.mqttv3.MqttCallback;
import com.ibm.micro.client.mqttv3.MqttClient;
import com.ibm.micro.client.mqttv3.MqttClientPersistence;
import com.ibm.micro.client.mqttv3.MqttConnectOptions;
import com.ibm.micro.client.mqttv3.MqttDeliveryToken;
import com.ibm.micro.client.mqttv3.MqttException;
import com.ibm.micro.client.mqttv3.MqttMessage;
import com.ibm.micro.client.mqttv3.MqttPersistenceException;
import com.ibm.micro.client.mqttv3.MqttTopic;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;

public class PushServices implements MqttCallback {
	private Context mContext;
	private static final String TAG = "---pushservices----";
	private OnReceiverListener mListener;

	public void setOnReceiverListener(OnReceiverListener listener) {
		if (listener != null) {
			this.mListener = listener;
		} else {
			this.mListener = OnReceiverListener.getInstance();
		}
	}

	// Let's not use the MQTT persistence.
	private MqttClientPersistence MQTT_PERSISTENCE = null;
	private MqttClient mqttClient = null;
	private String CLIENT_ID = "weilay";
	// the server is
	private boolean mStarted = false;
	private boolean mHasConnect=false;
	private String[] TOPICS = null;
	
	// log
	private static void log(String message) {
		L.d(TAG + message);
	}

	private MqttConnectOptions options = PushConstants.getOptions();

	public boolean isStart() {
		return mStarted;
	}

	public PushServices(Context context, String client_id) {
		this.mContext = context;
		this.CLIENT_ID = client_id;

	}

	public void start() {
		if (mStarted) {
			/// log("-pushservices alread started");
			return;
		}
		mStarted = true;
		handleCrashedService();
	}

	/*****
	 * @detail subscribe
	 */
	public void subscribe(OnReceiverListener listener, String... topicNames) {
		this.TOPICS = topicNames;
		final int[] qoss = new int[topicNames.length];
		for (int i = 0; i < topicNames.length; i++) {
			log("->MqttClient[subscribe]:" + topicNames[i]);
			qoss[i] = PushConstants.MQTT_QUALITY_OF_SERVICE;
		}
		try {
			if ((mqttClient == null) || (mqttClient.isConnected() == false)) {
				log("no connect");
				reconnectIfNecessary();
				setOnReceiverListener(listener);
			} else {
				setOnReceiverListener(listener);
				//先取消订阅之前的话题
				mqttClient.unsubscribe("banklay_card_*");
				mqttClient.subscribe(topicNames, qoss);
			}
		} catch (MqttException e) {
			log("connecct error!cause:" + e.getLocalizedMessage());
		}

	}

	/******
	 * @detail send command the connect
	 */
	private void handleCrashedService() {
		log("push server starting");
		
		// register the network listener
		mContext.registerReceiver(mConnectivityChanged, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
		// stopKeepAlives();
		connect();
	}

	/*****
	 * @detail connect 
	 */
	private boolean connect() {
		try {
			if (mqttClient != null && mqttClient.isConnected()) {
				log("---has connected");
			} else {
				String mqttConnSpec = "tcp://" + PushConstants.BROKER_HOST_NAME + ":"
						+ PushConstants.MQTT_BROKER_PORT_NUM;
				log("-begin connect to" + mqttConnSpec);
				mqttClient = new MqttClient(mqttConnSpec, CLIENT_ID, MQTT_PERSISTENCE);
				mqttClient.connect(options);
				mqttClient.setCallback(this);
				log("mqtt server connect success");
			}
			return true;
		} catch (MqttException me) {
			log(me.getLocalizedMessage());
		} catch (Exception ex) {
			log(ex.getLocalizedMessage());
		}
		return false;

	}

	/*******
	 * @Detail stop server
	 */
	public void stop() {
		log("mqtt server stop");
		mStarted = false;
		try {
			if(TOPICS!=null && mqttClient!=null){
				for(String topic:TOPICS){
					log("取消订阅的话题是："+topic);
				}
				mqttClient.unsubscribe("banklay_card_*");
				TOPICS = null;
			}
			if (mContext != null && mConnectivityChanged != null) {
				mContext.unregisterReceiver(mConnectivityChanged);
			}
			disconnect();
		} catch (Exception ex) {
		}

	}

	private Handler reconnectHandler = null;
	private boolean connecting=false;
	/******	
	 * @detail try to reconnet mqtt server
	 */
	private synchronized void reconnectIfNecessary() {
		if (mStarted == true && mqttClient == null &&  !connecting) {
			log("----pushservices Reconnecting...");
			reconnectHandler = new Handler(mContext.getMainLooper()){
				@Override
				public void handleMessage(Message msg) {
					switch (msg.what) {
					case 1:
						connecting=false;
						//reconnectHandler.removeCallbacks(temp);
						if (TOPICS != null) {// 重新订阅话题
							log("resubject the topics");
							subscribe(mListener, TOPICS);
						}
						break;

					default:
						break;
					}
				}
			};
			Thread thread=new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					while(!mHasConnect && NetworkUtils.isNetworkable(mContext)){
						mHasConnect=connect();
						if(mHasConnect){
							reconnectHandler.sendEmptyMessage(1);
							break;
						}
						//休眠15s
						SystemClock.sleep(PushConstants.RECONNECT_INTERVAL);
					}
				}
			});
			thread.start();
			connecting=true;
			/*reconnectHandler.postDelayed(new Runnable() {

				@Override
				public void run() {
					final Runnable temp = this;
					new AsyncTask<Void, Void, Boolean>() {

						@Override
						protected Boolean doInBackground(Void... params) {
							return connect();
						}

						@Override
						protected void onPostExecute(Boolean result) {
							// TODO Auto-generated method stub
							if (result) {
								log("reconnect success!!");
								
							} else {
								log("reconnect failed!!");
								reconnectHandler.removeCallbacks(temp);
								reconnectHandler.postDelayed(temp, PushConstants.RECONNECT_INTERVAL);
							}
						}

					}.execute();

				}
			}, PushConstants.RECONNECT_INTERVAL);*/

		}
	}

	/*******
	 * @detail 断开连接
	 */
	private void disconnect() {
		try {
			if (mqttClient != null) {
				log("disconnect");
				mqttClient.disconnect();
				mqttClient = null;
			}
		} catch (MqttPersistenceException e) {
			e.printStackTrace();
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}

	// This receiver listeners for network changes and updates the MQTT
	// connection
	// accordingly
	private BroadcastReceiver mConnectivityChanged = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			// Get network info
			NetworkInfo info = (NetworkInfo) intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
			// Is there connectivity?
			boolean hasConnectivity = (info != null && info.isConnected()) ? true : false;
			if (hasConnectivity) {
				log("network has  connect");
				reconnectIfNecessary();
			} else if (mqttClient != null) {
				// if there no connectivity, make sure MQTT connection is
				// destroyed
				log("lost network connetion");
				disconnect();
			}
		}
	};

	@Override
	public void connectionLost(Throwable arg0) {
		log("lost connect,the cause reason:" + arg0.getLocalizedMessage());
		try {
			mqttClient.disconnect();
		} catch (MqttException e) {
			log("--disconnect--"+e.getLocalizedMessage());
		}
		mqttClient = null;
		if (NetworkUtils.isNetworkable(mContext)) {
			reconnectIfNecessary();
		}
	}

	@Override
	public void deliveryComplete(MqttDeliveryToken arg0) {
		// TODO Auto-generated method stub
	}
	@Override
	public void messageArrived(MqttTopic topic, MqttMessage arg1) throws Exception {
		// TODO Auto-generated method stub
		try {
			String content = new String(arg1.getPayload());
			L.e("pushservices -> " + topic.getName() + ":" + content);
			mListener.onReceiver(topic.getName(), 0, content);
		} catch (Exception ex) {
			L.e(ex.getMessage());
			log("-- resolve content happend exception,the cause reseaon" + ex.getLocalizedMessage());
		}
	}

}