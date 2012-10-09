package es.netrunners;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

public class VideoRecorderActivity extends Activity {

	VideoView video;
	private static int RECORD_VIDEO = 1;
	private static int HIGH_VIDEO_QUALITY = 1;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		video = (VideoView) findViewById(R.id.videoView);
	}

	public void startRecording(View view) {
		Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, HIGH_VIDEO_QUALITY);
		startActivityForResult(intent, RECORD_VIDEO);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == RECORD_VIDEO) {
			try {
				AssetFileDescriptor videoAsset = getContentResolver()
						.openAssetFileDescriptor(data.getData(), "r");
				FileInputStream fis = videoAsset.createInputStream();
				File tmpFile = getFileName("3gp");
				FileOutputStream fos = new FileOutputStream(tmpFile);

				byte[] buf = new byte[1024];
				int len;
				while ((len = fis.read(buf)) > 0) {
					fos.write(buf, 0, len);
				}
				fis.close();
				fos.close();
				video.setVideoPath(tmpFile.getAbsolutePath());
				video.setMediaController(new MediaController(this));
				video.start();
			} catch (IOException e) {
				Toast.makeText(getApplicationContext(), e.getMessage(),
						Toast.LENGTH_LONG).show();
			}
		}
	}

	private File getFileName(String ext) {
		File mFile = null;
		File path = new File(Environment.getExternalStorageDirectory()
				.getPath());
		try {
			mFile = File.createTempFile("temp", "." + ext, path);
		} catch (IOException e) {
			Toast.makeText(getApplicationContext(), e.getMessage(),
					Toast.LENGTH_LONG).show();
		}
		return mFile;
	}

}