package pmos0011.biox;

import android.graphics.PointF;

public class Calculations {

    public static PointF calculatePoint(float angle, float param) {

        PointF point = new PointF();

        double radians = Math.toRadians(angle);
        point.x = param * (float) Math.sin(radians);
        point.y = param * (float) Math.cos(radians);
        point.x = -point.x;

        return point;
    }

}
