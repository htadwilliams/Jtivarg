package org.tsoft.jtivarg.math;

import static junit.framework.TestCase.assertTrue;

import org.junit.Test;

/**
 *
 */
public class VectorTests
{
    public static final double EXPECTED_VALUE = 0.23f;

    @Test
    public void testClampAngleZero()
    {
        double inputAngle = 0.0f;
        double outputAngle = Vector.clampAngle(inputAngle);

        assertTrue("Expect input angle to equal output angle", inputAngle == outputAngle);
    }

    @Test
    public void testClampAngleSmall()
    {
        double inputAngle = Vector.CIRCLE_RADIANS - 0.01f;
        double outputAngle = Vector.clampAngle(inputAngle);

        assertTrue("Expect input angle to equal output angle", inputAngle == outputAngle);
    }

    @Test
    public void testClampAngleClampedPositive()
    {
        double inputAngle = Vector.CIRCLE_RADIANS + EXPECTED_VALUE;
        double outputAngle = Vector.clampAngle(inputAngle);

        assertTrue("Expect output angle to be clamped at 2 * PI", outputAngle == EXPECTED_VALUE);
    }

    @Test
    public void testClampAngleClampedNegative()
    {
        double inputAngle = -1 * (Vector.CIRCLE_RADIANS + EXPECTED_VALUE);
        double outputAngle = Vector.clampAngle(inputAngle);

        assertTrue("Expect output angle to be clamped at -2 * PI", outputAngle == (-1 * EXPECTED_VALUE));
    }
}
