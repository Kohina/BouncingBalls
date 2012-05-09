import java.awt.*;

/**
 * Class that handles the physics between two bouncing balls. The class handles
 * the impact of the gravitational force, collision of the two balls and
 * collision to the walls.
 * 
 * Known issue: Because of the fix that makes it impossible for the balls to 
 * overlap each other or go (partly) outside of the box, the balls may get
 * stuck towards a wall and gain a lot of kinetic energy that isn't in the
 * system in the beginning. If the features are turned off, the total energy
 * in the system will remain at the initial level, but the balls may get 
 * stuck in each other or in the walls.
 * 
 * @author Nina Malm (910509-3646, ninam@student.chalmers.se, IT)
 * @author Marika Hansson (910305-0804, hmarika@student.chalmers.se, IT) <br>
 * Time spent: NM: 12.25 h, MH: 12.25 h
 * 
 */
public class Bounce extends Animation {

	protected double deltaT, pixelsPerMeter, grav;
	protected int radius;
	protected boolean firstTime = true;
	protected Color color = Color.red;
	protected Ball[] balls = new Ball[2];

	protected void initAnimator() {
		deltaT = 0.005; // simulation time interval in seconds
		setDelay((int) (1000 * deltaT)); // needed for Animation superclass
		pixelsPerMeter = 40;
		grav = 9.8;

		balls[0] = new Ball(1, 3, 20, 7, 0.05, 0.05, Color.red);
		balls[1] = new Ball(1, 1, 20, 7, 5, 4, Color.blue);		
	}

	protected void paintAnimator(Graphics g) {
		clearFrame(g);
		
		handleWallCollision(balls[0]);
		handleWallCollision(balls[1]);

		handleCollision(balls[0], balls[1]);
		
		applyGravity(balls[0]);
		applyGravity(balls[1]);
		
		applyPositionChange(balls[0]);
		applyPositionChange(balls[1]);
		
		paintBall(balls[0], g);
		paintBall(balls[1], g);
	}

	private void clearFrame(Graphics g) {
		g.setColor(Color.white);
		if (firstTime) {
			g.fillRect(0, 0, d.width, d.height);
			firstTime = false;
		} // g.fillRect(0,0,d.width,d.height); // slower?
		
		int pixelX1 = (int) (pixelsPerMeter * balls[0].getX()); // screen position
		int pixelY1 = (int) (pixelsPerMeter * balls[0].getY());
		int pixelX2 = (int) (pixelsPerMeter * balls[1].getX()); // screen position
		int pixelY2 = (int) (pixelsPerMeter * balls[1].getY());
		g.fillOval(pixelX1 - balls[0].getRadius(), d.height - pixelY1
				- balls[0].getRadius(), balls[0].getRadius() * 2,
				balls[0].getRadius() * 2);
		g.fillOval(pixelX2 - balls[1].getRadius(), d.height - pixelY2
				- balls[1].getRadius(), balls[1].getRadius() * 2,
				balls[1].getRadius() * 2);
		g.drawLine(pixelX1, d.height -pixelY1, pixelX1+(int)(balls[0].getVx()*10), d.height -(int)(pixelY1+balls[0].getVy()*10));
		g.drawLine(pixelX2, d.height -pixelY2, pixelX2+(int)(balls[1].getVx()*10), d.height -(int)(pixelY2+balls[1].getVy()*10));
	}
	
	private void paintBall(Ball ball, Graphics g) {
		int pixelX = (int) (pixelsPerMeter * ball.getX());
		int pixelY = (int) (pixelsPerMeter * ball.getY());
		
		g.setColor(ball.getColor());
		g.fillOval(pixelX - ball.getRadius(), 
				d.height - pixelY - ball.getRadius(), 
				ball.getRadius() * 2,
				ball.getRadius() * 2);
		g.setColor(ball.getColor().darker());
		g.drawLine(pixelX, d.height -pixelY, pixelX+(int)(ball.getVx()*10), d.height -(int)(pixelY+ball.getVy()*10));
	}
	
	private void handleWallCollision(Ball ball) {
		Ball next = ball.clone();
		applyGravity(next);
		applyPositionChange(next);
		
		if (next.getX()*pixelsPerMeter - next.getRadius() <= 0) { // left wall
			ball.setVelocity(-ball.getVx(), ball.getVy());
		}
		if (next.getX()*pixelsPerMeter + next.getRadius() >= d.width) { // right wall
			ball.setVelocity(-ball.getVx(), ball.getVy());
		}
		if (next.getY()*pixelsPerMeter - next.getRadius() <= 0) { // bottom wall
			ball.setVelocity(ball.getVx(), -ball.getVy());
		}
		if (next.getY()*pixelsPerMeter + next.getRadius() >= d.height) { // top wall
			ball.setVelocity(ball.getVx(), -ball.getVy());
		}
		
		//fixOverlap(ball);
	}
	
	private boolean collisionHandled = false; 
	
	private void handleCollision(Ball ball1, Ball ball2) {
		Ball next1 = ball1.clone();
		applyGravity(next1);
		applyPositionChange(next1);
		Ball next2 = ball2.clone();
		applyGravity(next2);
		applyPositionChange(next2);
		
		int dX = (int)((next1.getX()-next2.getX())*pixelsPerMeter); // Avst�nd mellan bollarna i x-led
		int dY = (int)((next1.getY()-next2.getY())*pixelsPerMeter); // Avst�nd mellan bollarna i y-led
		
		if (Math.sqrt(dX*dX + dY*dY) <= ball1.getRadius() + ball2.getRadius()) {
			if (!collisionHandled) {
			double a = Math.atan((double) dY / dX); // vinkeln som bollarna tr�ffas i

			double V1 = Math.sqrt(Math.pow(ball1.getVx(), 2) + Math.pow(ball1.getVy(), 2)); //Total velocity for ball1
			double aV1 = Math.atan(ball1.getVy() / ball1.getVx()); //Velocity angel ball1
			double V2 = Math.sqrt(Math.pow(ball2.getVx(), 2) + Math.pow(ball2.getVy(), 2)); //Total velocity for ball2
			double aV2 = Math.atan(ball2.getVy() / ball2.getVx()); //Velocity angel ball2
			double a1 = aV1 - a; //Angel in the new coordinate-system for ball1
			double a2 = aV2 - a; //Angel in the new coordinate-system for ball2

			double Vf1 = V1 * Math.cos(a1);	//velocity along the line between the centers of the balls (f-axis)
			double Vg1 = V1 * Math.sin(a1); //velocity along the line perpendicular to the line between the centers of the balls (g-axis)
			double Vf2 = V2 * Math.cos(a2);
			double Vg2 = V2 * Math.sin(a2);
			
			System.out.println("a: " + a);
			System.out.println("U1: " + V1 + ", " + aV1 + " radians");
			System.out.println("U2: " + V2 + ", " + aV2 + " radians");

			int m1 = ball1.getMass();
			int m2 = ball2.getMass();
			double Uf1 = Vf1;
			double Uf2 = Vf2;
			Vf1 = (m1 * Uf1 + 2 * m2 * Uf2 - m2 * Uf1) / (m1 + m2);
			Vf2 = -Uf2 + Uf1 + Vf1;
			
			System.out.println("Uf1: " + Uf1 + ", Vf1: " + Vf1);
			System.out.println("Uf2: " + Uf2 + ", Vf2: " + Vf2);

			double b1 = Math.atan(Vg1 / Vf1); //angle between the f-axis and the new velocity
			double b2 = Math.atan(Vg2 / Vf2);
			V1 = Math.sqrt(Math.pow(Vf1, 2) + Math.pow(Vg1, 2)); //new total velocity for ball1
			V2 = Math.sqrt(Math.pow(Vf2, 2) + Math.pow(Vg2, 2)); //new total velocity for ball2

			ball1.setVelocity(V1 * Math.cos(a + b1), V1 * Math.sin(a + b1)); //convert velocity back to x, y coordinates and set
			ball2.setVelocity(V2 * Math.cos(a + b2), V2 * Math.sin(a + b2));
			
			System.out.println("V1: " + V1 + ", " + (b1+a) + " radians");
			System.out.println("V2: " + V2 + ", " + (b2+a) + " radians");
			System.out.println("-----------------------------------------------");
			
			//fixOverlap(balls[0], balls[1]);
			collisionHandled = true;
			}
			
		} else {
			collisionHandled = false;
		}
		/*try {
			Thread.currentThread().sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}
	
	private void fixOverlap(Ball ball1, Ball ball2) {
		double radiusSum = ((double)(ball1.getRadius() + ball2.getRadius()))/pixelsPerMeter;
		double dX = (ball1.getX()-ball2.getX());
		double dY = (ball1.getY()-ball2.getY());
		
		if (dX > 0) {
			double overX = (dX - radiusSum);
			if(overX < 0){
				ball1.setPosition(ball1.getX()-overX/2, ball1.getY());
				ball2.setPosition(ball2.getX()+overX/2, ball2.getY());
				double inWall = ((double)(ball1.getX()*pixelsPerMeter+ball1.getRadius()-d.width))/pixelsPerMeter;
				if (inWall > 0) {
					ball1.setPosition(ball1.getX()-inWall, ball1.getY());
					ball2.setPosition(ball2.getX()-inWall, ball2.getY());
				}
				inWall = ((double)(ball2.getX()*pixelsPerMeter-ball2.getRadius()))/pixelsPerMeter;
				if (inWall < 0) {
					ball1.setPosition(ball1.getX()-inWall, ball1.getY());
					ball2.setPosition(ball2.getX()-inWall, ball2.getY());
				}
			}
		} else {
			double overX = (dX + radiusSum);
			if(overX > 0){
				ball1.setPosition(ball1.getX()-overX/2, ball1.getY());
				ball2.setPosition(ball2.getX()+overX/2, ball2.getY());
			}
			double inWall = ((double)(ball1.getX()*pixelsPerMeter-ball1.getRadius()))/pixelsPerMeter;
			if (inWall < 0) {
				ball1.setPosition(ball1.getX()-inWall, ball1.getY());
				ball2.setPosition(ball2.getX()-inWall, ball2.getY());
			}
			inWall = ((double)(ball2.getX()*pixelsPerMeter+ball2.getRadius()-d.width))/pixelsPerMeter;
			if (inWall > 0) {
				ball1.setPosition(ball1.getX()-inWall, ball1.getY());
				ball2.setPosition(ball2.getX()-inWall, ball2.getY());
			}
		}
		
		if (dY > 0) {
			double overY = (dY - radiusSum);
			if(overY < 0){
				ball1.setPosition(ball1.getX(), ball1.getY()-overY/2);
				ball2.setPosition(ball2.getX(), ball2.getY()+overY/2);
			}
			double inWall = ((double)(ball1.getX()*pixelsPerMeter+ball1.getRadius()-d.height))/pixelsPerMeter;
			if (inWall > 0) {
				ball1.setPosition(ball1.getX(), ball1.getY()-inWall);
				ball2.setPosition(ball2.getX(), ball2.getY()-inWall);
			}
			inWall = ((double)(ball2.getX()*pixelsPerMeter-ball2.getRadius()))/pixelsPerMeter;
			if (inWall < 0) {
				ball1.setPosition(ball1.getX(), ball1.getY()-inWall);
				ball2.setPosition(ball2.getX(), ball2.getY()-inWall);
			}
		} else {
			double overY = (dY + radiusSum);
			if(overY > 0){
				ball1.setPosition(ball1.getX(), ball1.getY()-overY/2);
				ball2.setPosition(ball2.getX(), ball2.getY()+overY/2);
			}
			double inWall = ((double)(ball1.getX()*pixelsPerMeter-ball1.getRadius()))/pixelsPerMeter;
			if (inWall < 0) {
				ball1.setPosition(ball1.getX(), ball1.getY()-inWall);
				ball2.setPosition(ball2.getX(), ball2.getY()-inWall);
			}
			inWall = ((double)(ball2.getX()*pixelsPerMeter+ball2.getRadius()-d.height))/pixelsPerMeter;
			if (inWall > 0) {
				ball1.setPosition(ball1.getX(), ball1.getY()-inWall);
				ball2.setPosition(ball2.getX(), ball2.getY()-inWall);
			}
		}
	}
	
	private void fixOverlap(Ball ball) {
		double r = ((double)(ball.getRadius()))/pixelsPerMeter;
		
		if (ball.getX()*pixelsPerMeter - ball.getRadius() <= 0) { // left wall
			ball.setPosition(0+r, ball.getY());
		}
		if (ball.getX()*pixelsPerMeter + ball.getRadius() >= d.width) { // right wall
			ball.setPosition(((double)(d.width))/pixelsPerMeter - r, ball.getY());
		}
		if (ball.getY()*pixelsPerMeter - ball.getRadius() <= 0) { // bottom wall
			ball.setPosition(ball.getX(), 0+r);
		}
		if (ball.getY()*pixelsPerMeter + ball.getRadius() >= d.height) { // top wall
			ball.setPosition(ball.getX(), ((double)(d.height))/pixelsPerMeter - r);
		}
	}
	
	private void applyGravity(Ball ball) {
		//ball.setVelocity(ball.getVx(), ball.getVy() - grav * deltaT);
	}
	
	private void applyPositionChange(Ball ball) {
		ball.setPosition(ball.getX() + ball.getVx() * deltaT,
				ball.getY() + ball.getVy() * deltaT);
	}
	
}