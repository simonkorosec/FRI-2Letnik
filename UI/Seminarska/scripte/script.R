#doma
# setwd("C:/Users/Simon Korošec/Documents/GitHub/FRI-2Letnik/UI/Seminarska")
#laptop
# setwd("C:/Users/Uporabnik/Documents/FRI-2Letnik/UI/Seminarska")



pod <- read.table("podatkiSem1.txt", sep=",", header=T)

# Nerabiš same 0
pod$Glob_sevanje_min <- NULL

# Sprememba tipa v datum
pod$Datum <- as.Date(pod$Datum, "%Y-%m-%d")
pod$Mesec <- months(pod$Datum)
pod$Mesec <- as.factor(pod$Mesec)
pod$Datum <- NULL


# Delitev na letne case
zima <- pod$Mesec == "februar" | pod$Mesec == "januar" | pod$Mesec == "december"
pomlad <- pod$Mesec == "marec" | pod$Mesec == "april" | pod$Mesec == "maj"
poletje <- pod$Mesec == "junij" | pod$Mesec == "julij" | pod$Mesec == "avgust"
jesen <- pod$Mesec == "september" | pod$Mesec == "oktober" | pod$Mesec == "november"

pod$Letni_cas[zima] <- "zima"
pod$Letni_cas[pomlad] <- "pomlad"
pod$Letni_cas[poletje] <- "poletje"
pod$Letni_cas[jesen] <- "jesen"

pod$Letni_cas <- as.factor(pod$Letni_cas)
# pod$Mesec <- NULL


#
# modeli za PM10
#
library(CORElearn)
source("mojefunkcije.R")

pod$O3 <- cut(pod$O3, c(-Inf, 60, 120, 180, Inf), labels=c("NIZKA", "SREDNJA", "VISOKA", "EKSTREMNA"))

sel <- sample(1:nrow(pod), size=as.integer(nrow(pod)*0.7), replace=F)
learn <- pod[sel,]
test <- pod[-sel,]

modelDT <- CoreModel(PM10 ~ ., learn, model="tree")
modelNB <- CoreModel(PM10 ~ ., learn, model="bayes")
modelKNN <- CoreModel(PM10 ~ ., data = learn, model="knn", kInNN = 7)

predDT <- predict(modelDT, test, type = "class")
caDT <- CA(test$PM10, predDT)
caDT # 0,88 # 0,89

predNB <- predict(modelNB, test, type="class")
caNB <- CA(test$PM10, predNB)
caNB # 0,79 # 0,84

predKNN <- predict(modelKNN, test, type="class")
caKNN <- CA(test$PM10, predKNN)
caKNN # 0,89 # 0,89


# Cela formula
# 
# Glasovanje 0,885
# Utezeno G. 0,883
# Bagging    0,892
# Random F.	 0,905
# Boosting   0,895

# Krajša formula
# 
# Glasovanje 0,892
# Utezeno G. 0,897
# Bagging    ??
# Random F.	 0,892
# Boosting   0,876



#
# Ocenitev atributev
pod <- read.table("podatkiSem1.txt", sep=",", header=T)
pod$Datum <- as.Date(pod$Datum, "%Y-%m-%d")
pod$Mesec <- months(pod$Datum)
pod$Mesec <- as.factor(pod$Mesec)
pod$Datum <- NULL

pod$O3 <- cut(pod$O3, c(-Inf, 60, 120, 180, Inf), labels=c("NIZKA", "SREDNJA", "VISOKA", "EKSTREMNA"))
pod$PM10 <- cut(pod$PM10, c(-Inf,35,Inf),labels=c("NIZKA","VISOKA"))

sort(attrEval(O3 ~ ., pod, "ReliefFequalK"), decreasing = TRUE)
dt <- CoreModel(O3 ~ ., pod, model="tree", selectionEstimator="ReliefFequalK")
plot(dt, pod)

#install.packages("ipred")
library(ipred)

source("wrapper.R")
wrapper(pod, className="O3", classModel="tree", folds=10)





#
# Grafi
#



povpLetniCasi <- data.frame(pod$Letni_cas, pod$PM10, pod$O3)

plot(povpLetniCasi$pod.Letni_cas, povpLetniCasi$pod.O3)
plot(povpLetniCasi$pod.Letni_cas, povpLetniCasi$pod.PM10)





#
# 1 Naloga
#

pod <- read.table("podatkiSem1.txt", sep=",", header=T)
pod$Datum <- as.Date(pod$Datum, "%Y-%m-%d")

library(lubridate)
pod$Leto <- year(pod$Datum)
pod$Leto <- as.factor(pod$Leto)


leto13 <- pod[pod$Leto == 2013,]
leto14 <- pod[pod$Leto == 2014,]
leto15 <- pod[pod$Leto == 2015,]
leto16 <- pod[pod$Leto == 2016,]

mean(leto13$PM10[leto13$Mesec == "oktober"])

v <- levels(as.factor(leto13$Mesec))
v


for (m in v) {
	print(m)
	print(mean(leto13$PM10[leto13$Mesec == m]))
}



plot(y=leto13$PM10, x=leto13$Datum)
points(y=leto14$PM10, x=leto14$Datum, col="#ff0000")
points(y=leto15$PM10, x=leto15$Datum, col="#00ff00")
points(y=leto16$PM10, x=leto16$Datum, col="#0000ff")



