package com.example.jean.video;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.demo.sdk.Lx520;
import com.example.jean.component.AdapterList;
import com.example.jean.rakvideotest.R;

import java.util.ArrayList;
import java.util.HashMap;

public class PlayBackVideoListActivity extends Activity {
	private com.example.jean.component.MainMenuButton _backButton;
	private ListView _listVideo;
	private AdapterList _videoVideoListAdapter;
	private ArrayList<HashMap<String, Object>> _videoVideoListItem;
	private ProgressDialog _progressDialog;
	private String _ip;
	private String _name;
	private String _psk;
	private ArrayList<String> _videoFoldersPath = new ArrayList<String>();
	private ArrayList<String> _videoFiles = new ArrayList<String>();
	private int _controlPort=80;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_playback_video_list);

		_backButton= (com.example.jean.component.MainMenuButton)findViewById(R.id.playback_video_list_back);
		_backButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		_listVideo = (ListView)findViewById(R.id.playback_video_list_video);
		_videoVideoListItem = new ArrayList<HashMap<String, Object>>();
		_videoVideoListAdapter = new AdapterList(this,_videoVideoListItem, R.layout.get_playback_list_item,
				new String[]{"img","name"},
				new int[]{R.id.Video_img,R.id.Video_name});
		_listVideo.setAdapter(_videoVideoListAdapter);
		_listVideo.setOnItemClickListener(_listVideoClick);

		Intent intent=getIntent();
		_ip=intent.getStringExtra("ip");
		_name=intent.getStringExtra("name");
		_psk=intent.getStringExtra("psk");
		_controlPort=intent.getIntExtra("controlport",80);
		if(_ip.equals("127.0.0.1")==false){
			_controlPort=80;
		}
		_progressDialog = ProgressDialog.show(PlayBackVideoListActivity.this, "",
				getString(R.string.get_video_list), true);
		getVideos(_name);
	}

	@Override
	public void onBackPressed() {
		finish();
	}

	/**
	 * Get Videos
	 */
	private void getVideos(String path){
		if((_name==null)||(_name.equals("")==true)){
			return;
		}
		_videoFiles.clear();
		_videoFoldersPath.clear();
		final Lx520 lx520=new Lx520(_ip+":"+_controlPort,_psk);
		lx520.setOnResultListener(new Lx520.OnResultListener() {
			@Override
			public void onResult(Lx520.Response result) {
				if(result.statusCode==200){
					if(result.type==20){
						Find_Str2(_videoFoldersPath,_videoFiles,result.body,"\"folder\":\"","\"filename\":\"","\"");
						int _size=_videoFiles.size();
						for(int i=0;i<_size;i++){
							AddVideoListItem(_videoFiles.get(i).toString().replace(".mp4",""));
						}
						if (_progressDialog != null && _progressDialog.isShowing())
							_progressDialog.dismiss();
					}
				}
				else{
					if (_progressDialog != null && _progressDialog.isShowing())
						_progressDialog.dismiss();
				}
			}
		});
		lx520.Get_Video_List(path);
	}

	/**
	 * Add Video List
	 */
	private void AddVideoListItem(String name)
	{
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("img", R.drawable.video_icon);
		map.put("name", name);
		_videoVideoListItem.add(map);
		_videoVideoListAdapter.notifyDataSetChanged();
	}

	/**
	 *	Play Video
	 */
	AdapterView.OnItemClickListener _listVideoClick=new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Intent intent=new Intent();
			intent.putStringArrayListExtra("path",_videoFoldersPath);
			intent.putStringArrayListExtra("name",_videoFiles);
			intent.putExtra("position",position);
			intent.putExtra("ip", _ip);
			intent.putExtra("psk", _psk);
			intent.putExtra("controlport", _controlPort);
			intent.setClass(PlayBackVideoListActivity.this,PlayBackVideoActivity.class);
			startActivity(intent);
		}
	};

	/**
	 * Find_Str2
	 **/
	private void Find_Str2(ArrayList<String> results1,ArrayList<String> results2,String srcStr,String keyStr1,String keyStr2,String endStr)
	{
		while(true) {
			int index = srcStr.indexOf(keyStr1);
			if (index != -1){
				int index1 = srcStr.indexOf(endStr, keyStr1.length() + index);
				if (index1 != -1) {
					String res1 = srcStr.substring(keyStr1.length() + index, index1);
					results1.add(res1);
					int index2 = srcStr.indexOf(keyStr2);
					if(index2!=-1){
						int index3 = srcStr.indexOf(endStr, keyStr2.length() + index2);
						if(index3!=-1){
							String res2 = srcStr.substring(keyStr2.length() + index2, index3);
							results2.add(res2);
							srcStr=srcStr.substring(index3);
						}
						else {
							break;
						}
					}
					else {
						break;
					}
				}
				else {
					break;
				}
			}
			else {
				break;
			}
		}
	}

}
