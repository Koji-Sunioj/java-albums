import java.util.ArrayList;

public class CartItem {
    public int albumId;
    public String albumName;
    public String artistName;

    public double price ;

    public int quantity;

    public CartItem ( int albumId, String artistName,String albumName,double price,int stock)
    {
        this.albumId = albumId;
        this.albumName = albumName;
        this.artistName = artistName;
        this.price =price;
        this.quantity = stock;
    }

}
