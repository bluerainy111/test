package com.atguigu.cdc;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.ververica.cdc.connectors.mysql.MySQLSource;
import com.alibaba.ververica.cdc.connectors.mysql.table.StartupOptions;
import com.alibaba.ververica.cdc.debezium.DebeziumDeserializationSchema;
import com.alibaba.ververica.cdc.debezium.DebeziumSourceFunction;
import com.alibaba.ververica.cdc.debezium.StringDebeziumDeserializationSchema;
import io.debezium.data.Envelope;
import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.runtime.state.filesystem.FsStateBackend;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;
import org.apache.kafka.connect.data.Field;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.source.SourceRecord;

import java.util.List;


public class FlinkCDC03_Custom {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
/*        //设置检查点 flink-cdc将读取到的binlog的位置信息以状态的的方式保存在ck如果要做断点续传,需要从检查点或者保存点读取
        //开启检查点 每5s进行一次  选择检查点对齐
        env.enableCheckpointing(5000L, CheckpointingMode.EXACTLY_ONCE);
        //设置检查点超时时间
        env.getCheckpointConfig().setCheckpointTimeout(60000L);
        //取消job后,检查点是否保留
        env.getCheckpointConfig().enableExternalizedCheckpoints(CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);
        //设置检查点重启策略
        env.setRestartStrategy(RestartStrategies.fixedDelayRestart(3,3000L));
        //设置状态后端
        env.setStateBackend(new FsStateBackend("hdfs://hadoop102:8020/gmall/ck"));
        System.setProperty("HADOOP_USER_NAME","atguigu");*/



        DebeziumSourceFunction<String> sourceFunction = MySQLSource.<String>builder()
                .hostname("hadoop102")
                .port(3306)
                .databaseList("gmall0519-realtime")
                ///可选配置项,如果不指定该参数,则会读取上一个配置中指定的数据库下的所有表的数据
                //注意：指定的时候需要使用"db.table"的方式
                .tableList("gmall0519-realtime.table_process")
                .username("root")
                .password("123456")
                .deserializer(new MyCustomSchema())
                .startupOptions(StartupOptions.initial())
                .build();
        env.addSource(sourceFunction).print();
        env.execute();
    }
    public static class MyCustomSchema implements DebeziumDeserializationSchema<String>{

        @Override
        public void deserialize(SourceRecord sourceRecord, Collector<String> collector) throws Exception {
            Struct valueStruct = (Struct) sourceRecord.value();

            Struct sourceStruct = valueStruct.getStruct("source");
            String database = sourceStruct.getString("db");
            String table = sourceStruct.getString("table");
            //String type = valueStruct.getString("op");
            String type = Envelope.operationFor(sourceRecord).toString().toLowerCase();
            if (type.equals("create")){
                type="insert";
            }
            JSONObject dateJsonObj = new JSONObject();
            Struct afterStruct = valueStruct.getStruct("after");
            if (afterStruct!=null){
                List<Field> fields = afterStruct.schema().fields();
                for (Field field : fields) {
                    String name = field.name();
                    Object fieldValue = afterStruct.get(field);
                    dateJsonObj.put(name,fieldValue);
                }
            }
            JSONObject result = new JSONObject();
            result.put("database", database);
            result.put("table", table);
            result.put("type", type);
            result.put("data", dateJsonObj);
            collector.collect(result.toJSONString());
        }

        @Override
        public TypeInformation<String> getProducedType() {
            return TypeInformation.of(String.class);
        }
    }
}
