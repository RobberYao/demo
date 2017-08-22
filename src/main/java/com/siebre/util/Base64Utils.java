package com.siebre.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import org.apache.commons.codec.binary.Base64;

public class Base64Utils {
    /**
     * Serializes an object and returns the Base64-encoded
     * version of that serialized object. If the object
     * cannot be serialized or there is another error,
     * the method will return <tt>null</tt>.
     *
     * @param object The object to encode
     * @param breakLines Break lines at 76 characters or less.
     * @return The Base64-encoded object
     * @since 1.4
     */
    public static String encodeObject(java.io.Serializable object, boolean breakLines) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(object);
            oos.close();

            byte[] encoded = Base64.encodeBase64(compress(bos.toByteArray()), breakLines);

            return new String(encoded);
        }
        catch (java.io.IOException e) {
            e.printStackTrace();
            return null;
        }
    } // end encode

    /**
     * Attempts to decode Base64 data and deserialize a Java
     * Object within. Returns <tt>null if there was an error.
     *
     * @param encodedObject The Base64 data to decode
     * @return The decoded and deserialized object
     * @since 1.4
     */
    public static Object decodeToObject(String encodedObject) {
        byte[] objBytes = Base64.decodeBase64(encodedObject.getBytes());

        ByteArrayInputStream bais = null;
        ObjectInputStream ois = null;

        try {
            bais = new ByteArrayInputStream(decompress(objBytes));
            ois = new ObjectInputStream(bais);

            return ois.readObject();
        } // end try
        catch (java.io.IOException e) {
            e.printStackTrace();
            return null;
        } // end catch
        catch (java.lang.ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        } // end catch
        finally {
            try {
                bais.close();
            }
            catch (Exception e) {
            }
            try {
                ois.close();
            }
            catch (Exception e) {
            }
        } // end finally
    } // end decodeObject

    public static byte[] compress(byte[] input) {
        // Create the compressor with highest level of compression
        Deflater compressor = new Deflater();
        compressor.setLevel(Deflater.BEST_COMPRESSION);

        // Give the compressor the data to compress
        compressor.setInput(input);
        compressor.finish();

        // Create an expandable byte array to hold the compressed data.
        // You cannot use an array that's the same size as the orginal because
        // there is no guarantee that the compressed data will be smaller than
        // the uncompressed data.
        ByteArrayOutputStream bos = new ByteArrayOutputStream(input.length);

        // Compress the data
        byte[] buf = new byte[1024];
        while (!compressor.finished()) {
            int count = compressor.deflate(buf);
            bos.write(buf, 0, count);
        }

        try {
            bos.close();
        }
        catch (IOException e) {
        }

        // Get the compressed data
        return bos.toByteArray();
    }

    public static byte[] decompress(byte[] compressedData) {
        //  Create the decompressor and give it the data to compress
        Inflater decompressor = new Inflater();
        decompressor.setInput(compressedData);

        // Create an expandable byte array to hold the decompressed data
        ByteArrayOutputStream bos = new ByteArrayOutputStream(compressedData.length);

        // Decompress the data
        byte[] buf = new byte[1024];
        while (!decompressor.finished()) {
            try {
                int count = decompressor.inflate(buf);
                bos.write(buf, 0, count);
            }
            catch (DataFormatException e) {
            }
        }
        try {
            bos.close();
        }
        catch (IOException e) {
        }

        // Get the decompressed data
        return bos.toByteArray();
    }
}
