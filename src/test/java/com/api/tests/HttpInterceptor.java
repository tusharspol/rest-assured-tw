package com.api.tests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.simple.JSONObject;
import org.testng.annotations.BeforeClass;
import utility.FrameworkUtility;

public class HttpInterceptor extends FrameworkUtility {

    private  RequestSpecification request;
    @BeforeClass
    public void setUp()
    {
        request = null;
        RestAssured.baseURI = getProperty("Base_URI");
        request = RestAssured.given()
                .contentType(ContentType.JSON);
    }

    protected static String token;
    public Response httpPost(String uri, JSONObject body){

        return request.log().all()
                .body(body.toJSONString())
                .when()
                .post(uri);
    }

    public Response httpGet(String uri, String param)
    {
        return request.log().all()
                .when()
                .get(uri+"/"+param);
    }
    public Response httpGet(String uri, String param,String value)
    {
        return request.log().all()
                .param(param,value)
                .when()
                .get(uri);
    }

    public Response httpDelete(String uri, String param)
    {

        return request.log().all()
                .when()
                .delete(uri+"/"+param);
    }

    public boolean verifyBadRequestWithMsg(Response response,String msg)
    {
        boolean flag=false;
        JsonPath jsonPathString=JsonPath.from(response.asString());
        if(msg.equals(jsonPathString.getString("error")) && response.statusCode()==400)
            flag=true;

        return flag;

     }

}
