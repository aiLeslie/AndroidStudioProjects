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

public class PlayBackFolderListActivity extends Activity {
	private com.example.jean.component.MainMenuButton _backButton;
	private ListView _listFolder;
	private AdapterList _videoFolderListAdapter;
	private ArrayList<HashMap<String, Object>> _videoFolderListItem;
	private ProgressDialog _progressDialog;
	private String _ip;
	private String _psk;
	private ArrayList<String> _videoFolders = new ArrayList<String>();
	private int _controlPort=80;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_playback_folder_list);

		_backButton= (com.example.jean.component.MainMenuButton)findViewById(R.id.playback_folder_list_back);
		_backButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		_listFolder = (ListView)findViewById(R.id.playback_folder_list_folder);
		_videoFolderListItem = new ArrayList<HashMap<String, Object>>();
		_videoFolderListAdapter = new AdapterList(this,_videoFolderListItem, R.layout.get_playback_list_item,
				new String[]{"img","name"},
				new int[]{R.id.Video_img,R.id.Video_name});
		_listFolder.setAdapter(_videoFolderListAdapter);
		_listFolder.setOnItemClickListener(_listFolderClick);

		Intent intent=getIntent();
		_ip=intent.getStringExtra("ip");
		_psk=intent.getStringExtra("psk");
		_controlPort=intent.getIntExtra("controlport",80);
		if(_ip.equals("127.0.0.1")==false){
			_controlPort=80;
		}
		_progressDialog = ProgressDialog.show(PlayBackFolderListActivity.this, "",
				getString(R.string.get_folder_list), true);
		getFolders();
	}

	@Override
	public void onBackPressed() {
		finish();
	}


	/**
	 * Get Folders
	 */
	private void getFolders(){
		_videoFolders.clear();
		Lx520 lx520=new Lx520(_ip+":"+_controlPort,_psk);
		lx520.setOnResultListener(new Lx520.OnResultListener() {
			@Override
			public void onResult(Lx520.Response result) {
				if(result.statusCode==200){
					if(result.type==19){
						Find_Str(_videoFolders,result.body,"\"folder\":\"","\"");
						int _size=_videoFolders.size();
						for(int i=0;i<_size;i++){
							AddVideoListItem(_videoFolders.get(i).toString());
						}
					}
					if (_progressDialog != null && _progressDialog.isShowing())
						_progressDialog.dismiss();
				}
				else{
					if (_progressDialog != null && _progressDialog.isShowing())
						_progressDialog.dismiss();
				}
			}
		});
		lx520.Get_Video_Folder_List();
	}

	/**
	 * Add Folders List
	 */
	private void AddVideoListItem(String name)
	{
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("img", R.drawable.dl_downloaded_directory);
		map.put("name", name);
		_videoFolderListItem.add(map);
		_videoFolderListAdapter.notifyDataSetChanged();
	}

	/**
	 *	Get Videos
	 */
	AdapterView.OnItemClickListener _listFolderClick=new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			String name=_videoFolderListItem.get(position).get("name").toString();
			Log.e("name==>",name);
			Intent intent=new Intent();
			intent.putExtra("name",name);
			intent.putExtra("ip", _ip);
			intent.putExtra("psk", _psk);
			intent.putExtra("controlport", _controlPort);
			intent.setClass(PlayBackFolderListActivity.this,PlayBackVideoListActivity.class);
			startActivity(intent);
		}
	};

	/**
	 * Find_Str
	 **/
	private void Find_Str(ArrayList<String> results,String srcStr,String keyStr,String endStr)
	{
		while(true) {
			int index = srcStr.indexOf(keyStr);
			if (index != -1){
				int index1 = srcStr.indexOf(endStr, keyStr.length() + index);
				if (index1 != -1) {
					String res = srcStr.substring(keyStr.length() + index, index1);
					results.add(res);
					srcStr=srcStr.substring(index1);
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
