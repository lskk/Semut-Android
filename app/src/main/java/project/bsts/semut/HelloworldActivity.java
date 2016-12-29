package project.bsts.semut;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.arasthel.asyncjob.AsyncJob;
import io.grpc.ManagedChannel;
import project.bsts.semut.connection.Channel;
import project.bsts.semut.models.UserManagement;
import io.grpc.helloworldexample.R;

public class HelloworldActivity extends ActionBarActivity {
    private Button mSendButton;
    private EditText mHostEdit;
    private EditText mPortEdit;
    private EditText mMessageEdit;
    private TextView mResultText;
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
        mResultText.setText("");
        AsyncJob.doInBackground(new AsyncJob.OnBackgroundJob() {
            @Override
            public void doOnBackground() {
                mChannel = Channel.buildChannel();
                res = new UserManagement(mChannel).login("caliandrat9@gmail.com", "");
                final String result = res;
                AsyncJob.doOnMainThread(new AsyncJob.OnMainThreadJob() {
                    @Override
                    public void doInUIThread() {
                        Log.i("INFO", "result : "+result);
                        mResultText.setText(result);
                        mSendButton.setEnabled(true);
                    }
                });
            }
        });
    }
}
