# setwd("C:/Users/Simon Korošec/Documents/GitHub/FRI-2Letnik/UI/Seminarska")

pod <- read.table("podatkiSem1.txt", sep=",", header=T)

pod$Glob_sevanje_min <- NULL

install.packages("FSelector") 
library(FSelector)
weights <- relief(PM10~., pod, neighbours.count = 5, sample.size = 20)
print(weights)
