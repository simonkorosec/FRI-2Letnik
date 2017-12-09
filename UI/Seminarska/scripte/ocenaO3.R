library(CORElearn)
library(randomForest)
library(ipred)
library(adabag)


pod <- read.table("podatkiSem1.txt", sep=",", header=T)

pod$O3 <- cut(pod$O3, c(-Inf, 60, 120, 180, Inf), labels=c("NIZKA", "SREDNJA", "VISOKA", "EKSTREMNA"))
#pod$PM10 <- cut(pod$PM10, c(-Inf,35,Inf),labels=c("NIZKA","VISOKA"))
pod$Glob_sevanje_min <- NULL

sort(attrEval(O3 ~ ., pod, "Relief"), decreasing = TRUE)
sort(attrEval(O3 ~ ., pod, "ReliefFequalK"), decreasing = TRUE)
sort(attrEval(O3 ~ ., pod, "ReliefFexpRank"), decreasing = TRUE)










