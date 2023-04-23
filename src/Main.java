import javax.swing.plaf.synth.SynthTabbedPaneUI;
import java.util.ArrayList;
import java.util.stream.IntStream;
import java.util.*;

public class Main {

    static String row = "| %-4s | %-20s | %-25s | %-5s | %-5s |";

    public static void showInventory(Album[] albumList) {
        String stockRow = row + " %-5s |";
        String header = String.format(stockRow, "id", "artist", "album", "year", "stock", "price");
        print(header);
        String divider = "-".repeat(header.length());
        print(divider);
        for (int index = 0; index < albumList.length; index++) {
            Album selected = albumList[index];
            String inventoryRow = String.format(stockRow, selected.albumId, selected.artistName, selected.albumName, selected.releasedYear, selected.stock, selected.price);
            print(inventoryRow);
        }
    }

    public static Album[] initialize() {
        Album SOTLB = new Album(141, "dissection", "storm of the light's bane", 1993, 12.99, 5);
        Album DOP = new Album(279, "immolation", "dawn of possession", 1996, 8.20, 3);
        Album SR = new Album(672, "skeletal remains", "condemmned to misery", 2012, 10.34, 1);
        Album[] albumList = {SOTLB, DOP, SR};
        return albumList;
    }

    static void print(String sentence) {
        System.out.println(sentence);
    }

    static void showCart(ArrayList<CartItem> cart) {
        if (cart.size() > 0) {
            String header = String.format(row, "id", "artist", "album", "amount", "price");
            print("\n your cart:\n");
            print(header);
            String divider = "-".repeat(header.length());
            print(divider);
            double balance = 0;
            for (int index = 0; index < cart.size(); index++) {
                CartItem selected = cart.get(index);
                balance += selected.quantity * selected.price;
                String cartRow = String.format(row, selected.albumId, selected.artistName, selected.albumName, selected.quantity, selected.price);
                print(cartRow);
            }
            print(String.format("\nyour balance: %.2f", balance));
        }
    }

    static int getAlbumIndex(Album[] albumList, String albumId) {
        return IntStream.range(0, albumList.length)
                .filter(index -> albumList[index].albumId == Integer.valueOf(albumId)).findFirst()
                .orElse(-1);
    }

    static void showOrder(ArrayList<Order> orders){
        if (orders.size() > 0)
        {
            Order lastOrder = orders.get(orders.size()-1);
            print(String.format("order id: %s\ndate: %s",lastOrder.orderId,String.valueOf(lastOrder.createdAt.getTime())));
            print(String.valueOf(lastOrder.items.size()));
             for (int i = 0; i < lastOrder.items.size();i++){
                CartItem selected = lastOrder.items.get(i);
                print(selected.artistName);
            }
        }
    }

    static int getCartIndex(ArrayList<CartItem> cart, String albumId) {
        return IntStream.range(0, cart.size())
                .filter(index -> cart.get(index).albumId == Integer.valueOf(albumId)).findFirst()
                .orElse(-1);
    }

    public static void main(String[] args) {
        ArrayList<Order> orders = new ArrayList<Order>();
        ArrayList<CartItem> cart = new ArrayList<CartItem>();
        Album[] albumList = initialize();
        String message = null;
        Boolean shopping = true;
        while (shopping) {
            System.out.print("\033[H\033[2J");
            System.out.flush();
            if (message != null) print(message);
            showOrder(orders);
            print("\ncurrent stock:\n");
            showInventory(albumList);
            showCart(cart);
            print("\ntype \"add album <id number> to add to cart\" or \"remove album <id number>\" to remove from your cart. type \"exit\" to leave, or \"checkout\" to create an order");
            Scanner reader = new Scanner(System.in);
            String[] input = reader.nextLine().split(" ");
            switch (input[0].toLowerCase()) {
                case "add":
                    try {
                        int albumIndex = getAlbumIndex(albumList, input[1]);
                        int cartIndex = getCartIndex(cart, input[1]);
                        Album selected = albumList[albumIndex];
                        if (selected.stock > 0) {
                            if (cart.size() > 0 && cartIndex != -1) {
                                cart.get(cartIndex).quantity++;
                                message = "\nupdated quantity in cart";
                            } else {
                                CartItem newItem = new CartItem(selected.albumId, selected.artistName, selected.albumName, selected.price, 1);
                                cart.add(newItem);
                                message = "\nadded cart in album";
                            }
                            albumList[albumIndex].stock -= 1;
                        } else {
                            message = "\nnot enough stock";
                        }
                    } catch (Exception error) {
                        message = "\nno album found";
                    }
                    break;
                case "remove":
                    try {
                        int cartIndex = getCartIndex(cart, input[1]);
                        print(String.valueOf(cartIndex));
                        int albumIndex = getAlbumIndex(albumList, input[1]);
                        if (cartIndex != -1) {
                            cart.get(cartIndex).quantity--;
                            albumList[albumIndex].stock++;
                            if (cart.get(cartIndex).quantity == 0) {
                                cart.remove(cartIndex);
                                message = "\nalbum removed from cart";
                            } else {
                                message = "\nquantity updated in cart";
                            }
                        } else {
                            message = "\nno items in cart";
                        }
                    } catch (Exception error) {
                        message = "\nno album with that id exists";
                    }
                    break;
                case "checkout":
                    if (cart.size() > 0){
                        Order newOrder = new Order(cart);
                        orders.add(newOrder);
                        cart.clear();
                        message = "\norder created";
                    }
                    break;
                case "exit":
                    shopping = false;
                    break;
                default:
                    print("unknown command");
            }
        }
    }
}