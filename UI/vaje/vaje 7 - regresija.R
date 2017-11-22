########################################################################################
#
# Regresija
#
########################################################################################

# umetna podatkovna mnozica - ohlajanje tekocine v termovki
 
min <- c(0,5,8,11,15,18,22,25,30,34,38,42,45,50)
temp <- c(93.3,90.8,89.4,87.9,86.1,84.7,82.9,81.6,79.5,77.9,76.3,74.8,73.6,71.84973)
ohlajanje <- data.frame(min, temp)

# izrisimo graf
plot(ohlajanje)

# iz grafa lahko sklepamo, da gre za linearno funkcijo

# linearni model
lm1 <- lm(temp ~ min, ohlajanje)
lm1

# izrisimo linearni model
abline(lm1)




# situacija se spremeni, ko opravimo meritve v daljsem casovnem obdobju...

min <- c(0,50,100,150,200,250,300,350,400,450,500,550,600,650,700)
temp <- c(93.3,71.8,56.5,45.5,37.7,32.2,28.2,25.3,23.3,21.9,20.8,20.1,19.6,19.2,18.99864)
ohlajanje2 <- data.frame(min, temp)

plot(ohlajanje2)

# graf razkriva, da gre za eksponentno funkcijo

# eksponentne funkcije ne moremo dobro modelirati z linearnim modelom 
lm2 <- lm(temp ~ min, ohlajanje2)
lm2
abline(lm2)

# residuali modela - razlike med izmerjenimi in modeliranimi vrednostmi
lm2$res

hist(lm2$res)

plot(ohlajanje2$min, lm2$res)
abline(h = 0)

plot(lm2$res ~ lm2$fit)
abline(h = 0)




#
# lokalno utezena regresija
#

# pri gradnji modela upostevamo samo ucne primere iz okolice podane tocke
 
lurm <- loess(temp ~ min, data = ohlajanje2, span = 0.5, degree = 1)
plot(ohlajanje2)
lines(ohlajanje2$min,lurm$fit, col = "red")

lurm2 <- loess(temp ~ min, data = ohlajanje2, span = 0.3, degree = 1)
lines(ohlajanje2$min,lurm2$fit, col = "blue")

lurm3 <- loess(temp ~ min, data = ohlajanje2, span = 0.5, degree = 2)
lines(ohlajanje2$min,lurm3$fit, col = "green")




# izris residualov modela lurm3
plot(ohlajanje2$min, lurm3$res)
abline(h=0)

# model lahko uporabimo za napovedovanje temperature pri novih tockah
newdata = c(11,12,14.5,123.4,156,320)
predict(lurm3, newdata)



####################################################################
#
# Mere za ocenjevanje ucenja v regresiji
#
####################################################################

mae <- function(observed, predicted)
{
	mean(abs(observed - predicted))
}

rmae <- function(observed, predicted, mean.val) 
{  
	sum(abs(observed - predicted)) / sum(abs(observed - mean.val))
}

mse <- function(observed, predicted)
{
	mean((observed - predicted)^2)
}

rmse <- function(observed, predicted, mean.val) 
{  
	sum((observed - predicted)^2)/sum((observed - mean.val)^2)
}




##########################################################################
#
# Pregled modelov za regresijo
#
##########################################################################

# ucna mnozica
ucna <- read.table("AlgaeLearn.txt", header = T)
summary(ucna)

# neodvisna testna mnozica
testna <- read.table("AlgaeTest.txt", header = T)
observed <- testna$a1




#
# linearni model
#

lm.model <- lm(a1 ~ ., data = ucna)
lm.model


predicted <- predict(lm.model, testna)
mae(observed, predicted)
rmae(observed, predicted, mean(ucna$a1))




#
# regresijsko drevo
#

library(rpart)

rt.model <- rpart(a1 ~ ., ucna)
predicted <- predict(rt.model, testna)
rmae(observed, predicted, mean(ucna$a1))

plot(rt.model);text(rt.model, pretty = 0)


# nastavitve za gradnjo drevesa
rpart.control()

# zgradimo drevo z drugimi parametri
rt <- rpart(a1 ~ ., ucna, minsplit = 100)
plot(rt);text(rt, pretty = 0)

# parameter cp kontrolira rezanje drevesa
rt.model <- rpart(a1 ~ ., ucna, cp = 0)
plot(rt.model);text(rt.model, pretty = 0)


# izpisemo ocenjene napake drevesa za razlicne vrednosti parametra cp
printcp(rt.model)

# drevo porezemo z ustrezno vrednostjo cp, pri kateri je bila minimalna napaka
rt.model2 <- prune(rt.model, cp = 0.02)
plot(rt.model2);text(rt.model2, pretty = 0)
predicted <- predict(rt.model2, testna)
rmae(observed, predicted, mean(ucna$a1))



# regresijska drevesa lahko gradimo tudi s pomocjo knjiznice CORElearn
library(CORElearn)

# modelTypeReg
# type: integer, default value: 5, value range: 1, 8 
# type of models used in regression tree leaves (1=mean predicted value, 2=median predicted value, 3=linear by MSE, 4=linear by MDL, 5=linear as in M5, 6=kNN, 7=Gaussian kernel regression, 8=locally weighted linear regression).

rt.core <- CoreModel(a1 ~ ., data=ucna, model="regTree", modelTypeReg = 1)
plot(rt.core, ucna)
predicted <- predict(rt.core, testna)
rmae(observed, predicted, mean(ucna$a1))

modelEval(rt.core, testna$a1, predicted)



# preveliko drevo se prevec prilagodi podatkom in slabse napoveduje odvisno spremeljivko
rt.core2 <- CoreModel(a1 ~ ., data=ucna, model="regTree",  modelTypeReg = 1, minNodeWeightTree = 1, selectedPrunerReg = 0)
plot(rt.core2, ucna)
predicted <- predict(rt.core2, testna)
rmae(observed, predicted, mean(ucna$a1))

# drevo z linearnim modelom v listih se lahko prevec prilagodi ucnim primerom
rt.core3 <- CoreModel(a1 ~ ., data=ucna, model="regTree",  modelTypeReg = 3, selectedPrunerReg = 2)
plot(rt.core3, ucna)
predicted <- predict(rt.core3, testna)
rmae(observed, predicted, mean(ucna$a1))

# model se prilega ucnim podatkom, ker ima prevec parametrov.
# rezultat lahko izboljsamo tako, da poenostavimo model (npr. uporabimo manj atributov)

rt.core4 <- CoreModel(a1 ~ PO4 + size + mxPH, data=ucna, model="regTree", modelTypeReg = 3)
plot(rt.core4, testna)
predicted <- predict(rt.core4, testna)
rmae(observed, predicted, mean(ucna$a1))




#
# nakljucni gozd
#

library(randomForest)

rf.model <- randomForest(a1 ~ ., ucna)
predicted <- predict(rf.model, testna)
rmae(observed, predicted, mean(ucna$a1))




#
# svm
#

library(e1071)

svm.model <- svm(a1 ~ ., ucna)
predicted <- predict(svm.model, testna)
rmae(observed, predicted, mean(ucna$a1))




#
# k-najbližjih sosedov
#

library(kknn)

knn.model <- kknn(a1 ~ ., ucna, testna, k = 5)
predicted <- fitted(knn.model)
rmae(observed, predicted, mean(ucna$a1))




#
# nevronska mreža
#

library(nnet)

#
# pomembno!!! 
# za regresijo je potrebno nastaviti linout = T

# zaradi nakljucne izbire zacetnih utezi bo vsakic nekoliko drugacen rezultat
# zato je dobro, da veckrat naucimo mrezo in zadrzimo model, ki se je najboljse obnesel

#set.seed(6789)
nn.model <- nnet(a1 ~ ., ucna, size = 5, decay = 0.0001, maxit = 10000, linout = T)
predicted <- predict(nn.model, testna)
rmae(observed, predicted, mean(ucna$a1))




#######################################################################################
#
# Izbira podmnozice atributov
#
#######################################################################################

#
# ocena kvalitete atributov pri regresijskih problemih
#
 
sort(attrEval(a1 ~ ., ucna, "MSEofMean"), decreasing = TRUE)
sort(attrEval(a1 ~ ., ucna, "RReliefFexpRank"), decreasing = TRUE)


# model lahko dodatno izboljsamo z izbiro ustrezne podmnozice atributov

rt.core <- CoreModel(a1 ~ ., data=ucna, model="regTree", selectionEstimatorReg="MSEofMean")
plot(rt.core, ucna)
predicted <- predict(rt.core, testna)
rmae(observed, predicted, mean(ucna$a1))


rt.core2 <- CoreModel(a1 ~ Cl + PO4 + oPO4 + NH4 + Chla + NO3, data=ucna, model="regTree", selectionEstimatorReg="MSEofMean")
plot(rt.core2, ucna)
predicted <- predict(rt.core2, testna)
rmae(observed, predicted, mean(ucna$a1))


# pri izbiri podmnozice atributov si lahko pomagamo z wrapper metodo
source("wrapperReg.R")
wrapperReg(ucna, "a1", folds=10, selectionEstimatorReg="MSEofMean")
