package com.bdt.assignment.service;


import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.bdt.assignment.data.CryptoData;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaPairInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka.KafkaUtils;

import kafka.serializer.StringDecoder;
import org.codehaus.jackson.map.ObjectMapper;

public class SparkKafkaConsumerService {


    public static void main(String[] args)  {
        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println("Spark Streaming started now .....");

        SparkConf conf = new SparkConf().setAppName("kafka-sandbox").setMaster("local[*]");
        JavaSparkContext sc = new JavaSparkContext(conf);
        JavaStreamingContext ssc = new JavaStreamingContext(sc, new Duration(20000));

        Set<String> topics = Collections.singleton("crypto_prices");


        Map<String, String> kafkaParams = new HashMap<>();
        kafkaParams.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        kafkaParams.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "largest");

        JavaPairInputDStream<String, String> directKafkaStream = KafkaUtils.createDirectStream(ssc, String.class,
                String.class, StringDecoder.class, StringDecoder.class, kafkaParams, topics);

        directKafkaStream.foreachRDD(rdd -> {
            System.out.println("New data arrived  " + rdd.partitions().size() + " Partitions and " + rdd.count() + " Records");
            if (rdd.count() > 0) {
                rdd.collect().forEach(rawRecord -> {
                    System.out.println("rawRecord = " + rawRecord);
                    System.out.println(rawRecord);
                    System.out.println("***************************************");
                    System.out.println(rawRecord._2);
                    String record = rawRecord._2();
                    CryptoData cryptoData = null;
                    try {
                        cryptoData = objectMapper.readValue(record, CryptoData.class);
                    } catch (Exception e) {
                        System.out.println("Error in parsing record : " + record);
                    }
                    System.out.println("cryptoData = " + cryptoData);
                    HBaseService hBaseDataInserter = new HBaseService();
                    hBaseDataInserter.insertData(record);

                });
            }
        });

        ssc.start();
        ssc.awaitTermination();
    }


}



