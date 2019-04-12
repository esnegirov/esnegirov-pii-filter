package com.slice.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.slice.models.Item;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service("orderMapper")
public class ItemMapperImpl implements ItemMapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(ItemMapperImpl.class);

    private ObjectMapper objectMapper = new ObjectMapper();

    public Map<String, Item> itemFromJson(Map<String, String> itemList) throws Exception {

        Map <String, Item> mappedList = new HashMap<>();

        itemList.forEach((fileName, fileAsItem) ->{
            try {
                mappedList.put(fileName, objectMapper.readValue(fileAsItem, Item.class));
            } catch (IOException e) {
                LOGGER.info("ERROR, can't mapping file to Item.class");
                e.printStackTrace();

            }
        });
        return mappedList;
    }

    @Override
    public String itemToJson(Item item) throws JsonProcessingException {
            return objectMapper.writeValueAsString(item);

    }


}
