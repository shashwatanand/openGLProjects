package com.shashwat.opengl.firstdemo;

import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

import com.shashwat.opengl.firstdemo.parts.editor.OpenGLEditor;
import com.shashwat.opengl.firstdemo.parts.editor.OpenGLEditorInput;

public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {

    public ApplicationWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
        super(configurer);
    }

    public ActionBarAdvisor createActionBarAdvisor(IActionBarConfigurer configurer) {
        return new ApplicationActionBarAdvisor(configurer);
    }
    
    public void preWindowOpen() {
        IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
        configurer.setInitialSize(new Point(1200, 600));
        configurer.setShowCoolBar(false);
        configurer.setShowStatusLine(false);
    }
    
    @Override
    public void postWindowOpen() {
    	IWorkbenchWindow activeWorkbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
    	try {
    		activeWorkbenchWindow.getActivePage().openEditor(new OpenGLEditorInput(), OpenGLEditor.ID);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
}
