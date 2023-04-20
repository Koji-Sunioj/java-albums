import java.util.ArrayList;

public class Album {
    public int albumId;
    public String albumName;
    public String artistName;
    public int releasedYear;

    public double price ;

    public int stock;

    public Album ( int albumId, String artistName,String albumName,int releasedYear,double price,int stock)
    {
        this.albumId = albumId;
        this.albumName = albumName;
        this.artistName = artistName;
        this.releasedYear = releasedYear;
        this.price =price;
        this.stock = stock;
    }

}