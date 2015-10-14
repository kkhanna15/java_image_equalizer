package ImageEqualizer;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.awt.image.*;
import java.awt.*;        
import java.io.IOException;
import javax.swing.*;
import java.io.File;
import javax.imageio.ImageIO;

/**
 * This class models a Histogram.
 * A histogram object must be created with an input JPG file whose histogram
 * it will represent.
 * It can be used to save histogram as a JPG file, as well as to display the
 * histogram on screen.
 * @author kanvikhanna
 */
public class Histogram extends JFrame {
    
    /**
     * attributes
     */
    /**
     * Histogram is developed in a raw form as an integer array.
     */
    private int[] hist = new int[256];    
    private final int height_hist;
    private final int width_hist;
    

    /**
     * 2D image representation of raw histogram.
     */    
    private int[][] hist_img_rep;
    
    
    /**
     * Methods
     */
    
    /**
     * Creates a Histogram object using a JPG file as input, whose histogram 
     * needs to be created.
     * @param imageFile JPG file whose histogram is desired. 
     */
    public Histogram( String imageFile )
    {
        super( imageFile );
        height_hist = 256;
        width_hist = 512;
        hist_img_rep = new int[width_hist][height_hist];
        
        try { 
            calHist( imageFile ); 
        } 
        catch(Exception e){
            System.out.print(e.toString());
        }
    } // end of method
    
    /**
     * Saves the histogram as a JPG file.
     * @param saveImgAs file-name to be used to save the file. Must be a
     * JPG type of name.
     */
    public void Save( String saveImgAs )
    {
        BufferedImage imgHist = 
            new BufferedImage(
                width_hist, 
                height_hist, 
                BufferedImage.TYPE_BYTE_GRAY
            );

        WritableRaster imgHistRaster = (WritableRaster)imgHist.getRaster();

        for( int j = 0; j < height_hist; ++j)
           for(int i = 0; i < width_hist; ++i )       
               imgHistRaster.setSample( i, j, 0, hist_img_rep[i][j] );

        imgHist.setData(imgHistRaster);
        saveImage(imgHist, saveImgAs);
            
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
        BufferedImage imgHist = 
            new BufferedImage(
                width_hist, 
                height_hist, 
                BufferedImage.TYPE_BYTE_GRAY
            );

        WritableRaster imgHistRaster = (WritableRaster)imgHist.getRaster();

        for( int j = 0; j < height_hist; ++j)
           for(int i = 0; i < width_hist; ++i )       
               imgHistRaster.setSample( i, j, 0, hist_img_rep[i][j] );

        imgHist.setData(imgHistRaster);
        DisplayImage(imgHist, where, title );
        
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
     * Helper function to read the input image file, and create a histogram
     * out of it.
     */
    private void calHist( String imageFile ) throws IOException
    {
        // convert imageFile into a BufferedImage object
        
        BufferedImage img = ImageIO.read(new File(imageFile));
        if( img.getType() != BufferedImage.TYPE_BYTE_GRAY )
        {
            System.out.println("Image not Gray scale. Exiting...");
            System.exit(0);
        }
        
        Raster raster = img.getRaster();
        //int[] hist = new int[256];

        int i, j;
        int nH;
        int height_img = img.getHeight();
        int width_img = img.getWidth();

        for( i = 0; i < 256; i++)
            hist[i] = 0;

        for( j=0; j < height_img; ++j)
            for(i=0; i < width_img; ++i)
                hist[raster.getSample(i,j,0)] = hist[raster.getSample(i,j,0)]+1;

        int highest = hist[0];
        for(i = 1; i < 256; i++)
        {
            //System.out.println(hist[i]);
            if(hist[i] > highest)
                highest = hist[i];
        }
             
        for(j = 0; j < height_hist; ++j )
          for( i = 0; i < width_hist; ++i)
                hist_img_rep[i][j] = 255;

        int k = 0;
        for( i = 0; i < width_hist; i = i + 2)
        { 
            nH = (int)(hist[k] * ((float)height_hist/(float)highest));
            for( j = height_hist-1; j > (height_hist-1 - nH); --j)
            {
                hist_img_rep[i][j] = 0;
                //hist_img_rep[i+1][j] = 0;
            } 
            k = k+1;      
        } 
        
    } // end of method
    
} // end of class definition
