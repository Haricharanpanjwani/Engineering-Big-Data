Commands to run the GroupLens file on Pseudo and Fully Distributed mode

scp -r "inputGroupLens" saksh@192.168.56.101:/home/saksh

bin/hadoop fs -put "../Hadoop_Workspace/inputGroupLens/u.data" recommenderInput/u.data

bin/hadoop fs -put "../Hadoop_Workspace/inputGroupLens/users.txt" recommenderInput/users.txt

../../hadoop-0.20.2/bin/hadoop fs -copyToLocal outputItr* "C:\cygwin64\home\saksh\Hadoop Workspace\PageRank\outputFile"

#recommendation for all users
hadoop jar mahout-core-0.7-job.jar org.apache.mahout.cf.taste.hadoop.item.RecommenderJob -s SIMILARITY_COOCCURRENCE --input recommenderInput/u.data --output recommenderInput/output


bin/hadoop jar mahout-core-0.7-job.jar org.apache.mahout.cf.taste.hadoop.item.RecommenderJob -Dmapred.input.dir=recommenderInput/u.data -Dmapred.output.dir=recommenderInput/OutputUser --usersFile recommenderInput/users.txt --booleanData false --similarityClassname SIMILARITY_COOCCURRENCE

#standalone mode
bin/hadoop jar mahout-core-0.7-job.jar org.apache.mahout.cf.taste.hadoop.item.RecommenderJob -Dmapred.input.dir=../Hadoop_Workspace/inputGroupLens/u.data -Dmapred.output.dir=../Hadoop_Workspace/inputGroupLens/OutputUser --usersFile ../Hadoop_Workspace/inputGroupLens/users.txt --booleanData false --similarityClassname SIMILARITY_COOCCURRENCE
