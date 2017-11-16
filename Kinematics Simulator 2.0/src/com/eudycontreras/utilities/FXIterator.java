package com.eudycontreras.utilities;

import java.util.List;


public class FXIterator{
 
	public static <T> void Iterate(List<T> items, FXIteration<T> iterator){
		
		for(int i = 0; i<items.size(); iterator.Iteration(items.get(i), i), i++);
	}
	
	public static <T> void Iterate(List<T> items, int start, int end, FXIteration<T> iterator){
		
		start = start >= 0 ? start : 0;
		
		end = end >= 0 && end <= items.size() ? end : items.size();
		
		for(int i = start; i< end; iterator.Iteration(items.get(i), i), i++);
	}
	
	public static <T> void Iterate(T[] items, FXIteration<T> iterator){
		
		for(int i = 0; i<items.length; iterator.Iteration(items[i], i), i++);
	}
	
	public static <T> void Iterate(T[] items, int start, int end, FXIteration<T> iterator){
		
		start = start >= 0 ? start : 0;
		
		end = end >= 0 && end <= items.length ? end : items.length;
		
		for(int i = start; i<end; iterator.Iteration(items[i], i), i++);
	}
	
	public interface FXIteration<T>{
		
		public void Iteration(T current, int index);
	}
}