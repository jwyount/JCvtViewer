package datr.data.cvt;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
// Events
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
// Files
import java.io.BufferedReader;
import java.io.FileFilter;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.File;

import java.awt.event.KeyEvent;
import java.util.Scanner;

enum OS_TYPE
{
    Unknown(0),
    WINDOWS(1),
    UNIX(2),
    OTHER(100);

    private int value;

    private OS_TYPE(int value)
    {
        this.value = value;
    }
}

/**
 * Created by John on 2/4/2017.
 */
public class CvtViewer
{
    private JButton btnUpdate;
    private JPanel panelMain;

    JMenuBar menuBar;
    JMenu menuFile, menuHelp;
    JMenuItem menuItem_openCvt, menuItem_openSharedMemory;
    JFrame frame;

    int lg_iTotalParms;
    CVTRECORD g_CVT[];

    public OS_TYPE currentOs = OS_TYPE.Unknown;

    public static void main(String[] args)
    {
        CvtViewer theApp = new CvtViewer();

        String osName = System.getProperty("os.name");
        if (osName.startsWith("Windows"))
            theApp.currentOs = OS_TYPE.WINDOWS;
        else
            theApp.currentOs = OS_TYPE.UNIX;

        theApp.frame.setVisible(true);
    }


    public CvtViewer()
    {
        // Create and set properties for the main-frame.
        frame = new JFrame("App");
        frame.setContentPane(panelMain);
        frame.setSize(new Dimension(600, 480));
        frame.setLocation(500, 500);
        //frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create the menuBar.
        menuBar = new JMenuBar();
        // Add the the menu bar containing all the menu and menu sub-items
        // to the form.
        frame.setJMenuBar(menuBar);

        ///////////////////
        // File Menu
        ///////////////////
        menuFile = new JMenu("File");
        menuFile.setMnemonic(KeyEvent.VK_F);
        //menuFile.addMenuListener(new menuListener());
        menuBar.add(menuFile);

        // File - MenuItem: Open CVT
        menuItem_openCvt = new JMenuItem("Open CVT");
        menuItem_openCvt.addActionListener(new menuListener());

        menuFile.add(menuItem_openCvt);


        btnUpdate.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent actionEvent)
            {
                JFileChooser openFileDlg = new JFileChooser();

                java.nio.file.Path cvtPath = Paths.get("C:\\CVT");
                if (Files.exists(cvtPath))
                {
                    openFileDlg.setCurrentDirectory(new File(cvtPath.toString()));
                }

                int choice = openFileDlg.showOpenDialog(panelMain);
            }
        });

    }

    public boolean OpenSharedMemoryToRead();
    {

    }

    public boolean CreateCVT(File cvtFile)
    {
        char            szTmpBuf[];         /* Temporary string buffer */
        char            szData[];           /* Data string buffer */
        int             iCounter;           /* Generic counter */

        /*
         * Open CVT file in /usr/CVT for UNIX machines or C:\CVT for NT machines.
         * If it fails, open it locally.
         */
        String fn = cvtFile.getAbsolutePath();
        FileReader aRdr;

        try
        {
            aRdr = new FileReader(cvtFile);
        }
        catch (Exception ex)
        {
            return false;
        }

        BufferedReader myRdr = new BufferedReader(aRdr);
        //BufferedReader myRdr = new BufferedReader(fn);

        /*
         * Count the number of parameters in CVT
         */
        lg_iTotalParms = 0;
        //while ( fgets( szTmpBuf, sizeof( szTmpBuf ), fpCVT ) != NULL ) {
        try
        {
            while (myRdr.readLine() != null)
            {
                /*
                 * Convert all characters to uppercase
                 * and removing trailing spaces
                 */
                /*silStrUpper( szTmpBuf );
                silStrRmLeadSpace( szTmpBuf );
                silStrRmTrailSpace( szTmpBuf );*/

                /* Ignore empty string */
                //if ( ( szTmpBuf[0] == (char) NULL ) || ( szTmpBuf[0] == '_' ) )
                //    continue;

                /* Increment number of parameters */
                ++lg_iTotalParms;
            }
        }
        catch (java.io.IOException ioEx)
        {
            return false;
        }

        /*
         * Rewind to beginning of the file
         */
        //rewind( fpCVT );
        try
        {
            aRdr = new FileReader(cvtFile);
            myRdr = new BufferedReader(aRdr);  // Just create a new BufferReader.
        }
        catch (Exception ex)
        {
            return false;
        }

        /*
         * Allocate an array of CVT structure
         */
        //g_CVT = ( CVTRECORD * ) calloc( lg_iTotalParms, sizeof( CVTRECORD ) );
        g_CVT = new CVTRECORD[lg_iTotalParms];

        /*if ( ! g_CVT ) {
            silSetLastError( "Out of memory (array of CVT structure)" );
            fclose( fpCVT );    *//* Close CVT file *//*
            return ( SIL_ERROR );
        }*/

        /*
         * Get each line from CVT file, parse it,
         * then store information in an array of structure.
         */
        iCounter = 0;
        String newLine;

        //while ( fgets( szTmpBuf, sizeof( szTmpBuf ), fpCVT ) != NULL ) {
        try
        {
            while ((newLine = myRdr.readLine()) != null)
            {
            /*
             * Convert all characters to uppercase
             * and removing trailing spaces
             */
                newLine = newLine.toUpperCase();
                newLine = newLine.trim();

                /* Ignore empty string */
                if (newLine.isEmpty() == true)
                    continue;

            /* Parse CVT record */
                // sscanf( szTmpBuf, "%s %s %x %s", g_CVT[iCounter].szName,
                //        g_CVT[iCounter].szDataType, &g_CVT[iCounter].uiAddress,
                //        g_CVT[iCounter].szSource );
                Scanner lnScanner = new Scanner(newLine);
                lnScanner.useRadix(16);

                CVTRECORD cvtRecord = new CVTRECORD();

                cvtRecord.szName = lnScanner.next();
                cvtRecord.szDataType = lnScanner.next();
                cvtRecord.address = lnScanner.nextInt();
                cvtRecord.szSource = lnScanner.next();

                // CVT file address is byte offset so convert to 32-bit word.
                // Relative to word location.
                cvtRecord.address /= 4;

                g_CVT[iCounter] = cvtRecord;



                // Set last CVT location
                //if ( g_CVT[iCounter].address > uiCVTSize )
                //    lg_uiCVTSize =  g_CVT[iCounter].uiAddress;

                iCounter++;
            }

        }
        catch (java.io.IOException ioEx)
        {
            return false;
        }

        /* Close file */
        //myRdr.close();

        return( true );
    }

    class menuListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            int id = e.getID();
            String strSrc = e.getActionCommand();
            boolean fileRdrSuccess = false;

            if (strSrc.compareTo(menuItem_openCvt.getText()) == 0)
            {

                JFileChooser openFileDlg = new JFileChooser();

                java.nio.file.Path cvtPath = Paths.get("C:\\CVT");
                if (Files.exists(cvtPath))
                {
                    openFileDlg.setCurrentDirectory(new File(cvtPath.toString()));
                }

                FileNameExtensionFilter fileFilter = new FileNameExtensionFilter("CVT File", "cvt");
                openFileDlg.setFileFilter(fileFilter);

                int choice = openFileDlg.showOpenDialog(frame);

                if (choice == JFileChooser.APPROVE_OPTION)
                {
                    //JOptionPane.showMessageDialog(null, "File selected: \"" + openFileDlg.getSelectedFile() + "\"",
                    //        "Information", JOptionPane.INFORMATION_MESSAGE);

                    File filePath = openFileDlg.getSelectedFile();

                    fileRdrSuccess = CreateCVT(filePath);

                }

                JOptionPane.showMessageDialog(null, "CVT File read operation is successful: " +
                    fileRdrSuccess);

            }
        }

    }
}

