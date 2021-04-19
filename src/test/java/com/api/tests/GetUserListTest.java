package com.api.tests;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import pogo.Support;
import pogo.UserData;



public class GetUserListTest extends HttpInterceptor{


    @Test(description = "Verify get user List")
    public void verifyGetUserList()
    {
        Response response= httpGet(getProperty("listURI"),"page","2");
        Assert.assertEquals(response.getStatusCode(),200);

        JsonPath jsonPathString=JsonPath.from(response.asString());

        Assert.assertEquals(jsonPathString.getString("page"),"2");
        Assert.assertEquals(jsonPathString.getString("per_page"),getProperty("per_page"));
        Assert.assertNotNull(jsonPathString.getString("total"));
        Assert.assertNotNull(jsonPathString.getString("total_pages"));

        UserData[] users = getUserListFromUserDataJson(response);
        for(UserData user:users)
            Assert.assertTrue(verifyUserDataObject(user));

        Support support = getSupportJsonObject(response);
        Assert.assertNotNull(support.getUrl());
        Assert.assertNotNull(support.getText());

    }


    @Test(description = "verify get single user")
    public void verifyGetSingleUser()
    {
        Response response= httpGet(getProperty("listURI"),"1");
        Assert.assertEquals(response.getStatusCode(),200);
    }

    @Test(description = "verify get single user not found")
    public void verifyGetSingleUserNotFound()
    {
        Response response= httpGet(getProperty("listURI"),"20");
        Assert.assertEquals(response.getStatusCode(),404);
    }


    public UserData[] getUserListFromUserDataJson(Response response)
    {
        return response.jsonPath().getObject("data",UserData[].class);
    }

    public boolean verifyUserDataObject(UserData user)
    {
        return user.getId() != null && user.getEmail() != null && user.getFirst_name() != null && user.getLast_name() != null && user.getAvatar() != null;
    }
    public Support getSupportJsonObject(Response response)
    {
        return response.jsonPath().getObject("support", Support.class);
    }
}
