package com.shashwat.opengl.firstdemo.parts.editor;

import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.part.EditorActionBarContributor;

public class OpenGLEditorActionBarContributor extends
		EditorActionBarContributor {

	private RunSuspendAction runSuspendAction;

	public OpenGLEditorActionBarContributor() {
		this.runSuspendAction = new RunSuspendAction();
	}

	@Override
	public void init(IActionBars bars, IWorkbenchPage page) {
		super.init(bars, page);
		bars.setGlobalActionHandler(RunSuspendAction.ID, this.runSuspendAction);
	}

	@Override
	public void contributeToToolBar(IToolBarManager toolBarManager) {
		toolBarManager.add(this.runSuspendAction);
	}
}
