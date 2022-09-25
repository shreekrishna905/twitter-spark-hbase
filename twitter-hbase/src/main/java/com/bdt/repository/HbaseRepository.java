package com.bdt.repository;

import java.io.IOException;
import java.util.UUID;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;
import org.springframework.stereotype.Component;

import com.bdt.Constant;



@Component
public class HbaseRepository {

	private Configuration configuration;

	public HbaseRepository() {
		configuration = HBaseConfiguration.create();
	}

	public void insert(String hash, String count) {	
		try (Connection connection = ConnectionFactory
				.createConnection(configuration);
				Admin admin = connection.getAdmin()){
			Table tbl = connection.getTable(TableName.valueOf(Constant.TABLE_NAME));
			HTableDescriptor table = new HTableDescriptor(
					TableName.valueOf(Constant.TABLE_NAME));
			table.addFamily(new HColumnDescriptor(Constant.TWEET_HASH_FAMILY));
			table.addFamily(new HColumnDescriptor(Constant.TWEET_COUNT_FAMILY));
			if (!admin.tableExists(table.getTableName())) {
				admin.createTable(table);
			}
			Put put = new Put(Bytes.toBytes(String.valueOf(UUID.randomUUID().toString())));
			put.addColumn(Bytes.toBytes(Constant.TWEET_HASH_FAMILY), Bytes.toBytes(Constant.HASH_COLUMN),
					Bytes.toBytes(hash));
			put.addColumn(Bytes.toBytes(Constant.TWEET_COUNT_FAMILY),
					Bytes.toBytes(Constant.COUNT_COLUMN), Bytes.toBytes(count));
			tbl.put(put);
			tbl.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
