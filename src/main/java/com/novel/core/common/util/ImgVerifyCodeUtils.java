package com.novel.core.common.util;

import lombok.experimental.UtilityClass;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Random;

@UtilityClass
public class ImgVerifyCodeUtils {

    public final String ranNumber = "0123456789";

    private final int width = 100;

    private final int height = 38;

    private final Random random = new Random();

    private Font getFont() {
        return new Font("Fixed", Font.PLAIN, 23);
    }

    //生成图片验证码，并将其转换为 Base64 编码的字符串
    public String generateVerifyCodeImg(String verifyCode) throws IOException {
        BufferedImage image =  new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        g.fillRect(0, 0, width, height);
        g.setColor(new Color(204, 204, 204));
        int lineSize = 40;
        for (int i = 0; i < lineSize; i++) {
            drawLine(g);
        }
        drawString(g,verifyCode);
        g.dispose();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        ImageIO.write(image, "png", stream);
        return Base64.getEncoder().encodeToString(stream.toByteArray());
    }

    private  void drawString(Graphics g, String verifyCode){
        for (int i = 0; i < verifyCode.length(); i++) {
            g.setFont(getFont());
            g.setColor(new Color(
                    random.nextInt(101),
                    random.nextInt(111),
                    random.nextInt(121)));
            g.translate(random.nextInt(3),random.nextInt(3));
            g.drawString(String.valueOf(verifyCode.charAt(i)),13*i,23);
        }
    }
    private void drawLine(Graphics g) {
        int x = random.nextInt(width);
        int y = random.nextInt(height);
        int xl = random.nextInt(12);
        int yl = random.nextInt(12);
        g.drawLine(x, y, x + xl, y + yl);
    }
    public String getRandomVerifyCode(int num) {
        int randNumberSize = ranNumber.length();
        StringBuilder verifyCode = new StringBuilder();
        for (int i = 0; i < num; i++) {
            String rand = String.valueOf(ranNumber.charAt(random.nextInt(randNumberSize)));
            verifyCode.append(rand);
        }
        return verifyCode.toString();
    }
}
