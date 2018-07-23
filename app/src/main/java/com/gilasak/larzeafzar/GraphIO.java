package com.gilasak.larzeafzar;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;

import com.jjoe64.graphview.series.DataPoint;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Manfered on 1/11/2017.
 */

public class GraphIO {

    Context context;

    public GraphIO(Context _context) {
        this.context = _context;
    }

    public GraphIOResult save(String filename,
                              String unit,
                              Iterator<DataPoint> mseriesXList,
                              Iterator<DataPoint> mseriesYList,
                              Iterator<DataPoint> mseriesZList,
                              Iterator<DataPoint> mseriesBarList) {

        GraphIOResult graphIOResult = new GraphIOResult();

        //daryaft name file
        //barresi khali nabudan
        //barresi sehat name file
        filename = filename.trim();
        filename = filename.replaceAll("[^a-zA-Z0-9.-]", "_");

        if (filename.length() != 0) {
            //barresi tekrari nabudan
            if (!fileExistance(filename)) {

                File path = Environment.getDataDirectory();
                StatFs stat = new StatFs(path.getPath());
                long bytesAvailable = 0;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2)
                    bytesAvailable = (long) stat.getBlockSizeLong() * (long) stat.getAvailableBlocksLong();
                else
                    bytesAvailable = (long) stat.getBlockSize() * (long) stat.getAvailableBlocks();

                //start making the string to save
                //show the progress
                //progressBar.setVisibility(View.VISIBLE);

                String textToSave = "";

                textToSave += unit;
                textToSave += "~";
                while (mseriesXList.hasNext()) {
                    DataPoint d = mseriesXList.next();
                    textToSave += String.valueOf(d.getX()) + "@" + String.valueOf(d.getY()) + "#";
                }
                textToSave += "~";
                while (mseriesYList.hasNext()) {
                    DataPoint d = mseriesYList.next();
                    textToSave += String.valueOf(d.getX()) + "@" + String.valueOf(d.getY()) + "#";
                }
                textToSave += "~";
                while (mseriesZList.hasNext()) {
                    DataPoint d = mseriesZList.next();
                    textToSave += String.valueOf(d.getX()) + "@" + String.valueOf(d.getY()) + "#";
                }
                textToSave += "~";
                while (mseriesBarList.hasNext()) {
                    DataPoint d = mseriesBarList.next();
                    textToSave += String.valueOf(d.getX()) + "@" + String.valueOf(d.getY()) + "#";
                }


                if (textToSave.getBytes().length * 3 < bytesAvailable) {
                    FileOutputStream outputStream;
                    try {
                        outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
                        outputStream.write(textToSave.getBytes());
                        outputStream.flush();
                        outputStream.close();
                        graphIOResult.result = true;
                        graphIOResult.message = context.getResources().getString(R.string.dialogSAveFileMessageSAveOK);
                    } catch (Exception e) {
                        graphIOResult.result = false;
                        graphIOResult.message = context.getResources().getString(R.string.dialogSAveFileErrorGeneralError);
                        e.printStackTrace();
                    }
                    //invisible the progressbar
                    //progressBar.setVisibility(View.GONE);
                } else {
                    graphIOResult.result = false;
                    graphIOResult.message = context.getResources().getString(R.string.dialogSAveFileErrorNotEnoughSpace);
                }

            } else {
                graphIOResult.result = false;
                graphIOResult.message = context.getResources().getString(R.string.dialogSAveFileErrorFileNameDuplicate);
            }
        } else {
            graphIOResult.result = false;
            graphIOResult.message = context.getResources().getString(R.string.dialogSAveFileErrorFileNameEmpty);
        }

        return graphIOResult;
    }


    public GraphIOResult load(String filename,
                              ArrayList<DataPoint> mseries1ArrayList,
                              ArrayList<DataPoint> mseries2ArrayList,
                              ArrayList<DataPoint> mseries3ArrayList,
                              ArrayList<DataPoint> mseries1BarArrayList) {


        GraphIOResult graphIOResult = new GraphIOResult();

        try {
            /*FileInputStream fin = context.openFileInput(filename);
            int c;
            String temp = "";
            while ((c = fin.read()) != -1) {
                temp = temp + Character.toString((char) c);
            }*/

            FileInputStream fin = context.openFileInput(filename);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fin));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                //sb.append(line).append("\n");
                sb.append(line);
            }
            reader.close();
            String temp = sb.toString();

            String[] strSeries = temp.split("~");

            //be dalil inke dar version 1.0 dar neveshtan file ha scale dar nazar gerefte nashode bud
            //bayad in file ha ra betavanim bekhanim ba meghyas ms2
            //banabar in variable j tarif mishavad va tedad khane haye string khande shode az file check mishavad

            int k = 0;
            if(strSeries.length == 5){
                //file sakhte shode dar version 1.1 be bad ast
                k = 1;
                graphIOResult.unit = strSeries[0];
            }
            else{
                //file sakhte shode dar noskheye aval barname
                k = 0;
                graphIOResult.unit = "MS2";
            }


            for (int i = k; i < strSeries.length; ++i) {
                String[] strDataPoints = strSeries[i].split("#");
                for (int j = 0; j < strDataPoints.length - 1; ++j) {
                    String[] strXY = strDataPoints[j].split("@");
                    DataPoint dp = new DataPoint(Double.parseDouble(strXY[0]), Double.parseDouble(strXY[1]));
                    if(strSeries.length == 5){
                        switch (i) {
                            case 1:
                                mseries1ArrayList.add(dp);
                                break;
                            case 2:
                                mseries2ArrayList.add(dp);
                                break;
                            case 3:
                                mseries3ArrayList.add(dp);
                                break;
                            case 4:
                                mseries1BarArrayList.add(dp);
                                break;
                        }
                    }else{
                        switch (i) {
                            case 0:
                                mseries1ArrayList.add(dp);
                                break;
                            case 1:
                                mseries2ArrayList.add(dp);
                                break;
                            case 2:
                                mseries3ArrayList.add(dp);
                                break;
                            case 3:
                                mseries1BarArrayList.add(dp);
                                break;
                        }
                    }
                }
            }

            fin.close();

            graphIOResult.result = true;
            graphIOResult.message = context.getResources().getString(R.string.dialogLoadFileOk);

        } catch (Exception e) {
            graphIOResult.result = false;
            graphIOResult.message = context.getResources().getString(R.string.dialogLoadFileGeneralError);
            e.printStackTrace();
        }


        return graphIOResult;
    }


    public GraphIOResult delete(String filename){
        GraphIOResult graphIOResult = new GraphIOResult();

        try{
            boolean res = context.deleteFile(filename);
            if(res){
                graphIOResult.result = true;
                graphIOResult.message = context.getResources().getString(R.string.DialogDeleteFileMessageOK);
            }
            else{
                graphIOResult.result = false;
                graphIOResult.message = context.getResources().getString(R.string.DialogDeleteFileGeneralError);
            }
        }
        catch (Exception e){
            graphIOResult.result = false;
            graphIOResult.message = context.getResources().getString(R.string.DialogDeleteFileGeneralError);
            e.printStackTrace();
        }

        return graphIOResult;
    }

    public boolean fileExistance(String fname) {
        File file = context.getFileStreamPath(fname);
        return file.exists();
    }

}
