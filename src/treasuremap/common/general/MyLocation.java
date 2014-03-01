package treasuremap.common.general;

import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

public class MyLocation extends MapFragment {
	private Location loc;

	public double[] getMyLocation(Context context) {

		double[] result = null;

		Criteria criteria = new Criteria();

		criteria.setAccuracy(Criteria.ACCURACY_FINE); // 정확도
	
		criteria.setAltitudeRequired(false); // 고도

		criteria.setBearingRequired(false); // ..

		criteria.setSpeedRequired(false); // 속도

		LocationManager locationManager;

		locationManager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);

		// true=현재 이용가능한 공급자 제한

		String provider = locationManager.getBestProvider(criteria, true);// "gps";

		Log.d("meme", " $$$$$ >>> $$$$$ 1111 " );
		
		if (provider == null) {
			Log.d("meme", " $$$$$ >>> provider null " );
			provider = "network";
		}
		
		locationManager.requestLocationUpdates(provider, (long) 1f, 1l, loclistener);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, (long) 1f, 1l, loclistener);
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, (long) 1f, 1l, loclistener);
		
		
		loc = locationManager.getLastKnownLocation(provider);
		

		if (loc == null) {
			Log.d("meme", " == loc is null  !! == " );
			loc = locationManager.getLastKnownLocation( LocationManager.GPS_PROVIDER );
			
			if(loc == null){
				Log.d("meme", " == loc gps Null  !! == " );
//				chkGpsService(context);
				Toast.makeText(context, "실내나 GPS가 잘 잡히지 않는 곳에 있습니다. 기본위치(서울 시청)로 이동합니다.", Toast.LENGTH_SHORT).show();
//				context.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
				loc = locationManager.getLastKnownLocation( LocationManager.NETWORK_PROVIDER );
			}
//			chkGpsService(context);
		} else {

			result = new double[] {

			loc.getLatitude(), loc.getLongitude()

			};

		}

		return result;

	}

	 LocationListener loclistener = new LocationListener() {

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onLocationChanged(Location location) {
			Log.d("meme", " MyLocation: onLocationChanged !! " );
			loc = location;
			Log.d("meme", " (in onLocationChanged)  loc 2 lat :  ==>> " + loc.getLatitude());
			Log.d("meme", " (in onLocationChanged)  loc 2 longi :  ==>> " + loc.getLongitude());
		}
	};

	public static boolean chkGpsService(final Context context) {// GPS의 설정여부 확인 및 자동 설정 변경
		Log.d("meme", " $$$$$ >>> $$$$$ 44444 " );
		
		String gs = android.provider.Settings.Secure.getString(
				context.getContentResolver(),
				android.provider.Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
		Log.w("meme", "get GPs Service");

		if (gs.indexOf("gps", 0) < 0) {
			Log.w("meme", "status: off");
			// GPS OFF 일때 Dialog 띄워서 설정 화면으로 이동.
			AlertDialog.Builder gsDialog = new AlertDialog.Builder(
					context);
			gsDialog.setTitle("GPS가 꺼져 있습니다.");
			gsDialog.setMessage("GPS 설정을 변경을 해주세요.");
			gsDialog.setPositiveButton("OK",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							// GPS설정 화면으로 이동
							Intent intent = new Intent(
									android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
							intent.addCategory(Intent.CATEGORY_DEFAULT);
							context.startActivity(intent);
						}
					}).create().show();
			return false;
		} else {
			Log.w("meme", "status: on");
			return true;
		}
	}

}
