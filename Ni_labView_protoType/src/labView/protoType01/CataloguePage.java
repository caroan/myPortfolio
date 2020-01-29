package labView.protoType01;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout.LayoutParams;

public class CataloguePage extends Activity{
	public CataloguePage() {
		// TODO Auto-generated constructor stub
	}
//	DefineMachineType type = new DefineMachineType();
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.catalogue_grid);
		
		View_cataloguePage view = new View_cataloguePage(this, findViewById(R.id.grid));
		view.inflateView();
		view.getView().setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View myself, int position,
					long id) {
				// TODO Auto-generated method stub
				Intent goTodetailPage = new Intent(CataloguePage.this, ShowMachinePDfList.class);
				goTodetailPage.putExtra("MachineType", (int)id);
				startActivity(goTodetailPage);
			}
		});
	}

}