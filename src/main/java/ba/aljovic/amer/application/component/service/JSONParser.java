package ba.aljovic.amer.application.component.service;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
class JSONParser
{
    public String getByName(String json, String name)
    {
        try
        {
            JSONObject jm = new JSONObject(json);
            return jm.getString(name);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
            return null;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
}
