package treasuremap.common.general;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Vector;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

public class MakeServerConnection {
	
	private ArrayList<String> dataToSend = new ArrayList<String>();
	private String desURL;
	private String sendObjName;
	
	public MakeServerConnection(ArrayList<String> dataToSend, String desURL, String sendObjName){
		this.dataToSend = dataToSend;
		this.desURL = desURL;
		this.sendObjName = sendObjName;
	}

	private String sendData() throws Exception {
		
		HttpPost request = makeHttpPost(dataToSend, desURL, sendObjName);
		HttpClient client = new DefaultHttpClient();
		ResponseHandler<String> reshandler = new BasicResponseHandler();

		String result = client.execute(request, reshandler);
		Log.d("meme", "result >>>>> " + result.toString());
		return result;

	}

	private HttpPost makeHttpPost(ArrayList<String> dataToSend, String url, String sendingName)
			throws Exception {

		HttpPost request = new HttpPost(url);

		Vector<NameValuePair> nameValue = new Vector<NameValuePair>();
		
		for(int i=0; i< dataToSend.size() ; i++){
			nameValue.add(new BasicNameValuePair(sendingName+i, dataToSend.get(i)));
			Log.d("meme", " nameValue >> " + nameValue.get(i).getValue());
		}

		request.setEntity(makeEntity(nameValue));

		Log.d("meme", "request >>>>> " + request.toString());

		return request;
	}

	private HttpEntity makeEntity(Vector<NameValuePair> nameValue)
			throws Exception {
		HttpEntity result = null;

		try {

			result = new UrlEncodedFormEntity(nameValue, "euc-kr");

			Log.d("meme", "result >>>>> " + result.toString());

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return result;
	}
	
	public void startConnection(){
		try{
			new serverConnectionTask().execute();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public class serverConnectionTask extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... urls) {
			String result = "";
			try {
				result = sendData();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			Log.d("meme", " result >>> " + result);
			super.onPostExecute(result);

			String serverResponseMessage = "";
			final int action;

			if (result.contains("Succeed")) {
				serverResponseMessage = "성공적으로 입력되었습니다.";
				action = 0;
			} else {
				serverResponseMessage = "죄송합니다. 네트워크 및 서버 오류 입니다. 잠시 후 다시 입력 부탁드립니다.";
				action = -1;
			}

		}
	}
}
