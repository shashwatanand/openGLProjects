package com.shashwat.opengl.firstdemo.parts.editor;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.List;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GL2ES1;
import javax.media.opengl.GLContext;
import javax.media.opengl.GLDrawableFactory;
import javax.media.opengl.GLProfile;
import javax.media.opengl.fixedfunc.GLMatrixFunc;
import javax.media.opengl.glu.GLU;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.opengl.GLCanvas;
import org.eclipse.swt.opengl.GLData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.EditorPart;

import com.jogamp.common.nio.Buffers;
import com.shashwat.opengl.firstdemo.parts.editor.model.DataModel;
import com.shashwat.opengl.firstdemo.parts.editor.model.ModelObj;

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
    
    /** Ratio of world-space units to screen pixels.
    * Increasing this zooms the display out,
    * decreasing it zooms the display in. */
   private float fObjectUnitsPerPixel = 0.03f;

   /** Index of vertex buffer object. We store interleaved vertex and color data here
    * like this: x0, r0, y0, g0, z0, b0, x1, r1, y1, g1, z1, b1...
    * Stored in an array because glGenBuffers requires it. */
   private int [] vertexBufferIndices = new int [] {-1};

   /** Constant used in FPS calculation. */
   private static final long MILLISECONDSPERSECOND = 1000;

   /** Number of frames drawn since last FPS calculation. */
   private int iFPSFrames;

   /** Time in milliseconds at start of FPS calculation interval. */
   private long lFPSIntervalStartTimeMS;

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
		disposeVertexBuffers();
		this.glCanvas.dispose();
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
                        if(true)
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
	
	private void render() {
		PlatformUI.getWorkbench().getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				if ((glCanvas != null) && !glCanvas.isDisposed()) {
					glCanvas.setCurrent();
					glContext.makeCurrent();
					GL2 gl2 = glContext.getGL().getGL2();
					gl2.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
					gl2.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
					
					int[] noOfVertiecs = createAndFillVertexBuffer(gl2, dataModel.getData());
					
					gl2.glColorMaterial(GL.GL_FRONT_AND_BACK, GL2.GL_AMBIENT_AND_DIFFUSE);
					gl2.glEnable(GL2.GL_COLOR_MATERIAL);
					
					gl2.glBindBuffer(GL.GL_ARRAY_BUFFER, vertexBufferIndices[0]);
					gl2.glEnableClientState(GL2.GL_VERTEX_ARRAY);
					gl2.glEnableClientState(GL2.GL_COLOR_ARRAY);
					gl2.glVertexPointer(3, GL.GL_FLOAT, 6 * Buffers.SIZEOF_FLOAT, 0);					
					gl2.glColorPointer(3, GL.GL_FLOAT, 6 * Buffers.SIZEOF_FLOAT, 3 * Buffers.SIZEOF_FLOAT);
					gl2.glPolygonMode(GL.GL_FRONT, GL2.GL_FILL);
					gl2.glDrawArrays(GL2.GL_QUADS, 0, noOfVertiecs[0]);
					
					gl2.glBindBuffer(GL.GL_ARRAY_BUFFER, 0);
					gl2.glDisableClientState(GL2.GL_VERTEX_ARRAY);
					gl2.glDisableClientState(GL2.GL_COLOR_ARRAY);
					gl2.glDisable(GL2.GL_COLOR_MATERIAL);
					
					glCanvas.swapBuffers();
					glContext.release();
					
					dataModel.incTimeUnit(0.005);
					
					calculateAndShowFPS();
				}
			}
		});
	}
	
	private void calculateAndShowFPS() {
		++this.iFPSFrames;
		long time = System.currentTimeMillis();
		
		long timeIntervalMs = time - this.lFPSIntervalStartTimeMS;
		if (timeIntervalMs >= MILLISECONDSPERSECOND) {
			this.lFPSIntervalStartTimeMS = time;
			int fps = (int) ((double) (iFPSFrames * MILLISECONDSPERSECOND) / (double)timeIntervalMs);
			this.iFPSFrames = 0;
			getEditorSite().getActionBars().getStatusLineManager().setMessage(String.format("FPS %d", fps)); 
		}
	}
	
	private int[] createAndFillVertexBuffer(GL2 gl2, List<ModelObj> list) {
		int[] noOfVertices = new int[] {list.size() * 4};
		
		if (this.vertexBufferIndices[0] == -1) {
			if (!gl2.isFunctionAvailable("glGenBuffers")
					|| !gl2.isFunctionAvailable("glBindBuffer")
					|| !gl2.isFunctionAvailable("glBufferData")
					|| !gl2.isFunctionAvailable("glDeleteBuffers")) {
				System.out.println("Vertex buffer objects not supporte");
			}
			
			gl2.glGenBuffers(1, this.vertexBufferIndices, 0);
			
			gl2.glBindBuffer(GL.GL_ARRAY_BUFFER, this.vertexBufferIndices[0]);
			gl2.glBufferData(GL.GL_ARRAY_BUFFER,
					noOfVertices[0] * 3 * Buffers.SIZEOF_FLOAT * 2,
					null,
					GL2.GL_DYNAMIC_DRAW);
		}
		
		gl2.glBindBuffer(GL.GL_ARRAY_BUFFER, this.vertexBufferIndices[0]);
		ByteBuffer byteBuffer = gl2.glMapBuffer(GL.GL_ARRAY_BUFFER, GL2.GL_WRITE_ONLY);
		FloatBuffer floatBuffer = byteBuffer.order(ByteOrder.nativeOrder()).asFloatBuffer();
		
		for (ModelObj obj : list) {
			addModelToBuffer(floatBuffer, obj);
		}
		gl2.glUnmapBuffer(GL.GL_ARRAY_BUFFER);
		return noOfVertices;
	}
	
	private void addModelToBuffer(FloatBuffer floatBuffer, ModelObj obj) {
		floatBuffer.put(obj.getObjX());
		floatBuffer.put(obj.getObjY());
		floatBuffer.put(0.0f);
		
		floatBuffer.put(obj.getObjColor()[0]);
		floatBuffer.put(obj.getObjColor()[1]);
		floatBuffer.put(obj.getObjColor()[2]);
		
		floatBuffer.put(obj.getObjX() + obj.getObjWidth());
		floatBuffer.put(obj.getObjY());
		floatBuffer.put(0.0f);
		
		floatBuffer.put(obj.getObjColor()[0]);
		floatBuffer.put(obj.getObjColor()[1]);
		floatBuffer.put(obj.getObjColor()[2]);
		
		floatBuffer.put(obj.getObjX());
		floatBuffer.put(obj.getObjY() + obj.getObjHeight());
		floatBuffer.put(0.0f);
		
		floatBuffer.put(obj.getObjColor()[0]);
		floatBuffer.put(obj.getObjColor()[1]);
		floatBuffer.put(obj.getObjColor()[2]);
	}

	private void setTransformsAndViewport(GL2 gl2) {
		Rectangle rectangle = this.glCanvas.getClientArea();
		int width = rectangle.width;
		int height = Math.max(rectangle.height, 1);
		
		gl2.glMatrixMode(GLMatrixFunc.GL_PROJECTION);
		gl2.glLoadIdentity();
		
		GLU glu = new GLU();
		glu.gluOrtho2D( - (this.fObjectUnitsPerPixel * width) / 2.0f,
						  (this.fObjectUnitsPerPixel * width) / 2.0f,
						- (this.fObjectUnitsPerPixel * height) / 2.0f,
						  (this.fObjectUnitsPerPixel * height) / 2.0f);
		
		gl2.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
		gl2.glViewport(0, 0, width, height);
		gl2.glLoadIdentity();
		gl2.glTranslatef(this.viewTranslateX, this.viewTranslateY, 0.0f);
	}

	private void disposeVertexBuffers() {
		this.glContext.makeCurrent();
		GL2 gl2 = this.glContext.getGL().getGL2();
		gl2.glDeleteBuffers(1, this.vertexBufferIndices, 0);
		this.vertexBufferIndices[0] = -1;
		this.glContext.release();
	}

	@Override
	public void setFocus() {
		// Nothing
	}
}
