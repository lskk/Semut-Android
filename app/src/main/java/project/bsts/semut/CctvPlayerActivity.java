package project.bsts.semut;

import android.app.Activity;
import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import project.bsts.semut.setup.Constants;


public class CctvPlayerActivity extends Activity implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, OnCompletionListener{
    private VideoView videoView;
    private String urlStr;
    private ProgressDialog pDialog;

    private MediaController mediaController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cctv_player);

        videoView = (VideoView)findViewById(R.id.videoView);
        Bundle bundle = this.getIntent().getExtras();
        setUrlStr(bundle.getString(Constants.INTENT_VIDEO_URL));

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(true);
        pDialog.setMessage("Loading...");
        showDialog();

        mediaController = new MediaController(CctvPlayerActivity.this){
            @Override
            public void hide() {
                super.show();
            }

            public boolean dispatchKeyEvent(KeyEvent event)
            {
                if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP)
                    CctvPlayerActivity.this.onBackPressed();

                return super.dispatchKeyEvent(event);
            }
        };

        mediaController.setAnchorView(videoView);
        Uri video = Uri.parse(getUrlStr());
        Log.i(this.getClass().getSimpleName(), String.valueOf(video));
        videoView.setMediaController(mediaController);
        videoView.setVideoURI(video);
        videoView.setOnPreparedListener(this);
        videoView.setOnErrorListener(this);
        videoView.setOnCompletionListener(this);
        videoView.start();
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    // setter dan getter

    public String getUrlStr() {
        return urlStr;
    }

    public void setUrlStr(String urlStr) {
        this.urlStr = urlStr;
        this.urlStr  = this.urlStr .replace("push-ios", "247");
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        hideDialog();
        try {
            if (!mediaController.isShowing()) mediaController.show();
        }catch (Exception e){
            Log.i(this.getClass().getSimpleName(), "Media Control Exception");
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        finish();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        hideDialog();
        Toast.makeText(getApplicationContext(),
                "Error when loading the cctv video.", Toast.LENGTH_SHORT)
                .show();
        finish();
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        videoView.stopPlayback();
        finish();
    }
}