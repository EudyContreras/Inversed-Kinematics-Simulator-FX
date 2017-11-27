
package com.eudycontreras.javafx.fbk.transitioners;


import com.eudycontreras.javafx.fbk.models.FBKSegment;

import javafx.animation.Transition;
import javafx.util.Duration;

public abstract class FBKTransition extends Transition {

    /**
     * The {@link com.eudycontreras.javafx.fbk.models.FBKSegment} which is affected by this transition.
     */
    private final FBKSegment FBKSegment;
    
    private final Duration duration;
    
    private final double fromX;
    private final double fromY;
    
    private final double byX;
    private final double byY;
    
    public final FBKSegment getBone() {return FBKSegment;}

    /**
     * The duration of this {@code Transition}.
     */

    public final Duration getDuration() {return duration;}

    /**
     * Specifies the start X coordinate value of this transition.
     */

    public final double getFromX() {return fromX;}

    /**
     * Specifies the start Y coordinate value of this transition.
     */

    public final double getFromY() {return fromY;}

    /**
     * Specifies the delta of the X coordinate of this transition.
     */
 
    public final double getByX() {return byX;}

    /**
     * Specifies the delta of the Y coordinate of this transition.
     */
    public final double getByY() {return byY;}

    /**
     * Specifies the end X coordinate value of this transition.
     */
    public final double getToX() {return fromX + byX;}

    /**
     * Specifies the stop Y coordinate value of this transition.
     */
    public final double getToY() {return fromY + byY;}

    /**
     * The constructor of {@code AbstractIKTransition}
     *
     * @param FBKSegment The {@link FBKSegment} affected by this transition
     * @param duration The duration
     * @param fromX The start X coordinate
     * @param fromY The start Y coordinate
     * @param byX The delta of the X coordinate
     * @param byY The delta of the Y coordinate
     */
    public FBKTransition(FBKSegment FBKSegment, Duration duration, double fromX, double fromY, double byX, double byY) {
        this.FBKSegment = FBKSegment;
        this.duration = duration;
        this.fromX = fromX;
        this.fromY = fromY;
        this.byX = byX;
        this.byY = byY;
        setCycleDuration(duration);
    }

}
