import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

public class Utils {

	public static void spawnCenter(JFrame frame) {

		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension screenSize = toolkit.getScreenSize();

		int x = (screenSize.width - frame.getWidth()) / 2;
		int y = (screenSize.height - frame.getHeight()) / 2;

		frame.setLocation(x, y);
	}

}