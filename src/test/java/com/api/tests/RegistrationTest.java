package com.api.tests;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.json.simple.JSONObject;
import org.junit.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;

public class RegistrationTest extends HttpInterceptor {

    @Test(description = "verify successful registration")
    public void registration()
    {
        //created map to set values in json object
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("email",getProperty("registration.emailId"));
        map.put("password",getProperty("registration.password"));

        JSONObject requestParams = loadRequestWithData(map);
        Response response = httpPost(getProperty("registrationURI"), requestParams);
        response.then().log().all();


        JsonPath jsonPathString=JsonPath.from(response.asString());
        token = jsonPathString.getString("token");
        Assert.assertEquals(response.getStatusCode(),200);
        Assert.assertNotNull(token);
        Assert.assertNotSame(token,"");
        Assert.assertNotNull(jsonPathString.getString("id"));
    }

    @Test(description = "verify Registration with Invalid Email id")
    public void registrationWithInvalidEmail()
    {
        //created map to set values in json object
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("email",getProperty("registration.emailId"));
       // map.put("password","");

        JSONObject requestParams = loadRequestWithData(map);
        Response response = httpPost(getProperty("registrationURI"), requestParams);
        response.then().log().all();

        Assert.assertTrue(verifyBadRequestWithMsg(response,getProperty("register.missingPassword")));

    }


    @Test(description = "verify Registration with Missing password")
    public void registrationWithMissingPassword()
    {
        //created map to set values in json object
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("email","vrushaspol@gmail.com");
        map.put("password",getProperty("registration.password"));

        JSONObject requestParams = loadRequestWithData(map);
        Response response = httpPost(getProperty("registrationURI"), requestParams);
        response.then().log().all();

        Assert.assertTrue(verifyBadRequestWithMsg(response,getProperty("register.invalidEmail")));

    }
    @Test(description = "verify Registration with Missing email and password both")
    public void registrationWithMissingEmailAndPassword()
    {
        //created map to set values in json object
        HashMap<String, String> map = new HashMap<String, String>();

        JSONObject requestParams = loadRequestWithData(map);
        Response response = httpPost(getProperty("registrationURI"), requestParams);
        response.then().log().all();

        Assert.assertTrue(verifyBadRequestWithMsg(response,getProperty("register.missingEmailAndPassword")));

    }

    public boolean verifyBadRequestWithMsg(Response response,String msg)
    {
        boolean flag=false;
        JsonPath jsonPathString=JsonPath.from(response.asString());
        if(jsonPathString.getString("token")==null && jsonPathString.getString("id")==null && response.statusCode()==400)
            flag=true;

        return msg.equals(jsonPathString.getString("error")) && flag;

    }
}
