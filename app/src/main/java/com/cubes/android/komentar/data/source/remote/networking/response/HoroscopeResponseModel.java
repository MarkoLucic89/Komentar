package com.cubes.android.komentar.data.source.remote.networking.response;


public class HoroscopeResponseModel extends BaseResponseModel {

    public HoroscopeDataResponseModel data;

    public static class HoroscopeDataResponseModel {

        public String name;
        public String date;
        public String horoscope;
        public String image_url;

    }


}
