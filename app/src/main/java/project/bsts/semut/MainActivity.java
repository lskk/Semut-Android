package project.bsts.semut;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.rabbitmq.client.QueueingConsumer;

import project.bsts.semut.connections.broker.BrokerCallback;
import project.bsts.semut.connections.broker.Config;
import project.bsts.semut.connections.broker.Consumer;
import project.bsts.semut.connections.broker.Factory;
import project.bsts.semut.connections.broker.Producer;

public class MainActivity extends AppCompatActivity implements BrokerCallback {

    private Factory mqFactory;
    private Consumer mqConsumer;

    private boolean consumerModeStart = true;
    private Producer mqProducer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.mqFactory = new Factory(Config.hostName,
                Config.virtualHostname,
                Config.username,
                Config.password,
                Config.exchange,
                Config.rotuingkey,
                Config.port);

        this.mqConsumer = this.mqFactory.createConsumer(this);
        this.mqProducer = this.mqFactory.createProducer(this);

        this.mqConsumer.setMessageListner(new Consumer.MQConsumerListener() {
            @Override
            public void onMessageReceived(QueueingConsumer.Delivery delivery) {
                Toast.makeText(MainActivity.this, "receive message", Toast.LENGTH_SHORT).show();
                Log.i("test", delivery.getBody().toString());
            }
        });

        findViewById(R.id.btn_consumer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(consumerModeStart){
                    mqConsumer.subsribe();


                    consumerModeStart = false;
                    ((Button)findViewById(R.id.btn_consumer)).setText("stop consumer");
                    Toast.makeText(MainActivity.this, "start consumer", Toast.LENGTH_SHORT).show();

                }
                else{
                    consumerModeStart = true;
                    mqConsumer.stop();
                    ((Button)findViewById(R.id.btn_consumer)).setText("start consumer");
                    Toast.makeText(MainActivity.this, "stop consumer", Toast.LENGTH_SHORT).show();

                }
            }
        });

        findViewById(R.id.btn_producer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = ((EditText) findViewById(R.id.edit_producer)).getText().toString();
                mqProducer.publish(message, null);
                Toast.makeText(MainActivity.this, "publish", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onMQConnectionFailure(String message) {
        Toast.makeText(this, "failure : "+ message, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onMQDisconnected() {
        Toast.makeText(this, "disconnected", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onMQConnectionClosed(String message) {
        Toast.makeText(this, "closed : "+ message, Toast.LENGTH_SHORT).show();

    }

}