// ===================================================================
// BIGDATA WORKSHOP ASSIGNEMENT APPEARS IN COMMENTS
// ===================================================================
package bigdata.workshop.storm;

/** Storm imports **/
import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.testing.TestWordSpout;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import backtype.storm.utils.Utils;

/** Java imports **/
import java.util.Map;

/** Project imports **/
import bigdata.workshop.storm.spouts.RandomSentenceSpout;
import bigdata.workshop.storm.bolts.SplitBolt;
import bigdata.workshop.storm.bolts.CountBolt;
import bigdata.workshop.storm.bolts.PrintBolt;

/** WordCountTopology
 ** =================
 ** Very simple topology for the workshop.
 ** Spout create and emit random sentences
 */
public class WordCountTopology {
    /** Entry Point (main)
     ** ==================
     ** First Method called
     */
    public static void main(String[] args) throws Exception
    {
	/** Initialize the topology builder */
	TopologyBuilder builder = new TopologyBuilder();

	// ============================================================================
	// ASSIGNEMENT : DEFINE THE TOPOLOGY USING SPOUT AND BOLTS
	// You have one Spout (RandomSentenceSpout)
	// You have three Bolts (SplitBolt, CountBolt and PrintBolt)
	// SplitBolt: Get the first element of a tuple as a String and split on " .?!;,()[]" and emit each word.
	// CountBolt: Get the first element of a tuple as a String and stores the number of occurence already encountered. Emit a tuple with the element<String> and its current count<Integer>
	//
	// To add a node to the topology you have to set it using the following methods:
	// For a SPOUT
	// builder.setSpout("<id>", <Spout>, <parallelism>);
	// the next line instanciate a spout to generate words, its id is "rand-word" and it go a parallelism level of 3
	builder.setSpout("rand-word", new TestWordSpout(), 3);
	// For BOLT, it's quite the same except that Bolt should connect to spout or other bolt.
	// builder.setBolt("<id>", <Bolt>, <parallelism>).{shuffle|fields|global|all}Grouping("<source id>"[, <fields>]);
	// There is 4 major way to connect :
	//  - shuffle (distributes randomly the stream among the connected bolts)
	//  - fields (distributes the stream among the connected bolts based on a key field)
	//  - all (replicates the stream toward all connected nodes)
	//  - global (transfers all the stream toward a single node)
	// the next line instanciate a bolt and connect it to the spout.
	builder.setBolt("print-rand-word", new PrintBolt(), 1).globalGrouping("rand-word");
	// Now change this topology and the PrintBolt to get data from the RandomSentenceSpout, count word occurences and display them.

	
	/** Launch locally or on the cluster **/
	Config conf = new Config();
	if (args != null && args.length > 0) {
	    conf.setDebug(false);
	    conf.setNumWorkers(4);
	    StormSubmitter.submitTopology(args[0], conf, builder.createTopology());
	} else {
	    conf.setDebug(true);
	    LocalCluster cluster = new LocalCluster();
	    cluster.submitTopology("wordcount", conf, builder.createTopology());
	    Thread.sleep(20000);
	    cluster.killTopology("wordcount");
	    cluster.shutdown();
	}
    }
}
