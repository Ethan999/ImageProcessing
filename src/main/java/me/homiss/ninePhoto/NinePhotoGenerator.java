package me.homiss.ninePhoto;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

/**
 * User : Homiss
 * Date : 2016/10/23
 * Time : 10:57
 * 九宫格生成器：
 * 将一张图片切割成9张小图
 * 实现方式：
 * 1. 将图片裁剪成正方形
 * 2. 将图片切割为九张图片
 */
public class NinePhotoGenerator {

    public static String srcImageFile = ClassLoader.getSystemResource("").getFile() + "\\image\\ninePhoto\\nine.jpg";

    public static void main(String[] args) throws IOException {
        String generatorFile = "D:\\"; // 图片生成文件夹

        // 裁剪图片并覆盖掉原图
        String path = generatorFile + "square.jpg";
        Tosmallerpic(srcImageFile, path, 900, 900, (float)0.7);

        BufferedImage bi = ImageIO.read(new File(path));
        CutImage.cutImg(path, generatorFile, bi.getHeight()/3, bi.getHeight()/3); // 从最中间开始切图
    }


    /**
     * @param url 图片所在的文件夹路径
     * @param w 目标宽
     * @param h 目标高
     * @param per 百分比
     */
    public static void  Tosmallerpic(String url, String targetUrl, int w, int h, float per){
        BufferedImage src;
        try {
            File filelist = new File(url);
            src = javax.imageio.ImageIO.read(filelist); //构造Image对象
            int old_w = src.getWidth(); //得到源图宽
            int old_h = src.getHeight();
            int new_w, new_h; //得到源图长

            double w2 = (old_w*1.00)/(w*1.00);
            double h2 = (old_h*1.00)/(h*1.00);

            //图片跟据长宽留白，成一个正方形图。
            BufferedImage oldpic;
            if(old_w > old_h) {
                oldpic = new BufferedImage(old_h,old_h,BufferedImage.TYPE_INT_RGB);
            } else {
                oldpic = new BufferedImage(old_w,old_w,BufferedImage.TYPE_INT_RGB);
            }
            Graphics2D g = oldpic.createGraphics();
            g.setColor(Color.white);
            if(old_w > old_h) {
                g.fillRect(0, 0, old_h, old_h);
                g.drawImage(src, (old_h - old_w) / 2, 0, old_w, old_h, Color.white, null);
            } else {
                if(old_w < old_h){
                    g.fillRect(0,0,old_w,old_w);
                    g.drawImage(src, 0, (old_w - old_h) / 2, old_w, old_h, Color.white, null);
                } else {
                    g.drawImage(src.getScaledInstance(old_w, old_h,  Image.SCALE_SMOOTH), 0,0,null);
                }
            }
            g.dispose();
            src = oldpic;
            //图片调整为方形结束
            if(old_w > w)
                new_w = (int)Math.round(old_w/w2);
            else
                new_w = old_w;
            if(old_h > h)
                new_h = (int)Math.round(old_h/h2);//计算新图长宽
            else
                new_h = old_h;
            int length = new_w > new_h ? new_h : new_w;
            BufferedImage tag = new BufferedImage(length, length, BufferedImage.TYPE_INT_RGB);
            //tag.getGraphics().drawImage(src,0,0,new_w,new_h,null); //绘制缩小后的图
            tag.getGraphics().drawImage(src.getScaledInstance(new_w, new_h, Image.SCALE_SMOOTH), 0,0,null);
            FileOutputStream newimage = new FileOutputStream(targetUrl); //输出到文件流
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(newimage);
            JPEGEncodeParam jep = JPEGCodec.getDefaultJPEGEncodeParam(tag);
            /* 压缩质量 */
            jep.setQuality(per, true);
            encoder.encode(tag, jep);
            //encoder.encode(tag); //近JPEG编码
            newimage.close();
        } catch (IOException ex) {

        }
    }
}

class CutImage {

    // 源图片路径名称如:c:\1.jpg
    private String srcpath;

    // 剪切图片存放路径名称.如:c:\2.jpg
    private String subpath;

    // 剪切点x坐标
    private int x;

    private int y;

    // 剪切点宽度
    private int width;

    private int height;

    public CutImage() {

    }

    public CutImage(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * 对图片裁剪，并把裁剪完蛋新图片保存 。
     */
    public void cut() throws IOException {

        FileInputStream is = null;
        ImageInputStream iis = null;

        try {
            // 读取图片文件
            is = new FileInputStream(srcpath);

            /**
             * 返回包含所有当前已注册 ImageReader 的 Iterator，这些 ImageReader 声称能够解码指定格式。
             * 参数：formatName - 包含非正式格式名称 . （例如 "jpeg" 或 "tiff"）等 。
             */
            Iterator<ImageReader> it = ImageIO
                    .getImageReadersByFormatName("jpg");
            ImageReader reader = it.next();
            // 获取图片流
            iis = ImageIO.createImageInputStream(is);

            /**
             * iis:读取源.true:只向前搜索.将它标记为 ‘只向前搜索’。 此设置意味着包含在输入源中的图像将只按顺序读取，可能允许
             * reader 避免缓存包含与以前已经读取的图像关联的数据的那些输入部分。
             */
            reader.setInput(iis, true);

            /**
             * <p>
             * 描述如何对流进行解码的类
             * <p>
             * .用于指定如何在输入时从 Java Image I/O 框架的上下文中的流转换一幅图像或一组图像。用于特定图像格式的插件 将从其
             * ImageReader 实现的 getDefaultReadParam 方法中返回 ImageReadParam 的实例。
             */
            ImageReadParam param = reader.getDefaultReadParam();

            /**
             * 图片裁剪区域。Rectangle 指定了坐标空间中的一个区域，通过 Rectangle 对象
             * 的左上顶点的坐标（x，y）、宽度和高度可以定义这个区域。
             */
            System.out.println(x + "  " + y + "  " + width + "  " + height);
            Rectangle rect = new Rectangle(x, y, width, height);

            // 提供一个 BufferedImage，将其用作解码像素数据的目标。
            param.setSourceRegion(rect);

            /**
             * 使用所提供的 ImageReadParam 读取通过索引 imageIndex 指定的对象，并将 它作为一个完整的
             * BufferedImage 返回。
             */
            BufferedImage bi = reader.read(0, param);

            // 保存新图片
            ImageIO.write(bi, "jpg", new File(subpath));
        } finally {
            if (is != null)
                is.close();
            if (iis != null)
                iis.close();
        }
    }

    /**
     * 图像切割
     *
     * @param srcImageFile
     *            源图像地址
     * @param descDir
     *            切片目标文件夹
     */
    public static java.util.List<String> cutImg(String srcImageFile, String descDir,
                                                int destWidth, int destHeight) {
        java.util.List<String> list = new java.util.ArrayList<String>(9);
        try {
            String dir;
            BufferedImage bi = ImageIO.read(new File(srcImageFile));
            int srcWidth = bi.getWidth(); // 源图宽度
            int srcHeight = bi.getHeight(); // 源图高度
            if (srcWidth > destWidth && srcHeight > destHeight) {
                int cols = 3; // 切片横向数量
                int rows = 3; // 切片纵向数量

                // 循环建立切片
                for (int i = 0; i < rows; i++) {
                    for (int j = 0; j < cols; j++) {
                        CutImage cutImage = new CutImage(j * destWidth, i * destHeight, destWidth,
                                destHeight);
                        cutImage.setSrcpath(srcImageFile);
                        dir = descDir + "cut_image_" + i + "_" + j + ".jpg";
                        cutImage.setSubpath(dir);
                        cutImage.cut();
                        list.add(dir);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getSrcpath() {
        return srcpath;
    }

    public void setSrcpath(String srcpath) {
        this.srcpath = srcpath;
    }

    public String getSubpath() {
        return subpath;
    }

    public void setSubpath(String subpath) {
        this.subpath = subpath;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }


}
