package incident.timo.com;

import org.apache.http.client.utils.URIBuilder;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Created by tim on 9/9/2016.
 */
public class ProcessIncidentDataChicago extends ProcessIncidentData {

    protected String INCIDENT_BASE_URL = "data.cityofchicago.org";
    protected String INCIDENT_PATH = "/resource/ijzp-q8t2.json";
    protected  String QUERY_PARAM_YEAR = "year";

    public ProcessIncidentDataChicago(String...params){
        super(params);
        super.setBaseUrl(INCIDENT_BASE_URL);
        super.setIncidentPath(INCIDENT_PATH);
        super.setQueryString(QUERY_PARAM_YEAR + "=" + mCurrentYear);
    }

    protected void processIncidentData(String jsonStr) throws JSONException{
        IncidentData incident = new IncidentData();
        String streetAddr = new String();
        incident.setLatitude(" ");
        incident.setLongitude(" ");

        JSONObject crimeJson = new JSONObject(jsonStr);
        incident.setDescription(crimeJson.getString("description"));
        incident.setDateTime(crimeJson.getString("date"));
        incident.setGangRelated("N/A");
        incident.setShortDesc(crimeJson.getString("primary_type"));
        streetAddr = (crimeJson.getString("block"));
        incident.setAddress(streetAddr.replace("XX","00"));
        incident.setVictimCnt("N/A");
        //  JSONArray locInfo = (JSONArray)crimeJson.get("geo_crime_location");
        if (jsonStr.contains("latitude")) {
            JSONObject locObj = (JSONObject) crimeJson.get("location");
            incident.setLatitude(locObj.getString("latitude"));
        }
        if (jsonStr.contains("longitude")) {
            JSONObject locObj = (JSONObject) crimeJson.get("location");
            incident.setLongitude(locObj.getString("longitude"));
        }
        // Log.d(LOG_TAG,"DONE PARSING - SHort Desc" + crimeJson.getString("summarized_offense_description"));
        if(incidentMap.containsKey(streetAddr)){
            incidentMap.put(streetAddr+incident.getDateTime(),incident);
        }else {
            incidentMap.put(streetAddr, incident);
        }
    }
}
