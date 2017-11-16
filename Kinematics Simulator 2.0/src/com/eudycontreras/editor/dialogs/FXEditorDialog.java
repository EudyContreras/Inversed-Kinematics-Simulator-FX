package com.eudycontreras.editor.dialogs;

public interface FXEditorDialog {

	public void computeSize();
	
	public void addCloseAction(Runnable action);
}
