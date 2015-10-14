/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ImageEqualizer;

import java.awt.image.*;
import java.awt.*;        
import java.io.IOException;
import javax.swing.*;
import java.io.File;
import javax.imageio.ImageIO;

/**
 *
 * @author kanvikhanna
 */
public class MyImage extends JFrame {

    /**
     * 
     */
    private BufferedImage image;
    
    /**
     * 
     * @param imageFile
     * @throws IOException 
     */
    public MyImage( String imageFile ) throws IOException
    {
        super( imageFile );
        
        // convert imageFile into a BufferedImage object
        
        image = ImageIO.read(new File(imageFile));
        if( image.getType() != BufferedImage.TYPE_BYTE_GRAY )
        {
            System.out.println("Image not Gray scale. Exiting...");
            System.exit(0);
        }
        
    } // end of method
    
    /**
     * Saves this image as a JPG file.
     * @param saveImgAs file-name to be used to save the file. Must be a
     * JPG type of name.
     */
    public void Save( String saveImgAs )
    {
        saveImage(image, saveImgAs);            
    } // end of method

    /**
     * Helper method to save image.
     * @param newImg
     * @param fileName 
     */ 
    private void saveImage(BufferedImage newImg, String fileName)
    {

        File imageFile = new File(fileName+".jpg");
        try
        {
        ImageIO.write(newImg,"jpg",imageFile);
        }
        catch (IOException e)
        {
            System.out.println(fileName+" not saved"+e);
        }

    } // end of method
  
    /**
     * Display this histogram on screen.
     * @param where
     * @param title 
     */
    public void display(Point where, String title)
    {
        DisplayImage(image, where, title );        
    } // end of method

    // Display in Jframe
    private void DisplayImage(BufferedImage img, Point where, String title )
    {
        JPanel panel=new JPanel();
        panel.add(new JLabel(new ImageIcon(img)));
        JFrame frame=new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setBounds(where.x,where.y,img.getWidth(), img.getHeight());
        frame.setContentPane(panel);
        frame.pack();
        frame.setVisible(true);
    }
  
    /**
     * 
     * @throws IOException 
     */
    public void equalize() throws IOException
    {
        int in_imgWidth = image.getWidth();
        int in_imgHeight = image.getHeight();
        Raster in_imgRaster = image.getRaster();

        float L = 256;
        float pix = in_imgWidth * in_imgHeight;
        int i, j;
        int[] h = new int[256];
        float[] c = new float[256];

        float scalingFactor =  L / pix;
        //System.out.println(pix+"\t"+scalingFactor+"\n");
        for( i = 0; i < 256; i++)
        {
            h[i] = 0;
            c[i] = 0;
        }

        for( j = 0; j < in_imgHeight; ++j)
            for(i = 0; i < in_imgWidth; ++i)
                h[in_imgRaster.getSample(i,j,0)] = 
                        h[in_imgRaster.getSample(i,j,0)]+1;

        c[0] = scalingFactor * h[0];
        //System.out.println(h[0]+"\t"+c[0]+"\n");
        for( i = 1; i < 256; i++)
        {
            c[i] = c[i-1] + scalingFactor * h[i];
            //System.out.println(h[i]+"\t"+c[i]+"\n");
        }

        BufferedImage out_img = new BufferedImage(in_imgWidth, in_imgHeight, 
        image.getType());

        WritableRaster out_imgRaster = (WritableRaster)out_img.getRaster();

        for(j = 0; j < in_imgHeight; ++j)
            for(i = 1; i < in_imgWidth; ++i)
                out_imgRaster.setSample(i, j, 0, 
                        c[in_imgRaster.getSample(i, j, 0)]);

        out_img.setData(out_imgRaster);

        image = out_img;
        
    } // end of method    
}
