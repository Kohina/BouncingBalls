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

		X2 = 1; // in meters
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
		
		if(Math.abs(Math.sqrt(Math.pow(pixelX1-pixelX2, 2)+Math.pow(pixelY1-pixelY2, 2))) < 2*radius) {
			int dX = pixelX1 - pixelX2; //Avstånd mellan bollarna i x-led
			int dY = pixelY1 - pixelY2; //Avstånd mellan bollarna i y-led
			double a = Math.atan((double)dY/dX);  //vinkeln som bollarna träffas i
			
			double V1 = Math.sqrt(Math.pow(Vx1, 2)+Math.pow(Vy1, 2));
			double aV1 = Math.atan(Vy1/Vx1);
			double V2 = Math.sqrt(Math.pow(Vx2, 2)+Math.pow(Vy2, 2));
			double aV2 = Math.atan(Vy2/Vx2);
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
			
			Vx1 = V1*Math.cos(a+b1);
			Vy1 = V1*Math.sin(a+b1);
			Vx2 = V2*Math.cos(a+b2);
			Vy2 = V2*Math.sin(a+b2);
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
		
		if (pixelX1 > d.width-radius) {
			pixelX1 = d.width-radius;
			Vx1 = -Vx1;
		}
		if (pixelX1 < radius) {
			pixelX1 = 0+radius;
			Vx1 = -Vx1;
		}
		if(pixelY1 > d.height-radius){
			pixelY1 = d.height-radius;
			Vy1 = -Vy1;
		}
		if (pixelY1 < radius) {
			pixelY1 = 0+radius;
			Vy1 = -Vy1;
		}
		
		if(pixelX2 > d.width-radius){
			pixelX2 = d.width-radius;
			Vx2 = -Vx2;
		}
		if (pixelX2 < radius) {
			pixelX2 = 0+radius;
			Vx2 = -Vx2;
		}
		if(pixelY2 > d.height-radius){
			pixelY2 = d.height-radius;
			Vy2 = -Vy2;
		}
		if (pixelY2 < radius) {
			pixelY2 = 0+radius;
			Vy2 = -Vy2;
		}
		
		g.setColor(color);
		g.fillOval(pixelX1 - radius, pixelY1 - radius, radius * 2, radius * 2);
		g.setColor(Color.BLUE);
		g.fillOval(pixelX2 - radius, pixelY2 - radius, radius * 2, radius * 2);
	}
}