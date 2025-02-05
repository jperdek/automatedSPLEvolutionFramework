#install.packages("SimplyAgree")
#install.packages("dplyr")

library(SimplyAgree)
library(dplyr)

setwd("E:/aspects/spaProductLine/fractalDynamicDataCollector/datasetAnalysis")


evaluatedData <- read.csv('./sources/annotated_test.csv', sep=";")
multiModelData <- read.csv('./methods_results/resultsMultiModel.csv', sep=";")
nativeEnhancedData <- read.csv('./methods_results/resultsNativeEnhanced.csv', sep=";")
shrinkData <- read.csv('./methods_results/resultsShrink.csv', sep=";")


all_rows0 <- full_join(evaluatedData, multiModelData,
                       by = join_by(Name == name))
all_rows1 <- full_join(all_rows0, nativeEnhancedData,
                       by = join_by(Name == name))
all_rows2 <- full_join(all_rows1, shrinkData,
                       by = join_by(Name == name))

resultDF <- all_rows2
get_target_class_probability <- function(index, column_with_name, processedDataFrame, updatedDataframe, updatedColumnName) {
  columnName <- paste("X", as.character(column_with_name[index]), sep="")
  updatedDataframe[[updatedColumnName]][index] <- as.numeric(as.vector(processedDataFrame[[columnName]])[index])
}
resultDF$results_native_enhanced_p <- nativeEnhancedData$category
resultDF$results_multimodel_p <- multiModelData$category
resultDF$results_shrink_p <- shrinkData$category
lapply(seq(nativeEnhancedData$category), get_target_class_probability, column_with_name=evaluatedData$perceivedAesthetics, 
       processedDataFrame=nativeEnhancedData, updatedDataframe=resultDF, updatedColumnName="results_native_enhanced_p")
lapply(seq(multiModelData$category), get_target_class_probability, column_with_name=evaluatedData$perceivedAesthetics, 
       processedDataFrame=multiModelData, updatedDataframe=resultDF, updatedColumnName="results_multimodel_p")
lapply(seq(shrinkData$category), get_target_class_probability, column_with_name=evaluatedData$perceivedAesthetics, 
       processedDataFrame=shrinkData, updatedDataframe=resultDF, updatedColumnName="results_shrink_p")


resultDF <- na.omit(resultDF)
newData <- data.frame()
a12 = agree_test(x=resultDF$results_multimodel_p, y=resultDF$results_native_enhanced_p,
                agree.level = .8)
print(a12)
print(plot(a12, type = 1, main="Bland-Altman: Multi-model vs Native (increased input size)",xlab="Difference between models", ylab="Average between models"))
print(plot(a12, type = 2, main="Line Of Identity:  Multi-model vs Native (increased input size)", xlab="Multi-model", ylab="Native model (increased input size)"))


a13 = agree_test(x=resultDF$results_multimodel_p, y=resultDF$results_shrink_p,
                 agree.level = .8)
print(a13)
print(plot(a13, type = 1, main="Bland-Altman: Multi-model vs Native (shrinked input size)", xlab="Difference between models", ylab="Average between models"))
print(plot(a13, type = 2, main="Line Of Identity:  Multi-model vs Native (shrinked input size)", xlab="Multi-model", ylab="Native model (shrinked input size)"))


a23 = agree_test(x=resultDF$results_native_enhanced_p, y=resultDF$results_shrink_p,
                 agree.level = .8)
print(a23)
print(plot(a23, type = 1, main="Bland-Altman: Native (increased input size) vs Native (shrinked input size)",xlab="Difference between models", ylab="Average between models"))
print(plot(a23, type = 2, main="Line Of Identity: Native (increased input size) vs Native (shrinked input size)", xlab="Native model (increased input size)", ylab="Native model (shrinked input size)"))
