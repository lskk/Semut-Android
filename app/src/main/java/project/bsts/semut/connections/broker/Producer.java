package project.bsts.semut.connections.broker;

import android.os.Handler;
import android.util.Log;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.ShutdownListener;
import com.rabbitmq.client.ShutdownSignalException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import project.bsts.semut.setup.Constants;

public class Producer extends Connector{
    private static final String TAG = "Producer";
    private BrokerCallback mCallback;
    private String mQueueName;
    private String mExchange;
    private String mRoutingKey;
    private Thread publishThread;
    int publishTimeout = 5 * 1000;
    private Handler mCallbackHandler = new Handler();


    public static Producer createInstance(Factory factory, BrokerCallback callback ){
        return new Producer(factory.getHostName(),
                factory.getVirtualHostName(),
                factory.getUsername(),
                factory.getPassword(),
                factory.getPort(),
                factory.getRoutingKey(),
                factory.getExcahnge(),
                callback);
    }
    public Producer(String host, String virtualHost, String username, String password, int port, String routingKey, String exchange, BrokerCallback callback) {
        super(host, virtualHost, username, password, port);
        this.mCallback = callback;
        this.mRoutingKey = routingKey;
        this.mExchange = exchange;
        this.mQueueName = createDefaultRoutingKey();
    }

    private String createDefaultRoutingKey() {
        return Constants.MQ_QUEUE_NAME;
    }

    private void initConnection() throws IOException, TimeoutException {
        if(!isConnected()){
            createConnection();
        }
    }


    private void initchanenel(){
        if(!isChannelAvailable()){
            createChannel();
        }
    }

    public void stop(){
        try {
            closeMQConnection();
        } catch (IOException | TimeoutException e) {
            sendBackErrorMessage(e);
            e.printStackTrace();
        }
    }

    public void publish(final String message, final BasicProperties properties, final boolean isDeclareQueue){
        publishThread = new Thread(() -> {
            while(isRunning) {
                try {
                    initConnection();
                    initchanenel();
                    if(isDeclareQueue) {
                        declareQueue();
                    }
                    mChannel.confirmSelect();
                //    mChannel.queueBind(mQueueName, mExchange, mRoutingKey);
                    byte [] messageBytes = message.getBytes();
                    mChannel.basicPublish(mExchange, mRoutingKey, properties, messageBytes);
                    mChannel.waitForConfirms(publishTimeout);
                    closeMQConnection();
                    isRunning = false;
                } catch (InterruptedException | IOException | TimeoutException e) {
                    sendBackErrorMessage(e);
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e1) {
                        isRunning = false;
                        break;
                    }
                }
            }
        });
        publishThread.start();
    }




    private void sendBackErrorMessage(Exception e) {
        final String errorMessage = e.getMessage() == null ? e.toString() : e.getMessage();
        mCallbackHandler.post(() -> mCallback.onMQConnectionFailure(errorMessage));
    }

    private void declareQueue() throws IOException {

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("x-expires", 2 * 60 * 60 * 1000);
        mChannel.queueDeclare(mQueueName, true, false, false, params);
        Log.d(TAG, "Queue :" + "queue:name:" + mQueueName + " declared");
    }

    @Override
    protected ShutdownListener createShutDownListener() {
        ShutdownListener listener = cause -> {
            String errorMessage = cause.getMessage() == null ? "produser connection was shutdown" : "consumer " + cause.getMessage();
            mCallback.onMQConnectionClosed(errorMessage);
        };
        return listener;
    }



    public int getPublishTimeout() {
        return publishTimeout;
    }

    public void setPublishTimeout(int publishTimeout) {
        this.publishTimeout = publishTimeout;
    }

    public String getExchange() {
        return mExchange;
    }

    public void setExchange(String mExchange) {
        this.mExchange = mExchange;
    }

    public String getRoutingkey() {
        return mRoutingKey;
    }

    public void setRoutingkey(String mRoutingKey) {
        this.mRoutingKey = mRoutingKey;
    }

    public String getQueueName() {
        return mQueueName;
    }

    public void setQueueName(String mQueueName) {
        this.mQueueName = mQueueName;
    }
}