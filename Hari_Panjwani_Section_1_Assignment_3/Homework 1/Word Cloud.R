#setting the working directory
setwd("C:/Users/saksh/R Workspace/Week 3 - Text Mining in R/")

#install.packages('tm')
#install.packages('qdap')
library(qdap)
library(tm)

#change the text file from iliad to odyssey.txt for the word cloud
#Loading in the data
reviews <- read.table("data/odyssey.txt", 
                      sep=" ", 
                      fill=TRUE, header = FALSE,
                      strip.white=TRUE, quote=NULL)
reviews

#setting up source and corpus
review_source=VectorSource(reviews)
Corpus=Corpus(review_source)

#cleaning
#removing whitespaces, punctuations, stop words
Corpus=tm_map(Corpus,content_transformer(tolower))
Corpus=tm_map(Corpus,removePunctuation)
Corpus=tm_map(Corpus,stripWhitespace)
Corpus=tm_map(Corpus, removeWords, stopwords("english"))

#Making a document-term matric
dtm=DocumentTermMatrix(Corpus)
dtm2=as.matrix(dtm)

#Finding the most frequent terms
frequency=colSums(dtm2)
frequency=sort(frequency, decreasing=TRUE)
frequency

#install.packages('wordcloud')
library(wordcloud)

#getting the words from the frequency
words=names(frequency)

#defining the color code for the word cloud
colorcode = rev(colorRampPalette(brewer.pal(9,"BuGn"))(32)[seq(20,32,6)])

#creating the word cloud
wordcloud(words[1:100], frequency[10:200], scale=c(5,.2), colors = rainbow(7, s=0.8, v=0.8))

wordcloud(names(frequency), frequency, max.words = 100, rot.per = 0.2, colors = rainbow(7, s=0.8, v=0.8))
