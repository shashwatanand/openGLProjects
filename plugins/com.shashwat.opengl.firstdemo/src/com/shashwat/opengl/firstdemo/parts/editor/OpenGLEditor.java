package com.shashwat.opengl.firstdemo.parts.editor;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GL2ES1;
import javax.media.opengl.GL2ES2;
import javax.media.opengl.GLContext;
import javax.media.opengl.GLDrawableFactory;
import javax.media.opengl.GLProfile;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.opengl.GLCanvas;
import org.eclipse.swt.opengl.GLData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

import com.shashwat.opengl.firstdemo.parts.editor.model.DataModel;

public class OpenGLEditor extends EditorPart {
	public final static String ID = "com.shashwat.opengl.firstdemo.opengleditor";
	
	/** Source of data to draw. */
	private DataModel dataModel;
	
    /** Holds the OpenGL canvas. */
    private Composite composite;
 
    /** Widget that displays OpenGL content. */
    private GLCanvas glCanvas;
 
    /** Used to get OpenGL object that we need to access OpenGL functions. */
    private GLContext glContext;
    
    /** X distance to translate the viewport by. */
    private float viewTranslateX = 0.0f;
 
    /** Y distance to translate the viewport by. */
    private float viewTranslateY = 0.0f;

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

	/**
	 * Creates the OpenGl Canvas
	 */
	@Override
	public void createPartControl(Composite parent) {
		GLProfile profile = GLProfile.get(GLProfile.GL2);
		this.composite = new Composite(parent, SWT.NONE);
		this.composite.setLayout(new FillLayout());
		
		GLData data = new GLData();
		data.doubleBuffer = true;
		this.glCanvas = new GLCanvas(this.composite, SWT.NO_BACKGROUND, data);
		this.glCanvas.setCurrent();
		this.glContext = GLDrawableFactory.getFactory(profile).createExternalGLContext();
		
		this.glCanvas.addListener(SWT.Resize, new Listener() {
			@Override
			public void handleEvent(Event event) {
				glCanvas.setCurrent();
				glContext.makeCurrent();
				GL2 gl2 = glContext.getGL().getGL2();
				setTransformsAndViewport(gl2);
				glContext.release();
			}
		});
		
		this.glContext.makeCurrent();
		GL2 gl2 = glContext.getGL().getGL2();
		gl2.setSwapInterval(1);
		gl2.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		gl2.glColor3f(1.0f, 0.0f, 0.0f);
		gl2.glHint(GL2ES1.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);
		gl2.glClearDepth(1.0);
		gl2.glLineWidth(2);
		gl2.glEnable(GL.GL_DEPTH_TEST);
		this.glContext.release();
		
		(new Thread() {
			public void run() {
				render();
				try {
                    while( (glCanvas != null) && !glCanvas.isDisposed() ) {
                        // if we're running, render in the GUI thread
                        if( true)
                            render();
                        // else we're paused, so sleep for a little so we don't peg the CPU
                        else
                            sleep(250);
                    }
                } catch( InterruptedException interruptedexception ) {
                    // if sleep interrupted just let the thread quite
                }
			};
		}).start();
	}

	@Override
	public void setFocus() {
		// Nothing
	}

}
