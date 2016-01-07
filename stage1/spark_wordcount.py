# =========================================================================================
# BIGDATA WORKSHOP ASSIGNEMENT APPEARS IN COMMENT
# =========================================================================================
from pyspark import SparkContext, SparkConf

if __name__ == "__main__":
    # Create Spark context and spark configuration
    conf = SparkConf().setAppName("WordCount").setMaster("yarn-client")
    sc = SparkContext(conf=conf)

    # =====================================================================================
    # Point to the data file on hdfs
    text_file = sc.textFile("...")
    # =====================================================================================

    # This first create load the file in a RDD (FlatMap)
    # Then map each word with the integer 1
    # and reduce using words as a key and sum of the value (1)
    counts = text_file.flatMap(lambda line: line.split(" ")).map(lambda word: (word, 1)).reduceByKey(lambda a, b: a + b)

    # =====================================================================================
    # Point to the result folder
    counts.saveAsTextFile("hdfs://ip-172-31-19-124.eu-west-1.compute.internal/user/testuser/exo1/wordcount.txt")
    # =====================================================================================
