package com.QRCode;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;
import javax.imageio.ImageIO;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;


public class QRCode {
    private static final int BLACK = 0xff000000;
    private static final int WHITE = 0xFFFFFFFF;

    /**
     * @param args
     */    
    public static void main(String[] args) {
    	String LogoPath="D://C338.png";
        String Content="NAD C338 WiFi 11223344 BT 11223344 WiFi Otp 12344 Serial 1234r1324#";    	
//    	String Content=null;
        String filePath = "D:\\MappingDetails.txt";
        new readTxt().readTxtFile(filePath);
        QRCode test = new QRCode();
        File file = new File("D://test.png"); 
        test.encode(Content, file, BarcodeFormat.QR_CODE, 600, 600,LogoPath, null);
        test.decode(file);
        
    }

    /**
     * ����QRCode��ά��<br> 
     * �ڱ���ʱ��Ҫ��com.google.zxing.qrcode.encoder.Encoder.java�е�<br>
     *  static final String DEFAULT_BYTE_MODE_ENCODING = "ISO8859-1";<br>
     *  �޸�ΪUTF-8���������ı�����������<br>
     */
    public void encode(String contents, File file, BarcodeFormat format, int width, int height, String logoPath, Map<EncodeHintType,?> hints) {
        try {
                 //��������
        	contents = new String(contents.getBytes("UTF-8"), "ISO-8859-1"); 
            BitMatrix bitMatrix = new MultiFormatWriter().encode(contents, format, width, height);
            writeToFile(bitMatrix, "png", file,logoPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * ���ɶ�ά��ͼƬ<br>
     * 
     * @param matrix
     * @param format
     *            ͼƬ��ʽ
     * @param file
     *            ���ɶ�ά��ͼƬλ��
     * @throws IOException
     */
    public static void writeToFile(BitMatrix matrix, String format, File file,String logoPath) throws IOException {
        BufferedImage image = toBufferedImage(matrix);
        Graphics2D gs = image.createGraphics(); 
        Image img = ImageIO.read(new File(logoPath));  
        gs.drawImage(img, 250, 250, null);  
        gs.dispose();  
        img.flush();
        if(!ImageIO.write(image, format, file)){  
            throw new IOException("Could not write an image of format " + format + " to " + file);    
        }  
    }

    /**
     * ���ɶ�ά������<br>
     * 
     * @param matrix
     * @return
     */
    public static BufferedImage toBufferedImage(BitMatrix matrix) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, matrix.get(x, y) == true ? BLACK : WHITE);
            }
        }
        return image;
    }

    /**
     * ����QRCode��ά��
     */
    @SuppressWarnings("unchecked")
    public void decode(File file) {
        try {
            BufferedImage image;
            try {
                image = ImageIO.read(file);
                if (image == null) {
                    System.out.println("Could not decode image");
                }
                LuminanceSource source = new BufferedImageLuminanceSource(image);
                BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
                Result result;
                @SuppressWarnings("rawtypes")
                Hashtable hints = new Hashtable();
                //�������ñ��뷽ʽΪ��utf-8
                hints.put(DecodeHintType.CHARACTER_SET, "utf-8");
                result = new MultiFormatReader().decode(bitmap, hints);
                String resultStr = result.getText();
                System.out.println("���������ݣ�" + resultStr);
            } catch (IOException ioe) {
                System.out.println(ioe.toString());
            } catch (ReaderException re) {
                System.out.println(re.toString());
            }
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }
}