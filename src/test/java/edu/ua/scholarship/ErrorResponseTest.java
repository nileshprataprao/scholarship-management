package edu.ua.scholarship;

import edu.ua.scholarship.dto.ErrorResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


public class ErrorResponseTest {


    @Test
    void test_error_response() {

       ErrorResponse errorResponse = new ErrorResponse("400" , "Invalid Request");

       Assertions.assertEquals("400", errorResponse.getCode());

   }
}
