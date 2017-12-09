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
#pod$Mesec <- NULL
pod$Datum <- NULL

pod$O3 <- cut(pod$O3, c(-Inf, 60, 120, 180, Inf), labels=c("NIZKA", "SREDNJA", "VISOKA", "EKSTREMNA"))
pod$PM10 <- cut(pod$PM10, c(-Inf,35,Inf),labels=c("NIZKA","VISOKA"))



library(CORElearn)
library(randomForest)


source("mojefunkcije.R")


#set.seed(8678686)

#
#
# VECINSKI KLASIFIKATOR
# 
# Povpreèna CA toènost
# 0.609328

v <- vector()

for (i in 1:100) {
	sel <- sample(1:nrow(pod), size=as.integer(nrow(pod)*0.7), replace=F)
	learn <- pod[sel,]
	test <- pod[-sel,]
	which.max(table(learn$O3))
	majority.class <- names(which.max(table(learn$O3)))
	v[i] <- sum(test$O3== majority.class) / length(test$O3)
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

modelDT <- CoreModel(O3 ~ ., learn, model="tree")
modelRF <- CoreModel(O3 ~ ., learn, model="rf")
modelRFN <- CoreModel(O3 ~ ., learn, model="rfNear")
modelKNN <- CoreModel(O3 ~ ., learn, model="knn", kInNN = 5)

predDT <- predict(modelDT, test, type = "class")
predRF <- predict(modelRF, test, type="class")
predKNN <- predict(modelKNN, test, type="class")
predRFN <- predict(modelRFN, test, type="class")


pred <- data.frame(predDT, predKNN, predRF, predRFN)

predicted <- voting(pred)
CA(test$O3, predicted)




#
# Utezeno glasovanje
#



caDT <- CA(test$O3, predDT)
caRF <- CA(test$O3, predRF)
caRFN <- CA(test$O3, predRFN)
caKNN <- CA(test$O3, predKNN)

predDT.prob <- predict(modelDT, test, type="probability")
predRF.prob <- predict(modelRF, test, type="probability")
predRFN.prob <- predict(modelRFN, test, type="probability")
predKNN.prob <- predict(modelKNN, test, type="probability")

# sestejemo napovedane verjetnosti s strani razlicnih modelov
pred.prob <- caDT * predDT.prob + caRF * predRF.prob + caRFN * predRFN.prob + caKNN * predKNN.prob
predicted <- levels(learn$O3)[apply(pred.prob, 1, which.max)]

CA(test$O3, predicted)



#
# Bagging
#
#library(ipred)

sel <- sample(1:nrow(pod), size=as.integer(nrow(pod)*0.8), replace=F)
learn <- pod[sel,]
test <- pod[-sel,]

bag <- bagging(O3 ~ ., learn, nbagg=30)
bag.pred <- predict(bag, test, type="class")
CA(test$O3, bag.pred)



#
# Nakljucni gozd 
#

sel <- sample(1:nrow(pod), size=as.integer(nrow(pod)*0.7), replace=F)
learn <- pod[sel,]
test <- pod[-sel,]

rf <- randomForest(O3 ~ ., learn)
predicted <- predict(rf, test, type = "class")
CA(test$O3, predicted)



#
# Boosting
#
#library(adabag)

sel <- sample(1:nrow(pod), size=as.integer(nrow(pod)*0.8), replace=F)
learn <- pod[sel,]
test <- pod[-sel,]

bm <- boosting(O3 ~ ., learn)
predictions <- predict(bm, test)
predicted <- predictions$class
CA(test$O3, predicted)



#
#
# UMETNE NEVRONSKE MREZE
#
#

sel <- sample(1:nrow(pod), size=as.integer(nrow(pod)*0.85), replace=F)

norm.data <- scale.data(pod)
norm.learn <- norm.data[sel,]
norm.test <- norm.data[-sel,]

nn <- nnet(O3 ~ ., data = norm.learn, size = 5, decay = 0.00001, maxit = 100000)
predicted <- predict(nn, norm.test, type = "class")
CA(norm.test$O3, predicted)









