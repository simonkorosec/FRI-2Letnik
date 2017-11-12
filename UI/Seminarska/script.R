# setwd("C:/Users/Simon Korošec/Documents/GitHub/FRI-2Letnik/UI/Seminarska")

pod 			<- read.table("podatkiSem1.txt", sep=",", header=T)

# pod_Koper 		<- pod[pod$Postaja == "Koper",]
# pod_Ljubljana 	<- pod[pod$Postaja == "Ljubljana",]

# Nerabiš same 0
# pod$Glob_sevanje_min <- NULL

# Sprememba tipa v datum
pod$Datum <- as.Date(pod$Datum, "%Y-%m-%d")
pod$Mesec <- months(pod$Datum)
pod$Mesec <- as.factor(pod$Mesec)
# pod$Datum <- NULL


# Delitev na letne èase
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


#
# modeli za O3
#
library(CORElearn)
source("mojefunkcije.R")

pod$O3 <- cut(pod$O3, c(-Inf, 60, 120, 180, Inf), labels=c("NIZKA", "SREDNJA", "VISOKA", "EKSTREMNA"))

sel <- sample(1:nrow(pod), size=as.integer(nrow(pod)*0.7), replace=F)
learn <- pod[sel,]
test <- pod[-sel,]

modelDT <- CoreModel(O3 ~ ., learn, model="tree")
modelNB <- CoreModel(O3 ~ ., learn, model="bayes")
modelKNN <- CoreModel(O3 ~ ., data = learn, model="knn", kInNN = 7)

predDT <- predict(modelDT, test, type = "class")
caDT <- CA(test$O3, predDT)
caDT

predNB <- predict(modelNB, test, type="class")
caNB <- CA(test$O3, predNB)
caNB

predKNN <- predict(modelKNN, test, type="class")
caKNN <- CA(test$O3, predKNN)
caKNN

#
#> caDT
#[1] 0.7885375
#> caNB
#[1] 0.6047431
#> caKNN
#[1] 0.798419 
#



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


















