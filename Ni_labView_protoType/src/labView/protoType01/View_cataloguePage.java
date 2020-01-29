package labView.protoType01;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import android.R.integer;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.widget.LinearLayout.LayoutParams;
import android.content.Context;
import android.graphics.*;

public class View_cataloguePage {
	
	private CataloguePage  myController;
	private GridView myView;
	
	public View_cataloguePage(CataloguePage _myController ){
		
	}
	
	public View_cataloguePage(CataloguePage _myController, View _myView) {
		// TODO Auto-generated constructor stub
		myController = _myController;
		myView = (GridView) _myView;
	}
	
	public void inflateView(){
		ImageAdapter adapter = new ImageAdapter(myController);
		myView.setAdapter(adapter);
	}
	
	public GridView getView(){
		return myView;
	}

}

class ImageAdapter extends BaseAdapter{
	private Context myContext;
	ArrayList<MachineData> machineList = new ArrayList<MachineData>();
	
	public ImageAdapter(Context _context){
		myContext = _context;
		machineList.add(new MachineData(R.drawable.elvis2, DefineMachineType.ELVIS2));
		machineList.add(new MachineData(R.drawable.mydaq, DefineMachineType.MYDAQ));
		machineList.add(new MachineData(R.drawable.labview, DefineMachineType.LABVIEW));
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return machineList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return machineList.get(arg0).getSourceNum();
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return machineList.get(arg0).getMachineNum();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ImageView image;
		if(convertView == null){
			image = new ImageView(myContext);
			image.setLayoutParams(new GridView.LayoutParams(80,100));
			image.setAdjustViewBounds(false);
			image.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
			image.setPadding(0, 0, 0, 0);
		}
		else{
			image = (ImageView)convertView;
		}
		image.setImageResource(machineList.get(position).getSourceNum());
		return image;
	}
	
	class MachineData{
		int sourceNum;
		int machineTypeNum;
		public MachineData(int _sourceNum, int _machineTypeNum) {
			sourceNum = _sourceNum;
			machineTypeNum = _machineTypeNum;
		}
		public int getSourceNum(){
			return sourceNum;
		}
		public int getMachineNum(){
			return machineTypeNum;
		}
	}
}
