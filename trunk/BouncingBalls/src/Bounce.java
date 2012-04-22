import java.awt.*;

public class Bounce extends Animation {

	protected double deltaT, pixelsPerMeter, grav;
	protected boolean firstTime = true;
	protected int radius, pixelX1, pixelY1, pixelX2, pixelY2;
	protected Ball[] balls = new Ball[2];

	protected void initAnimator() {
		deltaT = 0.007; // simulation time interval in seconds
		setDelay((int) (1000 * deltaT)); // needed for Animation superclass
		pixelsPerMeter = 40;
		grav = 9.8;

		balls[0] = new Ball(3, 3, 20, 7, 1, 5, Color.red);
		balls[1] = new Ball(1, 1, 15, 7, -2, 5, Color.blue);
	
		pixelX1 = (int) (pixelsPerMeter * balls[0].getX()); // screen position
		pixelY1 = (int) (pixelsPerMeter * balls[0].getY());
		pixelX2 = (int) (pixelsPerMeter * balls[1].getX()); // screen position
		pixelY2 = (int) (pixelsPerMeter * balls[1].getY());
	}

	protected void paintAnimator(Graphics g) {
		g.setColor(Color.white);
		if (firstTime) {
			g.fillRect(0, 0, d.width, d.height);
			firstTime = false;
		}
		// g.fillRect(0,0,d.width,d.height); // slower?
		g.fillOval(pixelX1 - balls[0].getRadius(), d.height - pixelY1 - balls[0].getRadius(), balls[0].getRadius() * 2, balls[0].getRadius() * 2);
		g.fillOval(pixelX2 - balls[1].getRadius(), d.height - pixelY2 - balls[1].getRadius(), balls[1].getRadius() * 2, balls[1].getRadius() * 2);
		
		//System.out.println("Before: b1: (" + balls[0].getX() + ", " + balls[0].getY() + ") v(" + balls[0].getVx() + ", " + balls[0].getVy() + ") \t\t" + 
		//		"b2: (" + balls[1].getX() + ", " + balls[1].getY() + ") v(" + balls[1].getVx() + ", " + balls[1].getVy() + ") \t\t");
		
		if(pixelX1-balls[0].getRadius() <= 0) { //left wall
			balls[0].setVelocity(-balls[0].getVx(), balls[0].getVy());
		}
		if(pixelX1+balls[0].getRadius() >= d.width) { //right wall
			balls[0].setVelocity(-balls[0].getVx(), balls[0].getVy());
		}
		if(pixelY1-balls[0].getRadius() <= 0) { //bottom wall
			balls[0].setVelocity(balls[0].getVx(), -balls[0].getVy());
		}
		if(pixelY1+balls[0].getRadius() >= d.height) { //top wall
			balls[0].setVelocity(balls[0].getVx(), -balls[0].getVy());
		}
		
		if(pixelX2-balls[1].getRadius() <= 0) { //left wall
			balls[1].setVelocity(-balls[1].getVx(), balls[1].getVy());
		}
		if(pixelX2+balls[1].getRadius() >= d.width) { //right wall
			balls[1].setVelocity(-balls[1].getVx(), balls[1].getVy());
		}
		if(pixelY2-balls[1].getRadius() <= 0) { //bottom wall
			balls[1].setVelocity(balls[1].getVx(), -balls[1].getVy());
		}
		if(pixelY2+balls[1].getRadius() >= d.height) { //top wall
			balls[1].setVelocity(balls[1].getVx(), -balls[1].getVy());
		}
		
		
		if(Math.sqrt(Math.pow(pixelX1-pixelX2, 2)+Math.pow(pixelY1-pixelY2, 2)) <= balls[0].getRadius() + balls[1].getRadius()) {
			int dX = pixelX1 - pixelX2;
			int dY = pixelY1 - pixelY2;
			double a = Math.atan((double)dY/dX);
			
			double V1 = Math.sqrt(Math.pow(balls[0].getVx(), 2)+Math.pow(balls[0].getVy(), 2));
			double aV1 = Math.atan(balls[0].getVy()/balls[0].getVx());
			double V2 = Math.sqrt(Math.pow(balls[1].getVx(), 2)+Math.pow(balls[1].getVy(), 2));
			double aV2 = Math.atan(balls[1].getVy()/balls[1].getVx());
			double a1 = aV1-a;
			double a2 = aV2-a;
			
			double Vf1 = V1*Math.cos(a1);
			double Vg1 = V1*Math.sin(a1);
			double Vf2 = V2*Math.cos(a2);
			double Vg2 = V2*Math.sin(a2);
						
			int m1 = balls[0].getMass();
			int m2 = balls[1].getMass();
			double Uf1 = Vf1;
			double Uf2 = Vf2;
			Vf1 = (m1*Uf1+2*m2*Uf2-m2*Uf1)/(m1+m2);
			Vf2 = -Uf2+Uf1+Vf1;
			
			double b1 = Math.atan(Vg1/Vf1);
			double b2 = Math.atan(Vg2/Vf2);
			V1 = Math.sqrt(Math.pow(Vf1, 2)+Math.pow(Vg1, 2));
			V2 = Math.sqrt(Math.pow(Vf2, 2)+Math.pow(Vg2, 2));
			
			balls[0].setVelocity(V1*Math.cos(a+b1), V1*Math.sin(a+b1));
			balls[1].setVelocity(V2*Math.cos(a+b2), V2*Math.sin(a+b2));
			
			double over = Math.abs(((balls[0].getX()*pixelsPerMeter)-(balls[1].getX()*pixelsPerMeter))-(balls[0].getRadius()+balls[1].getRadius()));
			if(over > 0){
				if(balls[0].getX() < balls[1].getX()){
					balls[0].setPosition(balls[0].getX()-((over/2)/pixelsPerMeter), balls[0].getY());
					balls[1].setPosition(balls[1].getX()+((over/2)/pixelsPerMeter), balls[1].getY());
				}
				else{
					balls[1].setPosition(balls[1].getX()-((over/2)/pixelsPerMeter), balls[1].getY());
					balls[0].setPosition(balls[0].getX()+((over/2)/pixelsPerMeter), balls[0].getY());
				}
			}
		}
		
		balls[0].setVelocity(balls[0].getVx(), balls[0].getVy()-grav*deltaT);
		balls[1].setVelocity(balls[1].getVx(), balls[1].getVy()-grav*deltaT);
		
		balls[0].setPosition(balls[0].getX() + balls[0].getVx()*deltaT, balls[0].getY() + balls[0].getVy()*deltaT);
		balls[1].setPosition(balls[1].getX() + balls[1].getVx()*deltaT, balls[1].getY() + balls[1].getVy()*deltaT);

		pixelX1 = (int) (pixelsPerMeter * balls[0].getX());
		pixelY1 = (int) (pixelsPerMeter * balls[0].getY());
		pixelX2 = (int) (pixelsPerMeter * balls[1].getX());
		pixelY2 = (int) (pixelsPerMeter * balls[1].getY());

		g.setColor(balls[0].getColor());
		g.fillOval(pixelX1 - balls[0].getRadius(), d.height - pixelY1 - balls[0].getRadius(), balls[0].getRadius() * 2, balls[0].getRadius() * 2);
		g.setColor(balls[1].getColor());
		g.fillOval(pixelX2 - balls[1].getRadius(), d.height - pixelY2 - balls[1].getRadius(), balls[1].getRadius() * 2, balls[1].getRadius() * 2);
	}
}