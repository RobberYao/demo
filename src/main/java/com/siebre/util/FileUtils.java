package com.siebre.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Common file utility methods.
 */
public class FileUtils {
	/** Default buffer size. */
	public static final int DEFAULT_BUFFER = 1024 * 4;

	/** prevent instantiation */
	private FileUtils() {
	}

	/**
	 * Copy the source file to the destination file. Neither file can be a
	 * directory.
	 * 
	 * @param aSource
	 *            The source file.
	 * @param aDestination
	 *            The destination file.
	 * @throws IOException
	 */
	public static void copyFile(File aSource, File aDestination)
			throws IOException {
		if (!aSource.isFile()) {
			throw new IOException(aSource + " is not a valid file.");
		}

		FileInputStream input = new FileInputStream(aSource);

		try {
			FileOutputStream output = new FileOutputStream(aDestination);
			try {
				FileUtils.copy(input, output);
			} finally {
				output.close();
			}
		} finally {
			input.close();
		}

		if (aSource.length() != aDestination.length()) {
			throw new IOException("Failed to copy " + aSource + " to "
					+ aDestination);
		}
	}

	/**
	 * Read the contents of the input stream and write them to the output
	 * stream. Uses the default buffer size.
	 * 
	 * @param aIn
	 * @param aOut
	 * @throws IOException
	 */
	public static void copy(InputStream aIn, OutputStream aOut)
			throws IOException {
		copy(aIn, aOut, DEFAULT_BUFFER);
	}

	/**
	 * Read the contents of the input stream and write them to the output
	 * stream. Uses the given buffer size.
	 * 
	 * @param aIn
	 * @param aOut
	 * @param aBufferSize
	 *            The buffer size to use for reading.
	 * @throws IOException
	 */
	public static void copy(InputStream aIn, OutputStream aOut, int aBufferSize)
			throws IOException {
		byte[] buffer = new byte[aBufferSize];
		int read = 0;
		while (-1 != (read = aIn.read(buffer))) {
			aOut.write(buffer, 0, read);
		}
	}

	/**
	 * Recursivelt deletes the given directory and all of its nested contents.
	 * Use carefully!
	 * 
	 * @param aDirectory
	 */
	public static void recursivelyDelete(File aDirectory) {
		if (!aDirectory.isDirectory()) {
			return;
		}

		File[] files = aDirectory.listFiles();

		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				if (files[i].isFile()) {
					files[i].delete();
				} else {
					recursivelyDelete(files[i]);
				}
			}
		}
		aDirectory.delete();
	}

	/**
	 * Copies a given JAR entry to the given output stream.
	 * 
	 * @param aIn
	 *            The JAR input stream.
	 * @param aOut
	 *            The output stream to write to.
	 * @throws IOException
	 */
	protected static void copyEntry(JarInputStream aIn, OutputStream aOut)
			throws IOException {
		try {
			FileUtils.copy(aIn, aOut);
		} finally {
			try {
				aIn.closeEntry();
			} catch (IOException io) {
				io.printStackTrace();
			}
		}
	}

	/**
	 * Unpacks the contents of a given JAR file to the given target directory.
	 * 
	 * @param aFile
	 *            The source JAR file.
	 * @param aTargetDir
	 *            The target directory to unpack into.
	 * @throws IOException
	 */
	public static void unpackJar(File aFile, File rootDir) throws IOException {
		rootDir.mkdirs();

		JarInputStream in = null;
		try {
			in = new JarInputStream(new FileInputStream(aFile));
			JarEntry entry = in.getNextJarEntry();
			while (entry != null) {
				FileOutputStream out = null;
				try {
					if (!entry.isDirectory()) {
						File newFile = new File(rootDir, entry.getName());
						newFile.getParentFile().mkdirs();
						out = new FileOutputStream(newFile);
						copyEntry(in, out);
					}
					entry = in.getNextJarEntry();
				} catch (IOException io) {
					io.printStackTrace();
					throw io;
				} finally {
					if (out != null)
						out.close();
				}
			}
		} finally {
			if (in != null)
				in.close();
		}
	}

	/**
	 * Returns the filename without the extension. Will return the string passed
	 * in if the filename does not contain a '.' character.
	 * 
	 * @param aFileName
	 */
	public static String stripOffExtenstion(String aFileName) {
		if (aFileName.indexOf('.') != -1) {
			return aFileName.substring(0, aFileName.lastIndexOf('.'));
		} else {
			return aFileName;
		}
	}

	// add by lu at 2009-10-14 begin
	public static byte[] readFileToBytes(InputStream input) throws IOException {
		int length = input.available();
		byte[] buffer = new byte[length];
		try {
			input.read(buffer, 0, length);
		} finally {
			safeClose(input);
		}
		return buffer;
	}

	public static void writeFile(File output, byte[] contents)
			throws IOException {
		FileOutputStream bos = new FileOutputStream(output);
		try {
			File pDir = output.getParentFile();
			if (!pDir.exists())
				pDir.mkdirs();

			bos.write(contents, 0, contents.length);

		} finally {
			bos.close();
		}
	}
	
	/**
	 * 带编码格式的文件输出
	 * @param output
	 * @param contents
	 * @throws IOException
	 */
	public static void writeFile(File output, byte[] contents, String charset)
			throws IOException {
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(output), charset));
		try {
			File pDir = output.getParentFile();
			if (!pDir.exists())
				pDir.mkdirs();

			writer.write(new String(contents, charset));
			writer.flush();
		} finally {
			writer.close();
		}
	}

	public static boolean safeClose(InputStream in) {
		if (in != null) {
			try {
				in.close();
				return true;
			} catch (IOException e) {
				return false;
			}
		}
		return true;
	}

	public static String readFile(File input) throws IOException {
		BufferedReader bfr = new BufferedReader(new FileReader(input));
		try {
			// char fs[] = new char[(int) input.length()];
			StringBuffer buffer = new StringBuffer();
			String line;
			while ((line = bfr.readLine()) != null) {
				buffer.append(line + "\n");
			}
			// bfr.read(fs, 0, fs.length);
			// String s = new String(fs);
			return buffer.toString();
		} finally {
			safeClose(bfr);
		}
	}

	public static boolean safeClose(Reader reader) {
		if (reader != null) {
			try {
				reader.close();
				return true;
			} catch (IOException e) {
				return false;
			}
		}
		return true;
	}

	public static boolean createDir(String dirName) {
		return createDir(new File(dirName));
	}

	public static boolean createDir(File dir) {
		return dir.exists() ? dir.isDirectory() : dir.mkdirs();
	}

	// add by lu at 2009-10-14 end
	public static String readResourceFile(String fileName) {
		String result = null;

		ClassLoader cl = Thread.currentThread().getContextClassLoader();

		if (fileName != null && fileName.length() > 0) {
			InputStream file = cl.getResourceAsStream(fileName);
			try {
				result = FileUtils.readFile(file);
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				safeClose(file);
			}
		}

		return result;
	}

	public static String readFile(InputStream input) throws IOException {
		int length = input.available();

		byte[] buffer = new byte[length];
		input.read(buffer, 0, length);

		return new String(buffer);
	}

	public static File createZipFile(String source, String zipSource)
			throws IOException {
		File sourceFile = new File(source);
		File zipFile = new File(zipSource);
		ZipOutputStream zipOutput = null;
		// 创建压缩文件输出流
		try {
			FileOutputStream fileOutput = new FileOutputStream(zipFile);
			BufferedOutputStream bufferOutput = new BufferedOutputStream(
					fileOutput);
			zipOutput = new ZipOutputStream(bufferOutput);
			String basePath = null;
			if (sourceFile.isDirectory()) {
				basePath = sourceFile.getPath();
			} else {
				basePath = sourceFile.getParent();
			}
			doZip(sourceFile,basePath,zipOutput);
		} finally {
			if (zipOutput != null) {
				zipOutput.closeEntry();
				zipOutput.close();
			}
		}
		return zipFile;
	}

	private static void doZip(File sourceFile, String basePath,ZipOutputStream zipOutput) throws IOException {
		File[] files;
		if (sourceFile.isDirectory()) {
			files = sourceFile.listFiles();
		} else {
			files = new File[1];
			files[0] = sourceFile;
		}
		InputStream input = null;
		String pathName;
		byte[] buf = new byte[1024];
		int length = 0;
		try {
			for(File file:files){
				if(file.isDirectory()){
					 pathName = file.getPath().substring(basePath.length() + 1) + "/";
					 zipOutput.putNextEntry(new ZipEntry(pathName));
					 doZip(file, basePath, zipOutput);
				}else{
				     pathName = file.getPath().substring(basePath.length() + 1);
				     input = new FileInputStream(file);
				     BufferedInputStream bis = new BufferedInputStream(input);
				     zipOutput.putNextEntry(new ZipEntry(pathName));
				     while ((length = bis.read(buf)) > 0) {
				    	 zipOutput.write(buf, 0, length);
				     }
				}
			}
		} finally {
			if (input != null) {
				input.close();
			}
		}

	}
	
	public static void deleteFiles(File[] files) {
		if (files != null) {
			for (File file : files) {
				if (!file.isDirectory()) {
					file.delete();
				}
			}
		}

	}

}
