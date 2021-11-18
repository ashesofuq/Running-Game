package util;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Animation {

	private List<BufferedImage> list;
	private long time;
	private int currentFrame = 0;
	private long previousTime;

	public Animation(int time) {
		this.time = time;
		list = new ArrayList<BufferedImage>();
		previousTime = 0;
	}

	public void updateFrame() {
		if (System.currentTimeMillis() - previousTime >= time) {
			currentFrame++;
			if (currentFrame >= list.size()) {
				currentFrame = 0;
			}
			previousTime = System.currentTimeMillis();
		}
	}

	public void addFrame(BufferedImage image) {
		list.add(image);
	}

	public BufferedImage getFrame() {
		return list.get(currentFrame);
	}

}
