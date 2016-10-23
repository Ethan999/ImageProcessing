package me.homiss.ninePhoto;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * User : Homiss
 * Date : 2016/10/23
 * Time : 12:10
 * 将用户输入的文字转成一张张的图片
 */
public class WordNinePhotoGenerator {

    public static void main(String[] args) throws IOException {
        String path = "D:\\";
        String text = "123456789";
        String bgColor = "204,255,255", fontColor = "250,167,50";
        char[] chars = text.toCharArray();
        String[] bgRGB = bgColor.split(",");
        String[] fontRGB = fontColor.split(",");
        for(int i=0; i < chars.length; i++){
            if(i < 9){
                String url = path + "word_" + i + ".png";
                generator(String.valueOf(chars[i]), url,
                        new Color(Integer.parseInt(bgRGB[0]), Integer.parseInt(bgRGB[1]), Integer.parseInt(bgRGB[2])),
                        new Color(Integer.parseInt(fontRGB[0]), Integer.parseInt(fontRGB[1]), Integer.parseInt(fontRGB[2])));
            }
        }
    }

    public static void generator(String text, String path, Color backgroundColor, Color fontColor) throws IOException {
        int width = 300;
        int height = 300;
        BufferedImage image = new BufferedImage(width, height,BufferedImage.TYPE_INT_RGB);

        Font font = new Font("宋体",Font.PLAIN,250);
        Graphics2D g;
        g = image.createGraphics();
        g.setBackground(backgroundColor);
        g.setPaint(fontColor);
        g.clearRect(0, 0, width, height);
        g.setFont(font);
        FontMetrics m = g.getFontMetrics();
        char[] chars = text.toCharArray();
        int textWidth = 0;
        for(char c : chars){
            textWidth += m.charWidth(c);
        }
        int x = (width - textWidth) / 2;
        g.drawString(text, x, 240);
        //释放对象
        g.dispose();
        // 保存文件
        ImageIO.write(image, "png", new File(path));
    }
}
