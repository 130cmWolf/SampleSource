using System;
using System.Collections.Generic;
using System.Globalization;

public static class DateTimeExtension
{
    public static String ToStringExtension(this DateTime dt, String format)
    {
        return dt.ToStringExtension(format, CultureInfo.CurrentCulture);
    }
    public static String ToStringExtension(this DateTime dt, String format, IFormatProvider provider)
    {
        String ret = format;
        ret = ret.Replace("bb", dt.GetHour12().ToString("00"));
        ret = ret.Replace("b", dt.GetHour12().ToString("0"));
        ret = ret.Replace("j", dt.NowEra());
        ret = ret.Replace("ii", dt.NowEraYear().ToString("00"));
        ret = ret.Replace("i", dt.NowEraYear().ToString("0"));
        return dt.ToString(ret, provider);
    }
    /// <summary>
    /// 0-11時の時刻が取得できます。
    /// </summary>
    /// <param name="dt"></param>
    /// <returns></returns>
    // StringFormat b bb
    public static int GetHour12(this DateTime dt)
    {
        return dt.Hour % 12;
    }
    /// <summary>
    /// 現在の和暦が取得できます。
    /// </summary>
    /// <param name="dt"></param>
    /// <returns></returns>
    // StringFormat j
    public static String NowEra(this DateTime dt)
    {
        foreach(var ele in Eras){
            TimeSpan ts = dt - ele.EraFrom;
            if (ts.TotalDays >= 0)
            {
                return ele.Era;
            }
        }
        return "";
    }
    /// <summary>
    /// 現在の和暦に対する年が取得できます。
    /// </summary>
    /// <param name="dt"></param>
    /// <returns></returns>
    // StringFormat i ii
    public static int NowEraYear(this DateTime dt)
    {
        foreach (var ele in Eras)
        {
            TimeSpan ts = dt - ele.EraFrom;
            if (ts.TotalDays >= 0)
            {
                return dt.Year - ele.EraFrom.Year + 1;
            }
        }
        return 0;
    }

    // 平成 1989/1/8～
    // 昭和 1926/12/25～
    // 大正 1912/7/30～
    // 明治 1868/9/8～
    public static List<EraData> Eras = new List<EraData>(){
        new EraData(){Era = "新和暦", EraFrom = new DateTime(2020,10,10)},
        new EraData(){Era = "平成", EraFrom = new DateTime(1989,1,8)},
        new EraData(){Era = "昭和", EraFrom = new DateTime(1926,12 ,25)},
        new EraData(){Era = "大正", EraFrom = new DateTime(1912,7 ,30)},
        new EraData(){Era = "明治", EraFrom = new DateTime(1868,9 ,8)},
    };
    
    public class EraData
    {
        public String Era { get; set; }
        public DateTime EraFrom { get; set; }
    }
}
