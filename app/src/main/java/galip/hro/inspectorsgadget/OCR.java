package galip.hro.inspectorsgadget;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Environment;
import android.util.Log;
import android.widget.EditText;

import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OCR
{
    private String PhotoFilePath;
    public static final String DATA_DIR = Environment.getExternalStorageDirectory().toString() + "/OCR/";
    public static final String lang = "eng";
    public static final String LOG_DEBUG_TAG = "OCR";
    private HashSet<String> CheckedContainerNumbers = new HashSet<String>();

    public HashSet<String> findContainerNumbers(String photoFilePath)
    {
        File PhotoFile = new File(photoFilePath);
        if(PhotoFile.exists()) {
            Bitmap photoBitmap = getBitmapFromFile(PhotoFile);
            //mImageView.setImageBitmap(photoBitmap);

            try {
                ExifInterface exif = new ExifInterface(PhotoFile.getAbsolutePath());
                int exifOrientation = exif.getAttributeInt(
                        ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_NORMAL);

                Log.v(LOG_DEBUG_TAG, "Orient: " + exifOrientation);

                int rotate = 0;

                switch (exifOrientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        rotate = 90;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        rotate = 180;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        rotate = 270;
                        break;
                }

                Log.v(LOG_DEBUG_TAG, "Rotation: " + rotate);

                if (rotate != 0) {

                    // Getting width & height of the given image.
                    int w = photoBitmap.getWidth();
                    int h = photoBitmap.getHeight();

                    // Setting pre rotate
                    Matrix mtx = new Matrix();
                    mtx.preRotate(rotate);

                    // Rotating Bitmap
                    photoBitmap = Bitmap.createBitmap(photoBitmap, 0, 0, w, h, mtx, false);
                }

                // Convert to ARGB_8888, required by tess
                photoBitmap = photoBitmap.copy(Bitmap.Config.ARGB_8888, true);

            } catch (IOException e) {
                Log.e(LOG_DEBUG_TAG, "Couldn't correct orientation: " + e.toString());
            }

            // _image.setImageBitmap( bitmap );

            Log.v(LOG_DEBUG_TAG, "Before baseApi");

            TessBaseAPI baseApi = new TessBaseAPI();
            baseApi.setDebug(true);
            String OCRDirectory = DATA_DIR;
            File dir = new File(OCRDirectory);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            try {
                baseApi.init(OCRDirectory, "eng");
            } catch (Exception e) {
                //Log.e(LOG_DEBUG_TAG, e.getMessage());
            }
            //start changing file using Leptonica
//            Pix a = ReadFile.readBitmap(photoBitmap);
//            Pix b = Scale.scale(a, 2.0f);
//            Pix c = Enhance.unsharpMasking(a, 9, 0.7f);
//
//
//            BitmapToFile(b, "b.png");
//            BitmapToFile(c, "c.png");


//            File finalphoto = new File(DATA_DIR + "c.png");

//            photoBitmap = getBitmapFromFile(finalphoto);
            //mImageView.setImageBitmap(photoBitmap);
            //end
            baseApi.setImage(photoBitmap);

            String recognizedText = baseApi.getUTF8Text();
            baseApi.end();

            // You now have the text in recognizedText var, you can do anything with it.
            // We will display a stripped out trimmed alpha-numeric version of it (if lang is eng)
            // so that garbage doesn't make it to the display.

            Log.v(LOG_DEBUG_TAG, "OCRED TEXT: " + recognizedText);

            if ("eng".equalsIgnoreCase("eng")) {
                recognizedText = recognizedText.replaceAll("[^a-zA-Z0-9]+", " ");
            }

            recognizedText = recognizedText.trim();

//            EditText edtText = (EditText) findViewById(R.id.txtResultOCR);
//            edtText.setText(recognizedText);

            recogniseContainerNO(recognizedText);

        }
        return CheckedContainerNumbers;
    }

    private Bitmap getBitmapFromFile(File file)
    {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 1;
        Bitmap photoBitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);

        return photoBitmap;
    }

    public boolean isContainerNumber(String input)
    {
        boolean result = false;
        int totalValue = 0;
        int calculatedCheckDigit = 0;
        int checkDigit = input.charAt(input.length()-1)-48;//48 is ASCII value for number 0
        int CharValue[] = {10,12,13,14,15,16,17,18,19,20,21,23,24,25,26,27,28,29,30,31,32,34,35,36,37,38};

        for(int i=0; i<input.length()-1;i++)
        {
            //There are 4 letters.
            if(i<=3)
            {
                Character c = input.charAt(i);
                int position = (int)c-65;
                int multiplier = (int)Math.pow(2,i);

                totalValue += CharValue[position] * multiplier;
            }
            else
            {
                int value = ((int)input.charAt(i))-48;
                int multiplier = (int)Math.pow(2,i);
                totalValue += value * multiplier;
            }
        }

        calculatedCheckDigit = totalValue - ((int)(totalValue / 11)) * 11;
        if (calculatedCheckDigit == checkDigit)
        {
            result = true;
        }

        return result;
    }

    public void recogniseContainerNO(String input)
    {
        Set<String> FoundPatternSet = new HashSet<String>();
        //Use regex to find ContainerNO pattern from messy OCR text
        Pattern pattern = Pattern.compile("[a-z]{4}[\\d]{7}", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(input);

        //EditText edtContainerNO = (EditText) findViewById(R.id.txtContainerNO);
        //edtContainerNO.setText("");

        while(matcher.find())
        {
            FoundPatternSet.add(matcher.group(0).toUpperCase());
//            edtContainerNO.setText(edtContainerNO.getText() + "\n" + matcher.group(0).toUpperCase());
        }

        //Start the algorithm part
        for(String containerNr : FoundPatternSet)
        {
            if (isContainerNumber(containerNr))
            {
                CheckedContainerNumbers.add(containerNr);
            }else
            {

                Log.d(LOG_DEBUG_TAG, containerNr + "- did not pass containernr algoritm");
            }

        }
//        TotalFoundTag+=FoundPatternSet.size();
    }
}
