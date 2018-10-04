package com.example.jean.video;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.example.jean.component.RequestPermission;
import com.example.jean.rakvideotest.R;
import com.xinlan.imageedit.editimage.EditImageActivity;

public class photolistActivity extends Activity
{
	private LinearLayout exit_photolist;
	private TextView photodelete;
	public static adapter_my listItemAdapter; // ListView的适配器
	public static ArrayList<HashMap<String, Object>> listItem; // ListView的数据源，这里是一个HashMap的列表
	private ListView Photolist;
	public static ImageView photoshow;
	public static int photo_num= 0;
	public static boolean deleteflag=false;
	int delete_num;
	String delete_photo;
	private long Creat_time;//照片上次被修改时间
	private String Recourd_time="";//记录照片拍摄时间
	private int Dataline=-1;//记录日期行
	Dialog deleteDialog = null;
	private RequestPermission _requestPermission;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.photo_list);
		_requestPermission=new RequestPermission();
		deleteDialog = new Dialog(this, R.style.myDialogTheme);
		exit_photolist=(LinearLayout)findViewById(R.id.exit_photolist);
		exit_photolist.setOnClickListener(new Exit_photolist_Click());
		photodelete=(TextView)findViewById(R.id.delete);
		photodelete.setOnClickListener(new Del_photolist_Click());
		Photolist=(ListView)findViewById(R.id.photolist);
		// 绑定listview
		listItem = new ArrayList<HashMap<String, Object>>();
		// wmii_item为每一项的布局文件名字，最后一个参数 int[] 为每一项的布局文件中的控件ID
		listItemAdapter = new adapter_my(this,listItem, R.layout.photo_listitem,
				new String[]
				{ "photo", "data","del","date"}, new int[]
				{ R.id.photonum, R.id.data, R.id.del,R.id.date});
		Photolist.setAdapter(listItemAdapter);
		// 注册点击的事件
		Photolist.setOnItemClickListener(list_item_clik);
		//Photolist.setOnItemLongClickListener(list_item_longclik);
		// 添加长按点击事件
		//Photolist.setOnCreateContextMenuListener(list_item_create_click);
		LayoutInflater inflater=getLayoutInflater();
		View photos=inflater.inflate(R.layout.photo_listitem, null);
		photoshow=(ImageView)photos.findViewById(R.id.photonum);
			
		getFiles(VideoPlay.photofile_path);//获取文件夹下所有图片信息，并添加到列表
	}
	@Override
	protected void onDestroy()
	{
		photo_del_mode=false;
		photo_del_num=0;
		Arrays.fill(adapter_my.flag, (byte)0);//退出列表界面时，清空选中标记
		super.onDestroy();
	}
	/*********************************************************************************************************
	 ** 功能说明：获取本地文件，添加到列表
	 ** 传入参数：无
	 ** 得到参数：无
	 *********************************************************************************************************/
	private int mFileSize = 0;
	private List<String> mPathString = new ArrayList<String>();
	ArrayList<Map<String, Object>> files_info;
	private void getFiles(String string)
	{
		// TODO Auto-generated method stub
		//sortFolder(string);
		if(string==null){
			_requestPermission.requestWriteSettings(photolistActivity.this);
			return;
		}
		File file = new File(string);
		if(file==null){
			_requestPermission.requestWriteSettings(photolistActivity.this);
			return;
		}
		File[] files = file.listFiles();
		if(files==null){
			_requestPermission.requestWriteSettings(photolistActivity.this);
			return;
		}
		mFileSize = files.length;

		if(files!=null)
		{
			for (int i = 0; i < mFileSize; i++) {
				mPathString.add(files[i].getAbsolutePath());
			}
			Collections.sort(mPathString);
//			Collections.reverse(mPathString);

			for (int j = 0; j < mFileSize; j++)
			{
				Log.e("name==>", mPathString.get(j).toString());
				File file1 = new File(mPathString.get(j).toString());
				String name =file1.getName();//获得图片名
				Creat_time=file1.lastModified();
				SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd");
				String tsForm = formatter.format(new Date(Creat_time));
				if(Recourd_time.equals(tsForm)==false)
				{
					AddListItem(0,null, tsForm, 0);//添加日期
					Recourd_time = tsForm;
				}
				Bitmap _bitmap=getImageThumbnail(mPathString.get(j).toString(), 120, 120);
				AddListItem(1,_bitmap, name.substring(0, name.length() - 4), 1);
			}
		}
	}

	/**
	 * 根据指定的图像路径和大小来获取缩略图
	 * 此方法有两点好处：
	 *     1. 使用较小的内存空间，第一次获取的bitmap实际上为null，只是为了读取宽度和高度，
	 *        第二次读取的bitmap是根据比例压缩过的图像，第三次读取的bitmap是所要的缩略图。
	 *     2. 缩略图对于原图像来讲没有拉伸，这里使用了2.2版本的新工具ThumbnailUtils，使
	 *        用这个工具生成的图像不会被拉伸。
	 * @param imagePath 图像的路径
	 * @param width 指定输出图像的宽度
	 * @param height 指定输出图像的高度
	 * @return 生成的缩略图
	 */
	private Bitmap getImageThumbnail(String imagePath, int width, int height) {
		Bitmap bitmap = null;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		// 获取这个图片的宽和高，注意此处的bitmap为null
		bitmap = BitmapFactory.decodeFile(imagePath, options);
		options.inJustDecodeBounds = false; // 设为 false
		// 计算缩放比
		int h = options.outHeight;
		int w = options.outWidth;
		int beWidth = w / width;
		int beHeight = h / height;
		int be = 1;
		if (beWidth < beHeight) {
			be = beWidth;
		} else {
			be = beHeight;
		}
		if (be <= 0) {
			be = 1;
		}
		options.inSampleSize = be;
		// 重新读入图片，读取缩放后的bitmap，注意这次要把options.inJustDecodeBounds 设为 false
		bitmap = BitmapFactory.decodeFile(imagePath, options);
		// 利用ThumbnailUtils来创建缩略图，这里要指定要缩放哪个Bitmap对象
		bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
				ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
		return bitmap;
	}
	/*********************************************************************************************************
	** 功能说明：返回上一个界面
	** 传入参数：无
	** 得到参数：无      
	*********************************************************************************************************/		
	 class Exit_photolist_Click implements OnClickListener
	 {
		@Override
		public void onClick(View arg0)
		{
			photo_del_num=0;
			Arrays.fill(adapter_my.flag, (byte)0);//退出列表界面时，清空选中标记   
			photolistActivity.this.finish();
		}		 
	 }

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			photo_del_num=0;
			Arrays.fill(adapter_my.flag, (byte)0);//退出列表界面时，清空选中标记
			photolistActivity.this.finish();
		}
		return false;
	}

	/*********************************************************************************************************
	 ** 功能说明：删除操作
	 ** 传入参数：无
	 ** 得到参数：无
	 *********************************************************************************************************/
	public static boolean photo_del_mode=false;
	int photo_del_num=0;
	 class Del_photolist_Click implements OnClickListener
	 {
		@Override
		public void onClick(View arg0)
		{
			if(photodelete.getText().equals("Edit"))
			{
				photo_del_mode=true;
				photodelete.setText("Delete");
			}
			else
			{
				if(photo_del_num==0)
				{
					photo_del_mode = false;
					photo_del_num = 0;
					Arrays.fill(adapter_my.flag, (byte) 0);//退出列表界面时，清空选中标记
					photodelete.setText("Edit");
					listItemAdapter.notifyDataSetChanged();
					return;
				}
				//获取设备提示
				LayoutInflater delete_Dialog_inflater =getLayoutInflater();
				View delete_Dialog_admin=delete_Dialog_inflater.inflate(R.layout.delete_admin, (ViewGroup) findViewById(R.id.delete_admin1));
				TextView device_delete_admin_title =(TextView)delete_Dialog_admin.findViewById(R.id.del_title);
				TextView device_delete_admin_note =(TextView)delete_Dialog_admin.findViewById(R.id.del_note);
				TextView device_delete_admin_ok =(TextView)delete_Dialog_admin.findViewById(R.id.del_ok_btn);
				TextView device_delete_admin_cancel=(TextView)delete_Dialog_admin.findViewById(R.id.del_cancel_btn);
				device_delete_admin_title.setText(R.string.delete_photo_title_note);
				device_delete_admin_note.setText(R.string.delete_photo_admin_note);
				device_delete_admin_ok.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						int line = listItem.size();
						int del_num = 0;
						for (int i = line; i >= 0; i--) {
							if (adapter_my.flag[i] == 1) {
								if ((i > 0) && (i < line)) {
									del_num++;
									delete_photo = listItem.get(i).get("data").toString();
									File file = new File(VideoPlay.photofile_path + "/" + delete_photo + ".jpg");
									DeleteFile(file);
									listItem.remove(i);
									if (listItem.get(i - 1).get("photo") == null)//检测到上一行是日期行则删除日期行
									{
										if (i + del_num == line)//最后一行
										{
											//删除日期行
											listItem.remove(i - 1);
										} else //不是最后一行
										{
											if (listItem.get(i).get("photo") == null)//检测到下一行是日期行则删除日期行
											{
												//删除日期行
												listItem.remove(i - 1);
											}
										}
									}
								}

								adapter_my.flag[i] = 0;
							}
						}
						photo_del_mode = false;
						photo_del_num = 0;
						Arrays.fill(adapter_my.flag, (byte) 0);//退出列表界面时，清空选中标记
						photodelete.setText("Edit");
						listItemAdapter.notifyDataSetChanged();
						deleteDialog.dismiss();
					}
				});
				device_delete_admin_cancel.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						photo_del_mode = false;
						photo_del_num = 0;
						Arrays.fill(adapter_my.flag, (byte) 0);//退出列表界面时，清空选中标记
						photodelete.setText("Edit");
						listItemAdapter.notifyDataSetChanged();
						deleteDialog.dismiss();
					}
				});
				deleteDialog.setCanceledOnTouchOutside(false);
				deleteDialog.setContentView(delete_Dialog_admin);
				deleteDialog.show();
			}

			listItemAdapter.notifyDataSetChanged();

		}		 
	 }	
	 
	 /*********************************************************************************************************
	  ** 功能说明：扫描到的模块，添加到列表
	  ********************************************************************************************************/
	 	public static void AddListItem(int photo,Bitmap bitmap, String data, int chek)
	 	{
	 		HashMap<String, Object> map = new HashMap<String, Object>();
	 		// 添加图片
	 		if(photo==0)
	 		{
				map.put("date", data);//日期栏则填充数据
	 		}
	 		else 
	 		{
				map.put("date", "");//不是日期栏则填充空
	 			map.put("photo", bitmap);
			}
	 		
	 		map.put("data", data);
			map.put("del", R.drawable.choose);
	 
	 		listItem.add(map);
	 		// 刷新列表
	 		listItemAdapter.notifyDataSetChanged();
	 	}
	 /*********************************************************************************************************
	  ** 功能说明：list点击的事件，进行模块认证
	  ********************************************************************************************************/
	 	OnItemClickListener list_item_clik = new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3)
			{
				//listItemAdapter.setSelectedPosition(arg2);
				String photoString=listItem.get(arg2).get("data").toString();

				if(photo_del_mode)
				{
					if(adapter_my.flag[arg2]==0)
					{
						adapter_my.flag[arg2]=1;
						photo_del_num++;
					}
					else
					{
						adapter_my.flag[arg2]=0;
						photo_del_num--;
					}
					listItemAdapter.notifyDataSetChanged();// 刷新列表
					photodelete.setText("Delete("+photo_del_num+")");
				}
				else
				{
					if(listItem.get(arg2).get("photo")!=null)//非日期行
					{
						Intent intent = new Intent();
						intent.putExtra("photodata", photoString);
						intent.setClass(photolistActivity.this, EditImageActivity.class);
						startActivity(intent);
					}
				}
			}
		};
	 /*********************************************************************************************************
	  ** 功能说明：list长按的事件，进行模块认证
	  ********************************************************************************************************/
	// list 长按事件，获取IP
	OnItemLongClickListener	list_item_longclik = new OnItemLongClickListener()
	{

		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
		{
		// TODO Auto-generated method stub
		delete_photo = listItem.get(arg2).get("data").toString();//获取当前照片名字
		Dataline=arg2;
		delete_num=arg2;
		Photolist.showContextMenu();//弹出菜单
		return true;
		}	 		
	};
	// list 长按后创建菜单选项		
	OnCreateContextMenuListener list_item_create_click = new OnCreateContextMenuListener()
	{
		@Override
		public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo)
		{
		// 长按后 显示的内容
		// TODO Auto-generated method stub
		if(listItem.get(Dataline).get("photo")!=null)//非日期行
		{
			//menu.setHeaderTitle("模块管理");
			menu.add(0, 0, 0, "删除当前照片");
			menu.add(0, 1, 0, "删除所有照片");
		}
		}
	};
	// 长按list的内容显示出的菜单响应函数
	@Override
	public boolean onContextItemSelected(MenuItem item)
	{
        if(item.getItemId()==0)
        {        	       	
        	int line=listItem.size();
        	if((Dataline>0)&&(Dataline<line))
        	{       		           	
        		File file = new File(VideoPlay.photofile_path+"/"+delete_photo+".jpg");
        		DeleteFile(file); 
        		listItem.remove(Dataline);
        		if(listItem.get(Dataline-1).get("photo")==null)//检测到上一行是日期行则删除日期行
				{
					if(Dataline+1==line)//是最后一行
					{
						//删除日期行
		        		listItem.remove(Dataline-1);	
					}
					else//不是最后一行
					{
		        		if(listItem.get(Dataline).get("photo")==null)//检测到下一行是日期行则删除日期行
		        		{
		        			//删除日期行
			        		listItem.remove(Dataline-1);
		        		}
					}
				}
        		listItemAdapter.notifyDataSetChanged();
        	}        	    	                   	
        }
        if(item.getItemId()==1)
        {       	
        	AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("删除所有照片");
			//builder.setIcon(R.drawable.dialog_alert_icon);
			builder.setMessage("确实要删除所有照片吗？")
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener()
							{
							  public void onClick(DialogInterface dialog,int which)
								{
								  listItem.clear();
						          listItemAdapter.notifyDataSetChanged();
						          File file = new File(VideoPlay.photofile_path);
						          DeleteFile(file); 
								}
							})
					.setNegativeButton("取消", null)
					.show();       	
        }
        return super.onContextItemSelected(item);
	}
	
	 public void DeleteFile(File file) 
	 { 
	        if (file.exists() == false) 
	        { 
	            return; 
	        } 
	        else 
	        { 
	            if (file.isFile()) 
	            { 
	                file.delete(); 
	                return; 
	            } 
	            if (file.isDirectory()) 
	            { 
	                File[] childFile = file.listFiles(); 
	                if (childFile == null || childFile.length == 0) 
	                { 
	                    file.delete(); 
	                    return; 
	                } 
	                for (File f : childFile) 
	                { 
	                    DeleteFile(f); 
	                } 
	                file.delete(); 
	            } 
	        } 
	    } 
	/*********************************************************************************************************
	 ** 功能说明：UI界面消息显示
	 ********************************************************************************************************/
	public void DisplayToast(String str)
	{
		Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
	}

	/*********************************************************************************************************
	 ** 功能说明：没有访问权限
	 ********************************************************************************************************/
	void NoteDialog(){
		LayoutInflater delete_Dialog_inflater =getLayoutInflater();
		View delete_Dialog_admin=delete_Dialog_inflater.inflate(R.layout.delete_admin, (ViewGroup) findViewById(R.id.delete_admin1));
		TextView device_delete_admin_title =(TextView)delete_Dialog_admin.findViewById(R.id.del_title);
		TextView device_delete_admin_note =(TextView)delete_Dialog_admin.findViewById(R.id.del_note);
		TextView device_delete_admin_ok =(TextView)delete_Dialog_admin.findViewById(R.id.del_ok_btn);
		TextView device_delete_admin_cancel=(TextView)delete_Dialog_admin.findViewById(R.id.del_cancel_btn);
		device_delete_admin_title.setText(R.string.permission_title_note);
		device_delete_admin_note.setText(R.string.permission_admin_note);
		device_delete_admin_ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				deleteDialog.dismiss();
				finish();
			}
		});
		device_delete_admin_cancel.setVisibility(View.GONE);
		deleteDialog.setCanceledOnTouchOutside(false);
		deleteDialog.setContentView(delete_Dialog_admin);
		deleteDialog.show();
	}
	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode == _requestPermission.WRITE_EXTERNAL_STORAGE) {
			int grantResult = grantResults[0];
			boolean granted = grantResult == PackageManager.PERMISSION_GRANTED;
			if (granted){
				_requestPermission.createSDCardDir(MainActivity.RAKVideo);
				_requestPermission.createSDCardDir(MainActivity.RAKVideo_Photo);
				_requestPermission.createSDCardDir(MainActivity.RAKVideo_Video);
				_requestPermission.createSDCardDir(MainActivity.RAKVideo_Voice);
				_requestPermission.createSDCardDir(MainActivity.RAKVideo_PlayBack);
			}else{
			}
		}
	}
}
