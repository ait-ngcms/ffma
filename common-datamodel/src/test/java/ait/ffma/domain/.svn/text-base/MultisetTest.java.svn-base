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
package ait.ffma.domain;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;

import org.junit.Test;

import ait.ffma.utils.KeyAndFrequency;
import ait.ffma.utils.Multiset;

/**
 * MultisetTest.java
 *
 * @author Diego Ceccarelli, diego.ceccarelli@isti.cnr.it
 * created on 27/mar/2012
 */
public class MultisetTest {

	@Test
	public void test() {
		Multiset<String> multiset = new Multiset<String>();
		multiset.add("Ffma", 1);
		multiset.add("Ffma", 5);
		multiset.add("diego", 1);
		multiset.add("diego", 1);
		assertEquals(6, multiset.getValue("Ffma"));
		assertEquals(2, multiset.getValue("diego"));
		multiset.remove("diego");
		assertFalse(multiset.contains("diego"));
		assertEquals( 6, multiset.getTotal());
		
		
	}
	@Test
	public void testSort() {
		Multiset<String> multiset = new Multiset<String>();
		multiset.add("first", 1);
		
		multiset.add("second", 2);
		multiset.add("third", 3);
		List<KeyAndFrequency> list = multiset.sortByFrequency();
		assertEquals("first",list.get(0).getKey());
		assertEquals("second",list.get(1).getKey());
		assertEquals("third",list.get(2).getKey());
		list = multiset.sortByKeys();
		assertEquals("first",list.get(0).getKey());
		assertEquals("second",list.get(1).getKey());
		assertEquals("third",list.get(2).getKey());
		multiset.add("cc", 3);
		multiset.add("bb", 3);
		multiset.add("aa", 3);
		list = multiset.sortByKeys();
		assertEquals("aa",list.get(0).getKey());
		assertEquals("bb",list.get(1).getKey());
		assertEquals("cc",list.get(2).getKey());	
	}
	
	
	
	@Test
	public void testJson() {
		Multiset<String> multiset = new Multiset<String>();
		multiset.add("first", 1);
		
		multiset.add("second", 2);
		multiset.add("third", 3);
		KeyAndFrequency k = new KeyAndFrequency("first",1);
		//assertEquals("{}", k.toJson().toString());
		List<KeyAndFrequency> list = multiset.sortByFrequency();
		String json = KeyAndFrequency.listToJson(list).toString();
		System.out.println(KeyAndFrequency.listToJson(list));
		List<KeyAndFrequency> el = KeyAndFrequency.listFromJson(json);
		assertEquals("third", el.get(2).getKey());
		
		Multiset<Integer> intMultiset = new Multiset<Integer>();
		intMultiset.add(10, 1);
		intMultiset.add(2, 2);
		intMultiset.add(5, 3);
		
		list = intMultiset.sortByKeys();
		json = KeyAndFrequency.listToJson(list).toString();
		el = KeyAndFrequency.listFromJson(json, Integer.class);
		assertEquals(2, el.get(0).getKey());
		assertEquals(5, el.get(1).getKey());
		assertEquals(10, el.get(2).getKey());
		list = intMultiset.sortByDescFrequency();
		assertEquals(3,list.get(0).getFreq());
		assertEquals(2,list.get(1).getFreq());
		assertEquals(2,list.get(1).getKey());
		assertEquals(1,list.get(2).getFreq());
		
	}
	
	@Test
	public void testAddAll(){
		Multiset<String> multiset = new Multiset<String>();
		multiset.add("a", 1);
		
		multiset.add("b", 2);
		multiset.add("c", 3);
		Multiset<String> multiset2 = new Multiset<String>();
		multiset2.add("a", 2);
		
		multiset2.add("b", 3);
		multiset2.add("d", 4);
		multiset.addAll(multiset2);
		assertEquals(3, multiset.getValue("a"));
		assertEquals(5, multiset.getValue("b"));
		assertEquals(4, multiset.getValue("d"));
	}

	@Test
	public void testDate(){
		Multiset<Date> multiset = new Multiset<Date>();
		multiset.add(new Date(1181734914093L), 3);
		multiset.add(new Date(1181644914093L), 1);
		multiset.add(new Date(1181534914093L), 1);
		String json =KeyAndFrequency.listToJson(multiset.sortByKeys()).toString();
		assertEquals(3, multiset.sortByKeys().get(2).getFreq());
		List<KeyAndFrequency> dates = KeyAndFrequency.listFromJson(json, Date.class);
		System.out.println(dates);
		
	}

}
