package com.eudycontreras.observers;

public interface FXSubject<T> {

	public void register(FXObserver<T> observer);

	public void unregister(FXObserver<T> observer);

	public T getUpdate(FXObserver<T> observer);

	public void notifyObservers();

}
