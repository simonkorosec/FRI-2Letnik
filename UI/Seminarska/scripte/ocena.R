# setwd("C:/Users/Simon Korošec/Documents/GitHub/FRI-2Letnik/UI/Seminarska")

pod <- read.table("podatkiSem1.txt", sep=",", header=T)

pod$Glob_sevanje_min <- NULL

pod$Datum <- as.Date(pod$Datum, "%Y-%m-%d")
pod$Mesec <- months(pod$Datum)
pod$Mesec <- as.factor(pod$Mesec)
pod$Dan   <- weekdays(pod$Datum)
pod$Dan <- as.factor(pod$Dan)

pod$Datum <- NULL


install.packages("FSelector") 
library(FSelector)
weights <- relief(PM10~., pod, neighbours.count = 5, sample.size = 20)
print(weights)
sort(print(weights), decreasing = TRUE)


## TEST ##
evaluator <- function(subset) {
	print("--DELA--")
	flush.console()
	#k-fold cross validation
	k <- 5
	splits <- runif(nrow(pod))
	results = sapply(1:k, function(i) {
		test.idx <- (splits >= (i - 1) / k) & (splits < i / k)
		train.idx <- !test.idx
		test <- pod[test.idx, , drop=FALSE]
		train <- pod[train.idx, , drop=FALSE]
		tree <- rpart(as.simple.formula(subset, "PM10"), train)
		error.rate <- sum(test$PM10 != predict(tree, test, type="c")) / nrow(test)
		return(1 - error.rate)
	})
	print(subset)
	print(mean(results))
	return(mean(results))
}

subset <- forward.search(names(pod)[-24], evaluator)
f <- as.simple.formula(subset, "PM10")
print(f)



################################################################################

  iris <- pod
  
pod$O3 <- cut(pod$O3, c(-Inf, 60, 120, 180, Inf), labels=c("NIZKA", "SREDNJA", "VISOKA", "EKSTREMNA"))
pod$PM10 <- cut(pod$PM10, c(-Inf,35,Inf),labels=c("NIZKA","VISOKA"))


    evaluator <- function(subset) {
    #k-fold cross validation
    k <- 5
    splits <- runif(nrow(iris))
    results = sapply(1:k, function(i) {
      test.idx <- (splits >= (i - 1) / k) & (splits < i / k)
      train.idx <- !test.idx
      test <- iris[test.idx, , drop=FALSE]
      train <- iris[train.idx, , drop=FALSE]
      tree <- rpart(as.simple.formula(subset, "PM10"), train)
      error.rate = sum(test$PM10 != predict(tree, test, type="class")) / nrow(test)
      return(1 - error.rate)
    })
    print(subset)
    print(mean(results))
    return(mean(results))
  }
  
  subset <- forward.search(names(iris)[-24], evaluator)
  f <- as.simple.formula(subset, "PM10")
  print(f)



##
PM10 ~ Sunki_vetra_max + Padavine_mean + Padavine_sum + Temperatura_lokacija_max + Temperatura_lokacija_min


modelDT <- CoreModel(PM10 ~ Sunki_vetra_max + Padavine_mean + Padavine_sum + Temperatura_lokacija_max + Temperatura_lokacija_min, learn, model="tree")
modelNB <- CoreModel(PM10 ~ Sunki_vetra_max + Padavine_mean + Padavine_sum + Temperatura_lokacija_max + Temperatura_lokacija_min, learn, model="bayes")
modelKNN <- CoreModel(PM10 ~ Sunki_vetra_max + Padavine_mean + Padavine_sum + Temperatura_lokacija_max + Temperatura_lokacija_min, data = learn, model="knn", kInNN = 7)
##