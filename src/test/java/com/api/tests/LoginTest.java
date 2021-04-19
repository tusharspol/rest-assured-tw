package com.api.tests;


import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;

public class LoginTest extends HttpInterceptor {

    @Test(description = "Login with correct email and password", suiteName = "login")
    public void verifySuccessfulLogin()
    {
        //created map to set values in json object
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("email",getProperty("email"));
        map.put("password",getProperty("password"));
        JSONObject requestParams = loadRequestWithData(map);

        Response response = httpPost(getProperty("loginURI"), requestParams);
        response.then().log().all();

        token = JsonPath.from(response.asString()).getString("token");
        Assert.assertEquals(response.getStatusCode(),200);
        Assert.assertNotNull(token);
        Assert.assertNotSame(token,"");

    }

    @Test(description = "Login with invalid email id" , suiteName = "login")
    public void loginIfEmailIdInvalid()
    {
        //created map to set values in json object
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("email","test@abc.com");
        map.put("password",getProperty("password"));
        JSONObject requestParams = loadRequestWithData(map);

        Response response = httpPost(getProperty("loginURI"), requestParams);
        response.then().log().all();

        Assert.assertTrue(verifyBadRequestWithMsg(response,getProperty("login.UserInvalidError")));

    }

    @Test(description = "Login with email id and password both are missing" , suiteName = "login")
    public void loginIfEmailAndPasswordMissing()
    {
        //created map to set values in json object
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("email","");
        map.put("password","");
        JSONObject requestParams = loadRequestWithData(map);

        Response response = httpPost(getProperty("loginURI"), requestParams);
        response.then().log().all();

        Assert.assertTrue(verifyBadRequestWithMsg(response,getProperty("login.emailAndPasswordMissingError")));

    }

    @Test(description = "Login with missing password" , suiteName = "login")
    public void verifyFailedLogin()
    {
        //created map to set values in json object
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("email",getProperty("email"));
        map.put("password","");
        JSONObject requestParams = loadRequestWithData(map);

        Response response = httpPost(getProperty("loginURI"), requestParams);
        response.then().log().all();
        Assert.assertTrue(verifyBadRequestWithMsg(response,getProperty("login.PasswordMissingError")));
        }

    // APIs are accepting any password, so this test is failing
    @Test(description = "Login with incorrect password" , suiteName = "login")
    public void verifyIfIncorrectPassword()
    {
        //created map to set values in json object
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("email",getProperty("email"));
        map.put("password","password@123");
        JSONObject requestParams = loadRequestWithData(map);

        Response response = httpPost(getProperty("loginURI"), requestParams);
        response.then().log().all();


        JsonPath jsonPathString=JsonPath.from(response.asString());
        Assert.assertEquals(response.getStatusCode(),400);
        Assert.assertNull(jsonPathString.getString("token"));
    }



    public boolean verifyBadRequestWithMsg(Response response,String msg)
    {
        boolean flag=false;
        JsonPath jsonPathString=JsonPath.from(response.asString());
        if(jsonPathString.getString("token")==null && response.statusCode()==400)
            flag=true;

        return msg.equals(jsonPathString.getString("error")) && flag;

    }
}
