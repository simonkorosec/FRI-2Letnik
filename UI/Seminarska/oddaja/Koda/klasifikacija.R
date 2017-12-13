###########################################
#	KLASIFIKACIJA ZA ATRIBUT PM10         #
###########################################


source("mojefunkcije.R")

# Potrebne knjižnice
library(CORElearn)
library(ipred)
library(nnet)


pod <- read.table("noviPodatki.txt", sep=",", header=T)
pod$PM10 <- cut(pod$PM10, c(-Inf,35,Inf),labels=c("NIZKA","VISOKA"))


# Izbrane formule
formula <- as.formula("PM10  ~  .")			# Formula 1
# formula <- as.formula("PM10 ~ Temperatura_lokacija_min + Sunki_vetra_max + Padavine_mean + postaja + Temperatura_Krvavec_min + O3 + Sunki_vetra_mean + Temperatura_lokacija_mean ")		# Formula 2
# formula <- as.formula("PM10 ~ Temperatura_lokacija_max + Temperatura_lokacija_mean + Letni_cas + O3 + Temperatura_Krvavec_mean + Pritisk_max + Pritisk_mean + Glob_sevanje_max  + Vlaga_min + Glob_sevanje_mean")      	# Formula 3



sel <- sample(1:nrow(pod), size=as.integer(nrow(pod)*0.7), replace=F)
learn <- pod[sel,]
test <- pod[-sel,]


#
# KNN
#


modelKNN <- CoreModel(formula, learn, model="knn", kInNN = 15) 		# Za k od 5 do 25
predKNN <- predict(modelKNN, test, type="class")
cv.dt <- CA(test$PM10, predKNN)
cv.dt



#
# Glasovanje
#

modelDT <- CoreModel(formula, learn, model="tree")
modelRF <- CoreModel(formula, learn, model="rf")
modelRFN <- CoreModel(formula, learn, model="rfNear")
modelKNN <- CoreModel(formula, learn, model="knn", kInNN = 20)

predDT <- predict(modelDT, test, type = "class")
predRF <- predict(modelRF, test, type="class")
predKNN <- predict(modelKNN, test, type="class")
predRFN <- predict(modelRFN, test, type="class")

pred <- data.frame(predDT, predKNN, predRF, predRFN)
predicted <- voting(pred)

cv.dt <- CA(test$PM10, predicted)
cv.dt



#
# Bagging
#


bag <- bagging(formula, learn, nbagg=25)
bag.pred <- predict(bag, test, type="class")

cv.dt <- CA(test$PM10, bag.pred)
cv.dt


#
# UMETNE NEVRONSKE MREZE
#


norm.data <- scale.data(pod)
norm.learn <- norm.data[sel,]
norm.test <- norm.data[-sel,]

nn <- nnet(formula, data = norm.learn, size = 8, decay = 0.005, maxit = 1000)
predicted <- predict(nn, norm.test, type = "class")
cv.dt <- CA(norm.test$PM10, predicted)
cv.dt






###########################################
#	KLASIFIKACIJA ZA ATRIBUT O3           #
###########################################

pod <- read.table("noviPodatki.txt", sep=",", header=T)
pod$O3 <- cut(pod$O3, c(-Inf, 60, 120, 180, Inf), labels=c("NIZKA", "SREDNJA", "VISOKA", "EKSTREMNA"))


# Izbrane formule
formula <- as.formula("O3  ~  .")			# Formula 1
# formula <- as.formula("O3 ~ Temperatura_lokacija_max + Glob_sevanje_mean + Temperatura_Krvavec_mean + Pritisk_mean + Hitrost_vetra_max + Glob_sevanje_max + Temperatura_Krvavec_min + Padavine_sum")		# Formula 2
# formula <- as.formula("O3 ~ Glob_sevanje_mean + Glob_sevanje_max + Letni_cas + Temperatura_lokacija_mean + Temperatura_Krvavec_max + Vlaga_min + PM10 + Vlaga_max")      	# Formula 3




sel <- sample(1:nrow(pod), size=as.integer(nrow(pod)*0.7), replace=F)
learn <- pod[sel,]
test <- pod[-sel,]


library(randomForest)
library(adabag)
library(kernlab)



#
# Nakljucni gozd 
#

rf <- randomForest(formula, learn)
predicted <- predict(rf, test, type = "class")
CA(test$O3, predicted)




#
# Boosting
#

bm <- boosting(formula, learn)
predictions <- predict(bm, test)
predicted <- predictions$class
CA(test$O3, predicted)



#
# SVM
#

svm <- ksvm(formula, data = learn, kernel = "rbfdot")
predicted <- predict(svm, test, type = "response")
CA(test$O3, predicted)

