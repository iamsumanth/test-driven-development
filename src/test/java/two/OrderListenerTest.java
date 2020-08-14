package two;

import org.junit.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;

public class OrderListenerTest {

    @Test(expected = InvalidOrderException.class)
    public void shouldThrowExceptionWhenOrdersAreEmpty() {
        OrderListener orderListener = new OrderListener(null);

        List<Order> orders = Collections.emptyList();
        orderListener.process(orders);
    }

    @Test
    public void shouldPassOrderToService() {
        OrderService orderService = Mockito.mock(OrderService.class);
        OrderListener orderListener = new OrderListener(orderService);

        Order order = new Order("123456", "QWERASDF", "SUCCESS", "2019-08-21T11:05:03");
        List<Order> orders = Collections.singletonList(order);
        orderListener.process(orders);

        Mockito.verify(orderService).publish(Arrays.asList(order));
    }

    @Test
    public void shouldPassOrdersToService() {
        OrderService orderService = Mockito.mock(OrderService.class);
        OrderListener orderListener = new OrderListener(orderService);

        Order order1 = new Order("123456", "QWERASDF", "SUCCESS", "2019-08-21T11:05:03");
        Order order2 = new Order("999999", "AAAAAA", "SUCCESS", "2019-08-21T11:05:03");
        List<Order> orders = Arrays.asList(order1, order2);
        orderListener.process(orders);

        Mockito.verify(orderService).publish(orders);
    }

    @Test(expected = OrderIsNotProcessed.class)
    public void shouldThrowExceptionWhenWeCannotPublishOrders() {
        OrderService orderService = Mockito.mock(OrderService.class);
        OrderListener orderListener = new OrderListener(orderService);

        Order order1 = new Order("123456", "QWERASDF", "SUCCESS", "2019-08-21T11:05:03");
        Order order2 = new Order("999999", "AAAAAA", "SUCCESS", "2019-08-21T11:05:03");
        List<Order> orders = Arrays.asList(order1, order2);
        Mockito.doThrow(new PublishFailedException()).when(orderService).publish(orders);
        orderListener.process(orders);
    }

}
