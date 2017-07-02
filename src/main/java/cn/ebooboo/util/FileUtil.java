package cn.ebooboo.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp3.MP3AudioHeader;
import org.jaudiotagger.audio.mp3.MP3File;

import cn.ebooboo.common.IOUtil;

public class FileUtil {

	private static final int BOM_LENGTH = 3;
	private static final long FILE_COPY_BUFFER_SIZE = 30*1024*1024;
	
    public static int getMp3TrackLength(File mp3File) {  
        try {  
            MP3File f = (MP3File) AudioFileIO.read(mp3File);  
            MP3AudioHeader audioHeader = (MP3AudioHeader)f.getAudioHeader();  
            int len = audioHeader.getTrackLength();
            f=null;
			return len;  
        } catch(Exception e) {  
            return 0;  
        }  
    }  
    
	public static boolean hasBoom(BufferedInputStream in) throws IOException {
		byte[] head = new byte[BOM_LENGTH];
		in.mark(BOM_LENGTH);
		in.read(head);
		in.reset();
		if (head[0] == -17 && head[1] == -69 && head[2] == -65) {
			return true;
		}
		return false;
	}
	
	public static List<String> loadFile(String fileName, String encoding) {
		File file = new File(fileName);
		if (!file.exists()) {
			throw new RuntimeException("File not found : " + fileName);
		}

		List<String> results = new ArrayList<String>();
		BufferedReader br = null;
		try {
			BufferedInputStream fis = new BufferedInputStream( new FileInputStream(file));
			if(hasBoom(fis)) {
				fis.skip(BOM_LENGTH);
			}
			br = new BufferedReader(new InputStreamReader(fis, encoding));
			// br = new BufferedReader(new FileReader(fileName));
			String line = br.readLine();
			if (line != null) {
				results.add(line);
			} else {
				return results;
			}

			while ((line = br.readLine()) != null) {
				results.add(line);
			}
			return results;
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					com.jfinal.kit.LogKit.error(e.getMessage(), e);
				}
			}
		}
	}

	public static String loadJsonFile(String fileName, String encoding) {
		List<String> lines = loadFile(fileName, encoding);
		StringBuffer sb = new StringBuffer();
		for (String line : lines) {
			sb.append(line.trim());
		}
		return sb.toString();
	}

	private static void doCopyFile(File srcFile, File destFile, boolean preserveFileDate) throws IOException {
		if (destFile.exists() && destFile.isDirectory()) {
			throw new IOException("Destination '" + destFile + "' exists but is a directory");
		}

		FileInputStream fis = null;
		FileOutputStream fos = null;
		FileChannel input = null;
		FileChannel output = null;
		try {
			fis = new FileInputStream(srcFile);
			fos = new FileOutputStream(destFile);
			input = fis.getChannel();
			output = fos.getChannel();
			long size = input.size();
			long pos = 0;
			long count = 0;
			while (pos < size) {
				count = size - pos > FILE_COPY_BUFFER_SIZE ? FILE_COPY_BUFFER_SIZE : size - pos;
				pos += output.transferFrom(input, pos, count);
			}
		} finally {
			IOUtil.closeQuietly(output);
			IOUtil.closeQuietly(fos);
			IOUtil.closeQuietly(input);
			IOUtil.closeQuietly(fis);
		}

		if (srcFile.length() != destFile.length()) {
			throw new IOException("Failed to copy full contents from '" + srcFile + "' to '" + destFile + "'");
		}
		if (preserveFileDate) {
			destFile.setLastModified(srcFile.lastModified());
		}
	}

	public static void copyFile(File srcFile, File destFile, boolean preserveFileDate) throws IOException {
		if (srcFile == null) {
			throw new NullPointerException("Source must not be null");
		}
		if (destFile == null) {
			throw new NullPointerException("Destination must not be null");
		}
		if (srcFile.exists() == false) {
			throw new FileNotFoundException("Source '" + srcFile + "' does not exist");
		}
		if (srcFile.isDirectory()) {
			throw new IOException("Source '" + srcFile + "' exists but is a directory");
		}
		if (srcFile.getCanonicalPath().equals(destFile.getCanonicalPath())) {
			throw new IOException("Source '" + srcFile + "' and destination '" + destFile + "' are the same");
		}
		File parentFile = destFile.getParentFile();
		if (parentFile != null) {
			if (!parentFile.mkdirs() && !parentFile.isDirectory()) {
				throw new IOException("Destination '" + parentFile + "' directory cannot be created");
			}
		}
		if (destFile.exists() && destFile.canWrite() == false) {
			throw new IOException("Destination '" + destFile + "' exists but is read-only");
		}
		doCopyFile(srcFile, destFile, preserveFileDate);
	}

	

    public static FileOutputStream openOutputStream(File file, boolean append) throws IOException {
        if (file.exists()) {
            if (file.isDirectory()) {
                throw new IOException("File '" + file + "' exists but is a directory");
            }
            if (file.canWrite() == false) {
                throw new IOException("File '" + file + "' cannot be written to");
            }
        } else {
            File parent = file.getParentFile();
            if (parent != null) {
                if (!parent.mkdirs() && !parent.isDirectory()) {
                    throw new IOException("Directory '" + parent + "' could not be created");
                }
            }
        }
        return new FileOutputStream(file, append);
    }
    
    public static void writeStringToFile(File file, String data, Charset encoding, boolean append) throws IOException {
        OutputStream out = null;
        try {
            out = openOutputStream(file, append);
            write(data, out, encoding);
            out.close(); // don't swallow close Exception if copy completes normally
        } finally {
            closeQuietly(out);
        }
    }
    
    public static void write(String data, OutputStream output, Charset encoding) throws IOException {
        if (data != null) {
            output.write(data.getBytes(Charsets.toCharset(encoding)));
        }
    }
    
    public static void closeQuietly(OutputStream output) {
        closeQuietly((Closeable)output);
    }
    public static void closeQuietly(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException ioe) {
            // ignore
        }
    }
}
