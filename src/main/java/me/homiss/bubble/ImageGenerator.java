package me.homiss.bubble;

import java.awt.*;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

/**
 * Created with wechatShare.
 * User : Homiss
 * Date : 2016/9/2
 * Time : 10:03
 */
public class ImageGenerator {

    /**
     * 文字生成器 自动换行（默认微软雅黑字体，黑色画笔）
     * @param image 图片流
     * @param text 文字内容
     * @param font 字体
     * @param color 字体颜色
     * @param x x坐标
     * @param y y坐标
     * @return
     * @throws IOException
     */
    public static BufferedImage wordArtGenerator(BufferedImage image, String text,
                                                 Font font, Color color,
                                                 int lineWidth, int cellHeight,
                                                 int x, int y) throws IOException, FontFormatException {
        Map<TextAttribute, Object> map = new Hashtable<TextAttribute, Object>();
        map.put(TextAttribute.KERNING, TextAttribute.KERNING_ON);
        font = font.deriveFont(map);
        Graphics2D g;
        g = image.createGraphics();
        g.setColor(color);
        g.setFont(font);
        drawStringMultiLine(g, text, lineWidth, cellHeight, x, y, font);
        g.dispose();
        return image;
    }

    /**
     *
     *描述: 长字符串缩小字体自动换行
     *@param g
     *@param text 字符串
     *@param lineWidth 单元格宽度
     *@param cellHeight 单元格高度
     *@param x x坐标
     *@param y y坐标
     *@param cellFont 原字体
     */
    public static Graphics2D drawStringMultiLine(Graphics2D g, String text, int lineWidth, int cellHeight,int x, int y,Font cellFont) throws IOException, FontFormatException {
        FontMetrics m = g.getFontMetrics();
        char[] chars = text.toCharArray();
        int strWidth = 0;
        int oneLineWidth = 0;
        for(char c : chars){
            strWidth += m.charWidth(c);
            if(strWidth > lineWidth && oneLineWidth == 0){
                oneLineWidth = strWidth - m.charWidth(c);
            }
        }
        if(m.stringWidth(text) < lineWidth) {
            x = x + (lineWidth - strWidth)/2;
            g.drawString(text, x, y);
        } else {
            int widthLine = 1;
            for(int i = 0; i < chars.length; i++){
                if(m.charWidth(chars[i]) > lineWidth){ //单个字比单元格宽，肯定缩小字体
                    widthLine = 10000;
                    break;
                }

                strWidth += m.charWidth(chars[i]);
                if(strWidth > lineWidth){
                    widthLine++;
                    strWidth = 0;
                    i--;
                }
            }

            int	high = cellFont.getSize();
            int	fontHeight = m.getAscent() + m.getDescent();

            int heightLine =2;//一个单元格只能写2行
            x = x + (lineWidth - oneLineWidth)/2;
            while (widthLine > heightLine){
                /* 缩小字体，重复计算应该打印行数和允许打印行数 */
                cellFont = cellFont.deriveFont((float)(--high));
                m = g.getFontMetrics(cellFont);

                /* 字体高度 */
                fontHeight = m.getAscent() + m.getDescent();
                if (fontHeight <= 0)
                    return g;

                strWidth = 0;
                /* 使用当前字体, 根据单元格宽度计算出应该打印行数 */
                widthLine = 1;
                for(int i = 0; i < chars.length; i++){
                    if(m.charWidth(chars[i]) > lineWidth){ //单个字比单元格宽，肯定缩小字体
                        widthLine = 10000;
                        break;
                    }

                    strWidth += m.charWidth(chars[i]);
                    if(strWidth > lineWidth){
                        widthLine++;
                        strWidth = 0;
                        i--;
                    }
                }

                /* 使用当前字体时,根据单元格高度计算出允许打印行数 */
                heightLine = 0;
                while((fontHeight*heightLine) <= cellHeight)//最后一行没有行间距
                    heightLine++;
                heightLine--;

                if(widthLine <= heightLine) {
                    break;
                }
            }

            Font oldFont = g.getFont();
            Stroke oldStroke = g.getStroke();
            g.setFont(cellFont);
            g.setStroke(new BasicStroke(1.0f));

            /* 分行，计算各行文字内容 */
            java.util.List<String> rows = new ArrayList<String>();
            int fromIndex = 0;
            strWidth = 0;
            for (int bgn=0; bgn<text.length();){//逐个字符累加计算长度,超过行宽,自动换行
                strWidth += m.charWidth(chars[bgn]);
                if(strWidth > lineWidth){
                    rows.add(text.substring(fromIndex, bgn));
                    strWidth = 0;
                    fromIndex = bgn;
                }
                else
                    bgn++;
            }

            if(fromIndex < text.length()) // 加上最后一行
                rows.add(text.substring(fromIndex, text.length()));
            String element;
            for (Iterator iter = rows.iterator(); iter.hasNext();) {
                element = (String) iter.next();
                g.drawString(element, (float)x, (float)(y + m.getAscent()));
                y += fontHeight;
            }
            g.setFont(oldFont);
            g.setStroke(oldStroke);
        }
        return g;
    }
}
