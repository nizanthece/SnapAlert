package Service;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.io.File;
import javax.imageio.ImageIO;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
/**
 * Created by nizan on 6/13/2017.
 */
public class CreateSnap {
    SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd hh mm ss a");
    private BufferedImage cropImage(BufferedImage src, Rectangle rect) {
        BufferedImage dest = src.getSubimage(rect.x, rect.y, rect.width, rect.height);
        return dest;
    }

    public BufferedImage robo(Rectangle rectangle) throws Exception
    {
        Calendar now = Calendar.getInstance();
        Robot robot = new Robot();
        BufferedImage screenShot = robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
        BufferedImage copyBufferedImage =  cropImage(screenShot, rectangle);
        //ImageIO.write(copyBufferedImage, "JPG", new File("G:\\Alert\\"+formatter.format(now.getTime())+".jpg"));
        System.out.println(formatter.format(now.getTime()));
        return copyBufferedImage;
    }

    public static void main(String[] args) throws Exception
    {
        Calendar now = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd hh mm ss a");
        CreateSnap s2i = new CreateSnap();
        SendEmail sendEmail = new SendEmail();
        Robot robot = new Robot();
        Rectangle rectangle = new Rectangle();
        Rectangle rectangle2 = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        rectangle.setBounds(0, 145, rectangle2.width, rectangle2.height-240);
        BufferedImage bufferedImage = robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
        while(1==1)
        {
            now = Calendar.getInstance();
            BufferedImage previousbufferedImage = bufferedImage;
            bufferedImage = s2i.robo(rectangle);
            if ( bufferedImage.equals(previousbufferedImage)) {
                System.out.println("Both snap shots are same");
            }
            else {
                System.out.println("Both snapshots are different");
            }
            Float percent = compareImage(bufferedImage,previousbufferedImage);
            if(percent < 99 ) {
                String fileName = "D:\\Alert\\"+formatter.format(now.getTime())+".jpg";
                ImageIO.write(bufferedImage, "JPG", new File(fileName));
                sendEmail.sendEmail(fileName,"Alert","hello");
            }
            System.out.println("Comparison percentage is " + percent);
            Thread.sleep(6000);
        }
    }
    static float compareImage(BufferedImage biA, BufferedImage biB) {

        float percentage = 0;
        try {
            // take buffer data from both image files //
            //BufferedImage biA = ImageIO.read(fileA);
            DataBuffer dbA = biA.getData().getDataBuffer();
            int sizeA = dbA.getSize();
            //BufferedImage biB = ImageIO.read(fileB);
            DataBuffer dbB = biB.getData().getDataBuffer();
            int sizeB = dbB.getSize();
            int count = 0;
            // compare data-buffer objects //
            if (sizeA == sizeB) {

                for (int i = 0; i < sizeA; i++) {

                    if (dbA.getElem(i) == dbB.getElem(i)) {
                        count = count + 1;
                    }

                }
                percentage = (count * 100) / sizeA;
            } else {
                System.out.println("Both the images are not of same size");
            }

        } catch (Exception e) {
            System.out.println("Failed to compare image files ...");
        }
        return percentage;
    }

}
