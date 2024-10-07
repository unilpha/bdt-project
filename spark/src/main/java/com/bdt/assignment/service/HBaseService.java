package com.bdt.assignment.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.conf.Configuration;

import java.io.IOException;

public class HBaseService {
    public void insertData(String jsonData) {

        Configuration config = HBaseConfiguration.create();
        config.set("hbase.zookeeper.quorum", "localhost");
        config.set("hbase.zookeeper.property.clientPort", "2181");

        try (Connection connection = ConnectionFactory.createConnection(config);
             Table table = connection.getTable(TableName.valueOf("crypto_prices"))) {


            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(jsonData);

            JsonNode bitcoinNode = rootNode.get("bitcoin");
            double bitcoinPrice = bitcoinNode.get("usd").asDouble();
            String bitcoinRowKey = "bitcoin_" + System.currentTimeMillis();
            Put bitcoinPut = new Put(Bytes.toBytes(bitcoinRowKey));
            bitcoinPut.addColumn(Bytes.toBytes("price_data"), Bytes.toBytes("usd"), Bytes.toBytes(bitcoinPrice));
            table.put(bitcoinPut);

            JsonNode ethereumNode = rootNode.get("ethereum");
            double ethereumPrice = ethereumNode.get("usd").asDouble();
            String ethereumRowKey = "ethereum_" + System.currentTimeMillis();
            Put ethereumPut = new Put(Bytes.toBytes(ethereumRowKey));
            ethereumPut.addColumn(Bytes.toBytes("price_data"), Bytes.toBytes("usd"), Bytes.toBytes(ethereumPrice));
            table.put(ethereumPut);

            System.out.println("Data inserted successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
