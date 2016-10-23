package me.homiss.bubble;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * User : Homiss
 * Date : 2016/10/23
 * Time : 12:20
 * 气泡文字生成
 */
public class BubbleGenerator {

    public static String fontPath = ClassLoader.getSystemResource("").getFile() + "\\image\\bubble\\font\\Hiragino Sans GB W6.TTF";

    public static void main(String[] args) throws IOException, FontFormatException {
        String path = ClassLoader.getSystemResource("").getFile() + "\\image\\bubble\\green.png";
        String text = "与你若只如初见何须感伤离别";
        BufferedImage newImage = wordToBubbleImage(path, text);
        ImageIO.write(newImage, "jpg", new File("D:\\bubble.jpg"));
    }

    public static BufferedImage wordToBubbleImage(String imagePath, String text) throws IOException, FontFormatException {
        BufferedImage image = ImageIO.read(new File(imagePath));
        Font font = Font.createFont(Font.TRUETYPE_FONT, new File(fontPath));
        font = font.deriveFont(300.0f);
        char[] chars = text.toCharArray();
        int x = 100;
        int y = chars.length < 4 ? 300 : 0;
        int width = 1000;
        int height = chars.length <= 5 ? 370 : chars.length < 12 ? 340 : 370;
        return ImageGenerator.wordArtGenerator(image, text, font, Color.black, width, height, x, y);
    }
}
