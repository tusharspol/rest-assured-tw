package com.api.tests;

import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

public class DeleteUserTest extends HttpInterceptor{

    @Test(description = "Verify successful delete user")
    public void deleteSuccessful()
    {

        Response response = httpDelete(getProperty("deleteURI"),"3");
        Assert.assertEquals(response.getStatusCode(),204);

    }

    //this test is failing , as API not giving error when id is missing in request
    @Test(description = "Verify delete request with missing param id")
    public void deleteWithMissingIdParam()
    {

        Response response = httpDelete(getProperty("deleteURI"),"");
        Assert.assertEquals(response.getStatusCode(),400);

    }


    //this test is failing , as there is no validation of id in the request
    @Test(description = "Verify delete request with invalid param id")
    public void deleteWithInvalidIdParam()
    {

        Response response = httpDelete(getProperty("deleteURI"),"10");
        Assert.assertEquals(response.getStatusCode(),400);

    }


    //this test is failing , as there is no validation of id in the request
    @Test(description = "Verify delete request with invalid format param id")
    public void deleteWithInvalidFormatParam()
    {

        Response response = httpDelete(getProperty("deleteURI"),"abc");
        Assert.assertEquals(response.getStatusCode(),400);

    }


}
