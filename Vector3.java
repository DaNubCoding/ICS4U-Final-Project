/**
 * A 3D vector class.
 * <p>
 * This class represents a 3D vector with x, y, and z components. Y is the
 * vertical axis. It supports basic vector operations like addition,
 * subtraction, multiplication, division, and normalization.
 * <p>
 * This class is immutable, every operation returns a new vector.
 * <p>
 * Note that all angles are in degrees.
 *
 * @author Andrew Wang
 * @version May 2024
 */
public class Vector3 {
    /**
     * Create a new Vector3 with the given x and y components as a
     * {@link Vector2}.
     *
     * @param xy the x and y components as a Vector2
     * @return the new Vector3
     */
    public static Vector3 fromXY(Vector2 xy) {
        return new Vector3(xy.x, xy.y, 0);
    }

    /**
     * Create a new Vector3 with the given y and z components as a
     * {@link Vector2}.
     *
     * @param yz the y and z components as a Vector2
     * @return the new Vector3 `
     */
    public static Vector3 fromYZ(Vector2 yz) {
        return new Vector3(0, yz.x, yz.y);
    }

    /**
     * Create a new Vector3 with the given x and z components as a
     * {@link Vector2}.
     *
     * @param xz the x and z components as a Vector2
     * @return the new Vector3
     */
    public static Vector3 fromXZ(Vector2 xz) {
        return new Vector3(xz.x, 0, xz.y);
    }

    /**
     * The x component of the vector.
     */
    public final double x;
    /**
     * The y component of the vector.
     */
    public final double y;
    /**
     * The z component of the vector.
     */
    public final double z;

    /**
     * A 2D vector representing the x and y components of this vector.
     */
    public final Vector2 xy;
    /**
     * A 2D vector representing the y and z components of this vector.
     */
    public final Vector2 yz;
    /**
     * A 2D vector representing the x and z components of this vector.
     */
    public final Vector2 xz;

    /**
     * Create a new Vector3 with the given components.
     *
     * @param x the x component
     * @param y the y component
     * @param z the z component
     */
    public Vector3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;

        xy = new Vector2(x, y);
        yz = new Vector2(y, z);
        xz = new Vector2(x, z);
    }

    /**
     * Create a new zero Vector3.
     * <p>
     * This is equivalent to calling {@code new Vector3(0, 0, 0)}.
     */
    public Vector3() {
        this(0, 0, 0);
    }

    /**
     * Set the x value of this vector and return the resulting vector.
     *
     * @param x the new x value
     * @return the new vector with the x value set
     */
    public Vector3 setX(double x) {
        return new Vector3(x, y, z);
    }

    /**
     * Set the y value of this vector and return the resulting vector.
     *
     * @param y the new y value
     * @return the new vector with the y value set
     */
    public Vector3 setY(double y) {
        return new Vector3(x, y, z);
    }

    /**
     * Set the z value of this vector and return the resulting vector.
     *
     * @param z the new z value
     * @return the new vector with the z value set
     */
    public Vector3 setZ(double z) {
        return new Vector3(x, y, z);
    }

    /**
     * Add the given vector to this vector.
     *
     * @param other the vector to add
     * @return the sum of the two vectors
     */
    public Vector3 add(Vector3 other) {
        return new Vector3(x + other.x, y + other.y, z + other.z);
    }

    /**
     * Add the given {@link Vector2} to this vector in the x and y components.
     *
     * @param other the vector to add
     * @return the resulting Vector3 after adding the Vector2
     */
    public Vector3 addXY(Vector2 other) {
        return new Vector3(x + other.x, y + other.y, z);
    }

    /**
     * Add the given {@link Vector2} to this vector in the y and z components.
     *
     * @param other the vector to add
     * @return the resulting Vector3 after adding the Vector2
     */
    public Vector3 addYZ(Vector2 other) {
        return new Vector3(x, y + other.x, z + other.y);
    }

    /**
     * Add the given {@link Vector2} to this vector in the x and z components.
     *
     * @param other the vector to add
     * @return the resulting Vector3 after adding the Vector2
     */
    public Vector3 addXZ(Vector2 other) {
        return new Vector3(x + other.x, y, z + other.y);
    }

    /**
     * Subtract the given vector from this vector.
     *
     * @param other the vector to subtract
     * @return the difference of the two vectors
     */
    public Vector3 subtract(Vector3 other) {
        return new Vector3(x - other.x, y - other.y, z - other.z);
    }

    /**
     * Subtract the given {@link Vector2} from this vector in the x and y
     * components.
     *
     * @param other the vector to subtract
     * @return the resulting Vector3 after subtracting the Vector2
     */
    public Vector3 subtractXY(Vector2 other) {
        return new Vector3(x - other.x, y - other.y, z);
    }

    /**
     * Subtract the given {@link Vector2} from this vector in the y and z
     * components.
     *
     * @param other the vector to subtract
     * @return the resulting Vector3 after subtracting the Vector2
     */
    public Vector3 subtractYZ(Vector2 other) {
        return new Vector3(x, y - other.x, z - other.y);
    }

    /**
     * Subtract the given {@link Vector2} from this vector in the x and z
     * components.
     *
     * @param other the vector to subtract
     * @return the resulting Vector3 after subtracting the Vector2
     */
    public Vector3 subtractXZ(Vector2 other) {
        return new Vector3(x - other.x, y, z - other.y);
    }

    /**
     * Multiply this vector by a given scalar.
     *
     * @param scalar the scalar to multiply by
     * @return the product of the vector and the scalar
     */
    public Vector3 multiply(double scalar) {
        return new Vector3(x * scalar, y * scalar, z * scalar);
    }

    /**
     * Divide this vector by a given scalar.
     *
     * @param scalar the scalar to divide by
     * @return the quotient of the vector and the scalar
     */
    public Vector3 divide(double scalar) {
        return new Vector3(x / scalar, y / scalar, z / scalar);
    }

    /**
     * Get the magnitude of this vector.
     *
     * @return the magnitude of the vector
     */
    public double magnitude() {
        return Math.sqrt(x * x + y * y + z * z);
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
    public Vector3 normalize() {
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
    public Vector3 scaleToMagnitude(double magnitude) {
        return normalize().multiply(magnitude);
    }

    /**
     * Clamp the magnitude of this vector to the given value.
     *
     * @param maxMagnitude the maximum magnitude
     * @return the clamped vector
     */
    public Vector3 clampMagnitude(double maxMagnitude) {
        return clampMagnitude(0, maxMagnitude);
    }

    /**
     * Clamp the magnitude of this vector to the given range.
     *
     * @param minMagnitude the minimum magnitude
     * @param maxMagnitude the maximum magnitude
     * @return the clamped vector
     */
    public Vector3 clampMagnitude(double minMagnitude, double maxMagnitude) {
        double magnitude = magnitude();
        if (magnitude < minMagnitude) return scaleToMagnitude(minMagnitude);
        if (magnitude > maxMagnitude) return scaleToMagnitude(maxMagnitude);
        return this;
    }

    /**
     * Clamp the {@link Vector2} representing the x and y components of this
     * vector to the given magnitude and return the resulting Vector3.
     *
     * @param maxMagnitude the maximum magnitude
     * @return the clamped vector
     */
    public Vector3 clampXYMagnitude(double maxMagnitude) {
        return clampXYMagnitude(0, maxMagnitude);
    }

    /**
     * Clamp the {@link Vector2} representing the x and y components of this
     * vector to the given range and return the resulting Vector3.
     *
     * @param minMagnitude the minimum magnitude
     * @param maxMagnitude the maximum magnitude
     * @return the clamped vector
     */
    public Vector3 clampXYMagnitude(double minMagnitude, double maxMagnitude) {
        Vector2 clamped = xy.clampMagnitude(minMagnitude, maxMagnitude);
        return new Vector3(clamped.x, clamped.y, z);
    }

    /**
     * Clamp the {@link Vector2} representing the y and z components of this
     * vector to the given magnitude and return the resulting Vector3.
     *
     * @param maxMagnitude the maximum magnitude
     * @return the clamped vector
     */
    public Vector3 clampYZMagnitude(double maxMagnitude) {
        return clampYZMagnitude(0, maxMagnitude);
    }

    /**
     * Clamp the {@link Vector2} representing the y and z components of this
     * vector to the given range and return the resulting Vector3.
     *
     * @param minMagnitude the minimum magnitude
     * @param maxMagnitude the maximum magnitude
     * @return the clamped vector
     */
    public Vector3 clampYZMagnitude(double minMagnitude, double maxMagnitude) {
        Vector2 clamped = yz.clampMagnitude(minMagnitude, maxMagnitude);
        return new Vector3(x, clamped.x, clamped.y);
    }

    /**
     * Clamp the {@link Vector2} representing the x and z components of this
     * vector to the given magnitude and return the resulting Vector3.
     *
     * @param maxMagnitude the maximum magnitude
     * @return the clamped vector
     */
    public Vector3 clampXZMagnitude(double maxMagnitude) {
        return clampXZMagnitude(0, maxMagnitude);
    }

    /**
     * Clamp the {@link Vector2} representing the x and z components of this
     * vector to the given range and return the resulting Vector3.
     *
     * @param minMagnitude the minimum magnitude
     * @param maxMagnitude the maximum magnitude
     * @return the clamped vector
     */
    public Vector3 clampXZMagnitude(double minMagnitude, double maxMagnitude) {
        Vector2 clamped = xz.clampMagnitude(minMagnitude, maxMagnitude);
        return new Vector3(clamped.x, y, clamped.y);
    }

    /**
     * Get the distance between the tip of this vector and the tip of the given
     * vector.
     *
     * @param other the other vector
     * @return the distance between the two vectors
     */
    public double distanceTo(Vector3 other) {
        return subtract(other).magnitude();
    }

    /**
     * Rotate this vector around the x-axis by the given angle.
     *
     * @param angle the angle to rotate by
     * @return the rotated vector
     */
    public Vector3 rotateX(double angle) {
        double rad = Math.toRadians(angle);
        double cos = Math.cos(rad);
        double sin = Math.sin(rad);
        return new Vector3(x, y * cos - z * sin, y * sin + z * cos);
    }

    /**
     * Rotate this vector around the y-axis by the given angle.
     *
     * @param angle the angle to rotate by
     * @return the rotated vector
     */
    public Vector3 rotateY(double angle) {
        double rad = Math.toRadians(angle);
        double cos = Math.cos(rad);
        double sin = Math.sin(rad);
        return new Vector3(x * cos + z * sin, y, x * sin - z * cos);
    }

    /**
     * Rotate this vector around the z-axis by the given angle.
     *
     * @param angle the angle to rotate by
     * @return the rotated vector
     */
    public Vector3 rotateZ(double angle) {
        double rad = Math.toRadians(angle);
        double cos = Math.cos(rad);
        double sin = Math.sin(rad);
        return new Vector3(x * cos - y * sin, x * sin + y * cos, z);
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
    public boolean equals(Object other) {
        try {
            Vector3 otherVec = (Vector3) other;
            return x == otherVec.x && y == otherVec.y && z == otherVec.z;
        } catch (ClassCastException e) {
            return false;
        }
    }

    /**
     * Get a string representation of this vector.
     *
     * @return a string representation of this vector
     */
    @Override
    public String toString() {
        return "Vector3(" + x + ", " + y + ", " + z + ")";
    }
}
