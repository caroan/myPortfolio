package labView.protoType01;

import java.net.URL;
import java.util.List;

import android.util.Log;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Source;

public class NIXMLParser {
//test��	 ����
	static public String getName()
	{
		URL url;
		try
		{
			url = new URL("http", "128.134.57.116", 8000, "/123456.html");
		}
		catch(Exception e)
		{
			return "";
		}
		
		return getName(url);
	}
	
	static public String getName(String ip, int port, String file)
	{
		URL url;
		try
		{
			url = new URL("http", ip, port, file);
		}
		catch (Exception e)
		{
			return "";
		}
		
		return getName(url);
	}
//�׽�Ʈ�� ��	
	static public String getName(URL url)
	{
//		Log.d("TAG", "����~!!!!!!!!!!!!!!!!!");
		String result = "";
		try
		{
			Source source = new Source(url);
			
			List<Element> imgtags = source.getAllElements(HTMLElementName.IMG);		//IMG �±׸� ���� ã��.
			if(imgtags.size() != 0)
			{
				Element img = imgtags.get(0);
				result = img.getAttributeValue("src");
			}
		
			List<Element> paramtags = source.getAllElements(HTMLElementName.PARAM);
			if(paramtags.size() != 0)
			{
				for(Element param : paramtags)
				{
					if(param.getAttributeValue("name").equals("LVFPPVINAME"))
					result = "/.snap?" + param.getAttributeValue("value");
				}
				
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
//		Log.d("TAG", "������~!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		return result.replace(" ", "%20");			//���� ��ο� ������ ��������� ó������ ����.
	}
}
