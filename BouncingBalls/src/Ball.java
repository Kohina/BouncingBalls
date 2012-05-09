import java.awt.Color;

/**
 * Class that represents a ball.
 * 
 * @author Nina Malm (910509-3646, ninam@student.chalmers.se, IT)
 * @author Marika Hansson (910305-0804, hmarika@student.chalmers.se, IT)
 *
 */
public class Ball {
	private int radius;	//in px
	private int mass;	
	private double x;	//in m
	private double y;	//in m
	private double vx;	//in m/s
	private double vy;	//in m/s
	private Color color;
	
	public Ball(double x, double y, int radius, int mass, double vx, double vy, Color color) {
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
	
	public int getRadius() {
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
	
	@Override
	public Ball clone() {
		return new Ball(x, y, radius, mass, vx, vy, color);
	}
}