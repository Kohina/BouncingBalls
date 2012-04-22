import java.awt.Color;


public class Ball {
	private double radius;
	private int mass;
	private double x;
	private double y;
	private double vx;
	private double vy;
	private Color color;
	
	public Ball(double x, double y, double radius, int mass, double vx, double vy, Color color) {
		this.setPosition(x, y);
		this.radius=radius;
		this.mass=mass;
		this.setVelocity(vx, vy);
		this.color = color;
	}
	
	public void setPosition(double x, double y) {
		this.x=x;
		this.y=y;
	}
	
	public void setVelocity(double vx, double vy) {
		this.vx = vx;
		this.vy = vy;
	}
	
	public double getRadius() {
		return this.radius;
	}
	
	public int getMass() {
		return this.mass;
	}
	
	public double getX() {
		return this.x;
	}
	
	public double getY() {
		return this.y;
	}
	
	public double getVx() {
		return this.vx;
	}
	
	public double getVy() {
		return this.vy;
	}
	
	public Color getColor() {
		return this.color;
	}
}