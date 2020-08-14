package two;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class OrderServiceTest {

  private OrderRepository orderRepository;
  private OrderPublisher publisher;
  private OrderService orderService;
  private Order order;

  @Before
  public void setUp() {
    orderRepository = Mockito.mock(OrderRepository.class);
    publisher = Mockito.mock(OrderPublisher.class);
    orderService = new OrderService(publisher, orderRepository);

    order = new Order("234567", "WWERASDF", "PENDING", "2019-08-21T11:05:03");
    when(orderRepository.exists(order.getSalesOrderId())).thenReturn(false);
  }

  @Test
  public void shouldSaveAndPublishOrdersWithStatusSuccess() {
    Order order = new Order("234567", "QWERASDF", "SUCCESS", "2019-08-21T11:05:03");

    orderService.publish(Arrays.asList(order));

    verify(publisher).publish(Arrays.asList(order));
    verify(orderRepository).save(Arrays.asList(order));
  }

  @Test
  public void shouldNotSaveAndPublishOrdersWithStatusPending() {
    Order order = new Order("234567", "WWERASDF", "PENDING", "2019-08-21T11:05:03");

    orderService.publish(Arrays.asList(order));

    verify(publisher,never()).publish(anyList());
    verify(orderRepository,never()).save(anyList());
  }

  @Test
  public void shouldSaveAndPublishOrdersOnlyWithStatusSuccess() {
    final OrderPublisher publisher = Mockito.mock(OrderPublisher.class);
    final OrderService orderService = new OrderService(publisher, orderRepository);
    Order secondOrder = new Order("234567", "QWERASDF", "SUCCESS", "2019-08-21T11:05:03");

    orderService.publish(Arrays.asList(order,secondOrder));

    verify(publisher).publish(Arrays.asList(secondOrder));
    verify(orderRepository).save(Arrays.asList(secondOrder));
  }

  @Test
  public void shouldSaveAndPublishUnpublishedOrdersOnly() {
    Order firstOrder = new Order("234567", "WWERASDF", "SUCCESS", "2019-08-21T11:05:03");
    Order secondOrder = new Order("123456", "QWERASDF", "SUCCESS", "2019-08-21T11:05:03");

    when(orderRepository.exists(firstOrder.getSalesOrderId())).thenReturn(true);
    orderService.publish(Arrays.asList(firstOrder,secondOrder));

    verify(publisher).publish(Arrays.asList(secondOrder));
    verify(orderRepository).save(Arrays.asList(secondOrder));
  }

  @Test
  public void shouldNotPublishEmptyListWhenAllOrdersAreInvalid() {
    Order firstOrder = new Order("234567", "WWERASDF", "SUCCESS", "2019-08-21T11:05:03");
    Order secondOrder = new Order("123456", "QWERASDF", "SUCCESS", "2019-08-21T11:05:03");

    when(orderRepository.exists(firstOrder.getSalesOrderId())).thenReturn(true);
    when(orderRepository.exists(secondOrder.getSalesOrderId())).thenReturn(true);
    orderService.publish(Arrays.asList(firstOrder,secondOrder));

    verify(publisher,never()).publish(anyList());
    verify(orderRepository,never()).save(anyList());
  }

  @Test
  public void shouldNotSaveAndPublishWhenOrderIsInFutureTime() {
    LocalDateTime dateTime = LocalDateTime.now();
    LocalDateTime futureTime = dateTime.plusDays(2);
    Order order = new Order("123456", "QWERASDF", "SUCCESS", futureTime.toString());
    List<Order> orders = Collections.singletonList(order);
    orderService.publish(orders);

    verify(publisher,never()).publish(anyList());
    verify(orderRepository,never()).save(anyList());
  }

  @Test
  public void shouldIgnoreFutureOrdersAndSaveAndPublishOtherOrders() {
    LocalDateTime dateTime = LocalDateTime.now();
    LocalDateTime futureTime = dateTime.plusDays(2);
    Order order1 = new Order("123456", "QWERASDF", "SUCCESS", futureTime.toString());
    Order order2 = new Order("999999", "AAAAAA", "SUCCESS", "2019-08-21T11:05:03");
    List<Order> orders = Arrays.asList(order1, order2);
    orderService.publish(orders);

    verify(publisher).publish(Arrays.asList(order2));
    verify(orderRepository).save(Arrays.asList(order2));
  }
}
