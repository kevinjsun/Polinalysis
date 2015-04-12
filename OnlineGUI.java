import static com.googlecode.charts4j.Color.WHITE;

import java.awt.Font;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.Calendar;

import javax.imageio.ImageIO;







import com.googlecode.charts4j.AxisLabels;
import com.googlecode.charts4j.AxisLabelsFactory;
import com.googlecode.charts4j.AxisStyle;
import com.googlecode.charts4j.AxisTextAlignment;
import com.googlecode.charts4j.Color;
import com.googlecode.charts4j.Data;
import com.googlecode.charts4j.Fills;
import com.googlecode.charts4j.GCharts;
import com.googlecode.charts4j.LineStyle;
import com.googlecode.charts4j.LinearGradientFill;
import com.googlecode.charts4j.Plots;
import com.googlecode.charts4j.Shape;
import com.googlecode.charts4j.XYLine;
import com.googlecode.charts4j.XYLineChart;

public class OnlineGUI {
    private static boolean tab;
    private static String text;
    private static long[] mseconds;
    private static double[] happiness;
    private static double[] liberal;
    private static double[] green;
    private static double[] conservative;
    private static double[] libertarian;
    private static String[] tweets;
    private static boolean computing = false;
    private static boolean searched = false;
    private static long firstDate;
    private static long lastDate;
    private static NearestNeighborST<String> points;
    private static boolean error;

    private static boolean hasGraph = false;

    
    
    private static class Animation extends Thread {
        public boolean running;
        public void run() {
            double dt = 25000.0;
            double[] px = new double[3];
            double[] py = new double[3];
            double[] vx = new double[3];
            double[] vy = new double[3];
            double[] ax = new double[3];
            double[] ay = new double[3];
            double[] fx = new double[3];
            double[] fy = new double[3];        
            double[] m = new double[3];
            double scale = 1.25e11;
            px[0] = 0;
            py[0] = 0;
            vx[0] = 0.0500e04; 
            vy[0] = 0.000e00 ;
            m[0] = 5.974e24; 
            px[1] = 0.000e00;
            py[1] = 4.500e10 ;
            vx[1] = 3.000e04  ;
            vy[1] = 0.000e00   ;
            m[1] =  1.989e30 ;
            px[2] = 0.000e00;
            py[2] = -4.500e10 ;
            vx[2] = -3.000e04  ;
            vy[2] = 0.000e00   ;
            m[2] =  1.989e30 ;
        
        while (running) {
            DrawGUI.clear();
            DrawGUI.setPenColor(DrawGUI.BLACK);
            DrawGUI.filledRectangle(480, 360, 480, 360);
            
            if (!tab) {
                DrawGUI.picture(480, 270, "bluebigrectangle.png");
                DrawGUI.picture(105, 560, "psentbold.png");
                DrawGUI.picture(315, 560, "positivity.png");
                DrawGUI.picture(830, 560, "bbox.png");
                DrawGUI.picture(663, 560, "bluesearch.png");
            }
            else {
                DrawGUI.picture(480, 270, "biggreenrectangle.png");
                DrawGUI.picture(105, 560, "psent.png");
                DrawGUI.picture(315, 560, "positivitybold.png");
                DrawGUI.picture(830, 560, "gbox.png");
                DrawGUI.picture(663, 560, "greensearch.png");
            }
            DrawGUI.picture(480, 270, "loading.png");
          DrawGUI.setPenColor(DrawGUI.LIGHT_GRAY);
            DrawGUI.filledCircle(px[0] / scale * 100 + 480, py[0] / scale * 100 + 200, 10);
            DrawGUI.setPenColor(DrawGUI.WHITE);
            DrawGUI.filledCircle(px[1] / scale * 100 + 480, py[1] / scale * 100 + 200, 10);

            DrawGUI.filledCircle(px[2] / scale * 100 + 480, py[2] / scale * 100 + 200, 10);
            
            DrawGUI.show(10);
            
            //calculating the forces                      
            double G = 6.67*1e-11;
            for (int i = 0; i < 3; i++) {
                fx[i] = 0;
                fy[i] = 0;
                for (int j = 0; j < 3; j++) {
                    if (i != j) { 
                        //this is to avoid force of the planet on itself
                        double force; //force is gravity multiplied by 1/r here
                        force = (-G*m[i]*m[j])
                            /Math.pow(d(px[i], py[i], px[j], py[j]), 1.5);
                        fy[i] = fy[i] + (force * (py[i] - py[j]));
                        fx[i] = fx[i] + (force * (px[i] - px[j]));
                    }
                }
            }
            
            // updating the values of the accleration, velocity and position
            for (int i = 0; i < 3; i++) {
                ax[i] = fx[i]/m[i];
                ay[i] = fy[i]/m[i];
                vx[i] = vx[i] + ax[i]*dt;
                vy[i] = vy[i] + ay[i]*dt;
                px[i] = px[i] + vx[i]*dt;
                py[i] = py[i] + vy[i]*dt;
                
            }
        }
    }
        public void kill() {
            running = false;
        }
    }
    private static double d(double px1, double py1, double px2, double py2) {
        double distance = (px1-px2)*(px1-px2)+ (py1-py2)*(py1-py2);
        return distance;
    }
    public static void main(String[] args) {
        error = false;
        DrawGUI.setCanvasSize(960, 580);
        DrawGUI.setPenColor(DrawGUI.BLACK);
        DrawGUI.setXscale(0,960);
        DrawGUI.setYscale(0, 580);
        DrawGUI.filledRectangle(480, 360, 480, 360);
        setFalse();
        DrawGUI.show(10);
        DrawGUI.setPenColor(DrawGUI.WHITE);
        while (true) {
            
            // the location (x, y) of the mouse
            double x = DrawGUI.mouseX();
            double y = DrawGUI.mouseY();
            
            
            
            DrawGUI.setPenColor(DrawGUI.BLACK);
            DrawGUI.filledRectangle(870, 455, 50, 30);

            DrawGUI.setPenColor(DrawGUI.WHITE);
            DrawGUI.text(870, 470, ""+x);
            DrawGUI.text(870, 440, "" + y);
            if (tab) {
                setTrue();
            }
            else {
                setFalse();
            }
            if (error) {
                DrawGUI.picture(480, 270, "blackerror.png");
            }
            if (hasGraph) {
                
                Point2D clo = points.nearest(new Point2D(x, y));
                if (points.distance < 400) {
                    DrawGUI.setPenColor(DrawGUI.WHITE);
                    DrawGUI.filledCircle(clo.x(), clo.y(), 2);
                    DrawGUI.setPenColor(DrawGUI.BLACK);
                    DrawGUI.filledRectangle(480, 10, 480, 10);
                    DrawGUI.setPenColor(DrawGUI.WHITE);
                    DrawGUI.setFont(new Font("Helvetica", Font.PLAIN, 12));
                    DrawGUI.text(480, 10, points.get(clo));
                }
                
            }
            DrawGUI.show(10);
            
            if (DrawGUI.entered && !computing) {
                error = false;
                hasGraph = false;
                Animation a = new Animation();
                a.start();
                a.running = true;
                DrawGUI.entered = false;
                searched = true;
                text = DrawGUI.text;
                try {
            
                
                
                Info  inf = GetTweetInfo.getStats(text, 100);
                mseconds = inf.time; 
                firstDate = mseconds[mseconds.length - 1];
                lastDate = mseconds[0];
                
                happiness = inf.mood;
                liberal = inf.liberal;
                green = inf.green;
                conservative = inf.conservative;
                libertarian = inf.libertarian;
                tweets = inf.tweets;
                for (int i = 0; i < happiness.length; i++) {
                    happiness[i] *= 100;
                    liberal[i] *= 100;
                    green[i] *= 100;
                    conservative[i] *= 100;
                    libertarian[i] *= 100;
                }
                }
                catch (Exception e) {
                    a.kill();
                    searched = false;
                    error = true;
                }
                if (searched) {
                    if (tab) {
                        a.kill();
                        setTrue();
                        DrawGUI.show(10);
                    }
                    else 
                        a.kill();
                        setFalse();
                        DrawGUI.show(10);
                }
                a.kill();
                computing = false;
                
            }
            
            
            if (DrawGUI.mousePressed()) {
                error = false;
                if (540 < y && 580 > y && x > 0 && x < 210) {
                    if (tab) {
                    setFalse();
                    }
                }
                if (540 < y && 580 > y && x > 210 && x < 420) {
                    if (!tab) {
                    setTrue();
                    }
                }
                DrawGUI.show(10);
            }
            
        }
        
    }
    private static void setFalse() {
        tab = false;
        DrawGUI.picture(480, 270, "bluebigrectangle.png");
        DrawGUI.picture(105, 560, "psentbold.png");
        DrawGUI.picture(315, 560, "positivity.png");
        DrawGUI.picture(830, 560, "bbox.png");
        DrawGUI.picture(663, 560, "bluesearch.png");
        drawGraph1();
    }
    private static void setTrue() {
        tab = true;
    DrawGUI.picture(480, 270, "biggreenrectangle.png");
    DrawGUI.picture(105, 560, "psent.png");
    DrawGUI.picture(315, 560, "positivitybold.png");
    DrawGUI.picture(830, 560, "gbox.png");
    DrawGUI.picture(663, 560, "greensearch.png");
    drawGraph2();
        
    }
    
    private static void drawGraph1() {
        if (!searched) {
            DrawGUI.picture(480, 270, "blacksquare.png");
            return;
        }
        // Defining lines
        
        double[] xdat = new double[mseconds.length];
//        final double[] ydat1 = liberal;
//        final double[] ydat2 = green;
        final double[] ydat3 = conservative;
        final double[] ydat4 = libertarian;
        
        long distance = lastDate - firstDate;
        for (int i = 0; i < mseconds.length; i++) {
            xdat[i] = (double)(mseconds[i] - firstDate) / distance * 100;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(firstDate);
        int m1 = cal.get(Calendar.MONTH) + 1;
        int y1 = cal.get(Calendar.YEAR);
        cal.setTimeInMillis(firstDate + distance / 4);
        int m2 = cal.get(Calendar.MONTH) + 1;
        int y2 = cal.get(Calendar.YEAR);
        cal.setTimeInMillis(firstDate + distance / 2);
        int m3 = cal.get(Calendar.MONTH) + 1;
        int y3 = cal.get(Calendar.YEAR);
        cal.setTimeInMillis(firstDate + 3 * distance / 4);
        int m4 = cal.get(Calendar.MONTH) + 1;
        int y4 = cal.get(Calendar.YEAR);
        cal.setTimeInMillis(lastDate);
        int m5 = cal.get(Calendar.MONTH) + 1;
        int y5 = cal.get(Calendar.YEAR);
        
//        XYLine line1 = Plots.newXYLine(Data.newData(xdat), Data.newData(ydat1), Color.BLUE, "Liberalism");
//        XYLine line2 = Plots.newXYLine(Data.newData(xdat), Data.newData(ydat2), Color.GREEN, "Green");
        XYLine line3 = Plots.newXYLine(Data.newData(xdat), Data.newData(ydat3), Color.RED, "Conservatism");
        XYLine line4 = Plots.newXYLine(Data.newData(xdat), Data.newData(ydat4), Color.PURPLE, "Libertarianism");
        
//        line1.setLineStyle(LineStyle.newLineStyle(3, 1, 0));
//        line1.addShapeMarkers(Shape.DIAMOND, Color.BLUE, 12);
//        line1.addShapeMarkers(Shape.DIAMOND, Color.WHITE, 8);
        
//        line2.setLineStyle(LineStyle.newLineStyle(3, 1, 0));
//        line2.addShapeMarkers(Shape.DIAMOND, Color.GREEN, 12);
//        line2.addShapeMarkers(Shape.DIAMOND, Color.WHITE, 8);
        
        line3.setLineStyle(LineStyle.newLineStyle(3, 1, 0));
//        line3.addShapeMarkers(Shape.DIAMOND, Color.RED, 12);
//        line3.addShapeMarkers(Shape.DIAMOND, Color.WHITE, 8);
        
        line4.setLineStyle(LineStyle.newLineStyle(3, 1, 0));
//        line4.addShapeMarkers(Shape.DIAMOND, Color.PURPLE, 12);
//        line4.addShapeMarkers(Shape.DIAMOND, Color.WHITE, 8);
        


        // Defining chart.
        XYLineChart chart = GCharts.newXYLineChart(/*line1, line2, */line3, line4);
        chart.setSize(726, 412);
        chart.setTitle("Political Sentiment over Time: " + text, WHITE, 14);
        chart.setGrid(25, 20, 30, 2);

        // Defining axis info and styles
        AxisStyle axisStyle = AxisStyle.newAxisStyle(WHITE, 12, AxisTextAlignment.CENTER);
        AxisLabels xAxis = AxisLabelsFactory.newAxisLabels(m1 + "/" + y1, m2 + "/" + y2, m3 + "/" + y3
                , m4 + "/" + y4, m5 + "/" + y5);
        xAxis.setAxisStyle(axisStyle);
        
        AxisLabels yAxis = AxisLabelsFactory.newAxisLabels(""
                , "Less", "", "", "More", "");
        AxisLabels xAxis3 = AxisLabelsFactory.newAxisLabels("Month", 50.0);
        xAxis3.setAxisStyle(AxisStyle.newAxisStyle(WHITE, 14, AxisTextAlignment.CENTER));
        yAxis.setAxisStyle(axisStyle);

        // Adding axis info to chart.
        chart.addXAxisLabels(xAxis);
        chart.addXAxisLabels(xAxis3);
        chart.addYAxisLabels(yAxis);

        // Defining background and chart fills.
        chart.setBackgroundFill(Fills.newSolidFill(Color.newColor("1F1D1D")));
        LinearGradientFill fill = Fills.newLinearGradientFill(0, Color.newColor("363433"), 100);
        fill.addColorAndOffset(Color.newColor("2E2B2A"), 0);
        chart.setAreaFill(fill);
        BufferedImage img = null;
        try {
        img = ImageIO.read(new URL(chart.toURLString()));
        }
        catch(Exception e) {
        }
        
        points = new NearestNeighborST<String>();
        for (int i = 0; i < mseconds.length; i++) {
            Point2D p = new Point2D(xdat[i] * 7.37 + 71, ydat3[i] * 4.17 + 70);
            Point2D p2 = new Point2D(xdat[i] * 7.37 + 71, ydat4[i] * 4.17 + 70);
            points.put(p, tweets[i]);
            points.put(p2, tweets[i]);
        }
       
        DrawGUI.picture(480, 270, img, 900,510);
        hasGraph = true;
        
    }

    private static void drawGraph2() {
        if (!searched) {
            DrawGUI.picture(480, 270, "blacksquare.png");
            return;
        }
        // Defining lines
        double[] xdat = new double[mseconds.length];
        double[] ydat = happiness;
        double[] ydat2 = new double[mseconds.length];
        long distance = lastDate - firstDate;
        for (int i = 0; i < mseconds.length; i++) {
            xdat[i] = (double) (mseconds[i] - firstDate) / distance * 100;
        }
        double sum = 0;
        for (int i = happiness.length -1; i >= 0 ; i--) {
            sum += ydat[i];
            ydat2[i] = sum / (happiness.length - i);
        }
        double[] ydat3 = new double[mseconds.length];
        sum = 0;
        sum += ydat[happiness.length - 1];
        ydat3[happiness.length - 1] = sum;
        sum += ydat[happiness.length - 2];
        ydat3[happiness.length - 2] = sum / 2;
        for (int i = happiness.length - 3; i >= 0; i--) {
            sum += ydat[i];
            ydat3[i] = sum / 3;
            sum -= ydat[i + 2];
        }
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(firstDate);
        int m1 = cal.get(Calendar.MONTH) + 1;
        int y1 = cal.get(Calendar.YEAR);
        cal.setTimeInMillis(firstDate + distance / 4);
        int m2 = cal.get(Calendar.MONTH) + 1;
        int y2 = cal.get(Calendar.YEAR);
        cal.setTimeInMillis(firstDate + distance / 2);
        int m3 = cal.get(Calendar.MONTH) + 1;
        int y3 = cal.get(Calendar.YEAR);
        cal.setTimeInMillis(firstDate + 3 * distance / 4);
        int m4 = cal.get(Calendar.MONTH) + 1;
        int y4 = cal.get(Calendar.YEAR);
        cal.setTimeInMillis(lastDate);
        int m5 = cal.get(Calendar.MONTH) + 1;
        int y5 = cal.get(Calendar.YEAR);
        
        
       
        XYLine line1 = Plots.newXYLine(Data.newData(xdat), Data.newData(ydat), Color.GREEN, "Positivity");
        XYLine line2 = Plots.newXYLine(Data.newData(xdat), Data.newData(ydat2), Color.WHITE, "Average");

        XYLine line3 = Plots.newXYLine(Data.newData(xdat), Data.newData(ydat3), Color.RED, "Vicinity Average");
        
        
        line1.setLineStyle(LineStyle.newLineStyle(3, 1, 0));
//        line1.addShapeMarkers(Shape.DIAMOND, Color.GREEN, 12);
//        line1.addShapeMarkers(Shape.DIAMOND, Color.WHITE, 8);
//        
        line2.setLineStyle(LineStyle.newLineStyle(3, 1, 0));
        line3.setLineStyle(LineStyle.newLineStyle(3, 1, 0));
        


        // Defining chart.
        XYLineChart chart = GCharts.newXYLineChart(line1, line2, line3);
        chart.setSize(726, 412);
        chart.setTitle("Positivity over Time: " + text, WHITE, 14);
        chart.setGrid(25, 20, 30, 2);

        // Defining axis info and styles
        AxisStyle axisStyle = AxisStyle.newAxisStyle(WHITE, 12, AxisTextAlignment.CENTER);
        AxisLabels xAxis = AxisLabelsFactory.newAxisLabels(m1 + "/" + y1, m2 + "/" + y2, m3 + "/" + y3
                , m4 + "/" + y4, m5 + "/" + y5);
        xAxis.setAxisStyle(axisStyle);
        AxisLabels yAxis = AxisLabelsFactory.newAxisLabels("", "0.20", "0.40", "0.60", "0.80", "1.00");
        AxisLabels xAxis3 = AxisLabelsFactory.newAxisLabels("Month", 50.0);
        xAxis3.setAxisStyle(AxisStyle.newAxisStyle(WHITE, 14, AxisTextAlignment.CENTER));
        yAxis.setAxisStyle(axisStyle);
        AxisLabels yAxis2 = AxisLabelsFactory.newAxisLabels("Positivity", 50.0);
        yAxis2.setAxisStyle(AxisStyle.newAxisStyle(WHITE, 14, AxisTextAlignment.CENTER));
        yAxis2.setAxisStyle(axisStyle);

        // Adding axis info to chart.
        chart.addXAxisLabels(xAxis);
        chart.addXAxisLabels(xAxis3);
        chart.addYAxisLabels(yAxis);
        chart.addYAxisLabels(yAxis2);

        // Defining background and chart fills.
        chart.setBackgroundFill(Fills.newSolidFill(Color.newColor("1F1D1D")));
        LinearGradientFill fill = Fills.newLinearGradientFill(0, Color.newColor("363433"), 100);
        fill.addColorAndOffset(Color.newColor("2E2B2A"), 0);
        chart.setAreaFill(fill);
        BufferedImage img = null;
        try {
        img = ImageIO.read(new URL(chart.toURLString()));
        }
        catch(Exception e) {
        }
        hasGraph = true;
        DrawGUI.picture(480, 270, img, 900,510);
        points = new NearestNeighborST<String>();
        for (int i = 0; i < mseconds.length; i++) {
            Point2D p = new Point2D(xdat[i] * 6.65 + 130, ydat[i] * 4.17 + 70);

            points.put(p, tweets[i]);

        }

        
    }
}