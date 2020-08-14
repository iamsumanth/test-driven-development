package two;

import java.time.LocalDateTime;
import javax.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class Order {
    @NotEmpty
    private String salesOrderId;
    private String customerId;
    @NotEmpty
    private String status;
    private String timeStamp;

    public Order() {
    }

    public Order(String salesOrderId, String customerId, String status, String timeStamp) {
        this.salesOrderId = salesOrderId;
        this.customerId = customerId;
        this.status = status;
        this.timeStamp = timeStamp;
    }

    public boolean isValidOrderTime() {
        return isInvalidOrderTime(this);
    }

    private boolean isInvalidOrderTime(Order order) {
        LocalDateTime orderTime = LocalDateTime.parse(order.getTimeStamp());
        return !orderTime.isAfter(LocalDateTime.now());
    }

    public boolean isSuccessfulOrder() {
        return this.getStatus().equals("SUCCESS");
    }
}
