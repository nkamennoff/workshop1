# Big Data Workshop 1
## Goal
During this workshop we aim at installing a simple four node cluster and first use of the following frameworks:
* HDFS : Hadoop Distributed file system
* YARN : Next Generation MapReduce Framework and Resource Manager
* Spark : Large Scale Processing Engine
* Storm : Realtime Computation Framework
* Kafka : Distributed Message Passing System

### Process
During this workshop you will work in group of students.
Handling servers and clicking the next buttons does not require as much work as googling and reading doc is.
We intend you to search by yourselves for answers and discuss among groups.
Indeed, technologies are evoliving very fast, new framework and technologies are appearing everyday.
The ones we present here are representative of those used in real big data architectures but may not be the one you will use for your projects.

We expect you to understand what you are doing and to become autonomous big data engineers with evolution capabilities.
So try to find solutions on your own rather than rush to the repository for corrections.
Try to go as far as you can by your own.

## Technical context
During this workshop you will be provided with Amazon EC2 servers. Each group of student will manage a small cluster of four servers described below:

### Specs
| EC2 Instance | vCPU | RAM |  Storage  | Operating System | Quantity |
-----------------------------------------------------------------------
|  m4.xlarge   |  4   | 16GB| 100GB(EBS)|  CentOS 7 64bit  |    2     |
|  m4.2xlarge  |  8   | 32GB| 200GB(EBS)|  CentOS 7 64bit  |    2     |

### Accessing servers
As all servers on amazon EC2, you must access them using SSH with a registered private key.
Root SSH is obviously not permitted so you must log on through the `centos` user.
This user have sudo capabilities without password.

_Hint: servers FQDNs and RSA private key will be provided during the workshop_

## Stage 1: Deploying and testing the cluster
### Ambari
#### Getting information
You are here asked to define an architecture to create your cluster using [Apache Ambari](http://ambari.apache.org).
We will deploy the services for our workshop:
* HDFS
* YARN
* Zookeeper
* Ambari metrics
* Spark
* Storm
* Kafka

Start answering the following questions:
* Which server(s) will host the HDFS name node ?
* Which server(s) will host HDFS data node ?
* On how many servers will Spark and Yarn Clients must be installed ? Why ?
* What is Supervisor ?
* What is Nimbus ?

_Hint: checkout for resources at the bottom of this document_

#### Deploying
Now deploy your cluster.
_Hint: current ambari version is 2.2_

### HDFS
#### User
Create a user with permission to write on the HDFS file system.
_Hint: HDFS can be manage in two ways, let's keep it to the simplest way._

#### Put a first dataset
Gather a small dataset, Shakespeare's complete work in text file (http://www.gutenberg.org/cache/epub/100/pg100.txt) and store it in the HDFS at location `/user/<your hdfs user>/shakespeare/`
_Hint: curl is a good friend of yours_

#### First spark job
In the hadoop ecosystem, the equivalent of 'hello world!' (i.e. this first simplest program to wrote on a new language) is called WordCount and consists of counting the number of occurences of every word in a text dataset.

Now let's make a wordcount on the shakespeare dataset using Spark processing engine.
_Hint: Spark support bindings for Scala, Java and Python. If you are already a scala developper you may consider to use it. If you don't have past experience we highly recommand you to use Python instead._

## Stage 2: Kafka
### Kafka configuration
By default, kafka listens it's registered network interface. But as we have deploy it on EC2.
Configure through ambari servers configuration to make kafka to listen on its public interface.

### Test
Download a [kafka client](http://kafka.apache.org/downloads.html) and untar it.
Create a new topic called 'test'
_Hint: bin/kafka-topics.sh_

Open a console producer and a console consumer and type on the producer.
_Hint: bin/kafka-console-producer.sh_
_Hint: bin/kafka-console-consumer.sh_

### Kafka meetup
Create a topic called 'meetup'
Launch the python script in `stage2` folder and create consumers to see what is streamed.
Let the kafka python producer running...

## Stage 3: Gathering data from live dataset
We have installed a small cluser and launch a batch processing job on it. Now let's try the stream processing.
We will use the Apache Storm processing engine.
** Warning: ** There is another known project Storm which is a python ORM edited by cannonical, here we refer the Apache Storm project, so please be carefull while you are googling for information.

Storm project are composed of three main java classes:
* Spout : Represents an input stream node
* Bolt : Represents a data manipulation node
* Topology : Represents a analytic graph linking spouts and bolts

### WordCount in Storm
Go to the `stage3` folder and look at the structure.
_Hint: 'tree' can be an useful command._

This is a really basic project.
It generate and emit a stream of random sentences (`spouts/RandomSentenceSpout.java`) then parse split sentences in words (`bolts/SplitBolt.java`) and finally it counts each occurences
of words and emit the result (and prints it in the system console).

In its current state it lacks of the topology definition (`WordCountTopology.java`).
You are here asked to open the topology file with any text editor and to complete the file.
_Hint: Some hints are in the source code files_

Then go to the root folder (where there is the `pom.xml` file).
Clean and compile using Maven:
```shell
mvn clean
mvn package
```

After the project build successfully look again at the folder structure.
A `target` folder must have appeared, containing amongst others, two jars (compiled java files):
* bigdata-workshop_storm-wordcount-0.0.1-SNAPSHOT.jar
* bigdata-workshop_storm-wordcount-0.0.1-SNAPSHOT-jar-with-dependencies.jar

Now you can use the project to submit on the storm cluster:
```shell
storm jar <your jar> <class to execute> [OPT:topology id]
```

The topology Id is used to identify the job on the storm cluster.
As you may have seen in the topology source code file, there is two modes: Local and Cluster.
Local mode is used to debug and check if the topology runs well.
Cluster mode deploys the topology in the Storm Cluster.

```shell
// Local Mode
storm jar target/bigdata-workshop_storm-wordcount-0.0.1-SNAPSHOT-jar-with-dependencies.jar bigdata.workshop.storm.WordCountTopology

// Cluster Mode (just append with a topology id)
storm jar target/bigdata-workshop_storm-wordcount-0.0.1-SNAPSHOT-jar-with-dependencies.jar bigdata.workshop.storm.WordCountTopology wordcount
```
## Stage4: Stream kafka stream consuming with storm
Build a storm topology to consume kafka 'meetup' topic ending with a bolt display the top 10 meetups in term of rsvps.

Now the same for the best trends very 10 minutes.

## Resources
### Websites
#### Design
[https://hadoop.apache.org/docs/r1.2.1/hdfs_design.html]
#### HDFS
[https://hadoop.apache.org/docs/current/hadoop-project-dist/hadoop-hdfs/HdfsPermissionsGuide.html]

### MOOCs
#### Udacity
Realtime Analytics with Apache Storm
