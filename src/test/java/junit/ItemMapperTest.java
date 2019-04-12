package junit;

import com.slice.models.Item;
import com.slice.service.ItemMapper;
import com.slice.service.ItemMapperImpl;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class ItemMapperTest {

    @Test
    public void itemFromJsonTest(){
        ItemMapper orderMapper = new ItemMapperImpl();
        String fileName = "137.json";
        String itemJson = "{\"id\":137, \"description\": \"The Moustache Grower's Guide\", \"url\": \"http://www.amazon.com/dp/0811878805\"}";
        Map<String, String> itemHolder = new HashMap<>();
        itemHolder.put(fileName, itemJson);
        Item item = new Item(137, "The Moustache Grower's Guide", "http://www.amazon.com/dp/0811878805");
        try {
            Assert.assertEquals(item, orderMapper.itemFromJson(itemHolder).get(fileName));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
