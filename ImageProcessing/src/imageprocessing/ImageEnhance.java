/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imageprocessing;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.util.Iterator;
import java.util.concurrent.Callable;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

/**
 *
 * @author sakamoto
 */
public class ImageEnhance implements Callable<Integer> {
  
    private String Path;
    private boolean flag=false;
    private int exte;
    private int choi;
    private int w, h ;
    private BufferedImage bImg;
    private File img , trgt;
    private WritableRaster wRaster;
    private String ImageType() throws Exception
    {
        ImageInputStream iis = ImageIO.createImageInputStream(img);
        Iterator<ImageReader> iter = ImageIO.getImageReaders(iis);
        if (!iter.hasNext()) {
        throw new RuntimeException("Bhai Image Kaa Pura Naam daal with Extension");
        }
        ImageReader reader = iter.next();
        iis.close();
        return reader.getFormatName();
 
    }
    private void LoadImage ()throws Exception
    {
        img = new File(Path);
        if(!img.exists())
            throw new Exception(FileNotFound ());
         bImg = ImageIO.read(img);
         w = bImg.getWidth();
         h = bImg.getHeight();
        wRaster= bImg.getRaster();
    }
    
    private void SaveImage() throws Exception
    {
        bImg.setData(wRaster);
         trgt = new File(Path);
        ImageIO.write(bImg,  ImageType(),trgt);
    }
    public void Browse(String path,int cho, int ext) throws Exception
    {
        this.Path=path;
        flag=true;
        this.choi=cho;
        this.exte=ext;
        this.call();
    }
        
    private boolean caller(int choice,int extent) throws Exception
    {
        if(flag==true)
        {
            switch (choice)
            {
                case 1:
                    this.NegativeOfAnImage();
                    break;
                case 2:
                    this.MaxPooling(extent);
                    break;
                case 3:
                    this.GrayScale();
                    break;
                case 4:
                    this.ReduceQuality(extent);
                case 5 :
                    this.BlurImage(extent);
                    break;
                case 6 :
                    this.MirrorImage();
                    break;
                case 7 :
                    this.ChangeBrightNess(extent);
                    break;
                case 8 :
                    this.ChangeContrast(extent);
                    break;
                    default : throw(new Exception("Option To Select Kar Bhai"));
            }
            return true;
        }
        else
        {
            return false;
        }
    }
   private  void NegativeOfAnImage() throws Exception
    {
        LoadImage();
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
        SaveImage();
        
    }
    private void MaxPooling(int times) throws Exception
    {
        LoadImage();
        int windowsize=times;
        int r=0 , g=0, b=0;
        for(int i =0 ; i < h ; i=i+windowsize)
        {
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
        SaveImage();
    }

   private  void GrayScale() throws Exception
    {
        LoadImage();
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
       SaveImage();
    }
    private void ReduceQuality(int times) throws Exception
    {
        LoadImage();
        int windowsize=times;
        int r , g, b;
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
       SaveImage();
    }
   private void MirrorImage() throws Exception
   {
       LoadImage();
       int r , b ,g ,r1,g1,b1;
       for(int i =0 ; i < h ; i++)
       {
           for(int j=0; j < w/2; j++ )
           {
                r  = wRaster.getSample(j, i, 0);//RED
                g  = wRaster.getSample(j, i, 1);//BLUE
                b  = wRaster.getSample(j, i, 2);//GREEN
                r1 = wRaster.getSample(w-1-j, i, 0);//RED
                g1 = wRaster.getSample(w-1-j, i, 1);//BLUE
                b1 = wRaster.getSample(w-1-j, i, 2);//GREEN
                wRaster.setSample(j, i, 0, r1); //pixel[i][j].band(0) = r
                wRaster.setSample(j, i, 1, g1);
                wRaster.setSample(j, i, 2, b1);
                wRaster.setSample(w-1-j, i, 0, r); //pixel[i][j].band(0) = r
                wRaster.setSample(w-1-j, i, 1, g);
                wRaster.setSample(w-1-j, i, 2, b);
           }
       }
       SaveImage();
       
   }
   private void BlurImage(int times) throws Exception
    {
        throw (new Exception("ABHI Tak Banaya Nahi H Bhai\n"));
    }
   private void ChangeBrightNess(int times) throws Exception
   {
       //System.out.println(times);
       LoadImage();
       int r , g, b;
        for(int i =0 ; i < h ; i++)
        {
            for(int j =0 ; j < w ; j++)
            {
                r = wRaster.getSample(j, i, 0);//RED
                g = wRaster.getSample(j, i, 1);//BLUE
                b = wRaster.getSample(j, i, 2);//GREEN
                r = Math.min(r+times,254);
                g = Math.min(g+times,254) ;
                b = Math.min(b+times,254);
                wRaster.setSample(j, i, 0, r); //pixel[i][j].band(0) = r
                wRaster.setSample(j, i, 1, g);
                wRaster.setSample(j, i, 2, b);
            }
        }
       SaveImage();
   }
   private void ChangeContrast(int times) throws Exception
   {
       throw (new Exception("ABHI Tak Banaya Nahi H Bhai\n"));
   }
    @Override
    public Integer call() throws Exception 
    {
            this.caller(this.choi, this.exte); 
        return 0;
    }
   private String FileNotFound ()
    {
        return "O Bhai\n Path Galat H";
    }
    

}
    

