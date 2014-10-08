package com.shashwat.opengl.firstdemo;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import com.shashwat.opengl.firstdemo.parts.OpenGLView;


public class Perspective implements IPerspectiveFactory {

	public void createInitialLayout(IPageLayout layout) {
		layout.setEditorAreaVisible(false);
		layout.addView(OpenGLView.ID, IPageLayout.TOP, IPageLayout.RATIO_MAX, IPageLayout.ID_EDITOR_AREA);
	}
}
