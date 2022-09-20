package com.cubes.android.komentar.data.source.remote.networking.response;

public class WeatherResponseModel extends BaseResponseModel {

    public WeatherResponseDataModel data;

    public class WeatherResponseDataModel {

        public String id;
        public String name;
        public String temp;
        public String temp_min;
        public String temp_max;
        public String wind;
        public String pressure;
        public int humidity;
        public String description;
        public String icon_url;
        public DayResponse day_0;
        public DayResponse day_1;
        public DayResponse day_2;
        public DayResponse day_3;
        public DayResponse day_4;
        public DayResponse day_5;
        public DayResponse day_6;

        public class DayResponse {

            public String temp_min;
            public String temp_max;
            public String description;
            public String icon_url;

        }


    }
}
