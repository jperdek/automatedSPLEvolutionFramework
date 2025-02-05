library(SimplyAgree)
library(dplyr)

setwd("E:/aspects/spaProductLine/fractalDynamicDataCollector/datasetAnalysis")

evaluatedData <- read.csv('./sources/annotated_test.csv', sep=";")
multiModelData <- read.csv('./methods_results/resultsMultiModel.csv', sep=";")
nativeEnhancedData <- read.csv('./methods_results/resultsNativeEnhanced.csv', sep=";")
shrinkData <- read.csv('./methods_results/resultsShrink.csv', sep=";")


columnNamesLower <- c("X0", "X1", "X2")
multiModelData$lowAesthetics <- rowSums(multiModelData[columnNamesLower])
nativeEnhancedData$lowAesthetics <- rowSums(nativeEnhancedData[columnNamesLower])
shrinkData$lowAesthetics <- rowSums(shrinkData[columnNamesLower])

columnNamesHigh <- c("X7", "X8", "X9")
multiModelData$highAesthetics <- rowSums(multiModelData[columnNamesHigh])
nativeEnhancedData$highAesthetics <- rowSums(nativeEnhancedData[columnNamesHigh])
shrinkData$highAesthetics <- rowSums(shrinkData[columnNamesHigh])

columnNamesMedium <- c("X3", "X4", "X5", "X6")
multiModelData$mediumAesthetics <- rowSums(multiModelData[columnNamesMedium])
nativeEnhancedData$mediumAesthetics <- rowSums(nativeEnhancedData[columnNamesMedium])
shrinkData$mediumAesthetics <- rowSums(shrinkData[columnNamesMedium])


get_target_class_probability <- function(index, processedDataFrame, updatedDataframe, column_with_name, updatedColumnName, comparableVector) {
  columnName <- paste("X", as.character(column_with_name[index]), sep="")
  if (is.element(columnName, comparableVector)){
    updatedDataframe[[updatedColumnName]][index] <- as.numeric(as.vector(processedDataFrame[[columnName]])[index])
  } else {
    updatedDataframe[[updatedColumnName]][index] <- 1.0 - as.numeric(as.vector(processedDataFrame[[columnName]])[index])
  }
 
}


resultDF <- data.frame(nativeEnhancedData)
resultDF$lowAestheticsNativeEnhancedData <- nativeEnhancedData$category
resultDF$lowAestheticsMultiModelData <- multiModelData$category
resultDF$lowAestheticsShrinkData <- shrinkData$category
lapply(seq(nativeEnhancedData$category), get_target_class_probability,column_with_name=nativeEnhancedData$category,
       processedDataFrame=nativeEnhancedData, updatedDataframe=resultDF, 
       updatedColumnName="lowAestheticsNativeEnhancedData", comparableVector=columnNamesLower)
lapply(seq(multiModelData$category), get_target_class_probability, column_with_name=multiModelData$category,
       processedDataFrame=multiModelData, updatedDataframe=resultDF,
       updatedColumnName="lowAestheticsMultiModelData", comparableVector=columnNamesLower)
lapply(seq(shrinkData$category), get_target_class_probability, column_with_name=shrinkData$category, 
       processedDataFrame=shrinkData, updatedDataframe=resultDF,
       updatedColumnName="lowAestheticsShrinkData", comparableVector=columnNamesLower)


resultDF$highAestheticsNativeEnhancedData <- nativeEnhancedData$category
resultDF$highAestheticsMultiModelData <- multiModelData$category
resultDF$highAestheticsShrinkData <- shrinkData$category
lapply(seq(nativeEnhancedData$category), get_target_class_probability, column_with_name=nativeEnhancedData$category,  
       processedDataFrame=multiModelData, updatedDataframe=resultDF,
       updatedColumnName="highAestheticsNativeEnhancedData", comparableVector=columnNamesHigh)
lapply(seq(multiModelData$category), get_target_class_probability, column_with_name=nativeEnhancedData$category, 
       processedDataFrame=multiModelData, updatedDataframe=resultDF,
       updatedColumnName="highAestheticsMultiModelData", comparableVector=columnNamesHigh)
lapply(seq(shrinkData$category), get_target_class_probability, column_with_name=shrinkData$category,  
       processedDataFrame=shrinkData, updatedDataframe=resultDF,
       updatedColumnName="highAestheticsShrinkData", comparableVector=columnNamesHigh)


resultDF$mediumAestheticsNativeEnhancedData <- nativeEnhancedData$category
resultDF$mediumAestheticsMultiModelData <- multiModelData$category
resultDF$mediumAestheticsShrinkData <- shrinkData$category
lapply(seq(shrinkData$category), get_target_class_probability, column_with_name=nativeEnhancedData$category,
       processedDataFrame=shrinkData, updatedDataframe=resultDF, 
       updatedColumnName="mediumAestheticsNativeEnhancedData", comparableVector=columnNamesMedium)
lapply(seq(multiModelData$category), get_target_class_probability, column_with_name=multiModelData$category,
       processedDataFrame=multiModelData, updatedDataframe=resultDF, 
       updatedColumnName="mediumAestheticsMultiModelData", comparableVector=columnNamesMedium)
lapply(seq(shrinkData$category), get_target_class_probability, column_with_name=shrinkData$category,
       processedDataFrame=shrinkData, updatedDataframe=resultDF, 
       updatedColumnName="mediumAestheticsShrinkData", comparableVector=columnNamesMedium)


# LOW AESTHETIC DATA
resultDF <- na.omit(resultDF)
newData <- data.frame()
a12 = agree_test(x=resultDF$lowAestheticsNativeEnhancedData, y=resultDF$lowAestheticsMultiModelData,
                 agree.level = .8)
print(a12)
print(plot(a12, type = 1))
print(plot(a12, type = 2))


a13 = agree_test(x=resultDF$lowAestheticsNativeEnhancedData, y=resultDF$lowAestheticsShrinkData,
                 agree.level = .8)
print(a13)
print(plot(a13, type = 1))
print(plot(a13, type = 2))


a23 = agree_test(x=resultDF$lowAestheticsMultiModelData, y=resultDF$lowAestheticsShrinkData,
                 agree.level = .8)
print(a23)
print(plot(a23, type = 1))
print(plot(a23, type = 2))



# HIGH AESTHETIC DATA
resultDF <- na.omit(resultDF)
newData <- data.frame()
a12 = agree_test(x=resultDF$highAestheticsNativeEnhancedData, y=resultDF$highAestheticsMultiModelData,
                 agree.level = .8)
print(a12)
print(plot(a12, type = 1))
print(plot(a12, type = 2))


a13 = agree_test(x=resultDF$highAestheticsNativeEnhancedData, y=resultDF$highAestheticsShrinkData,
                 agree.level = .8)
print(a13)
print(plot(a13, type = 1))
print(plot(a13, type = 2))


a23 = agree_test(x=resultDF$highAestheticsMultiModelData, y=resultDF$highAestheticsShrinkData,
                 agree.level = .8)
print(a23)
print(plot(a23, type = 1))
print(plot(a23, type = 2))



# MEDIUM AESTHETIC DATA
resultDF <- na.omit(resultDF)
newData <- data.frame()
a12 = agree_test(x=resultDF$mediumAestheticsNativeEnhancedData, y=resultDF$mediumAestheticsMultiModelData,
                 agree.level = .8)
print(a12)
print(plot(a12, type = 1))
print(plot(a12, type = 2))


a13 = agree_test(x=resultDF$mediumAestheticsNativeEnhancedData, y=resultDF$mediumAestheticsShrinkData,
                 agree.level = .8)
print(a13)
print(plot(a13, type = 1))
print(plot(a13, type = 2))


a23 = agree_test(x=resultDF$mediumAestheticsMultiModelData, y=resultDF$mediumAestheticsShrinkData,
                 agree.level = .8)
print(a23)
print(plot(a23, type = 1))
print(plot(a23, type = 2))


