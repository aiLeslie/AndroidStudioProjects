package com.example.jean.component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
public class JsonDecodec 
{
	
	private JsonGenerator jsonGenerator = null;
    private static ObjectMapper objectMapper = null;
    //ap scan
  	public static ArrayList<String> ssids = new ArrayList<String>();
  	public static ArrayList<String> authmodes = new ArrayList<String>(); 	
  	
    public void init() 
    {  
       objectMapper = new ObjectMapper();
       try 
       {
           jsonGenerator = objectMapper.getJsonFactory().createJsonGenerator(System.out, JsonEncoding.UTF8);
       }
       catch (IOException e) 
       {
           e.printStackTrace();
       }
    }
   
    public void destory() 
    {
       try {
           if (jsonGenerator != null) {
              jsonGenerator.flush();
           }
           if (!jsonGenerator.isClosed()) {
              jsonGenerator.close();
           }
           jsonGenerator = null;
           objectMapper = null;
           //bean = null;
           System.gc();
       } catch (IOException e) {
           e.printStackTrace();
       }
    }
	public static void readJson2List(String json) 
	{
	    try 
	    {
	       objectMapper = new ObjectMapper();
	       @SuppressWarnings("unchecked")	       
		   List<LinkedHashMap<String, Object>> list = objectMapper.readValue(json, List.class);
	       for (int i = 0; i < list.size(); i++) 
	       {
	           Map<String, Object> map = list.get(i);
	           Set<String> set = map.keySet();
	           for (Iterator<String> it = set.iterator();it.hasNext();) 
	           {
	              String key = it.next();
	              if(key.equals("ssid"))
	              {
	            	  ssids.add(map.get(key).toString());
	              }
	              else if(key.equals("authmode"))
	              {
	            	  authmodes.add(map.get(key).toString());
	              }         		              
	           }
	       }
	    } 
	    catch (JsonParseException e) 
	    {
	       e.printStackTrace();
	    } catch (JsonMappingException e) 
	    {
	       e.printStackTrace();
	    } catch (IOException e)
	    {
	       e.printStackTrace();
	    }
	}
	//UTF-8 ����
	public static String toUnicode(String s) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < s.length(); ++i) {
			if (s.charAt(i) <= 256) {
				sb.append("\\u00");
			} else {
				sb.append("\\u");
			}
			sb.append(Integer.toHexString(s.charAt(i)));
		}
		return sb.toString();
	}
	//UTF-8 ����
	public static String decodeUnicode(String theString) {
		char aChar;
		int len = theString.length();
		StringBuffer outBuffer = new StringBuffer(len);
		for (int x = 0; x < len;) {
			aChar = theString.charAt(x++);
			if (aChar == '\\') {
				aChar = theString.charAt(x++);
				if (aChar == 'u') {
					// Read the xxxx
					int value = 0;
					for (int i = 0; i < 4; i++) {
						aChar = theString.charAt(x++);
						switch (aChar) {
						case '0':
						case '1':
						case '2':
						case '3':
						case '4':
						case '5':
						case '6':
						case '7':
						case '8':
						case '9':
							value = (value << 4) + aChar - '0';
							break;
						case 'a':
						case 'b':
						case 'c':
						case 'd':
						case 'e':
						case 'f':
							value = (value << 4) + 10 + aChar - 'a';
							break;
						case 'A':
						case 'B':
						case 'C':
						case 'D':
						case 'E':
						case 'F':
							value = (value << 4) + 10 + aChar - 'A';
							break;
						default:
							throw new IllegalArgumentException(
									"Malformed   \\uxxxx   encoding.");
						}

					}
					outBuffer.append((char) value);
				} else {
					if (aChar == 't')
						aChar = '\t';
					else if (aChar == 'r')
						aChar = '\r';
					else if (aChar == 'n')
						aChar = '\n';
					else if (aChar == 'f')
						aChar = '\f';
					outBuffer.append(aChar);
				}
			} else
				outBuffer.append(aChar);
		}
		return outBuffer.toString();
	}
}
