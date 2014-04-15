package com.purbon.pdfwriter.chart.extensions.jfreechart;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.jfree.chart.axis.AxisState;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.TickType;
import org.jfree.chart.axis.ValueTick;
import org.jfree.text.TextUtilities;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.TextAnchor;

public class PQAxis extends NumberAxis {

	private static final long serialVersionUID = -5085580382393125494L;

	private PQAxis aAxis;
	private RectangleEdge e;
	private RectangleEdge eL;

	public PQAxis(String label) {
		super(label);
	}

	@Override
	protected AxisState drawLabel(String label, Graphics2D g2, Rectangle2D plotArea, Rectangle2D dataArea, RectangleEdge edge, 	AxisState state) {

		if (state == null) {
			throw new IllegalArgumentException("Null 'state' argument.");
		}

		if ((label == null) || (label.equals(""))) {
			return state;
		}
		
		edge = this.geteL();
		Font font = new Font(getLabelFont().getName(), Font.BOLD, getLabelFont().getSize());			 
		RectangleInsets insets = getLabelInsets();
		g2.setFont(font);
		g2.setPaint(getLabelPaint());
		FontMetrics fm = g2.getFontMetrics();
		Rectangle2D labelBounds = TextUtilities.getTextBounds(label, g2, fm);
		
		if (edge == RectangleEdge.TOP) {
			AffineTransform t = AffineTransform.getRotateInstance(getLabelAngle(), labelBounds.getCenterX(), labelBounds.getCenterY());
			Shape rotatedLabelBounds = t.createTransformedShape(labelBounds); 
			labelBounds = rotatedLabelBounds.getBounds2D();
			double labelx = dataArea.getMaxX();
			double labely = state.getCursor() - 20 - insets.getBottom() - labelBounds.getHeight() / 2.0;
			TextUtilities.drawRotatedString(label, g2, (float) labelx, (float) labely, TextAnchor.CENTER_RIGHT, getLabelAngle(),TextAnchor.CENTER);
			state.cursorUp(insets.getTop() + labelBounds.getHeight()+ insets.getBottom());
			drawLabels(g2, dataArea, new int[]{0,1});
		} else if (edge == RectangleEdge.RIGHT) {
		      AffineTransform t = AffineTransform.getRotateInstance(getLabelAngle() + Math.PI / 2.0, labelBounds.getCenterX(), labelBounds.getCenterY());		    		          
		      Shape rotatedLabelBounds = t.createTransformedShape(labelBounds);
              labelBounds = rotatedLabelBounds.getBounds2D();
			
      		double x = aAxis.valueToJava2D(0, dataArea, getE());

            double labelx = x+insets.getLeft()+labelBounds.getWidth();
			double labely = dataArea.getY() + dataArea.getHeight() / 8.0;
			
			TextUtilities.drawRotatedString(label, g2, (float) labelx,	(float) labely, TextAnchor.CENTER, getLabelAngle() + Math.PI / 2.0, TextAnchor.CENTER);
			state.cursorRight(insets.getLeft() + labelBounds.getWidth() 	+ insets.getRight());
			drawLabels(g2, dataArea, new int[]{2,3});

		}
		return state;
	}

	
	private void drawLabels(Graphics2D g2, Rectangle2D area, int[] filter) {
		String[] labels = new String[]{"QI", "QII", "QIII", "QIV"};
		int oldSize = getLabelFont().getSize();
		Font font = new Font(getLabelFont().getName(), Font.BOLD, 20);			 
		g2.setFont(font);
		for (int i=0; i < labels.length; i++) {
			try {
				if (!contains(filter, i)) {
					throw new IllegalArgumentException();
				}
				drawLabel(g2, area, labels[i], i);
			} catch (IllegalArgumentException ex) {
				
			}
		}
		font = new Font(getLabelFont().getName(), Font.BOLD, oldSize);			 
		g2.setFont(font);
		
	}
	public boolean contains(final int[] array, final int key) {  
	     Arrays.sort(array);  
	     return Arrays.binarySearch(array, key) != -1;  
	}  
	
	private void drawLabel(Graphics2D g2, Rectangle2D area, String text, int i) {
		FontMetrics fm = g2.getFontMetrics();
		Rectangle2D labelBounds = TextUtilities.getTextBounds(text, g2, fm);
		double x = 0.0, y = 0.0;
		if (i == 0) {
			x = area.getMaxX()-(labelBounds.getWidth()*1.5);
			y = area.getMinY()+labelBounds.getHeight();
		} else if ( i == 1) {
			x = area.getMinX()+(labelBounds.getWidth()*1.5);
			y = area.getMinY()+labelBounds.getHeight();
		} else if (i == 2) {
			x = area.getMinX()+(labelBounds.getWidth()*1.5);
			y = area.getMaxY()-labelBounds.getHeight();
		} else if (i == 3) {
			x = area.getMaxX()-labelBounds.getWidth();
			y = area.getMaxY()-labelBounds.getHeight();
		}
		else {
			throw new IllegalArgumentException();
		}
		TextUtilities.drawRotatedString(text, g2, (float) x, (float) y, TextAnchor.CENTER_RIGHT, 0.0, TextAnchor.CENTER);
	}
	
	
	@Override
	@SuppressWarnings("rawtypes")
	protected AxisState drawTickMarksAndLabels(Graphics2D g2, double cursor,
			Rectangle2D plotArea, Rectangle2D dataArea, RectangleEdge edge) {

		double x = aAxis.valueToJava2D(0, dataArea, getE());
		cursor = x;

		AxisState state = new AxisState(cursor);

		if (isAxisLineVisible()) {
			drawAxisLine(g2, cursor, dataArea, edge);
		}

		List ticks = refreshTicks(g2, state, dataArea, edge);
		state.setTicks(ticks);
		g2.setFont(getTickLabelFont());
		Iterator iterator = ticks.iterator();
		while (iterator.hasNext()) {
			ValueTick tick = (ValueTick) iterator.next();
			if (isTickLabelsVisible()) {
				g2.setPaint(getTickLabelPaint());
				float[] anchorPoint = calculateAnchorPoint(tick, cursor,
						dataArea, edge);
				TextUtilities.drawRotatedString(tick.getText(), g2,
						anchorPoint[0], anchorPoint[1], tick.getTextAnchor(),
						tick.getAngle(), tick.getRotationAnchor());
			}

			if ((isTickMarksVisible() && tick.getTickType().equals(
					TickType.MAJOR))
					|| (isMinorTickMarksVisible() && tick.getTickType().equals(
							TickType.MINOR))) {

				double ol = (tick.getTickType().equals(TickType.MINOR)) ? getMinorTickMarkOutsideLength()
						: getTickMarkOutsideLength();

				double il = (tick.getTickType().equals(TickType.MINOR)) ? getMinorTickMarkInsideLength()
						: getTickMarkInsideLength();

				float xx = (float) valueToJava2D(tick.getValue(), dataArea,
						edge);
				Line2D mark = null;
				g2.setStroke(getTickMarkStroke());
				g2.setPaint(getTickMarkPaint());
				if (edge == RectangleEdge.LEFT) {
					mark = new Line2D.Double(cursor - ol, xx, cursor + il, xx);
				} else if (edge == RectangleEdge.RIGHT) {
					mark = new Line2D.Double(cursor + ol, xx, cursor - il, xx);
				} else if (edge == RectangleEdge.TOP) {
					mark = new Line2D.Double(xx, cursor - ol, xx, cursor + il);
				} else if (edge == RectangleEdge.BOTTOM) {
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
			} else if (edge == RectangleEdge.RIGHT) {
				used = findMaximumTickLabelWidth(ticks, g2, plotArea,
						isVerticalTickLabels());
				state.cursorRight(used);
			} else if (edge == RectangleEdge.TOP) {
				used = findMaximumTickLabelHeight(ticks, g2, plotArea,
						isVerticalTickLabels());
				state.cursorUp(used);
			} else if (edge == RectangleEdge.BOTTOM) {
				used = findMaximumTickLabelHeight(ticks, g2, plotArea,
						isVerticalTickLabels());
				state.cursorDown(used);
			}
		}
	
		return state;
	}

	public RectangleEdge geteL() {
		return eL;
	}

	public void seteL(RectangleEdge eL) {
		this.eL = eL;
	}

	public RectangleEdge getE() {
		return e;
	}

	public void setE(RectangleEdge e) {
		this.e = e;
	}

	public PQAxis getaAxis() {
		return aAxis;
	}

	public void setaAxis(PQAxis aAxis) {
		this.aAxis = aAxis;
	}
}
