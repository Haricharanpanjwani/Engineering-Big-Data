#reading the insurance dataset
data <- read.csv("C:/Users/saksh/Documents/4th Sem/Assignments/Assignment 6/InsuranceData.csv")
View(summary(data))

#plotted the boxplot to find the outliers in the dataset
boxplot(data$loss)

#compute the number of rows greater than 10000, as the data
#whicha is going to be removed should not be greater than
# total 5% percent of the dataset
nrow(data[data$loss > 10000,])

#putting the rows which has loss column value greater than 1000 in the dataset p
p <- data[(data$loss > 10000),]

#compute the summary of the loss column to get the mean
summary(p$loss)
