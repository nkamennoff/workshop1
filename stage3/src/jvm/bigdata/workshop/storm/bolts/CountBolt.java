package bigdata.workshop.storm.bolts;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import backtype.storm.utils.Utils;

import java.util.Map;
import java.util.HashMap;

public class CountBolt extends BaseRichBolt
{
    private OutputCollector _collector;
    private Map<String, Integer> _countMap;

    @Override
    public void prepare(Map map, TopologyContext topologyContext, OutputCollector collector)
    {
	_collector = collector;

	_countMap = new HashMap<String, Integer>();
    }

    @Override
    public void execute(Tuple tuple)
    {
	String word = tuple.getString(0);
	int count = _countMap.containsKey(word) ? _countMap.get(word) + 1: 1;

	_countMap.put(word, count);
	_collector.emit(new Values(word, count));
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer)
    {
	declarer.declare(new Fields("word", "count"));
    }
}
