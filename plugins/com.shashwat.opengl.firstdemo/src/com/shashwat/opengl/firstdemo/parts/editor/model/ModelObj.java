package com.shashwat.opengl.firstdemo.parts.editor.model;

public class ModelObj {
	/** Origin of object. */
    double objX, objY;

    /** Width and height of object. */
    double objWidth, objHeight;
    
    /** RGB color components. */
    float [] objColor;
    
    public ModelObj() {
    	// Nothing
	}

	/**
	 * @return the objX
	 */
	public float getObjX() {
		return (float) objX;
	}

	/**
	 * @param objX the objX to set
	 */
	public void setObjX(double objX) {
		this.objX = objX;
	}

	/**
	 * @return the objY
	 */
	public float getObjY() {
		return (float) objY;
	}

	/**
	 * @param objY the objY to set
	 */
	public void setObjY(double objY) {
		this.objY = objY;
	}

	/**
	 * @return the objWidth
	 */
	public float getObjWidth() {
		return (float) objWidth;
	}

	/**
	 * @param objWidth the objWidth to set
	 */
	public void setObjWidth(double objWidth) {
		this.objWidth = objWidth;
	}

	/**
	 * @return the objHeight
	 */
	public float getObjHeight() {
		return (float) objHeight;
	}

	/**
	 * @param objHeight the objHeight to set
	 */
	public void setObjHeight(double objHeight) {
		this.objHeight = objHeight;
	}

	/**
	 * @return the objColor
	 */
	public float[] getObjColor() {
		return objColor;
	}

	/**
	 * @param objColor the objColor to set
	 */
	public void setObjColor(float[] objColor) {
		this.objColor = objColor;
	}
}
