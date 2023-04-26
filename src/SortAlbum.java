import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;

class SortAlbum implements Comparator<Album> {

    public String direction;
    public String field;

    public SortAlbum(String direction,String field)
    {
        this.direction = direction.toLowerCase();
        this.field = field;
    }

    @Override
    public int compare(Album o1, Album o2) {
        String[] intFields = { "albumId", "year", "stock"};
        boolean isNumeric = Arrays.asList(intFields).contains(this.field);
        int index = 0;
        Field albumField = null;
        try {
            albumField = o2.getClass().getDeclaredField(this.field);
            if (isNumeric == true)
            {
                int value1 = (int) albumField.get(o2);
                int value2 = (int) albumField.get(o1);
                index = this.direction.contains("ascending") ? value1 - value2 : value2 - value1;
            }
            else if (this.field.contains("price"))
            {
                double value1 = (double) albumField.get(o2);
                double value2 = (double) albumField.get(o1);
                index = this.direction.contains("ascending") ? Double.compare(value1,value2) : Double.compare(value2,value1) ;
            }
            else
            {
                String value1 = (String) albumField.get(o2);
                String value2 = (String) albumField.get(o1);
                index = this.direction.contains("ascending") ? value1.compareTo(value2): value2.compareTo(value1);
            }
        } catch (NoSuchFieldException  | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return index;
    }
}