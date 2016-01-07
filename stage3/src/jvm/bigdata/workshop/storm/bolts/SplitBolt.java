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

public class SplitBolt extends BaseRichBolt
{
    private OutputCollector _collector;

    @Override
    public void prepare(Map map, TopologyContext topologyContext, OutputCollector collector)
    {
	_collector = collector;
    }

    @Override
    public void execute(Tuple tuple)
    {
	String line = tuple.getString(0);

	String[] words = line.split(" ");
	for (String word: words) {
	    _collector.emit(new Values(word));
	}
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer)
    {
	declarer.declare(new Fields("word"));
    }
}
