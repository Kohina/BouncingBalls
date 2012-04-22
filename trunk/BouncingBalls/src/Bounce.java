import java.awt.*;

public class Bounce extends Animation {

	protected double deltaT, pixelsPerMeter, grav;
	protected int radius, firstTime = 1, pixelX1, pixelY1, pixelX2, pixelY2;
	protected Color color = Color.red;
	protected Ball[] balls = new Ball[2];

	protected void initAnimator() {
		deltaT = 0.005; // simulation time interval in seconds
		setDelay((int) (1000 * deltaT)); // needed for Animation superclass
		radius = 25; // in pixels!
		pixelsPerMeter = 40;
		grav = -9.8;

		balls[0] = new Ball(3, 3, radius, 1, 2, -1.3, Color.red);
		balls[1] = new Ball(1, 1, radius, 1, -1, 5, Color.blue);
	
		pixelX1 = (int) (pixelsPerMeter * balls[0].getX()); // screen position
		pixelY1 = (int) (pixelsPerMeter * balls[0].getY());
		pixelX2 = (int) (pixelsPerMeter * balls[1].getX()); // screen position
		pixelY2 = (int) (pixelsPerMeter * balls[1].getY());
	}

	protected void paintAnimator(Graphics g) {
		g.setColor(Color.white);
		if (firstTime == 1) {
			g.fillRect(0, 0, d.width, d.height);
			firstTime = 0;
		}
		// g.fillRect(0,0,d.width,d.height); // slower?
		g.fillOval(pixelX1 - radius, pixelY1 - radius, radius * 2, radius * 2);
		g.fillOval(pixelX2 - radius, pixelY2 - radius, radius * 2, radius * 2);
		
		if(Math.abs(Math.sqrt(Math.pow(pixelX1-pixelX2, 2)+Math.pow(pixelY1-pixelY2, 2))) < 2*radius) {
			int dX = pixelX1 - pixelX2; //Avstånd mellan bollarna i x-led
			int dY = pixelY1 - pixelY2; //Avstånd mellan bollarna i y-led
			double a = Math.atan((double)dY/dX);  //vinkeln som bollarna träffas i
			
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
						
			double Uf1 = Vf1;
			Vf1 = Vf2;
			Vf2 = Uf1;
			
			double b1 = Math.atan(Vg1/Vf1);
			double b2 = Math.atan(Vg2/Vf2);
			V1 = Math.sqrt(Math.pow(Vf1, 2)+Math.pow(Vg1, 2));
			V2 = Math.sqrt(Math.pow(Vf2, 2)+Math.pow(Vg2, 2));
			
			balls[0].setVelocity(V1*Math.cos(a+b1), V1*Math.sin(a+b1));
			balls[1].setVelocity(V2*Math.cos(a+b2), V2*Math.sin(a+b2));
		}
		
		balls[0].setVelocity(balls[0].getVx(), balls[0].getVy()-grav*deltaT);
		balls[1].setVelocity(balls[1].getVx(), balls[1].getVy()-grav*deltaT);
		
		balls[0].setPosition(balls[0].getX() + balls[0].getVx()*deltaT, balls[0].getY() + balls[0].getVy()*deltaT);
		balls[1].setPosition(balls[1].getX() + balls[1].getVx()*deltaT, balls[1].getY() + balls[1].getVy()*deltaT);

		pixelX1 = (int) (pixelsPerMeter * balls[0].getX());
		pixelY1 = (int) (pixelsPerMeter * balls[0].getY());
		pixelX2 = (int) (pixelsPerMeter * balls[1].getX());
		pixelY2 = (int) (pixelsPerMeter * balls[1].getY());
		
		if (pixelX1 > d.width-radius) {
			pixelX1 = d.width-radius;
			balls[0].setVelocity(-balls[0].getVx(), balls[0].getVy());
		}
		if (pixelX1 < radius) {
			pixelX1 = 0+radius;
			balls[0].setVelocity(-balls[0].getVx(), balls[0].getVy());
		}
		if(pixelY1 > d.height-radius){
			pixelY1 = d.height-radius;
			balls[0].setVelocity(balls[0].getVx(), -balls[0].getVy());
		}
		if (pixelY1 < radius) {
			pixelY1 = 0+radius;
			balls[0].setVelocity(balls[0].getVx(), -balls[0].getVy());
			// TODO: Maybe change here?
			/*if (Vy1 < 5 && Vy1 > -5) {
				Vy1 = grav * deltaT;
			}*/
		}
		
		if(pixelX2 > d.width-radius){
			pixelX2 = d.width-radius;
			balls[1].setVelocity(-balls[1].getVx(), balls[1].getVy());
		}
		if (pixelX2 < radius) {
			pixelX2 = 0+radius;
			balls[1].setVelocity(-balls[1].getVx(), balls[1].getVy());
		}
		if(pixelY2 > d.height-radius){
			pixelX2 = d.height-radius;
			balls[1].setVelocity(balls[1].getVx(), -balls[1].getVy());
		}
		if (pixelY2 < radius) {
			pixelY2 = 0+radius;
			balls[1].setVelocity(balls[1].getVx(), -balls[1].getVy());
			// TODO: Maybe change here?
			/*if (Vy2 < 5 && Vy2 > -5) {
				Vy2 = grav * deltaT;
			}*/
		}
		
		g.setColor(balls[0].getColor());
		g.fillOval(pixelX1 - radius, pixelY1 - radius, radius * 2, radius * 2);
		g.setColor(balls[1].getColor());
		g.fillOval(pixelX2 - radius, pixelY2 - radius, radius * 2, radius * 2);
	}
}