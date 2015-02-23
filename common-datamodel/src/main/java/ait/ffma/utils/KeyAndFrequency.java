/**
 *  Copyright 2012 Diego Ceccarelli
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package ait.ffma.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * KeyAndFrequency.java
 * 
 * @author Diego Ceccarelli, diego.ceccarelli@isti.cnr.it created on 27/mar/2012
 */
public class KeyAndFrequency<K> {
	private K key;
	private int freq;
	private static final String KEY_FLD = "key";
	private static final String FREQ_FLD = "frequency";
	private static DateFormat format =  
            new SimpleDateFormat("yyyyMMdd");

	public KeyAndFrequency(K key, int freq) {
		this.key = key;
		this.freq = freq;
	}

	public KeyAndFrequency(K key) {
		this.key = key;
	}

	public void add(int value) {
		freq += value;
	}

	public K getKey() {
		return key;
	}

	public void setKey(K key) {
		this.key = key;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		KeyAndFrequency other = (KeyAndFrequency) obj;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		return true;
	}

	public int getFreq() {
		return freq;
	}

	public void setFreq(int freq) {
		this.freq = freq;
	}

	public JSONObject toJson() {
		JSONObject json = new JSONObject();
		try {
			if (key instanceof Date){
				json.put(KEY_FLD, format.format((Date)key));
				
			}else{
				json.put(KEY_FLD, key.toString());
			}
			json.put(FREQ_FLD, getFreq());
		} catch (JSONException e) {
			return null;
		}
		return json;
	}

	public static KeyAndFrequency fromJson(JSONObject obj, Class clazz) {
		try {
			if (clazz.equals(Integer.class)) {
				KeyAndFrequency kf = new KeyAndFrequency(obj.getInt(KEY_FLD),
						obj.getInt(FREQ_FLD));
				return kf;
			}
			if (clazz.equals(String.class)) {
				KeyAndFrequency kf = new KeyAndFrequency(obj.get(KEY_FLD),
						obj.getInt(FREQ_FLD));
				return kf;
			}
			
			if (clazz.equals(Date.class)) {
				KeyAndFrequency kf = null;
				try {
					
					kf = new KeyAndFrequency(format.parse(obj.getString(KEY_FLD)),
							obj.getInt(FREQ_FLD));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return kf;
			}
		} catch (JSONException e) {

		}
		return null;
	}

	
	public static KeyAndFrequency fromJson(JSONObject obj) {
		return fromJson(obj, String.class);
	}

	public static JSONArray listToJson(List<KeyAndFrequency> list) {
		JSONArray array = new JSONArray();
		for (KeyAndFrequency k : list) {
			array.put(k.toJson());
		}
		return array;
	}

	public static List<KeyAndFrequency> listFromJson(String json) {
		return listFromJson(json, String.class);
	}
	
	public static List<KeyAndFrequency> listFromJson(String json,Class clazz) {
		List<KeyAndFrequency> list = new ArrayList<KeyAndFrequency>();
		JSONArray array;
		try {
			array = new JSONArray(json);
		} catch (JSONException e1) {
			return list;
		}
		for (int i = 0; i < array.length(); i++) {
			JSONObject jo;
			try {
				jo = array.getJSONObject(i);
				list.add(fromJson(jo,clazz));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return list;
	}

	@Override
	public String toString() {
		return "KeyAndFreq [key=" + key + ", freq=" + freq + "]";
	}

	public static class CompareByKey implements Comparator<KeyAndFrequency> {

		@Override
		public int compare(KeyAndFrequency arg0, KeyAndFrequency arg1) {
			if (arg0.getKey() instanceof Comparable
					&& arg1.getKey() instanceof Comparable) {
				Comparable l = (Comparable) arg0.getKey();
				Comparable r = (Comparable) arg1.getKey();
				return l.compareTo(r);
			}
			return 0;
		}

	}

	public static class CompareByValue implements Comparator<KeyAndFrequency> {

		@Override
		public int compare(KeyAndFrequency arg0, KeyAndFrequency arg1) {
			return arg0.getFreq() - arg1.getFreq();
		}

	}
	
	

}
