package treasuremap.common.activities;

import java.util.ArrayList;

import treasuremap.common.general.Common;
import treasuremap.common.general.MakeServerConnection;
import treasuremap.common.general.MyLocation;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.treasuremap.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends FragmentActivity {

	private GoogleMap gMap;
	private LatLng my_location;
	private double[] my_gps;
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        init();
        
        Button btnSearch = (Button)findViewById(R.id.btnSearch);
        final EditText edSearch = (EditText)findViewById(R.id.edSearch);
        
        btnSearch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				passSearchKey(edSearch.getText().toString());
			}
		});
    }
    
	public void init() {
		if (gMap == null) {
			
			gMap = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map)).getMap();
			if (gMap != null) {

				gMap.setMyLocationEnabled(true);

				// gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new
				// LatLng(37.334087, 126.581799),13));

				MyLocation mLoc = new MyLocation();
				my_gps = mLoc.getMyLocation(MainActivity.this); // 내위치
				
				if(my_gps != null){// 가져오기
					my_location = new LatLng(my_gps[0], my_gps[1]); // 내 위치 설정
				}else{
					my_location = new LatLng(37.566535, 126.977969 ); // 내 위치 못 가져올 경우 서울 시청으로..
				}
				
				gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
						my_location, 17)); // 맵 상에서 자신의 위치로 이동
				
				
				gMap.setOnMapClickListener(new OnMapClickListener() {

					@Override
					public void onMapClick(LatLng clickPosition) {

					}
				});
	
			}
		}
	}
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add("등록");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getTitle().equals("등록")) {
			Intent intent = new Intent(this, RegisterActivity.class);
			startActivity(intent);
			finish();
		} 
		return super.onOptionsItemSelected(item);
	}
	
	public void passSearchKey(String keyword){
		// 아마존 웹서버에 keyword 를 던진다.
		ArrayList<String> keywords = new ArrayList<String>();
		keywords.add(keyword);
		MakeServerConnection searchConnect = new MakeServerConnection(keywords, Common.SEARCH_URL, Common.SEARCH_KEYWORD);
		searchConnect.startConnection();
	}
	
}
