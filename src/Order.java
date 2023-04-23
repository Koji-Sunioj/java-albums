import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

public class Order {
    public String orderId;
    public Calendar createdAt;

    public ArrayList<CartItem> items;

    public Order(ArrayList<CartItem> cart){
        ArrayList<CartItem> cartCopy = new ArrayList<CartItem>();
        for (int i = 0;i < cart.size(); i++)
        {
            cartCopy.add(cart.get(i));
        }
        this.orderId = String.valueOf(UUID.randomUUID());
        this.createdAt = Calendar.getInstance();
        this.items = cartCopy;
    }
}
