#install.packages("foreign")
#install.packages("ggplot2")
#install.packages("MASS")
#install.packages("Hmisc")
#install.packages("reshape2")

#FOR PCA
#install.packages("corrr")
#install.packages("ggcorrplot")
#install.packages("FactoMineR")
#install.packages("factoextra")
#install.packages("rjson")
#install.packages("stringr")
#install.packages("nnet")

require(nnet)
require(foreign)
require(ggplot2)
require(MASS)
require(Hmisc)
require(reshape2)
library(dplyr)

#FOR PCA
library(FactoMineR)
library(corrr)
library(ggcorrplot)
library(factoextra)
library("stringr") 

is.na.data.frame <- function(x)
do.call(cbind, lapply(x, is.na))

setwd("E:/aspects/spaProductLine/fractalDynamicDataCollector/datasetAnalysis")


trainData <- read.csv('./sources/annotated_train.csv', sep=";")
testData <- read.csv('./sources/annotated_test.csv', sep=";")


variablePointData <- as.data.frame(read.csv('./sources/vp_multi.csv', sep=";"))
variablePointData[is.na(variablePointData)] <- 0


variablePointDataTrain <- variablePointData[is.element(variablePointData$name, trainData$Name), ]
variablePointDataTest <- variablePointData[is.element(variablePointData$name, testData$Name), ]




nameLength <- length(colnames(variablePointDataTrain))
# GET DATA FOR COLUMN NAMES
usedColumnNames <- colnames(variablePointDataTrain)
numericalVPDataTrain <- variablePointDataTrain[usedColumnNames]
numericalVPDataTest <- variablePointDataTest[usedColumnNames]

trainData <- trainData[order(trainData$Name),]
variablePointDataTrain <- variablePointDataTrain[order(variablePointDataTrain$name),]
testData <- testData[order(testData$Name),]
variablePointDataTest <- variablePointDataTest[order(variablePointDataTest$name),]

variablePointDataTrain
#trainData <- cbind(trainData, variablePointDataTrain[!names(variablePointDataTrain) %in% names(trainData)])
#testData <- cbind(testData, variablePointDataTest[!names(variablePointDataTest) %in% names(testData)])
#print(head(trainData[1:8]))
print(colnames(variablePointDataTest))


variablePointDataTrain[,"perceivedAesthetics"] <- factor(trainData[,"perceivedAesthetics"])
#variablePointDataTrain["perceivedAesthetics"] <- trainData["perceivedAesthetics"]
# ORDINAL LOGISTIC REGRESSION WITH AIC AND ALL COLUMNS
#ordinalModel <- polr(perceivedAesthetics ~ ., data = variablePointDataTrain, method = c("logistic"), Hess=TRUE) %>% stepAIC(trace = FALSE)



#usedColnames = c("perceivedAesthetics", colnames(variablePointDataTrain[2:110])) #0,2828947
usedColnames = c("perceivedAesthetics", colnames(variablePointDataTrain[2:100])) #0,34210
#usedColnames = c("perceivedAesthetics", colnames(variablePointDataTrain[2:50])) #0,309
linearModel <- multinom(perceivedAesthetics ~ ., family=multinomial, data = variablePointDataTrain[usedColnames]) %>% stepBIC(trace = FALSE)

variablePointDataTest[,"perceivedAesthetics"] <- as.factor(testData[,"perceivedAesthetics"])
#variablePointDataTest["perceivedAesthetics"] <- testData["perceivedAesthetics"]
predictedTest <- predict(linearModel, newdata=variablePointDataTest[usedColnames])

#variablePointDataTest[,"perceivedAesthetics"] <- as.factor(testData[,"perceivedAesthetics"])
#variablePointDataTest["perceivedAesthetics"] <- testData["perceivedAesthetics"]


#CONVERSION
observedValues = sapply(unlist(variablePointDataTest[["perceivedAesthetics"]]), as.character)
predictedValues = sapply(unlist(predictedTest), as.character)


# EVALUATES ACCURACY
  #counter = 0
  #lapply(seq(predictedTest), function(i) if (observedValues[i] == predictedValues[i]) { counter <<- counter + 1;});
  #print(100.0 / length(observedValues) * counter)
#ACCURACY
  print(mean(predictedValues == observedValues))
print(summary(linearModel))
