package com.purbon.pdfwriter.chart.extensions.jfreechart;

import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;
import java.util.List;

import org.jfree.chart.axis.AxisState;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTick;
import org.jfree.chart.axis.ValueTick;
import org.jfree.text.TextUtilities;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;

public class NumberAxisWithUnits extends NumberAxis {


	private static final long serialVersionUID = 1681086733800848700L;
	private String units = "";

	public NumberAxisWithUnits(String label) {
		this(label,"");
	}
	
	public NumberAxisWithUnits(String label, String units) {
		super(label);
		this.units = units;
		this.setLabelInsets(new RectangleInsets(20,20,20,20));
		
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected AxisState drawTickMarksAndLabels(Graphics2D g2, double cursor, Rectangle2D plotArea, Rectangle2D dataArea, RectangleEdge edge) {
	
		 AxisState state = new AxisState(cursor);
		  
		          if (isAxisLineVisible()) {
		              drawAxisLine(g2, cursor, dataArea, edge);
		          }
		  
		          double ol = getTickMarkOutsideLength();
		          double il = getTickMarkInsideLength();
		  
		          List ticks = refreshTicks(g2, state, dataArea, edge);
		          state.setTicks(ticks);
		          g2.setFont(getTickLabelFont());
		          Iterator iterator = ticks.iterator();
		          while (iterator.hasNext()) {
		              ValueTick tick = (ValueTick) iterator.next();
		              if (!this.units.isEmpty()) {
		            	  String label = tick.getText()+" "+this.units;
		            	  if (!tick.getText().isEmpty())
		            		  tick = new NumberTick(tick.getValue(), label, tick.getTextAnchor(), tick.getRotationAnchor(), tick.getAngle());
		              }
		              if (isTickLabelsVisible()) {
		                  g2.setPaint(getTickLabelPaint());
		                  
		                  float[] anchorPoint = calculateAnchorPoint(tick, cursor, dataArea, edge);
		                  TextUtilities.drawRotatedString(tick.getText(), g2, anchorPoint[0], anchorPoint[1], tick.getTextAnchor(),tick.getAngle(), tick.getRotationAnchor());
		              }
		  
		              if (isTickMarksVisible()) {
		                  float xx = (float) valueToJava2D(tick.getValue(), dataArea, 
		                          edge);
		                  Line2D mark = null;
		                  g2.setStroke(getTickMarkStroke());
		                  g2.setPaint(getTickMarkPaint());
		                  if (edge == RectangleEdge.LEFT) {
		                      mark = new Line2D.Double(cursor - ol, xx, cursor + il, xx);
		                  }
		                  else if (edge == RectangleEdge.RIGHT) {
		                      mark = new Line2D.Double(cursor + ol, xx, cursor - il, xx);
		                  }
		                  else if (edge == RectangleEdge.TOP) {
		                      mark = new Line2D.Double(xx, cursor - ol, xx, cursor + il);
		                  }
		                  else if (edge == RectangleEdge.BOTTOM) {
		                      mark = new Line2D.Double(xx, cursor + ol, xx, cursor - il);
		                  }
		                  g2.draw(mark);
		              }
		          }
		          
		          // need to work out the space used by the tick labels...
		          // so we can update the cursor...
		          double used = 0.0;
		          if (isTickLabelsVisible()) {
		              if (edge == RectangleEdge.LEFT) {
		                  used += findMaximumTickLabelWidth(ticks, g2, plotArea, 
		                          isVerticalTickLabels());  
		                  state.cursorLeft(used);      
		              }
		              else if (edge == RectangleEdge.RIGHT) {
		                  used = findMaximumTickLabelWidth(ticks, g2, plotArea, 
		                          isVerticalTickLabels());
		                  state.cursorRight(used);      
		              }
		              else if (edge == RectangleEdge.TOP) {
		                  used = findMaximumTickLabelHeight(ticks, g2, plotArea, 
		                          isVerticalTickLabels());
		                  state.cursorUp(used);
		              }
		              else if (edge == RectangleEdge.BOTTOM) {
		                  used = findMaximumTickLabelHeight(ticks, g2, plotArea, 
		                          isVerticalTickLabels());
		                  state.cursorDown(used);
		              }
		          }
		         
		          return state;
		      }	
	
	}
	

