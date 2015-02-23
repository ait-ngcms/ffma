/**
 *  Copyright 2011 Diego Ceccarelli
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Multiset.java
 * 
 * @author Diego Ceccarelli, diego.ceccarelli@isti.cnr.it created on 18/ott/2011
 */
public class Multiset<K> implements Collection<KeyAndFrequency<K>> {

	// private TreeMap<K, Integer> multiset = new TreeMap();
	private List<KeyAndFrequency<K>> multiset = new ArrayList<KeyAndFrequency<K>>();
	private int total = 0;
	public Multiset(){
		
	}
	public Multiset(List<KeyAndFrequency> freqs){
		for (KeyAndFrequency k : freqs){
			add((K)k.getKey(), k.getFreq());
		}
	}


	public boolean add(KeyAndFrequency<K> k) {
		if (k == null)
			return false;
		int pos = multiset.indexOf(k);
		total += k.getFreq();
		if (pos >= 0) {
			multiset.get(pos).add(k.getFreq());
		} else {
			multiset.add(k);
			return true;
		}
		return false;
	}

	public boolean add(K key, int v) {
		if (key == null)
			return false;
		return add(new KeyAndFrequency<K>(key, v));
	}
	


	private KeyAndFrequency<K> get(Object key) {
		try {
			KeyAndFrequency<K> k = new KeyAndFrequency<K>((K) key);
			int pos = multiset.indexOf(k);
			if (pos >= 0) {
				return multiset.get(pos);
			}
		} catch (Exception e) {

		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Set#addAll(java.util.Collection)
	 */
	public boolean addAll(Multiset<K> set) {
		if (set == null)
			return false;
		for (KeyAndFrequency<K> k : set.multiset) {
			add(k);
		}
		return true;
	}

	/**
	 * @param elem
	 * @return
	 */
	public int getValue(K elem) {
		KeyAndFrequency<K> k = new KeyAndFrequency<K>(elem);
		int pos = multiset.indexOf(k);
		if (pos >= 0) {
			return multiset.get(pos).getFreq();
		}
		return -1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Set#clear()
	 */
	public void clear() {
		multiset.clear();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Set#contains(java.lang.Object)
	 */
	public boolean contains(Object key) {
		try {
			return multiset.contains(new KeyAndFrequency<K>((K) key));
		} catch (Exception e) {
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Set#containsAll(java.util.Collection)
	 */
	public boolean containsAll(Collection<?> arg0) {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Set#isEmpty()
	 */
	public boolean isEmpty() {
		return multiset.isEmpty();
	}

	/**
	 * returns a new multisect containing the intersection of the two multiset.
	 * Frequencies are the frequencies of this object
	 * 
	 * @param m
	 *            the multiset to intersect
	 * @return
	 */
	public Multiset<K> intersect(Multiset<K> m) {
		Multiset<K> intersection = new Multiset<K>();
		for (KeyAndFrequency<K> elem : m) {
			if (this.contains(elem)) {
				intersection.add(elem);
			}
		}
		return intersection;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Set#remove(java.lang.Object)
	 */
	public boolean remove(Object key) {
	
		KeyAndFrequency<K> k = get(key);
		if (k == null) return false;

		total -= k.getFreq();
		
		return multiset.remove(k);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Set#removeAll(java.util.Collection)
	 */
	public boolean removeAll(Collection<?> arg0) {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Set#retainAll(java.util.Collection)
	 */
	public boolean retainAll(Collection<?> arg0) {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Set#size()
	 */
	public int size() {
		return multiset.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Set#toArray()
	 */
	public Object[] toArray() {
		// TODO Auto-generated method stub
		return multiset.toArray();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Set#toArray(T[])
	 */
	public <T> T[] toArray(T[] arg0) {
		throw new UnsupportedOperationException();
	}

	
	

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (KeyAndFrequency el : multiset) {
			sb.append(el.getKey()).append("\t").append(el.getFreq()).append("\n");
		}
		return sb.toString();
	}

	public int getTotal() {
		return total;
	}

	public List<KeyAndFrequency> sortByFrequency() {
		List<KeyAndFrequency> sorted = new ArrayList<KeyAndFrequency>();
		for (KeyAndFrequency elems : multiset){
			sorted.add(elems);
		}
		Collections.sort(sorted, new KeyAndFrequency.CompareByValue());
		return sorted;
	}
	
	public List<KeyAndFrequency> sortByDescFrequency() {
		List<KeyAndFrequency> sorted = new ArrayList<KeyAndFrequency>();
		for (KeyAndFrequency elems : multiset){
			sorted.add(elems);
		}
		Collections.sort(sorted, Collections.reverseOrder(new KeyAndFrequency.CompareByValue()));
		return sorted;
	}
	
	public List<KeyAndFrequency> sortByKeys() {
		List<KeyAndFrequency> sorted = new ArrayList<KeyAndFrequency>();
		for (KeyAndFrequency elems : multiset){
			sorted.add(elems);
		}
		Collections.sort(sorted, new KeyAndFrequency.CompareByKey());
		return sorted;
	}
	
	
	@Override
	public boolean addAll(Collection<? extends KeyAndFrequency<K>> arg0) {
		for (KeyAndFrequency kf : arg0){
			add(kf);
		}
		return true;
	}
	@Override
	public Iterator<KeyAndFrequency<K>> iterator() {
		return multiset.iterator();
	}
	
	
	
}
