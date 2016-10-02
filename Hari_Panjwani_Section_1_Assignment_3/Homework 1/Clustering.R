#adding the required libraries
Needed <- c("tm", "SnowballCC", "RColorBrewer", "ggplot2", "wordcloud", "biclust", "cluster", "igrap
h", "fpc", "wordcloud")
#install.packages("wordcloud")
#install.packages(Needed, dependencies=TRUE)
#install.packages("Rcampdf", repos = "http://datacube.wu.ac.at/", type = "source")

#setting the file path of the directory
cname <- file.path("C:/Users/saksh/R Workspace/Week 3 - Text Mining in R", "data")
cname
dir(cname)


library(tm)
docs <- Corpus(DirSource(cname))
summary(docs)

#remove numbers
docs <- tm_map(docs, removeNumbers)

#remove punctuations
docs <- tm_map(docs, removePunctuation)

#convert to lower case
docs <- tm_map(docs, tolower)

#remove stopwords
docs <- tm_map(docs, removeWords, stopwords("english"))

#stripping whitespaces from the documents
docs <- tm_map(docs, stripWhitespace)

#preprocess document as plain text
docs <- tm_map(docs, PlainTextDocument)

#document-term matrix
dtm <- DocumentTermMatrix(docs)
inspect(dtm)
dim(dtm)

#term-document matrix
tdm <- TermDocumentMatrix(docs)
tdm

freq <- colSums(as.matrix(dtm))
freq

ord <- order(freq)

#removing sparse terms
dtms <- removeSparseTerms(dtm, 0.1)
inspect(dtms)


#creating a wordcloud
library(wordcloud)
set.seed(111)

word = names(freq)

wordcloud(word,freq, max.words = 20,  rot.per = 0.2, colors = rainbow(7, s=0.8, v=0.8))

#this makes a matrix which is only 15% empty
dtmss <- removeSparseTerms(dtm, 0.5)
inspect(dtmss)

#clustering by term similarity
#heirarchal clustering
library(cluster)
d <- dist(t(dtms), method = "euclidian")
fit <- hclust(d=d, method = "ward.D2")
#fit

#'k' depicts the number of cluster
plot(fit, hang = -1)
groups <- cutree(fit, k=5)
rect.hclust(fit, k=5, border="red")

#k-means clustering
library(fpc)
d <- dist(t(dtmss), method = "euclidian")
kfit <- kmeans(d, 2)
clusplot(as.matrix(d), kfit$cluster, color = T, shade = T, labels=2, lines = 0)