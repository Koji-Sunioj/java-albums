import java.util.ArrayList;
import java.util.stream.IntStream;
import java.util.*;



class Main {

    static String row = "| %-8s | %-20s | %-25s | %-5s | %-5s |";

    static void showInventory(Album[] albumList) {
        print("\ncurrent stock:\n");
        String stockRow = row + " %-5s |";
        String header = String.format(stockRow, "album id", "artist name", "album name", "year", "stock", "price");
        print(header);
        String divider = "-".repeat(header.length());
        print(divider);
        for (Album album : albumList) {
            String inventoryRow = String.format(stockRow, album.albumId, album.artistName, album.albumName, album.year, album.stock, album.price);
            print(inventoryRow);
        }
    }

     static Album[] initialize() {
        Album SOTLB = new Album(141, "dissection", "storm of the light's bane", 1993, 12.99, 5);
        Album DOP = new Album(279, "immolation", "dawn of possession", 1996, 8.20, 3);
        Album SR = new Album(672, "skeletal remains", "condemmned to misery", 2012, 10.34, 1);
        Album SOM = new Album(882, "dissection", "the somberlain", 1991, 10.20, 2);
        Album NOK = new Album(293, "nokturnal mortum", "nechrist", 1998, 8.59, 1);
        Album DAR = new Album(923, "necrophobic", "darkside", 1995, 12.99, 10);
        Album NOC = new Album(384, "necrophobic", "the nocturnal silence", 1993, 6.82, 4);
        Album MORS = new Album(832, "mors principium est", "... and death said live", 2012, 19.33, 7);
        Album NIG = new Album(295, "nightwish", "once", 2005, 18.20, 21);
        Album INF = new Album(495, "in flames", "the jester race", 1997, 12.22, 2);
        Album[] albumList = {SOTLB, DOP, SR, SOM, NOK, DAR, NOC, MORS, NIG, INF};
        return albumList;
    }

    static void print(String sentence) {
        System.out.println(sentence);
    }

    static void showCart(ArrayList<CartItem> cart,String type) {
        if (cart.size() > 0) {
            String rowHeader = String.format(row, "id", "artist", "album", "amount", "price");
            String header = type == "cart" ? "\nyour cart:\n" : "\norder items:\n";
            print(header);
            print(rowHeader);
            String divider = "-".repeat(rowHeader.length());
            print(divider);
            double balance = 0;
            for (CartItem cartItem : cart) {
                balance += cartItem.quantity * cartItem.price;
                String cartRow = String.format(row, cartItem.albumId, cartItem.artistName, cartItem.albumName, cartItem.quantity, cartItem.price);
                print(cartRow);
            }
            print(String.format("\nbalance: %.2f", balance));
        }
    }

    static int getAlbumIndex(Album[] albumList, String albumId) {
        return IntStream.range(0, albumList.length)
                .filter(index -> albumList[index].albumId == Integer.parseInt(albumId)).findFirst()
                .orElse(-1);
    }

    static void showOrder(ArrayList<Order> orders) {
        if (orders.size() > 0) {
            Order lastOrder = orders.get(orders.size() - 1);
            print(String.format("order id: %s\ndate: %s", lastOrder.orderId, lastOrder.createdAt.getTime()));
            showCart(lastOrder.items,"order");
        }
    }

    static int getCartIndex(ArrayList<CartItem> cart, String albumId) {
        return IntStream.range(0, cart.size())
                .filter(index -> cart.get(index).albumId == Integer.parseInt(albumId)).findFirst()
                .orElse(-1);
    }

    public static void main(String[] args) {
        ArrayList<Order> orders = new ArrayList<>();
        ArrayList<CartItem> cart = new ArrayList<>();
        Album[] albumList = initialize();
        String message = null;
        String field = "albumId";
        String direction = "descending";
        boolean shopping = true;
        while (shopping) {
            System.out.print("\033[H\033[2J");
            System.out.flush();
            if (message != null) print(message);
            print(String.format("\ncurrently sorted as: %s %s",direction,field));
            showOrder(orders);
            Arrays.sort(albumList, new SortAlbum(direction,field));
            showInventory(albumList);
            showCart(cart,"cart");
            print("\ntype \"add album <id number> to add to cart\" or \"remove album <id number>\" to remove from your cart. type \"exit\" to leave, or \"checkout\" to create an order.\nyou can sort the list by entering \"sort <column name> <ascending | descending>\"");
            Scanner reader = new Scanner(System.in);
            String[] input = reader.nextLine().split(" ");
            try
            {
                switch (input[0].toLowerCase()) {
                    case "sort":
                        field=input[1].toLowerCase();
                        if (input.length ==4)
                        {
                            field += Character.toUpperCase(input[2].charAt(0))+input[2].substring(1);
                            direction = input[3];
                        }
                        else
                        {
                            direction = input[2];
                        }
                        break;
                    case "add":
                        try {
                            int albumIndex = getAlbumIndex(albumList, input[1]);
                            Album targetAlbum = albumList[albumIndex];
                            if (targetAlbum.stock > 0)
                            {
                                int cartIndex = getCartIndex(cart, input[1]);
                                if (cartIndex >= 0)
                                {
                                    cart.get(cartIndex).quantity++;
                                    message = "\nupdated quantity in cart";
                                }
                                else
                                {
                                    CartItem newItem = new CartItem(targetAlbum.albumId, targetAlbum.artistName, targetAlbum.albumName, targetAlbum.price, 1);
                                    cart.add(newItem);
                                    message = "\nadded new item to cart";
                                }
                                albumList[albumIndex].stock -= 1;
                            }
                            else {
                                message = "\nnot enough stock";
                            }
                        } catch (Exception error) {
                            message = "\nno album found";
                        }
                        break;
                    case "remove":
                        try {
                            int cartIndex = getCartIndex(cart, input[1]);
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
                        if (cart.size() > 0) {
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
            catch (Exception error)
            {
                print("invalid command");
            }


        }
    }
}