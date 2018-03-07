import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import javax.swing.JComponent;

public class Canvas extends JComponent {
	private static final long serialVersionUID = 1L;
	
	public Shape curShape;
	Color curColor=Color.BLACK;
	ArrayList<Shape> shapes=new ArrayList<Shape>();
	ArrayList<Color> colors=new ArrayList<Color>();
	ArrayList<Integer> strokes=new ArrayList<Integer>();
	
	double x, y;
    Point2D.Double drawPoint;
	Point2D.Double movePoint;
	
	DrawStatus tool;

	public Canvas(){
		tool=DrawStatus.DRAG;
    	this.addMouseListener(new MouseAdapter(){
    		@Override
            public void mouseClicked(MouseEvent e){
                if(tool==DrawStatus.DRAG||tool==DrawStatus.SCALE){
                    for(int i=0;i<shapes.size();i++){
                    	Shape s=shapes.get(i);
                        if(s instanceof Line2D.Double){
                            if(((Line2D)s).ptLineDist(e.getX(), e.getY()) < 5){
                                curShape = s;
                                repaint();
                                return;
                            }
                        }
                        else if(s.contains(e.getX(), e.getY())){
                            curShape = s;
                            repaint();
                            return;
                        }
                    }
                    curShape = null;
                    repaint();
                }
                else if(drawPoint == null){
                    drawPoint = new Point2D.Double(e.getX(), e.getY());
                    x = e.getX();
                    y = e.getY();
                    if(tool==DrawStatus.LINE){
                        drawShape(Shapes.LINES,x, y, curColor,"");
                    }
                    else if(tool==DrawStatus.RECTANGLE){
                        drawShape(Shapes.RECTANGLES,x, y, curColor,"");
                    }
                    else if(tool==DrawStatus.ELLIPSE){
                        drawShape(Shapes.ELLIPSES,x, y, curColor,"");
                    }
                    else if(tool==DrawStatus.TEXT){
                        drawShape(Shapes.TEXTS, x, y, curColor,Main.textContent.getText());
                        drawPoint = null;
                    }
                    repaint();
                }
                else{
                    curShape = null;
                    drawPoint = null;
                    x = 0;
                    y = 0;
                    repaint();
                }
            }
    		@Override
            public void mouseReleased(MouseEvent e){
                movePoint = null;
            }
        });
        this.addMouseMotionListener(new MouseMotionAdapter(){
        	@Override
            public void mouseMoved(MouseEvent e){
                if(drawPoint!=null){
                    double xp=e.getX();
                    double yp=e.getY();
                    if(tool==DrawStatus.LINE){
                        ((Line2D)curShape).setLine(x,y,xp,yp);
                    }
                    else if(tool==DrawStatus.RECTANGLE){
                        ((Rectangle2D)curShape).setRect(Math.min(x,xp),Math.min(y,yp),Math.abs(x-xp),Math.abs(y-yp));
                    }
                    else if(tool==DrawStatus.ELLIPSE){
                        ((Ellipse2D)curShape).setFrame(Math.min(x,xp),Math.min(y,yp),Math.abs(x-xp),Math.abs(y-yp));
                    }
                    repaint();
                }
            }
            @Override
            public void mouseDragged(MouseEvent e){
                if(curShape!=null){
                    if(movePoint==null){
                        movePoint=new Point2D.Double(e.getX(),e.getY());
                    }
                    if(tool==DrawStatus.DRAG){
                    	if(curShape instanceof Line2D){
                        	Line2D curLine=(Line2D)curShape;
                        	double x1=curLine.getX1() + e.getX() - movePoint.getX();
                        	double y1=curLine.getY1() + e.getY() - movePoint.getY();
                        	double x2=curLine.getX2() + e.getX() - movePoint.getX();
                        	double y2=curLine.getY2() + e.getY() - movePoint.getY();
                            ((Line2D)curShape).setLine(x1,y1,x2,y2);
                        }
                        else if(curShape instanceof Rectangle2D){
                        	Rectangle2D curRect=(Rectangle2D)curShape;
                        	double x=curRect.getX() + e.getX() - movePoint.getX();
                        	double y=curRect.getY() + e.getY() - movePoint.getY();
                        	double w=curRect.getWidth();
                        	double h=curRect.getHeight();
                            ((Rectangle2D)curShape).setRect(x,y,w,h);
                        }
                        else if(curShape instanceof Ellipse2D){
                        	Ellipse2D curElli=(Ellipse2D)curShape;
                        	double x=curElli.getX() + e.getX() - movePoint.getX();
                        	double y=curElli.getY() + e.getY() - movePoint.getY();
                        	double w=curElli.getWidth();
                        	double h=curElli.getHeight();
                            ((Ellipse2D)curShape).setFrame(x,y,w,h);
                        }
                        else if(curShape instanceof Text2D){
                        	double x=((Text2D)curShape).getX()+e.getX()-movePoint.getX();
                        	double y=((Text2D)curShape).getY()+e.getY()-movePoint.getY();
                            ((Text2D)curShape).setPosition(x,y);
                        }
                    }else if(tool==DrawStatus.SCALE){
                    	if(curShape instanceof Line2D){
                        	Line2D curLine=(Line2D)curShape;
                        	double x1,y1,x2,y2;
                        	if(curLine.getX1()>=curLine.getX2()){
                        		x1=curLine.getX1()+e.getX()-movePoint.getX();
                        		x2=curLine.getX2()-e.getX()+movePoint.getX();
                        	}else{
                        		x1=curLine.getX1()-e.getX()+movePoint.getX();
                        		x2=curLine.getX2()+e.getX()-movePoint.getX();
                        	}
                        	if(curLine.getY1()>=curLine.getY2()){
                        		y1=curLine.getY1()+e.getY()-movePoint.getY();
                        		y2=curLine.getY2()-e.getY()+movePoint.getY();
                        	}else{
                        		y1=curLine.getY1()-e.getY()+movePoint.getY();
                        		y2=curLine.getY2()+e.getY()-movePoint.getY();
                        	}
                        	((Line2D)curShape).setLine(x1,y1,x2,y2);
                    	}else if(curShape instanceof Rectangle2D){
                        	Rectangle2D curRect=(Rectangle2D)curShape;
                        	double x=curRect.getX();
                        	double y=curRect.getY();
                        	double w=curRect.getWidth()+e.getX()-movePoint.getX();
                        	double h=curRect.getHeight()+e.getY()-movePoint.getY();
                        	((Rectangle2D)curShape).setRect(x,y,w,h);
                    	}else if(curShape instanceof Ellipse2D){
                        	Ellipse2D curElli=(Ellipse2D)curShape;
                        	double x=curElli.getX();
                        	double y=curElli.getY();
                        	double w=curElli.getWidth()+e.getX() - movePoint.getX();
                        	double h=curElli.getHeight()+ e.getY() - movePoint.getY();
                            ((Ellipse2D)curShape).setFrame(x,y,w,h);
                    	}
                    }
                }
                movePoint=new Point2D.Double(e.getX(),e.getY());
                repaint();
            }
        });
        this.addMouseWheelListener(new MouseWheelListener(){
			@Override
			public void mouseWheelMoved(MouseWheelEvent w) {
				if(curShape!=null){
					int i=shapes.indexOf(curShape);
					if(i!=-1){
						if(w.getWheelRotation()<0){
							strokes.set(i, strokes.get(i)+1);
						}else if(w.getWheelRotation()>0){
							if(strokes.get(i)>1){
								strokes.set(i,strokes.get(i)-1);
							}
						}
					}
				}
				repaint();
			}
        });
    }
    
    enum Shapes{LINES,RECTANGLES,ELLIPSES,TEXTS};
    
    public void drawShape(Shapes s,double x,double y,Color c,String str){
    	Shape tmp;
    	switch (s){
    	case LINES:
    		tmp=new Line2D.Double(x,y,x,y);
    		break;
    	case RECTANGLES:
    		tmp=new Rectangle2D.Double(x,y,0,0);
    		break;
    	case ELLIPSES:
    		tmp=new Ellipse2D.Double(x,y,0,0);
    		break;
    	case TEXTS:
    		tmp=new Text2D(x,y,c,str);
    		break;
    	default:
    		System.out.println("Wrong shape type!");
    		return;
    	}
    	shapes.add(tmp);
    	colors.add(c);
    	strokes.add(1);
    	curShape=tmp;
    }
    
    public void changeColorForCurShape(){
    	int i=shapes.indexOf(curShape);
    	if(i!=-1){
    		colors.set(i,curColor);
    	}
    	repaint();
    }
    
    public void removeCurShape(){
    	if(curShape!=null){
    		int i=shapes.indexOf(curShape);
    		if(i!=-1){
    			shapes.remove(i);
    			colors.remove(i);
    			strokes.remove(i);
    			repaint();
    		}
    	}
    }
    
    public void paint(Graphics g){
    	Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        for(int i=0;i<shapes.size();i++){
        	Shape s=shapes.get(i);
            if(s==curShape){
                if(s instanceof Text2D){
                    g.setFont(new Font("Consolas", Font.BOLD, ((Text2D)s).getSize()));
                    g.setColor(Color.RED);
                    g.drawString(((Text2D)s).getContent(), (int)((Text2D)s).getX(), (int)((Text2D)s).getY());
                }
                else{
                    g2.setStroke(new BasicStroke(strokes.get(i)+2));
                    g2.setPaint(Color.RED);
                    g2.draw(s);
                }
            }
            else{
                if(s instanceof Text2D){
                    g.setFont(new Font("Consolas", Font.PLAIN, ((Text2D)s).getSize()));
                    g.setColor(colors.get(i));
                    g.drawString(((Text2D)s).getContent(), (int)((Text2D)s).getX(), (int)((Text2D)s).getY());
                }
                else{
                    g2.setStroke(new BasicStroke(strokes.get(i)));
                    g2.setPaint(colors.get(i));
                    g2.draw(s);
                }
            }
        }
    }
}
