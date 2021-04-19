package com.api.tests;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.json.simple.JSONObject;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;

public class CreateTest extends HttpInterceptor{

    @Test(description = "Verify successful Create scenario")
    public void createSuccess()
    {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("name",getProperty("create.name"));
        map.put("job",getProperty("create.job"));
        JSONObject requestParams = loadRequestWithData(map);
        Response response = httpPost(getProperty("createURI"), requestParams);
        response.then().log().all();

        JsonPath jsonPathString=JsonPath.from(response.asString());
        Assert.assertEquals(response.getStatusCode(),201);
        Assert.assertEquals(getProperty("create.name"),jsonPathString.getString("name"));
        Assert.assertEquals(getProperty("create.job"),jsonPathString.getString("job"));
        Assert.assertNotNull(jsonPathString.getString("id"));
        Assert.assertNotNull(jsonPathString.getString("createdAt"));


    }

    //this test is failing, as there is no validation at create api and it always returns success
    @Test(description = "Verify Create request with missing name and job")
    public void createWithMissingNameAndJob()
    {
        HashMap<String, String> map = new HashMap<String, String>();
        JSONObject requestParams = loadRequestWithData(map);
        Response response = httpPost(getProperty("createURI"), requestParams);
        response.then().log().all();
        Assert.assertTrue(verifyBadRequestWithMsg(response));

    }

    //this test is failing, as there is no validation at create api and it always returns success
    @Test(description = "Verify Create request with missing name")
    public void createWithMissingName()
    {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("job",getProperty("create.job"));
        JSONObject requestParams = loadRequestWithData(map);
        Response response = httpPost(getProperty("createURI"), requestParams);
        response.then().log().all();
        Assert.assertTrue(verifyBadRequestWithMsg(response));

    }

    //this test is failing, as there is no validation at create api and it always returns success
    @Test(description = "Verify Create request with missing job")
    public void createWithMissingJob()
    {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("name",getProperty("create.name"));
        JSONObject requestParams = loadRequestWithData(map);
        Response response = httpPost(getProperty("createURI"), requestParams);
        response.then().log().all();
        Assert.assertTrue(verifyBadRequestWithMsg(response));

    }

    public boolean verifyBadRequestWithMsg(Response response)
    {
        boolean flag=false;
        JsonPath jsonPathString=JsonPath.from(response.asString());
        if(jsonPathString.getString("name")==null && jsonPathString.getString("job")==null
                && jsonPathString.getString("id")==null && response.statusCode()==400
                && jsonPathString.getString("createdAt")==null)
            flag=true;

        return  flag;

    }
}
