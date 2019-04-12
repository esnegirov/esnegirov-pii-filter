package junit;

import com.slice.models.Item;
import com.slice.models.RegexpTable;
import com.slice.service.PiiFilter;
import com.slice.service.PiiFilterImpl;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class PiiReplaceTest {

    @Test
    public void successReplace(){
        PiiFilter piiFilter = new PiiFilterImpl();
        List<RegexpTable> regExpList = new ArrayList<>();
        regExpList.add(new RegexpTable(1, "\\d+", "*number*"));
        Item item = new Item(1, "description123", "http:\\url123@.com");

        final Item itemResult = piiFilter.replacePii(item, regExpList);

        Assert.assertEquals(itemResult.getDescription(),("description*number*"));
        Assert.assertEquals(itemResult.getUrl(),("http:\\url*number*@.com"));

    }


}
