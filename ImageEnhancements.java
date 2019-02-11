package com.company;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;

public class ImageEnhancements {

    String Path;
    boolean flag=false;
    public void Browse(String path) throws Exception
    {
        this.Path=path;
        flag=true;
        this.caller(3);
    }
    boolean caller(int choice) throws Exception
    {
        if(flag==true)
        {
            switch (choice)
            {
                case 1:
                    this.NegativeOfAnImage();
                    break;
                case 2:
                    this.MaxPooling(8);
                    break;
                case 3:
                    this.GrayScale();
                    break;
                case 4:
                    this.ReduceQuality(20);
                case 5 :
                    this.BlurImage(4);
                    break;
            }
            return true;
        }
        else
        {
            return false;
        }
    }
    void NegativeOfAnImage() throws Exception
    {

        File img = new File(Path);
        if(!img.exists())
            throw new Exception(" image (" + Path+ ") not found");
        BufferedImage bImg = ImageIO.read(img);
        int w = bImg.getWidth();
        int h = bImg.getHeight();
        WritableRaster wRaster = bImg.getRaster();
        int r , g, b;
        for(int i =0 ; i < h ; i++)
        {
            for(int j =0 ; j < w ; j++)
            {
                r = wRaster.getSample(j, i, 0);//RED
                g = wRaster.getSample(j, i, 1);//BLUE
                b = wRaster.getSample(j, i, 2);//GREEN
                r = 255 - r;
                g = 255 - g;
                b = 255 - b;
                wRaster.setSample(j, i, 0, r); //pixel[i][j].band(0) = r
                wRaster.setSample(j, i, 1, g);
                wRaster.setSample(j, i, 2, b);
            }
        }

        bImg.setData(wRaster);
        File trgt = new File(Path);
        ImageIO.write(bImg, "JPG", trgt);
    }
    void MaxPooling(int times) throws Exception
    {
        File img = new File(Path);
        if(!img.exists())
            throw new Exception(" image (" + Path + ") not found");
        BufferedImage bImg = ImageIO.read(img);
        int windowsize=times;
        int w = bImg.getWidth()-(windowsize+1);
        int h = bImg.getHeight()-(windowsize+1);
        WritableRaster wRaster = bImg.getRaster();
        int r=0 , g=0, b=0;
        for(int i =0 ; i < h ; i=i+windowsize) {
            for (int j = 0; j < w; j = j + windowsize) {
                r = 0;
                g = 0;
                b = 0;
                for (int k = 0; k < windowsize; k++) {
                    for (int l = 0; l < windowsize; l++) {

                        r = Math.max(r,wRaster.getSample(j + k, i + l, 0));

                        g = Math.max(g ,wRaster.getSample(j + k, i + l, 1));

                        b = Math.max(b,wRaster.getSample(j + k, i + l, 2));
                    }
                }
                //System.out.println(r +" " + g +" " +b );

                for (int k = 0; k < windowsize; k++) {
                    for (int l = 0; l < windowsize; l++) {
                        //System.out.println(r +" " + g +" " +b );
                        wRaster.setSample(j + k, i + l, 0, r);
                        wRaster.setSample(j + k, i + l, 1, g);
                        wRaster.setSample(j + k, i + l, 2, b);

                    }
                }

            }

        }
        bImg.setData(wRaster);
        File trgt = new File(Path);
        ImageIO.write(bImg, "JPG", trgt);
    }

    void GrayScale() throws Exception
    {
        File img = new File(Path);
        if(!img.exists())
            throw new Exception(" image (" + Path + ") not found");
        BufferedImage bImg = ImageIO.read(img);
        int w = bImg.getWidth();
        int h = bImg.getHeight();
        WritableRaster wRaster = bImg.getRaster();
        int r , g, b;
        for(int i =0 ; i < h ; i++)
        {
            for(int j =0 ; j < w ; j++)
            {
                r = wRaster.getSample(j, i, 0);//RED
                g = wRaster.getSample(j, i, 1);//BLUE
                b = wRaster.getSample(j, i, 2);//GREEN
                r = (r+g+b)/3;
                g = r ;
                b = r;
                wRaster.setSample(j, i, 0, r); //pixel[i][j].band(0) = r
                wRaster.setSample(j, i, 1, g);
                wRaster.setSample(j, i, 2, b);
            }
        }
        bImg.setData(wRaster);
        File trgt = new File(Path);
        ImageIO.write(bImg, "JPG", trgt);
    }
    void ReduceQuality(int times) throws Exception
    {
        File img = new File(Path);
        if(!img.exists())
            throw new Exception(" image (" + Path + ") not found");
        BufferedImage bImg = ImageIO.read(img);
        int windowsize=times;
        int w = bImg.getWidth()-(windowsize+1);
        int h = bImg.getHeight()-(windowsize+1);
        WritableRaster wRaster = bImg.getRaster();
        int r=0 , g=0, b=0;
        for(int i =0 ; i < h ; i=i+windowsize)
        {
            for(int j =0 ; j < w ; j=j+windowsize)
            {
                r=0;
                g=0;
                b=0;
                for(int k =0 ; k < windowsize; k++)
                {
                    for(int l=0; l < windowsize; l++)
                    {

                        r += wRaster.getSample(j+k, i+l, 0);

                        g += wRaster.getSample(j+k, i+l, 1);

                        b += wRaster.getSample(j+k, i+l, 2);
                    }
                }
                r = r /(windowsize*windowsize);
                g = g/ (windowsize*windowsize);
                b = b/ (windowsize*windowsize);

                for(int k =0 ; k < windowsize; k++)
                {
                    for(int l=0; l < windowsize; l++)
                    {
                        wRaster.setSample(j+k, i+l, 0, r);
                        wRaster.setSample(j+k, i+l, 1, g);
                        wRaster.setSample(j+k, i+l, 2, b);

                    }
                }

            }
        }
        bImg.setData(wRaster);
        File trgt = new File(Path);
        ImageIO.write(bImg, "JPG", trgt);
    }
    void BlurImage(int times)
    {

    }

}
