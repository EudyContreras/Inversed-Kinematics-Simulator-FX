
package com.eudycontreras.javafx.fbk.transitioners;


import com.eudycontreras.javafx.fbk.models.FBKSegment;

import javafx.geometry.Point2D;
import javafx.util.Duration;

public class FBKTransitionHead extends FBKTransition {

    /**
     * The constructor of {@code TranslateHeadTransition}
     *
     * @param FBKSegment The {@link FBKSegment} affected by this transition
     * @param duration The duration
     * @param fromX The start X coordinate
     * @param fromY The start Y coordinate
     * @param byX The delta of the X coordinate
     * @param byY The delta of the Y coordinate
     */
    public FBKTransitionHead(FBKSegment FBKSegment, Duration duration, double fromX, double fromY, double byX, double byY) {
        super(FBKSegment, duration, fromX, fromY, byX, byY);
    }

    /**
     * The constructor of {@code TranslateHeadTransition}
     *
     * @param FBKSegment The {@link FBKSegment} affected by this transition
     * @param duration The duration
     * @param from The start X coordinate
     * @param by The delta
     */
    public FBKTransitionHead(FBKSegment FBKSegment, Duration duration, Point2D from, Point2D by) {
        super(FBKSegment, duration, from.getX(), from.getY(), by.getX(), by.getY());
    }

    /**
     * The constructor of {@code TranslateHeadTransition}
     *
     * @param FBKSegment The {@link FBKSegment} affected by this transition
     * @param duration The duration
     * @param byX The delta of the X coordinate
     * @param byY The delta of the Y coordinate
     */
    public FBKTransitionHead(FBKSegment FBKSegment, Duration duration, double byX, double byY) {
        super(FBKSegment, duration, FBKSegment.getCurrentHead().getX(), FBKSegment.getCurrentHead().getY(), byX, byY);
    }

    /**
     * The constructor of {@code TranslateHeadTransition}
     *
     * @param FBKSegment The {@link FBKSegment} affected by this transition
     * @param duration The duration
     * @param by The delta
     */
    public FBKTransitionHead(FBKSegment FBKSegment, Duration duration, Point2D by) {
        super(FBKSegment, duration, FBKSegment.getCurrentHead().getX(), FBKSegment.getCurrentHead().getY(), by.getX(), by.getY());
    }

    @Override
    protected void interpolate(double v) {
        final double x = getFromX() + v * getByX();
        final double y = getFromY() + v * getByY();
        getBone().moveHead(x,y);
    }
}
