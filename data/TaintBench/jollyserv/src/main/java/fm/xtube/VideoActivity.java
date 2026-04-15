package fm.xtube;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;
import fm.xtube.core.GodHelpMe;

public class VideoActivity extends GodHelpMe {
    private MediaController mediaController = null;
    /* access modifiers changed from: private */
    public ProgressBar progressBar = null;
    private String videoUrl;
    /* access modifiers changed from: private */
    public VideoView videoView = null;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        getWindow().setFormat(-3);
        setContentView(R.layout.video);
        this.videoUrl = getIntent().getExtras().getString("url");
        this.progressBar = (ProgressBar) findViewById(R.id.prog);
        this.videoView = (VideoView) findViewById(R.id.video);
        Uri video = Uri.parse(this.videoUrl);
        this.mediaController = new MediaController(this);
        this.mediaController.setAnchorView(this.videoView);
        this.videoView.setMediaController(this.mediaController);
        this.videoView.setVideoURI(video);
        this.videoView.setOnErrorListener(new OnErrorListener() {
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Toast.makeText(VideoActivity.this.self, VideoActivity.this.self.getResources().getString(R.string.error_occured), 500).show();
                return false;
            }
        });
        this.videoView.setOnPreparedListener(new OnPreparedListener() {
            public void onPrepared(MediaPlayer arg0) {
                VideoActivity.this.progressBar.setVisibility(8);
                VideoActivity.this.videoView.start();
            }
        });
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        try {
            this.videoView.stopPlayback();
        } catch (Exception e) {
            Toast.makeText(this.self, this.self.getResources().getString(R.string.error_occured), 500).show();
        }
        super.onDestroy();
    }
}
