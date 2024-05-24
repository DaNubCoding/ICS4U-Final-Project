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
     * The x value of the vector.
     */
    public final double x;
    /**
     * The y value of the vector.
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
        this(Math.cos(angle), Math.sin(angle));
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
        return divide(magnitude());
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
     * @param amount the amount to move by
     * @return the new vector after moving
     */
    public Vector2 moveTowards(Vector2 target, double amount) {
        Vector2 delta = target.subtract(this);
        double dist = delta.magnitude();
        if (dist <= amount) return target;
        return add(delta.scaleToMagnitude(amount));
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
        return String.format("Vector2(" + x + ", " + y + ")");
    }
}
