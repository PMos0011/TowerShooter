package pmos0011.biox;

public class BitmapID {

    public static int[] getStaticBitmapID() {

        int[] tmp_bitmapID = new int[9];
        tmp_bitmapID[0] = R.drawable.steppe;
        tmp_bitmapID[1] = R.drawable.base;
        tmp_bitmapID[2] = R.drawable.left_cannon;
        tmp_bitmapID[3] = R.drawable.right_cannon;
        tmp_bitmapID[4] = R.drawable.turret;
        tmp_bitmapID[5] = R.drawable.l_arrow;
        tmp_bitmapID[6] = R.drawable.r_arrow;
        tmp_bitmapID[7] = R.drawable.l_cannon_b;
        tmp_bitmapID[8] = R.drawable.r_cannon_b;

        return tmp_bitmapID;
    }

    public enum textureNames {

        BACKGROUND(0),
        TURRET_BASE(1),
        TURRET_L_CANNON(2),
        TURRET_R_CANNON(3),
        TURRET_TOWER(4),
        LEFT_ARROW(5),
        RIGHT_ARROW(6),
        LEFT_CANNON_BUTTON(7),
        RIGHT_CANNON_BUTTON(8);

        private final int value;

        textureNames(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

}
