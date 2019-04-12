package com.slice.service;

import com.slice.models.Item;
import com.slice.models.RegexpTable;

import java.util.List;

public interface PiiFilter {
    Item replacePii(Item item, List<RegexpTable> regexpTableList);
}
