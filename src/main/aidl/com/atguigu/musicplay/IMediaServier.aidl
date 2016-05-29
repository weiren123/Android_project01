// IMediaServier.aidl
package com.atguigu.musicplay;

// Declare any non-default types here with import statements

interface IMediaServier {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);
    void openAuto(int position);

    void start();

    void pause();

     void pre();

     void next();

     int getCrttenPosition();

     long getDurition();

     String getName();

      String getlire();
      boolean isplaying();
      void seekto(int seekto);
}
