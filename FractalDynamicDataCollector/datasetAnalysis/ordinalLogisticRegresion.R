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


#variablePointDataTrain <- filter(variablePointData, name %in% trainData$Name) # variablePointData[variablePointData$name %in% trainData$Name, ]
variablePointDataTrain <- variablePointData[is.element(variablePointData$name, trainData$Name), ]
#variablePointDataTest <- filter(variablePointData, name %in% testData$Name) # variablePointData[variablePointData$name %in% testData$Name, ]
variablePointDataTest <- variablePointData[is.element(variablePointData$name, testData$Name), ]


nameLength <- length(colnames(variablePointDataTrain))
# GET DATA FOR COLUMN NAMES
usedColumnNames <- colnames(variablePointDataTrain)[2:nameLength]
numericalVPDataTrain <- variablePointDataTrain[usedColumnNames]


numericalVPDataTrainCenterX <- numericalVPDataTrain[str_detect(colnames(numericalVPDataTrain), "centerX")]
numericalVPDataTrainCenterY <- numericalVPDataTrain[str_detect(colnames(numericalVPDataTrain), "centerY")]   

samples <- nrow(variablePointDataTrain)
normalizedVPDataTrainCenterX <- scale(numericalVPDataTrainCenterX)
#correlationMatrixCenterX <- cor(normalizedVPDataTrainCenterX[,0:samples])
#trainedPCACenterX <- princomp(correlationMatrixCenterX)
trainedPCACenterX <- princomp(numericalVPDataTrainCenterX[,0:samples])




print(fviz_eig(trainedPCACenterX, addlabels = TRUE))
print(fviz_pca_var(trainedPCACenterX, col.var = "black"))
print(fviz_cos2(trainedPCACenterX, choice = "var", axes = 1:2))
print(fviz_pca_var(trainedPCACenterX, col.var = "cos2", gradient.cols = c("black", "orange", "green"), repel = TRUE))


normalizedVPDataTrainCenterY <- scale(numericalVPDataTrainCenterY)
#correlationMatrixCenterY <- cor(normalizedVPDataTrainCenterY[,0:samples])
#trainedPCACenterY <- princomp(correlationMatrixCenterY)
trainedPCACenterY <- princomp(numericalVPDataTrainCenterY[,0:samples])

#print(ggcorrplot(correlationMatrixCenterY))
#print(fviz_eig(trainedPCACenterY, addlabels = TRUE))
#print(fviz_pca_var(trainedPCACenterY, col.var = "black"))
#print(fviz_cos2(trainedPCACenterY, choice = "var", axes = 1:2))
#print(fviz_pca_var(trainedPCACenterY, col.var = "cos2", gradient.cols = c("black", "orange", "green"), repel = TRUE))



trainData$centerX <- trainedPCACenterX$loadings[,1]
trainData$centerY <- trainedPCACenterY$loadings[,1]



lapply(trainData[, c("perceivedAesthetics", "perceivedChaos", "centerX", "centerY")], table)
#categorizationGroupsTrain <- ftable(xtabs(~ perceivedAesthetics + centerX + centerY, data = trainData))
ftable(xtabs(~ perceivedAesthetics + centerX + centerY, data = trainData))
ordinalModel <- polr(as.factor(perceivedAesthetics) ~ centerX + centerY, data = trainData, method = c("logistic"), Hess=TRUE)
print(summary(ordinalModel))


#eigenValuesFromTrainCenterX <- get_eigenvalue(trainedPCACenterX)
#resVariablesFromTrainCenterX <- get_pca_var(trainedPCACenterY)
#resIndividualsFromTrainCenterX <- get_pca_ind(trainedPCACenterY)


numericalVPDataTest <- variablePointDataTest[usedColumnNames]
numericalVPDataTestCenterX <- numericalVPDataTest[str_detect(colnames(numericalVPDataTest), "centerX")]
numericalVPDataTestCenterY <- numericalVPDataTest[str_detect(colnames(numericalVPDataTest), "centerY")]   


#normalizedVPDataTestCenterX <- scale(numericalVPDataTestCenterX)
#correlationMatrixCenterXTest <- cor(normalizedVPDataTestCenterX[,0:samplesTest])
#normalizedVPDataTestCenterY <- scale(numericalVPDataTestCenterY)
#correlationMatrixCenterYTest <- cor(normalizedVPDataTestCenterY[,0:samplesTest])


predictedCenterXTest <- predict(trainedPCACenterX, newdata = numericalVPDataTestCenterX)
predictedCenterYTest <- predict(trainedPCACenterY, newdata = numericalVPDataTestCenterY)
testData$centerX <- predictedCenterXTest[,1]
testData$centerY <- predictedCenterYTest[,1]


#testData["perceivedAesthetics"] <- as.factor(testData["perceivedAesthetics"])
predictedTest <- predict(ordinalModel, newdata=testData)
print(predictedTest)

print(trainedPCACenterX)