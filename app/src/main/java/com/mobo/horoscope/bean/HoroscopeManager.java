package com.mobo.horoscope.bean;

import com.mobo.horoscope.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 十二星座管理类
 * @Author: jzhou
 * @CreateDate: 19-8-14 上午8:49
 */
public class HoroscopeManager {
    private static final List<Horoscope> horoscopeList;

    static {
        horoscopeList = new ArrayList<>();
        horoscopeList.add(new Horoscope(1, "Aries", R.drawable.ic_aries_1, R.drawable.ic_aries_2, 3, 21, 4, 19, "03.21-04.19"));
        horoscopeList.add(new Horoscope(2, "Taurus", R.drawable.ic_taurus_1, R.drawable.ic_taurus_2, 4, 20, 5, 20, "04.20-05.20"));
        horoscopeList.add(new Horoscope(3, "Gemini", R.drawable.ic_gemini_1, R.drawable.ic_gemini_2, 5, 21, 6, 21, "05.21-06.21"));
        horoscopeList.add(new Horoscope(4, "Cancer", R.drawable.ic_cancer_1, R.drawable.ic_cancer_2, 6, 22, 7, 22, "06.22-07.22"));
        horoscopeList.add(new Horoscope(5, "Leo", R.drawable.ic_leo_1, R.drawable.ic_leo_2, 7, 23, 8, 22, "07.23-08.22"));
        horoscopeList.add(new Horoscope(6, "Virgo", R.drawable.ic_virgo_1, R.drawable.ic_virgo_2, 8, 23, 9, 22, "08.23-09.22"));
        horoscopeList.add(new Horoscope(7, "Libra", R.drawable.ic_libra_1, R.drawable.ic_libra_2, 9, 23, 10, 23, "09.23-10.23"));
        horoscopeList.add(new Horoscope(8, "Scorpio", R.drawable.ic_scorpio_1, R.drawable.ic_scorpio_2, 10, 24, 11, 22, "10.24-11.22"));
        horoscopeList.add(new Horoscope(9, "Sagittarius", R.drawable.ic_sagittarius_1, R.drawable.ic_sagittarius_2, 11, 23, 12, 21, "11.23-12.21"));
        horoscopeList.add(new Horoscope(10, "Capricorn", R.drawable.ic_capricorn_1, R.drawable.ic_capricorn_2, 12, 22, 1, 19, "12.22-01.19"));
        horoscopeList.add(new Horoscope(11, "Aquarius", R.drawable.ic_aquarius_1, R.drawable.ic_aquarius_2, 1, 20, 2, 18, "01.20-02.18"));
        horoscopeList.add(new Horoscope(12, "Pisces", R.drawable.ic_pisces_1, R.drawable.ic_pisces_2, 2, 19, 3, 20, "02.19-03.20"));
    }

    public static List<Horoscope> getHoroscopeList() {
        return horoscopeList;
    }

    public static Horoscope getHoroscope(int month, int day) {
        for (Horoscope horoscope : horoscopeList) {
            if (month == horoscope.getBeginMonth()) {
                if (day >= horoscope.getBeginDay()) {
                    return horoscope;
                }
            } else if (month == horoscope.getEndMonth()) {
                if (day <= horoscope.getEndDay()) {
                    return horoscope;
                }
            }
        }

        return null;
    }

    public static Horoscope getHoroscope(int horoscopeId) {
        for (Horoscope horoscope : horoscopeList) {
            if (horoscopeId == horoscope.getHoroscopeId()) {
                return horoscope;
            }
        }

        return null;
    }

    public static Horoscope getHoroscope(String horoscopeName) {
        for (Horoscope horoscope : horoscopeList) {
            if (horoscope.getName().equals(horoscopeName)) {
                return horoscope;
            }
        }

        return null;
    }
}
