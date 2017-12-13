################################################
#	PREČNO PREVERJANJE ZA ATRIBUT PM10         #
################################################

source("mojefunkcije.R")
library(CORElearn)
library(ipred)
library(nnet)



pod <- read.table("noviPodatki.txt", sep=",", header=T)
pod$PM10 <- cut(pod$PM10, c(-Inf,35,Inf),labels=c("NIZKA","VISOKA"))


# Izbrane formule
formula <- as.formula("PM10  ~  .")			# Formula 1
# formula <- as.formula("PM10 ~ Temperatura_lokacija_min + Sunki_vetra_max + Padavine_mean + postaja + Temperatura_Krvavec_min + O3 + Sunki_vetra_mean + Temperatura_lokacija_mean ")		# Formula 2
# formula <- as.formula("PM10 ~ Temperatura_lokacija_max + Temperatura_lokacija_mean + Letni_cas + O3 + Temperatura_Krvavec_mean + Pritisk_max + Pritisk_mean + Glob_sevanje_max  + Vlaga_min + Glob_sevanje_mean")      	# Formula 3



#
# KNN
#


n <- nrow(pod)
k <- 1
bucket.id <- rep(1:k, length.out=n)
s <- sample(1:n, n, FALSE)
bucket.id <- bucket.id[s]

cv.dt <- vector()
for (i in 1:k)
{	
	print(paste("Processing fold", i))
	flush.console()

	sel <- bucket.id == i	
	learn <- pod[!sel,]
	test <- pod[sel,]
	observed <- pod[sel,]$O3

	modelKNN <- CoreModel(formula, learn, model="knn", kInNN = 15)
	predKNN <- predict(modelKNN, test, type="class")
	cv.dt[i] <- CA(observed, predKNN)
}

mean(cv.dt)




#
# Glasovanje
#

n <- nrow(pod)
k <- 15
bucket.id <- rep(1:k, length.out=n)
s <- sample(1:n, n, FALSE)
bucket.id <- bucket.id[s]

cv.dt <- vector()
for (i in 1:k)
{	
	print(paste("Processing fold", i))
	flush.console()

	sel <- bucket.id == i	
	learn <- pod[!sel,]
	test <- pod[sel,]
	observed <- pod[sel,]$PM10

	
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

	
	cv.dt[i] <- CA(observed, predicted)
}

mean(cv.dt)



#
# Bagging
#

n <- nrow(pod)
k <- 15
bucket.id <- rep(1:k, length.out=n)
s <- sample(1:n, n, FALSE)
bucket.id <- bucket.id[s]

cv.dt <- vector()
for (i in 1:k)
{	
	print(paste("Processing fold", i))
	flush.console()

	sel <- bucket.id == i	
	learn <- pod[!sel,]
	test <- pod[sel,]
	observed <- pod[sel,]$PM10

	bag <- bagging(formula, learn, nbagg=25)
	bag.pred <- predict(bag, test, type="class")
	
	cv.dt[i] <- CA(observed, bag.pred)
}

mean(cv.dt)


#
# UMETNE NEVRONSKE MREZE
#


norm.data <- scale.data(pod)
norm.learn <- norm.data[sel,]
norm.test <- norm.data[-sel,]

n <- nrow(pod)
k <- 15
bucket.id <- rep(1:k, length.out=n)
s <- sample(1:n, n, FALSE)
bucket.id <- bucket.id[s]

cv.dt <- vector()
for (i in 1:k)
{	
	print(paste("Processing fold", i))
	flush.console()

	sel <- bucket.id == i	
	learn <- norm.data[!sel,]
	test <- norm.data[sel,]
	observed <- norm.data$PM10

	nn <- nnet(formula, data = learn, size = 8, decay = 0.005, maxit = 10000)
	predicted <- predict(nn, norm.data, type = "class")	
	
	cv.dt[i] <- CA(observed, predicted)
}

mean(cv.dt)






################################################
#	PREČNO PREVERJANJE ZA ATRIBUT O3           #
################################################

pod <- read.table("noviPodatki.txt", sep=",", header=T)
pod$O3 <- cut(pod$O3, c(-Inf, 60, 120, 180, Inf), labels=c("NIZKA", "SREDNJA", "VISOKA", "EKSTREMNA"))


# Izbrane formule
formula <- as.formula("O3  ~  .")			# Formula 1
# formula <- as.formula("O3 ~ Temperatura_lokacija_max + Glob_sevanje_mean + Temperatura_Krvavec_mean + Pritisk_mean + Hitrost_vetra_max + Glob_sevanje_max + Temperatura_Krvavec_min + Padavine_sum")		# Formula 2
# formula <- as.formula("O3 ~ Glob_sevanje_mean + Glob_sevanje_max + Letni_cas + Temperatura_lokacija_mean + Temperatura_Krvavec_max + Vlaga_min + PM10 + Vlaga_max")      	# Formula 3


source("mojefunkcije.R")
library(randomForest)
library(adabag)
library(kernlab)



#
# Nakljucni gozd 
#

n <- nrow(pod)
k <- 15
bucket.id <- rep(1:k, length.out=n)
s <- sample(1:n, n, FALSE)
bucket.id <- bucket.id[s]

cv.dt <- vector()
for (i in 1:k)
{	
	print(paste("Processing fold", i))
	flush.console()

	sel <- bucket.id == i	
	learn <- pod[!sel,]
	test <- pod[sel,]
	observed <- pod[sel,]$O3

	rf <- randomForest(formula, learn)
	predicted <- predict(rf, test, type = "class")
	
	cv.dt[i] <- CA(observed, predicted)
}

mean(cv.dt)



#
# Boosting
#

n <- nrow(pod)
k <- 15
bucket.id <- rep(1:k, length.out=n)
s <- sample(1:n, n, FALSE)
bucket.id <- bucket.id[s]

cv.dt <- vector()
for (i in 1:k)
{	
	print(paste("Processing fold", i))
	flush.console()

	sel <- bucket.id == i	
	learn <- pod[!sel,]
	test <- pod[sel,]
	observed <- pod[sel,]$O3

	bm <- boosting(formula, learn)
	predictions <- predict(bm, test)
	predicted <- predictions$class
	
	cv.dt[i] <- CA(observed, predicted)
}

mean(cv.dt)



#
# SVM
#

n <- nrow(pod)
k <- 15
bucket.id <- rep(1:k, length.out=n)
s <- sample(1:n, n, FALSE)
bucket.id <- bucket.id[s]

cv.dt <- vector()
for (i in 1:k)
{	
	print(paste("Processing fold", i))
	flush.console()

	sel <- bucket.id == i	
	learn <- pod[!sel,]
	test <- pod[sel,]
	observed <- pod[sel,]$O3

	svm <- ksvm(formula, data = learn, kernel = "rbfdot")
	predicted <- predict(svm, test, type = "response")
	
	
	cv.dt[i] <- CA(observed, predicted)
}

mean(cv.dt)

