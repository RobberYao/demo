package com.siebre.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class ImageUtils {
	public static final String CONVERT_PROG = "C:\\Program Files\\ImageMagick-6.5.1-Q16\\convert";

	/**
	 * Uses a Runtime.exec()to use imagemagick to perform the given conversion
	 * operation. Returns true on success, false on failure. Does not check if
	 * either file exists.
	 *
	 * @param in Description of the Parameter
	 * @param out Description of the Parameter
	 * @param newSize Description of the Parameter
	 * @param quality Description of the Parameter
	 * @return Description of the Return Value
	 */
	public static boolean convert(File in, File out, int width, int height, int quality) {
		System.out.println("convert(" + in.getPath() + ", " + out.getPath() + ", " + width + ", " + quality);

		if (quality < 0 || quality > 100) {
			quality = 75;
		}

		ArrayList<String> command = new ArrayList<String>(10);

		// note: CONVERT_PROG is a class variable that stores the location of ImageMagick's convert command
		// it might be something like "/usr/local/magick/bin/convert" or something else, depending on where you installed it.
		command.add(CONVERT_PROG);
		command.add("-geometry");
		command.add(width + "x" + height);
		command.add("-quality");
		command.add("" + quality);
		command.add(in.getAbsolutePath());
		command.add(out.getAbsolutePath());

		System.out.println(command);

		return exec((String[]) command.toArray(new String[1]));
	}

	/**
	 * Tries to exec the command, waits for it to finsih, logs errors if exit
	 * status is nonzero, and returns true if exit status is 0 (success).
	 *
	 * @param command Description of the Parameter
	 * @return Description of the Return Value
	 */
	private static boolean exec(String[] command) {
		Process proc;

		try {
			//System.out.println("Trying to execute command " + Arrays.asList(command));
			proc = Runtime.getRuntime().exec(command);
		} catch (IOException e) {
			System.out.println("IOException while trying to execute " + Arrays.toString(command));
			return false;
		}

		//System.out.println("Got process object, waiting to return.");

		int exitStatus;

		while (true) {
			try {
				exitStatus = proc.waitFor();
				break;
			} catch (java.lang.InterruptedException e) {
				System.out.println("Interrupted: Ignoring and waiting");
			}
		}
		if (exitStatus != 0) {
			System.out.println("Error executing command: " + exitStatus);
		}
		return (exitStatus == 0);
	}
}
