import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

public class Order {
    public String orderId;
    public Calendar createdAt;

    public ArrayList<CartItem> items;

    public Order(ArrayList<CartItem> items){
        this.orderId = String.valueOf(UUID.randomUUID());
        this.createdAt = Calendar.getInstance();
        this.items = items;
    }
}
