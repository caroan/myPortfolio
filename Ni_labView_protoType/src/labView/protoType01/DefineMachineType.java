package labView.protoType01;

import java.net.*;
import java.util.ArrayList;

public class DefineMachineType {
	static public final int ELVIS2 = 0;
	static public final int MYDAQ = 1;
	static public final int LABVIEW = 3;
	static public final int ERROR_NOTFOUND_MACHINETYPE = -1;
	
	private DefineMachineType(){
	}
	
	static public String getMachineName(int _type){
		String result = null;
		
		switch(_type){
		case ELVIS2:
			result = "ELVIS2";
			break;
		case MYDAQ:
			result = "MYDAQ";
			break;
		case LABVIEW:
			result = "LABVIEW";
			break;
		}
		return result;
	}
	
	static public void getMachineNeedPdfNames(ArrayList<String> _result, int _type){
		switch(_type){
		case ELVIS2:
			_result.add("FAQ 가기");
			_result.add("ELVIS2 제원.pdf");
			_result.add("ELVIS2 사용자 메뉴얼.pdf");
			break;
		case MYDAQ:
			_result.add("FAQ 가기");
			_result.add("MYDAQ 제원.pdf");
			_result.add("MYDAQ 사용자 메뉴얼.pdf");
			_result.add("Introduction to LabVIEW with myDAQ: State Machines part1");
			_result.add("Introduction to LabVIEW with myDAQ: Read and Write Data");
			break;
		case LABVIEW:
			_result.add("What can do with labview?");
			_result.add("Learn LabVIEW");
			_result.add("Learn how to use labview?");
			_result.add("Writing Your First LabVIEW Program");
		}
	}
	
	static public URL getPdfAddressFromHomePage(int _type, String _fileName) throws MalformedURLException {
		switch(_type){
		case ELVIS2:
			if(_fileName.equals("ELVIS2 제원.pdf")){
				return new URL("http://www.ni.com/pdf/manuals/372590b.pdf");
			}else if(_fileName.equals("ELVIS2 사용자 메뉴얼.pdf")){
				return new URL("http://www.ni.com/pdf/manuals/374629c.pdf");
			}
			break;
		case MYDAQ:
			if(_fileName.equals("MYDAQ 제원.pdf")){
				return new URL("http://www.ni.com/pdf/manuals/373060e_0129.pdf");
			}else if(_fileName.equals("MYDAQ 사용자 메뉴얼.pdf")){
				return new URL("http://www.ni.com/pdf/manuals/371931e_0129.pdf");
			}
			else if(_fileName.equals("Introduction to LabVIEW with myDAQ: State Machines part1")){
				return new URL("http://www.youtube.com/watch?v=0aX2CEYMGYc");
			}
			else if(_fileName.equals("Introduction to LabVIEW with myDAQ: Read and Write Data")){
				return new URL("http://www.youtube.com/watch?v=WGDd4ihnbaM");
			}
			break;
		case LABVIEW:
			if(_fileName.equals("What can do with labview?")){
				return new URL("http://www.youtube.com/watch?feature=endscreen&NR=1&v=8oKQtn-7ctY");
			}
			else if(_fileName.equals("Learn LabVIEW")){
				return new URL("http://www.youtube.com/watch?v=wKWaTbiaDb8");
			}
			else if(_fileName.equals("Learn how to use labview?")){
				return new URL("http://www.youtube.com/watch?v=6rdkddXd2Xs");
			}
			else if(_fileName.equals("Writing Your First LabVIEW Program")){
				return new URL("http://www.youtube.com/watch?v=ZHNlKyYzrPE");
			}
			break;
		}
		return null;
	}
}
