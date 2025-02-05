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

assignCategories <- function(x) {
  x = as.double(sub(',', '.', x))
  if (x >= 0.9) {
    return (9);
  }  else if(x >= 0.8) {
    return (8);
  } else if(x >= 0.7) {
    return (7);
  } else if(x >= 0.6) {
    return (6);
  } else if(x >= 0.5) {
    return (5);
  } else if(x >= 0.4) {
    return (4);
  } else if(x >= 0.3) {
    return (3);
  } else if(x >= 0.2) {
    return (2);
  } else if(x >= 0.1) {
    return (1);
  }
  return (0);
} 

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


variablePointDataTrain[,"Aesthetic"] <- as.factor(unlist(lapply(trainData[,"Aesthetic"], assignCategories)))
print(colnames(variablePointDataTrain[2:length(colnames(variablePointDataTrain))]))
#linearModel2 <- mnl(variablePointDataTrain, rvar="Aesthetic" , evar=colnames(variablePointDataTrain[2:100]), check="standardize")
#linearModel2 <- mnl(variablePointDataTrain, rvar="Aesthetic" , evar=colnames(variablePointDataTrain[2:100]), check="stepwise-both")
#linearModel2 <- mnl(variablePointDataTrain, rvar="Aesthetic" , evar=colnames(variablePointDataTrain[2:100]), check="stepwise-backward")
linearModel2 <- mnl(variablePointDataTrain, rvar="Aesthetic" , evar=colnames(variablePointDataTrain[2:100]), check="stepwise-forward")

predictedTest <- predict(linearModel2, pred_data=variablePointDataTest)
variablePointDataTest[,"Aesthetic"] <- as.factor(unlist(lapply(testData[,"Aesthetic"], assignCategories)))

#CONVERSION
observedValues = sapply(unlist(variablePointDataTest[["Aesthetic"]]), as.character)

print(predictedTest)
pred_names <- c("0", "1", "2", "3", "4", "6", "7", "8", "9")
predictedTest$Largest_Column<-colnames(predictedTest[pred_names])[apply(predictedTest[pred_names],1,which.max)]
predictedValues = sapply(unlist(predictedTest$Largest_Column), as.character)


# EVALUATES ACCURACY
#counter = 0
#lapply(seq(predictedTest), function(i) if (observedValues[i] == predictedValues[i]) { counter <<- counter + 1;});
#print(100.0 / length(observedValues) * counter)
#ACCURACY
print(mean(predictedValues == observedValues))
#print(summary(linearModel))