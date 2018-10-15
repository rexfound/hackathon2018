package com.ze.hackathon.Hackathon2018.service;

import com.ze.hackathon.Hackathon2018.DBConnectionHelper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.mongodb.client.model.Sorts.ascending;
import static com.mongodb.client.model.Sorts.orderBy;

/**
 * Created by handy.kestury on 6/12/2018.
 */
@Service
public class DataService {

  MongoDatabase database;


  public MongoDatabase initConnection() {
    if(database == null) {
      database = DBConnectionHelper.getConnection();
    }
    return database;
  }
}
