package com.task.steel_notes.data;

import java.util.Random;

import com.task.steel_notes.R;

public class PictureIndexConverter {
    private static Random rnd = new Random();
    private static Object syncObj = new Object();

    private static int[] picIndex = {
            R.drawable.logo1,
            R.drawable.logo2,
            R.drawable.logo3,
            R.drawable.logo4,
            R.drawable.logo5,
            R.drawable.logo6,
            R.drawable.logo7,
    };

    public static int randomPictureIndex(){
        synchronized (syncObj){
            return rnd.nextInt(picIndex.length);
        }
    }

    public static int getPictureByIndex(int index){
        if (index < 0 || index >= picIndex.length){
            index = 0;
        }
        return picIndex[index];
    }

    public static int getIndexByPicture(int picture){
        for(int i = 0; i < picIndex.length; i++){
            if (picIndex[i] == picture){
                return i;
            }
        }
        return 0;
    }

}
