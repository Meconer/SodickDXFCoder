package SodickDXFCoder;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Line2D;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;

import javax.swing.JPanel;

import SodickDXFCoder.GeometricEntity.GeometryType;

public class PlotPanel extends JPanel implements MouseListener {
	private static final long serialVersionUID = 1L;
	private static final int SEL_RECT_SIZE = 5; // no of pixels for selection rectangle size halved.

	@SuppressWarnings("unused")
	private static final Color[] COLARR =  {Color.GREEN,
											Color.RED,
											Color.YELLOW,
											Color.CYAN,
											Color.LIGHT_GRAY,
											Color.ORANGE,
											Color.BLUE,
											Color.PINK,
											Color.MAGENTA};
	
	public ChainList chainList = new ChainList();

	public enum PanelState  { SELECT, DELETE };
	private PanelState panelState = PanelState.SELECT;
	private AffineTransform atScale;
	private double graphicWindowScale;
	private double graphicWindowYrange;
	private double graphicWindowYmax;
	private double graphicWindowXmin;
	private double graphicWindowXrange;

	private double geoXMin;
	private double geoYMin;
	private double geoXMax;
	private double geoYMax;

	private RenderingHints renderHints;
	private Rectangle2D.Double selectionRect;

	public PlotPanel() {
		setBackground(Color.WHITE);
		renderHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
		                     RenderingHints.VALUE_ANTIALIAS_ON);
			renderHints.put(RenderingHints.KEY_RENDERING,
			                RenderingHints.VALUE_RENDER_QUALITY);
		addMouseListener(this);

	}
	
	public PanelState getPanelState() {
		return panelState;
	}

	public void setPanelState(PanelState panelState) {
		this.panelState = panelState;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		if (!chainList.isEmpty()) {
			Graphics2D gr = (Graphics2D) g;

			checkGeoSize();
			setInitialScale();

			AffineTransform saveXform = gr.getTransform();
			gr.transform(atScale);
			
			gr.setRenderingHints(renderHints);

			setBackground(Color.BLACK);
			drawAxes(gr);

			drawList(g);
			if (selectionRect != null ) {
				gr.setColor(Color.GRAY);
				gr.draw(selectionRect);
			}
			gr.setTransform(saveXform);
		}

	}

	private void drawAxes(Graphics2D gr) {
		double xrange = geoXMax - geoXMin;
		double yrange = geoYMax - geoYMin;

		float dashLength = (float) Math.min(xrange, yrange) / 20;
		if (dashLength == 0) {
			dashLength = 0.1f;
		}
		if (dashLength < 0) dashLength = 5;
		float dash[] = new float[2];
		dash[0] = dashLength;
		dash[1] = dashLength / 4; 

		BasicStroke stroke = new BasicStroke(0.01f , BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 10.0f, dash, 0.0f);
		gr.setStroke(stroke);

		gr.setColor(Color.CYAN);
		// Draw x axis
		gr.draw( new Line2D.Double( geoXMin - xrange * 0.1 , 0 , geoXMax +  xrange * 0.1 , 0 ));

		// Draw y axis
		gr.draw( new Line2D.Double( 0 , geoYMin - yrange * 0.1 , 0 , geoYMax + yrange * 0.1 ));
		
/*		// Draw wider cross at center
		stroke = new BasicStroke( 0.05f );
		gr.setStroke( stroke );
		gr.draw(new Line2D.Double(-1,0,1,0));
		gr.draw(new Line2D.Double(0,-1,0,1));
		*/
	}

	
	
	private void setGraphicWindowScale() {
		int xwidth = getWidth();
		int yheight = getHeight();
		
		atScale = new AffineTransform();

		double xmidpoint = graphicWindowXmin + graphicWindowXrange / 2;
		double ymidpoint = graphicWindowYmax - graphicWindowYrange / 2;

		double xScale = xwidth / graphicWindowXrange;
		double yScale = yheight / graphicWindowYrange;
		if (xScale < yScale) {
			graphicWindowScale = xScale;
			graphicWindowYrange = graphicWindowYrange * yScale / xScale;
			graphicWindowYmax = ymidpoint + ( graphicWindowYrange / 2 ) ;
		} else {
			graphicWindowScale = yScale;
			graphicWindowXrange = graphicWindowXrange * xScale / yScale;
			graphicWindowXmin = xmidpoint - ( graphicWindowXrange / 2 )  ;
		}
		
					
		atScale.setToScale(graphicWindowScale, -graphicWindowScale);
		AffineTransform atTranslate = new AffineTransform();
		atTranslate.setToTranslation(-graphicWindowXmin, -graphicWindowYmax);
		atScale.concatenate(atTranslate);
		// repaint();
	}
	
	

	public void setInitialScale() {

		graphicWindowXrange = geoXMax - geoXMin;
		double xmidpoint = (geoXMax + geoXMin)/2;
		graphicWindowXrange = graphicWindowXrange *1.2; // 20 % extra space
		graphicWindowXmin = xmidpoint - graphicWindowXrange/2;

		graphicWindowYrange = geoYMax - geoYMin;
		double ymidpoint = (geoYMax + geoYMin)/2;
		graphicWindowYrange = graphicWindowYrange *1.2; // 20 % extra space
		double ymin = ymidpoint - graphicWindowYrange/2;
		graphicWindowYmax = ymin + graphicWindowYrange;
		setGraphicWindowScale();
	}

	public void drawList(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
		
		
		if ( !chainList.isEmpty() ) {
	
			Iterator<Chain> chainIter = chainList.iterator();
			while (chainIter.hasNext()) {
				BasicStroke stroke = new BasicStroke( 0.05f );
				g2d.setStroke( stroke );
				
				Chain ch = chainIter.next();
				Boolean first = true; // Flag for first element
				
				Color currColor;
				if (ch.isSelected()) {
					currColor = Color.YELLOW;
				} else {
					currColor = Color.CYAN;
				}

				Iterator<GeometricEntity> geo = ch.entityList.iterator();
				while (geo.hasNext()) {
					GeometricEntity geoEnt = geo.next();
					g2d.setColor(currColor);
					if (geoEnt.geometryType == GeometryType.LINE ) {
						// Object is a line
						Line l = (Line) geoEnt;
						if (l.isSelected()) { 
							g2d.setColor(Color.RED);
						}
						if (first) {
							if (ch.isSelected()) {
								g2d.setColor(Color.MAGENTA); 
							} else {
								g2d.setColor(Color.GREEN);
							}
							first = false;
						}
						g2d.draw(new Line2D.Double(l.x1, l.y1, l.x2, l.y2));
					}
					if (geoEnt.geometryType == GeometryType.ARC ) {
						// Object is an arc
						Arc a = (Arc)geoEnt;
						if (a.isSelected()) { 
							g2d.setColor(Color.RED);
						}
						Arc2D.Double a2d = new Arc2D.Double();
						double endAng = a.stAng + a.angExt;
						double stAng = 360 - endAng;
						double angExt = a.angExt;
						a2d.setArcByCenter(a.xCenter, a.yCenter, a.radius, stAng, angExt,Arc2D.OPEN);
						g2d.draw(a2d);
					}
				}
			}
		}
	}

	private void checkGeoSize() {
		geoXMin = 999999.0;
		geoYMin = geoXMin;
		geoXMax = -geoXMin;
		geoYMax = -geoXMin;
		Iterator<Chain> chainIter = chainList.iterator();
		while (chainIter.hasNext()) {
			Iterator<GeometricEntity> geo = chainIter.next().entityList.iterator();
			while (geo.hasNext()) {
				GeometricEntity geoEnt = geo.next();
				geoXMin = Math.min(geoEnt.getX1(), geoXMin);
				geoXMin = Math.min(geoEnt.getX2(), geoXMin);
				geoXMax = Math.max(geoEnt.getX1(), geoXMax);
				geoXMax = Math.max(geoEnt.getX2(), geoXMax);
				geoYMin = Math.min(geoEnt.getY1(), geoYMin);
				geoYMin = Math.min(geoEnt.getY2(), geoYMin);
				geoYMax = Math.max(geoEnt.getY1(), geoYMax);
				geoYMax = Math.max(geoEnt.getY2(), geoYMax);
				
			}
			
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		Point clickedPoint = e.getPoint();
		int xLl = clickedPoint.x - SEL_RECT_SIZE;
		int yLl = clickedPoint.y + SEL_RECT_SIZE;
		int xUr = clickedPoint.x + SEL_RECT_SIZE;
		int yUr = clickedPoint.y - SEL_RECT_SIZE;
		Point2D.Double urPoint2D = new Point2D.Double((double) xUr, (double) yUr);
		Point2D.Double llPoint2D = new Point2D.Double((double) xLl, (double) yLl);
		Point2D.Double clickedPoint2D = new Point2D.Double((double) clickedPoint.x, (double) clickedPoint.y);

		
		System.out.println("mouseClicked : (" + clickedPoint.x + " , "+clickedPoint.y + ")");
		try {
			atScale.inverseTransform(urPoint2D, urPoint2D);
			atScale.inverseTransform(llPoint2D, llPoint2D);
			atScale.inverseTransform(clickedPoint2D, clickedPoint2D);
		} catch (NoninvertibleTransformException e1) {
			e1.printStackTrace();
		}
		System.out.println("transformed clP : (" + clickedPoint2D.x + " , "+clickedPoint2D.y + ")");
		double selRectSize = urPoint2D.x - llPoint2D.x;
		
		selectionRect = new Rectangle2D.Double(llPoint2D.x,llPoint2D.y,selRectSize,selRectSize);
		chainList.selectElement(selectionRect);
		repaint();
	    
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
