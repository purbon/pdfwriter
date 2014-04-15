package com.purbon.pdfwriter.chart.extensions.jfreechart;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Map;

import org.jfree.chart.axis.AxisSpace;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CrosshairState;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.PlotState;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.AbstractXYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.Layer;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;

@SuppressWarnings("unused")
public class XYPQPlot extends XYPlot {

	private static final long serialVersionUID = 5966427253717593930L;

	 public XYPQPlot() {
		super();
	}

	public XYPQPlot(XYDataset dataset, ValueAxis domainAxis, ValueAxis rangeAxis, XYItemRenderer renderer) {
		super(dataset, domainAxis, rangeAxis, renderer);
		setAxisRange(getRangeAxis());
		setAxisRange(getDomainAxis());
	}

	
	
	@SuppressWarnings({ "rawtypes", "deprecation" })
	@Override
	 public void draw(Graphics2D g2, Rectangle2D area, Point2D anchor, PlotState parentState, PlotRenderingInfo info) {
		         boolean b1 = (area.getWidth() <= MINIMUM_WIDTH_TO_DRAW);
		         boolean b2 = (area.getHeight() <= MINIMUM_HEIGHT_TO_DRAW);
		         if (b1 || b2) {
		             return;
		         }
		 
		         // record the plot area...
		         if (info != null) {
		             info.setPlotArea(area);
		         }
		 
		         // adjust the drawing area for the plot insets (if any)...
		         RectangleInsets insets = getInsets();
		         insets.trim(area);
		 
		         AxisSpace space = calculateAxisSpace(g2, area);
		         Rectangle2D dataArea = space.shrink(area, null);
		         this.getAxisOffset().trim(dataArea);
		        
		         if (info != null) {
		             info.setDataArea(dataArea);
		         }
		 
		         // draw the plot background and axes...
		         drawBackground(g2, dataArea);
		 
		         PlotOrientation orient = getOrientation();
		 
		         // the anchor point is typically the point where the mouse last
		         // clicked - the crosshairs will be driven off this point...
		         if (anchor != null && !dataArea.contains(anchor)) {
		             anchor = null;
		         }
		         CrosshairState crosshairState = new CrosshairState();
		         crosshairState.setCrosshairDistance(Double.POSITIVE_INFINITY);
		         crosshairState.setAnchor(anchor);
		         
		         crosshairState.setAnchorX(Double.NaN);
		         crosshairState.setAnchorY(Double.NaN);            
		         if (anchor != null) {
		             ValueAxis domainAxis = getDomainAxis();
		             if (domainAxis != null) {
		                 double x;
		                 if (orient == PlotOrientation.VERTICAL) {
		                     x = domainAxis.java2DToValue(anchor.getX(), dataArea, 
		                             getDomainAxisEdge());
		                 } 
		                 else {
		                     x = domainAxis.java2DToValue(anchor.getY(), dataArea, 
		                             getDomainAxisEdge());
		                 }
		                 crosshairState.setAnchorX(x);
		             }
		             ValueAxis rangeAxis = getRangeAxis();
		             if (rangeAxis != null) {
		                 double y;
		                 if (orient == PlotOrientation.VERTICAL) {
		                     y = rangeAxis.java2DToValue(anchor.getY(), dataArea, 
		                             getRangeAxisEdge());
		                 } 
		                 else {
		                     y = rangeAxis.java2DToValue(anchor.getX(), dataArea, 
		                             getRangeAxisEdge());
		                 }
		                 crosshairState.setAnchorY(y);                
		             }
		         }
		         crosshairState.setCrosshairX(getDomainCrosshairValue());
		         crosshairState.setCrosshairY(getRangeCrosshairValue());
		         Shape originalClip = g2.getClip();
		         Composite originalComposite = g2.getComposite();
		 
		         g2.clip(dataArea);
		         g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 
		                 getForegroundAlpha()));
		 
		         /*AxisState domainAxisState = (AxisState) axisStateMap.get(
		                 getDomainAxis());
		         if (domainAxisState == null) {
		             if (parentState != null) {
		                 domainAxisState = (AxisState) parentState.getSharedAxisStates()
		                         .get(getDomainAxis());
		             }
		         }
		 
		         AxisState rangeAxisState = (AxisState) axisStateMap.get(getRangeAxis());
		         if (rangeAxisState == null) {
		             if (parentState != null) {
		                 rangeAxisState = (AxisState) parentState.getSharedAxisStates().get(getRangeAxis());
		             }
		         }
		         if (domainAxisState != null) {
		             drawDomainTickBands(g2, dataArea, domainAxisState.getTicks());
		         }
		         if (rangeAxisState != null) {
		             drawRangeTickBands(g2, dataArea, rangeAxisState.getTicks());
		         }
		         if (domainAxisState != null) {
		             drawDomainGridlines(g2, dataArea, domainAxisState.getTicks());
		             drawZeroDomainBaseline(g2, dataArea);
		         }
		         if (rangeAxisState != null) {
		             drawRangeGridlines(g2, dataArea, rangeAxisState.getTicks());
		             drawZeroRangeBaseline(g2, dataArea);
		         }
		 
		         // draw the markers that are associated with a specific renderer...
		         for (int i = 0; i < this.getRendererCount(); i++) {
		             drawDomainMarkers(g2, dataArea, i, Layer.BACKGROUND);
		         }
		         for (int i = 0; i < this.getRendererCount(); i++) {
		             drawRangeMarkers(g2, dataArea, i, Layer.BACKGROUND);
		         }*/
		 
		         // now draw annotations and render data items...
		         boolean foundData = false;
		         DatasetRenderingOrder order = getDatasetRenderingOrder();
		         if (order == DatasetRenderingOrder.FORWARD) {
		 
		             // draw background annotations
		             int rendererCount = this.getRendererCount();
		             for (int i = 0; i < rendererCount; i++) {
		                 XYItemRenderer r = getRenderer(i);
		                 if (r != null) {
		                     ValueAxis domainAxis = getDomainAxisForDataset(i);
		                     ValueAxis rangeAxis = getRangeAxisForDataset(i);
		                     r.drawAnnotations(g2, dataArea, domainAxis, rangeAxis, Layer.BACKGROUND, info);
		                 }
		             }
		 
		             // render data items...
		             for (int i = 0; i < getDatasetCount(); i++) {
		              foundData = render(g2, dataArea, i, info, crosshairState) || foundData;
		             }
			         Map axisStateMap = drawAxes(g2, area, dataArea, info);

		             // draw foreground annotations
		             for (int i = 0; i < rendererCount; i++) {
		                 XYItemRenderer r = getRenderer(i);
		                 if (r != null) {
		                     ValueAxis domainAxis = getDomainAxisForDataset(i);
		                     ValueAxis rangeAxis = getRangeAxisForDataset(i);
		                     r.drawAnnotations(g2, dataArea, domainAxis, rangeAxis,
		                             Layer.FOREGROUND, info);
		                 }
		             }
		 
		         }
		         else if (order == DatasetRenderingOrder.REVERSE) {
		 
		             // draw background annotations
		             int rendererCount = this.getRendererCount();
		             for (int i = rendererCount - 1; i >= 0; i--) {
		                 XYItemRenderer r = getRenderer(i);
		                 if (i >= getDatasetCount()) { // we need the dataset to make
		                     continue;                 // a link to the axes
		                 }
		                 if (r != null) {
		                     ValueAxis domainAxis = getDomainAxisForDataset(i);
		                     ValueAxis rangeAxis = getRangeAxisForDataset(i);
		                     r.drawAnnotations(g2, dataArea, domainAxis, rangeAxis,
		                             Layer.BACKGROUND, info);
		                 }
		             }
		 
		             for (int i = getDatasetCount() - 1; i >= 0; i--) {
		                foundData = render(g2, dataArea, i, info, crosshairState)
		                     || foundData;
		             }
		 
		             // draw foreground annotations
		             for (int i = rendererCount - 1; i >= 0; i--) {
		                 XYItemRenderer r = getRenderer(i);
		                 if (i >= getDatasetCount()) { // we need the dataset to make
		                     continue;                 // a link to the axes
		                 }
		                 if (r != null) {
		                     ValueAxis domainAxis = getDomainAxisForDataset(i);
		                     ValueAxis rangeAxis = getRangeAxisForDataset(i);
		                     r.drawAnnotations(g2, dataArea, domainAxis, rangeAxis,
		                             Layer.FOREGROUND, info);
		                 }
		             }
		 
		         }
		 
		         // draw domain crosshair if required...
		         int xAxisIndex = crosshairState.getDomainAxisIndex();
		         ValueAxis xAxis = getDomainAxis(xAxisIndex);
		         RectangleEdge xAxisEdge = getDomainAxisEdge(xAxisIndex);
		         if (!this.isDomainCrosshairLockedOnData() && anchor != null) {
		             double xx;
		             if (orient == PlotOrientation.VERTICAL) {
		                 xx = xAxis.java2DToValue(anchor.getX(), dataArea, xAxisEdge);
		             } 
		             else {
		                 xx = xAxis.java2DToValue(anchor.getY(), dataArea, xAxisEdge);
		             }
		             crosshairState.setCrosshairX(xx);
		         }
		         setDomainCrosshairValue(crosshairState.getCrosshairX(), false);
		         if (isDomainCrosshairVisible()) {
		             double x = getDomainCrosshairValue();
		             Paint paint = getDomainCrosshairPaint();
		             Stroke stroke = getDomainCrosshairStroke();
		             drawDomainCrosshair(g2, dataArea, orient, x, xAxis, stroke, paint);
		         }
		 
		         // draw range crosshair if required...
		         int yAxisIndex = crosshairState.getRangeAxisIndex();
		         ValueAxis yAxis = getRangeAxis(yAxisIndex);
		         RectangleEdge yAxisEdge = getRangeAxisEdge(yAxisIndex);
		         if (!this.isRangeCrosshairLockedOnData() && anchor != null) {
		             double yy;
		             if (orient == PlotOrientation.VERTICAL) {
		                 yy = yAxis.java2DToValue(anchor.getY(), dataArea, yAxisEdge);
		             } else {
		                 yy = yAxis.java2DToValue(anchor.getX(), dataArea, yAxisEdge);
		             }
		             crosshairState.setCrosshairY(yy);
		         }
		         setRangeCrosshairValue(crosshairState.getCrosshairY(), false);
		         if (isRangeCrosshairVisible()) {
		             double y = getRangeCrosshairValue();
		             Paint paint = getRangeCrosshairPaint();
		             Stroke stroke = getRangeCrosshairStroke();
		             drawRangeCrosshair(g2, dataArea, orient, y, yAxis, stroke, paint);
		         }
		 
		         if (!foundData) {
		             drawNoDataMessage(g2, dataArea);
		         }
		 
		        /* for (int i = 0; i < this.getRendererCount(); i++) {
		             drawDomainMarkers(g2, dataArea, i, Layer.FOREGROUND);
		         }
		         for (int i = 0; i < this.getRendererCount(); i++) {
		             drawRangeMarkers(g2, dataArea, i, Layer.FOREGROUND);
		         }
		 
		         drawAnnotations(g2, dataArea, info);
		         g2.setClip(originalClip);
		         g2.setComposite(originalComposite);
		 
		         drawOutline(g2, dataArea);*/
		 
	}

	private void setAxisRange(ValueAxis axis) {
		double max = Math.abs(axis.getLowerBound());
		if (axis.getUpperBound() > max) {
			   max = axis.getUpperBound();
		}
		max += 1500;
		axis.setRange(-max, max);
	}
	
	@Override
	protected void drawZeroRangeBaseline(Graphics2D g2, Rectangle2D area) {
		if (isRangeZeroBaselineVisible()) {
			getRenderer().drawRangeLine(g2, this, getRangeAxis(), area, 0.0,	getRangeAxis().getAxisLinePaint(), getRangeAxis().getAxisLineStroke());
		}
	}
	
	@Override
	protected void drawZeroDomainBaseline(Graphics2D g2, Rectangle2D area) {
		if (isDomainZeroBaselineVisible()) {
			XYItemRenderer r = getRenderer();
			if (r instanceof AbstractXYItemRenderer) {
				AbstractXYItemRenderer renderer = (AbstractXYItemRenderer) r;
				renderer.drawDomainLine(g2, this, getDomainAxis(), area, 0.0, getDomainZeroBaselinePaint(), getDomainZeroBaselineStroke());
			}
		}
	}
	
	
}
