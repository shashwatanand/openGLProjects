package com.shashwat.opengl.firstdemo;

import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;

public class ApplicationActionBarAdvisor extends ActionBarAdvisor {
	private IWorkbenchAction mExitAction;

    public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
        super(configurer);
    }

    protected void makeActions(IWorkbenchWindow window) {
    	this.mExitAction = ActionFactory.QUIT.create(window);
    	register(this.mExitAction);
    }

    protected void fillMenuBar(IMenuManager menuBar) {
    	MenuManager lMenuManagerFile = new MenuManager("&File", IWorkbenchActionConstants.M_FILE);
    	lMenuManagerFile.add(this.mExitAction);
    	menuBar.add(lMenuManagerFile);
    }
    
    @Override
    protected void fillCoolBar(ICoolBarManager coolBar) {
    	super.fillCoolBar(coolBar);
    	ToolBarManager lToolBarManager = new ToolBarManager();
    	coolBar.add(lToolBarManager);
    }
}
