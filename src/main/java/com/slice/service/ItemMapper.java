package com.slice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.slice.models.Item;

import java.util.Map;

public interface ItemMapper {

    Map<String, Item> itemFromJson(Map<String, String> itemList) throws Exception;

    String itemToJson(Item item) throws JsonProcessingException;

}
