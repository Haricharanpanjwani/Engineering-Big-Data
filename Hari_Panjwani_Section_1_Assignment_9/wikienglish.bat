awk '{$3+=1}1' oldInput.txt > newInput.txt

sed "s/\\t/\,/g" newInput.txt >> input.txt

bin/hadoop jar mahout-core-0.7-job.jar org.apache.mahout.cf.taste.hadoop.item.RecommenderJob -Dmapred.input.dir=WikiEnglish/input.txt -Dmapred.output.dir=output --usersFile WikiEnglish/users.txt --booleanData true --similarityClassname SIMILARITY_COOCCURRENCE

../../hadoop-0.20.2/bin/hadoop fs -copyToLocal output "../WikiEnglish/"

For finding which datanode are active
bin/hadoop dfsadmin -report
 
<property>
    <name>mapred.child.java.opts</name>
    <value>-Xmx1024m</value>
</property>

set map and reduce tasks maximum to optimize your clusters in your configuration file as following:
<property>
<name>mapred.tasktracker.map.tasks.maximum</name>
<value>4</value>
</property>
<property>
<name>mapred.tasktracker.reduce.tasks.maximum</name>
<value>2</value>

172.31.36.235 ec2-54-186-60-56.us-west-2.compute.amazonaws.com master
172.31.40.94 ec2-54-202-245-133.us-west-2.compute.amazonaws.com slave
172.31.40.96 ec2-54-202-250-147.us-west-2.compute.amazonaws.com slave2
172.31.40.95 ec2-54-186-212-30.us-west-2.compute.amazonaws.com slave3
172.31.40.97 ec2-54-149-152-34.us-west-2.compute.amazonaws.com slave4
172.31.35.173 ec2-54-203-9-80.us-west-2.compute.amazonaws.com slave5

Following error occurred

jetty server
port error
heap error

Solution

bin/hadoop dfsadmin -safemode leave
fsck