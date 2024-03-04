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
#install.packages("StepReg")
#install.packages("radiant")
require(nnet)
require(foreign)
require(ggplot2)
require(MASS)
require(Hmisc)
require(reshape2)
library(dplyr)
library(StepReg)
#FOR PCA
library(FactoMineR)
library(corrr)
library(ggcorrplot)
library(factoextra)
library("stringr") 
library(radiant) 

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

#trainData <- cbind(trainData, variablePointDataTrain[!names(variablePointDataTrain) %in% names(trainData)])
#testData <- cbind(testData, variablePointDataTest[!names(variablePointDataTest) %in% names(testData)])
#print(head(trainData[1:8]))
print(colnames(variablePointDataTest))


variablePointDataTrain[,"perceivedAesthetics"] <- factor(trainData[,"perceivedAesthetics"])
print(colnames(variablePointDataTrain[2:length(colnames(variablePointDataTrain))]))
#linearModel2 <- mnl(variablePointDataTrain, rvar="perceivedAesthetics" , evar=colnames(variablePointDataTrain[2:100]), check="standardize")
#linearModel2 <- mnl(variablePointDataTrain, rvar="perceivedAesthetics" , evar=colnames(variablePointDataTrain[2:100]), check="stepwise-both")
linearModel2 <- mnl(variablePointDataTrain, rvar="perceivedAesthetics" , evar=colnames(variablePointDataTrain[2:100]), check="stepwise-backward")
#linearModel2 <- mnl(variablePointDataTrain, rvar="perceivedAesthetics" , evar=colnames(variablePointDataTrain[2:100]), check="stepwise-forward")

predictedTest <- predict(linearModel2, pred_data=variablePointDataTest)
variablePointDataTest[,"perceivedAesthetics"] <- as.factor(testData[,"perceivedAesthetics"])
#CONVERSION
observedValues = sapply(unlist(variablePointDataTest[["perceivedAesthetics"]]), as.character)

pred_names <- c("1", "2", "3", "4", "5", "6", "7", "8", "9")
predictedTest$Largest_Column<-colnames(predictedTest[pred_names])[apply(predictedTest[pred_names],1,which.max)]
predictedValues = sapply(unlist(predictedTest$Largest_Column), as.character)


# EVALUATES ACCURACY
  #counter = 0
  #lapply(seq(predictedTest), function(i) if (observedValues[i] == predictedValues[i]) { counter <<- counter + 1;});
  #print(100.0 / length(observedValues) * counter)
#ACCURACY
  print(mean(predictedValues == observedValues))
#print(summary(linearModel))
  
