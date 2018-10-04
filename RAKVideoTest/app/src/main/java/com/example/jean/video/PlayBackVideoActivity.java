package com.example.jean.video;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.sdk.Mp4Download;
import com.example.jean.component.HeaderButtonView;
import com.example.jean.rakvideotest.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.TimeZone;

public class PlayBackVideoActivity extends Activity implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener{
	private String url = null;
	private HeaderButtonView headerBackButton;
	private TextView _headerTextView;
	private HeaderButtonView _videoDownloadButton;
	private TextView _videoPlayTime;
	private TextView _videoWholeTime;
	private HeaderButtonView _videoPlayBtn;
	private HeaderButtonView _videoNextBtn;
	private SeekBar _videoPlayProgress;
	private LinearLayout _videoBottom;
	private RelativeLayout _videoHead;
	private RelativeLayout _videoView;

	private MediaPlayerCallback mCallback = null;
	private File mDownloadMP4File = null;
	private boolean isStartPlayer = false;
	private SurfaceView surfaceView = null;
	private SurfaceHolder surfaceHolder = null;
	private static MediaPlayer mediaPlayer = null;

	private boolean isPlay = false;
	private boolean isPause = false;
	private int mDuration = 0;
	private int mSeekBarMax = 0;
	private int currentPosition = 0;

	private ProgressBar _videoLoadProgress;
	private static final String DateFormatMS = "mm:ss";
	private static final int UPDATE_PROGRESS_MSG = 0x011;// 更新进度条和计时
	private static final int HIDDEN_CONTROL_MSG = 0x012;// 隐藏控制
	private static final int COMPLETION_PLAY_MSG = 0x013;// 播放完成

	private ArrayList<String> _videoFoldersPath = new ArrayList<String>();
	private ArrayList<String> _videoFiles = new ArrayList<String>();
	private int _position;
	private String _ip;
	private String _psk;
	private int _controlPort;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN); // 全屏
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.activity_playback_video);

		headerBackButton=(HeaderButtonView)findViewById(R.id.headerBackButton);
		headerBackButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		_headerTextView=(TextView)findViewById(R.id.headerTextView);
		_videoDownloadButton=(HeaderButtonView)findViewById(R.id.videoDownload);
		_videoDownloadButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent =new Intent();
				intent.putExtra("url",url);
				intent.putExtra("path",_videoFoldersPath.get(_position).toString());
				intent.putExtra("name",_videoFiles.get(_position).toString());
				//intent.setClass(PlayBackVideoActivity.this,DownloadVideoActivity.class);
				startActivity(intent);
			}
		});
		_videoPlayTime=(TextView)findViewById(R.id.videoPlayTime);
		_videoWholeTime=(TextView)findViewById(R.id.videoWholeTime);
		_videoPlayBtn=(HeaderButtonView)findViewById(R.id.videoPlay);
		_videoPlayBtn.setOnClickListener(_videoPlayBtnClick);
		_videoNextBtn=(HeaderButtonView)findViewById(R.id.videoNext);
		_videoNextBtn.setOnClickListener(_videoNextBtnClick);
		_videoPlayProgress=(SeekBar)findViewById(R.id.videoPlayProgress);
		_videoPlayProgress.setOnSeekBarChangeListener(_videoPlayProgressChanged);
		_videoBottom=(LinearLayout)findViewById(R.id.videoBottom);
		_videoHead=(RelativeLayout)findViewById(R.id.videoHead);
		_videoView=(RelativeLayout)findViewById(R.id.videoView);
		_videoView.setOnClickListener(_videoViewClick);
		_videoLoadProgress=(ProgressBar) findViewById(R.id.videoLoadProgress);

		Intent intent=getIntent();
		_videoFoldersPath=intent.getStringArrayListExtra("path");
		_videoFiles=intent.getStringArrayListExtra("name");
		_position=intent.getIntExtra("position",0);
		Log.e("_position==>",""+_position);
		_ip=intent.getStringExtra("ip");
		_psk=intent.getStringExtra("psk");
		_controlPort=intent.getIntExtra("controlport",80);
		if(_ip.equals("127.0.0.1")==false){
			_controlPort=80;
		}

		initMediaPlayer();

		if (surfaceView == null) {
			surfaceView = new SurfaceView(this);
			surfaceView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);// 不缓冲
			surfaceView.getHolder().setKeepScreenOn(true);// 保持屏幕高亮
			surfaceView.getHolder().addCallback(new SurfaceViewCallback());
			//surfaceView.setOnClickListener(PlayVideoActivity.this);
			surfaceView.setEnabled(false);
			surfaceHolder = surfaceView.getHolder();
			_videoView.addView(surfaceView);
		}
	}

	/**
	 * 初始化播放 http://admin:admin@192.168.100.1/link//mnt/rec_folder/video/pipe0/1970Y01M04D15H/NVTDV19700104_150156.mp4
	 */
	void initMediaPlayer(){
		url="http://admin:"+_psk+"@" + _ip+":"+_controlPort +"/link/"+_videoFoldersPath.get(_position).toString()+"/"+_videoFiles.get(_position).toString();
		Log.e("url==>",url);
		currentPosition = 0;
		_headerTextView.setText(_videoFiles.get(_position).toString().replace(".mp4",""));
		_videoPlayProgress.setProgress(0);
		_videoPlayProgress.setSecondaryProgress(0);
		_videoLoadProgress.setVisibility(View.VISIBLE);
		_videoPlayTime.setText("00:00");
		mDownloadMP4File = Mp4Download.playMp4File(url, _psk,_videoFiles.get(_position).toString(), videoHandler);
		_videoBottom.setVisibility(View.VISIBLE);
		_videoHead.setVisibility(View.VISIBLE);
		mHandler.postDelayed(hiddenControlRunnable, 5000);
	}

	/**
	 * 点击界面显示或隐藏控制栏
	 */
	View.OnClickListener _videoViewClick=new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			if(_videoBottom.getVisibility()==View.VISIBLE){
				_videoBottom.setVisibility(View.GONE);
				_videoHead.setVisibility(View.GONE);
			}
			else{
				_videoBottom.setVisibility(View.VISIBLE);
				_videoHead.setVisibility(View.VISIBLE);
			}
		}
	};

	/**
	 * 播放或者暂停
	 */
	View.OnClickListener _videoPlayBtnClick=new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			if (mediaPlayer == null) {
				createMediaPlayer();
			} else {
				if (mediaPlayer != null && isPause) {//播放
					isPlay = true;
					isPause = false;
					if (currentPosition > 0) {
						mediaPlayer.seekTo(currentPosition);
					}
					mediaPlayer.start();
					mHandler.post(updateProgressRunnable);
					_videoPlayBtn.setImageResource(R.drawable.fullplayer_icon_pause);
				}
				else if (mediaPlayer != null && mediaPlayer.isPlaying()) {//暂停
					_videoLoadProgress.setVisibility(View.GONE);
					isPlay = false;
					isPause = true;
					mediaPlayer.pause();
					_videoPlayBtn.setImageResource(R.drawable.fullplayer_icon_player);
					currentPosition = mediaPlayer.getCurrentPosition();
					mHandler.removeCallbacks(updateProgressRunnable);
				}
			}
		}
	};

	/**
	 * 快进或者快退
	 */
	SeekBar.OnSeekBarChangeListener _videoPlayProgressChanged =new SeekBar.OnSeekBarChangeListener() {
		@Override
		public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
			if (fromUser) {
				int milliseconds = countPosition(progress);
				_videoPlayTime.setText(getStringByFormat(milliseconds, DateFormatMS));
			}
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			mHandler.removeCallbacks(updateProgressRunnable);
		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			if (mediaPlayer != null) {
				currentPosition = countPosition(seekBar.getProgress());
				mediaPlayer.seekTo(currentPosition);
				mHandler.post(updateProgressRunnable);
			}
		}
	};

	/**
	 * 播放下一个视频文件
	 */
	View.OnClickListener _videoNextBtnClick=new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			_playNextVideo();
		}
	};
	//播放下一个视频文件
	void _playNextVideo(){
		Log.e("_position==>",""+_position);
		if(_position>=_videoFiles.size()-1){
			Toast.makeText(getApplication(),"已是最后一个视频",Toast.LENGTH_SHORT).show();
			return;
		}
		Toast.makeText(getApplication(),"正在跳转到下一个视频...",Toast.LENGTH_SHORT).show();
		_position++;
		isPlay = false;
		mHandler.removeCallbacks(updateProgressRunnable);
		if (mediaPlayer != null) {
			mediaPlayer.stop();
			mediaPlayer.release();
			mediaPlayer = null;
		}
		initMediaPlayer();
	}

	/**
	 * 视频下载时的handler
	 */
	private Handler videoHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
				// 有MP4的mdat数据时，创建播放器
				case Mp4Download.PLAYER_MP4_MSG:
					isStartPlayer = true;
					Log.d("MediaPlayer==>","start...");
					createMediaPlayer(mDownloadMP4File);
					break;
				case Mp4Download.DOWNLOAD_MP4_COMPLETION:
					Log.d("MediaPlayer==>","completion...");
					break;
				case Mp4Download.DOWNLOAD_MP4_FAIL:
					Log.d("MediaPlayer==>","fail...");
					break;
				default:
					break;
			}
		}
	};

	/**
	 * SurfaceView监听
	 */
	private class SurfaceViewCallback implements SurfaceHolder.Callback {

		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			createMediaPlayer();
		}

		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			destroy();
		}
	}

	/**
	 * 创建 mediaPlayer
	 */
	private void createMediaPlayer() {
		createMediaPlayer(null);
	}
	private void createMediaPlayer(File videoFile) {
		if (url == null && videoFile == null && !isStartPlayer) {
			return;
		}

		if (mediaPlayer == null) {
			mediaPlayer = new MediaPlayer();
		}
		mediaPlayer.reset();
		mediaPlayer.setOnCompletionListener(this);
		mediaPlayer.setOnPreparedListener(this);
		mediaPlayer.setDisplay(surfaceHolder);// 把视频显示到surfaceView上
		if (videoFile != null) {
			setDataSource(videoFile);
			Log.d("mediaPlayer==>","Play with file");
		} else {
			setDataSource(url);
			Log.d("mediaPlayer==>","Play with url");
		}
		mediaPlayer.prepareAsync();// 准备播放
	}

	/**
	 * 设置视频源来自url
	 */
	private void setDataSource(String src) {
		try {
			if (src.startsWith("http://")) {
				mediaPlayer.setDataSource(this, Uri.parse(src));
			} else {
				setDataSource(new File(src));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 设置视频源来自本地文件
	 */
	private void setDataSource(File file) {
		try {
			FileInputStream fis = new FileInputStream(file);
			mediaPlayer.setDataSource(fis.getFD());// 设置播放路径
			fis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 更新UI
	 */
	int _lastPosition=0;
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
				case UPDATE_PROGRESS_MSG:// 更新播放进度和计时器
					if (mediaPlayer != null && mediaPlayer.isPlaying()) {
						isPlay = true;
						currentPosition = mediaPlayer.getCurrentPosition();
						if(currentPosition==_lastPosition){
							_videoLoadProgress.setVisibility(View.VISIBLE);
							break;
						}
						_videoLoadProgress.setVisibility(View.GONE);
						_videoPlayProgress.setProgress(countProgress(currentPosition));
						_videoPlayTime.setText(getStringByFormat(currentPosition, DateFormatMS));
						_videoWholeTime.setText(getStringByFormat(mDuration, DateFormatMS));
						_lastPosition=currentPosition;
					}
					break;
				case HIDDEN_CONTROL_MSG:// 隐藏播放控制控件
					_videoBottom.setVisibility(View.GONE);
					_videoHead.setVisibility(View.GONE);
					break;
				case COMPLETION_PLAY_MSG:// 完成播放
					//finish();
					if (mCallback != null) {
						mCallback.onCompletion();
					}
					_playNextVideo();
					break;

				default:
					break;
			}
		}
	};

	public interface MediaPlayerCallback {
		public void onCompletion();
	}

	public void setMediaPlayerCallback(MediaPlayerCallback callback) {
		mCallback = callback;
	}

	/**
	 *  mediaPlayer 播放完成
	 */
	@Override
	public void onCompletion(MediaPlayer mp) {
		try {
				isPlay = false;
				_videoPlayProgress.setProgress(countProgress(mDuration));
				_videoPlayTime.setText(getStringByFormat(mDuration, DateFormatMS));
				_videoPlayBtn.setImageResource(R.drawable.fullplayer_icon_player);
				_videoBottom.setVisibility(View.VISIBLE);
				_videoHead.setVisibility(View.VISIBLE);
				mHandler.removeCallbacks(updateProgressRunnable);
				mHandler.sendEmptyMessageDelayed(COMPLETION_PLAY_MSG, 500);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 *  mediaPlayer 准备播放
	 */
	@Override
	public void onPrepared(MediaPlayer mp) {
		if (isPause) {

		} else {
			mediaPlayer.start();
			_videoPlayBtn.setImageResource(R.drawable.fullplayer_icon_pause);

			if (currentPosition > 0) {
				mediaPlayer.seekTo(currentPosition);
			}
		}
		//_videoLoadProgress.setVisibility(View.GONE);
		surfaceView.setEnabled(true);
		mHandler.post(updateProgressRunnable);

		if (mSeekBarMax == 0 || mDuration == 0) {
			mSeekBarMax = _videoPlayProgress.getMax();
			mDuration = mediaPlayer.getDuration();
		}
		//监听缓冲区
		mp.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
			@Override
			public void onBufferingUpdate(MediaPlayer mp, int percent) {
				_videoPlayProgress.setSecondaryProgress(percent);
			}
		});
	}


	private void destroy() {
		if (mediaPlayer != null && isPlay) {
			mediaPlayer.pause();
			currentPosition = mediaPlayer.getCurrentPosition();
			mHandler.removeCallbacks(updateProgressRunnable);
		}
	}

	/**
	 * 更新进度条线程
	 */
	private Runnable updateProgressRunnable = new Runnable() {

		@Override
		public void run() {
			mHandler.sendEmptyMessage(UPDATE_PROGRESS_MSG);
			mHandler.postDelayed(updateProgressRunnable, 1000);
		}
	};

	/**
	 * 隐藏控制线程
	 */
	private Runnable hiddenControlRunnable = new Runnable() {

		@Override
		public void run() {
			mHandler.sendEmptyMessage(HIDDEN_CONTROL_MSG);
		}
	};

	/**
	 * 计算播放进度
	 *
	 * @param position
	 * @return
	 */
	private int countProgress(int position) {
		if (mDuration == 0) {
			return 0;
		}

		return position * mSeekBarMax / mDuration;
	}

	/**
	 * 计算播放位置
	 *
	 * @param progress
	 * @return
	 */
	private int countPosition(int progress) {
		if (mSeekBarMax == 0) {
			return 0;
		}

		return progress * mDuration / mSeekBarMax;
	}

	/**
	 * 描述：获取milliseconds表示的日期时间的字符串.
	 *
	 * @param milliseconds
	 * @param format
	 *            格式化字符串，如："yyyy-MM-dd HH:mm:ss"
	 * @return String 日期时间字符串
	 */
	private String getStringByFormat(long milliseconds, String format) {
		String thisDateTime = null;
		try {
			SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
			mSimpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+0"));
			thisDateTime = mSimpleDateFormat.format(milliseconds);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return thisDateTime;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mHandler.removeCallbacks(updateProgressRunnable);
		mHandler.removeCallbacks(hiddenControlRunnable);
		if (mediaPlayer != null) {
			mediaPlayer.stop();
			mediaPlayer.release();
			mediaPlayer = null;
			currentPosition = 0;
		}
		Settings.System.getInt(getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 0);
		System.gc();
	}
}
