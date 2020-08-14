package two;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

public class OrderTest {

    @Test
    public void shouldCreateOrdersFromMessage() throws JsonProcessingException {
        String message = "[\n" +
                "  {\n" +
                "  \"salesOrderId\": \"123456\",\n" +
                "  \"customerId\": \"QWERASDF\",\n" +
                "  \"status\": \"SUCCESS\",\n" +
                "  \"timeStamp\": \"2019-08-21T11:05:03\"\n" +
                "  }]";

        ObjectMapper objectMapper = new ObjectMapper();
        Order[] orders = objectMapper.readValue(message, Order[].class);

        Order order = new Order("123456", "QWERASDF", "SUCCESS", "2019-08-21T11:05:03");

        assertEquals(Arrays.asList(orders), Arrays.asList(order));
    }

    @Test
    public void validationShouldFailWhenSalesOrderIdIsNull() throws JsonProcessingException {
        String message = "[\n" +
                "  {\n" +
                "  \"salesOrderId\": null,\n" +
                "  \"customerId\": \"QWERASDF\",\n" +
                "  \"status\": \"SUCCESS\",\n" +
                "  \"timeStamp\": \"2019-08-21T11:05:03\"\n" +
                "  }]";

        ObjectMapper objectMapper = new ObjectMapper();
        Order[] orders = objectMapper.readValue(message, Order[].class);

        boolean isValid = areValidOrders(Arrays.asList(orders));
        assertFalse(isValid);
    }

    @Test
    public void validationShouldFailWhenSalesOrderIdIsEmpty() throws JsonProcessingException {
        String message = "[\n" +
                "  {\n" +
                "  \"salesOrderId\": \"\",\n" +
                "  \"customerId\": \"QWERASDF\",\n" +
                "  \"status\": \"SUCCESS\",\n" +
                "  \"timeStamp\": \"2019-08-21T11:05:03\"\n" +
                "  }]";

        ObjectMapper objectMapper = new ObjectMapper();
        Order[] orders = objectMapper.readValue(message, Order[].class);

        boolean isValid = areValidOrders(Arrays.asList(orders));
        assertFalse(isValid);
    }

    @Test
    public void validationShouldFailWhenStatusIsNull() throws JsonProcessingException {
        String message = "[\n" +
                "  {\n" +
                "  \"salesOrderId\": \"123456\",\n" +
                "  \"customerId\": \"QWERASDF\",\n" +
                "  \"status\": null,\n" +
                "  \"timeStamp\": \"2019-08-21T11:05:03\"\n" +
                "  }]";

        ObjectMapper objectMapper = new ObjectMapper();
        Order[] orders = objectMapper.readValue(message, Order[].class);

        boolean isValid = areValidOrders(Arrays.asList(orders));
        assertFalse(isValid);
    }

    @Test
    public void validationShouldFailWhenStatusIsEmpty() throws JsonProcessingException {
        String message = "[\n" +
                "  {\n" +
                "  \"salesOrderId\": \"123456\",\n" +
                "  \"customerId\": \"QWERASDF\",\n" +
                "  \"status\": \"\",\n" +
                "  \"timeStamp\": \"2019-08-21T11:05:03\"\n" +
                "  }]";

        ObjectMapper objectMapper = new ObjectMapper();
        Order[] orders = objectMapper.readValue(message, Order[].class);

        boolean isValid = areValidOrders(Arrays.asList(orders));
        assertFalse(isValid);
    }

    private boolean areValidOrders(List<Order> orders) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Order>> constraintViolationsErrors = validator.validate(orders.get(0));
        return constraintViolationsErrors.isEmpty();
    }


//    @Test
//    public void validationShouldPassAfterCreatingOrderFromMessage() throws JsonProcessingException {
//        String message = "[\n" +
//                "  {\n" +
//                "  \"salesOrderId\": \"123456\",\n" +
//                "  \"customerId\": \"QWERASDF\",\n" +
//                "  \"status\": \"SUCCESS\",\n" +
//                "  \"timeStamp\": \"2019-08-21T11:05:03\"\n" +
//                "  }]";
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        Order[] orders = objectMapper.readValue(message, Order[].class);
//
//        boolean isValid = areValidOrders(Arrays.asList(orders));
//        assertTrue(isValid);
//    }

}
