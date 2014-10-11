package com.shashwat.opengl.firstdemo;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import com.shashwat.opengl.firstdemo.parts.view.OpenGLView;


public class Perspective implements IPerspectiveFactory {

	public void createInitialLayout(IPageLayout layout) {
		layout.setEditorAreaVisible(true);
		layout.addView(OpenGLView.ID, IPageLayout.LEFT, 0.40f, IPageLayout.ID_EDITOR_AREA);
	}
}
