import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class CreateImage {
    public static void main(String[] args) throws IOException {
        BufferedImage image =
            new BufferedImage(16, 16, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < 16; y++) {
            for (int x = 0; x < 16; x++) {
                // A black square surrounded by white pixels.
                if (x >= 4 && x <= 11 && y >= 4 && y <= 11) {
                    image.setRGB(x, y, 0x000000);
                } else {
                    image.setRGB(x, y, 0xFFFFFF);
                }
            }
        }

        ImageIO.write(image, "bmp", new File("image.bmp"));
        System.out.println("Created image.bmp");
    }
}