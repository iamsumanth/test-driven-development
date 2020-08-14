package two;

import java.util.List;

public class OrderListener {

    private OrderService orderService;

    public OrderListener(OrderService orderService) {

        this.orderService = orderService;
    }

    public void process(List<Order> orders) {
        if (orders.isEmpty()) {
            throw new InvalidOrderException();
        }
        try {
            orderService.publish(orders);
        } catch (PublishFailedException exception) {
            throw new OrderIsNotProcessed();
        }
    }
}
