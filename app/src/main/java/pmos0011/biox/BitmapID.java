package pmos0011.biox;

public class BitmapID {

    public static int[] getStaticBitmapID() {

        int[] tmp_bitmapID = new int[14];
        tmp_bitmapID[0] = R.drawable.background;
        tmp_bitmapID[1] = R.drawable.base;
        tmp_bitmapID[2] = R.drawable.left_cannon;
        tmp_bitmapID[3] = R.drawable.right_cannon;
        tmp_bitmapID[4] = R.drawable.turret;
        tmp_bitmapID[5] = R.drawable.l_arrow;
        tmp_bitmapID[6] = R.drawable.r_arrow;
        tmp_bitmapID[7] = R.drawable.l_cannon_b;
        tmp_bitmapID[8] = R.drawable.r_cannon_b;
        tmp_bitmapID[9] = R.drawable.radar;
        tmp_bitmapID[10] = R.drawable.shell;
        tmp_bitmapID[11] = R.drawable.calibri;
        tmp_bitmapID[12] = R.drawable.tank_chassis;
        tmp_bitmapID[13] = R.drawable.tank_turret;

        return tmp_bitmapID;
    }

    public enum textureNames {

        BACKGROUND(0),
        TOWER_BASE(1),
        TOWER_LEFT_CANNON(2),
        TOWER_RIGHT_CANNON(3),
        TURRET(4),
        LEFT_ARROW(5),
        RIGHT_ARROW(6),
        LEFT_CANNON_BUTTON(7),
        RIGHT_CANNON_BUTTON(8),
        RADAR(9),
        SHELL(10),
        FONT_MAP(11),
        TANK_CHASSIS(12),
        TANK_TURRET(13);

        private final int value;

        textureNames(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

}
