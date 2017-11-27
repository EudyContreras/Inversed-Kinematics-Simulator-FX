
package com.eudycontreras.javafx.fbk.transitioners;


import com.eudycontreras.javafx.fbk.models.FBKSegment;

import javafx.util.Duration;

public class FBKTransitionTail extends FBKTransition {

    /**
     * The constructor of {@code TranslateTailTransition}
     *
     * @param FBKSegment The {@link com.netopyr.javafx.ik.FBKSegment} affected by this transition
     * @param duration The duration
     * @param fromX The start X coordinate
     * @param fromY The start Y coordinate
     * @param byX The delta of the X coordinate
     * @param byY The delta of the Y coordinate
     */
    public FBKTransitionTail(FBKSegment FBKSegment, Duration duration, double fromX, double fromY, double byX, double byY) {
        super(FBKSegment, duration, fromX, fromY, byX, byY);
    }

    /**
     * The constructor of {@code TranslateTailTransition}
     *
     * @param FBKSegment The {@link FBKSegment} affected by this transition
     * @param duration The duration
     * @param byX The delta of the X coordinate
     * @param byY The delta of the Y coordinate
     */
    public FBKTransitionTail(FBKSegment FBKSegment, Duration duration, double byX, double byY) {
        super(FBKSegment, duration, FBKSegment.getCurrentTail().getX(), FBKSegment.getCurrentTail().getY(), byX, byY);
    }

    @Override
    protected void interpolate(double v) {
        final double x = getFromX() + v * getByX();
        final double y = getFromY() + v * getByY();
        getBone().moveTail(x,y);
    }
}
