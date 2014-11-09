package com.shashwat.opengl.firstdemo.parts.editor;

import org.eclipse.jface.action.Action;

import com.shashwat.opengl.firstdemo.Activator;

public class RunSuspendAction extends Action {
	public static final String ID = "com.shashwat.opengl.firstdemo.parts.editor.RunSuspendAction";

	private boolean isRunning = false;

	public RunSuspendAction() {
		super("Run", Action.AS_PUSH_BUTTON);
		setToolTipText("Run simulation");
		setImageDescriptor(Activator.getIcon("resume_co.gif"));
		setId(ID);
		setActionDefinitionId(ID);
	}

	/**
	 * Code to run the action.
	 *
	 * @see org.eclipse.jface.action.Action#run()
	 */
	@Override
	public void run() {
		if (isRunning) {
			setToolTipText("Run simulation");
			setImageDescriptor(Activator.getIcon("resume_co.gif"));
		} else {
			setToolTipText("Suspend simulation");
			setImageDescriptor(Activator.getIcon("suspend_co.gif"));
		}
		isRunning = !isRunning;
	}

	// ================================================================
	/**
	 * Accessor.
	 * 
	 * @return true if the simulation is running, false otherwise.
	 */
	public boolean isRunning() {
		return (isRunning);
	}
}
