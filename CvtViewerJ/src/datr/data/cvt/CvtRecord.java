package datr.data.cvt;

import java.util.Comparator;

/**
 * Created by John on 3/20/2017.
 */
public class CvtRecord
{
    private String _parmName;
    private String _dataType;
    private long _address;
    private String _source;

    public String getDataType()
    {
        return _dataType;
    }

    public String getParmName()
    {
        return _parmName;
    }

    public long getAddress()
    {
        return _address;
    }

    public void setAddress(long newAddress)
    {
        _address = newAddress;
    }

    public String getSource()
    {
        return _source;
    }


    public CvtRecord(String parmName, String dataType, String address, String source)
    {
        _parmName = parmName;
        _dataType = dataType;

        try
        {
            _address = Long.parseLong(address);
        }
        catch (NumberFormatException nfEx)
        {
            String badAddress = "0xFFFFF";
            _address = Long.decode(badAddress);
        }

        _source = source;
    }

    public CvtRecord (String parmName, String dataType, long address, String source)
    {
        _parmName = parmName;
        _dataType = dataType;
        _address = address;
        _source = source;
    }

    static Comparator<CvtRecord> getParmNameComparator()
    {
        return new Comparator<CvtRecord>()
        {

        };
    }
    // Work here on comparator(s) - JY - 03202017
    static final static int CompareName(CvtRecord parm1, CvtRecord parm2)
    {
        String p1 = parm1.getParmName();
        String p2 = parm2.getParmName();

        return p1.compareToIgnoreCase(p2);
    }

    public static int CompareAddress(CvtRecord parm1, CvtRecord parm2)
    {
        long p1 = parm1.getAddress();
        long p2 = parm2.getAddress();

        if (p1 < p2)
            return -1;
        else if (p1 > p2)
            return 1;
        else
            return 0;
    }
}
