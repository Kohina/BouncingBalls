import java.awt.*;

public class Bounce extends Animation {

	protected double X1, X2, Y1, Y2, Vx1, Vx2, Vy1, Vy2, deltaT, pixelsPerMeter, grav;
	protected int radius, firstTime = 1, pixelX1, pixelY1, pixelX2, pixelY2;
	protected Color color = Color.red;

	protected void initAnimator() {
		deltaT = 0.005; // simulation time interval in seconds
		setDelay((int) (1000 * deltaT)); // needed for Animation superclass

		X1 = 3; // in meters
		Y1 = 3; // Y reference direction downwards!
		Vx1 = 2; // in m/s
		Vy1 = -1.3;

		X2 = 4; // in meters
		Y2 = 1; // Y reference direction downwards!
		Vx2 = -1; // in m/s
		Vy2 = 5;
		pixelsPerMeter = 40;
		grav = -9.8;

		radius = 25; // in pixels!
		pixelX1 = (int) (pixelsPerMeter * X1); // screen position
		pixelY1 = (int) (pixelsPerMeter * Y1);
		pixelX2 = (int) (pixelsPerMeter * X2); // screen position
		pixelY2 = (int) (pixelsPerMeter * Y2);
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

		if (pixelX1 < radius || pixelX1 > d.width - radius) {
			Vx1 = -Vx1;
		}
		if (pixelY1 < radius || pixelY1 > d.height - radius) {
			Vy1 = -Vy1;
			// TODO: Maybe change here?
			/*if (Vy1 < 5 && Vy1 > -5) {
				Vy1 = grav * deltaT;
			}*/
		}

		if (pixelX2 < radius || pixelX2 > d.width - radius) {
			Vx2 = -Vx2;
		}
		if (pixelY2 < radius || pixelY2 > d.height - radius) {
			Vy2 = -Vy2;
			// TODO: Maybe change here?
			/*if (Vy2 < 5 && Vy2 > -5) {
				Vy2 = grav * deltaT;
			}*/
		}
		
		if(Math.abs(Math.sqrt(Math.pow(pixelX1-pixelX2, 2)+Math.pow(pixelY1-pixelY2, 2))) < 2*radius) {
			Vy1 = -Vy1;
			Vx1 = -Vx1;
			Vy2 = -Vy2;
			Vx2 = -Vx2;
		}
		
		Vy1 -= grav * deltaT;
		Vy2 -= grav * deltaT;

		X1 += Vx1 * deltaT;
		Y1 += Vy1 * deltaT;

		X2 += Vx2 * deltaT;
		Y2 += Vy2 * deltaT;

		pixelX1 = (int) (pixelsPerMeter * X1);
		pixelY1 = (int) (pixelsPerMeter * Y1);
		pixelX2 = (int) (pixelsPerMeter * X2);
		pixelY2 = (int) (pixelsPerMeter * Y2);

		g.setColor(color);
		g.fillOval(pixelX1 - radius, pixelY1 - radius, radius * 2, radius * 2);
		g.setColor(Color.BLUE);
		g.fillOval(pixelX2 - radius, pixelY2 - radius, radius * 2, radius * 2);
	}
}