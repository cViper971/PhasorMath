public class Phasor {
    public double rad;
    public double theta;

    public static final double pi = Math.PI;

    public static void main(String[] args) {
        Phasor i = p(20,210);
        int f = 5;

        Phasor z1 = add(parallel(l(4,f,0), r(20)),c(10,f,-3));
        Phasor temp1 = add(parallel(l(2,f,0), c(8,f,-3)),r(15));
        Phasor temp2 = add(l(8,f,0),r(40));
        Phasor z2 = add(temp1,temp2);

        System.out.println(idiv(z2,z1,i));
    }

    public static Phasor p(double rad, double theta) {
        return new Phasor(rad, theta);
    }

    public static Phasor r(double val) {
        return r(val,0);
    }

    public static Phasor r(double val, double exp) {
        return p(val * Math.pow(10,exp), 0);
    }

    public static Phasor c(double val, double freq, double exp) {
        return p(Math.pow(10,-exp) / (val * freq), -90);
    }

    public static Phasor l(double val, double freq, double exp) {
        return p(Math.pow(10,exp) * val * freq, 90);
    }

    public static Phasor p2(double a, double b) {
        double rad = Math.sqrt(a * a + b * b);
        double theta = Math.toDegrees(Math.atan2(b, a));
        return new Phasor(rad, theta);
    }

    public Phasor(double rad, double theta) {
        this.rad = rad;
        this.theta = theta;
    }

    public static Phasor add(Phasor p1, Phasor p2) {
        double x1 = p1.rad * Math.cos(Math.toRadians(p1.theta));
        double y1 = p1.rad * Math.sin(Math.toRadians(p1.theta));
        double x2 = p2.rad * Math.cos(Math.toRadians(p2.theta));
        double y2 = p2.rad * Math.sin(Math.toRadians(p2.theta));

        double x = x1 + x2;
        double y = y1 + y2;
        double r = Math.sqrt(x * x + y * y);
        double t = Math.toDegrees(Math.atan2(y, x));

        return new Phasor(r, t);
    }

    public static Phasor subtract(Phasor p1, Phasor p2) {
        double x1 = p1.rad * Math.cos(Math.toRadians(p1.theta));
        double y1 = p1.rad * Math.sin(Math.toRadians(p1.theta));
        double x2 = p2.rad * Math.cos(Math.toRadians(p2.theta));
        double y2 = p2.rad * Math.sin(Math.toRadians(p2.theta));

        double x = x1 - x2;
        double y = y1 - y2;
        double r = Math.sqrt(x * x + y * y);
        double t = Math.toDegrees(Math.atan2(y, x));

        return new Phasor(r, t);
    }

    public static Phasor multiply(Phasor p1, Phasor p2) {
        return new Phasor(p1.rad * p2.rad, p1.theta + p2.theta);
    }

    public static Phasor divide(Phasor p1, Phasor p2) {
        return new Phasor(p1.rad / p2.rad, p1.theta - p2.theta);
    }

    public Phasor inverse(){
        return divide(p(1,0),this);
    }
    public Phasor scale(int val){
        return p(rad*val,theta);
    }

    public Phasor negate(){
        return new Phasor(-rad,theta);
    }

    public static Phasor parallel(Phasor p1, Phasor p2) {
        return divide(new Phasor(1, 0), divide(add(p1, p2), multiply(p1, p2)));
    }

    public static Phasor series(Phasor p1, Phasor p2) {
        return add(p1, p2);
    }

    public static Phasor vdiv(Phasor z1, Phasor z2, Phasor v){
        return multiply(v, divide(z1, add(z1, z2)));
    }
    public static Phasor idiv(Phasor z1, Phasor z2, Phasor i){
        return multiply(i, divide(z2, add(z1, z2)));
    }

    public static Phasor[][] rref(Phasor[][] matrix) {
        int rows = matrix.length;
        int cols = matrix[0].length;
        int lead = 0;

        for (int r = 0; r < rows; r++) {
            if (lead >= cols) {
                return matrix;
            }
            int i = r;
            while (matrix[i][lead].rad == 0) {
                i++;
                if (i == rows) {
                    i = r;
                    lead++;
                    if (lead == cols) {
                        return matrix;
                    }
                }
            }
            Phasor[] temp = matrix[r];
            matrix[r] = matrix[i];
            matrix[i] = temp;

            Phasor lv = matrix[r][lead];
            for (int j = 0; j < cols; j++) {
                matrix[r][j] = divide(matrix[r][j], lv);
            }

            for (int i2 = 0; i2 < rows; i2++) {
                if (i2 != r) {
                    Phasor lv2 = matrix[i2][lead];
                    for (int j = 0; j < cols; j++) {
                        matrix[i2][j] = subtract(matrix[i2][j], multiply(lv2, matrix[r][j]));
                    }
                }
            }
            lead++;
        }
        return matrix;
    }

    @Override
    public String toString(){
        return Math.round(rad * 1000) / 1000.0 + " L " + Math.round(theta * 1000.0) / 1000.0;
    }

    public String toStringRect() {
        double a = rad * Math.cos(Math.toRadians(theta));
        double b = rad * Math.sin(Math.toRadians(theta));
        return Math.round(a * 1000) / 1000.0 + " + " + Math.round(b * 1000) / 1000.0 + "i";
    }
}
