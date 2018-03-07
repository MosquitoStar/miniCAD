import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class Text2D implements Shape {
	
	private double x,y;
	private String str;
	private int size=36;
	
	public Text2D(double x,double y,Color c,String str){
		this.x=x;
		this.y=y;
		this.str=str;
	}
	
	public double getX(){
		return x;
	}
	
	public double getY(){
		return y;
	}
	
	public String getContent(){
		return str;
	}
	
	public int getSize(){
		return size;
	}
	
	public void setPosition(double x,double y){
		this.x=x;
		this.y=y;
	}
	
	@Override
	public boolean contains(Point2D p) {
        if(p.distance(this.x, this.y) < 5){
            return true;
        }
        else{
            return false;
        }
	}

	@Override
	public boolean contains(Rectangle2D r) {
		System.out.println("The method \"contains(Rectangle2D)\" is not supported!");
		return false;
	}

	@Override
	public boolean contains(double x, double y) {
		Point2D tmp = new Point2D.Double(x, y);
        if(tmp.distance(this.x, this.y) < 5){
            tmp = null;
            return true;
        }
        else{
            tmp = null;
            return false;
        }
	}

	@Override
	public boolean contains(double x, double y, double w, double h) {
		System.out.println("The method \"contains(double,double,double,double)\" is not supported!");
		return false;
	}

	@Override
	public Rectangle getBounds() {
		System.out.println("The method \"getBounds()\" is not supported!");
		return null;
	}

	@Override
	public Rectangle2D getBounds2D() {
		System.out.println("The method \"getBounds2D()\" is not supported!");
		return null;
	}

	@Override
	public PathIterator getPathIterator(AffineTransform at) {
		System.out.println("The method \"getPathIterator(AffineTransform)\" is not supported!");
		return null;
	}

	@Override
	public PathIterator getPathIterator(AffineTransform at, double flatness) {
		System.out.println("The method \"getPathIterator(AffineTransform,double)\" is not supported!");
		return null;
	}

	@Override
	public boolean intersects(Rectangle2D r) {
		System.out.println("The method \"intersects(Rectangle2D)\" is not supported!");
		return false;
	}

	@Override
	public boolean intersects(double x, double y, double w, double h) {
		System.out.println("The method \"intersects(double,double,double,double)\" is not supported!");
		return false;
	}

}
