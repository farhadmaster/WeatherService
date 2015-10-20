package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;



import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.utils.IOUtils;

import com.google.inject.Singleton;

@Singleton
public final class ZipUtil extends AbstractTestCase { 
 
	 public void unZipIt(InputStream in) throws IOException {
		 in.available();
	      ZipInputStream zin = new ZipInputStream(in);
	      ZipEntry ze = null;
	      try {
			while ((ze = zin.getNextEntry()) != null) {
			    System.out.println("Unzipping " + ze.getName());
			    FileOutputStream fout = new FileOutputStream(ze.getName());
			    for (int c = zin.read(); c != -1; c = zin.read()) {
			      fout.write(c);
			    }
			    zin.closeEntry();
			    fout.close();
			   
			  }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	      zin.close();
	    }
    	
    public void testBzipCreation()  throws Exception { 
        File output = null; 
        final File input = getFile("test.txt"); 
        { 
            output = new File(dir, "test.txt.bz2"); 
            final OutputStream out = new FileOutputStream(output); 
            final CompressorOutputStream cos = new CompressorStreamFactory().createCompressorOutputStream("bzip2", out); 
            FileInputStream in = new FileInputStream(input); 
            IOUtils.copy(in, cos); 
            cos.close(); 
            in.close(); 
        } 
 
        final File decompressed = new File(dir, "decompressed.txt"); 
        { 
            final File toDecompress = output; 
            final InputStream is = new FileInputStream(toDecompress); 
            final CompressorInputStream in = 
                new CompressorStreamFactory().createCompressorInputStream("bzip2", is); 
            FileOutputStream os = new FileOutputStream(decompressed); 
            IOUtils.copy(in, os); 
            is.close(); 
            os.close(); 
        } 
 
        assertEquals(input.length(),decompressed.length()); 
    } 
 
    public void testBzip2Unarchive() throws Exception { 
        final File input = getFile("weeronl20150903M02_999257.csv.bz2"); 
        final File output = new File(dir, "bla.txt"); 
        final InputStream is = new FileInputStream(input); 
        final CompressorInputStream in = new CompressorStreamFactory().createCompressorInputStream("bzip2", is); 
        FileOutputStream os = new FileOutputStream(output); 
        IOUtils.copy(in, os); 
        is.close(); 
        os.close(); 
    } 
 
    public void testConcatenatedStreamsReadFirstOnly() throws Exception { 
        final File input = getFile("multiple.bz2"); 
        final InputStream is = new FileInputStream(input); 
        try { 
            final CompressorInputStream in = new CompressorStreamFactory() 
                .createCompressorInputStream("bzip2", is); 
            try { 
                assertEquals('a', in.read()); 
                assertEquals(-1, in.read()); 
            } finally { 
                in.close(); 
            } 
        } finally { 
            is.close(); 
        } 
    } 
 
    public void testConcatenatedStreamsReadFully() throws Exception { 
        final File input = getFile("multiple.bz2"); 
        final InputStream is = new FileInputStream(input); 
        try { 
            final CompressorInputStream in = 
                new BZip2CompressorInputStream(is, true); 
            try { 
                assertEquals('a', in.read()); 
                assertEquals('b', in.read()); 
                assertEquals(0, in.available()); 
                assertEquals(-1, in.read()); 
            } finally { 
                in.close(); 
            } 
        } finally { 
            is.close(); 
        } 
    } 
 
    public void testCOMPRESS131() throws Exception { 
        final File input = getFile("COMPRESS-131.bz2"); 
        final InputStream is = new FileInputStream(input); 
        try { 
            final CompressorInputStream in = 
                new BZip2CompressorInputStream(is, true); 
            try { 
                int l = 0; 
                while(in.read() != -1) { 
                    l++; 
                } 
                assertEquals(539, l); 
            } finally { 
                in.close(); 
            } 
        } finally { 
            is.close(); 
        } 
    } 
 
}
