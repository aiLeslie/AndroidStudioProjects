package com.testBlueTooth;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.jean.mscommcontrol.R;
import com.example.jean.mscommcontrol.testBlueTooth;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

public class RelayControl extends Activity  implements OnCheckedChangeListener{	
	
	public static boolean isRecording = false;// �߳̿��Ʊ��
	
	private ToggleButton tgbt1,tgbt2,tgbt3,tgbt4;
	
	private Button releaseCtrl,btBack,btSend;
	
	private OutputStream outStream = null;
	
	private EditText _txtRead,_txtSend ;
	
	private ConnectedThread manageThread;
	private Handler mHandler;
	
	private RadioGroup radioType;
	private RadioButton rbPC;
	private String  encodeType ="GBK";
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.relaycontrol);
		
		//�����߳�����
		manageThread=new ConnectedThread();
		mHandler  = new MyHandler();
		manageThread.Start();
		
		findMyView();
		
		setMyViewListener(); 
        
		
		setTitle("����ǰ���ȹر�socket����");
		//���������ɼ�
		_txtRead.setCursorVisible(false);      //����������еĹ�겻�ɼ�
		_txtRead.setFocusable(false);           //�޽���

	}

	private void findMyView() {
		
		tgbt1=(ToggleButton) findViewById(R.id.toggleButton1);
		tgbt2=(ToggleButton) findViewById(R.id.toggleButton2);
		tgbt3=(ToggleButton) findViewById(R.id.toggleButton3);
		tgbt4=(ToggleButton) findViewById(R.id.toggleButton4);		
		
		releaseCtrl=(Button)findViewById(R.id.button1);
		btBack=(Button) findViewById(R.id.button2);
		btSend=(Button) findViewById(R.id.btSend);		
       
        radioType = (RadioGroup) findViewById(R.id.radio_type);   
        rbPC =(RadioButton) findViewById(R.id.rb_pc);  
        
		_txtRead = (EditText) findViewById(R.id.etShow);
		_txtSend = (EditText) findViewById(R.id.etSend);
		
	}

	private void setMyViewListener() {
		// ����RadioButton
		radioType.setOnCheckedChangeListener(this);
		rbPC.setChecked(true);//����ΪĬ��ѡ��Ϊ PCͨ��
		
		tgbt1.setOnClickListener(new ClickEvent());
		tgbt2.setOnClickListener(new ClickEvent());
		tgbt3.setOnClickListener(new ClickEvent());
		tgbt4.setOnClickListener(new ClickEvent());
		
		releaseCtrl.setOnClickListener(new ClickEvent());
		btBack.setOnClickListener(new ClickEvent());
		btSend.setOnClickListener(new ClickEvent());
	}




	@Override
	public void onDestroy()  
    {  
		
		try {
			testBlueTooth.btSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
        super.onDestroy();  
    }
		

	
	public void onCheckedChanged(RadioGroup group, int checkedId){
		
		switch(checkedId){
		case R.id.rb_pc:
			encodeType="GBK";
			break;
		case R.id.rb_phone:
			encodeType="UTF-8";
			break;
		default:
			break;
		}
	}
	
	private	class ClickEvent implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			
			if (v == releaseCtrl)// �ͷ�����
			{
				try {
					testBlueTooth.btSocket.close();
					manageThread.Stop();
					//testBlueTooth.serverThread.cancel();					
					//Toast.makeText(getApplicationContext(), "socket�����ѹر�", Toast.LENGTH_SHORT);
					setTitle("socket�����ѹر�");
				} catch (IOException e) {
					//Log .e(TAG,"ON RESUME: Unable to close socket during connection failure", e2);
					//Toast.makeText(getApplicationContext(), "�ر�����ʧ��", Toast.LENGTH_SHORT);
					setTitle("�ر�����ʧ��");
				}
				
			} 
			else if (v == btBack) {// ����
				
				RelayControl.this.finish();				
				
			} 
			else if (v == tgbt1)// �̵�����ť1
			{

				if (tgbt1.isChecked() == false)
					sendMessage("11");
				else if (tgbt1.isChecked() == true)
					sendMessage("10");
				
			} else if (v == tgbt2) {
				
				if (tgbt2.isChecked() == false)
					sendMessage("21");
				else if (tgbt2.isChecked() == true)
					sendMessage("20");
				
			} else if (v == tgbt3) {
				
				if (tgbt3.isChecked() == false)
					sendMessage("31");
				else if (tgbt3.isChecked() == true)
					sendMessage("30");
				
			} else if (v == tgbt4) {
				
				if (tgbt4.isChecked() == false)
					sendMessage("41");
				else if (tgbt4.isChecked() == true)
					sendMessage("40");	
				
			}else if (v == btSend){				
					
					String infoSend =_txtSend.getText().toString();	
					sendMessage(infoSend);
					setTitle("���ͳɹ�");	
					
			}
			
		}

	}

	
	  public static void setEditTextEnable(TextView view,Boolean able){
	       // view.setTextColor(R.color.read_only_color);   //����ֻ��ʱ��������ɫ
	        if (view instanceof android.widget.EditText){
	            view.setCursorVisible(able);      //����������еĹ�겻�ɼ�
	            view.setFocusable(able);           //�޽���
	            view.setFocusableInTouchMode(able);     //����ʱҲ�ò�������
	        }
	  }
	
	public void sendMessage(String message) {
		
		//����ģ��
		try {
			outStream = testBlueTooth.btSocket.getOutputStream();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//Log.e(TAG, "ON RESUME: Output stream creation failed.", e);
			Toast.makeText(getApplicationContext(), " Output stream creation failed.", Toast.LENGTH_SHORT);
			
		}
		
		

		byte[] msgBuffer = null;		
		try {
			msgBuffer = message.getBytes(encodeType);//����
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			Log.e("write", "Exception during write encoding GBK ", e1);
		}		

			
		//while(true){
			try {
				outStream.write(msgBuffer);				
				//Toast.makeText(getApplicationContext(), "����������..", Toast.LENGTH_SHORT);
				setTitle("�ɹ�����ָ��:"+message);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//Log.e(TAG, "ON RESUME: Exception during write.", e);
				Toast.makeText(getApplicationContext(), "��������ʧ��", Toast.LENGTH_SHORT);
				
			}
			
		}

	
	 class ConnectedThread extends Thread {

		
		private InputStream inStream = null;// ��������������
		private long wait;
		private Thread thread;
		
		public ConnectedThread() {
			isRecording = false;
			this.wait=50;
			thread =new Thread(new ReadRunnable());
		}

		public void Stop() {
			isRecording = false;			
			//thread.stop();
			//State bb = thread.getState();
			}
		
		public void Start() {
			isRecording = true;
			State aa = thread.getState();
			if(aa==State.NEW){
			thread.start();
			}else thread.resume();
		}
		
		private class ReadRunnable implements Runnable {
		public void run() {
			
			while (isRecording) {
				
				try {					
					inStream = testBlueTooth.btSocket.getInputStream();						
				} catch (IOException e) {
					// TODO Auto-generated catch block
					//Log.e(TAG, "ON RESUME: Output stream creation failed.", e);
					Toast.makeText(getApplicationContext(), " input stream creation failed.", Toast.LENGTH_SHORT);
					
				}						
				//char[]dd= new  char[40]; 		                      
				int length=20;
				byte[] temp = new byte[length];
				//String readStr="";
				//keep listening to InputStream while connected
				if (inStream!= null) {
				try{
					int len = inStream.read(temp,0,length-1);	
					Log.e("available", String.valueOf(len));
					//setTitle("available"+len);
					if (len > 0) {
						byte[] btBuf = new byte[len];
						System.arraycopy(temp, 0, btBuf, 0, btBuf.length);	
						
//			            for(int iii=0;iii<len;iii++){  
//			            	String  out = new Character ((char) btBuf[iii]).toString();  
//			                readStr = readStr +out;                
//
//			            }
			            
//			            InputStreamReader inputStreamReader = new InputStreamReader(inStream, "UTF-8"); 
//			            int bb = inputStreamReader.read(dd,0,dd.length-1); 
//			            String  TempStr=new String(String.valueOf(dd).trim().getBytes()); 			            

						//������
			            String readStr1 = new String(btBuf,encodeType);
			            mHandler.obtainMessage(01,len,-1,readStr1).sendToTarget();
			            
					}			             
		             Thread.sleep(wait);// ��ʱһ��ʱ�仺������
					}catch (Exception e) {
						// TODO Auto-generated catch block
						mHandler.sendEmptyMessage(00);
					}				
				
				}
				}
		}
		}	
	}
	
	 private class MyHandler extends Handler{ 
	    	@Override		    
	        public void dispatchMessage(Message msg) { 
	    		switch(msg.what){
	    		case 00:
	    			isRecording=false;
	    			_txtRead.setText("");
	    			_txtRead.setHint("socket�����ѹر�");
	    			//_txtRead.setText("inStream establishment Failed!");
	    			break;
	    			
	    		case 01:
	    			String info=(String) msg.obj;
	    			_txtRead.append(info);
	    			break;    			

	            default:	            
	                break;
	    		}
	    		}
	    	}
	
	}