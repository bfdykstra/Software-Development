package falstad;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

public class GraphicsWrapper extends MazePanel {
	
	Graphics gc ;

	public GraphicsWrapper() {
		
	}
	
	public enum gwColor {black, darkGray, white, red, blue, yellow, orange, gray}
	
	public void setGraphics(){
		
		gc = getBufferGraphics();
	}
	
	static float[] getColorValuesWithInt(int col){
		Color c = new Color(col);
		
		float[] array = {c.getRed(), c.getGreen(), c.getBlue()};
		return array;
	}
	
	public static int getColorValues(float[] color){
		Color c = new Color(color[0], color[1], color[2]);
		//use float array on a 
		return c.getRGB();
	}
	
	public void setColorWithArray(float[] col){
		Color c = new Color((int) col[0],(int) col[1], (int) col[2]);
		gc.setColor(c);
	}
	public void setColor(gwColor c){
		Color actualColor = null;
		if(c == gwColor.black){
			actualColor = Color.black;
		} else if( c == gwColor.darkGray){
			actualColor = Color.darkGray;
		} else if(c == gwColor.white){
			actualColor = Color.white;
		} else if(c == gwColor.red){
			actualColor = Color.red;
		} else if(c == gwColor.blue){
			actualColor = Color.blue;
		} else if(c == gwColor.yellow){
			actualColor = Color.yellow;
		} else if(c == gwColor.orange){
			actualColor = Color.orange;
		} else if(c == gwColor.gray){
			actualColor = Color.gray;
		}
		
		if( actualColor != null){
			gc.setColor(actualColor);
		}
		
	}
	
	public void fillRect(int x, int y, int width, int height){
		gc.fillRect(x, y, width, height);
		
	}
	
	public void fillPolygon(int[] xPoints, int[] yPoints, int nPoints){
		gc.fillPolygon(xPoints, yPoints, nPoints);
	}

	public void drawLine(int nx1, int ny1, int nx2, int ny12) {
		gc.drawLine(nx1, ny1, nx2, ny12);
		
	}

	public void fillOval(int i, int j, int cirsiz, int cirsiz2) {
		gc.fillOval(i, j, cirsiz, cirsiz2);
		
	}

	public FontMetrics getFontMetrics() {
		return gc.getFontMetrics();
	}

	public void drawString(String str, int i, int ypos) {
		gc.drawString(str, i, ypos);
		
	}
	
	public void centerString(GraphicsWrapper g, String str, int ypos, Font f) {
		gc.setFont(f);
		FontMetrics fm = g.getFontMetrics();
		g.drawString(str, (Constants.VIEW_WIDTH-fm.stringWidth(str))/2, ypos);
	}
	
	public static Font smallBannerFont = new Font("TimesRoman", Font.BOLD, 16);
	public static Font largeBannerFont = new Font("TimesRoman", Font.BOLD, 48);

}
