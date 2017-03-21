package datr.data.cvt;

import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

//import com.sun.jna.Platform;
//import com.sun.jna.Library;
//import com.sun.jna.Native;

/**
 * Created by John on 2/13/2017.
 */
public class SharedMemoryCvt
{
    String strFileName = "_SIL";;
    int lg_iShrMemID;       /* Shared memory ID */
    //uint   *lg_puiShrMemData;  /* Pointer to memory-mapped data */
    RandomAccessFile memoryMappedFile = null;

    public enum eSIL_Status
    {
        SIL_ERROR,
        SIL_OK
    };

    /*
    #ifdef WIN32
        HANDLE          lg_hMapHandle;      */
    /* Handle to memory-mapped file *//*

        HANDLE          lg_hMemMap;         */
    /* Handle to memory-mapped area *//*

    #endif
    */

    public int ShrMemOpen()
    {

        /* Create memory-mapped file if one doesn't exist */
        //lg_hMapHandle = CreateFileMapping( (HANDLE) 0xFFFFFFFF,
        //        NULL,
        //        PAGE_READWRITE,
        //        0,
        //        silGetCVTSize() * sizeof( SILDATA),
        //        szMapName );

        //int fileSizeBytes
        try
        {
            memoryMappedFile = new RandomAccessFile(strFileName, "rw");
        }
        catch (FileNotFoundException fnfEx)
        {
            return (eSIL_Status.SIL_ERROR.ordinal());
        }

        //memoryMappedFile.getChannel().map(FileChannel.MapMode.READ_WRITE,

//        if ( lg_hMapHandle != NULL ) {
//            if ( GetLastError() == ERROR_ALREADY_EXISTS ) {
//                CloseHandle( lg_hMapHandle );
//                lg_hMapHandle = NULL;
//            }
//        } else {
//            silSetLastError( "Unable to create file mapping" );
//            return( SIL_ERROR );
//        }

    /* See if a memory-mapped file named _SIL already exists. */
//        lg_hMemMap = OpenFileMapping( FILE_MAP_READ | FILE_MAP_WRITE,
//                FALSE,
//                szMapName );
//        if ( ! lg_hMemMap ) {
//            silSetLastError( "Unable to open file mapping" );
//            return( SIL_ERROR );
//        }

    /*
     * Memmory-mapped file does exist.  Map a view of it into
     * the process's address space.
     */
//        lg_puiShrMemData = MapViewOfFile( lg_hMemMap,
//                FILE_MAP_READ | FILE_MAP_WRITE,
//                0,
//                0,
//                silGetCVTSize() * sizeof( SILDATA ) );

        return( eSIL_Status.SIL_OK.ordinal() ) ;
    }

    int ShrMemClose( void )
    {

    /* Unmap the view */
//        if ( lg_puiShrMemData ) {
//            UnmapViewOfFile( lg_puiShrMemData );
//            lg_puiShrMemData = NULL;
//        }


    /* Close file mapping handle */
        if ( lg_hMemMap ) {
            CloseHandle( lg_hMemMap );
            lg_hMemMap = NULL;
        }

    /* Close file mapping handle */
        if ( lg_hMapHandle ) {
            CloseHandle( lg_hMapHandle );
            lg_hMapHandle = NULL;
        }

#else

    /* Remove shared memory from system */
    /*if ( shmctl( lg_iShrMemID, IPC_RMID, 0 ) < 0 ) {*/
    /* Detach shared memory from system */
        if ( shmdt( (unsigned int *) lg_puiShrMemData ) < 0 ) {
        silSetLastError( "shmdt error" );
        return( SIL_ERROR );
    }

#endif

        return( SIL_OK );
    }

    int ShrMemPeek( int iAddr, SILDATA *Data )
    {
    /* Peek */
        //Data->ui = lg_puiShrMemData[iAddr];
        //Data->ui = g_ByteSwappedData[iAddr].ui;
        Data->ui = silData[iAddr].ui;

        return( SIL_OK );
    }

    int ShrMemPoke( int iAddr, SILDATA Data )
    {
    /* Poke */
        //lg_puiShrMemData[iAddr] = Data.ui;
        silData[iAddr].ui = Data.ui;

        return( SIL_OK );
    }

    int ShrMemRead( void )
    {
        memcpy( silData, lg_puiShrMemData, silGetCVTSize() * sizeof( SILDATA ) );
        //memcpy( lg_puiShrMemData, g_ByteSwappedData, silGetCVTSize() * sizeof( SILDATA ) );

        return( SIL_OK );
    }

    int ShrMemWrite( void )
    {
        //memcpy( g_ByteSwappedData, lg_puiShrMemData, silGetCVTSize() * sizeof( SILDATA ) );
        memcpy( lg_puiShrMemData, g_ByteSwappedData, silGetCVTSize() * sizeof( SILDATA ) );

        return( SIL_OK );
    }


}
