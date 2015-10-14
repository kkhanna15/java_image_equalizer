/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ImageEqualizer;

import java.awt.image.*;
import java.awt.*;
import java.io.IOException;
import java.util.Vector;
import javax.swing.*;
import com.pearsoneduc.ip.io.*;
import com.pearsoneduc.ip.gui.*;
import com.pearsoneduc.ip.op.OperationException;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.io.File;
import javax.imageio.ImageIO;



/**
 *
 * @author kanvikhanna
 */
public class ImageEqualizer extends JFrame{
    
    private BufferedImage image;
    private ViewWithROI view;

    public ImageEqualizer(String imageFile) 
            throws IOException, ImageDecoderException, OperationException 
    {
        super(imageFile);
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if (args.length > 0) {
      try {
        //ImageEqualizer frame = new ImageEqualizer(args[0]);
        //frame.pack();
        //frame.setVisible(true);
        MyImage testImage = new MyImage(args[0]);
        testImage.display( new Point(0,0), args[0] );
        Histogram testHist = new Histogram(args[0]);
        testHist.display(new Point(500, 0), "HistImg");
        testHist.Save("HistImg");
        testImage.equalize();       
        testImage.Save("EqualizedImg");
        MyImage equalizedImage = new MyImage("EqualizedImg.jpg");
        equalizedImage.display(new Point(0, 500), "EqualizedImg.jpg");
        Histogram eqHist = new Histogram("EqualizedImg.jpg");
        eqHist.display(new Point(500, 500), "EqualizedImgHist");     
        eqHist.Save("EqualizedImgHist");
        
      }
      catch (Exception e) {
        System.err.println(e);
        System.exit(1);
      }
    }
    else {
      System.err.println("usage: java K_histogram <imagefile>");
      System.exit(1);
    }
  }
}
    
