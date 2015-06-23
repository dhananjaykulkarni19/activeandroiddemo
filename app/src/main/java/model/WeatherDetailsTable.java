package model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.query.Select;

import java.util.List;

/**
 * Created by dhananjay on 23/6/15.
 */
public class WeatherDetailsTable extends Model {

    @Column(name="CityID",onUniqueConflict = Column.ConflictAction.REPLACE)
    public int _cityID;

    @Column(name="cityName")
    public String _cityName;

    @Column(name="Date")
    public String _date;

    @Column(name="MainWeather")
    public String _mainWeather;

    @Column(name="Description")
    public String _desc;

    @Column(name="DayTemprature")
    public float _dayTemp;

    @Column(name="MaxTemprature")
    public float _maxTemp;

    @Column(name="MinTemprature")
    public float _minTemp;

    public WeatherDetailsTable() {

        super();
    }

    public static WeatherDetailsTable getWeatherDetailsForGivenCity(String city)
    {
        return new Select().from(WeatherDetailsTable.class).where("CityName = ?",city).executeSingle();
    }
}