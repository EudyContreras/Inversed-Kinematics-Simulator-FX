package com.eudycontreras.javafx.fbk.models;


import java.util.List;

/**
 * Utility class used for iterating over different types of collections.
 * This class is meant to be used instead of a regular index loop or for-each loop
 * the iteration wrapper used by this class serves a solution to not having to 
 * write tedious index loops while having the benefits of a regular loop with the
 * ability to get the current index of iteration, the current item being iterated through
 * the action class which allows the break and continue commands to be issued to the loop
 * in the same fashion in which those commands are used in regular loops.
 * These class performs better than the java implementation of iterator since it allows
 * the reuse of the iterator which eliminates overhead.
 * 
 * @author Eudy Contreras
 *
 */
public class FBKIterator {

	private FBKIterator(){}
	 
	public static <T> void Iterate(List<T> items, FXIteration<T> iterator){	
		Iterate(items, 0, items.size(), iterator, null);
	}
	
	public static <T> void Iterate(List<T> items, FXIteration<T> iterator, T args){	
		Iterate(items, 0, items.size(), iterator, args);
	}
	
	public static <T> void Iterate(List<T> items, int start, int end, FXIteration<T> iterator, T args){
		
		IterateAction breaker = new IterateAction();
		
		start = start >= 0 ? start : 0;
		
		end = end >= 0 && end <= items.size() ? end : items.size();
		
		for(int i = start; i< end; i++){

			if(breaker.isBroken()){
				break;
			}
			if(breaker.isContinued()){
				continue;
			}
			iterator.Iteration(items.get(i), i, breaker, args);
		}
	}
	
	public static <T> void Iterate(T[] items, FXIteration<T> iterator){
		Iterate(items, 0, items.length, iterator, null);
	}
	
	public static <T> void Iterate(T[] items, int start, int end, FXIteration<T> iterator, T args){
		
		IterateAction breaker = new IterateAction();
		
		start = start >= 0 ? start : 0;
		
		end = end >= 0 && end <= items.length ? end : items.length;
		
		for(int i = start; i<end; i++){

			if(breaker.isBroken()){
				break;
			}
			if(breaker.isContinued()){
				continue;
			}
			iterator.Iteration(items[i], i, breaker, args);
		}
	}
	
	public static void Iterate(int[] items, FXIteration<Integer> iterator){
		Iterate(items, 0, items.length, iterator);
	}
	
	public static void Iterate(int[] items, int start, int end, FXIteration<Integer> iterator){
		
		IterateAction breaker = new IterateAction();
		
		start = start >= 0 ? start : 0;
		
		end = end >= 0 && end <= items.length ? end : items.length;
		
		for(int i = start; i<end; i++){
			
			if(breaker.isBroken()){
				break;
			}
			if(breaker.isContinued()){
				continue;
			}
			iterator.Iteration(items[i], i, breaker, null);
		}
	}
	
	public static void Iterate(long[] items, FXIteration<Long> iterator){
		Iterate(items, 0, items.length, iterator);
	}
	
	public static void Iterate(long[] items, int start, int end, FXIteration<Long> iterator){
		
		IterateAction breaker = new IterateAction();
		
		start = start >= 0 ? start : 0;
		
		end = end >= 0 && end <= items.length ? end : items.length;
		
		for(int i = start; i<end; i++){
			
			if(breaker.isBroken()){
				break;
			}
			if(breaker.isContinued()){
				continue;
			}
			iterator.Iteration(items[i], i, breaker, null);
		}
	}
	
	public static void Iterate(double[] items, FXIteration<Double> iterator){
		Iterate(items, 0, items.length, iterator);
	}
	
	public static void Iterate(double[] items, int start, int end, FXIteration<Double> iterator){
		
		IterateAction breaker = new IterateAction();
		
		start = start >= 0 ? start : 0;
		
		end = end >= 0 && end <= items.length ? end : items.length;
		
		for(int i = start; i<end; i++){
			
			if(breaker.isBroken()){
				break;
			}
			if(breaker.isContinued()){
				continue;
			}
			iterator.Iteration(items[i], i, breaker, null);
		}
	}
	
	public static void Iterate(boolean[] items, FXIteration<Boolean> iterator){
		Iterate(items, 0, items.length, iterator);
	}
	
	public static void Iterate(boolean[] items, int start, int end, FXIteration<Boolean> iterator){
		
		IterateAction breaker = new IterateAction();
		
		start = start >= 0 ? start : 0;
		
		end = end >= 0 && end <= items.length ? end : items.length;
		
		for(int i = start; i<end; i++){
			
			if(breaker.isBroken()){
				break;
			}
			if(breaker.isContinued()){
				continue;
			}
			iterator.Iteration(items[i], i, breaker, null);
		}
	}

	public interface FXIteration<T>{ 
		public void Iteration(T current, int index, IterateAction breaker, T args);
	}

    public static class IterateAction {
		
    	private boolean breaking;
		private boolean continuing;
		
		void breakIteration(){
			this.breaking = true;
		}
		
		void jumpIteration(){
			this.continuing = true;
		}
		
		boolean isBroken() { return breaking;}
		
		boolean isContinued() {return continuing;}
	}
}
