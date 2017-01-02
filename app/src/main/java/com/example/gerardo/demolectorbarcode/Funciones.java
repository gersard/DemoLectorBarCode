package com.example.gerardo.demolectorbarcode;

import android.content.res.Resources;

/**
 * Created by Gerardo on 02/01/2017.
 */

public class Funciones {

    public static int ConvertDpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }


}
