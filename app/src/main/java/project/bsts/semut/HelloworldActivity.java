
package project.bsts.semut;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.arasthel.asyncjob.AsyncJob;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import project.bsts.semutservice.LoginReply;
import project.bsts.semutservice.LoginRequest;
import project.bsts.semutservice.UsersManagementGrpc;
import io.grpc.helloworldexample.R;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.concurrent.TimeUnit;

public class HelloworldActivity extends ActionBarActivity {
    private Button mSendButton;
    private EditText mHostEdit;
    private EditText mPortEdit;
    private EditText mMessageEdit;
    private TextView mResultText;
    String mHost;
    String mMessage;
    int mPort;
    ManagedChannel mChannel;
    String res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helloworld);
        mSendButton = (Button) findViewById(R.id.send_button);
        mHostEdit = (EditText) findViewById(R.id.host_edit_text);
        mPortEdit = (EditText) findViewById(R.id.port_edit_text);
        mMessageEdit = (EditText) findViewById(R.id.message_edit_text);
        mResultText = (TextView) findViewById(R.id.grpc_response_text);
        mResultText.setMovementMethod(new ScrollingMovementMethod());
    }

    public void sendMessage(View view) {
        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(mHostEdit.getWindowToken(), 0);
        mSendButton.setEnabled(false);
        sendRequest();
    }

    private void sendRequest(){

        mHost = mHostEdit.getText().toString();
        mMessage = mMessageEdit.getText().toString();
        String portStr = mPortEdit.getText().toString();
        mPort = TextUtils.isEmpty(portStr) ? 0 : Integer.valueOf(portStr);
        mResultText.setText("");

        AsyncJob.doInBackground(new AsyncJob.OnBackgroundJob() {
            @Override
            public void doOnBackground() {
                try {
                    mChannel = ManagedChannelBuilder.forAddress(mHost, mPort)
                            .usePlaintext(true)
                            .build();
                    UsersManagementGrpc.UsersManagementBlockingStub stub = UsersManagementGrpc.newBlockingStub(mChannel);
                    LoginRequest message = LoginRequest.newBuilder().setEmail("caliandrat9@gmail.com").setPassword("").build();
                    LoginReply reply = stub.login(message);
                    res = reply.getResponse();
                } catch (Exception e) {
                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);
                    e.printStackTrace(pw);
                    pw.flush();
                    res =  String.format("Failed... : %n%s", sw);
                }
                final String result = res;
                AsyncJob.doOnMainThread(new AsyncJob.OnMainThreadJob() {
                    @Override
                    public void doInUIThread() {
                        Log.i(this.getClass().getSimpleName(), "result : "+result);
                        try {
                            mChannel.shutdown().awaitTermination(1, TimeUnit.SECONDS);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                        mResultText.setText(result);
                        mSendButton.setEnabled(true);
                    }
                });
            }
        });
    }
}
