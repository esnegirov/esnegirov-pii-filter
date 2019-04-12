package com.slice.service;

import com.slice.models.Item;
import com.slice.models.RegexpTable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("piiFilter")
public class PiiFilterImpl implements PiiFilter {

    @Override
    public Item replacePii(Item item, List<RegexpTable> regexpTableList){
        regexpTableList.forEach(regexp -> {
            item.setDescription(item.getDescription().replaceAll(regexp.getRegexpTest(),regexp.getReplaceWithTest()));
            item.setUrl(item.getUrl().replaceAll(regexp.getRegexpTest(),regexp.getReplaceWithTest()));
        });
        return item;
    }

}
