package com.utils;

import java.util.LinkedList;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;

/**
 * Class used for controlling and performing frame based animations
 *
 * @author Eudy
 *
 */
public class AnimationUtility {

	private LinkedList<Object> scenes = new LinkedList<Object>();
	private int sceneIndex;
	private long movieTime;
	private long totalTime;

	public AnimationUtility() {
		totalTime = 0;
		start();
	}

	public synchronized void addScene(Image image, long time) {
		totalTime += time;
		scenes.add(new OneScene(image, totalTime));
	}

	/**
	 * starts the animation
	 */
	public synchronized void start() {
		movieTime = 0;
		sceneIndex = 0;

	}

	/**
	 * Updates the animations by switching the frames
	 *
	 * @param timePassed
	 */
	public synchronized void update(long timePassed) {
		if (scenes.size() > 1) {
			movieTime += timePassed;
			if (movieTime >= totalTime) {
				movieTime = 0;
				sceneIndex = 0;
			}
			while (movieTime > getScene(sceneIndex).endTime) {
				sceneIndex++;
			}
		}
	}

	/**
	 * Fetches the image
	 *
	 * @return
	 */
	public synchronized Image getImage() {
		if (scenes.size() == 0) {
			return null;
		} else {
			return getScene(sceneIndex).pic;
		}
	}

	/**
	 * Fetches the image
	 *
	 * @return
	 */
	public synchronized ImagePattern getPattern() {
		if (scenes.size() == 0) {
			return null;
		} else {
			return getScene(sceneIndex).pattern;
		}
	}

	/**
	 * Fetches the frame
	 *
	 * @param x
	 * @return
	 */
	private OneScene getScene(int x) {
		return (OneScene) scenes.get(x);
	}

	/**
	 * Scene class holding one particular scene or frame
	 *
	 * @author Eudy Contreras
	 *
	 */
	private class OneScene {
		private Image pic;
		private ImagePattern pattern;
		private long endTime;

		public OneScene(Image pic, long endTime) {
			this.pic = pic;
			this.endTime = endTime;
			this.pattern = new ImagePattern(pic);
		}
	}

	/**
	 * Animation example. We first create and array of the image we wish to
	 * animate. then we initialize each image in the array according to their
	 * respective paths this should be done in the order of the animation. as
	 * seen here each image has a number making it easy to add to both
	 * initialize them and add them to scene...
	 */
	public void addImagesToAnimate() {
		Image[] objectToAnimate = new Image[4];

		for (int i = 0; i < objectToAnimate.length; i++) {
			objectToAnimate[i] = new Image("respectivePath" + i + ".png");
			addScene(objectToAnimate[i], 250);
		}
	}
}
