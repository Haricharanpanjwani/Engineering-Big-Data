setwd("C:/Users/saksh/R Workspace/")

#setting the enviornment variable for the plotly
Sys.setenv("plotly_username"="panjwani.h")
Sys.setenv("plotly_api_key"="8w5f8e9g0f")

#importing the required library
library(plotly)
library(ggplot2)
library(plyr)
library(gridExtra)
library(RColorBrewer)

# Load the dataset
rawCrime <- read.csv("sfoCrime.csv")
crime <- read.csv("crime.csv")

# Plot a histogram showing the frequency of each crime category
crimeCount <- cbind(aggregate(crime[, "IncidntNum"], by = list(crime$Category), sum))
colnames(crimeCount) <- c("Category", "Frequency")

h <- ggplot(crimeCount, aes(reorder(Category, Frequency), Frequency))
crimeFrequency <- h + geom_bar(stat="identity") + 
                  ggtitle("Frequency of crime in the SF Bay area") + 
                  ylab("Number of reports") + xlab("Crime category") + 
                  theme(axis.text.x = element_text(angle=90, hjust=1)) + 
                  geom_text(aes(label=round(Frequency), size = 2, hjust = 0.5, vjust = -1))

#publish to plotly
plotly_POST(crimeFrequency, filename = "crime-frequency-sf-bay-area")

### What days and times are especially dangerous?
# Subset only high crime frequencies
crimeHigh <- subset(crime, Category == "LARCENY/THEFT" | Category == "ASSAULT" | 
                      Category == "VANDALISM" | Category == "VEHICLE THEFT" | Category == "BURGLARY" | 
                      Category == "DRUG/NARCOTIC" | Category == "ROBBERY")
crimeHigh <- droplevels(crimeHigh)

# Sort the crime categories in decreasing order
crimeHigh <- within(crimeHigh, Category <- factor(Category, levels = names(sort(table(Category), decreasing = T))))
crimePd <- within(crime, PdDistrict <- factor(PdDistrict, levels = names(sort(table(PdDistrict), decreasing = T))))

# What are the crime patterns by categories and disricts according to DayOfWeek
catDow <- ggplot(data = crimeHigh, aes(x = DayOfWeek, fill = Category)) + 
          geom_bar(width = 0.9) + ggtitle("Frequency of Crime by District and Category")
          labs(x = "Day of Week", y = "Number of reports", fill = guide_legend(title = "Crime category"))

pdDow <- ggplot(data = crimePd, aes(x = DayOfWeek, fill = PdDistrict)) + 
        geom_bar(width = 0.9) + ggtitle("Frequency of Crime by District and Category")
        labs(x = "Day of Week", y = "Number of reports", fill = guide_legend(title = "District"))

#cpDow <- grid.arrange(pdDow, catDow, nrow=2)

cpDow <- subplot(ggplotly(catDow), 
                 ggplotly(pdDow), 
                 nrows = 2, margin = 0.05)

plotly_POST(cpDow, filename = "crime-frequency-Crime-category-and-by-District")


# To prepare for smoonth_geom plots
# Larceny
larc <- subset(crime, crime$Category == "LARCENY/THEFT")
larc <- droplevels(larc)
larcTime <- ddply(larc, c('Category', 'Time'), summarise, totalCat = sum(IncidntNum, na.rm=T))

# Assault
asst <- subset(crime, crime$Category == "ASSAULT")
asst <- droplevels(asst)
asstTime <- ddply(asst, c('Category', 'Time'), summarise, totalCat = sum(IncidntNum, na.rm=T))

# Vandalism
vand <- subset(crime, crime$Category == "VANDALISM")
vand <- droplevels(vand)
vandTime <- ddply(vand, c('Category', 'Time'), summarise, totalCat = sum(IncidntNum, na.rm=T))

# VEHICLE THEFT
vehc <- subset(crime, crime$Category == "VEHICLE THEFT")
vehc <- droplevels(vehc)
vehcTime <- ddply(vehc, c('Category', 'Time'), summarise, totalCat = sum(IncidntNum, na.rm=T))

# BURGLARY
burg <- subset(crime, crime$Category == "BURGLARY")
burg <- droplevels(burg)
burgTime <- ddply(burg, c('Category', 'Time'), summarise, totalCat = sum(IncidntNum, na.rm=T))

# DRUG/NARCOTIC
narc <- subset(crime, crime$Category == "DRUG/NARCOTIC")
narc <- droplevels(narc)
narcTime <- ddply(narc, c('Category', 'Time'), summarise, totalCat = sum(IncidntNum, na.rm=T))

# ROBBERY
robb <- subset(crime, crime$Category == "ROBBERY")
robb <- droplevels(robb)
robbTime <- ddply(robb, c('Category', 'Time'), summarise, totalCat = sum(IncidntNum, na.rm=T))


larcPlot <- ggplot(larcTime, aes(x=Time, y=totalCat, group=1)) + geom_point(colour="red", size=2) + 
  geom_smooth(method="loess") + labs(x = "Time (24-hour interval)", y = "Number of reports") +
  ggtitle("Larceny/Theft vs Time")

asstPlot <- ggplot(asstTime, aes(x=Time, y=totalCat, group=1)) + geom_point(colour="blue", size=2) + 
  geom_smooth(method="loess") + labs(x = "Time (24-hour interval)", y = "Number of reports") +
  ggtitle("Assault vs Time")

vandPlot <- ggplot(vandTime, aes(x=Time, y=totalCat, group=1)) + geom_point(colour="darkgreen", size=2) + 
  geom_smooth(method="loess") + labs(x = "Time (24-hour interval)", y = "Number of reports") + 
  ggtitle("Vandalism vs Time")

vehcPlot <- ggplot(vehcTime, aes(x=Time, y=totalCat, group=1)) + geom_point(colour="purple", size=2) + 
  geom_smooth(method="loess") + labs(x = "Time (24-hour interval)", y = "Number of reports") +
  ggtitle("Vehicle Theft vs Time")

burgPlot <- ggplot(burgTime, aes(x=Time, y=totalCat, group=1)) + geom_point(colour="orange", size=2) + 
  geom_smooth(method="loess") + labs(x = "Time (24-hour interval)", y = "Number of reports") + 
  ggtitle("Burglary vs Time")

narcPlot <- ggplot(narcTime, aes(x=Time, y=totalCat, group=1)) + geom_point(colour="black", size=2) + 
  geom_smooth(method="loess") + labs(x = "Time (24-hour interval)", y = "Number of reports") + 
  ggtitle("Drug/Narcotic vs Time")

robbPlot <- ggplot(robbTime, aes(x=Time, y=totalCat, group=1)) + geom_point(colour="brown", size=2) + 
  geom_smooth(method="loess") + labs(x = "Time (24-hour interval)", y = "Number of reports") + 
  ggtitle("Robbery vs Time")

grid.arrange(larcPlot, asstPlot, vandPlot, vehcPlot, burgPlot, narcPlot, robbPlot, ncol=3)


df <- layout(
      subplot(
        ggplotly(larcPlot),
        ggplotly(asstPlot),
        ggplotly(vandPlot),
        ggplotly(vehcPlot),
        ggplotly(burgPlot),
        ggplotly(narcPlot),
        ggplotly(robbPlot),
        margin = 0.05, shareX = TRUE, shareY = TRUE, nrows = 3),
        title = "Crime vs Time"
      )

plotly_POST(df,  filename = 'Crime-name-vs-time')

# Heatmap of District/Category
pdCatheat <- ddply(rawCrime, c("PdDistrict", "Category"), summarise, 
                   totalCrime = sum(IncidntNum, na.rm=T))

brks <- c(1,10^rep(1:6))
pdCatheat$bin <- cut(pdCatheat$totalCrime, breaks=brks, labels=1:6, include.lowest=T)

majorArea <- ggplot(pdCatheat, aes(y = Category, x = PdDistrict)) + geom_tile(aes(fill=bin)) + 
            scale_fill_manual(name="Crime Incidents", labels=brks, values=rev(brewer.pal(6,"Spectral"))) + 
            xlab("") + ylab("") + ggtitle("Heatmap of crime by District/Category")

plotly_POST(majorArea, filename = 'Heatmap of District/Category')