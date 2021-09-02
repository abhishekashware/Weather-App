package com.example.weather;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WeatherDataService {
    Context context;
    String cityId;
    public  static  final String QUERY_FOR_CITY_ID="https://www.metaweather.com/api/location/search/?query=";
    public  static  final String QUERY_FOR_CITY_WEATHER_ID="https://www.metaweather.com/api/location/";

    public WeatherDataService(Context context) {
        this.context = context;
    }


    public interface VolleyResopnseListener{
        void onError(String message);
       void onResponse(String  cityID);
    }

    public interface ForecastByIDResponse{
        void onError(String message);
        void onResponse(List<WeatherReportModel> weatherReportModels);
    }
    public void getCityID(String cityName,VolleyResopnseListener volleyResopnseListener){
        //  RequestQueue queue= Volley.newRequestQueue(MainActivity.this);
        String url=QUERY_FOR_CITY_ID+cityName;

        JsonArrayRequest request=new JsonArrayRequest(Request.Method.GET,url, null,new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                cityId="";
                try {
                    JSONObject cityinfo=response.getJSONObject(0);
                    cityId=cityinfo.getString("woeid");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
              //  Toast.makeText(context,"City ID:"+cityId,Toast.LENGTH_SHORT).show();
                volleyResopnseListener.onResponse(cityId);
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,"Something Wrong.",Toast.LENGTH_SHORT).show();
                volleyResopnseListener.onError("Something wrong");
            }
        });
        MySingleton.getInstance(context).addToRequestQueue(request);
    }

    public interface GetCityForecastByNameCallback{
        void onError(String message);
        void onResponse(List<WeatherReportModel> weatherReportModels);
    }
    public void getForecastByName(String cityName,GetCityForecastByNameCallback getCityForecastByNameCallback){
        getCityID(cityName, new VolleyResopnseListener() {
            @Override
            public void onError(String message) {
                Toast.makeText(context,"Something Wrong",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String cityID) {
                getForecastByID(cityID, new ForecastByIDResponse() {
                    @Override
                    public void onError(String message) {
                        Toast.makeText(context,"Something Wrong",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(List<WeatherReportModel> weatherReportModels) {
                        getCityForecastByNameCallback.onResponse(weatherReportModels);
                    }
                });
            }
        });
    }

    public void getForecastByID(String cityId,ForecastByIDResponse forecastByIDResponse){
        //  RequestQueue queue= Volley.newRequestQueue(MainActivity.this);


            String url=QUERY_FOR_CITY_WEATHER_ID+cityId;
            List<WeatherReportModel> weatherReportModels=new ArrayList<>();
        JsonObjectRequest request=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
//              Toast.makeText(context,response.toString(),Toast.LENGTH_SHORT).show();
                try {
                    JSONArray consolidated_weather_list= response.getJSONArray("consolidated_weather");
                    for(int i = 0;i<consolidated_weather_list.length();i++){
                        WeatherReportModel one_day_weather=new WeatherReportModel();
                        JSONObject day_from_api=(JSONObject) consolidated_weather_list.get(i);
                        one_day_weather.setId(day_from_api.getInt("id"));
                        one_day_weather.setWeather_state_name(day_from_api.getString("weather_state_name"));
                        one_day_weather.setWeather_state_abbr(day_from_api.getString("weather_state_abbr"));
                        one_day_weather.setWind_direction_compass(day_from_api.getString("wind_direction_compass"));
                        one_day_weather.setCreated(day_from_api.getString("created"));
                        one_day_weather.setApplicable_date(day_from_api.getString("applicable_date"));
                        one_day_weather.setMin_temp(day_from_api.getLong("min_temp"));
                        one_day_weather.setMax_temp(day_from_api.getLong("max_temp"));
                        one_day_weather.setThe_temp(day_from_api.getLong("the_temp"));
                        one_day_weather.setWind_speed(day_from_api.getLong("wind_speed"));
                        one_day_weather.setWind_direction(day_from_api.getLong("wind_direction"));
                        one_day_weather.setAir_pressure(day_from_api.getInt("air_pressure"));
                        one_day_weather.setHumidity(day_from_api.getInt("humidity"));
                        one_day_weather.setVisiblity(day_from_api.getLong("visibility"));
                        one_day_weather.setPredictability(day_from_api.getInt("predictability"));
                        weatherReportModels.add(one_day_weather);

                    }



                    forecastByIDResponse.onResponse(weatherReportModels);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

      MySingleton.getInstance(context).addToRequestQueue(request);
    }


}
