pod <- read.table("podatkiSem1.txt", sep=",", header=T)
pod$Glob_sevanje_min <- NULL
pod$Datum <- as.Date(pod$Datum, "%Y-%m-%d")
pod$Mesec <- months(pod$Datum)
pod$Mesec <- as.factor(pod$Mesec)
zima <- pod$Mesec == "februar" | pod$Mesec == "januar" | pod$Mesec == "december"
pomlad <- pod$Mesec == "marec" | pod$Mesec == "april" | pod$Mesec == "maj"
poletje <- pod$Mesec == "junij" | pod$Mesec == "julij" | pod$Mesec == "avgust"
jesen <- pod$Mesec == "september" | pod$Mesec == "oktober" | pod$Mesec == "november"
pod$Letni_cas[zima] <- "zima"
pod$Letni_cas[pomlad] <- "pomlad"
pod$Letni_cas[poletje] <- "poletje"
pod$Letni_cas[jesen] <- "jesen"
pod$Letni_cas <- as.factor(pod$Letni_cas)
pod$Mesec <- NULL
pod$Datum <- NULL

#pod$O3 <- cut(pod$O3, c(-Inf, 60, 120, 180, Inf), labels=c("NIZKA", "SREDNJA", "VISOKA", "EKSTREMNA"))
pod$PM10 <- cut(pod$PM10, c(-Inf,35,Inf),labels=c("NIZKA","VISOKA"))



library(CORElearn)
library(randomForest)
library(ipred)
library(adabag)

source("mojefunkcije.R")


#set.seed(8678686)

#
#
# VECINSKI KLASIFIKATOR
# 
# Povpreèna CA toènost
# 0.868414

v <- vector()

for (i in 1:50) {
	sel <- sample(1:nrow(pod), size=as.integer(nrow(pod)*0.7), replace=F)
	learn <- pod[sel,]
	test <- pod[-sel,]
	which.max(table(learn$PM10))
	majority.class <- names(which.max(table(learn$PM10)))
	v[i] <- sum(test$PM10== majority.class) / length(test$PM10)
}
mean(v)


#
# Glasovanje
#

voting <- function(predictions)
{
	res <- vector()

  	for (i in 1 : nrow(predictions))  	
	{
		vec <- unlist(predictions[i,])
    		res[i] <- names(which.max(table(vec)))
  	}

  	factor(res, levels=levels(predictions[,1]))
}

sel <- sample(1:nrow(pod), size=as.integer(nrow(pod)*0.7), replace=F)
learn <- pod[sel,]
test <- pod[-sel,]

modelDT <- CoreModel(PM10 ~ ., learn, model="tree")
modelRF <- CoreModel(PM10 ~ ., learn, model="rf")
modelRFN <- CoreModel(PM10 ~ ., learn, model="rfNear")
modelKNN <- CoreModel(PM10 ~ ., learn, model="knn", kInNN = 5)

predDT <- predict(modelDT, test, type = "class")
predRF <- predict(modelRF, test, type="class")
predKNN <- predict(modelKNN, test, type="class")
predRFN <- predict(modelRFN, test, type="class")


pred <- data.frame(predDT, predKNN, predRF, predRFN)


predicted <- voting(pred)
CA(test$PM10, predicted)




#
# Utezeno glasovanje
#



caDT <- CA(test$PM10, predDT)
caRF <- CA(test$PM10, predRF)
caRFN <- CA(test$PM10, predRFN)
caKNN <- CA(test$PM10, predKNN)

predDT.prob <- predict(modelDT, test, type="probability")
predRF.prob <- predict(modelRF, test, type="probability")
predRFN.prob <- predict(modelRFN, test, type="probability")
predKNN.prob <- predict(modelKNN, test, type="probability")

# sestejemo napovedane verjetnosti s strani razlicnih modelov
pred.prob <- caDT * predDT.prob + caRF * predRF.prob + caRFN * predRFN.prob + caKNN * predKNN.prob
predicted <- levels(learn$PM10)[apply(pred.prob, 1, which.max)]

CA(test$PM10, predicted)



#
# Bagging
#

sel <- sample(1:nrow(pod), size=as.integer(nrow(pod)*0.7), replace=F)
learn <- pod[sel,]
test <- pod[-sel,]

bag <- bagging(PM10 ~ ., learn, nbagg=20)
bag.pred <- predict(bag, test, type="class")
CA(test$PM10, bag.pred)




#
# Nakljucni gozd 
#

sel <- sample(1:nrow(pod), size=as.integer(nrow(pod)*0.7), replace=F)
learn <- pod[sel,]
test <- pod[-sel,]

rf <- randomForest(PM10 ~ ., learn)
predicted <- predict(rf, test, type = "class")
CA(test$PM10, predicted)



#
# Boosting
#

sel <- sample(1:nrow(pod), size=as.integer(nrow(pod)*0.7), replace=F)
learn <- pod[sel,]
test <- pod[-sel,]

bm <- boosting(PM10 ~ ., learn)
predictions <- predict(bm, test)
predicted <- predictions$class
CA(test$PM10, predicted)



#
#
# UMETNE NEVRONSKE MREZE
#
#

sel <- sample(1:nrow(pod), size=as.integer(nrow(pod)*0.7), replace=F)

norm.data <- scale.data(pod)
norm.learn <- norm.data[sel,]
norm.test <- norm.data[-sel,]

nn <- nnet(PM10 ~ ., data = norm.learn, size = 5, decay = 0.00001, maxit = 100000)
predicted <- predict(nn, norm.test, type = "class")
CA(norm.test$PM10, predicted)




#
# SVM
#

sel <- sample(1:nrow(pod), size=as.integer(nrow(pod)*0.7), replace=F)
learn <- pod[sel,]
test <- pod[-sel,]

svm <- ksvm(PM10 ~ ., data = learn, kernel = "rbfdot")
predicted <- predict(svm, test, type = "response")
CA(test$PM10, predicted)






