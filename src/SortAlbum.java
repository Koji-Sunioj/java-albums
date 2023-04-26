import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;

class SortAlbum implements Comparator<Album> {

    public String direction;
    public String field;

    public SortAlbum(String direction,String field)
    {
        this.direction = direction;
        this.field = field;
    }

    @Override
    public int compare(Album o1, Album o2) {
        String[] intFields = { "albumId", "releasedYear", "price", "stock"};
        boolean isNumeric = Arrays.stream(intFields).anyMatch(this.field::equals);
        int index = 0;
        Field albumField = null;
        try {
            albumField = o2.getClass().getDeclaredField(this.field);
            if (isNumeric == true)
            {
                int value1 = (int) albumField.get(o2);
                int value2 = (int) albumField.get(o1);
                index = this.direction == "ascending" ? value1 -  value2 : value2 -  value1;
            }
            else
            {
                String value1 = (String) albumField.get(o2);
                String value2 = (String) albumField.get(o1);
                index = this.direction == "ascending" ? value1.compareTo(value2): value2.compareTo(value1);
            }
        } catch (NoSuchFieldException  | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return index;
    }
}