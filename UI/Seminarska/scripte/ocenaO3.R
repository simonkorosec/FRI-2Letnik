library(CORElearn)
library(randomForest)
library(ipred)
library(adabag)


sort(attrEval(O3 ~ ., pod, "Relief"), decreasing = TRUE)
sort(attrEval(O3 ~ ., pod, "ReliefFequalK"), decreasing = TRUE)
sort(attrEval(O3 ~ ., pod, "ReliefFexpRank"), decreasing = TRUE)










