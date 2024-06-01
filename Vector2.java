/**
 * A 2D vector class.
 * <p>
 * This class represents a 2D vector with an x and y value. It supports basic
 * vector operations like addition, subtraction, multiplication, division,
 * normalization, and rotation.
 * <p>
 * This class is immutable, every operation will return a new Vector2.
 * <p>
 * Note that all angles are in degrees.
 *
 * @author Andrew Wang
 * @version May 2024
 */
public class Vector2 {
    /**
     * The x component of the vector.
     */
    public final double x;
    /**
     * The y component of the vector.
     */
    public final double y;

    /**
     * Create a new Vector2 with the given x and y values.
     *
     * @param x the x value
     * @param y the y value
     */
    public Vector2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Create a normalized Vector2 with the given angle.
     * <p>
     * The vector will have a magnitude of 1.
     *
     * @param angle the angle
     */
    public Vector2(double angle) {
        this(Math.cos(Math.toRadians(angle)), Math.sin(Math.toRadians(angle)));
    }

    /**
     * Create a new zero Vector2.
     * <p>
     * This is equivalent to calling {@code new Vector2(0, 0)}.
     */
    public Vector2() {
        this(0, 0);
    }

    /**
     * Set the x value of this vector and return the resulting vector.
     *
     * @param x the new x value
     * @return the new vector with the x value set
     */
    public Vector2 setX(double x) {
        return new Vector2(x, y);
    }

    /**
     * Set the y value of this vector and return the resulting vector.
     *
     * @param y the new y value
     * @return the new vector with the y value set
     */
    public Vector2 setY(double y) {
        return new Vector2(x, y);
    }

    /**
     * Add the given vector to this vector.
     *
     * @param other the vector to add
     * @return the sum of the two vectors
     */
    public Vector2 add(Vector2 other) {
        return new Vector2(x + other.x, y + other.y);
    }

    /**
     * Subtract the given vector from this vector.
     *
     * @param other the vector to subtract
     * @return the difference of the two vectors
     */
    public Vector2 subtract(Vector2 other) {
        return new Vector2(x - other.x, y - other.y);
    }

    /**
     * Multiply this vector by the given scalar.
     *
     * @param scalar the scalar to multiply by
     * @return the product of the vector and the scalar
     */
    public Vector2 multiply(double scalar) {
        return new Vector2(x * scalar, y * scalar);
    }

    /**
     * Divide this vector by the given scalar.
     *
     * @param scalar the scalar to divide by
     * @return the quotient of the vector and the scalar
     */
    public Vector2 divide(double scalar) {
        return new Vector2(x / scalar, y / scalar);
    }

    /**
     * Get the magnitude of this vector.
     *
     * @return the magnitude of the vector
     */
    public double magnitude() {
        return Math.sqrt(x * x + y * y);
    }

    /**
     * Normalize this vector.
     * <p>
     * This will return a new vector with the same direction as this vector, but
     * with a magnitude of 1.
     *
     * @return the normalized vector
     * @throws ArithmeticException if the magnitude of the vector is 0
     */
    public Vector2 normalize() {
        double mag = magnitude();
        if (mag == 0) {
            throw new ArithmeticException("Cannot normalize a zero vector");
        }
        return divide(mag);
    }

    /**
     * Scale this vector to the given magnitude.
     * <p>
     * This will return a new vector with the same direction as this vector, but
     * with the given magnitude.
     * <p>
     * This is equivalent to calling {@code normalize().multiply(magnitude)}.
     *
     * @param magnitude the magnitude to scale to
     * @return the scaled vector
     * @throws ArithmeticException if the magnitude of the vector is 0
     */
    public Vector2 scaleToMagnitude(double magnitude) {
        return normalize().multiply(magnitude);
    }

    /**
     * Get the dot product of this vector and the given vector.
     *
     * @param other the other vector
     * @return the dot product of the two vectors
     */
    public double dot(Vector2 other) {
        return x * other.x + y * other.y;
    }

    /**
     * Get the distance between the tip of this vector and the tip of the given
     * vector.
     *
     * @param other the other vector
     * @return the distance between the two vectors
     */
    public double distanceTo(Vector2 other) {
        return subtract(other).magnitude();
    }

    /**
     * Get the distance between the tip of this vector and the given point.
     *
     * @param x the x coordinate of the point
     * @param y the y coordinate of the point
     * @return the distance between the vector and the point
     */
    public double distanceTo(double x, double y) {
        return distanceTo(new Vector2(x, y));
    }

    /**
     * Rotate this vector by the given angle.
     *
     * @param angle the angle to rotate by
     * @return the rotated vector
     */
    public Vector2 rotate(double angle) {
        double rad = Math.toRadians(angle);
        double cos = Math.cos(rad);
        double sin = Math.sin(rad);
        return new Vector2(x * cos - y * sin, x * sin + y * cos);
    }

    /**
     * Get the angle of this vector.
     *
     * @return the angle of the vector
     */
    public double angle() {
        return Math.toDegrees(Math.atan2(y, x));
    }

    /**
     * Move this vector towards the given target vector by the given amount.
     *
     * @param target the target vector
     * @param maxDistanceDelta the amount to move by
     * @return the new vector after moving
     */
    public Vector2 moveTowards(Vector2 target, double maxDistanceDelta) {
        Vector2 delta = target.subtract(this);
        double dist = delta.magnitude();
        if (dist <= maxDistanceDelta) return target;
        return add(delta.scaleToMagnitude(maxDistanceDelta));
    }

    /**
     * Linearly interpolate between this vector and a target vector by the given
     * alpha value.
     * <p>
     * The alpha value should be between 0 and 1. Representing how far between
     * this vector and the target vector the resulting vector should be.
     *
     * @param target the target vector
     * @param alpha the alpha value
     * @return the interpolated vector
     */
    public Vector2 lerp(Vector2 target, double alpha) {
        alpha = Math.max(0, Math.min(1, alpha));
        return add(target.subtract(this).multiply(alpha));
    }

    /**
     * Clamp the magnitude of this vector to the given value.
     *
     * @param maxMagnitude the maximum magnitude
     * @return the clamped vector
     */
    public Vector2 clampMagnitude(double maxMagnitude) {
        return clampMagnitude(0, maxMagnitude);
    }

    /**
     * Clamp the magnitude of this vector to the given range.
     *
     * @param minMagnitude the minimum magnitude
     * @param maxMagnitude the maximum magnitude
     * @return the clamped vector
     */
    public Vector2 clampMagnitude(double minMagnitude, double maxMagnitude) {
        double magnitude = magnitude();
        if (magnitude < minMagnitude) return scaleToMagnitude(minMagnitude);
        if (magnitude > maxMagnitude) return scaleToMagnitude(maxMagnitude);
        return this;
    }

    /**
     * Check if this vector is equal to the given object.
     * <p>
     * This will return true if the object is a Vector2 and has the same x and y
     * values as this vector.
     *
     * @param obj the object to compare to
     * @return true if the object is equal to this vector, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        try {
            Vector2 other = (Vector2) obj;
            return x == other.x && y == other.y;
        } catch (ClassCastException e) {
            return false;
        }
    }

    /**
     * Get the hash code of this vector.
     * <p>
     * Note that this is not guaranteed to be unique.
     *
     * @return the hash code of the vector
     */
    @Override
    public int hashCode() {
        return Double.hashCode(x) * Double.hashCode(y);
    }

    /**
     * Get a string representation of this vector.
     *
     * @return a string representation of the vector
     */
    @Override
    public String toString() {
        return "Vector2(" + x + ", " + y + ")";
    }

    /**
     * Get the encoded value of the integer coordinates after passing through
     * the Szudzik pairing function, modified to support negative values.
     * <p>
     * Note: the coordinates must not contain decimals.
     * <p>
     * Credits go to nawfal at https://stackoverflow.com/questions/919612/mapping-two-integers-to-one-in-a-unique-and-deterministic-way
     * @return the encoded value after passing through the Szudzik pairing function
     * @throws UnsupportedOperationException if the values of this vector2 contain decimals
     */
    public long getSzudzikValue() {
        if(x!=(long)x||y!=(long)y)
            throw new UnsupportedOperationException("This method does not support decimals.");

        long X = (long)(x >= 0 ? 2 * (long)x : -2 * (long)x - 1);
        long Y = (long)(y >= 0 ? 2 * (long)y : -2 * (long)y - 1);
        long C = (long)((X >= Y ? X * X + X + Y : X + Y * Y) / 2);
        return x < 0 && y < 0 || x >= 0 && y >= 0 ? C : -C - 1;
    }

    /**
     * Normalize an angle in degrees to the range [0.0, 360.0).
     *
     * @param angle the angle to normalize, in degrees
     * @return an equivalent angle in degrees within the range [0.0, 360.0)
     */
    public static double normalizeAngle(double angle) {
        angle %= 360.0;
        if (angle < 0.0) {
            angle += 360.0;
        }
        // The above addition may result in a value of 360.0 due to floating
        // point error, take the remainder again to prevent returning 360.0
        angle %= 360.0;
        return angle;
    }

    /**
     * Move a current angle towards an end angle by the given amount via the
     * shortest path.
     *
     * @param current the current angle, in degrees
     * @param target the target angle, in degrees
     * @param maxDelta the maximum amount to move by
     * @return the new angle after moving
     */
    public static double moveTowardsAngle(double current, double target, double maxDelta) {
        double difference = Math.floorMod((int) (target - current + 180), 360) - 180;
        return current + Math.copySign(Math.min(maxDelta, Math.abs(difference)), difference);
    }

    /**
     * Linearly interpolate between two angles by the given alpha value via the
     * shortest path.
     * <p>
     * The alpha value should be between 0 and 1. Representing how far between
     * the current angle and the target angle the resulting angle should be.
     *
     * @param current the current angle, in degrees
     * @param target the target angle, in degrees
     * @param alpha the alpha value
     * @return the interpolated angle
     */
    public static double lerpAngle(double current, double target, double alpha) {
        double difference = Math.floorMod((int) (target - current + 180), 360) - 180;
        return current + difference * alpha;
    }
}
