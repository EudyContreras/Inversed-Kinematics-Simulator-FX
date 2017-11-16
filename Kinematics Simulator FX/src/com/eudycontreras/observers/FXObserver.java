package com.eudycontreras.observers;

public interface FXObserver<T> {

	public void update();
	
	public void notify(T subject);
	
	public void setSubject(T subject);
}
