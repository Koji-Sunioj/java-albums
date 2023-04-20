import java.util.ArrayList;
import java.util.stream.IntStream;
import java.util.*;

public class Main {

    static String row = "| %-4s | %-20s | %-25s | %-5s | %-5s |";

    public static void showInventory(Album[] albumList)
    {
        String stockRow = row+" %-5s |";
        String header = String.format(stockRow,"id","artist","album","year","stock","price");
        print(header);
        String divider = "-".repeat(header.length());
        print(divider);
        for (int index = 0; index < albumList.length;index++)
        {
            Album selected = albumList[index];
            String inventoryRow = String.format(stockRow,selected.albumId,selected.artistName,selected.albumName,selected.releasedYear,selected.stock,selected.price);
            print(inventoryRow);
        }
    }

    public static Album[] initialize(){
        Album SOTLB = new Album(141,"dissection","storm of the light's bane",1993,12.99,5);
        Album DOP = new Album(279,"immolation","dawn of possession",1996,8.20,3);
        Album SR = new Album(672,"skeletal remains","condemmned to misery",2012,10.34,1);
        Album[] albumList = {SOTLB, DOP, SR};
        return albumList;
    }

    static void print(String sentence){
        System.out.println(sentence);
    }

    static void showOptions(){
        print("\nwelcome to the record shop. please choose an option:");
        String[] options = {"1=go shopping","2=go to cart"};
        print(String.join("\n",options));
    }

    static void showCart(ArrayList<CartItem> cart){
        String header = String.format(row,"id","artist","album","amount","price");
        print(header);
        String divider = "-".repeat(header.length());
        print(divider);
        for (int index = 0; index < cart.size(); index++)
        {
            CartItem selected = cart.get(index);
            String cartRow = String.format(row,selected.albumId,selected.artistName,selected.albumName,selected.quantity,selected.price);
            print(cartRow);
        }
    }

    public static void main(String[] args) {
        ArrayList<CartItem> cart = new ArrayList<CartItem>();
        Album[] albumList = initialize();
        showOptions();
        Boolean showMenu = true;
        Boolean shopping = false;
        while (showMenu)
        {
            Scanner reader = new Scanner(System.in);
            String input = reader.nextLine();
            switch (input)
            {
                case "1":
                    showMenu = false;
                    shopping = true;
                    break;
                default:
                    print("hey");
            }
        }

        while (shopping)
        {
            print("\ncurrent stock:\n");
            showInventory(albumList);
            print("\ntype \"add album <id number> to add to cart\" or \"remove album <id number>\" to remove from your cart.");
            Scanner reader = new Scanner(System.in);
            String[] input = reader.nextLine().split(" ");
            switch (input[0].toLowerCase())
            {
                case "add":
                    try {
                        int albumIndex = IntStream.range(0, albumList.length)
                                .filter(index -> albumList[index].albumId == Integer.valueOf(input[1]))
                                .findFirst()
                                .getAsInt();
                        int cartIndex = IntStream.range(0, cart.size())
                                .filter(index -> cart.get(index).albumId == Integer.valueOf(input[1])).findFirst()
                                .orElse(-1);
                        if (cart.size() > 0 && cartIndex != -1){
                            cart.get(cartIndex).quantity ++;
                        }
                        else {
                            Album selected = albumList[albumIndex];
                            CartItem newItem = new CartItem(selected.albumId,selected.artistName,selected.albumName,selected.price,1);
                            cart.add(newItem);
                        }
                        albumList[albumIndex].stock -= 1;
                        showCart(cart);
                    }
                    catch(Exception error) {
                        print("no album found");
                    }
                    break;


            }


        }
    }
}