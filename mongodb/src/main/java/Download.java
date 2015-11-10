import java.io.File;
import java.net.URL;

import com.github.axet.vget.VGet;
public class Download {




	public static void main(String[] args) {
		try {
			// ex: http://www.youtube.com/watch?v=Nj6PFaDmp6c
			String url = "https://www.youtube.com/watch?v=vdTERSLWIoU";
			// ex: "C:\"
			String path = "C:\\";
			VGet v = new VGet(new URL(url), new File(path));
			v.download();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
