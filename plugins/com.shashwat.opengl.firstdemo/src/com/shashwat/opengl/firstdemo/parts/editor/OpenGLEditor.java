package com.shashwat.opengl.firstdemo.parts.editor;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

import com.shashwat.opengl.firstdemo.parts.editor.model.DataModel;

public class OpenGLEditor extends EditorPart {
	public final static String ID = "com.shashwat.opengl.firstdemo.opengleditor";
	private DataModel dataModel;

	public OpenGLEditor() {
		this.dataModel = new DataModel();
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		// Nothing
	}

	@Override
	public void doSaveAs() {
		// Nothing
	}

	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		setSite(site);
		if (input != null) {
			setInput(input);
		}
	}

	@Override
	public boolean isDirty() {
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}
	
	@Override
	public void dispose() {
		super.dispose();
	}

	@Override
	public void createPartControl(Composite parent) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

}
