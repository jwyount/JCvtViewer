package datr.data.cvt;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.BufferedReader;

/**
 * Created by John on 3/20/2017.
 */
public class CVT
{
    int totalParms;     // Number of parameters in CVT.
    int cvtSize;        // Number of 32-bit words in CVT.
    String planeCode;
    String baseCvtFileName;
    String baseCvtDirectory = "C:\\CVT\\";

    CVTRECORD[] g_CVT;

    public int getCvtSize()
    {
        return cvtSize;
    }

    public String getPlaneCode()
    {
        return planeCode;
    }

    public void setPlaneCode(String planeCode)
    {
        this.planeCode = planeCode;
        this.baseCvtFileName = planeCode + ".cvt";
    }

    public int getTotalParms()
    {
        return totalParms;
    }

    public int CreateCVT()
    {
        File fCvt;
        String singleLine;
        int iCounter;
        String cvtFilePathString = baseCvtDirectory + baseCvtFileName;
        Path cvtFilePath = Paths.get(cvtFilePathString);

        try (BufferedReader reader = new Files.newBufferedReader(cvtFilePath, StandardCharsets.UTF_8))
        {
            while ((singleLine = reader.readLine()) != null)
            {
                // Ignore empty string.
                if (singleLine.isEmpty())
                    continue;

                // Convert all chars to upper case.
                singleLine.toUpperCase();
                // Remove leading and trailing white space.
                singleLine.trim();

                // Increment  number of parameters.
                ++totalParms;
            }
        }
        catch(IOException ex)
        {
            System.err.Format("IOException: %s%n", ex);
        }

        // Allocate an array of CVTRECORD objects.
        g_CVT = new CVTRECORD[totalParms];
        iCounter = 0;

        try (BufferedReader reader = new Files.newBufferedReader(cvtFilePath, StandardCharsets.UTF_8))
        {
            while ((singleLine = reader.readLine()) != null)
            {
                String tokens[] = singleLine.split(" ");

                g_CVT[iCounter] = new CVTRECORD(tokens[0].toUpperCase(), tokens[1].toUpperCase(),
                        tokens[2].toUpperCase(), tokens[3].toUpperCase());

                // Convert address from byte value to word value
                long temp = g_CVT[iCounter].getAddress() / 4;
                g_CVT[iCounter].setAddress(temp);

                // Set last CVT location/size.
                if (g_CVT[iCounter].getAddress() > cvtSize)
                    cvtSize = (int)g_CVT[iCounter].getAddress();

                ++iCounter;

            }
        }
        catch(IOException ex)
        {
            System.err.Format("IOException: %s%n", ex);
        }
    }

}
