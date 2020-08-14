package two;

import java.util.List;
import java.util.stream.Collectors;

public class OrderService {
    private final OrderPublisher publisher;
    private final OrderRepository repository;

    public OrderService(OrderPublisher publisher, OrderRepository repository) {
        this.publisher = publisher;
        this.repository = repository;
    }

    public void publish(List<Order> orders) {
        final List<Order> successfulUnpublishedOrders = getValidOrders(orders);
        if(!successfulUnpublishedOrders.isEmpty()) {
            repository.save(successfulUnpublishedOrders);
            publisher.publish(successfulUnpublishedOrders);
        }
    }

    private List<Order> getValidOrders(List<Order> orders) {
        return orders.stream()
          .filter(Order::isValidOrderTime)
          .filter(Order::isSuccessfulOrder)
          .filter(order ->
            !repository.exists(order.getSalesOrderId()))
          .collect(Collectors.toList());
    }
}
