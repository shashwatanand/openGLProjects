package com.shashwat.opengl.firstdemo.parts.editor.model;

import java.util.ArrayList;
import java.util.List;

public class DataModel {
	/** Tracks the current time it arbitrary units. */
    private double dataTimeUnits;
    
    /** Number of x increments */
    private int xIncrements = 300000;
    
    /**List of Model Objects*/
    private List<ModelObj> listModelObjs;
    
    /**
     * Constructor
     */
    public DataModel() {
    	this.listModelObjs = new ArrayList<>();
    	for (int index = 0; index < xIncrements; index++) {
    		ModelObj modelObj = new ModelObj();
    		modelObj.setObjColor(new float[] {(float) Math.random(), (float) Math.random(), (float) Math.random()});
    		listModelObjs.add(modelObj);
    	}
	}
    
    public List<ModelObj> getData() {
    	double dX = 2.0 * Math.PI * (1.0 / xIncrements);
    	double x = -Math.PI;
    	for( int index = 0; index < xIncrements; x += dX, index++ ) {
    		double dY = Math.sin(x + dataTimeUnits);
    		ModelObj modelObj = listModelObjs.get(index);
    		modelObj.setObjX(x);
    		modelObj.setObjY(dY > 0 ? 0 : dY);
    		modelObj.setObjWidth(dX);
    		modelObj.setObjHeight(Math.abs(dY));
    	}
    	return this.listModelObjs;
    }
    
    public void incTimeUnit(double unit) {
    	dataTimeUnits += unit;
    }
}
